package nkn6294.java.test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {

    public static byte[] readFile(String filePath, int maxLengthFile) throws Exception {
        byte[] buffer = new byte[maxLengthFile];
        FileInputStream fileInputStream = new FileInputStream(filePath);
        int count = fileInputStream.read(buffer);
        if (count > 0) {
            byte[] result = new byte[count];
            System.arraycopy(buffer, 0, result, 0, count);
            fileInputStream.close();
            return result;
        }
        fileInputStream.close();
        return null;
    }

    public static void writeFile(String filePath, byte[] data) throws Exception {
        FileOutputStream fileOutput = new FileOutputStream(filePath);
        fileOutput.write(data);
        fileOutput.flush();
        fileOutput.close();
    }

    public static String byte2Hex(byte bytes[]) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }

        String result = sb.toString();
        sb.delete(0, sb.length());
        sb = null;
        return result;
    }

    public static String byte2Dex(byte bytes[]) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%03d ", b));
        }

        String result = sb.toString();
        sb.delete(0, sb.length());
        sb = null;
        return result;
    }

    public static byte[] readAllFromInput(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[8 * 1024];
        ByteArrayOutputStream bufferArray = new ByteArrayOutputStream();
        int count = inputStream.read(buffer);
        while (count > 0) {
            bufferArray.write(buffer, 0, count);
            try {
                count = inputStream.read(buffer);
            } catch (IOException ex) {
                break;
            }
        }
        bufferArray.flush();
        return bufferArray.toByteArray();
    }

    public static byte[] readBlockData(InputStream inputStream, byte endBlockByte) throws IOException {
        ByteArrayOutputStream bufferData = new ByteArrayOutputStream();
        int count = inputStream.read();
        if (count < 0) {
            return null;
        }
        while (count > 0) {
            if (count == endBlockByte) {
                break;
            }
            bufferData.write(count);
            count = inputStream.read();
        }
        bufferData.flush();
        byte[] data = bufferData.toByteArray();
        return data;
    }

    public static List<byte[]> splitByteArray(byte[] arrays, byte delimiter) {
        if (arrays == null) {
            return null;
        }
        List<byte[]> listData = new ArrayList<>();
        int currentIndex = -1;
        for (int index = 0; index < arrays.length; index++) {
            if (arrays[index] == delimiter) {
                if (currentIndex + 1 < index) {
                    byte[] temp = Arrays.copyOfRange(arrays, currentIndex + 1, index);
                    listData.add(temp);
                }
                currentIndex = index;
            }
        }
        if (currentIndex + 1 < arrays.length) {
            listData.add(Arrays.copyOfRange(arrays, currentIndex + 1, arrays.length));
        }
        return listData;
    }

    public static List<String> splitString(String string, char delimeter) {
        List<String> listData = new ArrayList<>();
        char[] chars = string.toCharArray();
        int currentIndex = -1;
        for (int index = 0; index < chars.length; index++) {
            if (chars[index] == delimeter) {
                if (currentIndex + 1 < index) {
                    int length = (index - 1) - (currentIndex + 1) + 1;
                    String temp = new String(chars, currentIndex + 1, length);
                    listData.add(temp);
                }
                currentIndex = index;
            }
        }
        if (currentIndex + 1 < string.length()) {
            int length = (string.length() - 1) - (currentIndex + 1) + 1;
            listData.add(new String(chars, currentIndex + 1, length));
        }
        return listData;
    }
}
