package nkn6294.java.event;

public class Event implements Message {

	@Override
	public Class<? extends Message> getType() {
		return getClass();
	}
	
}
