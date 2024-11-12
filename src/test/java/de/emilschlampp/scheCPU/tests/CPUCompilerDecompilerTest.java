package de.emilschlampp.scheCPU.tests;

import de.emilschlampp.scheCPU.compile.Compiler;
import de.emilschlampp.scheCPU.dissassembler.Decompiler;
import de.emilschlampp.scheCPU.util.StaticValues;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CPUCompilerDecompilerTest {
    @Test
    public void testCompileDecompileSimple() {
        String val = "OUTW 1 0"; // Only simple instructions are possible

        byte[] program = new Compiler(val).compile();

        assertEquals(val, new Decompiler(program).decompile());
    }

    @Test
    public void testDeserial() {
        String val = "OUTW 1 0"; // Do not change!!!

        byte[] program = new Compiler(val).compile();

        assertEquals(1, new Decompiler(program).getInstructions().length);
        assertEquals(StaticValues.OUTW_OPCODE, new Decompiler(program).getInstructions()[0].getOpCode());
    }
}
