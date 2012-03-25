package common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.LayeredHighlighter;

public class MonopolyGUI extends JFrame{

	private BoardImage j;
	private JLayeredPane pane;
	private static final long serialVersionUID = 1L;
	int ctr = 20;
	int count = 3;

	public MonopolyGUI(){
		JSplitPane splitOne = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel(), drawBoard());
		//JSplitPane splitTwo = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitOne, rightPanel());
		
		add(splitOne);
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

	private JPanel rightPanel(){
		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.PAGE_AXIS));
		//right.setLayout(new GridLayout(3,1));
		right.setBorder(BorderFactory.createEtchedBorder());
		right.add(new JLabel("Event window"));

		JTextArea event = new JTextArea(5,20);
		event.setWrapStyleWord(true);
		event.setLineWrap(true);
		event.setEditable(false);

		JScrollPane scroll = new JScrollPane(event);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		right.add(scroll);

		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons,BoxLayout.LINE_AXIS));
		JButton accept = new JButton("Accept");
		JButton deny = new JButton("Deny");

		buttons.add(accept);
		buttons.add(deny);

		right.add(buttons);

		return right;
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
		return card;
	}

	/**
	 * Draw the tabbed panel for the chat and history
	 * @return JTabbedPane
	 */
	private JTabbedPane historyChatPanel(){
		JTabbedPane pane = new JTabbedPane();

		pane.setBorder(BorderFactory.createEtchedBorder());
		JTextArea history = new JTextArea(5,20);
		history.setWrapStyleWord(true);
		history.setLineWrap(true);
		history.setEditable(false);

		JScrollPane scroll = new JScrollPane(history);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


		JTextArea chat = new JTextArea(5,20);
		chat.setWrapStyleWord(true);
		chat.setLineWrap(true);
		chat.setEditable(false);

		JScrollPane scrollChat = new JScrollPane(chat);
		scrollChat.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		//button for sending the message
		JTextField input = new JTextField();
		JButton send = new JButton("Send");
		//send.setSize(30, 10);

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
		JPanel buttons = new JPanel(new GridLayout(0,2));

		JButton buy = new JButton("Buy");
		JButton sell = new JButton("Sell");
		JButton mortgage = new JButton("Mortgage");
		JButton unmortgage = new JButton("Unmortgage");
		JButton throwDice = new JButton("Throw dice");
		JButton endTurn = new JButton("End turn");

		buy.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//System.out.println();
				BoardImage b = new BoardImage(300, 323-ctr);
				b.setBounds(300,323-ctr, 1000, 1000);
				b.setOpaque(false);
				pane.remove(pane.getComponentCount()-2);
				pane.add(b, JLayeredPane.MODAL_LAYER);
				//pane.repaint();
				ctr += 22;
			
				repaint();
				System.out.println(pane.getComponentCount());
				//pane.remove(count);
				//count++;
				System.out.println("ciao");
				
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
	private JLayeredPane drawBoard(){
		/*ImageIcon ii = new ImageIcon(getClass().getResource("/resources/monopoly.png"));
		this.lab = new JLabel(ii);

		//JPanel board = new BoardImage();
	    JScrollPane jsp = new JScrollPane(lab);

	    jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		return jsp;*/
		pane = new JLayeredPane();
		GridBagLayout layout = new GridBagLayout();

		JPanel board = new JPanel(layout);
		JPanel monopolyTiles[][] = new JPanel[11][11];

		for(int j = 0 ; j < 11 ; j++){
			for(int i = 0 ; i < 11 ; i++){
				monopolyTiles[j][i] = new JPanel();
				monopolyTiles[j][i].setBorder(BorderFactory.createEtchedBorder());

				if(j == 0 && i == 0 || j == 10 && i == 0 || j == 0 && i == 10 || j == 10 && i == 10){
				//	monopolyTiles[j][i].set
					monopolyTiles[j][i].add(new JLabel("Corner"));
					monopolyTiles[j][i].setBorder(BorderFactory.createRaisedBevelBorder());
					board.add(monopolyTiles[j][i], new GridBagConstraints(j, i, 1, 1, 0.12, 0.12, 
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
				else if(j == 1 && i == 1){
					JPanel main = new JPanel();
					main.add(new JLabel("hi"));
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

		pane.add(board, JLayeredPane.DEFAULT_LAYER);
		board.setBounds(0, 0, 900, 700);
		j = new BoardImage(300, 300);
		j.setOpaque(false);
		j.setBounds(300, 300, 1000, 1000);
		pane.add(j, JLayeredPane.PALETTE_LAYER);
		
		
	/*	JPanel on = new JPanel();
		on.add(new JLabel("my label"));
		JPanel onTwo = new JPanel();
		onTwo.add(new JLabel("my labelfdf"));
		pane.add(on, JLayeredPane.DEFAULT_LAYER);
		pane.add(onTwo, JLayeredPane.PALETTE_LAYER);
		on.setBounds(0, 0, 100, 100);
		onTwo.setBounds(0, 0, 100, 100);*/
		
		
		return pane;

	}
}
