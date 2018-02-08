package nkn6294.java.lib.delegate;

public interface Function<TParam, TResult> {
    @SuppressWarnings("unchecked")
	public TResult action(TParam... values);
}
