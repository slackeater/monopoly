package ch.bfh.monopoly.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;
import ch.bfh.monopoly.common.Board;
import ch.bfh.monopoly.common.BoardController;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.GameController;
import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.observer.PlayerListener;
import ch.bfh.monopoly.observer.PlayerStateEvent;
import ch.bfh.monopoly.observer.PlayerSubject;
import ch.bfh.monopoly.observer.WindowListener;
import ch.bfh.monopoly.observer.WindowMessage;
import ch.bfh.monopoly.observer.WindowStateEvent;
import ch.bfh.monopoly.observer.WindowSubject;
import ch.bfh.monopoly.tile.Property;
import ch.bfh.monopoly.tile.Tile;

public class ObserverPatternTests {

	Locale loc;
	GameClient gameClient;
	Board board;
	GameController gc;
	BoardController bc;
	boolean listenerActed;
	
	
	class MockWindowListener implements WindowListener {
		@Override
		public void updateWindow(WindowStateEvent wse) {
			if (wse.getType() == WindowMessage.MSG_FOR_CHAT) {
				System.out.println("Window updated: "
						+ wse.getEventDescription());
				listenerActed=true;
			}
		}

	}

	@Before
	public void setup() {
		TestInstanceGenerator tig = new TestInstanceGenerator("en");
		gameClient = tig.getGameClient();
		board = tig.getBoard();
		gc = tig.getGc();
		bc = tig.getBc();
	}

	@Test
	public void guiGetsPlayerSubject() {
		PlayerSubject ps = bc.getSubjectForPlayer();
		PlayerListener pl = new MockPlayerListener();
		ps.addListener(pl);
		gameClient.setCurrentPlayer("Justin", true);
		gameClient.buyHouse(1, true);
	}

	@Test
	public void guiGetsWindowSubject() {
		WindowSubject ws = gc.getWindowSubject();
		WindowListener wl = new MockWindowListener();
		ws.addListener(wl);
		gameClient
				.displayChat("THIS is a chat message for testing purposes only. do not try this at home");
		assertTrue(listenerActed);
		listenerActed=false;
	}
}