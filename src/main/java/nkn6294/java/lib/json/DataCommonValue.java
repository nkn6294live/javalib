package nkn6294.java.lib.json;

import java.util.ArrayList;

public abstract class DataCommonValue<T> extends DataCommon implements DataSimple{
	public DataCommonValue() {
		this.value = null;
		this.defaultValue = null;
		this.values = new ArrayList<Data>();
		this.values.add(this);
	}
	
	public DataCommonValue(T value) {
		this.value = value;
		this.defaultValue = null;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
	
	public T getValue() {
		return this.value;
	}
	public void setDefaultValue(T value) {
		this.defaultValue = value;
	}
	protected T getDefaultValue() {
		return this.defaultValue;
	}
	@Override
	public Iterable<Data> getDatas() {
		return values;
	}
	protected ArrayList<Data> values;
	protected T value;
	protected T defaultValue;
}
