package ch.bfh.monopoly.common;

import java.io.IOException;
import java.util.Locale;

import javax.swing.JFrame;

import ch.bfh.monopoly.gui.MonopolyGUI;
import ch.bfh.monopoly.gui.WelcomePanel;


public class Monopoly {

	public static NetworkController communicate = new NetworkController();

	public static void main(String[] args) throws IOException {
		
		JFrame welcome = new WelcomePanel();
		welcome.setTitle("Welcome to Monopoly");
		welcome.setBounds(500, 50, 300, 650);
		welcome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		welcome.setVisible(true);
		
	}
}
