package de.emilschlampp.scheCPU.high;

import de.emilschlampp.scheCPU.Main;
import de.emilschlampp.scheCPU.compile.Compiler;

import java.io.FileOutputStream;
import java.util.Scanner;

public class HighCompilerTest {
    public static void main(String[] args) throws Throwable {
        Scanner scanner = new Scanner(Main.class.getResourceAsStream("/simple-loadstrm.sasm"));
        String l = "";
        while (scanner.hasNextLine()) {
            l+="\n"+scanner.nextLine();
        }
        if(!l.isEmpty()) {
            l = l.substring(1);
        }

        byte[] code = new HighProgramCompiler(l).toBytecode();

        FileOutputStream outputStream = new FileOutputStream("compile.sbin");
        outputStream.write(code);
        outputStream.close();
    }
}
