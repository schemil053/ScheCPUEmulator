package de.emilschlampp.scheCPU.examples.high;

import de.emilschlampp.scheCPU.examples.Main;
import de.emilschlampp.scheCPU.high.HighProgramCompiler;
import de.emilschlampp.scheCPU.high.preprocessing.HighLangPreprocessor;

import java.io.FileOutputStream;
import java.util.Scanner;

public class HighPreproCompilerTest {
    public static void main(String[] args) throws Throwable {
        Scanner scanner = new Scanner(Main.class.getResourceAsStream("/concat-test.sasm"));
        String l = "";
        while (scanner.hasNextLine()) {
            l+="\n"+scanner.nextLine();
        }
        if(!l.isEmpty()) {
            l = l.substring(1);
        }

        byte[] code = new HighProgramCompiler(new HighLangPreprocessor(l).preprocess().getResult()).toBytecode();

        FileOutputStream outputStream = new FileOutputStream("compile.sbin");
        outputStream.write(code);
        outputStream.close();
    }
}
