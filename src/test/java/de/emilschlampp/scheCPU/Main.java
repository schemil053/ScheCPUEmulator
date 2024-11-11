package de.emilschlampp.scheCPU;

import de.emilschlampp.scheCPU.compile.Compiler;
import de.emilschlampp.scheCPU.dissassembler.Decompiler;
import de.emilschlampp.scheCPU.emulator.ProcessorEmulator;

import java.io.FileOutputStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Throwable {
        Scanner scanner = new Scanner(Main.class.getResourceAsStream("/simple-loadstrm.sasm"));
        String l = "";
        while (scanner.hasNextLine()) {
            l+="\n"+scanner.nextLine();
        }
        if(!l.isEmpty()) {
            l = l.substring(1);
        }

        byte[] code = new Compiler(l).compile();

        FileOutputStream outputStream = new FileOutputStream("compile.sbin");
        outputStream.write(code);
        outputStream.close();

        String codeR = new Decompiler(code).decompile();

        code = new Compiler(codeR).compile();

        ProcessorEmulator emulator = new ProcessorEmulator(512, 512, code);
        while (emulator.canExecute()) {
            emulator.execute();
        }
    }
}
