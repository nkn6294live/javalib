package nkn6294.java.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutorCommon {

	public static void scheduleExecutor(Runnable runnable) {
		ScheduledExecutorService logExecutor;
		logExecutor = Executors.newScheduledThreadPool(1);
		logExecutor.scheduleAtFixedRate(runnable, 2000, 2000, TimeUnit.MILLISECONDS);
	}
	
	public static boolean newFixedThreadPool() throws InterruptedException, ExecutionException {
		ExecutorService activePool;
		activePool = Executors.newFixedThreadPool(10);
		Callable<Boolean> callable = new Callable<Boolean>() {
			
			@Override
			public Boolean call() throws Exception {
				return null;
			}
		};
		//Async
		boolean result = activePool.submit(callable).get();
		return result;
	}
}
