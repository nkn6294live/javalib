package nkn6294.java.lib.json;

public interface DataComposit extends Data {
	public Data getData(String name);
	public Data setData(String name, Data value);
	public Iterable<String> getNames();
}
