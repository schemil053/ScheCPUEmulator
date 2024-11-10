package de.emilschlampp.scheCPU.high.processor;

public abstract class CompileProcessor {
    public abstract void startCompile(CompileContext compileContext);
    public abstract String generatePreCompileHigh();
    public abstract String generatePreCompileHighEnd();
    public abstract String generatePreCompileSCHESEM();
    public abstract String generateSchesemForInstruction(CompileContext compileContext, String[] instruction);
    public abstract String generateAfterCompileSCHESEM(CompileContext compileContext);
}
