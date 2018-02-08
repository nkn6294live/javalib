package nkn6294.java.event;

public interface Message {
	public Class<? extends Message> getType();
}
