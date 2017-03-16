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
		System.out.println("Scheduler constructor");
		/**
		 * Add general initialization code here (if needed)
		 */
	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void startScheduling(Policy policy, int quantum) {
		
		System.out.println("start scheduling");

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
			System.out.println("switch RR");
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
		System.out.println("Process added");
		if(!this.running){
			System.out.println("!this.running");
			processExecution.switchToProcess(processID);
			this.running = true;
			this.runningProcess = processID;
			if(this.policy == Policy.RR){
				new Waiter(this).run();
			}
		}else{
			processQueue.add(processID);
		}
		/**
		 * Add scheduling code here
		 */

	}

	public void switchProcess(Boolean pFinished) {
		System.out.println("SwitchProcess");
		if(processQueue.size() != 0){
			if(!pFinished){
				System.out.println("Adding old process back to queue");
				int oldPID = this.runningProcess;
				this.processQueue.add(oldPID);		
			}
			System.out.println("Queue not empty");
			int newPID = this.processQueue.remove();
			processExecution.switchToProcess(newPID);
			this.runningProcess = newPID;
			if(this.policy == Policy.RR){
				//new Waiter(this).run();
			}
		}else{
			System.out.println("Queue empty");
			if(pFinished){
				this.running = false;
				
			}else{
				//new Waiter(this).run();
			}
		}
		
	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void processFinished(int processID) {

		switchProcess(true);
		/**
		 * Add scheduling code here
		 */

	}
}
