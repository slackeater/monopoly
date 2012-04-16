package ch.bfh.monopoly.common;

import java.io.IOException;
import java.util.Locale;

import javax.swing.JFrame;

import ch.bfh.monopoly.gui.MonopolyGUI;
import ch.bfh.monopoly.gui.WelcomePanel;
import ch.bfh.monopoly.network.Network;


public class Monopoly {

	public static NetworkController communicate = new NetworkController();

	public static void main(String[] args) throws IOException {
		GameClient gc = new GameClient();

		Board b = new Board(new Locale("EN"), gc);

		BoardController bc = new BoardController(b);
		JFrame mainFrame = new MonopolyGUI(bc);
		mainFrame.setVisible(true);
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//		JFrame welcome = new WelcomePanel(mainFrame);
//		welcome.setTitle("Welcome to Monopoly");
//		welcome.setBounds(500, 50, 300, 650);
//		welcome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		welcome.setVisible(true);
		
		//communicate.startServer("192.168.1.2", 1234, 3);
	}
}
