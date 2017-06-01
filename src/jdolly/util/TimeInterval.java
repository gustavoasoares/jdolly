package jdolly.util;

public final class TimeInterval {

	private long timeAfter;
	private long timeBefore;
	
	public TimeInterval(final long timeBefore, final long timeAfter) {
		this.timeBefore = timeBefore;
		this.timeAfter = timeAfter;
	}
	
	public String intervalInSecsToStr(){
		long intervalInSec = getIntervalInSecs();
		return intervalInSec + " seconds";
	}

	public long getIntervalInSecs() {
		long intervalInSec = (timeAfter - timeBefore) / 1000;
		return intervalInSec;
	}
}
