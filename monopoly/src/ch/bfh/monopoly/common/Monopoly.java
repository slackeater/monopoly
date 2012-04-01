package ch.bfh.monopoly.common;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Locale;

import javax.swing.JFrame;

import ch.bfh.monopoly.gui.MonopolyGUI;
import ch.bfh.monopoly.network.Network;


public class Monopoly {
	
	public static Network communicate = new Network();

	public static void main(String[] args) {
		/*JFrame welcome = new WelcomePanel();
		welcome.setTitle("Welcome to Monopoly");
		welcome.setBounds(500, 150, 300, 550);
		welcome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		welcome.setVisible(true);
		*/
		
		//FOR TEST - INITIALIZE GAME
		GameClient gc = new GameClient();
		
		//why loc and game client?????
		Board b = new Board(new Locale("EN"), gc);
		
		BoardController bc = new BoardController(b);
		
		
		
		// TODO Auto-generated method stub
		JFrame mainFrame = new MonopolyGUI(bc);
		final String mainTitle = "Monopoly";
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		mainFrame.setSize((int)dim.getWidth(), (int)dim.getHeight()-40);
		mainFrame.setTitle(mainTitle);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
	
		/*	Network communicate = new Network();
		
		System.out.println("Starting the server...");
		communicate.startServer("192.168.1.2", 1234, 2);
		
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Stopping the server...");
		communicate.stopServer();*/
	}

}
