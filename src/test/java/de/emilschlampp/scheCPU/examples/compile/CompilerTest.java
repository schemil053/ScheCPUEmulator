package de.emilschlampp.scheCPU.examples.compile;

import de.emilschlampp.scheCPU.examples.Main;
import de.emilschlampp.scheCPU.compile.Compiler;

import java.io.FileOutputStream;
import java.util.Scanner;

public class CompilerTest {
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
    }
}
