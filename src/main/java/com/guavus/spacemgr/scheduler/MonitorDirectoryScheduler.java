package com.guavus.spacemgr.scheduler;

import com.guavus.spacemgr.processing.MonitorDirectoryTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.Lifecycle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 04-06-2020
 */
public class MonitorDirectoryScheduler implements Lifecycle {

	private static final Logger LOG = LoggerFactory.getLogger(MonitorDirectoryScheduler.class);

	private final ExecutorService executor;
	private final ScheduledExecutorService scheduledExecutorService;
	private final long interval;
	private final long delay;
	private volatile boolean running;
	private MonitorDirectoryTask monitorDirectoryTask;

	public MonitorDirectoryScheduler(MonitorDirectoryTask monitorDirectoryTask, Supplier<ExecutorService> executorSupplier,
			long interval, long delay) {
		this.monitorDirectoryTask = monitorDirectoryTask;
		this.executor = executorSupplier.get();
		this.interval = interval;
		this.delay = delay;
		this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
	}

	/**
	 * Schedules a task to clean directories. If already started has no effect.
	 */
	@Override
	public synchronized void start() {
		if (!running) {
			scheduledExecutorService.scheduleAtFixedRate(monitorDirectoryTask, delay, interval, TimeUnit.SECONDS);
			running = true;
		}
	}

	/**
	 * Cancels the scheduled task. This method blocks until all ongoing tasks
	 * have been completed.
	 */
	@Override
	public void stop() {
		scheduledExecutorService.shutdown();
		executor.shutdown();
		while (!executor.isShutdown()) {
			try {
				LOG.info("Waiting for ongoing Tasks to complete...");
				executor.awaitTermination(5, TimeUnit.SECONDS);
			} catch (InterruptedException ignore) {
				LOG.info("Got interrupted while waiting for ongoing Tasks to complete");
			}
		}
		running = false;
	}

	@Override
	public boolean isRunning() {
		return running;
	}
}
