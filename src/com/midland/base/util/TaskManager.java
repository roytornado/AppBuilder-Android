package com.midland.base.util;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskManager {

    private int maxConcurrent = 1;
    private Vector<ThreadTask> stack = new Vector<ThreadTask>();
    private Vector<ThreadTask> runningTasks = new Vector<ThreadTask>();
    private AtomicInteger count = new AtomicInteger();

    public TaskManager(int maxConcurrent) {
        this.maxConcurrent = maxConcurrent;
    }

    public String addTask(ThreadTask task) {
        task.id = Ran.ranStr();
        task.manager = this;
        stack.add(task);
        checkTasks();
        return task.id;
    }

    public void taskEnded(String id) {
        synchronized (runningTasks) {
            ThreadTask toRemove = null;
            for (ThreadTask task : runningTasks) {
                if (task.id.equals(id)) {
                    toRemove = task;
                }
            }
            if (toRemove != null) {
                runningTasks.remove(toRemove);
            }

        }
        count.decrementAndGet();
        checkTasks();
    }

    private synchronized void checkTasks() {
        if (stack.size() > 0 && count.get() < maxConcurrent) {
            ThreadTask task = stack.get(stack.size() - 1);
            stack.remove(stack.size() - 1);
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
        synchronized (runningTasks) {
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
    }

    public abstract static class ThreadTask extends Thread {

        public String id;
        public String pageOwner = "";
        public String viewOwner = "";
        public TaskManager manager;

        @Override
        final public void run() {
            try {
                //Common.d("Task start: " + id);
                doInBackground();
                //Common.d("Task end: " + id);
            } catch (Exception e) {
                //Common.d("Thread Killed: " + id);
            } finally {
                manager.taskEnded(id);
                manager = null;
            }
        }

        public abstract void doInBackground() throws Exception;

    }
}
