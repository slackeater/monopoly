package ch.bfh.monopoly.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;

import ch.bfh.monopoly.common.BoardController;
import ch.bfh.monopoly.common.GameController;
import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.common.Token;
import ch.bfh.monopoly.observer.TileSubject;
import ch.bfh.monopoly.observer.WindowListener;
import ch.bfh.monopoly.tile.TileInfo;

/**
 * This class is used to show the 
 * window with the all the graphical components 
 * of the game after the welcome panel
 * @author snake
 *
 */
public class MonopolyGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Space and counters constants
	 */
	public static final int TILE_NUMBER = 40;
	private final int LEFT_SPACER_HEIGHT = 10;
	private final int DICE_MOVEMENT_DELAY = 750;

	/** 
	 * Strings constants
	 * !!!!!!!!!!!!!!!!!!! TEMP
	 * !!!!!!!!!!!!!!!!!!! We will initialize these
	 * with the i18n values
	 */
	private final String TITLE = "Monopoly";
	private final String CHAT_TAB = "Chat";
	private final String HISTORY_TAB = "History";
	private final String FRONT = "Front";
	private final String REAR = "Rear";
	private final String THROWING_DICE = "I'm throwing the dice...";
	private final String DICE_RESULTS = "The results is: ";

	private final String THROW_DICE = "Throw";
	private final String JAIL_CARD = "Jail card";
	private final String BUY = "Buy";

	/**
	 * Graphical elements
	 */
	private JTextArea eventTextArea, chat, history;
	private JPanel tab1;
	private List<BoardTile> tiles = new ArrayList<BoardTile>();
	private JTabbedPane tabPane = new JTabbedPane();
	//used to show the terrain that belong to each player
	private Token[] initTokens = new Token[8];
	private Token newPlace = null;
	private JButton buy, throwDice, useCard, community, chance, endTurn, trade, sendTradeRequest;
	private JCheckBox terrainCheck, cardCheck, moneyCheck, rcvrTerrainCheck, rcvrCardCheck, rcvrMoneyCheck;
	private JComboBox usersBox, myTerrainBox, hisTerrainBox;
	private JSpinner moneySpinner, rcvrMoneySpinner;
	private JLabel jailCardLbl, rcvrJailCardLbl;


	/**
	 * Counters for dice throw
	 */
	private int throwValue = 0;
	private int currentPos = 0;
	private int step = 0;

	/**
	 * Other instance variable
	 */
	private int playerNumber;
	private BoardController bc;
	private GameController gc;


	private List<Player> pl;

	/**
	 * Construct a MonopolyGUI
	 * @param bc the board controller used to query the board
	 */
	public MonopolyGUI(BoardController bc, GameController gc){
		this.bc = bc;
		this.gc = gc;

		//token initialization
		initTokens[0] = new Token(Color.RED,0.1,0.375);
		initTokens[1] = new Token(Color.GREEN, 0.3, 0.375);
		initTokens[2] = new Token(Color.BLUE, 0.5, 0.375);
		initTokens[3] = new Token(Color.YELLOW, 0.7, 0.375);
		initTokens[4] = new Token(Color.BLACK, 0.1, 0.700);
		initTokens[5] = new Token(Color.CYAN, 0.3, 0.700);
		initTokens[6] = new Token(Color.GRAY, 0.5, 0.700);
		initTokens[7] = new Token(Color.ORANGE, 0.7, 0.700);

		//initialize the buttons with the action listener
		initializeButtons();

		this.playerNumber = gc.getPlayers().size();
		this.pl = gc.getPlayers();
				
		for (int x = 0 ; x < playerNumber ; x++){
			System.out.println(x);
			pl.get(x).setToken(initTokens[x]);
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(TITLE);
		setLayout(new BorderLayout());

		add(leftPanel(), BorderLayout.WEST);

		//init must be called before drawBoard()
		init();

		add(drawBoard(), BorderLayout.CENTER);

		pack();
	}


	/**
	 * Initialize the list of tiles, tokens
	 */
	private void init(){
		//Initialize all the tiles with the information 
		for(int j = 0 ; j < TILE_NUMBER ; j++){
			TileInfo t = bc.getTileInfoById(j);

			BoardTile bt = new BoardTile(t, tab1, this.bc,this.gc);

			TileSubject s = this.bc.getTileSubjectAtIndex(j);
			this.tiles.add(bt);
			s.addListener(bt.getTileListener());
		}

		//add the tokens to the first tile
		for(int i = 0 ; i < playerNumber ; i++){
			tiles.get(0).addToken(pl.get(i).getToken());
		}
	}

	/**
	 * Panel for the left container
	 * @return the left jpanel
	 */
	private JPanel leftPanel(){
		JPanel left = new JPanel();

		Dimension spacer = new Dimension(0,LEFT_SPACER_HEIGHT);

		left.setLayout(new BoxLayout(left, BoxLayout.PAGE_AXIS));
		left.setBorder(BorderFactory.createMatteBorder(0, 3, 0, 2, Color.decode("0xEEEEEE")));

		left.add(cardPanel());
		left.add(Box.createRigidArea(spacer));
		left.add(infoPanel());
		left.add(Box.createRigidArea(spacer));
		left.add(historyChatPanel());

		return left;
	}

	/**
	 * Draw the info panel
	 * @return JPanel with the information
	 */
	private JPanel infoPanel(){
		JPanel info = new JPanel();
		info.setLayout(new BoxLayout(info, BoxLayout.PAGE_AXIS));

		//for each player create the panel 
		//with his info
		for(int j = 0 ; j < playerNumber ; j++){
			PlayerInfo plInfo = new PlayerInfo(pl.get(j), this.initTokens[j].getColor());

			//the local player is always shown
			if(pl.get(j).getName().equals(gc.getLocalPlayerName()))
				plInfo.showTerrains();

			info.add(plInfo);
		}

		return info;
	}

	/**
	 * Draw the tabbed panel for the card
	 * @return JTabbedPane 
	 */
	private JTabbedPane cardPanel(){
		JTabbedPane card = new JTabbedPane();
		tab1 = new JPanel();
		tab1.setLayout(new BoxLayout(tab1, BoxLayout.PAGE_AXIS));

		JPanel tab2 = new JPanel();
		card.addTab(FRONT, tab1);
		card.addTab(REAR, tab2);
		return card;
	}

	/**
	 * Draw the tabbed panel for the chat and history
	 * @return JTabbedPane
	 */
	private JTabbedPane historyChatPanel(){
		JTabbedPane pane = new JTabbedPane();

		//create history text area
		history = new JTextArea(5,20);
		history.setWrapStyleWord(true);
		history.setLineWrap(true);
		history.setEditable(false);

		//add to the history text area a scroll pane
		JScrollPane historyScroll = new JScrollPane(history);
		historyScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		historyScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		//create chat text area
		chat = new JTextArea(7,20);
		chat.setWrapStyleWord(true);
		chat.setLineWrap(true);
		chat.setEditable(false);

		class TextUpdate implements WindowListener{

			@Override
			public void updateWindow(String text) {
				chat.append(text);
				chat.setCaretPosition(chat.getDocument().getLength());
			}
		}

		TextUpdate tu = new TextUpdate();

		gc.getWindowSubject().addListener(tu);

		//add to the chat text area a scroll pane
		JScrollPane scrollChat = new JScrollPane(chat);
		scrollChat.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		//text area for sending the message
		final JTextArea input = new JTextArea(2,20);
		input.setWrapStyleWord(true);
		input.setLineWrap(true);

		//when we press enter, we send a message
		input.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				//if we press the enter key
				if(e.getKeyCode() == 10 && input.getText().length() != 0){
					String text = input.getText();
					chat.append(gc.getLocalPlayerName() + ": " + text + "\n");
					chat.setCaretPosition(chat.getDocument().getLength());
					gc.sendChatMessage(text);
					input.setText("");
				}
			}
		});

		//add to the input text area a scroll pane
		JScrollPane scrollInput = new JScrollPane(input);
		scrollInput.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollInput.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollInput.setMaximumSize(new Dimension(250,65));

		//the chat panel, with the input and chat text area
		JPanel chatArea = new JPanel();
		chatArea.setLayout(new BoxLayout(chatArea, BoxLayout.PAGE_AXIS));
		chatArea.add(scrollChat);

		JPanel inputContainer = new JPanel();
		inputContainer.setLayout(new BoxLayout(inputContainer, BoxLayout.PAGE_AXIS));
		inputContainer.add(scrollInput);

		chatArea.add(inputContainer);

		//create the two tab
		pane.addTab(CHAT_TAB, chatArea);
		pane.addTab(HISTORY_TAB, historyScroll);
		return pane;
	}

	/**
	 * Draw the board
	 * @return a JPanel containing the board's elements
	 */
	private JPanel drawBoard(){
		ArrayList<JButton> guiButtons = new ArrayList<JButton>();
		guiButtons.add(throwDice);
		guiButtons.add(buy);
		guiButtons.add(useCard);
		guiButtons.add(trade);
		guiButtons.add(endTurn);

		//set the parameters for the event window
		this.eventTextArea = new JTextArea(13,23);
		eventTextArea.setWrapStyleWord(true);
		eventTextArea.setLineWrap(true);
		eventTextArea.setEditable(false);

		JPanel board = new BoardBuilder(this.eventTextArea, this.tabPane, this.tiles, guiButtons, community, chance);

		return board;
	}

	private void initializeButtons(){
		this.buy = new JButton(BUY);
		buy.setEnabled(false);

		this.useCard = new JButton(JAIL_CARD);
		useCard.setEnabled(false);

		this.throwDice = new JButton(THROW_DICE);

		//action listeners
		throwDice.addActionListener(new ActionListener() {


			@Override
			public void actionPerformed(ActionEvent e) {

				Timer t = new Timer(DICE_MOVEMENT_DELAY,this);

				//if we start a new turn
				if(throwValue == 0){
					step = 0;
					eventTextArea.setText(THROWING_DICE + "\n");

					tabPane.addTab("Trade", tradeTab());

					//generates number between 1 and 12
					int val = (int)(12*Math.random())+1;

					//if the value is one, take 2 else take val
					throwValue = val == 1 ? 2 : val;

					eventTextArea.append(DICE_RESULTS + throwValue + "\n");

					throwDice.setEnabled(false);

					//we get the token relative to the player with ID 1
					newPlace = initTokens[0];

					//start the timer
					t.start();
				}

				//move the token for "step" times
				if(step < throwValue){
					step++;

					//removing the token at the previous tile
					tiles.get((currentPos+step-1)%TILE_NUMBER).removeToken(newPlace);

					//add the token to the tile we are on
					tiles.get((currentPos+step)%TILE_NUMBER).addToken(newPlace);

					repaint();
				}
				else if(step == throwValue){
					((Timer)e.getSource()).stop();

					//show tile's information in the card box
					tiles.get((currentPos+throwValue)%TILE_NUMBER).showCard();

					//update the current position and reset counter
					currentPos = (currentPos+throwValue)%TILE_NUMBER;
					throwValue = 0;
					step = 0;

					throwDice.setEnabled(true);
				}
			}
		});

		this.community = new JButton("Community");
		this.community.setEnabled(false);

		this.chance = new JButton("Chance");
		this.chance.setEnabled(false);

		this.endTurn = new JButton("End turn");
		this.endTurn.setEnabled(false);

		this.trade = new JButton("Trade");
		this.trade.setEnabled(false);
	}

	/**
	 * This method is used to draw the JPanel used 
	 * to trade properties / card / money with another player
	 * @return
	 */
	private JScrollPane tradeTab(){
		JPanel yourOffer = new JPanel();
		yourOffer.setLayout(new GridLayout(9, 2));
		
		this.terrainCheck = new JCheckBox();
		this.cardCheck = new JCheckBox();
		this.moneyCheck = new JCheckBox();
		this.rcvrTerrainCheck = new JCheckBox();
		this.rcvrCardCheck = new JCheckBox();
		this.rcvrMoneyCheck = new JCheckBox();
		
		this.sendTradeRequest = new JButton("Send");
		
		this.myTerrainBox = new JComboBox();
		this.moneySpinner = new JSpinner();
		this.rcvrMoneySpinner = new JSpinner();
		this.jailCardLbl = new JLabel("Jail Card");
		this.rcvrJailCardLbl = new JLabel("Jail Card");
		
		this.usersBox = new JComboBox();
		
		//build the array with the user name
		for(int i = 0 ; i < playerNumber ; i++){
			this.usersBox.addItem(pl.get(i).getName());
		}
		
		this.hisTerrainBox = new JComboBox();
				
		//change 10 to selectedPlayer.getTerrain.size
		for(int i = 0 ; i < 10 ; i++){
			this.hisTerrainBox.addItem(i);
		}
		
		myTerrainBox.setEnabled(false);
		moneySpinner.setEnabled(false);
		jailCardLbl.setEnabled(false);
		
		hisTerrainBox.setEnabled(false);
		rcvrMoneySpinner.setEnabled(false);
		rcvrJailCardLbl.setEnabled(false);
		
		JLabel offerLbl = new JLabel("Your offer is ");
		JLabel wantedRes = new JLabel("What do you want from player ");
				
		//change 10 to localPlayer.getTerrains
		for(int i = 0 ; i < 10 ; i++){
			myTerrainBox.addItem(i);
		}
			
		//money spinner
		int startMoney = 0;
		moneySpinner.setModel(new SpinnerNumberModel(startMoney, startMoney,startMoney + 15000, 1));
		rcvrMoneySpinner.setModel(new SpinnerNumberModel(startMoney, startMoney,startMoney + 15000, 1));
				
		//add the panels to the container
		yourOffer.add(offerLbl,0);
		yourOffer.add(new JLabel(" "), 1);
		yourOffer.add(terrainCheck, 2);
		yourOffer.add(myTerrainBox, 3);
		yourOffer.add(cardCheck, 4);
		yourOffer.add(jailCardLbl, 5);
		yourOffer.add(moneyCheck, 6);
		yourOffer.add(moneySpinner, 7);
		yourOffer.add(wantedRes, 8);
		yourOffer.add(usersBox, 9);
		yourOffer.add(rcvrTerrainCheck, 10);
		yourOffer.add(hisTerrainBox, 11);
		yourOffer.add(rcvrCardCheck, 12);
		yourOffer.add(rcvrJailCardLbl, 13);
		yourOffer.add(rcvrMoneyCheck, 14);
		yourOffer.add(rcvrMoneySpinner, 15);
		yourOffer.add(new JLabel(" "), 16);
		yourOffer.add(sendTradeRequest, 17);
				
		JScrollPane scrollInput = new JScrollPane(yourOffer);
		scrollInput.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollInput.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		return scrollInput;
		
	}
}
