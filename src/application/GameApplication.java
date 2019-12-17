package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;

import application.controllers.ControllerGame;
import application.controllers.ControllerMenu;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;

public class GameApplication extends Application {

	private static Stage stage = new Stage();
	private static Stage emailStage = new Stage();
	private static Stage leaderboardStage = new Stage();
	private static Scene tutorialScene = null;
	private static Scene menuScene = null;
	private static Scene settingsScene = null;
	private static Scene leaderboardScene = null;
	private static Scene emailScene = null;
	private static Scene gameScene = null;
	private static ControllerGame controllerGame = null;
	private static ControllerMenu controllerMenu = null;

	@Override
	public void start(Stage primaryStage) {

		try {
			// caricamento musica in memoria
			ResourcesBundle.getMusicPlayer().putGameSound(getClass().getResource("res/sounds/end.mp3"), "end");
			ResourcesBundle.getMusicPlayer().putGameSound(getClass().getResource("res/sounds/recharge.mp3"), "recharge");
			ResourcesBundle.getMusicPlayer().putGameSound(getClass().getResource("res/sounds/hittedTarget.mp3"),
					"hittedTarget");
			ResourcesBundle.getMusicPlayer().putGameSound(getClass().getResource("res/sounds/enemy.mp3"), "enemy");

			ResourcesBundle.getMusicPlayer().putGameMusic(getClass().getResource("res/music/DieForYou.mp3"),
					"Starset - Die For You ");
			ResourcesBundle.getMusicPlayer().putGameMusic(getClass().getResource("res/music/HeroesTonight.mp3"),
					"Janji - Heroes Tonight");
			ResourcesBundle.getMusicPlayer().putGameMusic(getClass().getResource("res/music/InMyMind.mp3"),
					"D'Agostino - In My Mind");
			ResourcesBundle.getMusicPlayer().putGameMusic(getClass().getResource("res/music/LetItDie.mp3"),
					" Starset - Let It Die");
			ResourcesBundle.getMusicPlayer().putGameMusic(getClass().getResource("res/music/Shine.mp3"),
					"Spektrem - Shine");
			ResourcesBundle.getMusicPlayer().putGameMusic(getClass().getResource("res/music/TetrixBassVeela.mp3"),
					"The light - Tetrix bass feat. veela ");
			ResourcesBundle.getMusicPlayer().putGameMusic(getClass().getResource("res/music/TheNight.mp3"),
					"Avicii - The Nights ");
			ResourcesBundle.getMusicPlayer().putGameMusic(getClass().getResource("res/music/WhateverItTakes.mp3"),
					"Imagine Dragons - Whatever It Takes");

			FXMLLoader loaderMenu = new FXMLLoader(getClass().getResource("controllers/fxml/Menu.fxml"));
			FXMLLoader loaderSettings = new FXMLLoader(getClass().getResource("controllers/fxml/Settings.fxml"));
			FXMLLoader loaderGame = new FXMLLoader(getClass().getResource("controllers/fxml/Game.fxml"));
			FXMLLoader loaderLeaderboard = new FXMLLoader(getClass().getResource("controllers/fxml/Leaderboard.fxml"));
			FXMLLoader loaderEmail = new FXMLLoader(getClass().getResource("controllers/fxml/Mail.fxml"));
			gameScene = new Scene(loaderGame.load(), ResourcesBundle.menuWidth, ResourcesBundle.menuHeight);
			menuScene = new Scene(loaderMenu.load(), ResourcesBundle.menuWidth, ResourcesBundle.menuHeight);
			settingsScene = new Scene(loaderSettings.load(), ResourcesBundle.menuWidth, ResourcesBundle.menuHeight);
			leaderboardScene = new Scene(loaderLeaderboard.load(), ResourcesBundle.popupWidth,
					ResourcesBundle.popupHeight);
			emailScene = new Scene(loaderEmail.load(), ResourcesBundle.popupWidth, ResourcesBundle.popupHeight);
			tutorialScene = new Scene(FXMLLoader.load(getClass().getResource("controllers/fxml/Tutorial.fxml")));
			// caricamento controllers
			controllerMenu = loaderMenu.getController();
			controllerGame = loaderGame.getController();
			// caricamento vari css
			menuScene.getStylesheets().add(getClass().getResource("controllers/css/menu.css").toExternalForm());
			settingsScene.getStylesheets().add(getClass().getResource("controllers/css/settings.css").toExternalForm());
			emailScene.getStylesheets().add(getClass().getResource("controllers/css/mail.css").toExternalForm());
			leaderboardScene.getStylesheets()
					.add(getClass().getResource("controllers/css/leaderboard.css").toExternalForm());
			// settaggio grandezze varie stage immagini titolo ecc
			stage = primaryStage;
			stage.setWidth(ResourcesBundle.menuWidth);
			stage.setHeight(ResourcesBundle.menuHeight);
			stage.setResizable(false);
			stage.setTitle("Fruit ninja");

			stage.getIcons().add(new Image(getClass().getResourceAsStream("res/texture/target1.png")));
			// carico in "cache" la scena almeno non scatta la prima volta che si clicca su
			// play
			stage.setScene(gameScene);

			stage.setScene(menuScene);

			// assegnazione handle di chiusura quando il programma viene chiuso
			// si prova a salvare la leaderboard sul txt se non è possibile salvarla
			// appare un alert
			stage.setOnCloseRequest(e -> {
				stage.hide();
				try {
					ObjectOutputStream file = new ObjectOutputStream(
							new FileOutputStream(new File(getClass().getResource("/application/res/leaderboard.txt").toURI())));
					file.writeObject(ResourcesBundle.getLeaderboard());
					file.close();
					System.exit(0);
				} catch (IOException | URISyntaxException e1) {
					Alert l = new Alert(AlertType.ERROR);
					l.show();
					l.setTitle("Errore");
					l.setHeaderText("impossibile salvare la leaderboard");
					l.setContentText("File mancante o corrotto");
					l.setOnCloseRequest(dialog -> System.exit(0));
				}

			});

			emailStage.setResizable(false);
			emailStage.setWidth(ResourcesBundle.popupWidth);
			emailStage.setHeight(ResourcesBundle.popupHeight);
			emailStage.getIcons().add(new Image(getClass().getResourceAsStream("res/texture/contact.png")));
			emailStage.setTitle("Contact us");
			emailStage.setScene(emailScene);

			leaderboardStage.setResizable(false);
			leaderboardStage.setWidth(ResourcesBundle.popupWidth);
			leaderboardStage.setHeight(ResourcesBundle.popupHeight);
			leaderboardStage.getIcons()
					.add(new Image(getClass().getResourceAsStream("res/texture/leaderboard.png")));
			leaderboardStage.setTitle("Leaderboard");
			leaderboardStage.setScene(leaderboardScene);

			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
			Alert l = new Alert(AlertType.ERROR);
			l.setTitle("Errore");
			l.setHeaderText("risorse mancanti");
			l.setContentText(
					"non è stato possibile trovare tutte le risorse necessarie\n potrebbero verificarsi degli errori");
			l.show();
		}
	}

	/**
	 * set scena menu sullo stage principale
	 */
	public static void setMenuScene() {
		stage.setAlwaysOnTop(false);
		stage.setScene(menuScene);
		controllerMenu.draw();
	}

	/**
	 * set scena setting su stage principale
	 */
	public static void setSettingScene() {
		stage.setScene(settingsScene);
		controllerMenu.stopDraw();
	}

	/**
	 * visualizzazione stage per invio dell'email con scena caricata in prec (da
	 * risolvere problemi relativi al blocco della mail da parte del firewall)
	 */
	public static void setEmailScene() {
		emailStage.show();
	}

	/**
	 * visualizzazione stage leaderboard con scena caricata in prec
	 */
	public static void setLeaderboardScene() {
		leaderboardStage.show();
	}

	public static void setTutorialScene() {
		stage.setScene(tutorialScene);
		controllerMenu.stopDraw();
	}

	/**
	 * set scena game e starting del game
	 */
	public static void setGameScene() {
		stage.setAlwaysOnTop(true);
		stage.setScene(gameScene);
		controllerMenu.stopDraw();
		controllerGame.startGame();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
