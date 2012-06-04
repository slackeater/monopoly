package ch.bfh.monopoly.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.tile.EventPanelFactory;
import ch.bfh.monopoly.tile.EventPanelInfo;
import ch.bfh.monopoly.tile.Step;

public class GetFixedSumEvent extends AbstractTileEvent {

	int fixedSum;
	EventPanelFactory epf;
	
	public GetFixedSumEvent(String name, String eventDescription, int fixedSum,
			GameClient gameClient) {
		super(name, eventDescription, gameClient);
		this.fixedSum=fixedSum;
		epf = new EventPanelFactory(this);
		
	}

	@Override
	public void performEvent() {
		if (fixedSum<0)
			gameClient.payFee((fixedSum*-1), sendNetMessage);
		else
			gameClient.transferMoney("bank", gameClient.getCurrentPlayer().getName(), fixedSum, sendNetMessage);
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
