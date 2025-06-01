package de.emilschlampp.scheCPU.util;

public class EmulatorSandboxRestrictions {
    private boolean allowOutput = true;
    private boolean allowReset = true;
    private boolean allowFault = true;

    public EmulatorSandboxRestrictions() {
    }

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

    public boolean isAllowFault() {
        return allowFault;
    }

    public EmulatorSandboxRestrictions setAllowFault(boolean allowFault) {
        this.allowFault = allowFault;
        return this;
    }
}
