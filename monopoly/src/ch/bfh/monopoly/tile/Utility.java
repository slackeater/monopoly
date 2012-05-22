package ch.bfh.monopoly.tile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.common.WindowBuilder;
import ch.bfh.monopoly.event.EventManager;

public class Utility extends Property {

	JPanel jp = new JPanel();
	JButton buttonRight;
	JButton buttonLeft;
	JLabel descriptionLabel;

	public Utility(String name, int price, String group, int mortgageValue,
			int coordX, int coordY, int id, EventManager em, Player bank) {
		super(name, price, group, mortgageValue, coordX, coordY, id, em, bank);
		this.name = name;
		this.price = price;
		this.mortgageValue = mortgageValue;
	}

	// TODO this method need to be implemented in a TileEvent and accessed
	// through event manager
	public int feeToCharge() {
		// check if owner of this utility owns the other utility
		// generate a random number to simulate ROLL
		// if owner owns other utility return randNumber * 10
		// else return randNumber * 4
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

		buttonRight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showSecondStep(this);
			}
		});
		descriptionLabel
				.setText("This is the event description of UtilityEvent");
		buttonRight = new JButton("Roll");
		jp.add(descriptionLabel);
		jp.add(buttonRight);

		return jp;
	}

	public void showSecondStep(ActionListener al) {
		Random r = new Random();
		int roll = r.nextInt(10) + 2;
		int multiplier = 4;
		if (em.gameClient.hasBothUtilities())
			multiplier = 10;
		final int fee = roll * multiplier;

		jp.remove(buttonRight);

		buttonRight = new JButton("Pay");
		buttonRight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// SHOW THE ROLL VALUE AND TELL WHAT THEY HAVE TO PAY
				// CHANGE THE BUTTOM TEXT AND LISTENER
				em.gameClient.payFee(fee, sendNetMessage);
			}
		});
		descriptionLabel.setText("You rolled a " + r + " so the fee to pay is "
				+ fee);
		jp.add(buttonRight);
	}

	/**
	 * get the window builder object needed for the GUI to display a window in
	 * response to landing on a tile
	 * 
	 * @param sendNetMessage
	 *            true if a net message should be sent to the server
	 */
	@Override
	public WindowBuilder getWindowBuilder() {
		return new WindowBuilder(name, getEventDescription(),
				getActionListenerList());

	}

	/**
	 * creates the actionListeners that the GUI should display in response to a
	 * player landing on this tile
	 * 
	 * @return a list of actionListeners for the GUI to add to buttons
	 */
	public List<ActionListener> getActionListenerList() {
		List<ActionListener> actionList = new ArrayList<ActionListener>();
		// check if this property is already owned
		boolean owned = true;
		if (owner.getName().equals("bank"))
			owned = false;

		if (owned) {
			ActionListener al = new ActionListener() {
				// TODO implement a means to ROLL DICE AND then PAY RENT
				@Override
				public void actionPerformed(ActionEvent e) {
					performEvent();

				}
			};
			actionList.add(al);
		} else {
			ActionListener al = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					em.gameClient.buyCurrentPropertyForPlayer("currentPlayer",
							true);
				}
			};
			actionList.add(al);
		}
		return actionList;
	}

	/**
	 * perform the action that this tile causes if a player lands on it
	 */
	@Override
	public void performEvent() {
		em.performEventForTileAtId(tileId);

	}

	/**
	 * get the text that should be displayed when a play lands on this tile
	 */
	@Override
	public String getEventDescription() {
		return em.getEventDescriptionById(tileId);
	}

}
