package de.emilschlampp.scheCPU.high;

import de.emilschlampp.scheCPU.compile.Compiler;
import de.emilschlampp.scheCPU.dissassembler.Decompiler;
import de.emilschlampp.scheCPU.emulator.ProcessorEmulator;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;

public class HighProgramCompilerTest {
    public static void main(String[] args) throws Throwable {
        Scanner scanner = new Scanner(new File("t2.shf"));

        String code = "";

        while (scanner.hasNextLine()) {
            code = code+"\n"+scanner.nextLine();
        }

        if(code.startsWith("\n")) {
            code = code.substring(1);
        }

        code = new HighProgramCompiler(code).compile().getOutput();

        System.out.println(code);

        System.out.println();

        byte[] data = new Compiler(code).compile();

        System.out.println("~~~~~~~~~~~~~~~~~~~[ Decomp ]~~~~~~~~~~~~~~~~~~~~~");
        System.out.println(new Decompiler(data).decompile());

        FileOutputStream outputStream = new FileOutputStream("compile.sbin");
        outputStream.write(data);
        outputStream.close();

        System.out.println("~~~~~~~~~~~~~~~~~~~[ Run ]~~~~~~~~~~~~~~~~~~~~~");
        ProcessorEmulator emulator = new ProcessorEmulator(512, 512, data);
        while (emulator.canExecute()) {
            emulator.execute();
        }
        //System.out.println(Arrays.toString(emulator.getMemory()));
    }
}
