package ch.bfh.monopoly.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.TestTile;
import ch.bfh.monopoly.tile.AbstractTile;
import ch.bfh.monopoly.tile.Chance;
import ch.bfh.monopoly.tile.NonProperty;
import ch.bfh.monopoly.tile.Property;
import ch.bfh.monopoly.tile.Terrain;


public class MonopolyGUI extends JFrame {

	private JPanel board;
	private JPanel monopolyTiles[][];
	private JTextArea text;
	private static final long serialVersionUID = 1L;
	private int throwValue = 0;
	private int currentPos = 0;
	private int tempNumPlayers = 2;
	private int columnCtr = 1;

	public MonopolyGUI(){
		setLayout(new BorderLayout());
		add(leftPanel(), BorderLayout.WEST);
		add(drawBoard(), BorderLayout.CENTER);
		((AbstractTile)monopolyTiles[throwValue][0]).setDraw(tempNumPlayers);
		monopolyTiles[throwValue][0].repaint();
		pack();
	}

	/**
	 * Panel for the left container
	 * @return the left jpanel
	 */
	private JPanel leftPanel(){
		JPanel left = new JPanel(new GridLayout(4,1));
		left.add(cardPanel());
		left.add(bottomPanel());
		left.add(infoPanel());
		left.add(historyChatPanel());
		return left;
	}

	/**
	 * Draw the info panel
	 * @return JPanel with the information
	 */
	private JPanel infoPanel(){
		JPanel info = new JPanel();
		JLabel lab = new JLabel("Player 1: 100000$");

		info.add(lab);
		return info;
	}

	/**
	 * Draw the tabbed panel for the card
	 * @return JTabbedPane 
	 */
	private JTabbedPane cardPanel(){
		JTabbedPane card = new JTabbedPane();
		JPanel tab1 = new JPanel();
		JPanel tab2 = new JPanel();
		card.addTab("Front", tab1);
		card.addTab("Rear", tab2);
		return card;
	}

	/**
	 * Draw the tabbed panel for the chat and history
	 * @return JTabbedPane
	 */
	private JTabbedPane historyChatPanel(){
		JTabbedPane pane = new JTabbedPane();

		JTextArea history = new JTextArea(5,20);
		history.setWrapStyleWord(true);
		history.setLineWrap(true);
		history.setEditable(false);

		JScrollPane scroll = new JScrollPane(history);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		JTextArea chat = new JTextArea(7,20);
		chat.setWrapStyleWord(true);
		chat.setLineWrap(true);
		chat.setEditable(false);

		JScrollPane scrollChat = new JScrollPane(chat);
		scrollChat.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		//button for sending the message
		JTextArea input = new JTextArea(2,20);
		input.setWrapStyleWord(true);
		input.setLineWrap(true);

		JScrollPane scrollInput = new JScrollPane(input);
		scrollInput.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollInput.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		JPanel chatArea = new JPanel();
		chatArea.setLayout(new BoxLayout(chatArea, BoxLayout.PAGE_AXIS));
		chatArea.add(scrollChat);

		JPanel inputContainer = new JPanel();
		inputContainer.setLayout(new BoxLayout(inputContainer, BoxLayout.PAGE_AXIS));
		inputContainer.add(scrollInput);

		chatArea.add(inputContainer);

		pane.addTab("Chat", chatArea);
		pane.addTab("History", scroll);
		return pane;
	}

	/**
	 * The bottom container inside the left container
	 * @return the bottom panel
	 */
	private JPanel bottomPanel(){
		JPanel bottom = new JPanel();
		bottom.add(buttonsPanel());
		return bottom;
	}

	/**
	 * It draws the buttons for the bottom panel
	 * @return
	 */
	private JPanel buttonsPanel(){
		JPanel buttons = new JPanel(new GridLayout(0,2));

		JButton buy = new JButton("Buy");
		JButton sell = new JButton("Sell");
		JButton mortgage = new JButton("Mortgage");
		JButton unmortgage = new JButton("Unmortgage");
		final JButton throwDice = new JButton("Throw dice");
		JButton endTurn = new JButton("End turn");


		throwDice.addActionListener(new ActionListener() {


			@Override
			public void actionPerformed(ActionEvent e) {
				Timer t = new Timer(1000,this);
				t.setInitialDelay(2000);

				if(throwValue == 0){
					text.append("I'm throwing the dice... \n");
					throwValue = (int)(6*Math.random())+1;
					text.append("The results is: " + throwValue + "\n");
					throwDice.setEnabled(false);
					t.start();
				}

				if(columnCtr <= (throwValue+currentPos)){
					((AbstractTile) monopolyTiles[columnCtr-1][0]).setDraw(0);
					System.out.println("Deleting on " + (columnCtr-1) + " and 0");

					System.out.println("Drawing on " + (columnCtr) + " and 0");
					((AbstractTile) monopolyTiles[columnCtr][0]).setDraw(tempNumPlayers);
					columnCtr++;
					repaint();
				}
				else{
					((Timer)e.getSource()).stop();


					currentPos += throwValue;
					columnCtr = currentPos;
					throwValue = 0;
					throwDice.setEnabled(true);


				}


			}
		});

		buttons.add(buy);
		buttons.add(sell);
		buttons.add(mortgage);
		buttons.add(unmortgage);
		buttons.add(throwDice);
		buttons.add(endTurn);

		return buttons;
	}

	/**
	 * Draw the board
	 * @return
	 */
	private JPanel drawBoard(){
		GridBagLayout layout = new GridBagLayout();

		board = new JPanel(layout);
		monopolyTiles = new JPanel[11][11];

		for(int j = 0 ; j < 11 ; j++){ //columns
			for(int i = 0 ; i < 11 ; i++){ //rows

				//free parking
				if(j == 0 && i == 0){
					monopolyTiles[j][i] = new NonProperty();
					monopolyTiles[j][i].add(new JLabel("<html>Free<br>Parking</html>"));
					monopolyTiles[j][i].setBorder(BorderFactory.createEtchedBorder());

					board.add(monopolyTiles[j][i], new GridBagConstraints(j, i, 1, 1, 0.12, 0.12, 
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
				//jail
				else if(j == 0 && i == 10){
					monopolyTiles[j][i] = new NonProperty();
					monopolyTiles[j][i].add(new JLabel("Jail"));
					monopolyTiles[j][i].setBorder(BorderFactory.createEtchedBorder());

					board.add(monopolyTiles[j][i], new GridBagConstraints(j, i, 1, 1, 0.12, 0.12, 
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
				//start 
				else if(j == 10 && i == 10){
					monopolyTiles[j][i] = new NonProperty();
					monopolyTiles[j][i].add(new JLabel("<-- Go"));
					monopolyTiles[j][i].setBorder(BorderFactory.createEtchedBorder());

					board.add(monopolyTiles[j][i], new GridBagConstraints(j, i, 1, 1, 0.12, 0.12, 
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
				//go to jail
				else if(j == 10 && i == 0){
					monopolyTiles[j][i] = new NonProperty();
					monopolyTiles[j][i].add(new JLabel("Go to jail"));
					monopolyTiles[j][i].setBorder(BorderFactory.createEtchedBorder());
					board.add(monopolyTiles[j][i], new GridBagConstraints(j, i, 1, 1, 0.12, 0.12, 
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
				//chance cards
				else if ((j == 2 && i == 0) || (j == 10 && i == 6) || (j == 2 && i == 10)){
					monopolyTiles[j][i] = new Chance("Chance", new GameClient());
					monopolyTiles[j][i].add(new JLabel("Chance"));
					monopolyTiles[j][i].setBorder(BorderFactory.createEtchedBorder());
					board.add(monopolyTiles[j][i], new GridBagConstraints(j, i, 1, 1, 0.1, 0.1, 
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

				}
				//community chest
				else if ((j == 10 && i == 3) || (j == 8 && i == 10) || (j == 0 && i == 3)){
					//TO CHANGE TO new Community
					monopolyTiles[j][i] = new Chance("Community", new GameClient());
					monopolyTiles[j][i].add(new JLabel("Community"));
					monopolyTiles[j][i].setBorder(BorderFactory.createEtchedBorder());
					board.add(monopolyTiles[j][i], new GridBagConstraints(j, i, 1, 1, 0.1, 0.1, 
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
				else if(j == 1 && i == 1){
					monopolyTiles[j][i] = new JPanel();
					JPanel main = new JPanel();
					main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));

					JPanel notify = new JPanel();
					notify.setLayout(new BoxLayout(notify, BoxLayout.PAGE_AXIS));
					notify.setBorder(BorderFactory.createEtchedBorder());

					JButton buy = new JButton("Buy");
					JButton auction = new JButton("Auction");

					JPanel btnPanel = new JPanel();
					btnPanel.add(buy);
					btnPanel.add(auction);

					text = new JTextArea(15, 23);
					text.setWrapStyleWord(true);
					text.setLineWrap(true);
					text.setEditable(false);

					JScrollPane eventPane = new JScrollPane(text);
					eventPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
					eventPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

					JLabel title = new JLabel("Events");

					notify.add(title);
					notify.add(eventPane);
					notify.add(btnPanel);

					main.add(notify, BorderLayout.NORTH);
					monopolyTiles[j][i].add(main);
					monopolyTiles[j][i].setOpaque(false);
					board.add(monopolyTiles[j][i], new GridBagConstraints(j, i, 9,9, 0.1, 0.1, 
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
				else if((j == 2 && i == 2) || (j == 8 && i == 8)){
					JButton card = new JButton("Card");
					monopolyTiles[j][i] = new JPanel();
					monopolyTiles[j][i].add(card);
					monopolyTiles[j][i].setLayout(new GridLayout(1,1));
					monopolyTiles[j][i].setBorder(null);
					board.add(monopolyTiles[j][i], new GridBagConstraints(j, i, 1, 1, 0.1, 0.1, 
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
				else if(j == 0 || i == 0 || j == 10 || i == 10){
					monopolyTiles[j][i] = new Terrain();
					monopolyTiles[j][i].add(new JLabel("Tile"));
					monopolyTiles[j][i].setBorder(BorderFactory.createEtchedBorder());
					board.add(monopolyTiles[j][i], new GridBagConstraints(j, i, 1, 1, 0.1, 0.1, 
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
			}
		}

		return board;

	}
}
