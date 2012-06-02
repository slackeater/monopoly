package ch.bfh.monopoly.tile;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.event.EventManager;

public abstract class Property extends AbstractTile implements IProperty {

	protected int price;
	protected Player owner;
	protected int mortgageValue;
	protected boolean mortgageActive;
	protected String msgIsOwned;
	protected String msgIsOwnedRent;
	protected String msgIsNotOwned;
	protected String msgYouBought;
	protected String thankYouRent;
	protected String buttonTextBuy;
	protected String buttonTextPay;
	protected String buttonTextContinue;
	private String group;
	private ActionListener al;
	

	public Property(String name, int price, String group, int mortgageValue,
			int coordX, int coordY, int tileId, EventManager em, Player bank,
			GameClient gameClient, ResourceBundle rb) {
		super(name, coordX, coordY, tileId, em, gameClient);
		this.price = price;
		this.group = group;
		this.owner = bank;
		this.mortgageValue = mortgageValue;
		this.mortgageActive = false;
		this.msgIsNotOwned= rb.getString("isNotOwned");
		this.msgIsOwned= rb.getString("isOwned");
		this.msgIsOwnedRent= rb.getString("isOwnedRent");
		this.msgYouBought= rb.getString("youBought");
		this.thankYouRent= rb.getString("thankYouRent");
		this.buttonTextPay = rb.getString("payButton");
		this.buttonTextBuy= rb.getString("buyButton");
		this.buttonTextContinue= rb.getString("continueButton");
	}

	public JPanel getBuyTileWindow() {
		eventInfoLabel.setText(name +" "+msgIsNotOwned);
		jpanel.add(eventInfoLabel, BorderLayout.CENTER);
		buttonRight = new JButton(buttonTextBuy);
		
		
		al =new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				gameClient.buyCurrentPropertyForPlayer("currentPlayer",
						sendNetMessage);
				System.out.println(gameClient.getCurrentPlayer().getAccount());
				System.out.println(gameClient.getCurrentPlayer().ownsProperty(
						(Tile) Property.this));
				buttonRight.removeActionListener(al);
				buttonRight.setText(buttonTextContinue);
				al =new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						jpanel.removeAll();
						gameClient.sendTransactionSuccesToGUI(sendNetMessage);
						buttonRight.setEnabled(false);
					}
				};
				buttonRight.addActionListener(al);
				eventInfoLabel.setText(msgYouBought +" "+ name);
				

			}
		};
		
		
		buttonRight.addActionListener(al);
		jpanel.add(buttonRight,BorderLayout.SOUTH);
		return jpanel;
	}

	public boolean isMortgageActive() {
		return mortgageActive;
	}

	public void setMortgageActive(boolean mortgageActive) {
		this.mortgageActive = mortgageActive;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public int getMortgageValue() {
		return mortgageValue;
	}

	public void setMortgageValue(int mortgageValue) {
		this.mortgageValue = mortgageValue;
	}

}
