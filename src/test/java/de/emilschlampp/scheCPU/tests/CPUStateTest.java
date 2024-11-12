package de.emilschlampp.scheCPU.tests;

import de.emilschlampp.scheCPU.compile.Compiler;
import de.emilschlampp.scheCPU.emulator.ProcessorEmulator;
import de.emilschlampp.scheCPU.util.FolderIOUtil;
import de.emilschlampp.scheCPU.util.StaticValues;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CPUStateTest {
    @Test()
    public void testSerialVersion() throws IOException {
        ProcessorEmulator processorEmulator = new ProcessorEmulator(5, 5, new Compiler("OUTW 1 0").compile());
        processorEmulator.execute();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(processorEmulator.saveState());

        assertEquals(FolderIOUtil.readInt(inputStream), StaticValues.SERIAL_VERSION);
    }

    @Test()
    public void testState() {
        byte[] program = new Compiler("OUTW 1 0").compile();

        ProcessorEmulator processorEmulator1 = new ProcessorEmulator(5, 5, program);
        processorEmulator1.execute();

        ProcessorEmulator processorEmulator2 = new ProcessorEmulator(5, 5, program);


        ProcessorEmulator stateCopy1 = new ProcessorEmulator(processorEmulator1.saveState());
        ProcessorEmulator stateCopy2 = new ProcessorEmulator(processorEmulator2.saveState());

        assertNotEquals(processorEmulator1.getJmp(), stateCopy2.getJmp());
        assertNotEquals(processorEmulator2.getJmp(), stateCopy1.getJmp());

        assertEquals(processorEmulator1.getJmp(), stateCopy1.getJmp());
        assertEquals(processorEmulator2.getJmp(), stateCopy2.getJmp());
    }
}
