package application.components;

public class Timer implements Runnable {

	public interface TimerFunction {
		public void execute();
	}

	private volatile double start;
	private volatile double seconds;
	private volatile boolean isRunning = false;
	private volatile boolean interrupt = false;
	private TimerFunction function;
	private Thread thread;

	public Timer(double startSeconds) {
		if (startSeconds <= 0)
			this.seconds = 1;
		this.start = startSeconds;
	}

	public void onTimeExpired(TimerFunction function) {
		this.function = function;
	}

	public synchronized double getTime() {
		return seconds;
	}

	public synchronized boolean isPaused() {
		return interrupt;
	}

	@Override
	public void run() {
		while (seconds >= 0 && isRunning) {
			try {
				if (interrupt)
					synchronized (this) {
						wait();
					}
				Thread.sleep(9, 470000); // sleep 9.5ms -> lo scheduler ha un ritardo variante tra 10 e 20ms su 3m best
											// sleep=9.5ms
				this.seconds -= 0.01;
			} catch (InterruptedException e) {
				break;
			}
		}
		seconds = 0;
		if (isRunning)
			function.execute();// se ho stoppato di forza non eseguire

		isRunning = false;

	}

	public synchronized void start() {
		if (!isRunning) {
			isRunning = true;
			seconds = start;
			interrupt = false;
			thread = new Thread(this);
			thread.start();
		}
	}

	public synchronized void suspend() {
		interrupt = true;
	}

	public synchronized void resume() {
		interrupt = false;
		notify();
	}

	public synchronized void stop() {
		if (isRunning) {
			thread.interrupt();

		}
	}

}
