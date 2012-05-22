package ch.bfh.monopoly.tile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.common.WindowBuilder;
import ch.bfh.monopoly.event.EventManager;

public class Railroad extends Property {
	private int rent;
	private int[] rrMultiplier = {1,2,4,8};
	

	public Railroad (String name, int price, int rent, String group, int mortgageValue, int coordX, int coordY, int tileId,EventManager em,Player bank){ 
		super(name,  price,  group,  mortgageValue,  coordX,  coordY, tileId,em,bank);
		this.rent = rent;
	} 
	
	
	//calculates the fee to charge by checking how many RRs are owned by the owner of this tile
	//the base rent value of 25 is mutilplied by 1,2,4, or 8 depending on the number of RRs owned
	public int feeToCharge(){
		int numberOfRRsOwned = this.owner.numberRailRoadsOwned();
		return rrMultiplier[numberOfRRsOwned-1] * rent;
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
		boolean owned = true;
		List<ActionListener> actionList = new ArrayList<ActionListener>();

		if (owner.getName().equals("bank"))
			owned = false;

		if (owned) {
			ActionListener al = new ActionListener() {

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
	

	public int getRent() {
		return rent;
	}
	
	public void setRent(int rent) {
		this.rent = rent;
	}
	
	public String toString(){
		return super.toString() + "\nname: " + name +
				"\nprice: " + this.price +
				"\nrent: " + this.rent +
				"\nmortgageValue: " + mortgageValue +
				"\nowner: " + owner;
	}
}
