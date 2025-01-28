package de.emilschlampp.scheCPU.compile;

import de.emilschlampp.scheCPU.util.FolderIOUtil;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static de.emilschlampp.scheCPU.util.StaticValues.*;

public class Compiler {
    private String program;

    public Compiler(String program) {
        this.program = program;
    }

    public byte[] compile() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int debugLine = 0;
        try {
            String[] split = program.split("\n", -1);

            Map<String, Integer> functionsInstructionMap = new HashMap<>();
            int fnC = 0;
            for (String s : split) {
                debugLine++;
                s = splitComment(s);
                if (s.replace(" ", "").isEmpty() || s.startsWith(";") || s.startsWith("#") || s.startsWith("//")) {
                    continue;
                }
                String[] cmd = s.split(" ", -1);

                if (cmd.length == 0) {
                    continue;
                }

                if (cmd[0].equals("FUNC")) {
                    if (cmd.length == 2) {
                        if (isInt(cmd[1])) {
                            throw new RuntimeException("Invalid func name: " + cmd[1] + " Line: " + (fnC + 1));
                        }
                        functionsInstructionMap.put(cmd[1], fnC);
                    }
                } else if (cmd[0].equals("LOADSTRM")) {
                    String val = "";
                    for (int i = 2; i < cmd.length; i++) {
                        val += (" " + cmd[i]);
                    }
                    if (!val.isEmpty()) {
                        val = val.substring(1);
                    }
                    val = val.replace("\\n", "\n");
                    for (char c : val.toCharArray()) {
                        fnC++;
                    }
                } else if (cmd[0].equals("LOADSTRMC")) {
                    String val = "";
                    for (int i = 2; i < cmd.length; i++) {
                        val += (" " + cmd[i]);
                    }
                    if (!val.isEmpty()) {
                        val = val.substring(1);
                    }
                    val = val.replace("\\n", "\n");
                    fnC++;
                    for (char c : val.toCharArray()) {
                        fnC++;
                    }
                } else {
                    fnC++;
                }
            }
            FolderIOUtil.writeInt(outputStream, fnC);

            debugLine = 0;
            for (String s : split) {
                debugLine++;
                s = splitComment(s);
                if (s.replace(" ", "").isEmpty() || s.startsWith(";") || s.startsWith("#") || s.startsWith("//")) {
                    continue;
                }
                String[] cmd = s.split(" ", -1);

                if (cmd.length == 0) {
                    continue;
                }

                cmd[0] = cmd[0].toUpperCase(Locale.ROOT);
                if (cmd[0].equals("JMP")) { //Jump to Adress, or use function defined by FUNC
                    if (cmd.length == 2) {
                        int address;
                        if (isInt(cmd[1])) {
                            address = parseInt(cmd[1]);
                        } else {
                            address = functionsInstructionMap.getOrDefault(cmd[1], -1);
                        }
                        if (address == -1) {
                            throw new RuntimeException("invalid address: " + cmd[1]);
                        }
                        outputStream.write(JMP_OPCODE);
                        FolderIOUtil.writeInt(outputStream, address);
                    } else {
                        throw new RuntimeException("jmp args");
                    }
                } else if (cmd[0].equals("LOAD")) { //Load value to Register
                    if (cmd.length == 3) {
                        outputStream.write(LOAD_OPCODE);
                        outputStream.write(toRegID(cmd[1]));
                        FolderIOUtil.writeInt(outputStream, parseInt(cmd[2])); //Value
                    } else {
                        throw new RuntimeException("err");
                    }
                } else if (cmd[0].equals("LOADMEM")) { //Load memory value to register
                    if (cmd.length == 3) {
                        outputStream.write(LOADMEM_OPCODE);
                        outputStream.write(toRegID(cmd[1])); //Register
                        FolderIOUtil.writeInt(outputStream, parseInt(cmd[2])); //memadress
                    } else {
                        throw new RuntimeException("err");
                    }
                } else if (cmd[0].equals("STORE")) { //Store register value to memory
                    if (cmd.length == 3) {
                        outputStream.write(STORE_OPCODE);
                        outputStream.write(toRegID(cmd[1])); //Register
                        FolderIOUtil.writeInt(outputStream, parseInt(cmd[2])); //memadress
                    } else {
                        throw new RuntimeException("err");
                    }
                } else if (cmd[0].equals("STOREREG")) { //Store memory value to register
                    if (cmd.length == 3) {
                        outputStream.write(STOREREG_OPCODE);
                        FolderIOUtil.writeInt(outputStream, parseInt(cmd[1])); //memadress
                        outputStream.write(toRegID(cmd[2])); //Register
                    } else {
                        throw new RuntimeException("err");
                    }
                } else if (cmd[0].equals("STOREREGM")) { //Store memory value to register
                    if (cmd.length == 3) {
                        outputStream.write(STOREREGM_OPCODE);
                        FolderIOUtil.writeInt(outputStream, parseInt(cmd[1])); //memadress
                        outputStream.write(toRegID(cmd[2])); //Register
                    } else {
                        throw new RuntimeException("err");
                    }
                } else if (cmd[0].equals("CMPM")) { //Compare mem Value with Register
                    if (cmd.length == 3) {
                        outputStream.write(CMPM_OPCODE);
                        outputStream.write(toRegID(cmd[1])); //Register
                        FolderIOUtil.writeInt(outputStream, parseInt(cmd[2])); //memadress
                    } else {
                        throw new RuntimeException("err");
                    }
                } else if (cmd[0].equals("CMPMC")) { //Current mem address load by STORE
                    if (cmd.length == 2) {
                        outputStream.write(CMPMC_OPCODE);
                        outputStream.write(toRegID(cmd[1])); //Register
                    } else {
                        throw new RuntimeException("err");
                    }
                } else if (cmd[0].equals("CMPR")) { //Current mem address load by STORE
                    if (cmd.length == 3) {
                        outputStream.write(CMPR_OPCODE);
                        outputStream.write(toRegID(cmd[1])); //Register 1
                        outputStream.write(toRegID(cmd[2])); //Register 2
                    } else {
                        throw new RuntimeException("err");
                    }
                } else if (cmd[0].equals("ADDR")) {
                    if (cmd.length == 3) {
                        outputStream.write(ADDR_OPCODE);
                        outputStream.write(toRegID(cmd[1])); //Register 1
                        outputStream.write(toRegID(cmd[2])); //Register 2
                    } else {
                        throw new RuntimeException("err");
                    }
                } else if (cmd[0].equals("COPYR")) {
                    if (cmd.length == 3) {
                        outputStream.write(COPYR_OPCODE);
                        outputStream.write(toRegID(cmd[1])); //Register 1
                        outputStream.write(toRegID(cmd[2])); //Register 2
                    } else {
                        throw new RuntimeException("err");
                    }
                } else if (cmd[0].equals("BJMP")) {
                    outputStream.write(BJMP_OPCODE);
                } else if (cmd[0].equals("CBJMP")) {
                    outputStream.write(CBJMP_OPCODE);
                } else if (cmd[0].equals("CNBJMP")) {
                    outputStream.write(CNBJMP_OPCODE);
                } else if (cmd[0].equals("CZBJMP")) {
                    outputStream.write(CZBJMP_OPCODE);
                } else if (cmd[0].equals("CMPMEM")) { //Compare mem address values
                    if (cmd.length == 3) {
                        outputStream.write(CMPMEM_OPCODE);
                        FolderIOUtil.writeInt(outputStream, parseInt(cmd[1])); //ADDR1
                        FolderIOUtil.writeInt(outputStream, parseInt(cmd[2])); //ADDR2
                    } else {
                        throw new RuntimeException("err");
                    }
                } else if (cmd[0].equals("STOREMEM")) { //Store Value in Memory
                    if (cmd.length == 3) {
                        outputStream.write(STOREMEM_OPCODE);
                        FolderIOUtil.writeInt(outputStream, parseInt(cmd[1])); //memadress
                        FolderIOUtil.writeInt(outputStream, parseInt(cmd[2])); //value
                    } else {
                        throw new RuntimeException("err");
                    }
                } else if (cmd[0].equals("CJMP")) { //Reads bool from boolstore, if it's positive JUMP
                    if (cmd.length == 2) {
                        int address;
                        if (isInt(cmd[1])) {
                            address = parseInt(cmd[1]);
                        } else {
                            address = functionsInstructionMap.getOrDefault(cmd[1], -1);
                        }
                        if (address == -1) {
                            throw new RuntimeException("err");
                        }
                        outputStream.write(CJMP_OPCODE);
                        FolderIOUtil.writeInt(outputStream, address);
                    } else {
                        throw new RuntimeException("err");
                    }
                } else if (cmd[0].equals("CNJMP")) { //Reads bool from boolstore, if it's positive JUMP
                    if (cmd.length == 2) {
                        int address;
                        if (isInt(cmd[1])) {
                            address = parseInt(cmd[1]);
                        } else {
                            address = functionsInstructionMap.getOrDefault(cmd[1], -1);
                        }
                        if (address == -1) {
                            throw new RuntimeException("err");
                        }
                        outputStream.write(CNJMP_OPCODE);
                        FolderIOUtil.writeInt(outputStream, address);
                    } else {
                        throw new RuntimeException("err");
                    }
                } else if (cmd[0].equals("CZJMP")) { //Reads bool from boolstore, if it's positive JUMP
                    if (cmd.length == 2) {
                        int address;
                        if (isInt(cmd[1])) {
                            address = parseInt(cmd[1]);
                        } else {
                            address = functionsInstructionMap.getOrDefault(cmd[1], -1);
                        }
                        if (address == -1) {
                            throw new RuntimeException("address invalid");
                        }
                        outputStream.write(CZJMP_OPCODE);
                        FolderIOUtil.writeInt(outputStream, address);
                    } else {
                        throw new RuntimeException("czjmp args");
                    }
                } else if (cmd[0].equals("ADD")) {
                    if (cmd.length == 3) {
                        outputStream.write(ADD_OPCODE);
                        outputStream.write(toRegID(cmd[1])); //Register
                        FolderIOUtil.writeInt(outputStream, parseInt(cmd[2])); //Value
                    } else {
                        throw new RuntimeException("add args");
                    }
                } else if (cmd[0].equals("ADDM")) {
                    outputStream.write(ADDM_OPCODE);
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[1])); //Memory Address
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[2])); //Value
                } else if (cmd[0].equals("SUB")) {
                    if (cmd.length == 3) {
                        outputStream.write(SUB_OPCODE);
                        outputStream.write(toRegID(cmd[1])); //Register
                        FolderIOUtil.writeInt(outputStream, parseInt(cmd[2])); //Value
                    } else {
                        throw new RuntimeException("sub args");
                    }
                } else if (cmd[0].equals("SUBM")) {
                    outputStream.write(SUBM_OPCODE);
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[1])); //Memory Address
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[2])); //VAl
                } else if (cmd[0].equals("MUL")) {
                    if (cmd.length == 3) {
                        outputStream.write(MUL_OPCODE);
                        outputStream.write(toRegID(cmd[1])); //Register
                        FolderIOUtil.writeInt(outputStream, parseInt(cmd[2])); //VAl
                    } else {
                        throw new RuntimeException("mul args");
                    }
                } else if (cmd[0].equals("MJMP")) {
                    outputStream.write(MJMP_OPCODE);
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[1])); //Memory Address
                } else if (cmd[0].equals("CMJMP")) {
                    outputStream.write(CMJMP_OPCODE);
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[1])); //Memory Address
                } else if (cmd[0].equals("CZMJMP")) {
                    outputStream.write(CZMJMP_OPCODE);
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[1])); //Memory Address
                } else if (cmd[0].equals("CNMJMP")) {
                    outputStream.write(CNMJMP_OPCODE);
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[1])); //Memory Address
                } else if (cmd[0].equals("MULM")) {
                    outputStream.write(MULM_OPCODE);
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[1])); //Memory
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[2])); //Value
                } else if (cmd[0].equals("DIV")) {
                    if (cmd.length == 3) {
                        outputStream.write(DIV_OPCODE);
                        outputStream.write(toRegID(cmd[1])); //Register
                        FolderIOUtil.writeInt(outputStream, parseInt(cmd[2])); //VAl
                    } else {
                        throw new RuntimeException("div args");
                    }
                } else if (cmd[0].equals("DIVM")) {
                    outputStream.write(DIVM_OPCODE);
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[1])); //Memory
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[2])); //Value
                } else if (cmd[0].equals("OUTW")) { //Out from Value
                    outputStream.write(OUTW_OPCODE);
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[1])); //Port
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[2])); //Value
                } else if (cmd[0].equals("OUTWM")) { //Out from Memory
                    outputStream.write(OUTWM_OPCODE);
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[1])); //Port
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[2])); //Memory Address
                } else if (cmd[0].equals("OUTWDM")) { //Out from Memory
                    outputStream.write(OUTWDM_OPCODE);
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[1])); //Port
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[2])); //Memory Address
                } else if (cmd[0].equals("OUTWR")) { //Out from Register
                    outputStream.write(OUTWR_OPCODE);
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[1])); //Port
                    outputStream.write(toRegID(cmd[2]));
                } else if (cmd[0].equals("INWM")) { //IN to Memory
                    outputStream.write(INWM_OPCODE);
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[1])); //Port
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[2])); //Memory Address
                } else if (cmd[0].equals("INWDM")) { //IN to Memory
                    outputStream.write(INWDM_OPCODE);
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[1])); //Port
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[2])); //Memory Address
                } else if (cmd[0].equals("INWR")) { //IN to Register
                    outputStream.write(INWR_OPCODE);
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[1])); //Port
                    outputStream.write(toRegID(cmd[2]));
                } else if (cmd[0].equals("ADDMM")) {
                    outputStream.write(ADDMM_OPCODE);
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[1])); //Addr1
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[2])); //Addr2
                } else if (cmd[0].equals("SUBMM")) {
                    outputStream.write(SUBMM_OPCODE);
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[1])); //Addr1
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[2])); //Addr2
                } else if (cmd[0].equals("DIVMM")) {
                    outputStream.write(DIVMM_OPCODE);
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[1])); //Addr1
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[2])); //Addr2
                } else if (cmd[0].equals("MULMM")) {
                    outputStream.write(MULMM_OPCODE);
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[1])); //Addr1
                    FolderIOUtil.writeInt(outputStream, parseInt(cmd[2])); //Addr2
                } else if (cmd[0].equals("LOADSTRM")) {
                    int address = parseInt(cmd[1]);
                    String val = "";
                    for (int i = 2; i < cmd.length; i++) {
                        val += (" " + cmd[i]);
                    }
                    if (!val.isEmpty()) {
                        val = val.substring(1);
                    }
                    val = val.replace("\\n", "\n");
                    int off = 0;
                    for (char c : val.toCharArray()) {
                        outputStream.write(STOREMEM_OPCODE);
                        FolderIOUtil.writeInt(outputStream, address + off); //memadress
                        FolderIOUtil.writeInt(outputStream, c); //value
                        off++;
                    }
                } else if (cmd[0].equals("LOADSTRMC")) {
                    int address = parseInt(cmd[1]);
                    String val = "";
                    for (int i = 2; i < cmd.length; i++) {
                        val += (" " + cmd[i]);
                    }
                    if (!val.isEmpty()) {
                        val = val.substring(1);
                    }
                    val = val.replace("\\n", "\n");
                    char[] ch = val.toCharArray();
                    int off = 0;
                    outputStream.write(STOREMEM_OPCODE);
                    FolderIOUtil.writeInt(outputStream, address + off); //memadress
                    FolderIOUtil.writeInt(outputStream, ch.length); //value
                    off++;
                    for (char c : ch) {
                        outputStream.write(STOREMEM_OPCODE);
                        FolderIOUtil.writeInt(outputStream, address + off); //memadress
                        FolderIOUtil.writeInt(outputStream, c); //value
                        off++;
                    }
                } else if(cmd[0].equals("OUTWFUNC")) {
                    int port = parseInt(cmd[1]);
                    int address = functionsInstructionMap.getOrDefault(cmd[2], -1);

                    if (address == -1) {
                        throw new RuntimeException("address invalid");
                    }
                    outputStream.write(OUTW_OPCODE);
                    FolderIOUtil.writeInt(outputStream, port);
                    FolderIOUtil.writeInt(outputStream, address);
                } else {
                    if (!cmd[0].equals("FUNC")) {
                        throw new RuntimeException("Invalid " + cmd[0] + " line: " + debugLine);
                    }
                }
            }
        } catch (Throwable throwable) {
            throw new RuntimeException("check line " + debugLine, throwable);
        }

        return outputStream.toByteArray();
    }

    private boolean isInt(String i) {
        try {
            Integer.parseInt(i);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    private int parseInt(String i) {
        if (!isInt(i)) {
            throw new RuntimeException("Expected Integer");
        }
        return Integer.parseInt(i);
    }

    private int toRegID(String v) {
        switch (v) {
            case "A":
                return REGID_A;
            case "B":
                return REGID_B;
            case "C":
                return REGID_C;
            case "D":
                return REGID_D;
            case "BOOL":
                return REGID_BOOLVAL;
        }
        throw new RuntimeException("err");
    }

    private String splitComment(String orig) {
        if (orig.isEmpty()) {
            return "";
        }
        String l = orig;
        try {
            if (l.contains(" ;")) {
                l = l.split(" ;", 2)[0];
            }
            if (l.contains(";")) {
                l = l.split(";", 2)[0];
            }
            if (l.contains(" #")) {
                l = l.split(" #", 2)[0];
            }
            if (l.contains("#")) {
                l = l.split("#", 2)[0];
            }
        } catch (Throwable throwable) {

        }
        return l;
    }
}