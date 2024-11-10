package de.emilschlampp.scheCPU.emulator;

import de.emilschlampp.scheCPU.util.FolderIOUtil;

import java.io.IOException;
import java.io.InputStream;

import static de.emilschlampp.scheCPU.util.StaticValues.*;

public class Instruction {
    private int opCode;
    private int address;
    private int addressS;
    private int port;
    private int value;
    private int registerID;
    private int registerIDS;

    public static Instruction parse(InputStream inputStream) throws IOException {
        Instruction instruction = new Instruction();

        instruction.opCode = inputStream.read();

        if(instruction.opCode == JMP_OPCODE) {
            instruction.address = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == LOAD_OPCODE) {
            instruction.registerID = inputStream.read();
            instruction.value = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == LOADMEM_OPCODE) {
            instruction.registerID = inputStream.read();
            instruction.address = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == STORE_OPCODE) {
            instruction.registerID = inputStream.read();
            instruction.address = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == STOREREG_OPCODE) {
            instruction.address = FolderIOUtil.readInt(inputStream);
            instruction.registerID = inputStream.read();
        }
        if(instruction.opCode == CMPM_OPCODE) {
            instruction.registerID = inputStream.read();
            instruction.address = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == CMPR_OPCODE) {
            instruction.registerID = inputStream.read();
            instruction.registerIDS = inputStream.read();
        }
        if(instruction.opCode == CMPMC_OPCODE) {
            instruction.registerID = inputStream.read();
        }
        if(instruction.opCode == STOREMEM_OPCODE) {
            instruction.address = FolderIOUtil.readInt(inputStream);
            instruction.value = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == CJMP_OPCODE) {
            instruction.address = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == CNJMP_OPCODE) {
            instruction.address = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == CZJMP_OPCODE) {
            instruction.address = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == ADD_OPCODE) {
            instruction.registerID = inputStream.read();
            instruction.value = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == ADDM_OPCODE) {
            instruction.address = FolderIOUtil.readInt(inputStream);
            instruction.value = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == SUB_OPCODE) {
            instruction.registerID = inputStream.read();
            instruction.value = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == SUBM_OPCODE) {
            instruction.address = FolderIOUtil.readInt(inputStream);
            instruction.value = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == MUL_OPCODE) {
            instruction.registerID = inputStream.read();
            instruction.value = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == MULM_OPCODE) {
            instruction.address = FolderIOUtil.readInt(inputStream);
            instruction.value = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == DIV_OPCODE) {
            instruction.registerID = inputStream.read();
            instruction.value = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == DIVM_OPCODE) {
            instruction.address = FolderIOUtil.readInt(inputStream);
            instruction.value = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == OUTW_OPCODE) {
            instruction.port = FolderIOUtil.readInt(inputStream);
            instruction.value = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == OUTWM_OPCODE) {
            instruction.port = FolderIOUtil.readInt(inputStream);
            instruction.address = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == OUTWDM_OPCODE) {
            instruction.port = FolderIOUtil.readInt(inputStream);
            instruction.address = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == OUTWR_OPCODE) {
            instruction.port = FolderIOUtil.readInt(inputStream);
            instruction.registerID = inputStream.read();
        }
        if(instruction.opCode == INWM_OPCODE) {
            instruction.port = FolderIOUtil.readInt(inputStream);
            instruction.address = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == INWDM_OPCODE) {
            instruction.port = FolderIOUtil.readInt(inputStream);
            instruction.address = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == INWR_OPCODE) {
            instruction.port = FolderIOUtil.readInt(inputStream);
            instruction.registerID = inputStream.read();
        }
        if(instruction.opCode == CMPMEM_OPCODE) {
            instruction.address = FolderIOUtil.readInt(inputStream);
            instruction.addressS = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == ADDR_OPCODE) {
            instruction.address = FolderIOUtil.readInt(inputStream);
            instruction.addressS = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == COPYR_OPCODE) {
            instruction.address = FolderIOUtil.readInt(inputStream);
            instruction.addressS = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == MJMP_OPCODE) {
            instruction.address = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == CMJMP_OPCODE) {
            instruction.address = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == CNMJMP_OPCODE) {
            instruction.address = FolderIOUtil.readInt(inputStream);
        }
        if(instruction.opCode == CZMJMP_OPCODE) {
            instruction.address = FolderIOUtil.readInt(inputStream);
        }

        return instruction;
    }

    public int getOpCode() {
        return opCode;
    }

    public void setOpCode(int opCode) {
        this.opCode = opCode;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getRegisterID() {
        return registerID;
    }

    public void setRegisterID(int registerID) {
        this.registerID = registerID;
    }

    public int getRegisterIDS() {
        return registerIDS;
    }

    public void setRegisterIDS(int registerIDS) {
        this.registerIDS = registerIDS;
    }

    public int getAddressS() {
        return addressS;
    }

    public void setAddressS(int addressS) {
        this.addressS = addressS;
    }
}
