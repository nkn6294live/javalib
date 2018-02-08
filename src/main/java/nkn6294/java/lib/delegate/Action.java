package nkn6294.java.lib.delegate;

public interface Action<T> {
    @SuppressWarnings("unchecked")
	public void action(T... values);
}
