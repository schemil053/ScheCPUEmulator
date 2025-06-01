package de.emilschlampp.scheCPU.examples.dissassemble;

import de.emilschlampp.scheCPU.dissassembler.Decompiler;
import de.emilschlampp.scheCPU.util.FolderIOUtil;

import java.io.FileInputStream;

public class DisassembleExample {
    public static void main(String[] args) throws Throwable {
        FileInputStream inputStream = new FileInputStream("compile.sbin");
        byte[] program = new byte[inputStream.available()];
        FolderIOUtil.fillByteArray(inputStream, program);
        inputStream.close();

        System.out.println(new Decompiler(program).decompile());
    }
}
