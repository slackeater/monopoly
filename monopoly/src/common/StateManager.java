package common;

import java.awt.Color;
import java.net.Socket;

public class StateManager {
	Player[] players;
	private final int START_MONEY_US = 5000;
	private final int START_MONEY_SWISS = 200000;
	

	// TODO: how do we get the player's names and selected colors to this point?
	public StateManager (int numberOfPlayers) {
		players = new Player[numberOfPlayers];
		for (int i =0; i<numberOfPlayers;i++){
			
			//constructor takes params :  String name, Color color, int account, Socket socket
			players[i] = new Player(null, null, START_MONEY_US, null);
			
		}
	}
}
