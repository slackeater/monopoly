package ch.bfh.monopoly.tests;

import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JPanel;
import ch.bfh.monopoly.common.Board;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.GameController;

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
	TestInstanceGenerator tig = new TestInstanceGenerator("fr");

	public EventJPanelTest() {
		gameClient = tig.getGameClient();
		board = tig.getBoard();
		gc = tig.getGc();

		// testElectricCompanyOwned();
		// testElectricCompanyNotOwned();
		// testMediterraneanOwned();
		// testMediterraneanNotOwned();
		// testFirstRailRoadOwned();
		// testFirstRailRoadNotOwned();
		// testLandOnGo();
		testLandOnJail();
		// testJustVisiting();
		// testFreeParking();
		// testGoToJail();
		 jpanel= gameClient.getTileEventPanel(sendNetMessage);

		// true for in jail
//		testRollStartPanel(true);
//		jpanel = gameClient.getStartTurnPanel(sendNetMessage);

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

	public void testElectricCompanyNotOwned() {
		int electricCompany = 12;
		gameClient.setCurrentPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(electricCompany, sendNetMessage);
	}

	public void testElectricCompanyOwned() {
		int electricCompany = 12;
		gameClient.setCurrentPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(electricCompany, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("currentPlayer", sendNetMessage);
	}

	public void testMediterraneanNotOwned() {
		// mediterranean avenue
		int tileId = 1;
		gameClient.setCurrentPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
	}

	public void testMediterraneanOwned() {
		// mediterranean avenue
		int tileId = 1;
		gameClient.setCurrentPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("currentPlayer", sendNetMessage);
		// refund the money for purchase so comparison of result is clearer
		gameClient.getCurrentPlayer().depositMoney(
				board.castTileToTerrain(board.getTileById(tileId)).getPrice());
		System.out.println("Rent for "
				+ board.castTileToTerrain(board.getTileById(tileId)).getName()
				+ " is "
				+ board.castTileToTerrain(board.getTileById(tileId))
						.feeToCharge());
		gameClient.setCurrentPlayer("Giuseppe", sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
	}

	public void testFirstRailRoadOwned() {
		// first railroad
		int tileId = 5;
		gameClient.setCurrentPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
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

	public void testLandOnGo() {
		// goToJail tile
		int tileId = 0;
		gameClient.setCurrentPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
	}
	
	public void testLandOnJail() {
		// goToJail tile
		int tileId =30;
		gameClient.setCurrentPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
	}

	public void testJustVisiting() {
		// goToJail tile
		int tileId = 10;
		gameClient.setCurrentPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
	}

	public void testFreeParking() {
		// goToJail tile
		int tileId = 20;
		gameClient.setCurrentPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
	}

	public void testGoToJail() {
		// goToJail tile
		int tileId = 30;
		gameClient.setCurrentPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
	}

	public void testRollStartPanel(boolean gotojail) {
		gameClient.setCurrentPlayer("Justin", sendNetMessage);
		if (gotojail)
			gameClient.goToJail(sendNetMessage);
	}

}
