package ch.bfh.monopoly.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import java.util.ResourceBundle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import ch.bfh.monopoly.common.BoardController;
import ch.bfh.monopoly.common.Dice;
import ch.bfh.monopoly.common.GameController;
import ch.bfh.monopoly.common.Token;
import ch.bfh.monopoly.observer.PlayerListener;
import ch.bfh.monopoly.observer.PlayerStateEvent;
import ch.bfh.monopoly.observer.TileSubject;
import ch.bfh.monopoly.observer.WindowListener;
import ch.bfh.monopoly.observer.WindowMessage;
import ch.bfh.monopoly.observer.WindowStateEvent;
import ch.bfh.monopoly.tile.TileInfo;

/**
 * This class is used to show the 
 * window with the all the graphical components 
 * of the game after the welcome panel
 * @author snake
 *
 */
public class MonopolyGUI extends JFrame {

	private static final long serialVersionUID = -3409398396221480650L;

	/**
	 * Counters constants
	 */
	public static final int TILE_NUMBER = 40;
	private final int DICE_MOVEMENT_DELAY = 450;

	/**
	 * Graphical elements
	 */
	private JTextArea eventTextArea,chat;
	private JPanel tab1;
	private List<BoardTile> tiles = new ArrayList<BoardTile>();
	private JTabbedPane tabPane = new JTabbedPane();
	private JButton throwDice, useCard, endTurn, trade, sendTradeRequest;
	private JCheckBox terrainCheck, cardCheck, moneyCheck, rcvrTerrainCheck, rcvrCardCheck, rcvrMoneyCheck;
	private JComboBox usersBox, myTerrainBox, hisTerrainBox;
	private JSpinner moneySpinner, rcvrMoneySpinner;
	private JLabel jailCardLbl, rcvrJailCardLbl;

	/**
	 * Counter for dice throw
	 */
	private int step = 0;


	/**
	 * Other instance variable
	 */
	private int playerNumber;
	private BoardController bc;
	private GameController gc;
	private ResourceBundle res;
	private Dice dice = new Dice(6,6);
	private List<PlayerStateEvent> pse;
	private boolean tokenPlaced = false;
	private boolean beginTurnClicked = false;
	
	private enum Direction{
		FORWARDS,
		BACKWARDS;
	}
	
	/**
	 * Construct a MonopolyGUI 
	 * @param bc the board controller used to query the board
	 */
	public MonopolyGUI(BoardController bc, GameController gc){
		this.bc = bc;
		this.gc = gc;

		this.res = ResourceBundle.getBundle("ch.bfh.monopoly.resources.gui", gc.getLocale());
		System.out.println(this.res);

		System.out.println("INSIDE MONOPOLY FRAME");

		//initialize the buttons with the action listener
		initializeButtons();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(res.getString("title")  + " - " + gc.getLocalPlayerName());
		setLayout(new BorderLayout());

		System.out.println("BEFORE WRAPPER INIT");
		//initialize the element of the GUI
		wrapperInit();
		


		pack();
	}

	
	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
		       
            int exit = JOptionPane.showConfirmDialog(this, res.getString("text-quitgame"));
            
            if (exit == JOptionPane.YES_OPTION) {
            	gc.sendQuitGame();
            	            	
            	try {
					Thread.sleep(250);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
                System.exit(0);
            }
           
        } else {
            super.processWindowEvent(e);
        }
	}

	/**
	 * Move the token
	 * @param diceButton
	 * 			the JButton to enable/disable when we throw the dice
	 * 			set this value to null if you don't need the button but only the movement
	 * 			this because we don't know when the timer will stops outside this method
	 * @param t
	 * 			the Token to move on the board
	 * @param val
	 * 			the value of the throw
	 * @return Action
	 * 				the abstract action used to move the token
	 */
	private Action moveToken(final JButton diceButton, final Token t, final int val, final int startPosition, final Direction dir){
		Action moveToken = new AbstractAction() {

			private static final long serialVersionUID = 9219941791909195711L;

			@Override
			public void actionPerformed(ActionEvent e) {		
				if(diceButton != null){
					throwDice.setEnabled(false);
					trade.setEnabled(false);
					useCard.setEnabled(false);
				}

				System.out.println("==== MOVE TOKEN VALUES ====");

				//move the token for "step" times
				if(step < val){
					
					int numberTile = 0;
								
					if(dir == Direction.FORWARDS)
						numberTile = startPosition+step;
					else if(dir == Direction.BACKWARDS)
						numberTile = startPosition-step;
						
					//removing the token at the previous tile
					System.out.println("GET TOKEN TO REMOVE ON POSITION: " + (numberTile+40)%TILE_NUMBER);
					tiles.get((numberTile+40)%TILE_NUMBER).removeToken(t);
					
					step++;
				
					//compute the new position where to add the token, step has been incremented
					if(dir == Direction.FORWARDS)
						numberTile = startPosition+step;
					else if(dir == Direction.BACKWARDS)
						numberTile = startPosition-step;

					//add the token to the tile we are on
					tiles.get((numberTile+40)%TILE_NUMBER).addToken(t);
					System.out.println("GET TOKEN TO ADD ON POSITION: " + (numberTile+40)%TILE_NUMBER);

					repaint();
				}
				else if(step == val){
					((Timer)e.getSource()).stop();

					//position 30 is go to jail
					if((startPosition+val) == 30){
						//removing the token at go to jail
						tiles.get(30).removeToken(t);
						
						//add token to jail 
						tiles.get(10).addToken(t);	
						
						repaint();
					}
					
					//show tile's information in the card box
					tiles.get((startPosition+val+40)%TILE_NUMBER).showCard();
					
					//TODO REMOVE ONLY FOR TEST
					if(dir == Direction.FORWARDS)
						System.out.println("LANDED ON TILE : " + (startPosition+val+40)%TILE_NUMBER);
					else if(dir == Direction.BACKWARDS)
						System.out.println("LANDED ON TILE : " + (startPosition-val+40)%TILE_NUMBER);

					step = 0;	

					if(diceButton != null){
						//TODO only for test diceButton
						//diceButton.setEnabled(true);
						System.out.println("=====0 INSIDE ANIMATION FUNCTION ==== ENABLING BUTTONS TRADE; USE CARD; END TURN" );
						
							
						
								
						trade.setEnabled(true);
						useCard.setEnabled(true);
						endTurn.setEnabled(true);
					}
					
					tabPane.add("EVENT!", gc.getTileEventPanel());
					
					//TODO decomment to disable tabs
					for(int j = 1 ; j < tabPane.getTabCount()-1 ; j++){
						tabPane.setEnabledAt(j, false);
					}
					
					tabPane.setEnabledAt(tabPane.getTabCount()-1, true);
					
					
					tabPane.setSelectedIndex(tabPane.getTabCount()-1);
				
				}

			}
		};

		return moveToken;

	}
	

	/**
	 * Initialize the list of tiles, tokens and player listener
	 * !!! RESPECT THE ORDER OF METHOD'S CALL IN ORDER TO 
	 * AVOID PROBLEMS !!!
	 */
	private void wrapperInit(){
		/**
		 * This inner class is used to 
		 * implement the PlayerListener used
		 * to draw the token on the board
		 */
		class TokenDraw implements PlayerListener{
			@Override
			public void updatePlayer(ArrayList<PlayerStateEvent> playerStates) {
				pse = playerStates;

				System.out.println("MOETHOD FOR THE ANIMATION OBSERVER");

				for(PlayerStateEvent singlePlayer : pse){
					//used to place the token on the first tile for the first time
					if(!tokenPlaced){
						Token t = singlePlayer.getT();
						int position = singlePlayer.getPosition();
						tiles.get(position).addToken(t);
					}
					//used to move the token 
					else if(tokenPlaced){
						System.out.println("INSIDE THE METHOD TO DRAW THE ANIMATION");

						Token t = singlePlayer.getT();
						int throwValue = singlePlayer.getRollValue();
						int previousPosition = singlePlayer.getPreviousPosition();

						Timer timerAnimation = null;

						System.out.println("==== TOKEN / DICE VALUES ====");
						System.out.println("TOKEN COLOR: " + t.getColor());
						System.out.println("PLAYER NAME: " + singlePlayer.getName());
						System.out.println("ROLL VALUE: " + singlePlayer.getRollValue());
						System.out.println("ACTUAL POSITION: " + singlePlayer.getPosition());
						System.out.println("HAS TURN TOKEN: " + singlePlayer.hasTurnToken());
						System.out.println("PREVIOUS POSITION "+ previousPosition);

						//move the token only when the user has thrown the dice and is the current player
						if(singlePlayer.hasTurnToken()){
							System.out.println("-----====----- THE PLAYER WITH THE TOKEN INSIDE THE ANIMATION IS : "  + singlePlayer.getName());

							if(throwValue > 1 && throwValue < 13){
								//if we are the local player enable/disable the buttons
								if(singlePlayer.getName().equals(gc.getLocalPlayerName())){
									System.out.println("STARTING THE ANIMATION FOR PLAYER: " + singlePlayer.getName());
									timerAnimation = new Timer(DICE_MOVEMENT_DELAY, moveToken(throwDice, t, throwValue, previousPosition, Direction.FORWARDS));


								}
								else{
									System.out.println("STARTING THE ANIMATION FOR PLAYER: " + singlePlayer.getName());
									timerAnimation = new Timer(DICE_MOVEMENT_DELAY, moveToken(null, t, throwValue, previousPosition, Direction.FORWARDS));
								}

								timerAnimation.start();
							}
						}

					}
				}

				//we have placed the on the first tile
				tokenPlaced = true;
			}
		}

		System.out.println("AFTER INNER CLASS");

		//add the listener to the subject
		TokenDraw td = new TokenDraw();
		bc.getSubjectForPlayer().addListener(td);

		/**
		 * This inner class represent the implementation
		 * of the observer pattern for the area with state
		 * messages at the center of the board
		 * @author snake
		 *
		 */
		class InfoAreaUpdate implements WindowListener{

			@Override
			public void updateWindow(WindowStateEvent wse) {;
			if (wse.getType() == WindowMessage.MSG_FOR_ERROR)
				eventTextArea.append(wse.getEventDescription()+"\n");
			}

		}

		InfoAreaUpdate iau = new InfoAreaUpdate();
		gc.getWindowSubject().addListener(iau);

		//get the playerNumber
		this.playerNumber = bc.getPlayerCount();

		add(leftPanel(), BorderLayout.WEST);

		System.out.println("AFTER LEFT PANEL ADD");

		//Initialize all the tiles with the information 
		for(int j = 0 ; j < TILE_NUMBER ; j++){
			TileInfo t = bc.getTileInfoById(j);

			BoardTile bt = new BoardTile(t, tab1, this.bc,this.gc, this.res);

			//System.out.println("AFTER BOARD TILE CREATION " + j);

			TileSubject s = this.bc.getTileSubjectAtIndex(j);
			this.tiles.add(bt);
			s.addListener(bt.getTileListener());
		}

		//TODO check if these little sleep time are useful 
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("AFTER TILE INIT");

		//TODO check if these little sleep time are useful 
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		add(drawBoard(), BorderLayout.CENTER);

		//TODO check if these little sleep time are useful 
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//!!! leave this here !!!
		this.bc.initGUI();
	}

	/**
	 * Panel for the left container
	 * @return the left jpanel
	 */
	private JPanel leftPanel(){
		JPanel left = new JPanel();

		left.setLayout(new BoxLayout(left, BoxLayout.PAGE_AXIS));
		left.setBorder(BorderFactory.createMatteBorder(0, 3, 0, 2, Color.decode("0xEEEEEE")));

		left.add(cardPanel());
		left.add(Box.createVerticalGlue());
		left.add(infoPanel());
		left.add(Box.createVerticalGlue());
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

			//TODO remove bc
			PlayerInfo plInfo = new PlayerInfo(j, this.bc, gc.getLocalPlayerName());

			bc.getSubjectForPlayer().addListener(plInfo.getPlayerListener());
			
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

		card.addTab(res.getString("tab-card"), tab1);
		return card;
	}

	/**
	 * Draw the tabbed panel for the chat and history
	 * @return JTabbedPane
	 */
	private JTabbedPane historyChatPanel(){
		JTabbedPane pane = new JTabbedPane();

		//create chat text area
		chat = new JTextArea(7,20);
		chat.setWrapStyleWord(true);
		chat.setLineWrap(true);
		chat.setEditable(false);

		class ChatUpdate implements WindowListener{

			@Override
			public void updateWindow(WindowStateEvent wse) {
				if (wse.getType()==WindowMessage.MSG_FOR_CHAT){
					chat.append(wse.getEventDescription());
					chat.setCaretPosition(chat.getDocument().getLength());
				}
			}
		}

		ChatUpdate tu = new ChatUpdate();

		gc.getWindowSubject().addListener(tu);

		//add to the chat text area a scroll pane
		JScrollPane scrollChat = new JScrollPane(chat);
		scrollChat.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		final JTextField input = new JTextField(20);

		//when we press enter, we send a message
		input.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				//if we press the enter key
				System.out.println("Key pressed" + e.getKeyCode());
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
		pane.addTab(res.getString("tab-chat"), chatArea);
		return pane;
	}

	/**
	 * Draw the board
	 * @return a JPanel containing the board's elements
	 */
	private JPanel drawBoard(){
		ArrayList<JButton> guiButtons = new ArrayList<JButton>();
		guiButtons.add(throwDice);
		guiButtons.add(useCard);
		guiButtons.add(trade);
		guiButtons.add(endTurn);

		//set the parameters for the event window
		this.eventTextArea = new JTextArea(13,23);
		eventTextArea.setWrapStyleWord(true);
		eventTextArea.setLineWrap(true);
		eventTextArea.setEditable(false);

		System.out.println("DRAWBOARD METHOD");
		JPanel board = new BoardBuilder(this.eventTextArea, this.tabPane, this.tiles, guiButtons);

		return board;
	}

	

	private void initializeButtons(){
		this.useCard = new JButton(res.getString("button-jailcard"));
		this.throwDice = new JButton(res.getString("button-throwdice"));

		//action listeners
		throwDice.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int localPlayerthrowValue = dice.throwDice();
				//TODO remove this 
//				int localPlayerthrowValue = 10;		
				
				//move the player of throwValue positions, and communicate to the other player the new position
//				gc.advancePlayerNSpaces(localPlayerthrowValue);
				
				throwDice.setEnabled(false);
				beginTurnClicked = true;
				
				tabPane.addTab("EVENT!",  gc.getStartTurnPanel());	

				tabPane.setSelectedIndex(1);

				//TODO only for test
//				tabPane.addTab(res.getString("tab-trade"), tradeTab());
//				tabPane.addTab("Kick", kickPlayer());

//				eventTextArea.append(res.getString("text-throwindice") + "\n");
//				eventTextArea.append(res.getString("text-diceresult") + " " + dice.getDiceValues() + " =>" + localPlayerthrowValue + "\n");


			}
		});

		this.endTurn = new JButton(res.getString("button-endturn"));
		this.endTurn.setEnabled(false);

		endTurn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				endTurn.setEnabled(false);
				trade.setEnabled(false);
				useCard.setEnabled(false);
				
				//remove the useless tab. Only the event tab must remain
				for(int j = tabPane.getComponentCount()-1 ; j > 0 ; j--){
					tabPane.remove(j);
				}
				
				gc.endTurn();

			}
		});

		this.trade = new JButton(res.getString("button-trade"));
		//		this.trade.setEnabled(false);

		class ButtonManager implements PlayerListener{

			@Override
			public void updatePlayer(ArrayList<PlayerStateEvent> playerStates) {
				for(PlayerStateEvent playerState : playerStates){
					//if the localplayer has the token enable buttons
					if(playerState.getName().equals(gc.getLocalPlayerName()))
						if(playerState.hasTurnToken() && !beginTurnClicked){
							System.out.println("===== BUTTONS: ENABLING BUTTONS IN THE OBSERVER PATTERN FOR PLAYER: " +playerState.getName() );

							throwDice.setEnabled(true);
							useCard.setEnabled(true);
							trade.setEnabled(true);
						}
						else{
							System.out.println("===== BUTTONS: DISABLING BUTTONS IN THE OBSERVER PATTERN FOR PLAYER: " +playerState.getName() );
							//TODO remove comment
							throwDice.setEnabled(false);
							useCard.setEnabled(false);
							trade.setEnabled(false);
						}
				}
			}	
		}

		ButtonManager bl = new ButtonManager();

		bc.getSubjectForPlayer().addListener(bl);

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

		this.sendTradeRequest = new JButton(res.getString("button-sendoffer"));

		this.myTerrainBox = new JComboBox();
		this.moneySpinner = new JSpinner();
		this.rcvrMoneySpinner = new JSpinner();
		this.jailCardLbl = new JLabel(res.getString("label-jailcard"));
		this.rcvrJailCardLbl = new JLabel(res.getString("label-jailcard"));

		this.usersBox = new JComboBox();

		//build the array with the user name
		for(int i = 0 ; i < playerNumber ; i++){
			this.usersBox.addItem(pse.get(i).getName());
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

		JLabel offerLbl = new JLabel(res.getString("label-youroffer"));
		JLabel wantedRes = new JLabel(res.getString("label-requestfrompl"));

		//change 10 to localPlayer.getTerrains
		for(int i = 0 ; i < 10 ; i++){
			myTerrainBox.addItem(i);
		}

		//money spinner
		int startMoney = 0;
		//TODO adjust JSpinner money range
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
	
	//TODO ONLY FOR TEST SHOULD ME MOVED AWAY
	private JPanel kickPlayer(){
		JPanel kick = new JPanel();
		kick.setLayout(new BoxLayout(kick, BoxLayout.PAGE_AXIS));
	
		JLabel title = new JLabel("Kick a player by selecting its user name");
		
		JPanel userSelContainer = new JPanel();
		userSelContainer.setLayout(new BoxLayout(userSelContainer, BoxLayout.LINE_AXIS));
		
		JLabel sel = new JLabel("Select a user");
		JComboBox username = new JComboBox();
		
		userSelContainer.add(sel);
		userSelContainer.add(username);

		//build the array with the user name
		for(int i = 0 ; i < playerNumber ; i++){
			username.addItem(pse.get(i).getName());
		}
		
		JPanel btnCtr = new JPanel();
		btnCtr.setLayout(new BoxLayout(btnCtr, BoxLayout.LINE_AXIS));
		
		JButton sendKick = new JButton("Send kick");
				
		btnCtr.add(Box.createHorizontalGlue());
		btnCtr.add(sendKick);
		
		kick.add(title);
		kick.add(Box.createVerticalGlue());
		kick.add(userSelContainer);
		kick.add(Box.createVerticalGlue());
		kick.add(Box.createVerticalGlue());
		kick.add(btnCtr);

		return kick;
	}
}
