package application.controllers;


import javax.mail.MessagingException;

import application.ResourcesBundle;
import application.components.Email;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class ControllerMail {

	@FXML
	private GridPane root;
	@FXML
	private Button cancella;
	@FXML
	private TextArea testo;
	@FXML
	private Button invio;
	@FXML
	private Label label;

	@FXML
	private void initialize() {

		// background image
		root.setBackground(new Background(new BackgroundImage(
				new Image(getClass().getResourceAsStream("/application/res/texture/background.PNG"), ResourcesBundle.popupWidth,
						ResourcesBundle.popupHeight, false, true),
				BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				BackgroundSize.DEFAULT)));

	}

	@FXML
	private void handleCancellaButton() {
		testo.setText("");
	}

	/**
	 * invia una mail contenente il testo presente nella textArea
	 */
	@FXML
	private void handleSend() {
		label.setText("Sending...");
		label.setStyle(label.getStyle() + ";-fx-font-weight: bold"); // mostra un label che dice sending...
		label.setUnderline(true);
		label.setTextFill(Color.BLACK);

		new Thread(this::send).start();
	}

	private synchronized void send() {
		Email email = new Email();
		email.setSubject("Game feedback");
		email.setText(testo.getText());
		email.setCredentials("gameinformatica23@gmail.com", "Qwerty1*", "gameinformatica23@gmail.com");

		try {
			email.send();
			Platform.runLater(() -> {
				label.setText("Email sent successfully thanks!");
				label.setTextFill(Color.LIGHTGREEN);
			});
		} catch (MessagingException e) {
			// se non manda o non c è internet o qualche forza misteriosa rompe
			Platform.runLater(() -> {
				label.setText("Connection error check your internet and try again!");
				label.setTextFill(Color.RED);
			});
		}
	}

}