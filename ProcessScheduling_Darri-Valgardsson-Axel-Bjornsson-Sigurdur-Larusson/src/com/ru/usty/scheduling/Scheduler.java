package com.ru.usty.scheduling;

import java.util.LinkedList;
import java.util.Queue;

import com.ru.usty.scheduling.process.ProcessExecution;

public class Scheduler {

	ProcessExecution processExecution;
	Policy policy;
	int quantum;
	int runningProcess;
	
	Queue<Integer> processQueue;
	Boolean running;
	/**
	 * Add any objects and variables here (if needed)
	 */


	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public Scheduler(ProcessExecution processExecution) {
		this.processExecution = processExecution;

		/**
		 * Add general initialization code here (if needed)
		 */
	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void startScheduling(Policy policy, int quantum) {

		this.policy = policy;
		this.quantum = quantum;
		
		this.running = false;

		/**
		 * Add general initialization code here (if needed)
		 */

		switch(policy) {
		case FCFS:	//First-come-first-served
			System.out.println("Starting new scheduling task: First-come-first-served");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			this.processQueue = new LinkedList<Integer>();
			break;
		case RR:	//Round robin
			System.out.println("Starting new scheduling task: Round robin, quantum = " + quantum);
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			this.processQueue = new LinkedList<Integer>();
			break;
		case SPN:	//Shortest process next
			System.out.println("Starting new scheduling task: Shortest process next");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case SRT:	//Shortest remaining time
			System.out.println("Starting new scheduling task: Shortest remaining time");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case HRRN:	//Highest response ratio next
			System.out.println("Starting new scheduling task: Highest response ratio next");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case FB:	//Feedback
			System.out.println("Starting new scheduling task: Feedback, quantum = " + quantum);
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		}

		/**
		 * Add general scheduling or initialization code here (if needed)
		 */

	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void processAdded(int processID) {
		
		if(!this.running){
			processExecution.switchToProcess(processID);
			this.running = true;
			if(this.policy == Policy.RR){
				timer();
			}
		}else{
			processQueue.add(processID);
		}
		/**
		 * Add scheduling code here
		 */

	}

	private void timer() {
		try {
			Thread.sleep(this.quantum);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		switchProcess();
		
	}

	private void switchProcess() {
		
		if(processQueue.size() != 0){
			int oldPID = this.runningProcess;
			this.processQueue.add(oldPID);
			int newPID = this.processQueue.remove();
			processExecution.switchToProcess(newPID);
			this.runningProcess = newPID;
			if(this.policy == Policy.RR){
				timer();
			}
		}else{
			this.running = false;
		}
		
	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void processFinished(int processID) {

		switchProcess();
		/**
		 * Add scheduling code here
		 */

	}
}
