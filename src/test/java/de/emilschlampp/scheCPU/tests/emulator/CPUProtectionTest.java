package de.emilschlampp.scheCPU.tests.emulator;

import de.emilschlampp.scheCPU.compile.Compiler;
import de.emilschlampp.scheCPU.emulator.ProcessorEmulator;
import de.emilschlampp.scheCPU.util.EmulatorSandboxRestrictions;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;

public class CPUProtectionTest {
    @Test
    public void testCPUProtection() {
        String program = "OUTW 34 72\n" + //H
                "OUTW 34 105"; // i


        PrintStream printStream = System.out;

        System.setOut(new PrintStream(System.out) {
            @Override
            public void print(char c) {
                throw new IllegalStateException("should not print!");
            }
        });

        try {
            ProcessorEmulator processorEmulator = new ProcessorEmulator(5, 5, new Compiler(program).compile());
            processorEmulator.setRestrictions(new EmulatorSandboxRestrictions().setAllowOutput(false));

            while (processorEmulator.canExecute()) {
                processorEmulator.execute();
            }
        } catch (Throwable throwable) {
            System.setOut(printStream);
            throw throwable;
        }
        System.setOut(printStream);
    }
}
