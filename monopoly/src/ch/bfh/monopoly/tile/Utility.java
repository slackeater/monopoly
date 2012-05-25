package ch.bfh.monopoly.tile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.event.EventManager;

public class Utility extends Property {

	JPanel jp = new JPanel();
	JButton buttonRight;
	JButton buttonLeft;
	JLabel descriptionLabel;

	public Utility(String name, int price, String group, int mortgageValue,
			int coordX, int coordY, int id, EventManager em, Player bank, GameClient gameClient, ResourceBundle rb) {
		super(name, price, group, mortgageValue, coordX, coordY, id, em, bank, gameClient,rb);
		this.name = name;
		this.price = price;
		this.mortgageValue = mortgageValue;
		buttonLeft = new JButton();
		buttonRight = new JButton();
		descriptionLabel=new JLabel();
	}


	public int feeToCharge() {
		// TODO This method must be here because it extends property, but it can't be called unless there is a roll value..
		return 0;
	}

	public String toString() {
		return super.toString() + "\nname: " + name + "\nprice: " + this.price
				+ "\nmortgageValue: " + mortgageValue + "\nowner: " + owner;
	}

	/**
	 * get the JPanel to show in the GUI for this tile's event
	 */
	public JPanel getTileEventPanel() {
		boolean owned=!(owner.getName()=="bank");
		if(owned)
			return tileOwnedEvent();
		else
			return tileNotOwnedEvent();
	}

	public JPanel tileOwnedEvent(){
		buttonRight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ownedSecondStep();
			}
		});
		buttonLeft.setText("Roll");
		descriptionLabel
				.setText("This is the event description of UtilityEvent");
	
		jp.add(descriptionLabel);
		jp.add(buttonRight);

		return jp;
	}
	
	public JPanel tileNotOwnedEvent(){
		return getBuyTileWindow();
	}
	
	public void ownedSecondStep() {
		Random r = new Random();
		int roll = r.nextInt(10) + 2;
		int multiplier = 4;
		if (gameClient.hasBothUtilities())
			multiplier = 10;
		final int fee = roll * multiplier;

		jp.remove(buttonRight);

		buttonRight = new JButton("Pay");
		buttonRight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// SHOW THE ROLL VALUE AND TELL WHAT THEY HAVE TO PAY
				// CHANGE THE BUTTOM TEXT AND LISTENER
				gameClient.payFee(fee, sendNetMessage);
				System.out.println(gameClient.getCurrentPlayer().getAccount());
//				gameClient.sendTransactionSuccesToGUI(sendNetMessage);
			}
		});
		descriptionLabel.setText("You rolled a " + roll + ", so the fee to pay is "
				+ fee);
		jp.add(buttonRight);
		
		
	}

}
