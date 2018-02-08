package nkn6294.java.lib.json;

import nkn6294.java.lib.collection.CollectionUtil;
import nkn6294.java.lib.common.ExportAble;
import nkn6294.java.lib.common.ImportAble;

public interface Data extends ImportAble<Data>, ExportAble {
	public Iterable<Data> getDatas();
	public static final char[] SPACE_CHARS = {' ', '\t', '\n'};
	public static final char[] LETTER_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFJHIJKLMNOPQRSTUVWXYZ0123456789_.".toCharArray();
	public static final char SPLIT_OBJECT_ITEM_CHARS = ',';
	public static final char START_OBJECT_CHAR = '{';
	public static final char END_OBJECT_CHAR = '}';
	public static final char START_STRING_CHAR = '"';
	public static final char[] STRING_BOUND_CHAR = {'\'', '"'};
	public static final char SPLIT_NAME_VALUE_CHAR = ':';
	public static final char START_ARRAY_CHAR = '[';
	public static final char END_ARRAY_CHAR = ']';
	
	public static boolean isInvalidChar(int c) {
		return 	isStartObjectChar(c) ||
				isEndObjectChar(c) ||
				isSplitObjectChar(c) || 
				isSplitNameValueChar(c) ||
				isSpaceChar(c) || 
				isLetterChar(c);
	}
	public static boolean isStartArray(int c) {
		return c == START_ARRAY_CHAR;
	}
	public static boolean isEndArray(int c) {
		return c == END_ARRAY_CHAR;
	}
	public static boolean isStartObjectChar(int c) {
		return c == START_OBJECT_CHAR;
	}
	public static boolean isEndObjectChar(int c) {
		return c == END_OBJECT_CHAR;
	}
	public static boolean isStringBoundChar(int c) {
		return CollectionUtil.isContains(c, STRING_BOUND_CHAR);
	}
	public static boolean isSplitObjectChar(int c) {
		return c == SPLIT_OBJECT_ITEM_CHARS;
	}
	public static boolean isSplitNameValueChar(int c) {
		return c == SPLIT_NAME_VALUE_CHAR;
	}
	public static boolean isSpaceChar(int c) {
		return CollectionUtil.isContains(c, SPACE_CHARS);
	}
	public static boolean isLetterChar(int c) {
		return CollectionUtil.isContains(c, LETTER_CHARS);
	}
}
