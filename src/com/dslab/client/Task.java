package com.dslab.client;

public class Task {
	
	int id;
	String taskName;
	Type type;
	static int lastTaskId = 0;
	
	protected Task (String taskName, Type type) {
		this.taskName = taskName;
		this.type = type;
		lastTaskId ++;
		this.id = lastTaskId;
	}

}
