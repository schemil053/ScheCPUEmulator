package de.emilschlampp.scheCPU.emulator;

import de.emilschlampp.scheCPU.compile.Compiler;
import de.emilschlampp.scheCPU.dissassembler.Decompiler;
import de.emilschlampp.scheCPU.util.EmulatorSandboxRestrictions;
import de.emilschlampp.scheCPU.util.StaticValues;
import de.emilschlampp.scheCPU.util.FolderIOUtil;

import static de.emilschlampp.scheCPU.util.StaticValues.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ProcessorEmulator {
    private final int[] memory;
    private final int[] io;
    private final Instruction[] instructions;
    private final int[] register;
    private int jmp = 0;
    private int lastStoreVal = 0;
    private int lastJMP = 0;
    private EmulatorSandboxRestrictions restrictions = new EmulatorSandboxRestrictions();

    public ProcessorEmulator(int memorySize, int ioSize, byte[] program) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(program);

        this.memory = new int[memorySize];
        this.io = new int[ioSize + 128];
        this.register = new int[5];

        try {
            int instructionCount = FolderIOUtil.readInt(inputStream);

            instructions = new Instruction[instructionCount];

            for (int i = 0; i < instructionCount; i++) {
                instructions[i] = Instruction.parse(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ProcessorEmulator(byte[] state) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(state);

        try {
            int version = FolderIOUtil.readInt(inputStream);

            if(version == 1_00) {
                this.jmp = FolderIOUtil.readInt(inputStream);
                this.lastStoreVal = FolderIOUtil.readInt(inputStream);

                this.register = new int[FolderIOUtil.readInt(inputStream)];
                for (int i = 0; i < this.register.length; i++) {
                    this.register[i] = FolderIOUtil.readInt(inputStream);
                }

                this.memory = new int[FolderIOUtil.readInt(inputStream)];
                for (int i = 0; i < this.memory.length; i++) {
                    this.memory[i] = FolderIOUtil.readInt(inputStream);
                }

                this.io = new int[FolderIOUtil.readInt(inputStream)];
                for (int i = 0; i < this.io.length; i++) {
                    this.io[i] = FolderIOUtil.readInt(inputStream);
                }

                this.lastJMP = FolderIOUtil.readInt(inputStream);

                this.instructions = new Decompiler(FolderIOUtil.readByteArray(inputStream)).getInstructions();
            } else {
                throw new IllegalStateException("unsupported CPU version!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] saveState() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            FolderIOUtil.writeInt(outputStream, SERIAL_VERSION);

            FolderIOUtil.writeInt(outputStream, jmp);
            FolderIOUtil.writeInt(outputStream, lastStoreVal);

            FolderIOUtil.writeInt(outputStream, register.length);
            for (int j : register) {
                FolderIOUtil.writeInt(outputStream, j);
            }

            FolderIOUtil.writeInt(outputStream, memory.length);
            for (int j : memory) {
                FolderIOUtil.writeInt(outputStream, j);
            }

            FolderIOUtil.writeInt(outputStream, io.length);
            for (int j : io) {
                FolderIOUtil.writeInt(outputStream, j);
            }

            FolderIOUtil.writeInt(outputStream, lastJMP);

            FolderIOUtil.writeByteArray(outputStream, new Compiler(new Decompiler(instructions).decompile()).compile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return outputStream.toByteArray();
    }

    public boolean canExecute() {
        return jmp < instructions.length;
    }

    public void execute() {
        if (!canExecute()) {
            return;
        }

        if (io[3] == 1) {
            if (io[4] > 0) {
                io[4]--;
                return;
            }
        }

        int lastJMPBefore = lastJMP;
        io[5] = lastJMPBefore;

        int fjmp = jmp;

        Instruction instruction = instructions[fjmp];

        switch (instruction.getOpCode()) {
            case JMP_OPCODE:
                lastJMP = jmp + 1;
                jmp = instruction.getAddress();
                break;
            case LOAD_OPCODE:
                register[instruction.getRegisterID()] = instruction.getValue();
                jmp++;
                break;
            case LOADMEM_OPCODE:
            case STOREREG_OPCODE:
                register[instruction.getRegisterID()] = memory[instruction.getAddress()];
                jmp++;
                break;
            case STORE_OPCODE:
                memory[instruction.getAddress()] = register[instruction.getRegisterID()];
                lastStoreVal = instruction.getAddress();
                jmp++;
                break;
            case CMPM_OPCODE:
                register[REGID_BOOLVAL] = Integer.compare(register[instruction.getRegisterID()], memory[instruction.getAddress()]);
                jmp++;
                break;
            case CMPMEM_OPCODE:
                register[REGID_BOOLVAL] = Integer.compare(memory[instruction.getAddress()], memory[instruction.getAddressS()]);
                jmp++;
                break;
            case CMPR_OPCODE:
                register[REGID_BOOLVAL] = Integer.compare(register[instruction.getRegisterID()], register[instruction.getRegisterIDS()]);
                jmp++;
                break;
            case CMPMC_OPCODE:
                register[REGID_BOOLVAL] = Integer.compare(register[instruction.getRegisterID()], memory[lastStoreVal]);
                jmp++;
                break;
            case STOREMEM_OPCODE:
                memory[instruction.getAddress()] = instruction.getValue();
                jmp++;
                break;
            case CJMP_OPCODE:
                if (register[REGID_BOOLVAL] > 0) {
                    lastJMP = jmp + 1;
                    jmp = instruction.getAddress();
                } else {
                    jmp++;
                }
                break;
            case CZJMP_OPCODE:
                if (register[REGID_BOOLVAL] == 0) {
                    lastJMP = jmp + 1;
                    jmp = instruction.getAddress();
                } else {
                    jmp++;
                }
                break;
            case CNJMP_OPCODE:
                if (register[REGID_BOOLVAL] < 0) {
                    lastJMP = jmp + 1;
                    jmp = instruction.getAddress();
                } else {
                    jmp++;
                }
                break;
            case BJMP_OPCODE:
                jmp = lastJMP;
                break;
            case CBJMP_OPCODE:
                if (register[REGID_BOOLVAL] > 0) {
                    int newLastJMP = jmp + 1;
                    jmp = lastJMP;
                    lastJMP = newLastJMP;
                } else {
                    jmp++;
                }
                break;
            case CZBJMP_OPCODE:
                if (register[REGID_BOOLVAL] == 0) {
                    int newLastJMP = jmp + 1;
                    jmp = lastJMP;
                    lastJMP = newLastJMP;
                } else {
                    jmp++;
                }
                break;
            case CNBJMP_OPCODE:
                if (register[REGID_BOOLVAL] < 0) {
                    int newLastJMP = jmp + 1;
                    jmp = lastJMP;
                    lastJMP = newLastJMP;
                } else {
                    jmp++;
                }
                break;
            case ADD_OPCODE:
                register[instruction.getRegisterID()] += instruction.getValue();
                jmp++;
                break;
            case ADDR_OPCODE:
                register[instruction.getRegisterID()] += register[instruction.getRegisterIDS()];
                jmp++;
                break;
            case COPYR_OPCODE:
                register[instruction.getRegisterID()] = register[instruction.getRegisterIDS()];
                jmp++;
                break;
            case ADDM_OPCODE:
                memory[instruction.getAddress()] += instruction.getValue();
                jmp++;
                break;
            case SUB_OPCODE:
                register[instruction.getRegisterID()] -= instruction.getValue();
                jmp++;
                break;
            case SUBM_OPCODE:
                memory[instruction.getAddress()] -= instruction.getValue();
                jmp++;
                break;
            case MUL_OPCODE:
                register[instruction.getRegisterID()] *= instruction.getValue();
                jmp++;
                break;
            case MULM_OPCODE:
                memory[instruction.getAddress()] *= instruction.getValue();
                jmp++;
                break;
            case DIV_OPCODE:
                register[instruction.getRegisterID()] /= instruction.getValue();
                jmp++;
                break;
            case DIVM_OPCODE:
                memory[instruction.getAddress()] /= instruction.getValue();
                jmp++;
                break;
            case OUTW_OPCODE:
                io[instruction.getPort()] = instruction.getValue();
                jmp++;
                break;
            case OUTWM_OPCODE:
                io[instruction.getPort()] = memory[instruction.getAddress()];
                jmp++;
                break;
            case OUTWDM_OPCODE:
                io[instruction.getPort()] = memory[memory[instruction.getAddress()]];
                jmp++;
                break;
            case OUTWR_OPCODE:
                io[instruction.getPort()] = register[instruction.getRegisterID()];
                jmp++;
                break;
            case INWM_OPCODE:
                memory[instruction.getAddress()] = io[instruction.getPort()];
                jmp++;
                break;
            case INWDM_OPCODE:
                memory[memory[instruction.getAddress()]] = io[instruction.getPort()];
                jmp++;
                break;
            case INWR_OPCODE:
                register[instruction.getRegisterID()] = io[instruction.getPort()];
                jmp++;
                break;
            case MJMP_OPCODE:
                lastJMP = jmp + 1;
                jmp = memory[instruction.getAddress()];
                break;
            case CMJMP_OPCODE:
                if (register[REGID_BOOLVAL] > 0) {
                    lastJMP = jmp + 1;
                    jmp = memory[instruction.getAddress()];
                } else {
                    jmp++;
                }
                break;
            case CZMJMP_OPCODE:
                if (register[REGID_BOOLVAL] == 0) {
                    lastJMP = jmp + 1;
                    jmp = memory[instruction.getAddress()];
                } else {
                    jmp++;
                }
                break;
            case CNMJMP_OPCODE:
                if (register[REGID_BOOLVAL] < 0) {
                    lastJMP = jmp + 1;
                    jmp = memory[instruction.getAddress()];
                } else {
                    jmp++;
                }
                break;
            case ADDMM_OPCODE:
                memory[instruction.getAddress()]+=instruction.getAddressS();
                break;
            case SUBMM_OPCODE:
                memory[instruction.getAddress()]-=instruction.getAddressS();
                break;
            case DIVMM_OPCODE:
                memory[instruction.getAddress()]/=instruction.getAddressS();
                break;
            case MULMM_OPCODE:
                memory[instruction.getAddress()]*=instruction.getAddressS();
                break;
            case STOREREGM_OPCODE:
                register[instruction.getRegisterID()] = memory[memory[instruction.getAddress()]];
                break;
            default:
                throw new RuntimeException("not implemented: " + instruction.getOpCode());
        }

        if(restrictions.isAllowOutput()) {
            if (io[34] != 0) {
                System.out.print((char) io[34]);
                io[34] = 0;
            }
            if (io[1] != 0) {
                System.out.println("REG: " + Arrays.toString(register));
                System.out.println("MEM: " + Arrays.toString(memory));
                io[1] = 0;
            }
            if (io[2] != 0) {
                Arrays.stream(StaticValues.class.getFields()).forEach(f -> {
                    if (f.getName().endsWith("_OPCODE")) {
                        try {
                            if (f.get(null).equals(instruction.getOpCode())) {
                                System.out.println(f.getName() + " jp: " + fjmp + " -> j:" + jmp);
                            }
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }
        if (lastJMPBefore != io[5]) {
            lastJMP = io[5];
        }
        if(restrictions.isAllowReset()) {
            if (io[6] != 0) {
                jmp = 0;
                Arrays.fill(memory, 0);
                Arrays.fill(io, 0);
                Arrays.fill(register, 0);
            }
        }
    }

    public int[] getMemory() {
        return memory;
    }

    public int[] getIo() {
        return io;
    }

    public Instruction[] getInstructions() {
        return instructions;
    }

    public int getJmp() {
        return jmp;
    }

    public int[] getRegister() {
        return register;
    }

    public int getLastStoreVal() {
        return lastStoreVal;
    }

    public void setLastStoreVal(int lastStoreVal) {
        this.lastStoreVal = lastStoreVal;
    }

    public void setJmp(int jmp) {
        this.jmp = jmp;
    }

    public int getLastJMP() {
        return lastJMP;
    }

    public ProcessorEmulator setLastJMP(int lastJMP) {
        this.lastJMP = lastJMP;
        return this;
    }

    public EmulatorSandboxRestrictions getRestrictions() {
        return restrictions;
    }

    public ProcessorEmulator setRestrictions(EmulatorSandboxRestrictions restrictions) {
        this.restrictions = restrictions;
        return this;
    }
}
