package com.midland.base.util.network.multitask;

import java.util.ArrayList;

import android.os.AsyncTask;

import com.midland.base.util.Common;

public class MultiTaskManager {
	private MultiTaskEvent handler;
	private ArrayList<TasksRequest> tasks = new ArrayList<TasksRequest>();
	
	public MultiTaskManager(MultiTaskEvent handler) {
		super();
		this.handler = handler;
	}
	
	public void addTask(TasksRequest request) {
		tasks.add(request);
	}
	
	public void start() {
		Common.d("Task Size: " + tasks.size());
		new MainTask().execute();
	}
	
	private class MainTask extends AsyncTask<Void, Void, TasksResult> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			handler.onMainTaskStart();
		}
		
		@Override
		protected TasksResult doInBackground(Void... voids) {
			TasksResult result = new TasksResult();
			for (TasksRequest request : tasks) {
				try {
					handler.onSubTaskStart(request);
					request.execute();
					handler.onSubTaskEnd(request);
				} catch (Exception e) {
					Common.e(e);
					request.isOk = false;
				}
				Common.d(request.tag + ":" + request.isOk);
			}
			
			result.isOk = true;
			for (TasksRequest request : tasks) {
				if (!request.isOk) {
					result.isOk = false;
					result.code = request.code;
					result.resMsg = request.resMsg;
				}
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(TasksResult result) {
			super.onPostExecute(result);
			handler.onMainTaskEnd(result, tasks);
			tasks.clear();
			tasks = null;
			handler = null;
		}
		
	}
	
}
