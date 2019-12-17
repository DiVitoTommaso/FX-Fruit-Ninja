package application.components;

import javafx.scene.image.Image;

public class Entity implements Cloneable {

	private volatile double x = 0;
	private volatile double y = 0;
	private volatile double rotation = 0;
	private volatile boolean isHitted = false;
	private volatile int baseValue = 0;
	private Image hitted;
	private Image image;
	private HitBox hitBox;

	public Entity(Image image, Image hitted, HitBox hitBox, int baseValue) {
		this.image = image;
		this.hitBox = hitBox;
		this.hitted = hitted;
		this.baseValue = baseValue;
	}

	public int getBaseValue() {
		return baseValue;
	}

	public HitBox getHitBox() {
		return hitBox;
	}

	public void setHitted(boolean hit) {
		isHitted = hit;
	}

	public boolean isHitted() {
		return isHitted;
	}

	public Image getHittedImage() {
		return hitted;
	}

	public double getX() {
		return x;
	}

	public void addX(double x) {
		this.x += x;
	}

	public double getY() {
		return y;
	}

	public void addY(double y) {
		this.y += y;
	}

	public void setX(double d) {
		this.x = d;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Image getImage() {
		return image;
	}

	public Entity clone() {
		try {
			return (Entity) super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return null;
	}

	public void setRotate(double degrees) {
		rotation = degrees;
	}

	public void addRotate(double degrees) {
		rotation += degrees;
	}

	public double getRotate() {
		return rotation;
	}

	public boolean contains(int x, int y) {
		if (x > this.x && x < this.x + hitBox.getWidth())
			if (y > this.y && y < this.y + hitBox.getHeight()) {
				return true;
			}

		return false;
	}

}
