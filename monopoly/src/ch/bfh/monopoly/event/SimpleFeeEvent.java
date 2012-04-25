package ch.bfh.monopoly.event;

import ch.bfh.monopoly.common.GameClient;

public class SimpleFeeEvent extends AbstractTileEvent {

	public SimpleFeeEvent(String name, GameClient gameClient) {
		super(
				name,
				"If you are not the owner of this tile, you must pay rent.  The rent for this tile is ",
				gameClient);
	}

	@Override
	public void performEvent() {
		String currentPlayer = gameClient.getCurrentPlayer().getName();
		int currentPos = gameClient.getCurrentPlayer().getPosition();
		if (!(gameClient.playerIsOwnerOfTile(currentPlayer, currentPos))) {
			int fee = gameClient.getFeeForTileAtId(currentPos);
			if (!(gameClient.hasSufficientFunds(fee))) throw new RuntimeException("Current Player does not have enough money to pay fee");
			// TODO how do we allow player to perform other actions if he
			// doens't have enough money to pay fee?
			gameClient.getCurrentPlayer().withdawMoney(fee);
		}

	}

	@Override
	public String getEventDescription() {
		int tileId = gameClient.getCurrentPlayer().getPosition();
		int rent = gameClient.getFeeForTileAtId(tileId);
		String eventDescription = super.getEventDescription() + rent;
		if (!(gameClient.hasSufficientFunds(rent)))
			eventDescription=super.getEventDescription() + rent+ "\nBut you do not have sufficient fund to pay!";
		return eventDescription;
	}

}
