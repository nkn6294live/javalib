package nkn6294.java.lib.json;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class DataCompositCommon extends DataCommon implements DataComposit {
	public static Data createData(String data) {
		return new DataCompositCommon().importData(data);
	}
	
	public static Data createData(StringReader reader) {
		Data data = new DataCompositCommon();
		data.importData(reader);
		return data;
	}
	public DataCompositCommon() {
		this.maps = new HashMap<String, Data>();
	}
	
	@Override
	public Data importData(String data) {
		if ((this.importData(new StringReader(data)) != 0)) {
			return this;
		}
		return null;
	}
	/***
	 * Import this from reader.
	 * @return< 0: Success, otherwise : Error.
	 */
	@Override
	public int importData(Reader reader) {
		this.maps.clear();
		int c = -1;
		try {
			do {
				String name = DataCommon.getName(reader);
				if (name.isEmpty()) {
					return c;
				}
				Data data = DataCommon.getData(reader);
				this.setData(name, data);	
				c = DataCommon.ignoreSpaceChar(reader);
				if (Data.isEndObjectChar(c)) {
					break;
				}
				if (Data.isSplitObjectChar(c)) {
					continue;
				}
				throw new DataImportException("Invalid format");
			} while(c > 0);
		} catch(IOException ex) {
			this.maps.clear();
			return 0;
		} catch(DataImportException ex) {
			this.maps.clear();
			return 0;
		}
		return c;
	}
	@Override
	public String exportData() {
		StringBuilder builder = new StringBuilder();
		builder.append(Data.START_OBJECT_CHAR);
		int index = 0;
		for (Map.Entry<String, Data> item : this.maps.entrySet()) {
			if (index++ > 0) {
				builder.append(Data.SPLIT_OBJECT_ITEM_CHARS);
			}
			builder.append(item.getKey());
			builder.append(Data.SPLIT_NAME_VALUE_CHAR);
			builder.append(item.getValue().exportData());
		}
		builder.append(Data.END_OBJECT_CHAR);
		return builder.toString();
	}

	@Override
	public Data getData(String name) {
		return this.maps.get(name);
	}

	@Override
	public Data setData(String name, Data value) {
		return (Data)maps.put(name, value);
	}

	@Override
	public Iterable<Data> getDatas() {
		return maps.values();
	}

	@Override
	public Iterable<String> getNames() {
		return maps.keySet();
	}
	private Map<String, Data> maps;
}
