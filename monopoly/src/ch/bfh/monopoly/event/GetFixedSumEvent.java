package ch.bfh.monopoly.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;

public class GetFixedSumEvent extends AbstractTileEvent {

	int fixedSum;
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
