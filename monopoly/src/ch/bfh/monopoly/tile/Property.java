package ch.bfh.monopoly.tile;

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
	private String group;
	protected JPanel jpanel = new JPanel();
	protected JButton buttonRight = new JButton();
	protected JButton buttonLeft = new JButton();
	protected JLabel eventInfoLabel = new JLabel();

	private ActionListener actionBuy = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			gameClient.buyCurrentPropertyForPlayer("currentPlayer",
					sendNetMessage);
			System.out.println(gameClient.getCurrentPlayer().getAccount());
			System.out.println(gameClient.getCurrentPlayer().ownsProperty(
					(Tile) Property.this));
			buttonRight = new JButton();
			buttonRight.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					gameClient.sendTransactionSuccesToGUI(sendNetMessage);
					System.out
							.println("The GUI should receive a message to hide this JPanel");
				}
			});
			buttonRight.setEnabled(false);
			eventInfoLabel.setText(msgYouBought +" "+ name);

		}
	};

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
		thankYouRent= rb.getString("thankYouRent");
		
	}

	public JPanel getBuyTileWindow() {
		eventInfoLabel.setText(name +" "+msgIsNotOwned);
		jpanel.add(eventInfoLabel);
		buttonRight = new JButton("Buy");
		buttonRight.addActionListener(actionBuy);
		jpanel.add(buttonRight);
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
