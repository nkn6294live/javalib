package nkn6294.java.lib.delegate;

public interface Function2<TParam1, TPram2, TResult> {
    @SuppressWarnings("unchecked")
	public TResult action(TParam1 param1, TPram2... param2s);
}
