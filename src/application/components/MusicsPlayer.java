package application.components;

import java.net.URL;
import java.util.Hashtable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MusicsPlayer {

	private Hashtable<String, MediaPlayer> gameMusic = new Hashtable<>();
	private Hashtable<String, MediaPlayer> gameSounds = new Hashtable<>();

	public ObservableList<Label> getPlayerAsLabelObservableList() {
		ObservableList<Label> list = FXCollections.observableArrayList();
		for (String key : gameMusic.keySet()) {
			Label label = new Label(key);
			label.setFont(Font.font("verdana", FontWeight.BOLD, 26));
			list.add(label);
		}

		return list;

	}

	public boolean isPlayingMusic(String name) {
		return gameMusic.get(name).getStatus() == Status.PLAYING;
	}

	public boolean isPlayingSound(String name) {
		return gameSounds.get(name).getStatus() == Status.PLAYING;
	}

	public MusicsPlayer() {

	}

	public void suspendMusic(String name) {
		gameMusic.get(name).pause();
	}

	public void suspendSound(String name) {
		gameSounds.get(name).pause();
	}

	public void resumeMusic(String name) {
		gameMusic.get(name).play();
	}

	public void resumeSound(String name) {
		gameSounds.get(name).play();
	}

	public void setVolumeMusic(String name, double volume) {
		gameMusic.get(name).setVolume(volume);
	}

	public void setVolumeSound(String name, double volume) {
		gameSounds.get(name).setVolume(volume);
	}

	public void putGameMusic(URL path, String name) {
		gameMusic.put(name, new MediaPlayer(new Media(path.toExternalForm())));
		gameMusic.get(name).setCycleCount(MediaPlayer.INDEFINITE);
	}

	public void putGameSound(URL path, String name) {
		gameSounds.put(name, new MediaPlayer(new Media(path.toExternalForm())));
	}

	public void playMusic(String name) {
		gameMusic.get(name).play();
	}

	public void playSound(String name) {
		gameSounds.get(name).play();
	}

	public void stopMusic(String name) {
		gameMusic.get(name).stop();
	}

	public void stopSound(String name) {
		gameSounds.get(name).stop();
	}

}
