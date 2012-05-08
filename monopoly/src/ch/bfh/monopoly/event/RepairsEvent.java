package ch.bfh.monopoly.event;

import ch.bfh.monopoly.common.GameClient;

public class RepairsEvent extends AbstractTileEvent{

	int chargePerHouse;
	int chargePerHotel;
	public RepairsEvent(String name, String eventDescription, int chargePerHouse,int chargePerHotel,
			GameClient gameClient) {
		super(name, eventDescription, gameClient);
		this.chargePerHotel = chargePerHotel;
		this.chargePerHouse=chargePerHouse;
	}

	@Override
	public void performEvent() {
		int housesOnBoard = 32-gameClient.getAvailableHouses();
		int hotelsOnBoard = 12-gameClient.getAvailableHotels();
		int fee = housesOnBoard * chargePerHouse + hotelsOnBoard * chargePerHotel;
		gameClient.getCurrentPlayer().withdawMoney(fee);
	}

	
}
