package ch.bfh.monopoly.tests;

import java.util.ArrayList;
import java.util.Locale;
import ch.bfh.monopoly.common.Board;
import ch.bfh.monopoly.common.BoardController;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.GameController;
import ch.bfh.monopoly.event.EventManager;
import ch.bfh.monopoly.tile.AbstractTile;

public class TestInstanceGenerator {

	GameClient gameClient;
	GameController gc;
	Board board;
	BoardController bc;
	EventManager em;
	public TestInstanceGenerator(String localeName) {
		String myName = "Justin";
		ArrayList<String> playerNames = new ArrayList<String>();
		playerNames.add(myName);
		playerNames.add("Giuseppe");
		playerNames.add("Damien");
		playerNames.add("Cyril");
		playerNames.add("Elie");

		gameClient = new GameClient();
		gameClient.createBoard(new Locale(localeName), playerNames, myName);
		gc = new GameController(gameClient);
		board = gameClient.getBoard();
		bc = new BoardController(board);
		em = ((AbstractTile)board.getTileById(1)).getEventManager();
		
		em.setupForTesting();
		setTileSendNetMessage(false);
	}


	public GameClient getGameClient() {
		return gameClient;
	}

	public GameController getGc() {
		return gc;
	}

	public Board getBoard() {
		return board;
	}
	
	public BoardController getBc(){
		return bc;
	}
	
	public EventManager getEm(){
		return em;
	}

	public void setTileSendNetMessage(boolean sendNetMessage){
		for (int i=0; i<40;i++){
			((AbstractTile)board.getTileById(i)).setSendNetMessage(sendNetMessage);
		}
	}
	
}
