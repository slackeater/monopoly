package ch.bfh.monopoly.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

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
		int housesOnBoard = 32 - gameClient.getAvailableHouses();
		int hotelsOnBoard = 12 - gameClient.getAvailableHotels();
		int fee = housesOnBoard * chargePerHouse + hotelsOnBoard * chargePerHotel;
		gameClient.payFee(fee, sendNetMessage);
	}

	@Override
	public JPanel getTileEventPanel() {
		int housesOnBoard = 32 - gameClient.getAvailableHouses();
		int hotelsOnBoard = 12 - gameClient.getAvailableHotels();
		eventDescription="There are " + housesOnBoard + " houses on the board, and " + hotelsOnBoard +" hotels on the board.  You must pay "+chargePerHouse + "for each house, and " + chargePerHotel + " for each hotel";
		ActionListener al =new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				performEvent();
				gameClient.sendTransactionSuccesToGUI(sendNetMessage);
			}
		};
		return super.getTileEventPanel(al);
	}
	
}
