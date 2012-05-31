package ch.bfh.monopoly.common;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
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
	ActionListener al;
	boolean testOff;
	int attemptedRolls=0;
	ResourceBundle rb;
	
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
		rb= ResourceBundle.getBundle(
				"ch.bfh.monopoly.resources.tile",gameClient.getLoc());
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


	private JPanel imageLogo(String name){
		JPanel img = new JPanel();
		java.net.URL urlImg = Monopoly.class.getResource("/ch/bfh/monopoly/resources/" + name);
		ImageIcon logo = new ImageIcon(urlImg);
		JLabel imgLab = new JLabel(logo);
		img.add(imgLab);
		return img;
	}
	
	public JPanel getNormalStartTurnPanel() {
		buttonRight.setEnabled(true);
		buttonRight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				normalRollSecondStep();
			}
		});
		buttonRight.setText(rb.getString("roll"));
		descriptionLabel.setText(rb.getString("rollDescription"));

//		jp.add(imageLogo("roll.png"));
		jp.add(descriptionLabel);
		jp.add(buttonRight);

		return jp;
	}

	public void normalRollSecondStep() {
		jp.remove(buttonRight);
		final int roll = throwDice();
		buttonRight = new JButton(rb.getString("continueButton"));
		buttonRight.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonRight.setEnabled(false);
				int prevPos = gameClient.getCurrentPlayer().getPosition();
				gameClient.advancePlayerNSpaces(roll, testOff);
//				gameClient.sendTransactionSuccesToGUI(true);
				int curPos = gameClient.getCurrentPlayer().getPosition();
				System.out.println("PrevPos" + prevPos + "  CurPos" + curPos);
			}
		});

		descriptionLabel.setText(rb.getString("youRolled") + getDiceValues() + rb.getString("advance") + roll +rb.getString("spaces"));
		jp.add(imageLogo("roll.png"));
		jp.add(buttonRight);
	}
	
	
	public JPanel getJailStartTurnPanel() {
		attemptedRolls=0;
		al=new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				attemptedRolls++;
				jailRollSecondStep();
				System.out.println("attempted Rolls: "+attemptedRolls);
			}
		};
		
		buttonRight.addActionListener(al);
		buttonRight.setText("Roll");
		descriptionLabel.setText(rb.getString("inJail"));

		jp.add(imageLogo("mrjail.png"));
		jp.add(descriptionLabel);
		jp.add(buttonRight);

		return jp;
	}

	public void jailRollSecondStep() {
		buttonRight.setText("ok");
		final int roll = throwDice();
		if (isDoubles()){
			descriptionLabel.setText(rb.getString("youRolled") + getDiceValues() + rb.getString("outOfJail"));
			buttonRight.removeActionListener(al);
			al=new ActionListener() {
			
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("JailStatus:"+gameClient.getCurrentPlayer().isInJail());
					gameClient.getOutOfJailByRoll(testOff);
					gameClient.sendTransactionSuccesToGUI(testOff);
					System.out.println("JailStatus:"+gameClient.getCurrentPlayer().isInJail());
					buttonRight.removeActionListener(al);
					jp=gameClient.getStartTurnPanel(testOff);
				}
			};
			buttonRight.addActionListener(al);
		}
		else {
			if (attemptedRolls>2)
				rolledUnsuccessfully();
			descriptionLabel.setText(rb.getString("youRolled") + getDiceValues()+ " " + rb.getString("rollAgain") +" "+ (3-attemptedRolls)+" "+rb.getString("triesRemaining"));

		}	
	}
	
	public void rolledUnsuccessfully(){
		jp.remove(buttonRight);
		jp.add(new JLabel("rolledUnsuccessfully"));
		descriptionLabel.setText(rb.getString("youRolled") + getDiceValues() + " " + (3-attemptedRolls) +" "+rb.getString("triesRemaining")+ rb.getString("stayJail"));
		gameClient.sendTransactionSuccesToGUI(testOff);
	}

}
