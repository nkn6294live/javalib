package nkn6294.java.lib.json;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

public class DataArrayCommon extends DataCommon implements DataArray {

	public DataArrayCommon() {
		this.datas = new ArrayList<Data>();
	}
	@Override
	public Data importData(String data) {
		if (this.importData(new StringReader(data)) == 0) {
			return null;
		}
		return this;
	}

	@Override
	public int importData(Reader reader) {
		int c = -1;
		try {
			this.datas.clear();
			Data data;
			do {
				c = DataCommon.ignoreSpaceChar(reader);
				if (Data.isStringBoundChar(c)) {
					data = new StringValue();
					((StringValue)data).setBoundChar(c);
				} else if (Data.isStartObjectChar(c)) {
					data = new DataCompositCommon();
				} else {
					throw new DataImportException("Invalid format");
				}
				data.importData(reader);
				this.addData(data);					
				c = DataCommon.ignoreSpaceChar(reader);
				if (Data.isEndArray(c)) {
					break;
				}
				if (Data.isSplitObjectChar(c)) {
					continue;
				}
				throw new DataImportException("Invalid format");
			} while(c > 0);
		} catch(DataImportException ex) {
			this.datas.clear();
			return 0;
		}
		return c;
	}

	@Override
	public String exportData() {
		StringBuilder builder = new StringBuilder();
		builder.append(Data.START_ARRAY_CHAR);
		int index = 0;
		for (Data item : this.datas) {
			if (index++ > 0) {
				builder.append(Data.SPLIT_OBJECT_ITEM_CHARS);
			}
			builder.append(item.exportData());
		}
		builder.append(Data.END_ARRAY_CHAR);
		return builder.toString();
	}

	@Override
	public void addData(Data data) {
		this.datas.add(data);
	}

	@Override
	public Iterable<Data> getDatas() {
		return this.datas;
	}

	@Override
	public int size() {
		return this.datas.size();
	}

	@Override
	public Data getDataAtIndex(int index) {
		return this.datas.get(index);
	}
	
	protected ArrayList<Data> datas;
}
