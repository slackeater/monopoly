package ch.bfh.monopoly.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import ch.bfh.monopoly.common.BoardController;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.TestTile;
import ch.bfh.monopoly.common.Token;
import ch.bfh.monopoly.tile.AbstractTile;
import ch.bfh.monopoly.tile.Chance;
import ch.bfh.monopoly.tile.NonProperty;
import ch.bfh.monopoly.tile.Property;
import ch.bfh.monopoly.tile.Terrain;
import ch.bfh.monopoly.tile.TileInfo;


public class MonopolyGUI extends JFrame {

	
	private final int TILE_NUMBER = 40;
	
	private List<BoardTile> tiles = new ArrayList<BoardTile>();
	
	private BoardController bc;
	
	private JTextArea text;
	private JTextArea chat;
	private JPanel tab1;
	
	private static final long serialVersionUID = 1L;
	private int throwValue = 0;
	private int currentPos = 0;
	private int columnCtr = 1;
	private Token newPlace = null;
	
	private Token[] initTokens = new Token[8];
	
	public MonopolyGUI(BoardController bc){
		this.bc = bc;
				
		setLayout(new BorderLayout());
		add(leftPanel(), BorderLayout.WEST);
		
		//initialize the tiles
		initTiles();
		
		add(drawBoard(), BorderLayout.CENTER);

		//it must be moved in another class
		initTokens[0] = new Token(Color.RED,0.1,0.375);
		initTokens[1] = new Token(Color.GREEN, 0.3, 0.375);
		initTokens[2] = new Token(Color.BLUE, 0.5, 0.375);
		initTokens[3] = new Token(Color.YELLOW, 0.7, 0.375);
		initTokens[4] = new Token(Color.BLACK, 0.1, 0.700);
		initTokens[5] = new Token(Color.CYAN, 0.3, 0.700);
		initTokens[6] = new Token(Color.GRAY, 0.5, 0.700);
		initTokens[7] = new Token(Color.ORANGE, 0.7, 0.700);

		tiles.get(0).addToken(initTokens[0]);
		tiles.get(0).addToken(initTokens[1]);
		tiles.get(0).addToken(initTokens[2]);
		tiles.get(0).addToken(initTokens[3]);
		pack();
	}

	/**
	 * Initialize the list of tiles
	 */
	private void initTiles(){
		for(int j = 0 ; j < TILE_NUMBER ; j++){
			TileInfo t = bc.getTileInfoByID(j);
			
			BoardTile bc = new BoardTile(t, tab1);
			
			this.tiles.add(bc);
		}
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
		tab1 = new JPanel();
		tab1.setLayout(new BoxLayout(tab1, BoxLayout.PAGE_AXIS));
		
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

		chat = new JTextArea(7,20);
		chat.setWrapStyleWord(true);
		chat.setLineWrap(true);
		chat.setEditable(false);

		JScrollPane scrollChat = new JScrollPane(chat);
		scrollChat.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		//button for sending the message
		final JTextArea input = new JTextArea(2,20);
		input.setWrapStyleWord(true);
		input.setLineWrap(true);
		input.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				//if we press the enter key
				if(e.getKeyCode() == 10){
					chat.append(input.getText());
					input.setText("");
				}
				
			}
		});

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
	 * @return the JPanel with the buttons
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
				Timer t = new Timer(750,this);
				t.setInitialDelay(1250);
				
				if(throwValue == 0){
					text.append("I'm throwing the dice... \n");
					throwValue = (int)(12*Math.random())+1;
					text.append("The results is: " + throwValue + "\n");
					
					throwDice.setEnabled(false);
					
					//we remove the token relative to the player with ID 1
					newPlace = initTokens[0];
					t.start();
				}

				if(columnCtr <= ((throwValue+currentPos)%40)){
					//removing the token at the previous tile
					tiles.get(columnCtr-1).removeToken(newPlace);
					
					//add the token to the tile we are on
					tiles.get(columnCtr).addToken(newPlace);
					
					columnCtr++;
					repaint();
				}
				else{
					((Timer)e.getSource()).stop();
					currentPos = (currentPos+throwValue)%40;
					columnCtr = currentPos+1;
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
	 * @return a JPanel containing the board's elements
	 */
	private JPanel drawBoard(){
		this.text = new JTextArea(15,23);
		text.setWrapStyleWord(true);
		text.setLineWrap(true);
		text.setEditable(false);

		JPanel board = new BoardBuilder(this.text, this.tiles);
		return board;
	}
}
