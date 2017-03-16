package com.ru.usty.scheduling;

import java.util.concurrent.TimeUnit;

public class RoundRobinController implements Runnable{

	Scheduler scheduler;
	
	public RoundRobinController(Scheduler s){
		this.scheduler = s;
	}
	@Override
	public void run() {
		while(true){
			if(!scheduler.processQueue.isEmpty()){
				int process = scheduler.processQueue.remove();
				scheduler.processExecution.switchToProcess(process);
				long startTime = System.currentTimeMillis();
				while(!scheduler.finished.contains(process)){
					if(System.currentTimeMillis()>startTime + scheduler.quantum){
						break;
					}
				}
				if(!scheduler.finished.contains(process)){
					scheduler.processQueue.add(process);

				}
			}else{
				try {
					TimeUnit.MILLISECONDS.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			if(Scheduler.isDead){
				return;
			}
		}
		
	}

}
