package ch.bfh.monopoly.gui;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.naming.CommunicationException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import ch.bfh.monopoly.common.Monopoly;


public class WelcomePanel extends JFrame{

	private JTextArea info;
	private static final long serialVersionUID = 1L;


	public WelcomePanel(){
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));

		main.add(imageLogo());
		main.add(clientConfig());
		main.add(serverConfig());
		main.add(infoArea());

		add(main);
	}

	private JPanel imageLogo(){
		JPanel img = new JPanel();
		java.net.URL urlImg = Monopoly.class.getResource("/resources/welcomelogo.png");
		ImageIcon logo = new ImageIcon(urlImg);
		JLabel imgLab = new JLabel(logo);
		img.setMaximumSize(new Dimension(300,114));
		img.add(imgLab);
		img.setAlignmentX(Component.LEFT_ALIGNMENT);
		return img;
	}

	private JPanel clientConfig(){
		JPanel client = new JPanel();
		client.setLayout(new BoxLayout(client, BoxLayout.PAGE_AXIS));
		client.setBorder(BorderFactory.createTitledBorder("Client configuration"));

		JLabel labelIP = new JLabel("IP");
		JTextField serverIP = new JTextField();
		serverIP.setMaximumSize(new Dimension(300,20));

		JLabel labelPort = new JLabel("Port");
		JTextField serverPort = new JTextField();
		serverPort.setMaximumSize(new Dimension(300,20));



		JButton connect = new JButton("Connect");
		client.add(labelIP);
		client.add(serverIP);
		client.add(labelPort);
		client.add(serverPort);

		client.add(connect);

		client.setAlignmentX(Component.LEFT_ALIGNMENT);

		return client;
	}

	private JPanel serverConfig(){
		JPanel client = new JPanel();
		client.setLayout(new BoxLayout(client, BoxLayout.PAGE_AXIS));
		client.setBorder(BorderFactory.createTitledBorder("Server configuration"));

		JLabel labelIP = new JLabel("IP");
		final JTextField serverIP = new JTextField();
		serverIP.setMaximumSize(new Dimension(300,20));
		JLabel labelPort = new JLabel("Port");
		final JTextField serverPort = new JTextField();
		serverPort.setMaximumSize(new Dimension(300,20));

		JLabel player = new JLabel("Players");

		final SpinnerNumberModel numPlayers = new SpinnerNumberModel(2, 2, 8, 1);
		JSpinner spinPanel = new JSpinner(numPlayers);
		spinPanel.setMaximumSize(new Dimension(50,50));
		spinPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		final JButton connect = new JButton("Start server");

		connect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				connect.setEnabled(false);
				int port, maxPlayers;
				String ip;

				try{
					ip = serverIP.getText();
					port = Integer.parseInt(serverPort.getText());
					maxPlayers = (Integer) numPlayers.getNumber();
					info.append("Starting the server on IP " + ip + 
							" and port " + port + " with " + maxPlayers + " players...\n");
					
					try {
						Monopoly.communicate.startServer(ip, port, maxPlayers);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						info.append(e1.getMessage()+"\n");
						connect.setEnabled(true);
					}
				}catch(NumberFormatException e1){
					info.append("Please fill in all the fields\n");
					connect.setEnabled(true);
				}

			
			}
		});

		client.add(labelIP);
		client.add(serverIP);
		client.add(labelPort);
		client.add(serverPort);
		client.add(player);
		client.add(spinPanel);
		client.add(connect);

		client.setAlignmentX(Component.LEFT_ALIGNMENT);
		return client;
	}

	private JScrollPane infoArea(){
		info = new JTextArea();
		info.setEditable(false);
		info.setWrapStyleWord(true);
		info.setLineWrap(true);

		JScrollPane pane = new JScrollPane(info);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		pane.setAlignmentX(Component.LEFT_ALIGNMENT);
		return pane;
	}


}