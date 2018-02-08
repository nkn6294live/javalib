package nkn6294.java.lib.json;

public interface DataArray extends Data {
	public void addData(Data data);
	public int size();
	public Data getDataAtIndex(int index);
}
