package de.emilschlampp.scheCPU.tests.highlang;

import de.emilschlampp.scheCPU.high.processor.CompileContext;
import de.emilschlampp.scheCPU.high.processor.CompileProcessor;

public class HighlangIOCompileProcessor extends CompileProcessor {
    public static final int IO_PRINT = 129;


    @Override
    public void startCompile(CompileContext compileContext) {
        compileContext.getOptions().put("io-print-port", ""+IO_PRINT);
        compileContext.getProtectedOptions().add("io-print-port");
    }

    @Override
    public String generatePreCompileHigh() {
        return "";
    }

    @Override
    public String generatePreCompileHighEnd() {
        return "";
    }

    @Override
    public String generatePreCompileSCHESEM() {
        return "";
    }

    @Override
    public String generateSchesemForInstruction(CompileContext compileContext, String[] instruction) {
        return "";
    }

    @Override
    public String generateAfterCompileSCHESEM(CompileContext compileContext) {
        return "";
    }
}
