package ch.bfh.monopoly.event;

import ch.bfh.monopoly.common.GameClient;

public class PayUtilityEvent extends AbstractTileEvent {

	public PayUtilityEvent(String name, String eventDescription,
			GameClient gameClient) {
		super(name, eventDescription, gameClient);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void performEvent() {
		//TODO somehow the amount rolled needs to be communicated to the GUI
		int roll = 9;
		int multiplier = 4;
		if (gameClient.hasBothUtilities())
			multiplier = 10;
		else
			multiplier = 4;
		
		int fee = roll*multiplier;
		if (!gameClient.hasSufficientFunds(fee))
			throw new RuntimeException("Player does not have enough money to pay this fee");
		gameClient.payFee(fee,true);
		

	}
}
