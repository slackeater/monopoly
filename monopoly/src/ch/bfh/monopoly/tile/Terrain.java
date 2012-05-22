package ch.bfh.monopoly.tile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.common.WindowBuilder;
import ch.bfh.monopoly.event.EventManager;

public class Terrain extends Property {

	private int houseCost;
	private int hotelCost;
	private int houseCount;
	private int hotelCount;
	private int[] rentRates = new int[5];
	private int renthotel;
	private String rgb;

	public Terrain(String name, int price, int houseCost, int hotelCost,
			int rent, int rent1house, int rent2house, int rent3house,
			int rent4house, int renthotel, String group, int mortgageValue,
			int coordX, int coordY, int tileId, String rgb, EventManager em,
			Player bank) {
		super(name, price, group, mortgageValue, coordX, coordY, tileId, em, bank);
		this.name = name;
		// this.id = id;
		this.rentRates[0] = rent;
		this.rentRates[1] = rent1house;
		this.rentRates[2] = rent2house;
		this.rentRates[3] = rent3house;
		this.rentRates[4] = rent4house;
		this.renthotel = renthotel;
		this.price = price;
		this.houseCost = houseCost;
		this.hotelCost = hotelCost;
		this.mortgageValue = mortgageValue;
		this.rgb = rgb;
		mortgageActive = false;
		hotelCount = 0;
		houseCount = 0;
	}

	public void buildHouse() {
		houseCount++;
	}

	public void buildHotel() {
		hotelCount++;
	}

	public int getRentByHouseCount(int houseCount) {
		return rentRates[houseCount];
	}

	public int getRentHotel() {
		return renthotel;
	}

	public int feeToCharge() {
		return rentRates[houseCount] + renthotel * hotelCount;
	}

	public int getHouseCost() {
		return houseCost;
	}

	public void setHouseCost(int houseCost) {
		this.houseCost = houseCost;
	}

	public int getHotelCost() {
		return hotelCost;
	}

	public void setHotelCost(int hotelCost) {
		this.hotelCost = hotelCost;
	}

	public int getHouseCount() {
		return houseCount;
	}

	public void setHouseCount(int houseCount) {
		this.houseCount = houseCount;
	}

	public int getHotelCount() {
		return hotelCount;
	}

	public void setHotelCount(int hotelCount) {
		this.hotelCount = hotelCount;
	}

	public boolean isMortgageActive() {
		return mortgageActive;
	}

	public void setMortgageActive(boolean mortgageActive) {
		this.mortgageActive = mortgageActive;
	}

	public String getRGB() {
		return this.rgb;
	}

	public String toString() {
		return super.toString() + "\nname: " + name + "\nprice: " + this.price
				+ "\nhouseCost: " + houseCost + "\nhotelCost: " + hotelCost
				+ "\nmortgageValue: " + mortgageValue + "\nrent: "
				+ rentRates[0] + "\nrent w/Houses" + rentRates[1] + ", "
				+ rentRates[2] + ", " + rentRates[3] + ", " + rentRates[4]
				+ "\nrent w/Hotel" + renthotel + "\nowner: " + owner;
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
}
