package nkn6294.java.log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskRunner {
	public void runTasks(Runnable... tasks) {
		for (Runnable task : tasks) {
			exec.execute(task);
		}
	}
	
	public void shutdown() {
		exec.shutdown();
	}

	private BlockingQueue<Runnable> taskQueue = new PriorityBlockingQueue<Runnable>();

	private ThreadPoolExecutor exec = new ThreadPoolExecutor(8, 20, 10L, TimeUnit.SECONDS, taskQueue);
}