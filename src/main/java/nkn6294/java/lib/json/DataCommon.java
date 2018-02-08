package nkn6294.java.lib.json;

import java.io.IOException;
import java.io.Reader;

public abstract class DataCommon implements Data {

	protected static int ignoreSpaceChar(Reader reader) {
		int c = -1;
		try {
			while ((c = reader.read()) > 0) {
				if (!Data.isSpaceChar(c)) {
					break;
				}
			}
		} catch (IOException ex) {
			return -1;
		}
		return c;
	}
	/***
	 * Read next string in reader.
	 * @param reader
	 * @return next string without space before and before split name,value(':') or empty if next char is endObject.
	 * @throws IOException : reader exception
	 * @throws DataImportException : Invalid format.
	 */
	protected static String getName(Reader reader) throws IOException,
			DataImportException {
		StringBuilder builder = new StringBuilder();
		int c = ignoreSpaceChar(reader);
		if (c < 0) {
			throw new DataImportException("Invalid format");
		}
		if (Data.isEndObjectChar(c)) {
			return "";
		}
		if (Data.isLetterChar(c)) {
			builder.append((char) c);
		} else {
			throw new DataImportException("Invalid format");
		}
		while ((c = reader.read()) > 0) {
			if (Data.isSplitNameValueChar(c)) {
				return builder.toString();
			}
			if (Data.isSpaceChar(c)) {
				c = ignoreSpaceChar(reader);
				if (Data.isSplitNameValueChar(c)) {
					return builder.toString();
				} else {
					throw new DataImportException("Invalid format");
				}
			}
			if (Data.isLetterChar(c)) {
				builder.append((char) c);
				continue;
			}
			throw new DataImportException("Invalid format");
		}
		throw new DataImportException("Invalid format");
	}

	protected static Data getData(Reader reader)
			throws IOException, DataImportException {
		int c = ignoreSpaceChar(reader);
		if (c < -1) {
			throw new DataImportException("Invalid format");
		}
		Data data = null;
		if (Data.isStringBoundChar(c)) {
			data = new StringValue();
			((StringValue)data).setBoundChar(c);
		} else if (Data.isStartObjectChar(c)) {
			data = new DataCompositCommon();
		} else if (Data.isStartArray(c)) {
			data = new DataArrayCommon();
		} else {
			throw new DataImportException("Invalid format");
		}
		c = data.importData(reader);
		if (c == 0) {
			throw new DataImportException("Invalid format");
		}
		return data;
	}
	
	protected static Data createDataCommonValue(String content) {
		return null;
	}
	@Override
	public String toString() {
		return this.exportData();
	}
	
	
}
