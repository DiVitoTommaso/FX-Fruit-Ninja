package application.components;

public class NewsShifter implements Runnable {

	public static final boolean LEFT_RIGHT = false;
	public static final boolean RIGHT_LEFT = true;

	private Thread thread;
	private boolean isRunning = false;
	private int start;
	private volatile int x;
	private volatile int y;
	private int addX;
	private int xLimit;
	private int speedUpdate;

	public NewsShifter(int x, int y, boolean direction, int addX, int xLimit, int speedUpdate) {

		if (speedUpdate < 0)
			throw (new IllegalArgumentException());
		this.speedUpdate = speedUpdate;
		this.start = x;
		this.x = x;
		this.y = y;
		this.addX = Math.abs(addX);
		this.xLimit=xLimit;

		if (direction)
			this.addX = -addX;

	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public synchronized void startMove() {
		if (!isRunning) {
			isRunning = true;
			thread = new Thread(this);
			thread.start();
		}

	}

	public boolean isRunning() {
		return isRunning;
	}

	public synchronized void stopMove() {
		if (isRunning) {
			isRunning = false;
		}
	}

	@Override
	public void run() {

		while (isRunning) {
			try {
				Thread.sleep(speedUpdate);
				if (x < xLimit)
					x = start;
				x += addX;
			} catch (InterruptedException e) {
			}
		}

	}

}
