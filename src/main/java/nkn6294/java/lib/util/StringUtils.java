package nkn6294.java.lib.util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.security.InvalidParameterException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import nkn6294.java.lib.io.InputStreamBlock;

public class StringUtils {
	public static String deAccentConvert(String input){
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
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
			if (field == null || field.isEmpty()) {
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
    
    public static String LengthToSize(long lengthLong) {
        String suffix = "B";
        double length = (double) lengthLong;
        if (length >= 1024) {
            length /= 1024;
            suffix = "K";
        } else {
            return String.format("%.0f%s", length, suffix);
        }
        if (length >= 1024) {
            length /= 1024;
            suffix = "M";
        } else {
            return String.format("%.0f%s", length, suffix);
        }
        if (length >= 1024) {
            length /= 1024;
            suffix = "G";
        } else {
            return String.format("%.02f%s", length, suffix);
        }
        if (length >= 1024) {
            length /= 1024;
            suffix = "T";
            return String.format("%.02f%s", length, suffix);
        } else {
            return String.format("%.02f%s", length, suffix);
        }
    }
    
	public static float CheckSame(String[] sources, String[] dests) {
		if (sources == null || dests == null || sources.length == 0 || dests.length == 0) {
			return 0f;
		}
		int count = 0;
        for (String element : sources) {
        	if (count >= dests.length) {
        		return 1f;
        	}
            if (element.equals(dests[count])) {
                count++;
            }
        }
        return 1.0f * count / dests.length;
	}
	public static float CheckSame(String source, String dest) {//source: dau vao->dest can so sanh
		if (source == null || dest == null) {
			return 0f;
		}
		if (source.equals(dest)) {
			return 1f;
		}
		return CheckSame(splitString(source), splitString(dest));
	}
	public static float CheckSame(String source, String[] dest) {//source: dau vao->dest can so sanh
		if (source == null || dest == null || dest.length == 0) {
			return 0f;
		}
		return CheckSame(splitString(source), dest);
	}
    public static String[] splitString(String key) {
        return splitStringToList(key).stream().toArray(String[]::new);
    }
    
    public static List<String> splitStringToList(String key) {
        List<String> list = new ArrayList<>();
        try {
            final InputStreamBlock reader = new InputStreamBlock(new StringReader(key));
            String word = reader.nextWord();
            while (word != null) {
                list.add(word.toLowerCase());
                word = reader.nextWord();
            }
        } catch (IOException ex) {
        }
        return list;
    }
    
    public static int search(String key, String source) {
        String[] keys = key.split(" ");
        String[] sources = source.split(" ");
        int count = 0;
        for (String element : sources) {
            if (count >= keys.length) {
                break;
            }
            if (element.equals(keys[count])) {
                count++;
            }
        }
        return count;
    }

    public static List<String> search(String key, String[] sources) {
        int maxItem = key.split(" ").length;
        List<String> strings = new ArrayList<>();
        for (String item : sources) {
            if (search(key, item) == maxItem) {
                strings.add(item);
            }
        }
        return strings;
    }

    public static List<Integer> searchByIndex(String key, String[] sources) {
        int maxItem = key.split(" ").length;
        List<Integer> strings = new ArrayList<>();
        for (int i = 0; i < sources.length; i++) {
            String item = sources[i];
            if (search(key, item) == maxItem) {
                strings.add(i);
            }
        }
        return strings;
    }

    
    private final static char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefjhijklmnopqrstuvwxyz0123456789".toCharArray();
	private final static String invalidChars = "<>\"'{}()[]`&";
	private final static Random random = new Random();
}
