package de.emilschlampp.scheCPU.tests.highlang;

import de.emilschlampp.scheCPU.emulator.ProcessorEmulator;
import de.emilschlampp.scheCPU.high.HighProgramCompiler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HighlangCMPTest {
    @Test
    public void testCMP() {
        HighlangIOCompileProcessor ioCompileProcessor = new HighlangIOCompileProcessor();
        HighProgramCompiler compiler = new HighProgramCompiler(
                "reserve res 1\nreserve v1 1\nreserve v2 1\n" +
                        "var res 5\n" +
                        "var v1 1\n" +
                        "var v2 2\n" +
                        "cmp res v1 v2"
        ).setCompileProcessor(ioCompileProcessor);

        ProcessorEmulator emulator = new ProcessorEmulator(128, 128, compiler.toBytecode());

        int c = 0;
        while (emulator.canExecute()) {
            c++;
            Assertions.assertTrue(c < 1000, "Too many iterations!");

            emulator.execute();
        }

        Assertions.assertTrue(ioCompileProcessor.getVariableAddresses().containsKey("res"));
        Assertions.assertEquals(-1, emulator.getMemory()[ioCompileProcessor.getVariableAddresses().get("res")]);
    }
}
