package common;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Monopoly {

	/**
	 * @param args   HERE IS MY MARK - IS IT PUSHED?
	 * Testing Another push
	 * Testing setup from the Mac, please excuse the garbage pushes
	 */
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		JFrame mainFrame = new MonopolyGUI();
		final String mainTitle = "Monopoly";
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		mainFrame.setSize((int)dim.getWidth(), (int)dim.getHeight());
		mainFrame.setTitle(mainTitle);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
	}

}
