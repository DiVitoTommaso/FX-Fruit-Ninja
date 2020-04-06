package application.components;

import java.util.Random;

import javafx.geometry.Rectangle2D;

public class RandomTargetShifter implements Runnable {

	private static double gravity = 9.8;
	private Thread thread;
	private Thread threadRotation;
	private final Rectangle2D range;

	private volatile Entity entity;
	private volatile boolean isRunning = false;
	private boolean interrupt = false;

	private int angle;
	private int v0x;
	private int v0y;
	private double time;
	private double angleX;
	private double angleY;

	private Object object = new Object();

	/**
	 * 
	 * @param entity entita da modificare
	 * @param xStart bounds
	 * @param yStart bounds
	 * @param xEnd   bounds
	 * @param yEnd   bounds
	 *
	 */
	public RandomTargetShifter(Entity entity, Rectangle2D range) {
		if (entity == null)
			throw (new IllegalArgumentException("entita nulla"));
		this.entity = entity;
		this.range = range;

	}

	@Override
	public void run() {

		angle = new Random().nextInt(50) + 30;
		v0x = new Random().nextInt(80) + 80;
		v0y = new Random().nextInt(60) + 80;
		time = 0;
		angleX = v0x * Math.cos(Math.toRadians(angle));
		angleY = v0y * Math.sin(Math.toRadians(angle));

		boolean side = new Random().nextBoolean();

		if (side)
			rightSide();
		else
			leftSide();

		stop();
	}

	private void rightSide() {
		entity.setX(range.getMaxX());
		entity.setY(range.getMaxY());
		do {
			time += 0.15;

			entity.setX(range.getMaxX() - angleX * time);
			entity.setY(range.getMaxY() - angleY * time + gravity * (Math.pow(time, 2) / 2));
			try {
				if (interrupt)
					synchronized (this) {
						wait();
					}
				Thread.sleep(15);
			} catch (InterruptedException e) {
				return;
			}
		} while (entity.getX() > range.getMinX() && entity.getY() < range.getMaxY() && isRunning);
		// controllo ogni volta se è compreso nell'area se va fuori inutile continuare
	}

	private void leftSide() {
		entity.setX(range.getMinX());
		entity.setY(range.getMaxY());
		do {
			time += 0.15;

			entity.setX(range.getMinX() + angleX * time);
			entity.setY(range.getMaxY() - angleY * time + gravity * (Math.pow(time, 2) / 2));

			try {
				if (interrupt)
					synchronized (this) {
						wait();
					}
				Thread.sleep(15);
			} catch (InterruptedException e) {
				return;
			}
		} while (entity.getX() < range.getMaxX() && entity.getY() < range.getMaxY() && isRunning);
		// controllo ogni volta se è compreso nell'area se va fuori inutile continuare
	}

	private void rotate() {
		while (isRunning) {
			try {
				if (interrupt)
					synchronized (this) {
						wait();
					}
				Thread.sleep(5);
				entity.addRotate(3);
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	public synchronized void start() {
		if (isRunning)
			return;
		isRunning = true;
		interrupt = false;

		thread = new Thread(this, "shifter");
		threadRotation = new Thread(this::rotate, "rotator");

		thread.start();
		threadRotation.start();

	}

	public void waitTerminate() {
		if (isRunning)
			try {
				synchronized (object) {
					object.wait();
				}
			} catch (InterruptedException e) {
			}
	}

	public synchronized void stop() {
		if (isRunning) {
			isRunning = false;
			interrupt = false;
			thread.interrupt();
			threadRotation.interrupt();
			entity.setX(-Integer.MAX_VALUE);
			synchronized (object) {
				object.notifyAll();
			}

		}
	}

	public synchronized void suspend() {
		interrupt = true;
	}

	public synchronized void resume() {
		interrupt = false;
		notifyAll();
	}

	/**
	 * get status entity
	 * 
	 * @return
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * check if is running
	 * 
	 * @return
	 */
	public boolean isRunning() {
		return isRunning;
	}

}
