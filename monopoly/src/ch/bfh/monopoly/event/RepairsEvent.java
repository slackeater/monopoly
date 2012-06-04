package ch.bfh.monopoly.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.exception.TransactionException;
import ch.bfh.monopoly.tile.EventPanelFactory;
import ch.bfh.monopoly.tile.EventPanelInfo;
import ch.bfh.monopoly.tile.EventPanelSource;
import ch.bfh.monopoly.tile.Step;

public class RepairsEvent extends AbstractTileEvent{

	int chargePerHouse;
	int chargePerHotel;
	ResourceBundle rb = ResourceBundle.getBundle("ch.bfh.monopoly.resources.tile",
			gameClient.getLoc());
	EventPanelFactory epf;
	
	public RepairsEvent(String name, String eventDescription, int chargePerHouse,int chargePerHotel,
			GameClient gameClient) {
		super(name, eventDescription, gameClient);
		this.chargePerHotel = chargePerHotel;
		this.chargePerHouse=chargePerHouse;
		epf = new EventPanelFactory(this);
	}



	@Override
	public void performEvent() {
		int housesOnBoard = 32 - gameClient.getAvailableHouses();
		int hotelsOnBoard = 12 - gameClient.getAvailableHotels();
		int fee = housesOnBoard * chargePerHouse + hotelsOnBoard * chargePerHotel;
		gameClient.payFee(fee, sendNetMessage);
	}

	@Override
	public String getEventDescription() {
		int housesOnBoard = 32 - gameClient.getAvailableHouses();
		int hotelsOnBoard = 12 - gameClient.getAvailableHotels();
		return eventDescription=rb.getString("thereAre") + housesOnBoard + rb.getString("housesOnBoard") + hotelsOnBoard +" "+rb.getString("hotelsOnBoard")+" "+chargePerHouse + " "+ rb.getString("forEachHouse")+" " + chargePerHotel + rb.getString("forEachHotel");

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
			epi.setText(getEventDescription());
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
