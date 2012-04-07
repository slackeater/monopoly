package ch.bfh.monopoly.common;

import java.io.IOException;
import java.util.Locale;

import javax.swing.JFrame;

import ch.bfh.monopoly.gui.MonopolyGUI;
import ch.bfh.monopoly.gui.WelcomePanel;
import ch.bfh.monopoly.network.Network;


public class Monopoly {

	public static Network communicate = new Network();

	public static void main(String[] args) throws IOException {
		/*GameClient gc = new GameClient();

		Board b = new Board(new Locale("EN"), gc);

		BoardController bc = new BoardController(b);
		JFrame mainFrame = new MonopolyGUI(bc);
		JFrame welcome = new WelcomePanel(mainFrame);
		welcome.setTitle("Welcome to Monopoly");
		welcome.setBounds(500, 50, 300, 650);
		welcome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		welcome.setVisible(true);*/
		
		communicate.startServer("192.168.1.2", 1234, 1);
	}

}
