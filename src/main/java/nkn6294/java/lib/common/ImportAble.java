package nkn6294.java.lib.common;

import java.io.Reader;

public interface ImportAble<T> {
	public T importData(String data);
	public int importData(Reader reader);
}
