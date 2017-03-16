package com.ru.usty.scheduling;

public class Waiter implements Runnable{
	
	private Scheduler s;
	
	public Waiter(Scheduler s){
		this.s = s;
	}
	
	@Override
	public void run() {
		System.out.println("WAITER Timer before wait");
		try {
			Thread.sleep(s.quantum);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("WAITER Timer after wait");
		s.switchProcess(false);
		
	}

}
