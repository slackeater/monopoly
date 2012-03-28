package common;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class WelcomePanel extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WelcomePanel(){
		//setLayout();
		
		add(clientConfig());
		add(serverConfig());
	}
	
	private JPanel clientConfig(){
		JPanel client = new JPanel(new GridLayout(6,1));
		client.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JLabel cliInfo = new JLabel("Client");
		cliInfo.setBorder(BorderFactory.createEtchedBorder());
		
		JLabel labelIP = new JLabel("IP");
		cliInfo.setBorder(BorderFactory.createEtchedBorder());
		JTextField serverIP = new JTextField();
		JLabel labelPort = new JLabel("Port");
		cliInfo.setBorder(BorderFactory.createEtchedBorder());
		JTextField serverPort = new JTextField();
		
		JButton connect = new JButton("Connect");
		
		client.add(cliInfo);
		client.add(labelIP);
		client.add(serverIP);
		client.add(labelPort);
		client.add(serverPort);
		client.add(connect);
		
		return client;
	}
	
	private JPanel serverConfig(){
		JPanel client = new JPanel(new GridLayout(6,1));
		client.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JLabel cliInfo = new JLabel("Client");
		cliInfo.setBorder(BorderFactory.createEtchedBorder());
		
		JLabel labelIP = new JLabel("IP");
		cliInfo.setBorder(BorderFactory.createEtchedBorder());
		JTextField serverIP = new JTextField();
		JLabel labelPort = new JLabel("Port");
		cliInfo.setBorder(BorderFactory.createEtchedBorder());
		JTextField serverPort = new JTextField();
		
		JButton connect = new JButton("Connect");
		
		client.add(cliInfo);
		client.add(labelIP);
		client.add(serverIP);
		client.add(labelPort);
		client.add(serverPort);
		client.add(connect);
		
		return client;
	}
	

}
