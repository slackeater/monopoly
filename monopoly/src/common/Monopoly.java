package common;

import javax.swing.JFrame;

public class Monopoly {

	/**
	 * @param args   HERE IS MY MARK - IS IT PUSHED?
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame mainFrame = new MonopolyGUI();
		final int WINDOW_HEIGHT = 700;
		final int WINDOW_WIDTH = 1000;
		final String mainTitle = "Monopoly";
		
		mainFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		mainFrame.setTitle(mainTitle);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
	}

}
