package nkn6294.java.lib.json;

import java.io.IOException;
import java.io.Reader;

public class StringValue extends DataCommonValue<String> {

	public StringValue(String value) {
		super(value);
	}

	public StringValue() {
		super();
	}

	@Override
	public Data importData(String data) {
		this.setValue(data);
		return this;
	}
	public void setBoundChar(int c) {
		if (Data.isStringBoundChar(c)) {
			this.boundChar = c;
		} else {
			this.boundChar = '"';
		}
	}
	@Override
	public int importData(Reader reader) {
		StringBuilder builder = new StringBuilder();
		int c = -1;
		try {
			while ((c = reader.read()) > 0) {
				if (c == this.boundChar) {//(Data.isStringBoundChar(c)) {
					break;
				}
//				if (Data.isInvalidChar(c)) {
//					builder.append((char) c);
//					continue;
//				}
//				throw new DataImportException("Invalid format");
				builder.append((char) c);
			}
		} catch (IOException ex) {
			this.setValue(getDefaultValue());
			return 0;
//		} catch (DataImportException ex) {
//			this.setValue(getDefaultValue());
//			return 0;
		}
		this.value = builder.toString();
		return c;
	}

	@Override
	public String exportData() {
		if (this.getValue() == null) {
			return "";
		}
		return String.format("\"%s\"", this.getValue());
	}

	@Override
	protected String getDefaultValue() {
		return "";
	}
	
	private int boundChar;
}
