package com.midland.base.util;

import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskManager {
	
	private int maxConcurrent = 1;
	private LinkedBlockingDeque<ThreadTask> stack = new LinkedBlockingDeque<ThreadTask>();
	private Vector<ThreadTask> runningTasks = new Vector<ThreadTask>();
	private AtomicInteger count = new AtomicInteger();
	
	public TaskManager(int maxConcurrent) {
		this.maxConcurrent = maxConcurrent;
	}
	
	public String addTask(ThreadTask task) {
		task.id = Ran.ranStr();
		task.manager = this;
		stack.push(task);
		checkTasks();
		return task.id;
	}
	
	public synchronized void taskEnded(String id) {
		for (Iterator<ThreadTask> iterator = runningTasks.iterator(); iterator.hasNext();) {
			ThreadTask task = iterator.next();
			if (task.id.equals(id)) {
				iterator.remove();
			}
		}
		count.decrementAndGet();
		checkTasks();
	}
	
	private synchronized void checkTasks() {
		if (stack.size() > 0 && count.get() < maxConcurrent) {
			ThreadTask task = stack.pop();
			task.start();
			runningTasks.add(task);
			count.incrementAndGet();
		}
	}
	
	public void cleanTaskByViewOwner(String owner) {
		ThreadTask target = null;
		for (ThreadTask task : stack) {
			if (task.viewOwner.equals(owner)) {
				target = task;
			}
		}
		if (target != null) {
			stack.remove(target);
		}
		target = null;
		for (ThreadTask task : runningTasks) {
			if (task.viewOwner.equals(owner)) {
				target = task;
			}
		}
		if (target != null) {
			target.interrupt();
			runningTasks.remove(target);
		}
	}
	
	public abstract static class ThreadTask extends Thread {
		
		public String id;
		public String pageOwner = "";
		public String viewOwner = "";
		public TaskManager manager;
		
		@Override
		final public void run() {
			try {
				// Common.d("Task start: " + id);
				doInBackground();
				// Common.d("Task end: " + id);
			} catch (Exception e) {
				// Common.i("Thread Killed: " + id);
			} finally {
				manager.taskEnded(id);
				manager = null;
			}
		}
		
		public abstract void doInBackground() throws Exception;
		
	}
}
