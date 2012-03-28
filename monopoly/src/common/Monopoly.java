package common;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.omg.CosNaming.IstringHelper;

public class Monopoly {

	/**
	 * @param args   HERE IS MY MARK - IS IT PUSHED?
	 * Testing Another push
	 * Testing setup from the Mac, please excuse the garbage pushes
	 */
	public static void main(String[] args) {
		
		JFrame welcome = new WelcomePanel();
		welcome.setSize(300, 550);
		welcome.setVisible(true);
		
		// TODO Auto-generated method stub
		/*JFrame mainFrame = new MonopolyGUI();
		final String mainTitle = "Monopoly";
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		JPanel glass = (JPanel) mainFrame.getGlassPane();
		glass.setVisible(true);
	    //glass.setLayout(null);
	    JButton glassButton = new JButton("Hide");
	    glassButton.setBounds((int)dim.getWidth(), (int)dim.getHeight()-40, 40, 40);
	    glass.add(glassButton);
		
		mainFrame.setSize((int)dim.getWidth(), (int)dim.getHeight()-40);
		mainFrame.setTitle(mainTitle);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);*/
	
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
