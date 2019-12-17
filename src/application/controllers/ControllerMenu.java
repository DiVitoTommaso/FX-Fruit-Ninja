package application.controllers;

import application.GameApplication;
import application.ResourcesBundle;
import application.components.NewsShifter;
import application.components.ValueShifter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.scene.layout.*;

public class ControllerMenu {

	private String news = "Version:1.4.13--Custom leadearboard--New songs--New gamerules--Lag decreased-";
	private ValueShifter value;
	private NewsShifter ns;

	private GraphicsContext gc;
	private Timeline time;
	@FXML
	private StackPane root;
	@FXML
	private Pane pane;
	@FXML
	private ImageView play;
	@FXML
	private ImageView contact;
	@FXML
	private Canvas canvas;
	@FXML
	private ImageView settings;
	@FXML
	private ImageView leaderBoard;
	@FXML
	private ImageView tutorial;

	public ControllerMenu() {

	}

	@FXML
	private void initialize() {

		// inizializzazione immagini per imageView
		play.setImage(new Image(getClass().getResourceAsStream("/application/res/texture/play.png"), 400, 300, false, false));
		contact.setImage(
				new Image(getClass().getResourceAsStream("/application/res/texture/contact.png"), 250, 150, false, false));
		settings.setImage(
				new Image(getClass().getResourceAsStream("/application/res/texture/settings.png"), 250, 150, false, false));
		leaderBoard.setImage(
				new Image(getClass().getResourceAsStream("/application/res/texture/leaderboard.png"), 400, 400, false, false));
		tutorial.setImage(
				new Image(getClass().getResourceAsStream("/application/res/texture/credits.png"), 250, 150, false, false));
		
		canvas.setWidth(ResourcesBundle.menuWidth);
		canvas.setHeight(ResourcesBundle.menuHeight);
		
		root.setBackground(new Background(new BackgroundImage(
				new Image(getClass().getResourceAsStream("/application/res/texture/background.PNG"), ResourcesBundle.menuWidth,
						ResourcesBundle.menuHeight, false, true),
				BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				BackgroundSize.DEFAULT)));
	
		gc = canvas.getGraphicsContext2D();
		// creazione oggetti per permette il movimento delle news e della scritta"welcome"
		value = new ValueShifter(30, 70, 20, 1);
		ns = new NewsShifter(ResourcesBundle.menuWidth, 40, NewsShifter.RIGHT_LEFT, 1, -news.length() * 13, 28);

		value.startSwing();
		ns.startMove();
	
		time = new Timeline(new KeyFrame(Duration.millis(20), e -> drawMenu()));

		time.setCycleCount(Timeline.INDEFINITE);
		time.play();

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
		time.play();
	}

	public void stopDraw() {
		time.stop();
	}

	/**
	 * disegno news e welcome
	 */
	private void drawMenu() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.setFont(new Font("tahoma", value.getValue()));
		gc.fillText("Welcome", canvas.getWidth() / 2 - 150, 130);
		gc.setFont(new Font("tohama", 24));
		gc.setFill(Color.RED);
		gc.fillText("News:", ns.getX(), ns.getY());
		gc.setFill(Color.BLACK);
		gc.fillText(news, ns.getX() + 80, ns.getY());
	}
}
