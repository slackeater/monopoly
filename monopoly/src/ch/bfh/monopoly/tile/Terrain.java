package ch.bfh.monopoly.tile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JPanel;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.Player;
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
			Player bank,GameClient gameClient, ResourceBundle rb) {
		super(name, price, group, mortgageValue, coordX, coordY, tileId, em, bank, gameClient, rb);
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
	
	public void removeHouse() {
		houseCount--;
	}

	public void removeHotel() {
		hotelCount--;
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


	public int getHotelCost() {
		return hotelCost;
	}


	public int getHouseCount() {
		return houseCount;
	}

	public int getHotelCount() {
		return hotelCount;
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
//				buttonRight.setEnabled(false);
				eventInfoLabel.setText(thankYouRent);
				buttonRight.removeAll();
				buttonRight.setText(buttonTextContinue);
				buttonRight.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						jpanel.removeAll();
						gameClient.sendTransactionSuccesToGUI(sendNetMessage);
						buttonRight.setEnabled(false);
					}
				});
				System.out.println("The owner's bank account balance: " + owner.getAccount());
				System.out.println("The buyer's bank account balance: " + gameClient.getCurrentPlayer().getAccount());
			}
		});
		buttonRight.setText(buttonTextPay);
		eventInfoLabel
				.setText(name+" "+msgIsOwned+" "+ owner.getName() + ".  \n" + msgIsOwnedRent +" "+ feeToCharge());
	
		jpanel.add(eventInfoLabel);
		jpanel.add(buttonRight);

		return jpanel;
	}
	
	public JPanel tileNotOwnedEvent(){
		return getBuyTileWindow();
	}
	
	
}
