package de.emilschlampp.scheCPU.high;

import de.emilschlampp.scheCPU.compile.Compiler;
import de.emilschlampp.scheCPU.dissassembler.Decompiler;
import de.emilschlampp.scheCPU.high.processor.CompileContext;
import de.emilschlampp.scheCPU.high.processor.CompileProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static de.emilschlampp.scheCPU.util.StaticValues.*;

public class HighProgramCompiler {
    private final String program;
    private String output;
    private int currentLine;
    private Consumer<String> warningOutput = System.out::println;
    private CompileProcessor compileProcessor = null;

    public HighProgramCompiler(String program) {
        this.program = program;
    }

    public String getProgram() {
        return program;
    }

    public HighProgramCompiler compile() {
        currentLine = 0;
        String code = "; Auto-Compiled";

        Map<String, Integer> variableAddresses = new HashMap<>();
        //TODO 17.10.24: variableSizes -> muss bei reserve und bei var eingebaut werden!
        Map<String, String> options = new HashMap<>();

        List<String> protectedVariables = new ArrayList<>();
        List<String> protectedOptions = new ArrayList<>();
        List<String> methods = new ArrayList<>();

        List<Integer> ifStack = new ArrayList<>();

        int currentAddress = 25;
        int tmpFuncID = 0;
        String currentMethod = null;

        String toCompileProgram = program;

        CompileContext compileContext = new CompileContext(variableAddresses, options, methods, protectedVariables, protectedOptions, this::warn, this::error);

        if (this.compileProcessor != null) {
            this.compileProcessor.startCompile(compileContext);
            String d = this.compileProcessor.generatePreCompileHigh();
            if (d != null) {
                toCompileProgram = d + "\n" + toCompileProgram;
            }

            String a = this.compileProcessor.generatePreCompileSCHESEM();
            if (a != null) {
                code = a + "\n" + code;
            }

            String c = this.compileProcessor.generatePreCompileHighEnd();
            if (c != null) {
                toCompileProgram = c + "\n" + toCompileProgram;
            }
        }

        String[] spProgram = toCompileProgram.split("\n");

        for (String line : spProgram) {
            this.currentLine++;
            String[] split = line.split(" ");

            if (split.length == 0) {
                continue;
            }
            if (split[0].equals("protect")) {
                String type = split[1];
                String obj = split[2];

                if (type.equals("variable")) {
                    if (!protectedVariables.contains(obj)) {
                        protectedVariables.add(obj);
                    }
                } else if (type.equals("option")) {
                    if (!protectedOptions.contains(obj)) {
                        protectedOptions.add(obj);
                    }
                } else {
                    error("not found! available: variable, option");
                }
            } else if (split[0].equals("ret")) {
                if (currentMethod == null) {
                    error("can only be called inside a method!");
                }
                code = code + "\nBJMP";
            } else if (split[0].equals("opt")) {
                String optname = split[1];

                String val = "";
                for (int i = 2; i < split.length; i++) {
                    val += (" " + split[i]);
                }
                if (!val.isEmpty()) {
                    val = val.substring(1);
                }
                if (protectedOptions.contains(optname)) {
                    error("access violation!");
                }
                options.put(optname, val);
            } else if (split[0].equals("reserve")) {
                int address = 0;
                String varname = split[1];
                if (protectedVariables.contains(varname)) {
                    error("access violation!");
                }
                int size = Integer.parseInt(split[2]);
                if (variableAddresses.containsKey(varname)) {
                    error(varname + " is already reserved!");
                } else {
                    address = currentAddress + 1;
                    variableAddresses.put(varname, address);
                    currentAddress = currentAddress + size + 1;
                }
            } else if (split[0].equals("ifbool")) {
                String val = split[1];

                int f1 = tmpFuncID++;
                ifStack.add(f1);

                if (!variableAddresses.containsKey(val)) {
                    error("variable not found!");
                }

                int address = variableAddresses.get(val);

                code = code + "\n" +
                        "INWM 5 21\n" +
                        "STORE BOOL 1\n" + //Boolstore sichern
                        "LOADMEM BOOL " + address + "\n" +
                        "CJMP in_if_" + f1 + "\n" +
                        "LOADMEM BOOL 1\n" +
                        "JMP end_if_" + f1 + "\n" +
                        "FUNC in_if_" + f1 + "\n" +
                        "LOADMEM BOOL 1\n" +
                        "OUTWM 5 21";

            } else if (split[0].equals("endif")) {
                if (ifStack.isEmpty()) {
                    error("no if present!");
                }
                int stack = ifStack.remove(ifStack.size() - 1);
                code = code + "\n" +
                        "FUNC end_if_" + stack + "\n" +
                        "OUTWM 5 21";
            } else if (split[0].equals("var")) {
                String varname = split[1];

                if (protectedVariables.contains(varname)) {
                    error("access violation!");
                }

                String val = "";
                for (int i = 2; i < split.length; i++) {
                    val += (" " + split[i]);
                }
                if (!val.isEmpty()) {
                    val = val.substring(1);
                }

                boolean newA = false;
                int address = 0;
                if (variableAddresses.containsKey(varname)) {
                    address = variableAddresses.get(varname);
                } else {
                    warn("address not reserved. This variable will reserve it!");
                    address = currentAddress + 1;
                    variableAddresses.put(varname, address);
                    newA = true;
                }

                if (val.startsWith("\"")) {
                    String sp = val.substring(1, val.length() - 1);

                    code = code + "\nLOADSTRMC " + address + " " + sp;
                    if (newA) {
                        currentAddress = currentAddress + sp.length() + 1;
                    }
                } else if (val.equals("true") || val.equals("false")) {
                    code = code + "\nSTOREMEM " + address + " " + (Boolean.parseBoolean(val) ? 1 : 0);
                    if (newA) {
                        currentAddress++;
                    }
                } else {
                    code = code + "\nSTOREMEM " + address + " " + Integer.parseInt(val);
                    if (newA) {
                        currentAddress++;
                    }
                }
            } else if (split[0].equals("method")) {
                if (!ifStack.isEmpty()) {
                    error("double if!");
                }

                String name = split[1];

                if (methods.contains(name)) {
                    error("duplicate method: " + name);
                }

                if (currentMethod != null) {
                    error("We are inside a method!");
                }

                methods.add(name);

                currentMethod = name;
                code = code + "\n" +
                        "JMP ca_endmethod_" + currentMethod + "\n" +
                        "FUNC c_" + currentMethod;
            } else if (split[0].equals("endmethod")) {
                if (currentMethod == null) {
                    error("We are not inside a method!");
                }
                if (!ifStack.isEmpty()) {
                    error("end ifs first!");
                }

                code = code + "\n" +
                        "BJMP\n" +
                        "FUNC ca_endmethod_" + currentMethod;
                currentMethod = null;
            } else if (split[0].equals("callmethod")) {
                if (currentMethod != null) {
                    warn("stack overflow: current limit is 1");
                }
                String name = split[1];
                code = code + "\n" +
                        "JMP c_" + name;
            } else if (split[0].equals("putsamebool")) {
                String target = split[1];
                if (!variableAddresses.containsKey(target)) {
                    error("variable not found!");
                }

                String var1 = split[2];
                if (!variableAddresses.containsKey(var1)) {
                    error("variable not found!");
                }

                String var2 = split[3];
                if (!variableAddresses.containsKey(var2)) {
                    error("variable not found!");
                }

                if (protectedVariables.contains(target)) {
                    error("access violation!");
                }

                int f1 = tmpFuncID++;
                int f2 = tmpFuncID++;

                code = code + "\n" +
                        "STORE BOOL 7\n" +
                        "CMPMEM "+variableAddresses.get(var1)+" "+variableAddresses.get(var2)+"\n" +
                        "CZJMP start_"+f1+"\n" +
                        "JMP end_"+f2+"\n" +
                        "FUNC start_"+f1+"\n" +
                        "STOREMEM "+variableAddresses.get(target)+" 1\n" +
                        "FUNC end_"+f2+"\n" +
                        "LOADMEM BOOL 7"; //TODO: Fertig coden
            } else if (split[0].equals("print")) {
                String val = split[1];

                if (!variableAddresses.containsKey(val)) {
                    error("variable not found!");
                }

                int address = variableAddresses.get(val);

                int f1 = tmpFuncID++;
                int f2 = tmpFuncID++;
                int f3 = tmpFuncID++;

                code = code + "\n" +
                        "INWM 5 20\n" + // BJMP speichern
                        "STORE BOOL 1\n" + // Altes Register speichern
                        "STOREMEM 2 0\n" +
                        "LOADMEM A " + address + "\n" +
                        "STOREMEM 3 " + (address + 1) + "\n" + // +1 weil auf der Adresse ja die Anzahl der Zeichen liegt (und Adresse+1 = erstes Zeichen)
                        "FUNC tmp_" + f1 + "\n" +
                        "CMPM A 2\n" +
                        "CJMP printChar_" + f2 + "\n" +
                        "JMP end_" + f3 + "\n" +
                        "FUNC printChar_" + f2 + "\n" +
                        "OUTWDM " + options.getOrDefault("io-print-port", "34") + " 3\n" +
                        "ADDM 3 1\n" +
                        "ADDM 2 1\n" +
                        "JMP tmp_" + f1 + "\n" +
                        "FUNC end_" + f3 + "\n" +
                        "LOADMEM BOOL 1\n" +
                        "OUTWM 5 20"
                ;
            } else if(split[0].equals("out")) {
                String val = split[1];

                String portVal = split[2];

                if(!isInt(portVal)) {
                    error("invalid port!");
                }

                int port = parseInt(portVal);

                if (!variableAddresses.containsKey(val)) {
                    error("variable not found!");
                }

                int address = variableAddresses.get(val);

                code = code + "\n" +
                        "OUTWM "+port+" "+address;
            } else if(split[0].equals("in")) {
                String val = split[1];

                String portVal = split[2];

                if(!isInt(portVal)) {
                    error("invalid port!");
                }

                int port = parseInt(portVal);

                if (!variableAddresses.containsKey(val)) {
                    error("variable not found!");
                }

                int address = variableAddresses.get(val);

                code = code + "\n" +
                        "INWM "+port+" "+address;
            } else if (split[0].equals("println")) {
                String val = split[1];

                if (!variableAddresses.containsKey(val)) {
                    error("variable not found!");
                }

                int address = variableAddresses.get(val);

                int f1 = tmpFuncID++;
                int f2 = tmpFuncID++;
                int f3 = tmpFuncID++;

                code = code + "\n" +
                        "INWM 5 20\n" + // BJMP speichern
                        "STORE BOOL 1\n" + // Altes Register speichern
                        "STOREMEM 2 0\n" +
                        "LOADMEM A " + address + "\n" +
                        "STOREMEM 3 " + (address + 1) + "\n" + // +1 weil auf der Adresse ja die Anzahl der Zeichen liegt (und addresse+1 = erstes Zeichen)
                        "FUNC tmp_" + f1 + "\n" +
                        "CMPM A 2\n" +
                        "CJMP printChar_" + f2 + "\n" +
                        "JMP end_" + f3 + "\n" +
                        "FUNC printChar_" + f2 + "\n" +
                        "OUTWDM " + options.getOrDefault("io-print-port", "34") + " 3\n" +
                        "ADDM 3 1\n" +
                        "ADDM 2 1\n" +
                        "JMP tmp_" + f1 + "\n" +
                        "FUNC end_" + f3 + "\n" +
                        "OUTW " + options.getOrDefault("io-print-port", "34") + " 10\n" +
                        "LOADMEM BOOL 1\n" +
                        "OUTWM 5 20"
                ;
            } else if (split[0].equals("putnot")) {
                String varname = split[1];

                if (protectedVariables.contains(varname)) {
                    error("access violation!");
                }

                if (!variableAddresses.containsKey(varname)) {
                    error("variable not found!");
                }

                int address = variableAddresses.get(varname);

                code = code + "\n" +
                        "MULM " + address + " -1\n" +
                        "ADDM " + address + " 1";
            } else if(split[0].equals("asm")) {
                if(options.getOrDefault("disable-asm", "0").equals("1")) {
                    error("asm is disabled");
                }
                String asm = "";
                for (int i = 1; i < split.length; i++) {
                    asm += (" "+split[i]);
                }
                if(!asm.isEmpty()) {
                    asm = asm.substring(1);
                }
                code = code+"\n"+asm;
            } else {
                if (compileProcessor != null) {
                    String a = compileProcessor.generateSchesemForInstruction(compileContext, split);
                    if (a != null) {
                        code = code + "\n" + a;
                    }
                }
            }
        }

        if (currentMethod != null) {
            error("method doesn't close!");
        }


        if (this.compileProcessor != null) {
            String a = this.compileProcessor.generateAfterCompileSCHESEM(compileContext);
            if (a != null) {
                code = code + "\n" + a;
            }
        }

        this.output = code;

        return this;
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
        if(!isInt(i)) {
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

    private void error(String message) {
        throw new IllegalStateException("error in line " + currentLine + ": " + message);
    }

    private void warn(String message) {
        if (this.warningOutput != null) {
            this.warningOutput.accept("warning in line " + currentLine + ": " + message);
        }
    }

    public byte[] toBytecode() {
        if (this.output == null) {
            compile();
        }
        return new Compiler(this.output).compile();
    }

    public String compileDisassemble() {
        if (this.output == null) {
            compile();
        }
        return new Decompiler(new Compiler(this.output).compile()).decompile();
    }

    public String getOutput() {
        if (this.output == null) {
            throw new IllegalStateException("no output present - did you called compile()?");
        }
        return output;
    }

    public Consumer<String> getWarningOutput() {
        return warningOutput;
    }

    public HighProgramCompiler setWarningOutput(Consumer<String> warningOutput) {
        this.warningOutput = warningOutput;
        return this;
    }

    public CompileProcessor getCompileProcessor() {
        return compileProcessor;
    }

    public HighProgramCompiler setCompileProcessor(CompileProcessor compileProcessor) {
        this.compileProcessor = compileProcessor;
        return this;
    }
}
