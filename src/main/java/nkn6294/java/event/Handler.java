package nkn6294.java.event;

public class Handler implements Channel<Event>{

	@Override
	public void dispatch(Event message) {
		System.out.println(message.getType());
	}
}
