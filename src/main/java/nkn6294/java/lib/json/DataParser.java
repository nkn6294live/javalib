package nkn6294.java.lib.json;

import java.io.Reader;
import java.io.StringReader;
public class DataParser {
	public static Data parser(String content) {
		return DataParser.parser(new StringReader(content));
	}
	public static Data parser(Reader reader) {
		int c = DataCommon.ignoreSpaceChar(reader);
		if (Data.isStartObjectChar(c)) {
			DataComposit data = new DataCompositCommon();
			if (data.importData(reader) != 0) {
				return data;				
			}
		} else if (Data.isStartArray(c)) {
			DataArray data = new DataArrayCommon();
			if (data.importData(reader) != 0) {
				return data;				
			}
		}
		return null;
	}
	
}
