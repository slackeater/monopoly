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
import ch.bfh.monopoly.common.PlayerListener;
import ch.bfh.monopoly.common.PlayerStateEvent;
import ch.bfh.monopoly.common.PlayerSubject;
	import ch.bfh.monopoly.tile.Property;
import ch.bfh.monopoly.tile.Tile;

	public class ObserverPatternTests {

		Locale loc;
		GameClient gameClient;
		Board board;
		GameController gc;
		BoardController bc;

		@Before
		public void setup() {
			gameClient = new GameClient(new Locale("EN"), null);
			board = gameClient.getBoard();
			gc = new GameController(gameClient);
			bc = new BoardController(board);
		}
		
		@Test
		public void guiGetsPlayerSubject() {
			PlayerSubject ps = bc.getSubjectForPlayer("Justin");
			PlayerListener pl = new MockPlayerListener();
			ps.addListener(pl);
			Player plr = board.getPlayerByName("Justin");
			plr.setPosition(3);
			
		}
}