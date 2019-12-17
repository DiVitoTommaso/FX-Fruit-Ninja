package application.controllers;

import application.GameApplication;
import application.ResourcesBundle;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;

public class ControllerTutorial {

	@FXML
	private ImageView img;
	@FXML
	private ImageView leaderBoard;
	@FXML
	private ImageView settings;
	@FXML
	private ImageView contact;
	@FXML
	private Pane Pane;
	@FXML
	private ImageView goBack;
	@FXML
	private ImageView spada;
	@FXML
	private ImageView bersaglio;
	@FXML
	private ImageView nemico;

	public ControllerTutorial() {

	}

	@FXML
	private void initialize() {

		Pane.setBackground(new Background(new BackgroundImage(
				new Image(getClass().getResourceAsStream("/application/res/texture/background.PNG"), ResourcesBundle.menuWidth,
						ResourcesBundle.menuHeight, false, true),
				BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				BackgroundSize.DEFAULT)));

		contact.setImage(
				new Image(getClass().getResourceAsStream("/application/res/texture/contact.png")));

		settings.setImage(
				new Image(getClass().getResourceAsStream("/application/res/texture/settings.png")));

		leaderBoard.setImage(
				new Image(getClass().getResourceAsStream("/application/res/texture/leaderboard.png")));

		goBack.setImage(new Image(getClass().getResourceAsStream("/application/res/texture/back.png")));
		
		spada.setImage(new Image(getClass().getResourceAsStream("/application/res/texture/sword.png")));
		
		bersaglio.setImage(new Image(getClass().getResourceAsStream("/application/res/texture/target1.png")));
		
		nemico.setImage(new Image(getClass().getResourceAsStream("/application/res/texture/enemy.png")));

	}

	@FXML
	private void handleBackToMenu() {
		GameApplication.setMenuScene();
	}

}
