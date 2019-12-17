package application.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Pair;


public class Leaderboard implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Pair<String, Integer>> leaderboard = new ArrayList<Pair<String,Integer>>();

	public Leaderboard() {
	}

	public void clear() {
		leaderboard.clear();
	}

	public ObservableList<Model> getLeaderboardAsModelObservableList() {
		ObservableList<Model> leaderboard=FXCollections.observableArrayList();
		for(int i=0;i<this.leaderboard.size();i++) {
			Label position=new Label(Integer.toString(i+1));
			position.setTextFill(i==0 ? Color.GOLD : i==1 ? Color.GRAY : i==2 ? Color.BROWN : Color.BLACK);
			position.setFont(Font.font("verdana", FontWeight.BOLD, 26));
			Label name=new Label(this.leaderboard.get(i).getKey());
			name.setFont(Font.font("verdana", FontWeight.BOLD, 26));
			Label score=new Label(this.leaderboard.get(i).getValue().toString());
		    score.setTextFill(this.leaderboard.get(i).getValue()>=100000 ? Color.DARKBLUE : this.leaderboard.get(i).getValue() > 2000 ? Color.DARKVIOLET : this.leaderboard.get(i).getValue()>1000 ? Color.GREEN :this.leaderboard.get(i).getValue()>500 ? Color.DARKCYAN: this.leaderboard.get(i).getValue()>100 ? Color.BLACK : Color.RED);
			score.setFont(Font.font("verdana", FontWeight.BOLD, 26));
			leaderboard.add(new Model(position,name,score));
		}
		
		return leaderboard;
	}

	/**
	 * aggiunge la coppia solo se non è gia presente nella lista oppure se quella
	 * gia presente ha valore minore e viene ordinata dal valore più alto al valore
	 * più basso se due valori sono uguali vengono ordinati in ordine alfabetico
	 * 
	 * @param key
	 * @param value
	 */
	public void put(String key, int value) {
		if (key != null) {
			for (int i = 0; i < leaderboard.size(); i++) {

				if (leaderboard.get(i).getKey().equalsIgnoreCase(key) && leaderboard.get(i).getValue() < value) {
					leaderboard.remove(i);
					leaderboard.add(new Pair<String, Integer>(key, value));
					sortLeaderboard();
					return;
				}

				if (leaderboard.get(i).getKey().equalsIgnoreCase(key) && leaderboard.get(i).getValue() >= value)
					return;
			}
			leaderboard.add(new Pair<String, Integer>(key, value));
			sortLeaderboard();
		}
	}

	private void sortLeaderboard() {

		leaderboard.sort(new Comparator<Pair<String, Integer>>() {
			
			@Override
			public int compare(Pair<String, Integer> arg0, Pair<String, Integer> arg1) {
				if (arg0.getValue().compareTo(arg1.getValue()) == 0)
					return arg0.getKey().compareTo(arg1.getKey());
				return -arg0.getValue().compareTo(arg1.getValue());
			}

		});

	}

}
