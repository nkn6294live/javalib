package nkn6294.java.event;

public interface Channel <E extends Message> {
	public void dispatch(E message);
}
