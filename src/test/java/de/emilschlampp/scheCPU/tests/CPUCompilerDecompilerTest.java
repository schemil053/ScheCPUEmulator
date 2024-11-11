package de.emilschlampp.scheCPU.tests;

import de.emilschlampp.scheCPU.compile.Compiler;
import de.emilschlampp.scheCPU.dissassembler.Decompiler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CPUCompilerDecompilerTest {
    @Test
    public void testCompileDecompileSimple() {
        String val = "OUTW 1 0"; // Only simple instructions are possible

        byte[] program = new Compiler(val).compile();

        assertEquals(val, new Decompiler(program).decompile());

    }
}
