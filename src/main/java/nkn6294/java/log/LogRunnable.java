package nkn6294.java.log;

import java.util.LinkedList;
import java.util.Queue;

public class LogRunnable implements Runnable {

	public LogRunnable() {
		this.logs = new LinkedList<>();
	}

	public void addLog(String tag, Object... params) {
		synchronized (this.logs) {
			this.logs.offer(new LogInformation(LogInformation.INFORMATION_LOG_TYPE, tag, params));
		}
	}
	public void addWarning(String tag, Object...messages) {
		synchronized (this.logs) {
			this.logs.offer(new LogInformation(LogInformation.WARNING_LOG_TYPE, tag, messages));
		}
	}
	public void addLog(int type, String tag, Object... params) {
		synchronized (this.logs) {
			this.logs.offer(new LogInformation(type, tag, params));
		}
	}

	public void addLog(LogInformation log) {
		if (log == null)
			return;
		synchronized (this.logs) {
			this.logs.add(log);
		}
	}
	public void addError(String tag, Object... params) {
		synchronized (this.logs) {
			this.logs.offer(new LogInformation(LogInformation.ERROR_LOG_TYPE, tag, params));
		}
	}

	@Override
	public void run() {
		synchronized (this.logs) {
			while (!this.logs.isEmpty()) {
				LogInformation log = this.logs.poll();
				if (log == null)
					continue;
				if (log.type == LogInformation.ERROR_LOG_TYPE) {
					this.LogE(log.tag, log.params);
				} else if (log.type == LogInformation.INFORMATION_LOG_TYPE) {
					this.LogI(log.tag, log.params);
				} else if (log.type == LogInformation.WARNING_LOG_TYPE) {
					this.LogW(log.tag, log.params);
				} else {
					this.LogI(log.tag, log.params);
				}
			}
		}
	}

	private Queue<LogInformation> logs;

	private void LogI(String tag, Object... messages) {
		Object message = buildString(messages);
		String content = String.format("[%s]%s", tag, message == null ? "NULL" : message.toString());
		System.out.println(content);
	}
	private void LogW(String tag, Object... messages) {
		Object message = buildString(messages);
		String content = String.format("[%s]%s", tag, message == null ? "NULL" : message.toString());
		System.out.println(content);
	}
	private void LogE(String tag, Object... errors) {
		Object error = buildString(errors);
		String content = String.format("[%s]%s", tag, error == null ? "NULL" : error.toString());
		System.err.println(content);
	}
	private Object buildString(Object...messages) {
		Object message = null;
		if (messages.length == 0) {
			message = "";
		} else if (messages.length == 1) {
			message = messages[0] == null ? "NULL" : messages[0].toString();
		} else {
			StringBuilder builder = new StringBuilder();
			for (int index = 0; index < messages.length; index++) {
				Object item = messages[index];
				builder.append(String.format("[%s]", item == null ? "NULL" : item.toString()));
			}
			message = builder.toString();
		}
		return message;
	}
}
