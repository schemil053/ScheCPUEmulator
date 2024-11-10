package de.emilschlampp.scheCPU.emulator;

import de.emilschlampp.scheCPU.util.FolderIOUtil;

import java.io.FileInputStream;

public class CPUEmulatorTest {
    public static void main(String[] args) throws Throwable {
        FileInputStream inputStream = new FileInputStream("compile.sbin");
        byte[] program = new byte[inputStream.available()];
        FolderIOUtil.fillByteArray(inputStream, program);
        inputStream.close();

        ProcessorEmulator emulator = new ProcessorEmulator(512, 512, program);
        while (emulator.canExecute()) {
            emulator.execute();
        }
    }
}
