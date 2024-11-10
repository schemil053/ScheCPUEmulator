package de.emilschlampp.scheCPU.util;

public class EmulatorSandboxRestrictions {
    private boolean allowOutput = true;
    private boolean allowReset = true;


    public boolean isAllowOutput() {
        return allowOutput;
    }

    public EmulatorSandboxRestrictions setAllowOutput(boolean allowOutput) {
        this.allowOutput = allowOutput;
        return this;
    }

    public boolean isAllowReset() {
        return allowReset;
    }

    public EmulatorSandboxRestrictions setAllowReset(boolean allowReset) {
        this.allowReset = allowReset;
        return this;
    }
}
