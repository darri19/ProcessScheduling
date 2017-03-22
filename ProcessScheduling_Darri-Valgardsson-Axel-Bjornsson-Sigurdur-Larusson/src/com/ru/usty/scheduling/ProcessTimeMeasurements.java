package com.ru.usty.scheduling;

public class ProcessTimeMeasurements {
	long arrivalTime;
	long startTime;
	long completionTime;
	
	public ProcessTimeMeasurements(){
		arrivalTime = System.currentTimeMillis();
	}
	
	void setStart(){
		startTime = System.currentTimeMillis();
	}
	
	void setCompletion(){
		completionTime = System.currentTimeMillis();
	}
	
	long turnaroundTime(){
		return completionTime - arrivalTime;
	}
	
	long responseTime(){
		return startTime - arrivalTime;
	}
}
