package ch.bfh.monopoly.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;

public class getJailCardEvent extends AbstractTileEvent{

	public getJailCardEvent(String name, String eventDescription,
			GameClient gameClient) {
		super(name, eventDescription, gameClient);
	}

	@Override
	public void performEvent() {
		gameClient.winJailCard(sendNetMessage);
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
