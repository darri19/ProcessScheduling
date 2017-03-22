package com.ru.usty.scheduling;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import com.ru.usty.scheduling.process.ProcessExecution;
import com.ru.usty.scheduling.process.ProcessInfo;

public class Scheduler {

	static final int numOfFeedbackQueues = 7;
	ProcessExecution processExecution;
	Policy policy;
	int quantum;
	int runningProcess;
	ArrayList<Integer> finished;
	Thread thread;
	static Boolean isDead;
	SRTcomparator SRTComp;
	Map<Integer, ProcessTimeMeasurements> processTimes;
	
	
	Queue<Integer> processQueue;
	ArrayList<Queue<Integer>> feedbackQueue;
	Boolean running;
	/**
	 * Add any objects and variables here (if needed)
	 */


	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public Scheduler(ProcessExecution processExecution) {
		this.processExecution = processExecution;
	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void startScheduling(Policy policy, int quantum) {
		if(processTimes!=null){
			printMeasurements();
		}
		this.policy = policy;
		this.quantum = quantum;
		
		this.running = false;
		if(thread != null && thread.isAlive()){
			
			try {
				isDead = true;
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		isDead = false;
		
		this.processTimes = new HashMap<Integer, ProcessTimeMeasurements>();
		
		switch(policy) {
		case FCFS:	//First-come-first-served
			System.out.println("Starting new scheduling task: First-come-first-served");
			this.processQueue = new LinkedList<Integer>();
			break;
		case RR:	//Round robin
			System.out.println("Starting new scheduling task: Round robin, quantum = " + quantum);
			this.finished = new ArrayList<Integer>();
			this.processQueue = new LinkedList<Integer>();
			thread = new Thread(new RoundRobinController(this));
			thread.start();
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
			System.out.println("Starting new scheduling task: Highest response ratio next");
			Comparator<Integer> HRRNcomparator = new HRRNcomparator(this);
			this.processQueue = new PriorityQueue<Integer>(20, HRRNcomparator);
			break;
		case FB:	//Feedback
			System.out.println("Starting new scheduling task: Feedback, quantum = " + quantum);
			this.feedbackQueue = new ArrayList<Queue<Integer>>();
			for(int i = 0; i<numOfFeedbackQueues; i++){
				this.feedbackQueue.add(new LinkedList<Integer>());
			}
			this.finished = new ArrayList<Integer>();
			thread = new Thread(new FeedbackController(this));
			thread.start();
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
		this.processTimes.put(processID, new ProcessTimeMeasurements());
		switch(policy){
		case FCFS:
		case SPN:
		case HRRN:
			if(!this.running){
				switchToProcess(processID);
				this.running = true;
				this.runningProcess = processID;
			}else{
				processQueue.add(processID);
			}
			break;
		case RR:
			processQueue.add(processID);
			break;
		case SRT:
			if(!this.running){
				switchToProcess(processID);
				this.running = true;
				this.runningProcess = processID;
			}else{
				if(SRTComp.compare(processID, runningProcess) == -1){
					processQueue.add(runningProcess);
					switchToProcess(processID);
					this.running = true;
					this.runningProcess = processID;
				}else{
					processQueue.add(processID);
				}
			}
			break;
		case FB:
			feedbackQueue.get(0).add(processID);
			break;
		default:
			break;
		}

	}
	
	void switchToProcess(int processID) {
		processExecution.switchToProcess(processID);
		processTimes.get(processID).setStart();
	}

	long HRRNnumber(int PID){
		ProcessInfo info = processExecution.getProcessInfo(PID);
		long w = info.elapsedWaitingTime;
		long s = info.totalServiceTime;
		return (w+s)/s;
		
	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void processFinished(int processID) {
		switch(policy){
		case FCFS:
		case SPN:
		case SRT:
			if(processQueue.size() == 0){
				this.running = false;
			}else{
				int newPID = processQueue.remove();
				switchToProcess(newPID);
				this.runningProcess = newPID;
			}
			break;
		case RR:
		case FB:
			finished.add(processID);
			break;
		case HRRN:
			if(processQueue.size() == 0){
				this.running = false;
			}else{
				
				this.processQueue = new PriorityQueue<Integer>(this.processQueue);
				int newPID = processQueue.remove();
				switchToProcess(newPID);
				this.runningProcess = newPID;
			}
			break;
		default:
			break;
		}
		processTimes.get(processID).setCompletion();
		printMeasurements();
	}
	public void printMeasurements(){
		long responseTimes = 0;
		long turnaroundTimes = 0;
		for(Map.Entry<Integer, ProcessTimeMeasurements> entry: processTimes.entrySet()){
			responseTimes += entry.getValue().responseTime();
			turnaroundTimes += entry.getValue().turnaroundTime();
		}
		responseTimes/=(long)processTimes.size();
		turnaroundTimes/=(long)processTimes.size();

		System.out.println("Average response time: " + responseTimes);
		System.out.println("Average turnaround time: " + turnaroundTimes);
	}
}
