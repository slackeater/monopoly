package ch.bfh.monopoly.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.exception.TransactionException;

public class birthdayEvent extends AbstractTileEvent {

	public birthdayEvent(String name, String eventDescription,
			GameClient gameClient) {
		super(name, eventDescription, gameClient);
	}

	@Override
	public void performEvent() {
		int fee = 20;//TODO SET amount according to locale
		gameClient.birthdayEvent(fee, sendNetMessage);


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
