package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import application.components.Leaderboard;
import application.components.MusicsPlayer;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

//risorse condivise
public class ResourcesBundle {

	public static final int menuWidth = 1280;
	public static final int menuHeight = 720;

	public static final int popupWidth = 900;
	public static final int popupHeight = 600;

	private static Leaderboard leaderboard = new Leaderboard();
	private static MusicsPlayer musics = new MusicsPlayer();

	static {
		try {
			ObjectInputStream file = new ObjectInputStream(new FileInputStream(new File(ResourcesBundle.class.getResource("/application/res/leaderboard.txt").toURI())));
			Object temp = file.readObject();
			if (temp.getClass() == Leaderboard.class)
				leaderboard = (Leaderboard) temp;
			file.close();
		} catch (Exception e) {
			Alert l = new Alert(AlertType.ERROR);
			l.show();
			l.setTitle("Errore");
			l.setHeaderText("impossibile leggere la leaderboard");
			l.setContentText("File mancante o corrotto");
		}
	}

	public static synchronized Leaderboard getLeaderboard() {
		return leaderboard;
	}

	public static synchronized MusicsPlayer getMusicPlayer() {
		return musics;
	}

}
