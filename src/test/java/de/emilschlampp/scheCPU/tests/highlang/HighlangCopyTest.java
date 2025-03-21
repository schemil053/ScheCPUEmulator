package de.emilschlampp.scheCPU.tests.highlang;

import de.emilschlampp.scheCPU.emulator.ProcessorEmulator;
import de.emilschlampp.scheCPU.high.HighProgramCompiler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HighlangCopyTest {
    @Test
    public void testHighlangCopyFunction() {
        HighProgramCompiler compiler = new HighProgramCompiler(
                "var test 143456\n" +
                        "var new 344\n"+
                        "copy test new"
        );

        ProcessorEmulator emulator = new ProcessorEmulator(128, 128, compiler.toBytecode());

        int initial = -1;

        int c = 0;
        while (emulator.canExecute()) {
            c++;
            Assertions.assertTrue(c < 1000, "Too many iterations!");

            emulator.execute();

            if(initial == -1) {
                for (int i = 0; i < emulator.getMemory().length; i++) {
                    if(emulator.getMemory()[i] == 143456) {
                        initial = i;
                    }
                }
            }
        }

        Assertions.assertNotEquals(-1, initial);

        Assertions.assertEquals(344, emulator.getIo()[initial]);

    }
}
