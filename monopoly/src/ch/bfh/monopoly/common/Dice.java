package ch.bfh.monopoly.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Dice {

	int maxValDiceOne;
	int maxValDiceTwo;
	int throwValueOne = 0;
	int throwValueTwo = 0;
	JPanel jp = new JPanel();
	JButton buttonRight = new JButton();
	JButton buttonLeft = new JButton();
	JLabel descriptionLabel = new JLabel();
	GameClient gameClient;
	boolean testOff;
	/**
	 * Construct a dice object
	 * 
	 * @param maxValDiceOne
	 *            the maximal value for the die one
	 * @param maxValDiceTwo
	 *            the maximal value for the die two
	 */
	public Dice(int maxValDiceOne, int maxValDiceTwo) {
		this.maxValDiceOne = maxValDiceOne;
		this.maxValDiceTwo = maxValDiceTwo;
	}
	
	/**
	 * Construct a dice object
	 * 
	 * @param maxValDiceOne
	 *            the maximal value for the die one
	 * @param maxValDiceTwo
	 *            the maximal value for the die two
	 */
	public Dice(int maxValDiceOne, int maxValDiceTwo, GameClient gameClient, boolean testOff) {
		this.testOff = testOff;
		this.gameClient=gameClient;
		this.maxValDiceOne = maxValDiceOne;
		this.maxValDiceTwo = maxValDiceTwo;
	}

	/**
	 * Throw the dice
	 * 
	 * @return int an integer representing the throw of the two dice
	 */
	public int throwDice() {
		// generates number between 1 and 12
		// the one is added because Math.random() generates
		// number between 0.0 and 1.0. But 1.0 is not included.
		// so in the case where:
		// maxValDice = 6 ; Math.random = 0.99
		// 6*0.99 = 5.94 rounded => 5 + 1 = 6
		throwValueOne = (int) (maxValDiceOne * Math.random()) + 1;
		throwValueTwo = (int) (maxValDiceTwo * Math.random()) + 1;

		return throwValueOne + throwValueTwo;
	}

	
	/**
	 * Get the single value of both dice
	 * 
	 * @return String a string representing the two values, for example 3, 5
	 */
	public String getDiceValues() {
		return throwValueOne + ", " + throwValueTwo;
	}
	
	public boolean isDoubles(){
		return throwValueOne==throwValueTwo;
	}


	
	public JPanel getNormalStartTurnPanel() {
		
		buttonRight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				normalRollSecondStep();
			}
		});
		buttonLeft.setText("Roll");
		descriptionLabel.setText("Click Roll to roll the dice to your turn. \n");

		jp.add(descriptionLabel);
		jp.add(buttonRight);

		return jp;
	}

	public void normalRollSecondStep() {
		jp.remove(buttonRight);
		final int roll = throwDice();
		buttonRight = new JButton("Continue");
		buttonRight.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				int prevPos = gameClient.getCurrentPlayer().getPosition();
				gameClient.advancePlayerNSpaces(roll, testOff);
//				gameClient.sendTransactionSuccesToGUI(true);
				int curPos = gameClient.getCurrentPlayer().getPosition();
				System.out.println("PrevPos" + prevPos + "  CurPos" + curPos);
			}
		});

		descriptionLabel.setText("You rolled a " + getDiceValues() + ", advance " + roll +" spaces");
		jp.add(buttonRight);
	}
	
	
	
	
	
	
	
	
	public JPanel getJailStartTurnPanel() {

		buttonRight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				normalRollSecondStep();
			}
		});
		buttonLeft.setText("Roll");
		descriptionLabel.setText("You are in Jail, you have 3 options to get out: \n Roll\n Pay\n Use a Jail Card");

		jp.add(descriptionLabel);
		jp.add(buttonRight);

		return jp;
	}

	public void jailRollSecondStep() {
		jp.remove(buttonRight);
		final int roll = throwDice();
		buttonRight = new JButton("Continue");
		buttonRight.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				int prevPos = gameClient.getCurrentPlayer().getPosition();
				gameClient.advancePlayerNSpaces(roll, testOff);
//				gameClient.sendTransactionSuccesToGUI(true);
				int curPos = gameClient.getCurrentPlayer().getPosition();
				System.out.println("PrevPos" + prevPos + "  CurPos" + curPos);
			}
		});
		if (isDoubles()){
			// change description
			// event get out of jail
			
		}
			
		descriptionLabel.setText("You rolled a " + getDiceValues() + ", advance " + roll +" spaces");
		jp.add(buttonRight);
	}

}
