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
import java.util.ArrayList;

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

import ch.bfh.monopoly.common.AbstractTile;
import ch.bfh.monopoly.common.Chance;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.NonProperty;
import ch.bfh.monopoly.common.Property;
import ch.bfh.monopoly.common.Terrain;
import ch.bfh.monopoly.common.TestTile;


public class MonopolyGUI extends JFrame {

	private JPanel board;

	//this bidimensional array is only used to draw the JPanel
	private JPanel monopolyTiles[][];

	private JTextArea text;
	private static final long serialVersionUID = 1L;
	private int throwValue = 0;
	private int currentPos = 0;
	private int tempNumPlayers = 2;
	private int columnCtr = 1;
	private List<JPanel> tilesForDice = new ArrayList<JPanel>();

	public MonopolyGUI(){
		setLayout(new BorderLayout());
		add(leftPanel(), BorderLayout.WEST);
		add(drawBoard(), BorderLayout.CENTER);
		((AbstractTile)tilesForDice.get(0)).setDraw(tempNumPlayers);
		//((AbstractTile)monopolyTiles[throwValue][0]).setDraw(tempNumPlayers);
		//monopolyTiles[throwValue][0].repaint();
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

				if(columnCtr <= ((throwValue+currentPos)%40)){
					((AbstractTile)tilesForDice.get(columnCtr-1)).setDraw(0);
					//repaint();
					//((AbstractTile) monopolyTiles[columnCtr-1][0]).setDraw(0);
					//System.out.println("Deleting on " + (columnCtr-1) + " and 0");

					((AbstractTile)tilesForDice.get(columnCtr)).setDraw(tempNumPlayers);
					//System.out.println("Drawing on " + (columnCtr) + " and 0");
					//((AbstractTile) monopolyTiles[columnCtr][0]).setDraw(tempNumPlayers);
					columnCtr++;
					repaint();
				}
				else{
					((Timer)e.getSource()).stop();
					currentPos = (currentPos+throwValue)%40;
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
		this.text = new JTextArea(15,23);
		text.setWrapStyleWord(true);
		text.setLineWrap(true);
		text.setEditable(false);
		
		JPanel board = new BoardBuilder(this.text);
		return board;
	}
}