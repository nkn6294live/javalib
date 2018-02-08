package nkn6294.java.lib.util;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.Random;

public class StringUtils {
	public static boolean validateInputString(int maxLength, String... fields) {
		char[] chars = invalidChars.toCharArray();
		for(String field : fields) {
			if (field == null || field.isEmpty()) {
				continue;
			}
			if (maxLength > 0 && field.length() > maxLength) {
				return false;
			}
			for (char c : chars) {
				if (field.contains("" + c)) {
					return false;
				}
			}
		}
		return true;
	}
	public static boolean validateInputString(int maxLength, boolean isNullOrEmptyAble, String... fields) {
		char[] chars = invalidChars.toCharArray();
		if (maxLength < 0) {
			return false;
		}
		if (maxLength == 0 && !isNullOrEmptyAble) {
			return false;
		}
		for(String field : fields) {
			if (field == null) {
				if (isNullOrEmptyAble) {
					continue;
				}
				return false;
			}
			if (field.length() > maxLength) {
				return false;
			}
			for (char c : chars) {
				if (field.contains("" + c)) {
					return false;
				}
			}
		}
		return true;
	}
	public static String getShortString(String content) {
		if (content == null) return null;
		int length = content.length();
		if (length < 20) return content;
		return String.format("%s...%s", content.substring(0, 10), content.substring(length - 10, length - 1));
	}    
    public static boolean isStringNullOrEmpty(String o) {
    	if (o == null) return true;
    	return o.isEmpty();
    }
    
    public static int getRandomInt(int min, int max) {
    	if (min < 0 || min > max) {
    		throw new InvalidParameterException("Min value large max value");
    	}
    	return min + random.nextInt(max - min + 1);
    }
    public static String getRandomString(int maxLength) {
    	if (maxLength <= 0) return null;
    	int maxIndex = chars.length;
    	StringBuilder builder = new StringBuilder();
    	
    	for (int index = 0; index < maxLength; index++) {
    		builder.append(chars[random.nextInt(maxIndex)]);
    	}
    	return builder.toString();
    }
    
    public static byte[] fromHexString(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
    public static String byte2Hex(byte bytes[]) {
		StringBuilder sb = new StringBuilder();
	    for (byte b : bytes) {
	        sb.append(String.format("%02x", b));
	    }
	    
	    String result = sb.toString();
	    sb.delete(0, sb.length());
	    sb = null;
	    return result;
	}
    public static void testGetFileExt() {
		String filePath = "test.donw.zip";
		File file = new File(filePath);
		String fullName = file.getName();
		int index = fullName.lastIndexOf('.');
		String name = fullName;
		String ext = "";

		if (index >= 0) {
			name = fullName.substring(0, index);
			ext = fullName.substring(index + 1);
		}
		System.out.println("Name:" + name);
		System.out.println("Ext:" + ext);
	}
    private final static char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefjhijklmnopqrstuvwxyz0123456789".toCharArray();
	private final static String invalidChars = "<>\"'{}()[]`&";
	private final static Random random = new Random();
}
