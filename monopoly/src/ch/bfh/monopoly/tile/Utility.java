package ch.bfh.monopoly.tile;

import java.awt.BorderLayout;
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

public class Utility extends Property implements EventPanelSource {

	EventPanelFactory epf;
	ResourceBundle rb;
	String description, description1Owned, description2Owned;
	int rent1Owned;
	int rent2Owned;

	public Utility(String name, int price, String group, int mortgageValue,
			int coordX, int coordY, int id, EventManager em, Player bank,
			GameClient gameClient, ResourceBundle rb) {
		super(name, price, group, mortgageValue, coordX, coordY, id, em, bank,
				gameClient, rb);
		this.name = name;
		this.price = price;
		this.mortgageValue = mortgageValue;
		this.rb = rb;
		description = rb.getString("utility-description");
		description1Owned = rb.getString("utility-description1");
		description2Owned = rb.getString("utility-description2");
		rent1Owned = Integer.parseInt(rb.getString("tile12-rent").trim());
		rent2Owned = Integer.parseInt(rb.getString("tile12-rent1house").trim());

	}

	public int feeToCharge() {
		// TODO This method must be here because it extends property, but it
		// can't be called unless there is a roll value..
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
		epf = new EventPanelFactory(this, gameClient.getSubjectForPlayer());
		if (mortgageActive){
			epf.changePanel(getTileIsMortgagedEPI(epf));
			return epf.getJPanel();
		}
		boolean owned = (owner.getName() != "bank");
		System.out.println("owner: " + owner.getName()
				+ " and currentPlayerName: "
				+ gameClient.getCurrentPlayer().getName());
		if (owned) {
			if (gameClient.getCurrentPlayer().getName().equals(owner.getName()))
				epf.changePanel(Step.TILE_OWNED_BY_YOU);
			else
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
		String extendedDescription;

		switch (step) {
		case TILE_NOT_OWNED:
			epi = super.getTileNotOwnedEPI(epf);
			break;
		case TILE_NOT_OWNED2:
			epi = super.getTileNotOwnedEPI2(epf);
			break;
		case TILE_OWNED:
			epi = new EventPanelInfo(gameClient);

			extendedDescription = description + " \n\n" + description1Owned;
			if (gameClient.hasBothUtilities())
				extendedDescription = description + " \n\n" + description2Owned;

			al = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					epf.changePanel(Step.SECOND_STEP);
				}
			};

			epi.setText(extendedDescription);
			epi.addButton(rb.getString("roll"), 0, al);
			break;
		case SECOND_STEP:
			Random r = new Random();
			int roll = r.nextInt(10) + 2;
			int multiplier = rent1Owned;
			if (gameClient.hasBothUtilities()) {
				multiplier = rent2Owned;
			}
			final int fee = roll * multiplier;

			epi = new EventPanelInfo(gameClient);
			al = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Player account before:"
							+ gameClient.getCurrentPlayer().getAccount());
					gameClient.payUtilityFee(fee, sendNetMessage);
					System.out.println("Player account after:"
							+ gameClient.getCurrentPlayer().getAccount());
					if (gameClient.isDoublesRoll()) {
						epf.setEventPanelSource(gameClient.getDice());
						epf.changePanel(Step.DOUBLES_TRANSITION);
					} else {
						gameClient.sendTransactionSuccesToGUI(sendNetMessage);
						epf.disableAfterClick();
					}
				}
			};
			// check if player has enough money
			extendedDescription = "";
			if (fee > gameClient.getCurrentPlayer().getAccount()) {
				extendedDescription = rb.getString("youDontHaveEnough");
			}
			epi.setText("You rolled a " + roll + ", so the fee to pay is "
					+ fee + "\n\n" + extendedDescription);
			epi.addButton(rb.getString("pay"), fee, al);
			break;
		case TILE_OWNED_BY_YOU:
			epi = super.getTileOwnedByYouEPI(epf);
			break;
		default:
			epi = gameClient.getEventPanelInfoFromDice(step);
			break;
		}
		return epi;
	}

}
