package ch.bfh.monopoly.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.gui.MonopolyGUI;

public class MovementEvent extends AbstractTileEvent {

	int newPosition;
	
	public MovementEvent(String name, String eventDescription, int newPosition,
			GameClient gameClient) {
		super(name, eventDescription, gameClient);
		this.newPosition = newPosition;

	}

	@Override
	public String getEventDescription() {
		return eventDescription;
	}

	@Override
	public void performEvent() {
		
		if (newPosition > 0) {
			//GREATER than 0, advance as normal
			gameClient.advancePlayerToTile(newPosition, sendNetMessage);
		}
		else if (newPosition == 0 ){
			//EQUAL to 0, then it's go to JAIL
			gameClient.goToJail(sendNetMessage);
			}
		else {
			gameClient.advancePlayerNSpacesInDirection((newPosition*-1), MonopolyGUI.Direction.BACKWARDS, sendNetMessage);
		}
		
	}
	
	
	@Override
	public JPanel getTileEventPanel() {
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
