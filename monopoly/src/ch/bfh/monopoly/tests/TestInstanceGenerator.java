package ch.bfh.monopoly.tests;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.TransportMetadata;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.core.write.WriteRequestQueue;

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
	public TestInstanceGenerator() {
		String myName = "Justin";
		ArrayList<String> playerNames = new ArrayList<String>();
		playerNames.add(myName);
		playerNames.add("Giuseppe");
		playerNames.add("Damien");
		playerNames.add("Cyril");
		playerNames.add("Elie");

		gameClient = new GameClient();
		gameClient.createBoard(new Locale("EN"), playerNames, myName);
		gc = new GameController(gameClient);
		board = gameClient.getBoard();
		bc = new BoardController(board);
		em = ((AbstractTile)board.getTileById(1)).getEventManager();
		
		em.setSendNetMessage(false);
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

}
