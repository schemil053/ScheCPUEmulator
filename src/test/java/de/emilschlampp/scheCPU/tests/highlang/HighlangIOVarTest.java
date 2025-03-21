package de.emilschlampp.scheCPU.tests.highlang;

import de.emilschlampp.scheCPU.emulator.ProcessorEmulator;
import de.emilschlampp.scheCPU.high.HighProgramCompiler;
import de.emilschlampp.scheCPU.high.processor.CompileContext;
import de.emilschlampp.scheCPU.high.processor.CompileProcessor;
import de.emilschlampp.scheCPU.util.OCompileProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HighlangIOVarTest {
    @Test
    public void testHighlangOutVarFunction() {
        HighProgramCompiler compiler = new HighProgramCompiler(
                "reserve port 1\nreserve value 1\n" +
                        "var port 129\n" +
                        "var value 15\n" +
                        "outvar port value"
        );

        ProcessorEmulator emulator = new ProcessorEmulator(128, 128, compiler.toBytecode());

        int c = 0;
        while (emulator.canExecute()) {
            c++;
            Assertions.assertTrue(c < 1000, "Too many iterations!");

            emulator.execute();
        }

        Assertions.assertEquals(15, emulator.getIo()[129]);

    }

    @Test
    public void testHighlangInVarFunction() {
        HighProgramCompiler compiler = new HighProgramCompiler(
                "reserve port 1\nreserve port2 1\nreserve value 1\n" +
                        "var port 129\n" +
                        "var port2 130\n" +
                        "var value 15\n" +
                        "invar port value\n"+
                        "outvar port2 value"
        );

        ProcessorEmulator emulator = new ProcessorEmulator(128, 128, compiler.toBytecode());

        emulator.getIo()[129] = 32;

        int c = 0;
        while (emulator.canExecute()) {
            c++;
            Assertions.assertTrue(c < 1000, "Too many iterations!");

            emulator.execute();
        }

        Assertions.assertEquals(32, emulator.getIo()[130]);

    }
}
