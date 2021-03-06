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
import ch.bfh.monopoly.gui.EventPanelFactory;

public abstract class Property extends AbstractTile implements IProperty {

	protected int price;
	protected Player owner;
	protected int mortgageValue;
	protected boolean mortgageActive;
	protected String msgIsOwned, msgIsOwnedRent, msgIsNotOwned, msgYouBought,
			thankYouRent, buttonTextBuy, buttonTextDontBuy, buttonTextPay,
			buttonTextContinue, group, ownedByYou, wishToBuy, thePriceIs,
			youDontHaveEnough,isMortgaged;
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
		this.msgIsNotOwned = rb.getString("isNotOwned");
		this.msgIsOwned = rb.getString("isOwned");
		this.msgIsOwnedRent = rb.getString("isOwnedRent");
		this.msgYouBought = rb.getString("youBought");
		this.thankYouRent = rb.getString("thankYouRent");
		this.buttonTextPay = rb.getString("payButton");
		this.buttonTextBuy = rb.getString("buyButton");
		this.buttonTextDontBuy = rb.getString("dontButButton");
		this.buttonTextContinue = rb.getString("continueButton");
		this.ownedByYou = rb.getString("ownedByYou");
		this.wishToBuy = rb.getString("wishToBuy");
		this.thePriceIs = rb.getString("thePriceIs");
		this.youDontHaveEnough = rb.getString("youDontHaveEnough");
		this.isMortgaged=rb.getString("isMortgaged");
	}

	protected String getPayRentText(int fee) {
		return name + " " + msgIsOwned + " " + owner.getName() + ".  \n\n"
				+ msgIsOwnedRent + " \n" + fee;
	}

	protected EventPanelInfo getTileNotOwnedEPI(EventPanelFactory epfIn) {
		final EventPanelFactory epf = epfIn;
		EventPanelInfo epi = new EventPanelInfo(gameClient);
		ActionListener dontBuy = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gameClient.isDoublesRoll()) {
					epf.disableAfterClick();
					epf.setEventPanelSource(gameClient.getDice());
					epf.changePanel(Step.DOUBLES_TRANSITION);
				} else {
					epf.disableAfterClick();
					gameClient.sendTransactionSuccesToGUI(sendNetMessage);
				}
			}
		};
		al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameClient.buyCurrentPropertyForPlayer("currentPlayer",
						sendNetMessage);
				System.out.println(gameClient.getCurrentPlayer().getAccount());
				System.out.println(gameClient.getCurrentPlayer().ownsProperty(
						Property.this));
				epf.changePanel(Step.TILE_NOT_OWNED2);
			}
		};

		String extendedDescription = wishToBuy;
		if (price > gameClient.getCurrentPlayer().getAccount())
			extendedDescription = youDontHaveEnough;

		epi.setText(name + " " + msgIsNotOwned + " \n\n" + thePriceIs + "\n\n "
				+ price + " " + extendedDescription);
		epi.addButton(buttonTextDontBuy, 0, dontBuy);
		epi.addButton(buttonTextBuy, price, al);
		return epi;
	}

	protected EventPanelInfo getTileNotOwnedEPI2(EventPanelFactory epfIn) {
		final EventPanelFactory epf = epfIn;
		EventPanelInfo epi = new EventPanelInfo(gameClient);
		al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (gameClient.isDoublesRoll()) {
					epf.setEventPanelSource(gameClient.getDice());
					epf.changePanel(Step.DOUBLES_TRANSITION);
				} else {
					gameClient.sendTransactionSuccesToGUI(sendNetMessage);
					epf.disableAfterClick();
				}
			}
		};

		epi.setText(msgYouBought + " " + name);
		epi.addButton(buttonTextContinue, 0, al);
		return epi;
	}

	protected EventPanelInfo getTileOwnedByYouEPI(EventPanelFactory epfIn) {
		final EventPanelFactory epf = epfIn;
		EventPanelInfo epi = new EventPanelInfo(gameClient);
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gameClient.isDoublesRoll()) {
					epf.setEventPanelSource(gameClient.getDice());
					epf.changePanel(Step.DOUBLES_TRANSITION);
				} else {
					gameClient.sendTransactionSuccesToGUI(sendNetMessage);
					epf.disableAfterClick();
				}
			}
		};
		epi.setText(ownedByYou);
		epi.addButton(buttonTextContinue, 0, al);
		return epi;
	}

	protected EventPanelInfo getTileIsMortgagedEPI(EventPanelFactory epfIn) {
		final EventPanelFactory epf = epfIn;
		EventPanelInfo epi = new EventPanelInfo(gameClient);
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameClient.sendTransactionSuccesToGUI(sendNetMessage);
				epf.disableAfterClick();
			}

		};
		epi.setText(isMortgaged);
		epi.addButton(buttonTextContinue, 0, al);
		return epi;
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
