package nkn6294.java.lib.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

public class Util {

	public static String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	public static Random random = new Random();
	public static String randomString(int length) {
		if (length <= 0) {
			return null;
		}
		char[] chars = new char[length];
		int maxIndex = CHARS.length();
		for (int index = 0; index < length; index++) {
			chars[index] = CHARS.charAt(random.nextInt(maxIndex));
		}
		return new String(chars);
	}
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
    
    public static String GetString(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			builder.append(bytes[i]).append(" ");
		}
		return builder.toString();
	}
    
	public static String bytes2String(byte[] bytes) {
		// return Base64.getEncoder().encodeToString(bytes);
		return new String(bytes, StandardCharsets.UTF_8);
	}

	public static byte[] string2bytes(String content) {
		return Base64.getDecoder().decode(content);// ISO_8859_1
	}

	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static Map<String, String> parseParams(String paramString) {
		Map<String, String> params = new HashMap<String, String>();
		if (paramString == null || paramString.length() == 0) {
			return params;
		}
		StringTokenizer tokenizer = new StringTokenizer(paramString, "&");
		while (tokenizer.hasMoreElements()) {
			String param = tokenizer.nextToken();
			int sep = param.indexOf("=");
            String name = (sep >= 0) ? decodeUrl(param.substring(0, sep)).trim() : decodeUrl(param).trim();
            String value = (sep >= 0) ? decodeUrl(param.substring(sep + 1)) : null;
            
            if (name != null && value != null) {
            	params.put(name, value);            	
            }
		}		
		return params;
	}
	public static String decodeUrl(String input) {
        String decoded = null;
        try {
            decoded = URLDecoder.decode(input, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
        }
        return decoded;
	}

}
