package ch.bfh.monopoly.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.gui.EventPanelFactory;
import ch.bfh.monopoly.tile.EventPanelInfo;
import ch.bfh.monopoly.tile.Step;

public class GoToJailEvent extends MovementEvent{

	EventPanelFactory epf;
	
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
					epf.disableAfterClick();
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
