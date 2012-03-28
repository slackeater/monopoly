package common;

import java.awt.Color;
import java.net.Socket;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JMenu;

public class StateManager {
	Player[] players;
	AbstractTile[] tiles;
	private Locale loc;
	private ResourceBundle rb;
	

	// TODO: how do we get the player's names and selected colors to this point?
	public StateManager (Player[] players) {
		//constructor should accept local variable and set it to loc
//		loc = loc.getDefault();
		loc = new Locale("EN");
//		currentLoc = Locale.ITALIAN;
		
		this.players = players;
	}



	public StateManager() {
		//create tiles, cards, and events
		createTiles();
	}
	
	public void createTiles(){
		AbstractTile[0] = new Terrain(ResourceBundle.getBundle("gui", loc).getString("file"));
		
	}
	

}
