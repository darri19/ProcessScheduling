package com.ru.usty.scheduling;

import java.util.concurrent.TimeUnit;

public class FeedbackController implements Runnable{
	Scheduler scheduler;
	
	public FeedbackController(Scheduler s){
		this.scheduler = s;
	}
	@Override
	public void run() {
		while(true){
			boolean upperQueueNotEmpty = false;
			for(int i = 0; i < Scheduler.numOfFeedbackQueues; i++){
				while(!scheduler.feedbackQueue.get(i).isEmpty()){
					for(int j = 0; j < i; j++){
						if(!scheduler.feedbackQueue.get(j).isEmpty()){
							upperQueueNotEmpty = true;
							break;
						}
					}
					if(upperQueueNotEmpty){
						break;
					}
					doTheStuff(i);
				}
				if(upperQueueNotEmpty){
					break;
				}
			}
		}
	}
	private void doTheStuff(int i) {
		int process = scheduler.feedbackQueue.get(i).remove();
		scheduler.switchToProcess(process);
		long startTime = System.currentTimeMillis();
		while(!scheduler.finished.contains(process)){
			if(System.currentTimeMillis()>startTime + scheduler.quantum){
				break;
			}
		}
		if(!scheduler.finished.contains(process)){
			if(i<Scheduler.numOfFeedbackQueues-1){
				i++;
			}
			scheduler.feedbackQueue.get(i).add(process);

		}
		try {
			TimeUnit.MILLISECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
