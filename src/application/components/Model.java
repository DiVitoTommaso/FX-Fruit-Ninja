package application.components;

import javafx.scene.control.Label;

public class Model {

	private Label position;
	private Label name;
	private Label score;
	
	
	public Model(Label position, Label name, Label score) {
		this.position = position;
		this.name = name;
		this.score = score;
	}
	public Label getPosition() {
		return position;
	}
	public void setPosition(Label position) {
		this.position = position;
	}
	public Label getName() {
		return name;
	}
	public void setName(Label name) {
		this.name = name;
	}
	public Label getScore() {
		return score;
	}
	public void setScore(Label score) {
		this.score = score;
	}
}
