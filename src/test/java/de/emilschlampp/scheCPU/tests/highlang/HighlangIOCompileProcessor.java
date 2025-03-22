package de.emilschlampp.scheCPU.tests.highlang;

import de.emilschlampp.scheCPU.high.processor.CompileContext;
import de.emilschlampp.scheCPU.util.OCompileProcessor;

import java.util.Map;

public class HighlangIOCompileProcessor extends OCompileProcessor {
    public static final int IO_PRINT = 129;

    private Map<String, Integer> variableAddresses;

    @Override
    public void startCompile(CompileContext compileContext) {
        compileContext.getOptions().put("io-print-port", ""+IO_PRINT);
        compileContext.getProtectedOptions().add("io-print-port");
    }

    @Override
    public String generateAfterCompileSCHESEM(CompileContext compileContext) {
        variableAddresses = compileContext.getVariableAddresses();
        return "";
    }

    public Map<String, Integer> getVariableAddresses() {
        return variableAddresses;
    }
}
