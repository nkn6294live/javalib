package nkn6294.java.event;

public interface Router <E extends Message> {
	public void registerChannel(Class<? extends E> contentType, Channel<? extends E> channel);
	public abstract void dispatch(E content);
}
