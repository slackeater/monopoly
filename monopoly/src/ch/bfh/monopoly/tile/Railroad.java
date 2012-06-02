package ch.bfh.monopoly.tile;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JPanel;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.event.EventManager;

public class Railroad extends Property {
	private int rent;
	private int[] rrMultiplier = { 1, 2, 4, 8 };

	public Railroad(String name, int price, int rent, String group,
			int mortgageValue, int coordX, int coordY, int tileId,
			EventManager em, Player bank, GameClient gameClient,
			ResourceBundle rb) {
		super(name, price, group, mortgageValue, coordX, coordY, tileId, em,
				bank, gameClient, rb);
		this.rent = rent;
	}

	// calculates the fee to charge by checking how many RRs are owned by the
	// owner of this tile
	// the base rent value of 25 is mutilplied by 1,2,4, or 8 depending on the
	// number of RRs owned
	public int feeToCharge() {
		int numberOfRRsOwned = this.owner.numberRailRoadsOwned();
		return rrMultiplier[numberOfRRsOwned - 1] * rent;
	}



	public String toString() {
		return super.toString() + "\nname: " + name + "\nprice: " + this.price
				+ "\nrent: " + this.rent + "\nmortgageValue: " + mortgageValue
				+ "\nowner: " + owner;
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
				gameClient.payRent(sendNetMessage);
				buttonRight.setEnabled(false);
				eventInfoLabel.setText(thankYouRent);
				gameClient.sendTransactionSuccesToGUI(sendNetMessage);
			}
		});
		buttonRight.setText(buttonTextPay);
		eventInfoLabel
				.setText(name+" "+msgIsOwned+" "+ owner.getName() + ".  \n" + msgIsOwnedRent +" "+ feeToCharge());
	
		jpanel.add(eventInfoLabel,BorderLayout.CENTER);
		jpanel.add(buttonRight,BorderLayout.SOUTH);

		return jpanel;
	}
	
	public JPanel tileNotOwnedEvent(){
		return getBuyTileWindow();
	}
}
