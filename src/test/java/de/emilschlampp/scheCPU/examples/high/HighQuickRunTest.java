package de.emilschlampp.scheCPU.examples.high;

import de.emilschlampp.scheCPU.dissassembler.Decompiler;
import de.emilschlampp.scheCPU.emulator.ProcessorEmulator;
import de.emilschlampp.scheCPU.examples.Main;
import de.emilschlampp.scheCPU.high.HighProgramCompiler;

import java.util.Scanner;

public class HighQuickRunTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(Main.class.getResourceAsStream("/concat-test.shf"));
        String l = "";
        while (scanner.hasNextLine()) {
            l+="\n"+scanner.nextLine();
        }
        if(!l.isEmpty()) {
            l = l.substring(1);
        }

        byte[] code = new HighProgramCompiler(l).toBytecode();

        //System.out.println(new Decompiler(code).decompile());

        ProcessorEmulator emulator = new ProcessorEmulator(512, 512, code);
        while (emulator.canExecute()) {
            emulator.execute();
        }
    }
}
