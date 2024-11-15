package de.emilschlampp.scheCPU.tests.emulator;

import de.emilschlampp.scheCPU.compile.Compiler;
import de.emilschlampp.scheCPU.emulator.ProcessorEmulator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CPUExecuteTest {
    @Test
    public void testExecute() {
        ProcessorEmulator processorEmulator = new ProcessorEmulator(5, 5, new Compiler("OUTW 16 5").compile());
        processorEmulator.execute();

        assertEquals(processorEmulator.getIo()[1], 0);
    }
}
