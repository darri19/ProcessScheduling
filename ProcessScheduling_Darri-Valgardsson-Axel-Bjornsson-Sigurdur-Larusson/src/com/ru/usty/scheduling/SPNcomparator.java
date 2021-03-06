package com.ru.usty.scheduling;

import java.util.Comparator;

public class SPNcomparator implements Comparator<Integer>{
	Scheduler s;
	
	public SPNcomparator(Scheduler s){
		this.s = s;
	}

	@Override
	public int compare(Integer arg0, Integer arg1) {
		long length0 = s.processExecution.getProcessInfo(arg0).totalServiceTime;
		long length1 = s.processExecution.getProcessInfo(arg1).totalServiceTime;
		
		if (length0 < length1){
            return -1;
        }else if(length1 < length0)
        {
            return 1;
        }
		return 0;
	}

}
