package ch.bfh.monopoly.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.gui.EventPanelFactory;
import ch.bfh.monopoly.tile.EventPanelInfo;
import ch.bfh.monopoly.tile.Step;

public class GetFixedSumEvent extends AbstractTileEvent {

	int fixedSum;
	EventPanelFactory epf;
	
	public GetFixedSumEvent(String name, String eventDescription, int fixedSum,
			GameClient gameClient) {
		super(name, eventDescription, gameClient);
		this.fixedSum=fixedSum;
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
			epi = new EventPanelInfo(gameClient);
			buttonText = "ok";
			al =new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					epf.disableAfterClick();
					performEvent();
					if (gameClient.isDoublesRoll()) {
						epf.setEventPanelSource(gameClient.getDice());
						epf.changePanel(Step.DOUBLES_TRANSITION);
					} else {
						gameClient.sendTransactionSuccesToGUI(sendNetMessage);
					}
				}
			};
			epi.setText(eventDescription);
			epi.addButton(buttonText, -1*fixedSum, al);
			break;

		default:
			epi = gameClient.getEventPanelInfoFromDice(step);
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
