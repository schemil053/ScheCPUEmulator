package de.emilschlampp.scheCPU.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;

public class CompileCompressorTest {
    public static void main(String[] args) throws Throwable {
        FileInputStream inputStream = new FileInputStream(new File("compile.sbin"));

        byte[] b = new byte[inputStream.available()];
        inputStream.read(b);

        System.out.println(Base64.getEncoder().encodeToString(b));

        inputStream.close();
    }
}
