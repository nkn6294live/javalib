package nkn6294.java.log;

public class LogInformation {
	public static final int INFORMATION_LOG_TYPE = 0;
	public static final int WARNING_LOG_TYPE = 10;
	public static final int ERROR_LOG_TYPE = 100;
	public String tag;
	public Object[] params;
	public int type;
	public LogInformation(int type, String tag, Object... params) {
		this.type = type;
		this.tag = tag;
		this.params = params;
	}
	public LogInformation(String tag, Object... params) {
		this.type = INFORMATION_LOG_TYPE;
		this.tag = tag;
		this.params = params;
	}
}