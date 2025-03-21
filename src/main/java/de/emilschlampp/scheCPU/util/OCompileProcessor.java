package de.emilschlampp.scheCPU.util;

import de.emilschlampp.scheCPU.high.processor.CompileContext;
import de.emilschlampp.scheCPU.high.processor.CompileProcessor;

public class OCompileProcessor extends CompileProcessor {
    @Override
    public void startCompile(CompileContext compileContext) {

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
