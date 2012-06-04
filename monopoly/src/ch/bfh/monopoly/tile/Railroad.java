package ch.bfh.monopoly.tile;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JPanel;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.event.EventManager;

public class Railroad extends Property implements EventPanelSource {
	private int rent;
	private int[] rrMultiplier = { 1, 2, 4, 8 };
	EventPanelFactory epf;

	public Railroad(String name, int price, int rent, String group,
			int mortgageValue, int coordX, int coordY, int tileId,
			EventManager em, Player bank, GameClient gameClient,
			ResourceBundle rb) {
		super(name, price, group, mortgageValue, coordX, coordY, tileId, em,
				bank, gameClient, rb);
		this.rent = rent;
		epf = new EventPanelFactory(this);
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
		boolean owned = !(owner.getName() == "bank");
		if (owned) {
			epf.changePanel(Step.TILE_OWNED);
			return epf.getJPanel();
		} else
			epf.changePanel(Step.TILE_NOT_OWNED);
		return epf.getJPanel();
	}

	public EventPanelInfo getEventPanelInfoForStep(Step step) {
		String labelText;
		String buttonText;
		ActionListener al;
		EventPanelInfo epi;

		switch (step) {
		case TILE_NOT_OWNED:
			epi = super.getTileNotOwnedEPI(epf);
			break;
		case TILE_NOT_OWNED2:
			epi = super.getTileNotOwnedEPI2(epf);
			break;
		case TILE_OWNED:
			epi = new EventPanelInfo();
			al = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gameClient.payRent(sendNetMessage);
					gameClient.sendTransactionSuccesToGUI(sendNetMessage);
					epf.changePanel(Step.TILE_OWNED2);
				}
			};
			epi.setText(getPayRentText(feeToCharge()));
			epi.addButtonText(buttonTextPay);
			epi.addActionListener(al);
			break;
		case TILE_OWNED2:
			epi = new EventPanelInfo();
			al = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gameClient.sendTransactionSuccesToGUI(sendNetMessage);
					epf.disableAfterClick();
				}
			};
			epi.setText(this.thankYouRent);
			epi.addButtonText(buttonTextContinue);
			epi.addActionListener(al);
			break;
		default:
			epi = new EventPanelInfo();
			labelText = "No case defined";
			buttonText = "ok";
			al = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gameClient.sendTransactionSuccesToGUI(sendNetMessage);
				}
			};
			epi.setText(labelText);
			epi.addActionListener(al);
			epi.addButtonText(buttonText);
			break;
		}
		return epi;
	}
}
