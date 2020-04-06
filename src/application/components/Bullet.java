package application.components;

public class Bullet implements Runnable {

	private volatile int bullet;
	private volatile boolean isRunning = false;
	private volatile boolean interrupt = false;
	private Thread thread;
	private int time;

	public Bullet() {
		bullet = 0;
	}

	public int getBulletStatus() {
		return bullet;
	}

	/**
	 * @param tempo di ricarica in millisecondi
	 */

	public synchronized void loadBullet(int rechargeTime) {

		if (!isRunning) {
			this.time = rechargeTime;
			interrupt = false;
			isRunning = true;
			if (rechargeTime <= 0 && isRunning)
				throw (new IllegalArgumentException());
			thread = new Thread(this);
			thread.start();
		}
	}

	public synchronized void setBulletValue(int value) {
		if (value >= 0 && value <= 100) {
			bullet = value;
			return;
		}
		bullet = 0;
	}

	/**
	 * return true se è possibile sparare (colpo carico) e spara scaricandosi nota:
	 * per risparare bisogna fare loadBullet() return false se non è possibile
	 * sparare (colpo non ancora totalmente caricato
	 * 
	 */

	public synchronized void suspend() {
		interrupt = true;
	}

	public synchronized void resume() {
		interrupt = false;
		notify();
	}

	public synchronized boolean shootBullet() {
		if (bullet == 100) {
			bullet = 0;
			return true;
		}
		return false;
	}

	public synchronized void stop() {
		if (thread != null)
			thread.interrupt();
	}

	@Override
	public void run() {
		while (bullet < 100 && isRunning) {
			bullet++;
			try {
				if (interrupt)
					synchronized (this) {
						wait();
					}
				Thread.sleep(time / 100);
			} catch (InterruptedException e) {
				break;
			}
		}
		bullet = 100;
		interrupt = false;
		isRunning = false;
	}

}
