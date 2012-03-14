package common;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
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
		JPanel left = new JPanel(new GridLayout(2,1));
		//left.setBorder(BorderFactory.createEtchedBorder());
		JPanel info = new JPanel();
		info.setBorder(BorderFactory.createEtchedBorder());
		info.add(new JLabel("kdhgdslhgdlhgdlhfgldhgd"));
		
		left.add(info);
		left.add(bottomPanel());

		return left;
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
		GridLayout cellTable = new GridLayout(11,11, 0, 0);
		JPanel board = new JPanel(cellTable);
		board.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		JPanel monopolyTiles[][] = new JPanel[11][11];

		int j,i;

		for(j = 0 ; j < 11 ; j++){
			for(i = 0 ; i < 11 ; i++){
				if(j==0 || j==10 || i == 0 || i == 10) 
					monopolyTiles[j][i] = new JPanel();
				else
					monopolyTiles[j][i] = new JPanel();
			}
		}

		for(j = 0 ; j < 11 ; j++){
			for(i = 0 ; i < 11 ; i++){
				if(j == 0 || j == 10 || i == 0 || i == 10){
					monopolyTiles[j][i].setBorder(BorderFactory.createEtchedBorder());
					JLabel name = new JLabel("<html>Meditterrean<br>avenue</html>");
					Font myFont = new Font(name.getFont().getFontName(), name.getFont().getStyle(),10);
					name.setFont(myFont);
					monopolyTiles[j][i].add(name);
					board.add(monopolyTiles[j][i]);
				}
				else{
					monopolyTiles[j][i].setBackground(Color.WHITE);
					board.add(monopolyTiles[j][i]);
				}
			}
		}

		return board;
	}




}
