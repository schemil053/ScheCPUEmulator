package de.emilschlampp.scheCPU.dissassembler;

import de.emilschlampp.scheCPU.emulator.Instruction;
import de.emilschlampp.scheCPU.util.FolderIOUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static de.emilschlampp.scheCPU.util.StaticValues.*;

public class Decompiler {
    private final Instruction[] instructions;

    public Decompiler(byte[] program) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(program);

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

    public Decompiler(Instruction[] program) {
        this.instructions = program;
    }

    public String decompile() {
        String pr = "";

        for (Instruction instruction : instructions) {
            pr+="\n"+decompileInstruction(instruction);
        }

        if(!pr.isEmpty()) {
            pr = pr.substring(1);
        }

        return pr;
    }

    public String decompileInstruction(int index) {
        return decompileInstruction(instructions[index]);
    }

    public String decompileInstructionSave(int index) {
        if(index >= instructions.length || index < 0) {
            return null;
        }
        return decompileInstruction(index);
    }

    public String decompileInstruction(Instruction instruction) {
        String line = "";

        if(instruction.getOpCode() == JMP_OPCODE) {
            line = "JMP "+instruction.getAddress();
        }
        if(instruction.getOpCode() == LOAD_OPCODE) {
            line = "LOAD "+fromRegID(instruction.getRegisterID())+" "+instruction.getValue();
        }
        if(instruction.getOpCode() == LOADMEM_OPCODE) {
            line = "LOADMEM "+fromRegID(instruction.getRegisterID())+" "+instruction.getAddress();
        }
        if(instruction.getOpCode() == STORE_OPCODE) {
            line = "STORE "+fromRegID(instruction.getRegisterID())+" "+instruction.getAddress();
        }
        if(instruction.getOpCode() == STOREREG_OPCODE) {
            line = "STOREREG "+instruction.getAddress()+" "+fromRegID(instruction.getRegisterID());
        }
        if(instruction.getOpCode() == STOREREGM_OPCODE) {
            line = "STOREREGM "+instruction.getAddress()+" "+fromRegID(instruction.getRegisterID());
        }
        if(instruction.getOpCode() == LOADREGM_OPCODE) {
            line = "STOREREGM "+fromRegID(instruction.getRegisterID())+ " " +instruction.getAddress();
        }
        if(instruction.getOpCode() == CMPM_OPCODE) {
            line = "CMPM "+fromRegID(instruction.getRegisterID())+" "+instruction.getAddress();
        }
        if(instruction.getOpCode() == CMPMEM_OPCODE) {
            line = "CMPMEM "+instruction.getAddress()+" "+instruction.getAddressS();
        }
        if(instruction.getOpCode() == CMPR_OPCODE) {
            line = "CMPR "+fromRegID(instruction.getRegisterID())+" "+fromRegID(instruction.getRegisterIDS());
        }
        if(instruction.getOpCode() == CMPMC_OPCODE) {
            line = "CMPMC "+fromRegID(instruction.getRegisterID());
        }
        if(instruction.getOpCode() == STOREMEM_OPCODE) {
            line = "STOREMEM "+instruction.getAddress()+" "+instruction.getValue();
        }
        if(instruction.getOpCode() == CJMP_OPCODE) {
            line = "CJMP "+instruction.getAddress();
        }
        if(instruction.getOpCode() == ADD_OPCODE) {
            line = "ADD "+fromRegID(instruction.getRegisterID())+" "+instruction.getValue();
        }
        if(instruction.getOpCode() == ADDM_OPCODE) {
            line = "ADDM "+instruction.getAddress()+" "+instruction.getValue();
        }
        if(instruction.getOpCode() == ADDR_OPCODE) {
            line = "ADDR "+fromRegID(instruction.getRegisterID())+" "+fromRegID(instruction.getRegisterIDS());
        }
        if(instruction.getOpCode() == COPYR_OPCODE) {
            line = "COPYR "+fromRegID(instruction.getRegisterID())+" "+fromRegID(instruction.getRegisterIDS());
        }
        if(instruction.getOpCode() == SUB_OPCODE) {
            line = "SUB "+fromRegID(instruction.getRegisterID())+" "+instruction.getValue();
        }
        if(instruction.getOpCode() == SUBM_OPCODE) {
            line = "SUBM "+instruction.getAddress()+" "+instruction.getValue();
        }
        if(instruction.getOpCode() == MUL_OPCODE) {
            line = "MUL "+fromRegID(instruction.getRegisterID())+" "+instruction.getValue();
        }
        if(instruction.getOpCode() == MULM_OPCODE) {
            line = "MULM "+instruction.getAddress()+" "+instruction.getValue();
        }
        if(instruction.getOpCode() == DIV_OPCODE) {
            line = "DIV "+fromRegID(instruction.getRegisterID())+" "+instruction.getValue();
        }
        if(instruction.getOpCode() == DIVM_OPCODE) {
            line = "DIVM "+instruction.getAddress()+" "+instruction.getValue();
        }
        if(instruction.getOpCode() == OUTW_OPCODE) {
            line = "OUTW "+instruction.getPort()+" "+instruction.getValue();
        }
        if(instruction.getOpCode() == OUTWM_OPCODE) {
            line = "OUTWM "+instruction.getPort()+" "+instruction.getAddress();
        }
        if(instruction.getOpCode() == OUTWDM_OPCODE) {
            line = "OUTWDM "+instruction.getPort()+" "+instruction.getAddress();
        }
        if(instruction.getOpCode() == OUTWR_OPCODE) {
            line = "OUTWR "+instruction.getPort()+" "+fromRegID(instruction.getRegisterID());
        }
        if(instruction.getOpCode() == INWM_OPCODE) {
            line = "INWM "+instruction.getPort()+" "+instruction.getAddress();
        }
        if(instruction.getOpCode() == INWDM_OPCODE) {
            line = "INWDM "+instruction.getPort()+" "+instruction.getAddress();
        }
        if(instruction.getOpCode() == INWR_OPCODE) {
            line = "INWR "+instruction.getPort()+" "+fromRegID(instruction.getRegisterID());
        }
        if(instruction.getOpCode() == CNJMP_OPCODE) {
            line = "CNJMP "+instruction.getAddress();
        }
        if(instruction.getOpCode() == CZJMP_OPCODE) {
            line = "CZJMP "+instruction.getAddress();
        }
        if(instruction.getOpCode() == BJMP_OPCODE) {
            line = "BJMP";
        }
        if(instruction.getOpCode() == MJMP_OPCODE) {
            line = "MJMP "+instruction.getAddress();
        }
        if(instruction.getOpCode() == CMJMP_OPCODE) {
            line = "CMJMP "+instruction.getAddress();
        }
        if(instruction.getOpCode() == CNMJMP_OPCODE) {
            line = "CNMJMP "+instruction.getAddress();
        }
        if(instruction.getOpCode() == CZMJMP_OPCODE) {
            line = "CZMJMP "+instruction.getAddress();
        }
        if(instruction.getOpCode() == ADDMM_OPCODE) {
            line = "ADDMM "+instruction.getAddress()+" "+instruction.getAddressS();
        }
        if(instruction.getOpCode() == SUBMM_OPCODE) {
            line = "SUBMM "+instruction.getAddress()+" "+instruction.getAddressS();
        }
        if(instruction.getOpCode() == DIVMM_OPCODE) {
            line = "DIVMM "+instruction.getAddress()+" "+instruction.getAddressS();
        }
        if(instruction.getOpCode() == MULMM_OPCODE) {
            line = "MULMM "+instruction.getAddress()+" "+instruction.getAddressS();
        }
        if(instruction.getOpCode() == OUTWMP_OPCODE) {
            line = "OUTWMP "+instruction.getAddress()+" "+instruction.getAddressS();
        }
        return line;
    }

    public Instruction[] getInstructions() {
        return instructions;
    }

    private String fromRegID(int v) {
        switch (v) {
            case REGID_A:
                return "A";
            case REGID_B:
                return "B";
            case REGID_C:
                return "C";
            case REGID_D:
                return "D";
            case REGID_BOOLVAL:
                return "BOOL";
        }
        throw new RuntimeException("err");
    }
}
