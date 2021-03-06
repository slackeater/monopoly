package ch.bfh.monopoly.tests;

import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JPanel;
import ch.bfh.monopoly.common.Board;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.GameController;
import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.exception.TransactionException;

public class EventJPanelTest extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	boolean sendNetMessage = false;
	GameClient gameClient;
	Board board;
	GameController gc;
	JPanel jpanel;
	Locale loc;
	TestInstanceGenerator tig = new TestInstanceGenerator("en");
	boolean owned= true;
	
	public EventJPanelTest()  {
		gameClient = tig.getGameClient();
		board = tig.getBoard();
		gc = tig.getGc();
		
		
//		int tileId=10;
//		goToTile(tileId, !owned);
//		Player plyr1 = board.getPlayerByName("Justin");
//		plyr1.setInJail(true);
//			gameClient.toggleMortgageStatus(1, sendNetMessage);

//		 testBothUtilitiesOwned();

		
		 
//		 this player does not own the properties
//		 gameClient.setCurrentPlayer("giuseppe", sendNetMessage);
//		 gameClient.advancePlayerToTile(tileId, sendNetMessage);
//		 Player plyr2 = board.getPlayerByName("giuseppe");
//		 try {
//			plyr2.withdawMoney(plyr2.getAccount()-30);
//		} catch (TransactionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		 
//		 jpanel= gameClient.getTileEventPanel(sendNetMessage);

		// true for in jail
		testRollStartPanel(true);
		jpanel = gameClient.getStartTurnPanel(sendNetMessage);

		setSize(300, 300);
		add(jpanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("EventTestForJPanels");
		setVisible(true);
		pack();

	}

	public static void main(String[] args) {
		EventJPanelTest ept = new EventJPanelTest();
	}

	public void goToTile(int tileId, boolean owned) {
		gameClient.setCurrentPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
		if (owned)
			gameClient.buyCurrentPropertyForPlayer("currentPlayer", sendNetMessage);
		gameClient.getStartTurnPanel(sendNetMessage);
	}
	
	public void testBothUtilitiesOwned() {
		int electricCompany = 12;
		gameClient.setCurrentPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(electricCompany, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("currentPlayer", sendNetMessage);
		gameClient.advancePlayerToTile(28, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("currentPlayer", sendNetMessage);
		
	}


	public void testFirstRailRoadNotOwned() {
		// first railroad
		int tileId = 5;
		gameClient.setCurrentPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("currentPlayer", sendNetMessage);
		// refund the money for purchase so comparison of result is clearer
		gameClient.getCurrentPlayer().depositMoney(
				board.castTileToProperty(board.getTileById(tileId)).getPrice());
		System.out.println("Rent for "
				+ board.castTileToProperty(board.getTileById(tileId)).getName()
				+ " is "
				+ board.castTileToProperty(board.getTileById(tileId))
						.feeToCharge());
		gameClient.setCurrentPlayer("Giuseppe", sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
	}

	

	public void testRollStartPanel(boolean gotojail) {
		gameClient.setCurrentPlayer("Justin", sendNetMessage);
		if (gotojail)
			gameClient.goToJail(sendNetMessage);
	}
	


}
