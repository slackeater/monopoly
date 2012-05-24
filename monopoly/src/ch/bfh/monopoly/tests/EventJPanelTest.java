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
	
	Locale loc;
	GameClient gameClient;
	Board board;
	GameController gc;
	JPanel jpanel;
	TestInstanceGenerator tig= new TestInstanceGenerator();

	public EventJPanelTest(){
		gameClient = tig.getGameClient();
		board = tig.getBoard();
		gc = tig.getGc();
		
		int electricCompany = 12;
		gameClient.setCurrentPlayer("Justin", false);
		gameClient.advancePlayerNSpaces(electricCompany, false);
		jpanel= gameClient.getTileEventPanel(false);
		
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
	

}
