package common;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.TextArea;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class MonopolyGUI extends JFrame {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MonopolyGUI(){
		JPanel main = new JPanel(new BorderLayout());
		main.add(leftPanel(), BorderLayout.WEST);
		main.add(rightContainer(), BorderLayout.CENTER);
		add(main);
		pack();
	}

	/**
	 * Panel for the left container
	 * @return the left jpanel
	 */
	private JPanel leftPanel(){
		JPanel left = new JPanel(new GridLayout(4,1));
		JPanel info = new JPanel();
		info.setBorder(BorderFactory.createEtchedBorder());

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
		info.add(new JLabel("Player 1: 100000$"));
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
		card.setBorder(BorderFactory.createEtchedBorder());
		card.setSize(new Dimension(100,100));
		return card;
	}

	/**
	 * Draw the tabbed panel for the chat and history
	 * @return JTabbedPane
	 */
	private JTabbedPane historyChatPanel(){
		JTabbedPane pane = new JTabbedPane();
		
		pane.setBorder(BorderFactory.createEtchedBorder());
		pane.setSize(new Dimension(100,100));
		JTextArea history = new JTextArea(5,25);
		history.setWrapStyleWord(true);
		history.setLineWrap(true);
		history.setEditable(false);
		
		JScrollPane scroll = new JScrollPane(history);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		JTextArea chat = new JTextArea(5,25);
		chat.setWrapStyleWord(true);
		chat.setLineWrap(true);
		chat.setEditable(false);
		
		JScrollPane scrollChat = new JScrollPane(chat);
		scrollChat.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		//button for sending the message
		JTextField input = new JTextField();
		JButton send = new JButton("Send");
		send.setSize(30, 10);
		
		JPanel chatArea = new JPanel();
		chatArea.setLayout(new BoxLayout(chatArea, BoxLayout.PAGE_AXIS));
		chatArea.add(scrollChat);
		
		JPanel inputContainer = new JPanel(new GridLayout(1,2));
		inputContainer.add(input);
		inputContainer.add(send);
		
		chatArea.add(inputContainer);
		
		pane.addTab("Chat", chatArea);
		pane.addTab("History", scroll);
		return pane;
	}

	/**
	 * The container panel for the right part
	 * @return the right container jpanel
	 */
	private JPanel rightContainer(){
		JPanel right = new JPanel(new BorderLayout());
		right.add(drawBoard(), BorderLayout.CENTER);
		return right;
	}

	/**
	 * The bottom container inside the left container
	 * @return the bottom panel
	 */
	private JPanel bottomPanel(){
		JPanel bottom = new JPanel();
		bottom.setBorder(BorderFactory.createEtchedBorder());
		bottom.add(buttonsPanel());
		return bottom;
	}

	/**
	 * It draws the buttons for the bottom panel
	 * @return
	 */
	private JPanel buttonsPanel(){
		JPanel buttons = new JPanel(new GridLayout(0,2,2,2));

		JButton buy = new JButton("Buy");
		JButton sell = new JButton("Sell");
		JButton mortgage = new JButton("Mortgage");
		JButton unmortgage = new JButton("Unmortgage");
		JButton throwDice = new JButton("Throw dice");
		JButton endTurn = new JButton("End turn");
		JButton pauseGame = new JButton("Pause");
		JButton quitGame = new JButton("Quit");

		buttons.add(buy);
		buttons.add(sell);
		buttons.add(mortgage);
		buttons.add(unmortgage);
		buttons.add(throwDice);
		buttons.add(endTurn);
		buttons.add(pauseGame);
		buttons.add(quitGame);

		return buttons;
	}

	/**
	 * Draw the board
	 * @return
	 */
	private JPanel drawBoard(){
		GridBagLayout layout = new GridBagLayout();

		JPanel board = new JPanel(layout);
		JPanel monopolyTiles[][] = new JPanel[11][11];

		for(int j = 0 ; j < 11 ; j++){
			for(int i = 0 ; i < 11 ; i++){
				monopolyTiles[j][i] = new JPanel();
				monopolyTiles[j][i].setBorder(BorderFactory.createEtchedBorder());

				if(j == 0 && i == 0 || j == 10 && i == 0 || j == 0 && i == 10 || j == 10 && i == 10){
					monopolyTiles[j][i].add(new JLabel("Corner"));
					monopolyTiles[j][i].setBorder(BorderFactory.createRaisedBevelBorder());
					board.add(monopolyTiles[j][i], new GridBagConstraints(j, i, 1, 1, 0.12, 0.12, 
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
				else if(j == 1 && i == 1){
					JPanel main = new JPanel();
					main.add(new JLabel("hi"));
					main.setSize(new Dimension(300,200));
					main.setBackground(Color.GREEN);
					monopolyTiles[j][i].add(main);
					monopolyTiles[j][i].setBackground(Color.GREEN);
					board.add(monopolyTiles[j][i], new GridBagConstraints(j, i, 9,9, 0.1, 0.1, 
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
				else{
					monopolyTiles[j][i].add(new JLabel("Tile"));
					board.add(monopolyTiles[j][i], new GridBagConstraints(j, i, 1, 1, 0.1, 0.1, 
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
			}
		}


		return board;
	}




}