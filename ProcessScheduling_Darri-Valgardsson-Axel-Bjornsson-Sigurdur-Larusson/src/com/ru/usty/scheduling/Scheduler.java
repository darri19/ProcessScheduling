package com.ru.usty.scheduling;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import com.ru.usty.scheduling.process.ProcessExecution;

public class Scheduler {

	ProcessExecution processExecution;
	Policy policy;
	int quantum;
	int runningProcess;
	ArrayList<Integer> finished;
	Thread robinThread;
	static Boolean isDead;
	SRTcomparator SRTComp;
	
	
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
		if(robinThread != null && robinThread.isAlive()){
			
			try {
				isDead = true;
				robinThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		isDead = false;

		/**
		 * Add general initialization code here (if needed)
		 */

		switch(policy) {
		case FCFS:	//First-come-first-served
			System.out.println("Starting new scheduling task: First-come-first-served");
			this.processQueue = new LinkedList<Integer>();
			break;
		case RR:	//Round robin
			System.out.println("Starting new scheduling task: Round robin, quantum = " + quantum);
			this.finished = new ArrayList<Integer>();
			this.processQueue = new LinkedList<Integer>();
			robinThread = new Thread(new RoundRobinController(this));
			robinThread.start();
			break;
		case SPN:	//Shortest process next
			System.out.println("Starting new scheduling task: Shortest process next");
			Comparator<Integer> SPNcomparator = new SPNcomparator(this);
			this.processQueue = new PriorityQueue<Integer>(20, SPNcomparator);
			break;
		case SRT:	//Shortest remaining time
			System.out.println("Starting new scheduling task: Shortest remaining time");
			SRTComp = new SRTcomparator(this);
			this.processQueue = new PriorityQueue<Integer>(20, SRTComp);
			break;
		case HRRN:	//Highest response ratio next
			Comparator<Integer> HRRNcomparator = new SPNcomparator(this);
			this.processQueue = new PriorityQueue<Integer>(20, HRRNcomparator);
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
		switch(policy){
		case FCFS:
		case SPN:
		case HRRN:
			if(!this.running){
				processExecution.switchToProcess(processID);
				this.running = true;
				this.runningProcess = processID;
			}else{
				processQueue.add(processID);
			}
			break;
		case RR:
			break;
		case SRT:
			if(!this.running){
				processExecution.switchToProcess(processID);
				this.running = true;
				this.runningProcess = processID;
			}else{
				System.out.println("Running process: " + runningProcess);
				System.out.println("new process: " + processID);
				if(SRTComp.compare(processID, runningProcess) == -1){
					processQueue.add(runningProcess);
					processExecution.switchToProcess(processID);
					this.running = true;
					this.runningProcess = processID;
				}else{
					processQueue.add(processID);
				}
			}
		default:
			break;
		}

	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void processFinished(int processID) {
		switch(policy){
		case FCFS:
		case SPN:
		case SRT:
		case HRRN:
			if(processQueue.size() == 0){
				this.running = false;
			}else{
				int newPID = processQueue.remove();
				processExecution.switchToProcess(newPID);
				this.runningProcess = newPID;
			}
			break;
		case RR:
			finished.add(processID);
			break;
		default:
			break;
		}
	}
}
