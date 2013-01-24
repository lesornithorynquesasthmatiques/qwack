package org.lesornithorynquesasthmatiques.batch;

import java.util.concurrent.Phaser;

/**
 * A small wrapper around a {@link Phaser} to control task submission and
 * completion. Allows for the caller to wait until all tasks are reported
 * complete.
 * 
 * @see #awaitUntilAllPendingTasksCompleted()
 * 
 */
public class TaskSynchronizer {

	private final Phaser phaser = new Phaser(1);

	/**
	 * Signals that a task is about to be submitted. Such a task must then be
	 * signaled completed by calling {@link #onAfterTaskCompleted()}.
	 */
	public void onBeforeTaskSubmitted() {
		phaser.register();
	}

	/**
	 * Signals that a task has been completed.
	 */
	public void onAfterTaskCompleted() {
		phaser.arriveAndDeregister();
	}

	/**
	 * Awaits until all tasks signaled to this Synchronizer via
	 * {@link #onBeforeTaskSubmitted()} are reported complete by subsequent
	 * calls to {@link #onAfterTaskCompleted()}.
	 * 
	 * @throws InterruptedException
	 */
	public void awaitUntilAllPendingTasksCompleted() throws InterruptedException {
		phaser.awaitAdvanceInterruptibly(phaser.arrive());
	}
}
