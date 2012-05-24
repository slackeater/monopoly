package ch.bfh.monopoly.tests;

import java.awt.HeadlessException;
import java.util.Locale;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.junit.Before;

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
	Locale loc ;
	TestInstanceGenerator tig= new TestInstanceGenerator("fr");

	public EventJPanelTest(){
		gameClient = tig.getGameClient();
		board = tig.getBoard();
		gc = tig.getGc();
		
//		testElectricCompanyOwned();
//		testElectricCompanyNotOwned();
//		testMediterraneanOwned();
		testMediterraneanNotOwned();
		
		jpanel= gameClient.getTileEventPanel(sendNetMessage);
		setSize(300, 300);
		add(jpanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("EventTestForJPanels");
		setVisible(true);
		pack();

	}
	
	public static void main(String[] args){
		EventJPanelTest ept=new EventJPanelTest();
	}
	
	
	public void testElectricCompanyOwned(){
		int electricCompany = 12;
		gameClient.setCurrentPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(electricCompany, sendNetMessage);
	}
	
	public void testElectricCompanyNotOwned(){
		int electricCompany = 12;
		gameClient.setCurrentPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(electricCompany, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("currentPlayer", sendNetMessage);
	}
	
	public void testMediterraneanNotOwned(){
		//mediterranean avenue
		int tileId = 1;
		gameClient.setCurrentPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
	}
	public void testMediterraneanOwned(){
		//mediterranean avenue
		int tileId = 1;
		gameClient.setCurrentPlayer("Justin", sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
		gameClient.buyCurrentPropertyForPlayer("currentPlayer", sendNetMessage);
		//refund the money for purchase so comparison of result is clearer
		gameClient.getCurrentPlayer().depositMoney(board.castTileToTerrain(board.getTileById(tileId)).getPrice());
		System.out.println("Rent for "+board.castTileToTerrain(board.getTileById(tileId)).getName() + " is " + board.castTileToTerrain(board.getTileById(tileId)).feeToCharge());
		gameClient.setCurrentPlayer("Giuseppe", sendNetMessage);
		gameClient.advancePlayerNSpaces(tileId, sendNetMessage);
	}

}
