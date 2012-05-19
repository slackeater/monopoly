package ch.bfh.monopoly.event;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.exception.TransactionException;

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
		try {
			gameClient.getCurrentPlayer().withdawMoney(fee);
		} catch (TransactionException e) {
			gameClient.sendTransactionErrorToGUI(e,true);
		}
	}

	
}
