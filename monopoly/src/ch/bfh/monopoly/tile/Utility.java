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

	JPanel jpanel;
	JButton buttonRight;
	JButton buttonLeft;
	EventPanelFactory epf;

	public Utility(String name, int price, String group, int mortgageValue,
			int coordX, int coordY, int id, EventManager em, Player bank,
			GameClient gameClient, ResourceBundle rb) {
		super(name, price, group, mortgageValue, coordX, coordY, id, em, bank,
				gameClient, rb);
		this.name = name;
		this.price = price;
		this.mortgageValue = mortgageValue;
		epf = new EventPanelFactory(this);
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

	public EventPanelInfo getEventPanelInfoForStep(Step step) {
		String labelText;
		String buttonText;
		ActionListener al;
		EventPanelInfo epi;

		switch (step) {
		case GET_EVENT:
			epi = new EventPanelInfo();
			al = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					epf.changePanel(Step.SECOND_STEP);
				}
			};

			epi.setText("This is the event description of UtilityEvent");
			epi.addButtonText("roll");
			epi.addActionListener(al);
			break;
		case SECOND_STEP:

			Random r = new Random();
			int roll = r.nextInt(10) + 2;
			int multiplier = 4;
			if (gameClient.hasBothUtilities())
				multiplier = 10;
			final int fee = roll * multiplier;

			epi = new EventPanelInfo();
			al = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Player account before:" + gameClient.getCurrentPlayer()
							.getAccount());
					gameClient.payUtilityFee(fee, sendNetMessage);
					System.out.println("Player account after:" + gameClient.getCurrentPlayer()
							.getAccount());
					gameClient.sendTransactionSuccesToGUI(sendNetMessage);
					epf.disableAfterClick();
				}
			};

			epi.setText("You rolled a " + roll + ", so the fee to pay is "
					+ fee);
			epi.addButtonText("pay");
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

	@Override
	public JPanel getTileEventPanel() {

		boolean owned = !(owner.getName() == "bank");
		if (owned) {
			epf.changePanel(Step.GET_EVENT);
			return epf.getJPanel();
		} else
			return tileNotOwnedEvent();

	}

	public JPanel tileNotOwnedEvent() {
		return getBuyTileWindow();
	}

}
