package application.controllers;

import application.GameApplication;
import application.ResourcesBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ControllerSettings {

	@FXML
	private GridPane pane;
	@FXML
	private Button veryEasy;
	@FXML
	private Button easy;
	@FXML
	private Button medium;
	@FXML
	private Button hard;
	@FXML
	private Button expert;
	@FXML
	private Button extreme;
	@FXML
	private Button sounds;
	@FXML
	private Slider sliderSoundtrack;
	@FXML
	private Slider sliderSounds;
	@FXML
	private Text text;
	@FXML
	private Text text2;
	@FXML
	private ImageView goBack;
	@FXML
	private ListView<Label> musicsListView = new ListView<Label>();

	public ControllerSettings() {

	}

	@FXML
	private void initialize() {

		// set background image
		pane.setBackground(new Background(new BackgroundImage(
				new Image(getClass().getResourceAsStream("/application/res/texture/background.PNG"),
						ResourcesBundle.menuWidth, ResourcesBundle.menuHeight, false, true),
				BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				BackgroundSize.DEFAULT)));

		veryEasy.setFont(new Font("verdana", 26));
		easy.setFont(new Font("verdana", 26));
		medium.setFont(new Font("verdana", 26));
		hard.setFont(new Font("verdana", 26));
		expert.setFont(new Font("verdana", 26));
		extreme.setFont(new Font("verdana", 26));
		text.setFont(new Font("verdana", 22));
		text2.setFont(new Font("verdana", 22));

		goBack.setImage(
				new Image(getClass().getResourceAsStream("/application/res/texture/back.png"), 50, 50, false, false));
		musicsListView.setItems(ResourcesBundle.getMusicPlayer().getPlayerAsLabelObservableList());

		musicsListView.getSelectionModel().selectFirst();
		ControllerGame.setGameMusic(musicsListView.getSelectionModel().getSelectedItem().getText());

		musicsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Label>() {

			@Override
			public void changed(ObservableValue<? extends Label> arg0, Label arg1, Label arg2) {
				ControllerGame.setGameMusic(arg2.getText());
				ResourcesBundle.getMusicPlayer().stopMusic(arg1.getText());
				ResourcesBundle.getMusicPlayer().playMusic(arg2.getText());
			}
		
		});

		sliderSoundtrack.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				ResourcesBundle.getMusicPlayer().setVolumeMusic(musicsListView.getSelectionModel().getSelectedItem().getText(), sliderSoundtrack.getValue());
				ControllerGame.setTrackVolume((double) newValue);
			}

		});

		sliderSounds.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				ControllerGame.setGameVolume((double) newValue);
			}
		});
	}

	/*----------------------------------------------------------
	 * cambio colore dei bottoni a seconda del bottone cliccato
	 *----------------------------------------------------------
	 */
	@FXML
	private void handleVeryEasy() {
		ControllerGame.setDifficulty(1);
		veryEasy.setStyle(veryEasy.getStyle() + ";-fx-background-color:  #03c617");
		easy.setStyle(easy.getStyle() + ";-fx-background-color: #037fc6");
		medium.setStyle(medium.getStyle() + ";-fx-background-color: #037fc6");
		hard.setStyle(hard.getStyle() + ";-fx-background-color: #037fc6");
		expert.setStyle(expert.getStyle() + ";-fx-background-color: #037fc6 ");
		extreme.setStyle(extreme.getStyle() + ";-fx-background-color: #037fc6 ");
	}

	@FXML
	private void handleEasy() {
		ControllerGame.setDifficulty(2);
		veryEasy.setStyle(veryEasy.getStyle() + ";-fx-background-color: #037fc6");
		easy.setStyle(easy.getStyle() + ";-fx-background-color:  #03c617 ");
		medium.setStyle(medium.getStyle() + ";-fx-background-color: #037fc6 ");
		hard.setStyle(hard.getStyle() + ";-fx-background-color: #037fc6 ");
		expert.setStyle(expert.getStyle() + ";-fx-background-color: #037fc6 ");
		extreme.setStyle(extreme.getStyle() + ";-fx-background-color: #037fc6 ");
	}

	@FXML
	private void handleMedium() {
		ControllerGame.setDifficulty(3);
		veryEasy.setStyle(veryEasy.getStyle() + ";-fx-background-color: #037fc6");
		easy.setStyle(easy.getStyle() + ";-fx-background-color: #037fc6 ");
		medium.setStyle(medium.getStyle() + ";-fx-background-color:  #03c617 ");
		hard.setStyle(hard.getStyle() + ";-fx-background-color: #037fc6 ");
		expert.setStyle(expert.getStyle() + ";-fx-background-color: #037fc6 ");
		extreme.setStyle(extreme.getStyle() + ";-fx-background-color: #037fc6 ");
	}

	@FXML
	private void handleHard() {
		ControllerGame.setDifficulty(4);
		veryEasy.setStyle(veryEasy.getStyle() + ";-fx-background-color: #037fc6");
		easy.setStyle(easy.getStyle() + ";-fx-background-color: #037fc6 ");
		medium.setStyle(medium.getStyle() + ";-fx-background-color: #037fc6 ");
		hard.setStyle(hard.getStyle() + ";-fx-background-color:  #03c617 ");
		expert.setStyle(expert.getStyle() + ";-fx-background-color: #037fc6 ");
		extreme.setStyle(extreme.getStyle() + ";-fx-background-color: #037fc6 ");
	}

	@FXML
	private void handleInsane() {
		ControllerGame.setDifficulty(5);
		veryEasy.setStyle(veryEasy.getStyle() + ";-fx-background-color: #037fc6");
		easy.setStyle(easy.getStyle() + ";-fx-background-color: #037fc6 ");
		medium.setStyle(medium.getStyle() + ";-fx-background-color: #037fc6 ");
		hard.setStyle(hard.getStyle() + ";-fx-background-color: #037fc6 ");
		expert.setStyle(expert.getStyle() + ";-fx-background-color:  #03c617 ");
		extreme.setStyle(extreme.getStyle() + ";-fx-background-color: #037fc6 ");
	}

	@FXML
	private void handleExtreme() {
		ControllerGame.setDifficulty(6);
		veryEasy.setStyle(veryEasy.getStyle() + ";-fx-background-color: #037fc6");
		easy.setStyle(easy.getStyle() + ";-fx-background-color: #037fc6 ");
		medium.setStyle(medium.getStyle() + ";-fx-background-color: #037fc6 ");
		hard.setStyle(hard.getStyle() + ";-fx-background-color: #037fc6 ");
		expert.setStyle(expert.getStyle() + ";-fx-background-color: #037fc6 ");
		extreme.setStyle(extreme.getStyle() + ";-fx-background-color:  #03c617 ");
	}

	@FXML
	private void handleBackToMenu() {
		ResourcesBundle.getMusicPlayer().stopMusic(musicsListView.getSelectionModel().getSelectedItem().getText());
		GameApplication.setMenuScene();

	}

}
