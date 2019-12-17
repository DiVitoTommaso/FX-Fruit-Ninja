package application.components;


public class ValueShifter implements Runnable {

	private Thread thread;
	private boolean isRunning = false;
	private int startValue;
	private int endValue;
	private int speedUpdate;
	private int addValue;
	
	private volatile int value;

	public ValueShifter(int startValue, int endValue, int speedUpdate, int addValue) {
		this.startValue = startValue;
		this.endValue = endValue;
		this.speedUpdate = speedUpdate;
		this.addValue = Math.abs(addValue);
		this.value=startValue;
	}
	
	public int getValue() {
		return value;
	}

	public synchronized void startSwing() {
		if(!isRunning) {
		isRunning=true;
		thread = new Thread(this);
		thread.start();
		}
	}
	
	public synchronized void stopSwing() {
		if(isRunning) {
			isRunning=false;
		}
	}

	@Override
	public void run() {
		this.value=startValue;
		boolean tmp=true;
		while(isRunning) {
			if (value > endValue)
				tmp = false;
			if (value <= startValue)
				tmp = true;
			
			if (tmp)
				value+=addValue;
			if (!tmp)
				value-=addValue;
			try {
				Thread.sleep(speedUpdate);
			} catch (InterruptedException e) {}
		}
		thread=null;

	}

}
