package de.emilschlampp.scheCPU.high.preprocessing;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class HighLangPreprocessor {
    private PreprocessorEnvironment preprocessorEnvironment = new PreprocessorEnvironment();

    private final String program;
    private String result;

    public HighLangPreprocessor(String program) {
        this.program = program;
    }

    public HighLangPreprocessor preprocess() {
        //TODO 01.02.2025 ~ifdef, ~define, ~ifndef, ~undef

        List<String> programSplit = new ArrayList<>(Arrays.asList(program.split("\n")));
        Map<String, Boolean> definedMacros = new HashMap<>();

        List<String> newProgram = new ArrayList<>();

        for (int i = 0; i < programSplit.size(); i++) {
            String line = programSplit.get(i);

            if(line.startsWith("~")) {
                String command = line.substring(1);

                String[] split = command.split(" ");

                if(split[0].equals("include")) {
                    InputStream in = null;

                    try {
                        in = preprocessorEnvironment.getFileInputStreamCreator().apply(split[1]);
                    } catch (IOException throwable) {
                        throw new RuntimeException(throwable);
                    }
                    if(in == null) {
                        throw new RuntimeException(); //TODO 01.02.2025 err
                    }

                    Scanner scanner = new Scanner(in);

                    int cur = 0;
                    while (scanner.hasNextLine()) {
                        programSplit.add(i+1+cur, scanner.nextLine());
                        cur++;
                    }
                } else if(split[0].equals("define")) {
                    if(split.length == 2) {
                        definedMacros.put(split[1], true); //TODO 01.02.2025 Values
                    }
                } else if(split[0].equals("undef")) {
                    if(split.length == 2) {
                        definedMacros.remove(split[1]);
                    }
                } else if(split[0].equals("ifdef")) {
                    if(split.length != 2) {
                        throw new RuntimeException();
                    }

                    String macro = split[1];

                    if(!definedMacros.containsKey(macro)) {
                        int bracesCount = 1;

                        while (bracesCount > 0 && i < programSplit.size() -1) {
                            i++;
                            String nextLine = programSplit.get(i);
                            if(nextLine.startsWith("~ifdef") || nextLine.startsWith("~ifndef")) {
                                bracesCount++;
                            } else if(nextLine.startsWith("~endif")) {
                                bracesCount--;
                            }
                        }
                    }
                } else if(split[0].equals("ifndef")) {
                    if(split.length != 2) {
                        throw new RuntimeException();
                    }

                    String macro = split[1];

                    if(definedMacros.containsKey(macro)) {
                        int bracesCount = 1;

                        while (bracesCount > 0 && i < programSplit.size() -1) {
                            i++;
                            String nextLine = programSplit.get(i);
                            if(nextLine.startsWith("~ifdef") || nextLine.startsWith("~ifndef")) {
                                bracesCount++;
                            } else if(nextLine.startsWith("~endif")) {
                                bracesCount--;
                            }
                        }
                    }
                } else if(split[0].equals("endif")) {

                }
            } else {
                newProgram.add(line);
            }

            //programSplit.add(line);
        }

        StringBuilder res = new StringBuilder();
        boolean a = false;
        for (String s : newProgram) {
            res.append(s).append("\n");
            a = true;
        }

        if(a) {
            this.result = res.substring(0, res.length()-1);
        } else {
            this.result = res.toString();
        }

        return this;
    }

    public String getProgram() {
        return program;
    }

    public String getResult() {
        return result;
    }

    public PreprocessorEnvironment getPreprocessorEnvironment() {
        return preprocessorEnvironment;
    }

    public HighLangPreprocessor setPreprocessorEnvironment(PreprocessorEnvironment preprocessorEnvironment) {
        this.preprocessorEnvironment = preprocessorEnvironment;
        return this;
    }
}
