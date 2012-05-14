package ch.bfh.monopoly.gui;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import org.apache.mina.core.session.IoSession;

import ch.bfh.monopoly.common.BoardController;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.GameController;
import ch.bfh.monopoly.common.Monopoly;
import ch.bfh.monopoly.net.Messages;
import ch.bfh.monopoly.net.NetMessage;

/**
 * This class is used to represent the welcome window of our game
 * @author snake
 *
 */
public class WelcomePanel extends JFrame{


	private static final long serialVersionUID = -1865410778558897233L;
	private JTextArea info;
	private JFrame board;
	private JTextField nameField;

	private String name;
	private int port;
	private String ip, strIP, strPort;
	private Locale loc;

	private GameClient gameClient;
	private GameController gc;
	private BoardController bc;

	private Dimension fieldSize, panelSize;

	private static final int FIELD_WIDTH = 125;
	private static final int FIELD_HEIGHT = 20;

	private static final int PANEL_WIDTH = 300;
	private static final int PANEL_HEIGHT = 0;

	private static final String USER_NAME_PATTERN =  "^[a-z0-9_-]{3,15}$";
	private static final String IPv4_PATTERN = 
		"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	/**
	 * Construct a WelcomePanel
	 */
	public WelcomePanel(){
		this.gameClient = new GameClient(new Locale("EN"));
		this.gc = new GameController(this.gameClient);
		this.bc = new BoardController(gameClient.getBoard());

		this.strIP = "IP";
		this.strPort = "Port";

		this.fieldSize = new Dimension(FIELD_WIDTH,FIELD_HEIGHT);
		this.panelSize = new Dimension(PANEL_WIDTH,PANEL_HEIGHT);

		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));

		main.add(imageLogo());
		main.add(nameLabel());
		main.add(clientConfig());
		main.add(serverConfig());
		main.add(infoArea());

		add(main);
		setResizable(false);
	}

	/**
	 * Initialize the client by starting the network
	 * and checking the user name
	 */
	public void initClient() throws IOException, UnknownHostException, Exception{
		Pattern p;
		Matcher m;		
		IoSession cliSession;

		p = Pattern.compile(USER_NAME_PATTERN);
		m = p.matcher(name);

		//if the user name doesn't match
		if(!m.matches())
			throw new Exception("Please insert a correct user name");
		
		p = Pattern.compile(IPv4_PATTERN);
		m = p.matcher(ip);
		
		//if the ip doesn't match
		if(!m.matches())
			throw new Exception("Please insert a valid IPv4 address");

		//if everything works connect to the server
		cliSession = Monopoly.communicate.startClient(ip, port, gameClient, name);

		//set the IoSession in the GameClient
		gameClient.setIoSession(cliSession);
	}


	/**
	 * Draw the panel with the image
	 * @return a JPanel with the logo of monopoly
	 */
	private JPanel imageLogo(){
		JPanel img = new JPanel();
		java.net.URL urlImg = Monopoly.class.getResource("/ch/bfh/monopoly/resources/welcomelogo.png");
		ImageIcon logo = new ImageIcon(urlImg);
		JLabel imgLab = new JLabel(logo);
		img.setMaximumSize(new Dimension(300,114));
		img.add(imgLab);
		img.setAlignmentX(Component.LEFT_ALIGNMENT);
		return img;
	}

	/**
	 * Draw the panel with the label for the name
	 * @return a JPanel with the name field
	 */
	private JPanel nameLabel(){
		JPanel nameContainer = new JPanel();
		nameContainer.setLayout(new BoxLayout(nameContainer, BoxLayout.PAGE_AXIS));
		nameContainer.setBorder(BorderFactory.createTitledBorder("Insert your name"));
		nameContainer.setMaximumSize(panelSize);
		nameField = new JTextField();
		nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
		nameField.setMaximumSize(fieldSize);
		nameContainer.add(nameField);
		return nameContainer;
	}


	/**
	 * Draw the panel with the configuration for join a game
	 * @return a JPanel with the fields and button for join a game
	 */
	private JPanel clientConfig(){
		JPanel client = new JPanel();
		client.setLayout(new BoxLayout(client, BoxLayout.PAGE_AXIS));
		client.setBorder(BorderFactory.createTitledBorder("Join a game"));
		client.setMaximumSize(panelSize);

		final JTextField clientIP = new JTextField();
		clientIP.setAlignmentX(Component.LEFT_ALIGNMENT);
		clientIP.setMaximumSize(fieldSize);

		final JTextField clientPort = new JTextField();
		clientPort.setAlignmentX(Component.LEFT_ALIGNMENT);
		clientPort.setMaximumSize(fieldSize);

		final JButton connect = new JButton("Connect");

		connect.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {
				connect.setEnabled(false);

				try{
					ip = clientIP.getText();
					port = Integer.parseInt(clientPort.getText());
					name = nameField.getText();

					initClient();

					info.append("Connecting to " + ip + " and port " + port + "\n");

					while(true){
						Thread.sleep(1250);
						if(Monopoly.communicate.gameCanBegin()){
							dispose();

							System.out.println("BEFORE FRAME");
							//create the frame
							board = new MonopolyGUI(bc,gc);
							board.setVisible(true);
							board.setExtendedState(JFrame.MAXIMIZED_BOTH);
							break;
						}
					}

				}catch(NumberFormatException e1){
					info.append("Please fill in all the fields\n");
					connect.setEnabled(true);
				}
				catch(NullPointerException e1){
					info.append("Please insert the user name\n");
					connect.setEnabled(true);
				} catch (UnknownHostException e1) {
					info.append(e1.getMessage()+"\n");
					connect.setEnabled(true);
				} catch (IOException e1) {
					info.append(e1.getMessage()+"\n");
					connect.setEnabled(true);
				} catch (Exception e1) {
					info.append(e1.getMessage()+"\n");
					connect.setEnabled(true);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseClicked(MouseEvent e) {}

		});

		client.add(new JLabel(this.strIP));
		client.add(clientIP);
		client.add(new JLabel(this.strPort));
		client.add(clientPort);
		client.add(Box.createRigidArea(new Dimension(0,10)));
		client.add(connect);

		return client;
	}

	/**
	 * Draw the panel with used to start a new server
	 * @return a JPanel with the fields and button used to start monopoly a server 
	 */
	private JPanel serverConfig(){
		JPanel server = new JPanel();
		server.setLayout(new BoxLayout(server, BoxLayout.PAGE_AXIS));
		server.setBorder(BorderFactory.createTitledBorder("Start a server"));
		server.setMaximumSize(panelSize);

		final JTextField serverIP = new JTextField();
		serverIP.setMaximumSize(fieldSize);
		serverIP.setAlignmentX(Component.LEFT_ALIGNMENT);

		final JTextField serverPort = new JTextField();
		serverPort.setMaximumSize(fieldSize);
		serverPort.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel player = new JLabel("Players");

		final SpinnerNumberModel numPlayers = new SpinnerNumberModel(2, 2, 8, 1);
		JSpinner spinPanel = new JSpinner(numPlayers);
		spinPanel.setMaximumSize(new Dimension(50,50));
		spinPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel labelLang = new JLabel("Locale");
		String menuLang[] = {"French", "English"};
		final JComboBox langs = new JComboBox(menuLang);
		langs.setMaximumSize(fieldSize);
		langs.setAlignmentX(Component.LEFT_ALIGNMENT);

		final JButton connect = new JButton("Start server");

		connect.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {
				connect.setEnabled(false);
				int maxPlayers;

				try{
					ip = serverIP.getText();
					port = Integer.parseInt(serverPort.getText());
					maxPlayers = (Integer) numPlayers.getNumber();
					name = nameField.getText();
					String localeCode = langs.getSelectedItem().toString().substring(0, 2);
					loc = new Locale(localeCode);
					
					Monopoly.communicate.startServer(ip, port);

					initClient();

					info.append("Starting the server on IP " + ip + 
							" and port " + port + " with " + maxPlayers + " players...\n");

					while(true){
						Thread.sleep(1250);

						//when all the client are connected
						if(Monopoly.communicate.getServerOpenedSession() == maxPlayers){
							NetMessage gameStart = 
								new NetMessage(Monopoly.communicate.getServerUsernames(),loc, Messages.GAME_START);
							
							//send a NetMessage GAME_START with the chosen locale
							Monopoly.communicate.sendBroadcast(gameStart);
							dispose();

							//create the frame
							board = new MonopolyGUI(bc,gc);
							board.setVisible(true);
							board.setExtendedState(JFrame.MAXIMIZED_BOTH);
							break;
						}
					}

				} catch(NumberFormatException e1){
					info.append("Please fill in all the fields\n");
					connect.setEnabled(true);
				} catch(NullPointerException e1){
					info.append("Please insert the user name\n");
					connect.setEnabled(true);
				} catch (Exception e1) {
					info.append(e1.getMessage()+"\n");
					connect.setEnabled(true);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseClicked(MouseEvent e) {}	
		});

		server.add(new JLabel(this.strIP));
		server.add(serverIP);
		server.add(new JLabel(this.strPort));
		server.add(serverPort);
		server.add(player);
		server.add(spinPanel);
		server.add(labelLang);
		server.add(langs);

		server.add(Box.createRigidArea(new Dimension(0,10)));
		server.add(connect);

		server.setAlignmentX(Component.LEFT_ALIGNMENT);
		return server;
	}

	/**
	 * Draw the text area with the informational messages
	 * @return a JScrollPane used to alert the player
	 */
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
