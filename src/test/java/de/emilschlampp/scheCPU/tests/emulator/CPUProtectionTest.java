package de.emilschlampp.scheCPU.tests.emulator;

import de.emilschlampp.scheCPU.compile.Compiler;
import de.emilschlampp.scheCPU.emulator.ProcessorEmulator;
import de.emilschlampp.scheCPU.util.EmulatorSandboxRestrictions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;

public class CPUProtectionTest {
    @Test
    public void testCPUOutputProtection() {
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

    @Test
    public void testCPUResetProtection() {
        String program = "OUTW 6 1"; // 6 = reset trigger

        ProcessorEmulator processorEmulator = new ProcessorEmulator(5, 5, new Compiler(program).compile());
        processorEmulator.setRestrictions(new EmulatorSandboxRestrictions().setAllowReset(false));

        int cur = 0;

        while (processorEmulator.canExecute()) {
            Assertions.assertTrue(cur < 5);
            processorEmulator.execute();
            cur++;
        }
    }

    @Test
    public void testCPUFaultProtection() {
        String program = "JMP setup\n" +
                "FUNC faulthandler\n" +
                "OUTW 34 70\n"+ // F
                "OUTW 34 97\n"+ // a
                "OUTW 34 117\n"+// u
                "OUTW 34 108\n"+// l
                "OUTW 34 116\n"+// t
                "OUTW 34 10\n"+ // \n
                "ADDM 3 1\n"+
                "FUNC setup\n" +
                "OUTWFUNC 8 faulthandler\n" +
                "FUNC loop\n"+
                "ADDM 2 1\n"+
                "JMP loop";

        ProcessorEmulator processorEmulator = new ProcessorEmulator(5, 5, new Compiler(program).compile());
        processorEmulator.setRestrictions(new EmulatorSandboxRestrictions().setAllowFault(false));

        while (processorEmulator.canExecute()) {
            processorEmulator.execute();
            if(processorEmulator.getMemory()[2] > 5) {
                break;
            }
        }
        processorEmulator.fault(5);
        while (processorEmulator.canExecute()) {
            processorEmulator.execute();
            if(processorEmulator.getMemory()[2] > 15) {
                break;
            }
        }

        Assertions.assertEquals(0, processorEmulator.getMemory()[3]);
    }
}
