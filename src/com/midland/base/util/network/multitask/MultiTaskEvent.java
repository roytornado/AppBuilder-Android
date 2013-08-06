package com.midland.base.util.network.multitask;

import java.util.ArrayList;

public interface MultiTaskEvent {
	
	public void onMainTaskStart();
	
	public void onMainTaskEnd(TasksResult result, ArrayList<TasksRequest> requests);
	
	public void onSubTaskStart(TasksRequest request) throws Exception;
	
	public void onSubTaskEnd(TasksRequest request) throws Exception;
}
