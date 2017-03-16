package com.ru.usty.scheduling;

import java.util.Comparator;

public class SRTcomparator implements Comparator<Integer> {

	Scheduler s;
	public SRTcomparator(Scheduler s) {
		this.s = s;
	}

	@Override
	public int compare(Integer arg0, Integer arg1) {
		long length0 = s.processExecution.getProcessInfo(arg0).totalServiceTime-s.processExecution.getProcessInfo(arg0).elapsedExecutionTime;
		long length1 = s.processExecution.getProcessInfo(arg1).totalServiceTime-s.processExecution.getProcessInfo(arg1).elapsedExecutionTime;
		if (length0 < length1)
        {
            return -1;
        }
        if (length1 < length0)
        {
            return 1;
        }
		return 0;
	}

}
