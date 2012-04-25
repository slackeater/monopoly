package ch.bfh.monopoly.event;

import ch.bfh.monopoly.common.GameClient;

public class TileEvent extends AbstractTileEvent{

	public TileEvent(String name, String eventDescription, GameClient gameClient) {
		super(name, eventDescription, gameClient);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void performEvent() {
		String currentPlayer = gameClient.getCurrentPlayer().getName();
		int currentPos = gameClient.getCurrentPlayer().getPosition();
		if (!(gameClient.playerIsOwnerOfTile(currentPlayer, currentPos)));
				int fee = gameClient.getFeeForTileAtId(currentPos);
				//TODO how do we allow player to perform other actions if he doens't have enough money to pay fee?
		
	}
	
	

}
