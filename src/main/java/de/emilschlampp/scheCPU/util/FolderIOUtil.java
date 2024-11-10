package de.emilschlampp.scheCPU.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;

public class FolderIOUtil {
    public static int CONTENT_LEN_LIMIT = 65_535; //10_000
    public static String readString(InputStream inputStream) throws IOException {
        synchronized (inputStream) {
            return new String(readByteArray(inputStream), StandardCharsets.UTF_8);
        }
    }

    public static String readStringNoLimit(InputStream inputStream) throws IOException {
        synchronized (inputStream) {
            return new String(readByteArrayNoLimit(inputStream), StandardCharsets.UTF_8);
        }
    }

    public static void writeString(OutputStream outputStream, String string) throws IOException {
        synchronized (outputStream) {
            writeByteArray(outputStream, string.getBytes(StandardCharsets.UTF_8));  //Fix: desync
        }
    }

    public static double readDouble(InputStream inputStream) throws IOException {
        return Double.longBitsToDouble(readLong(inputStream));
    }

    public static void writeDouble(OutputStream outputStream, double v) throws IOException {
        writeLong(outputStream, Double.doubleToLongBits(v));
    }

    public static float readFloat(InputStream inputStream) throws IOException {
        return Float.intBitsToFloat(readInt(inputStream));
    }

    public static void writeFloat(OutputStream outputStream, float v) throws IOException {
        writeInt(outputStream, Float.floatToIntBits(v));
    }

    public static int readInt(InputStream inputStream) throws IOException {
        synchronized (inputStream) { //Fix: desync
            int ch1 = inputStream.read();
            int ch2 = inputStream.read();
            int ch3 = inputStream.read();
            int ch4 = inputStream.read();
            if ((ch1 | ch2 | ch3 | ch4) < 0) {
                throw new EOFException();
            }
            return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
        }
    }

    public static void writeInt(OutputStream outputStream, int i) throws IOException {
        synchronized (outputStream) { //Fix: desync
            outputStream.write((i >>> 24) & 0xFF);
            outputStream.write((i >>> 16) & 0xFF);
            outputStream.write((i >>> 8) & 0xFF);
            outputStream.write((i >>> 0) & 0xFF);
        }
    }

    public static long readLong(InputStream inputStream) throws IOException {
        byte[] readBuffer = new byte[8];
        fillByteArray(inputStream, readBuffer);
        return (((long)readBuffer[0] << 56) +
                ((long)(readBuffer[1] & 255) << 48) +
                ((long)(readBuffer[2] & 255) << 40) +
                ((long)(readBuffer[3] & 255) << 32) +
                ((long)(readBuffer[4] & 255) << 24) +
                ((readBuffer[5] & 255) << 16) +
                ((readBuffer[6] & 255) <<  8) +
                ((readBuffer[7] & 255) <<  0));
    }

    public static void writeLong(OutputStream outputStream, long v) throws IOException {
        byte[] writeBuffer = new byte[8];
        writeBuffer[0] = (byte)(v >>> 56);
        writeBuffer[1] = (byte)(v >>> 48);
        writeBuffer[2] = (byte)(v >>> 40);
        writeBuffer[3] = (byte)(v >>> 32);
        writeBuffer[4] = (byte)(v >>> 24);
        writeBuffer[5] = (byte)(v >>> 16);
        writeBuffer[6] = (byte)(v >>>  8);
        writeBuffer[7] = (byte)(v >>>  0);
        outputStream.write(writeBuffer, 0, 8);
    }

    public static byte[] readByteArray(InputStream inputStream) throws IOException {
        synchronized (inputStream) { //Fix: desync
            int len = readInt(inputStream);
            if (CONTENT_LEN_LIMIT > 0) {
                if (len > CONTENT_LEN_LIMIT) {
                    throw new ContentSizeException(len + " is to big.");
                }
            }
            if (len < 0) {
                throw new ContentSizeException(len + " is to small.");
            }

            int r = len;
            byte[] read = new byte[len];
            while (r > 0) {
                r = r - inputStream.read(read, read.length-r, r);
            }
            return read;
        }
    }

    public static void fillByteArray(InputStream inputStream, byte[] data) throws IOException {
        synchronized (inputStream) { //Fix: desync
            int r = data.length;
            while (r > 0) {
                r = r - inputStream.read(data, data.length-r, r);
            }
        }
    }

    public static String[] readStringArray(InputStream inputStream) throws IOException {
        synchronized (inputStream) { //Fix: desync
            int len = readInt(inputStream);
            if (CONTENT_LEN_LIMIT > 0) {
                if (len > CONTENT_LEN_LIMIT) {
                    throw new ContentSizeException(len + " is to big.");
                }
            }
            if (len < 0) {
                throw new ContentSizeException(len + " is to small.");
            }

            String[] s = new String[len];
            for (int i = 0; i < len; i++) {
                s[i] = readStringNoLimit(inputStream);
            }

            return s;
        }
    }

    public static void writeStringArray(OutputStream outputStream, String[] a) throws IOException {
        synchronized (outputStream) { //Fix: desync
            if (CONTENT_LEN_LIMIT > 0) {
                if (a.length > CONTENT_LEN_LIMIT) {
                    throw new ContentSizeException(a.length + " is to big.");
                }
            }
            writeInt(outputStream, a.length);
            for (String s : a) {
                writeString(outputStream, s);
            }
        }
    }

    public static Map<String, String> readMap(InputStream inputStream) throws IOException {
        synchronized (inputStream) {  //Fix: desync
            String[] a = readStringArray(inputStream);
            String[] b = readStringArray(inputStream);

            if (a.length != b.length) {
                throw new ContentInvalidException("Not a map.");
            }


            Map<String, String> map = new HashMap<>();

            for (int i = 0; i < a.length; i++) {
                map.put(a[i], b[i]);
            }

            return map;
        }
    }

    public static void writeMap(Map<String, String> map, OutputStream outputStream) throws IOException {
        synchronized (outputStream) { //Fix: desync
            List<Map.Entry<String, String>> list = new ArrayList<>(map.entrySet());

            String[] a = new String[list.size()];
            String[] b = new String[list.size()];

            int i = 0;
            for (Map.Entry<String, String> entry : list) {
                a[i] = entry.getKey();
                b[i] = entry.getValue();
                i++;
            }
            writeStringArray(outputStream, a);
            writeStringArray(outputStream, b);
        }
    }

    public static byte[] readByteArrayNoLimit(InputStream inputStream) throws IOException {
        synchronized (inputStream) {  //Fix: desync

            int len = readInt(inputStream);

            int r = len;
            byte[] read = new byte[len];
            while (r > 0) {
                r = r - inputStream.read(read, read.length-r, r);
            }
            return read;
        }
    }

    public static void writeByteArray(OutputStream outputStream, byte[] write) throws IOException {
        synchronized (outputStream) {  //Fix: desync
            writeInt(outputStream, write.length);
            outputStream.write(write);
        }
    }

    public static boolean readBoolean(InputStream inputStream) throws IOException {
        synchronized (inputStream) {  //Fix: desync
            return inputStream.read() == 1;
        }
    }

    public static void writeBoolean(OutputStream outputStream, boolean b) throws IOException {
        synchronized (outputStream) {  //Fix: desync
            outputStream.write(b ? 1 : 0);
        }
    }
}
