package ch.bfh.monopoly.event;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.gui.MonopolyGUI;
import ch.bfh.monopoly.tile.EventPanelFactory;
import ch.bfh.monopoly.tile.EventPanelInfo;
import ch.bfh.monopoly.tile.EventPanelSource;
import ch.bfh.monopoly.tile.Step;

public class MovementEvent extends AbstractTileEvent {

	int newPosition;
	EventPanelFactory epf;
	
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
		
		if (newPosition == 10 ){
			//EQUAL to 10, then it's go to JAIL
			gameClient.goToJail(sendNetMessage);
			}
		else if (newPosition >= 0) {
			//GREATER than 0, advance as normal
			gameClient.advancePlayerToTile(newPosition, sendNetMessage);
		}
		else {
			gameClient.advancePlayerNSpacesInDirection((newPosition*-1), MonopolyGUI.Direction.BACKWARDS, sendNetMessage);
		}
		
	}
	

	
	public EventPanelInfo getEventPanelInfoForStep(Step step) {
		String labelText;
		String buttonText;
		ActionListener al;
		EventPanelInfo epi;

		switch (step) {
		case GET_EVENT:	
			epi = new EventPanelInfo(gameClient);
			buttonText = "ok";
			
			al =new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					performEvent();
					gameClient.sendTransactionSuccesToGUI(sendNetMessage);
				}
			};
			epi.setText(eventDescription);
			epi.addButton(buttonText, 0, al);
			break;

		default:
			epi = new EventPanelInfo(gameClient);
			labelText = "No case defined";
			buttonText = "ok";
			al = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gameClient.sendTransactionSuccesToGUI(sendNetMessage);
				}
			};
			epi.setText(labelText);
			epi.addButton(buttonText, 0, al);
			break;
		}
		return epi;
	}
	
	
	@Override
	public JPanel getTileEventPanel() {
		epf = new EventPanelFactory(this, gameClient.getSubjectForPlayer());
		epf.changePanel(Step.GET_EVENT);
		return epf.getJPanel();
	}
}
