package nkn6294.java.lib.delegate;

public interface Predicate<T> {
    @SuppressWarnings("unchecked")
	public boolean isInvalid(T... values);
}
