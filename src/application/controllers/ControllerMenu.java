package application.controllers;

import application.GameApplication;
import application.ResourcesBundle;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import javafx.scene.layout.*;

public class ControllerMenu {

	@FXML
	private StackPane root;
	@FXML
	private Pane pane;
	@FXML
	private ImageView play;
	@FXML
	private ImageView contact;
	@FXML
	private ImageView settings;
	@FXML
	private ImageView leaderBoard;
	@FXML
	private ImageView tutorial;
	@FXML
	private TextFlow welcome;
	@FXML
	private TextFlow news;

	private ScaleTransition playButton = new ScaleTransition(Duration.millis(1000));
	private ScaleTransition welcomeText = new ScaleTransition(Duration.millis(1000));
	private TranslateTransition newsText=new TranslateTransition();
	public ControllerMenu() {

	}

	@FXML
	private void initialize() {

		// inizializzazione immagini per imageView
		play.setImage(
				new Image(getClass().getResourceAsStream("/application/res/texture/play.png"), 400, 300, false, false));
		contact.setImage(new Image(getClass().getResourceAsStream("/application/res/texture/contact.png"), 250, 150,
				false, false));
		settings.setImage(new Image(getClass().getResourceAsStream("/application/res/texture/settings.png"), 250, 150,
				false, false));
		leaderBoard.setImage(new Image(getClass().getResourceAsStream("/application/res/texture/leaderboard.png"), 400,
				400, false, false));
		tutorial.setImage(new Image(getClass().getResourceAsStream("/application/res/texture/credits.png"), 250, 150,
				false, false));
		
		newsText.setDuration(Duration.millis(50000));

		playButton.setNode(play);
		welcomeText.setNode(welcome);
		newsText.setNode(news);

		newsText.setToX(-3500);
        newsText.setInterpolator(Interpolator.LINEAR);
        
		playButton.setByY(0.2);
		playButton.setByX(0.2);
		welcomeText.setByX(0.4);
		welcomeText.setByY(0.4);
		newsText.setByX(-1);
		
		playButton.setCycleCount(-1);
		welcomeText.setCycleCount(-1);
		newsText.setCycleCount(-1);
		
		playButton.setAutoReverse(true);
		welcomeText.setAutoReverse(true);
		newsText.setAutoReverse(false);
		
		root.setBackground(new Background(new BackgroundImage(
				new Image(getClass().getResourceAsStream("/application/res/texture/background.PNG"),
						ResourcesBundle.menuWidth, ResourcesBundle.menuHeight, false, true),
				BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				BackgroundSize.DEFAULT)));

		playButton.play();
		welcomeText.play();
		newsText.play();
	}

	@FXML
	private void handleTutorial() {
		GameApplication.setTutorialScene();
	}

	@FXML
	private void handleSettings() {

		GameApplication.setSettingScene();
	}

	@FXML
	private void handlePlay() {
		GameApplication.setGameScene();
	}

	@FXML
	private void handleEmail() {
		GameApplication.setEmailScene();
	}

	@FXML
	private void handleLeaderBoard() {

		GameApplication.setLeaderboardScene();
	}

	public void draw() {
		playButton.play();
		welcomeText.play();
		newsText.play();
	}

	public void pauseDraw() {
		playButton.pause();
		welcomeText.pause();
		newsText.pause();
		
	}

}
