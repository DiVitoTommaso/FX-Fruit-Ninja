package application.components;

public class Timer implements Runnable {

	public interface TimerFunction {
		public void execute();
	}

	private volatile double start;
	private volatile double seconds;
	private volatile boolean isRunning = false;

	private  TimerFunction function;
	private Thread thread;

	public Timer(double startSeconds) {
		if (startSeconds <= 0)
			this.seconds = 1;
		this.start = startSeconds;
	}

	public void onTimeExpired(TimerFunction function) {
		this.function = function;
	}

	public double getTime() {
		return seconds;
	}

	@Override
	public void run() {
		while (seconds >= -0 && isRunning) {
			try {
				Thread.sleep(9,470000); // sleep 9.5ms -> lo scheduler ha un ritardo variante tra 10 e 20ms su 3m best sleep=9.5ms
				this.seconds -= 0.01;
			} catch (InterruptedException e) {
			}
		}
		if (isRunning)
			function.execute();//se ho stoppato di forza non eseguire
		
		isRunning = false;

	}

	public synchronized void start() {
		if (!isRunning) {
			isRunning = true;
			seconds = start;
			thread = new Thread(this);
			thread.start();
		}
	}

	public synchronized void stop() {
		if (isRunning) {
			isRunning = false;
			thread.interrupt();

		}
	}

}
