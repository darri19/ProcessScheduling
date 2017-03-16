package com.ru.usty.scheduling;

import java.util.Comparator;

public class HRRNcomparator implements Comparator<Integer>{

	Scheduler s;
	public HRRNcomparator(Scheduler s) {
		this.s = s;
	}

	@Override
	public int compare(Integer arg0, Integer arg1) {
		long length0 = (s.processExecution.getProcessInfo(arg0).totalServiceTime+s.processExecution.getProcessInfo(arg0).elapsedWaitingTime)/s.processExecution.getProcessInfo(arg0).totalServiceTime;
		long length1 = (s.processExecution.getProcessInfo(arg1).totalServiceTime+s.processExecution.getProcessInfo(arg1).elapsedWaitingTime)/s.processExecution.getProcessInfo(arg1).totalServiceTime;
		if (length0 < length1)
        {
            return 1;
        }
        if (length1 < length0)
        {
            return -1;
        }
		return 0;
	}

}
