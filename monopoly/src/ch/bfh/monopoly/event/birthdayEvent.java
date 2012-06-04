package ch.bfh.monopoly.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.exception.TransactionException;
import ch.bfh.monopoly.tile.EventPanelFactory;
import ch.bfh.monopoly.tile.EventPanelInfo;
import ch.bfh.monopoly.tile.Step;

public class birthdayEvent extends AbstractTileEvent  {

	EventPanelFactory epf;
	
	public birthdayEvent(String name, String eventDescription,
			GameClient gameClient) {
		super(name, eventDescription, gameClient);
		epf = new EventPanelFactory(this);
	}

	@Override
	public void performEvent() {
		int fee = 20;//TODO SET amount according to locale
		gameClient.birthdayEvent(fee, sendNetMessage);


	}

	public EventPanelInfo getEventPanelInfoForStep(Step step) {
		String labelText;
		String buttonText;
		ActionListener al;
		EventPanelInfo epi;

		switch (step) {
		case GET_EVENT:	
			epi = new EventPanelInfo();
			al =new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					performEvent();
					gameClient.sendTransactionSuccesToGUI(sendNetMessage);
				}
			};
			epi.setText(eventDescription);
			epi.addButtonText("ok");
			epi.addActionListener(al);
			break;

		default:
			epi = new EventPanelInfo();
			labelText = "No case defined";
			buttonText = "ok";
			al = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gameClient.sendTransactionSuccesToGUI(sendNetMessage);
				}
			};
			epi.setText(labelText);
			epi.addActionListener(al);
			epi.addButtonText(buttonText);
			break;
		}
		return epi;
	}
	
	
	@Override
	public JPanel getTileEventPanel() {
		epf.changePanel(Step.GET_EVENT);
		return epf.getJPanel();
	}
}
