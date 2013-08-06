package com.midland.base.util.network.multitask;

public class TasksRequest {
	
	public String tag;
	public boolean isOk;
	public String code;
	public String resMsg;
	
	public TasksRequest(String tag) {
		super();
		this.tag = tag;
	}
	
	public void execute() throws Exception {
		isOk = true;
	}
	
}
