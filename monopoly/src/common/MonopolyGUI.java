package common;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
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
		JPanel left = new JPanel();
		left.setBorder(BorderFactory.createEtchedBorder());
		left.add(new JLabel("property view property view property view"));

		JPanel property = new JPanel();

		return left;
	}

	/**
	 * The container panel for the right part
	 * @return the right container jpanel
	 */
	private JPanel rightContainer(){
		JPanel right = new JPanel(new BorderLayout());
		right.setBorder(BorderFactory.createEtchedBorder());
		right.add(topPanel(), BorderLayout.CENTER);

		right.add(bottomPanel(), BorderLayout.SOUTH);

		return right;
	}

	/**
	 * The top container inside the right container
	 * @return the top panel
	 */
	private JPanel topPanel(){
		JPanel bottom = new JPanel();
		bottom.setBorder(BorderFactory.createEtchedBorder());
		bottom.add(drawBoard());
		return bottom;	
	}

	/**
	 * The bottom container inside the right container
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
		JPanel buttons = new JPanel();
		JButton buy = new JButton();
		buy.setText("Buy");
		JButton sell = new JButton();
		sell.setText("Sell");
		JButton mortgage = new JButton();
		mortgage.setText("Mortgage");
		JButton unmortgage = new JButton();
		unmortgage.setText("Unmortgage");
		JButton throwDice = new JButton();
		throwDice.setText("Throw Dice");
		JButton endTurn = new JButton();
		endTurn.setText("End Turn");

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
		GridLayout cellTable = new GridLayout(11,11, 0, 0);
		JPanel board = new JPanel(cellTable);
		board.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		JPanel monopolyTiles[][] = new JPanel[11][11];

		int j,i;

		for(j = 0 ; j < 11 ; j++){
			for(i = 0 ; i < 11 ; i++){
				if(j==0 || j==10) 
					monopolyTiles[j][i] = new JPanel();
				else if(i==0 || i==10)
					monopolyTiles[j][i] = new JPanel();
				else
					monopolyTiles[j][i] = new JPanel();
			}
		}

		for(j = 0 ; j < 11 ; j++){
			for(i = 0 ; i < 11 ; i++){
				if(j == 0 || j == 10){
					monopolyTiles[j][i].setPreferredSize(new Dimension(60,60));
					monopolyTiles[j][i].setBorder(BorderFactory.createEtchedBorder());
					monopolyTiles[j][i].add(new JLabel("C: "+j+", "+i));
					board.add(monopolyTiles[j][i]);
				}
				else if(i == 0 || i == 10){
					monopolyTiles[j][i].setPreferredSize(new Dimension(60,60));
					monopolyTiles[j][i].setBorder(BorderFactory.createEtchedBorder());
					monopolyTiles[j][i].add(new JLabel("C: "+j+", "+i));
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
