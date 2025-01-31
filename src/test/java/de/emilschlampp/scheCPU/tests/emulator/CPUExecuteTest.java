package de.emilschlampp.scheCPU.tests.emulator;

import de.emilschlampp.scheCPU.compile.Compiler;
import de.emilschlampp.scheCPU.emulator.ProcessorEmulator;
import de.emilschlampp.scheCPU.util.EmulatorSandboxRestrictions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CPUExecuteTest {
    @Test
    public void testExecute() {
        ProcessorEmulator processorEmulator = new ProcessorEmulator(5, 5, new Compiler("OUTW 16 5").compile());
        processorEmulator.execute();

        assertEquals(processorEmulator.getIo()[1], 0);
    }

    @Test
    public void testFault() {
        String program = "JMP setup\n" +
                "FUNC faulthandler\n" +
                "ADDM 3 1\n" +
                "JMP end\n"+
                "FUNC setup\n" +
                "OUTWFUNC 8 faulthandler\n" +
                "FUNC loop\n"+
                "ADDM 2 1\n"+
                "JMP loop\n" +
                "FUNC end";

        ProcessorEmulator processorEmulator = new ProcessorEmulator(5, 5, new Compiler(program).compile());

        while (processorEmulator.canExecute()) {
            processorEmulator.execute();
            if(processorEmulator.getMemory()[2] > 5) {
                break;
            }
        }
        processorEmulator.fault(5);
        while (processorEmulator.canExecute()) {
            processorEmulator.execute();
            Assertions.assertFalse(processorEmulator.getMemory()[2] > 6); // the code should end after the fault (see 'jmp end' and 'func end')
        }

        Assertions.assertEquals(1, processorEmulator.getMemory()[3]);
    }
}
