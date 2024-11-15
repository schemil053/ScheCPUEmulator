package de.emilschlampp.scheCPU.tests.highlang;

import de.emilschlampp.scheCPU.emulator.ProcessorEmulator;
import de.emilschlampp.scheCPU.high.HighProgramCompiler;
import org.junit.jupiter.api.Test;

import static de.emilschlampp.scheCPU.tests.highlang.HighlangIOCompileProcessor.IO_PRINT;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HighlangCompileTest {
    @Test
    public void testHighlangCompiler() {
        String test = "Hallo!";

        HighProgramCompiler compiler = new HighProgramCompiler(
                "reserve t "+test.length()+"\n" +
                        "var t \""+test+"\"\n" +
                        "print t"
        );

        compiler.setCompileProcessor(new HighlangIOCompileProcessor());


        byte[] program = compiler.compile().toBytecode();

        ProcessorEmulator emulator = new ProcessorEmulator(64, 10, program);

        StringBuffer buffer = new StringBuffer();

        while (emulator.canExecute()) {
            emulator.execute();

            if(emulator.getIo()[IO_PRINT] != 0) {
                buffer.append((char) emulator.getIo()[IO_PRINT]);
                emulator.getIo()[IO_PRINT] = 0;
            }
        }

        assertEquals(buffer.toString(), test);
    }
}
