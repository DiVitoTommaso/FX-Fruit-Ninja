package application.controllers;

import java.text.DecimalFormat;
import java.util.Random;

import application.GameApplication;
import application.ResourcesBundle;
import application.components.Bullet;
import application.components.Entity;
import application.components.HitBox;
import application.components.RandomTargetShifter;
import application.components.Timer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class ControllerGame {

	private static double trackVolume = 0.5;
	private static double gameVolume = 1;
	private static int difficulty = 3;
	private static String gameMusic;

	private Image spada;
	private Image spadaLanciata;

	private RandomTargetShifter[] targets = new RandomTargetShifter[12];
	private RandomTargetShifter[] enemies = new RandomTargetShifter[4];

	private Timer timer = new Timer(181); // piccolo lag 1 in piu
	private Bullet bullet = new Bullet();
	private Random random = new Random();
	private DecimalFormat df = new DecimalFormat("0.00");

	private int numeroMassimoBersagli;
	private int numeroNemici;

	private boolean isRunning;

	private int score;
	private int numeroNemiciColpiti;
	private double moltiplicatore;
	private double maxErrors;

	private double mouseX;
	private double mouseY;

	private Timeline timeLine;
	private Thread lanciatore;
	private GraphicsContext gc;
	@FXML
	private Canvas canvas;
	@FXML
	private StackPane root;
	@FXML
	private TextField text;
	@FXML
	private Label info2;
	@FXML
	private GridPane pane;
	@FXML
	private Label info;
	@FXML
	private Button conferma;
	@FXML
	private ImageView backToMenu;

	private Entity entity1 = new Entity(
			new Image(getClass().getResourceAsStream("/application/res/texture/target1.png")),
			new Image(getClass().getResourceAsStream("/application/res/texture/macchiaGialla.png")),
			new HitBox(180, 150), 1);
	private Entity entity2 = new Entity(
			new Image(getClass().getResourceAsStream("/application/res/texture/target2.png")),
			new Image(getClass().getResourceAsStream("/application/res/texture/macchiaArancione.png")),
			new HitBox(145, 110), 2);
	private Entity entity3 = new Entity(
			new Image(getClass().getResourceAsStream("/application/res/texture/target3.png")),
			new Image(getClass().getResourceAsStream("/application/res/texture/macchiaRossa.png")), new HitBox(120, 70),
			3);
	private Entity enemy = new Entity(new Image(getClass().getResourceAsStream("/application/res/texture/enemy.png")),
			null, new HitBox(120, 120), 0);

	public ControllerGame() {

	}

	@FXML
	private void initialize() {
		// inizializzazione scena
		isRunning = false;
		spada = new Image(getClass().getResourceAsStream("/application/res/texture/sword.png"));
		spadaLanciata = new Image(getClass().getResourceAsStream("/application/res/texture/brokenSword.png"));

		canvas.setWidth(ResourcesBundle.menuWidth);
		canvas.setHeight(ResourcesBundle.menuHeight);

		root.setBackground(new Background(new BackgroundImage(
				new Image(getClass().getResourceAsStream("/application/res/texture/background.PNG"),
						ResourcesBundle.menuWidth, ResourcesBundle.menuHeight, false, true),
				BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				BackgroundSize.DEFAULT)));

		gc = canvas.getGraphicsContext2D();
		gc.setFont(Font.font("verdana", FontWeight.BOLD, 24));

		timeLine = new Timeline(new KeyFrame(Duration.millis(16), e -> draw()));

		timeLine.setCycleCount(Timeline.INDEFINITE);

		backToMenu.setImage(
				new Image(getClass().getResourceAsStream("/application/res/texture/back.png"), 50, 50, false, false));
		timer.onTimeExpired(this::stop);

	}

	public void startGame() {
		// -----setup gioco------------//
		initializeWindow();

		new Thread(this::initializeGameEngine).start();

	}

	private void initializeWindow() {
		// immagine cursore
		root.setCursor(new ImageCursor(new Image(getClass().getResourceAsStream("/application/res/texture/cursor.png")),
				500, 500));
		// rimuovo componenti della precedente partita
		text.setVisible(false);
		info.setVisible(false);
		conferma.setVisible(false);
		info2.setVisible(false);
	}

	private void initializeGameEngine() {
		// leggo i valori del volume della musica selezionati e li imposto
		ResourcesBundle.getMusicPlayer().setVolumeMusic(gameMusic, trackVolume);
		ResourcesBundle.getMusicPlayer().setVolumeSound("recharge", gameVolume);
		ResourcesBundle.getMusicPlayer().setVolumeSound("end", gameVolume);
		ResourcesBundle.getMusicPlayer().setVolumeSound("enemy", gameVolume);
		ResourcesBundle.getMusicPlayer().setVolumeSound("hittedTarget", gameVolume);
		// resetto le variabili
		score = 0;
		isRunning = true;
		numeroNemiciColpiti = 0;
		mouseX = -Integer.MAX_VALUE;
		mouseY = -Integer.MAX_VALUE;
		entity1.setX(-Integer.MAX_VALUE);
		entity2.setX(-Integer.MAX_VALUE);
		entity3.setX(-Integer.MAX_VALUE);
		enemy.setX(-Integer.MAX_VALUE);

		// creo un thread incaricato di controllare se ci sono oggetti lanciati
		// se ci sono oggetti (buoni) in fase di lancio non fa niente se non ci sono ne
		// lancia altri
		lanciatore = new Thread(() -> {

			while (isRunning) {

				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}
				// controllo se il tempo è finito o sono stati colpiti troppi nemici
				if (numeroNemiciColpiti > maxErrors)
					stop();
				// lancio altro se tutti i bersagli sono fermi
				boolean targetInmovimento = false;

				for (int i = 0; i < targets.length; i++)
					if (targets[i].isRunning())
						targetInmovimento = true;

				for (int i = 0; i < enemies.length; i++)
					if (enemies[i].isRunning())
						targetInmovimento = true;

				if (!targetInmovimento) {
					for (int i = 0; i < numeroMassimoBersagli; i++)
						targets[random.nextInt(targets.length)].start();

					for (int i = 0; i < numeroNemici; i++)
						enemies[i].start();

				}
			}

		});

		// in base alla difficolta creo i bersagli
		switch (difficulty) {
		case 1:

			numeroMassimoBersagli = 1;
			numeroNemici = 1;
			maxErrors = 3;
			moltiplicatore = 1;
			break;
		case 2:
			numeroMassimoBersagli = 2;
			numeroNemici = 2;
			maxErrors = 2;
			moltiplicatore = 2;
			break;

		case 3:
			numeroMassimoBersagli = 3;
			numeroNemici = 3;
			maxErrors = 2;
			moltiplicatore = 4;
			break;

		case 4:
			numeroMassimoBersagli = 4;
			numeroNemici = 3;
			maxErrors = 1;
			moltiplicatore = 6;
			break;

		case 5:
			numeroMassimoBersagli = 5;
			numeroNemici = 4;
			maxErrors = 1;
			moltiplicatore = 8;
			break;

		case 6:
			numeroMassimoBersagli = 6;
			numeroNemici = 4;
			maxErrors = 0;
			moltiplicatore = 10;
			break;
		}
		Rectangle2D range = new Rectangle2D(-50, -50, (int) canvas.getWidth(), (int) canvas.getHeight());
		// metto i bersagli dentro l'oggetto per farle muovere
		targets[0] = new RandomTargetShifter(entity1.clone(), range);
		targets[1] = new RandomTargetShifter(entity1.clone(), range);
		targets[2] = new RandomTargetShifter(entity1.clone(), range);
		targets[3] = new RandomTargetShifter(entity1.clone(), range);
		targets[4] = new RandomTargetShifter(entity2.clone(), range);
		targets[5] = new RandomTargetShifter(entity2.clone(), range);
		targets[6] = new RandomTargetShifter(entity2.clone(), range);
		targets[7] = new RandomTargetShifter(entity2.clone(), range);
		targets[8] = new RandomTargetShifter(entity3.clone(), range);
		targets[9] = new RandomTargetShifter(entity3.clone(), range);
		targets[10] = new RandomTargetShifter(entity3.clone(), range);
		targets[11] = new RandomTargetShifter(entity3.clone(), range);

		enemies[0] = new RandomTargetShifter(enemy.clone(), range);
		enemies[1] = new RandomTargetShifter(enemy.clone(), range);
		enemies[2] = new RandomTargetShifter(enemy.clone(), range);
		enemies[3] = new RandomTargetShifter(enemy.clone(), range);

		ResourcesBundle.getMusicPlayer().playMusic(gameMusic);

		timeLine.play();
		timer.start();
		lanciatore.start();

	}

	private void stop() {
		// stoppo il gioco mostro la schermata di inserimento del nome col relativo
		// punteggio
		new Thread(() -> {
			ResourcesBundle.getMusicPlayer().playSound("end");
			ResourcesBundle.getMusicPlayer().stopMusic(gameMusic);

			timer.stop();
			timeLine.stop();
			isRunning = false;

			Platform.runLater(() -> {

				info.setText("You scored " + score + " points");
				root.setCursor(Cursor.DEFAULT);
				info.setVisible(true);
				info2.setVisible(true);
				text.setVisible(true);
				conferma.setVisible(true);

			});
		}).start();
	}

	@FXML
	private void handleConferma() {

		ResourcesBundle.getMusicPlayer().stopSound("end");
		if (!text.getText().isEmpty())
			ResourcesBundle.getLeaderboard().put(text.getText(), score);
		GameApplication.setMenuScene();
	}

	@FXML
	private void handleClear() {
		text.clear();
	}

	@FXML
	private void handleRipristino() {
		conferma.setStyle("-fx-font-size:24");
	}

	@FXML
	private void handleColore() {
		conferma.setStyle("-fx-background-color:green ; -fx-font-size:24");
	}

	@FXML
	private void handleColpisci(MouseEvent event) {

		// creo un thread per evitare lag nella view
		new Thread(() -> {
			// se il colpo è carico
			// salvo la posizione del colpo
			// guardo se ha colpito qualcosa
			// aumento o dimuisco lo score e imposto il tempo di ricarica per il prossimo
			// colpo
			if (bullet.shootBullet()) {
				int reloadTime = 500;
				for (int i = 0; i < targets.length; i++)
					targets[i].getEntity().setHitted(false);

				mouseX = event.getX();
				mouseY = event.getY();
				ResourcesBundle.getMusicPlayer().stopSound("recharge");
				ResourcesBundle.getMusicPlayer().playSound("recharge");

				for (int i = 0; i < targets.length; i++) {
					if (targets[i].getEntity().contains((int) event.getX(), (int) event.getY())) {
						targets[i].getEntity().setHitted(true); // ho colpito un bersaglio?
						score += targets[i].getEntity().getBaseValue() * moltiplicatore;
						targets[i].stop();
						ResourcesBundle.getMusicPlayer().stopSound("hittedTarget");
						ResourcesBundle.getMusicPlayer().playSound("hittedTarget");
						// break;
					}
				}

				for (int i = 0; i < enemies.length; i++) {
					if (enemies[i].getEntity().contains((int) event.getX(), (int) event.getY())) {
						numeroNemiciColpiti++; // ho colpito un nemico ? si aumento il numero di volte che l'ho// preso
						enemies[i].stop();
						ResourcesBundle.getMusicPlayer().stopSound("enemy");
						ResourcesBundle.getMusicPlayer().playSound("enemy");
						reloadTime = 2000;
					}
				}

				bullet.loadBullet(reloadTime); // se non ho colpito un nemico carico veloce
			}
		}).start();
	}

	@FXML
	private void handleBackToMenu() {
		stop();

	}

	/**
	 * funzione disegna
	 */
	private void draw() {
		// disegno stato del poiettile
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		if (bullet.getBulletStatus() < 50)
			gc.setFill(Color.RED);
		if (bullet.getBulletStatus() >= 50 && bullet.getBulletStatus() < 100)
			gc.setFill(Color.ORANGE);
		if (bullet.getBulletStatus() >= 100)
			gc.setFill(Color.rgb(10, 206, 23));
		gc.fillRect(50, canvas.getHeight() - 200, bullet.getBulletStatus() * 2, 15);

		// disegno la spada lanciata dove è stato cliccato col mouse
		gc.drawImage(spadaLanciata, mouseX - 90, mouseY - 90, 200, 200);
		// disegno la spada
		gc.drawImage(spada, 50, canvas.getHeight() - bullet.getBulletStatus() * 2, 250, 250);

		// guardo se un oggetto è stato colpito disegno la macchia corrispondente
		for (int i = 0; i < targets.length; i++)
			if (targets[i].getEntity().isHitted())
				gc.drawImage(targets[i].getEntity().getHittedImage(), mouseX - 30, mouseY - 70, 120, 120);

		// disegno i nemici
		for (int i = 0; i < enemies.length; i++) {
			gc.save();
			gc.translate(enemies[i].getEntity().getX() + enemies[i].getEntity().getHitBox().getWidth() / 2,
					enemies[i].getEntity().getY() + enemies[i].getEntity().getHitBox().getHeight() / 2);
			gc.rotate(enemies[i].getEntity().getRotate());
			gc.drawImage(enemies[i].getEntity().getImage(), -enemies[i].getEntity().getHitBox().getWidth() / 2,
					-enemies[i].getEntity().getHitBox().getHeight() / 2, enemies[i].getEntity().getHitBox().getWidth(),
					enemies[i].getEntity().getHitBox().getHeight());
			gc.restore();

		}
		// disegno i bersagli
		for (int i = 0; i < targets.length; i++) {
			gc.save();
			gc.translate(targets[i].getEntity().getX() + targets[i].getEntity().getHitBox().getWidth() / 2,
					targets[i].getEntity().getY() + targets[i].getEntity().getHitBox().getHeight() / 2);
			gc.rotate(targets[i].getEntity().getRotate());
			gc.drawImage(targets[i].getEntity().getImage(), -targets[i].getEntity().getHitBox().getWidth() / 2,
					-targets[i].getEntity().getHitBox().getHeight() / 2, targets[i].getEntity().getHitBox().getWidth(),
					targets[i].getEntity().getHitBox().getHeight());
			gc.restore();
		}
		// disegno la scritta moltiplicatore
		gc.setFill(Color.BLACK);
		gc.fillText("Multiplier: ", canvas.getWidth() / 2 - 110, canvas.getHeight() - 30);
		gc.setFill(Color.BLUE);
		gc.fillText("x" + (int) moltiplicatore, canvas.getWidth() / 2 + 30, canvas.getHeight() - 30);
		// disegno la scritta errori massimi
		gc.setFill(Color.BLACK);
		gc.fillText("Errors: ", canvas.getWidth() / 2 - 110, 40);

		// cambio colore a seconda del numero di errori
		if (maxErrors / 2 <= numeroNemiciColpiti)
			gc.setFill(Color.RED);
		// numero errori
		gc.fillText(numeroNemiciColpiti + "/" + (int) maxErrors, canvas.getWidth() / 2 - 10, 40);
		// disegno scritta tempo e punteggio
		gc.setFill(Color.WHITE);
		gc.fillText("Score:", canvas.getWidth() - 180, canvas.getHeight() - 30);
		gc.fillText("Time:", canvas.getWidth() - 180, 40);
		// cambio colore in base al tempo rimasto
		if (timer.getTime() > 10)
			gc.setFill(Color.LIGHTGREEN);
		else
			gc.setFill(Color.RED);
		// scrivo solo 2 cifre dopo la virgola
		gc.fillText(df.format(timer.getTime()), canvas.getWidth() - 100, 40);
		// disegno il numero di punti
		gc.setFill(Color.WHITE);
		gc.fillText("" + score, canvas.getWidth() - 90, canvas.getHeight() - 30);

	}

	public static void setTrackVolume(double trackVolume) {
		if (trackVolume <= 1)
			ControllerGame.trackVolume = trackVolume;
	}

	public static void setGameVolume(double gameVolume) {
		if (gameVolume <= 1)
			ControllerGame.gameVolume = gameVolume;
	}

	public static void setDifficulty(int difficulty) {
		if (difficulty > 0 && difficulty < 7)
			ControllerGame.difficulty = difficulty;
	}

	public static void setGameMusic(String music) {
		gameMusic = music;
	}

}
