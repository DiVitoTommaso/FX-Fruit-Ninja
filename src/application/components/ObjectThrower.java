package application.components;

import java.util.Random;

public class ObjectThrower {

	public static interface ThrowerFunction{
		public void execute(RandomTargetShifter e);
	}
	private Random random = new Random();

	private int tries;
	private RandomTargetShifter[] targets;
	private RandomTargetShifter[] enemies;

	private boolean isRunning = false;

	private int maxTargetsNumber;
	private int enemiesNumber;

	private Runnable onTriesExpired;
	private ThrowerFunction onTargetsHit;
	private ThrowerFunction onEnemiesHit;

	public ObjectThrower(RandomTargetShifter[] targets, RandomTargetShifter[] enemies) {
		this.targets = targets;
		this.enemies = enemies;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public synchronized void start() {
		if (!isRunning) {
			isRunning = true;
			new Thread(this::throwObjects, "thrower").start();
		}
	}

	private void throwObjects() {
		while (isRunning) {
			for (int i = 0; i < maxTargetsNumber; i++)
				targets[random.nextInt(targets.length)].start();
			for (int i = 0; i < enemiesNumber; i++)
				enemies[i].start();

			for (RandomTargetShifter t : targets)
				t.waitTerminate();
			for (RandomTargetShifter e : enemies)
				e.waitTerminate();
		}

	}
	
	public void setOnHit(ThrowerFunction onTargetsHit,ThrowerFunction onEnemiesHit) {
		this.onTargetsHit=onTargetsHit;
		this.onEnemiesHit=onEnemiesHit;
	}

	public void resetHit() {
		for (RandomTargetShifter r : targets)
			r.getEntity().setHitted(false);
		
		for (RandomTargetShifter r : enemies)
			r.getEntity().setHitted(false);
	}

	public void checkHit(double x, double y) {
		for (RandomTargetShifter r : targets) {
			if (r.getEntity().contains((int) x, (int) y)) {
				r.getEntity().setHitted(true); // ho colpito un bersaglio?
				if(onTargetsHit != null)
					onTargetsHit.execute(r);
			}
		}

		for (RandomTargetShifter r : enemies) {
			if (r.getEntity().contains((int) x, (int) y)) {
				r.getEntity().setHitted(true);
				if(onEnemiesHit != null)
					onEnemiesHit.execute(r);
			}
		}

	}

	public void stop() {
		isRunning = false;
		for (RandomTargetShifter t : targets)
			t.stop();

		for (RandomTargetShifter e : enemies)
			e.stop();
	}

	public void suspendAll() {
		for (RandomTargetShifter t : targets)
			t.suspend();

		for (RandomTargetShifter e : enemies)
			e.suspend();
	}

	public void resumeAll() {
		for (RandomTargetShifter t : targets)
			t.resume();

		for (RandomTargetShifter e : enemies)
			e.resume();
	}

	public void setThrowableObjects(int maxTargetsNumber, int enemiesNumber) {
		this.maxTargetsNumber = maxTargetsNumber;
		this.enemiesNumber = enemiesNumber;
	}

	public void setTries(int tries) {
		this.tries = tries;
	}

	public synchronized void decreaseTries() {
		if (tries == 0) {
			stop();
			if (onTriesExpired != null) {
				onTriesExpired.run();
				return;
			}
		}
		tries--;

	}

	public int getRemainingTries() {
		return tries;
	}

	public void setOnTriesExpire(Runnable runnable) {
		this.onTriesExpired = runnable;
	}
}
