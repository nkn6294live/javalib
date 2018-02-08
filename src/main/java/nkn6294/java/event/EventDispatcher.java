package nkn6294.java.event;

import java.util.HashMap;
import java.util.Map;

public class EventDispatcher implements Router<Event> {

	public EventDispatcher() {
		this.handlers = new HashMap<>();
	}
	
	@Override
	public void registerChannel(Class<? extends Event> contentType, Channel<? extends Event> channel) {
		this.handlers.put(contentType, (Handler)channel);
	}

	@Override
	public void dispatch(Event content) {
		this.handlers.get(content.getClass()).dispatch(content);
	}
	private Map<Class<? extends Event>, Handler> handlers;
}
