package ch.bfh.monopoly.event;

import ch.bfh.monopoly.common.GameClient;

public class GoToJailEvent extends MovementEvent{

	public GoToJailEvent(String name, String eventDescription, int newPosition,
			GameClient gameClient) {
		super(name, eventDescription, newPosition, gameClient);
		
	}
	
	@Override
	public void performEvent() {
		// TODO Auto-generated method stub
		super.performEvent();
		gameClient.goToJail(sendNetMessage);
	}
	
}
