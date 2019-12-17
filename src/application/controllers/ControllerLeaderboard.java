package application.controllers;

import application.GameApplication;
import application.ResourcesBundle;
import application.components.Model;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;

public class ControllerLeaderboard {

	@FXML
	private VBox root;
	@FXML
	private TableView<Model> table;
	@FXML
	private TextField barra;
	@FXML
	private Button ricerca;
	@FXML
	private Button aggiorna;

	public ControllerLeaderboard() {

	}

	@FXML
	private void initialize() {
		TableColumn<Model, Label> tc1 = new TableColumn<>("Rank");
		TableColumn<Model, Label> tc2 = new TableColumn<>("Name");
		TableColumn<Model, Label> tc3 = new TableColumn<>("Score");
		tc1.setMinWidth(100);
		tc1.setMaxWidth(100);
		tc1.setStyle("-fx-alignment: CENTER");
		tc2.setMinWidth(575);
		tc2.setMaxWidth(575);
		tc2.setStyle("-fx-alignment: CENTER");
		tc3.setMinWidth(200);
		tc3.setMaxWidth(200);
		tc3.setStyle("-fx-alignment: CENTER");
		tc1.setSortable(false);
		tc2.setSortable(false);
		tc3.setSortable(false);
		tc1.setCellValueFactory(new PropertyValueFactory<Model, Label>("position"));
		tc2.setCellValueFactory(new PropertyValueFactory<Model, Label>("name"));
		tc3.setCellValueFactory(new PropertyValueFactory<Model, Label>("score"));
		table.getColumns().add(tc1);
		table.getColumns().add(tc2);
		table.getColumns().add(tc3);
		table.setItems(ResourcesBundle.getLeaderboard().getLeaderboardAsModelObservableList());
		table.getSelectionModel().select(null);

		root.setBackground(new Background(new BackgroundImage(
				new Image(getClass().getResourceAsStream("/application/res/texture/background.PNG"),
						ResourcesBundle.popupWidth, ResourcesBundle.popupHeight, false, true),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				BackgroundSize.DEFAULT)));
	}

	@FXML
	private void handleRefresh() {
		table.setItems(ResourcesBundle.getLeaderboard().getLeaderboardAsModelObservableList());
	}

	@FXML
	private void handleClear() {
		barra.clear();
	}

	@FXML
	private void handleSearch() {
		for (Model model : table.getItems())
			if (model.getName().getText().equalsIgnoreCase(barra.getText())) {
				table.getSelectionModel().select(model);
				table.scrollTo(model);
				barra.setStyle(barra.getStyle() + ";-fx-effect: dropshadow(two-pass-box,#1fe50d,10.0,0.0,4,5);");
				return;
			}
		barra.setStyle(barra.getStyle() + ";-fx-effect: dropshadow(two-pass-box,#e50d18,10.0,0.0,4,5);");

	}

	@FXML
	private void handleBackToMenu(KeyEvent event) {
		if (event.getCode().toString().equalsIgnoreCase("escape")) {
			GameApplication.setMenuScene();
		}
	}

}
