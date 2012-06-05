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
	ResourceBundle rb = ResourceBundle.getBundle("ch.bfh.monopoly.resources.events",
			gameClient.getLoc());
	EventPanelFactory epf;
	
	public RepairsEvent(String name, String eventDescription, int chargePerHouse,int chargePerHotel,
			GameClient gameClient) {
		super(name, eventDescription, gameClient);
		this.chargePerHotel = chargePerHotel;
		this.chargePerHouse=chargePerHouse;
	}

	public int feeToCharge(){
		int housesOnBoard = 32 - gameClient.getAvailableHouses();
		int hotelsOnBoard = 12 - gameClient.getAvailableHotels();
		return housesOnBoard * chargePerHouse + hotelsOnBoard * chargePerHotel;
	}

	@Override
	public void performEvent() {
		gameClient.payFee(feeToCharge(), sendNetMessage);
	}

	@Override
	public String getEventDescription() {
		int housesOnBoard = 32 - gameClient.getAvailableHouses();
		int hotelsOnBoard = 12 - gameClient.getAvailableHotels();
		return eventDescription=name + "\n\n " + eventDescription +" \n\n"+rb.getString("thereAre")+ " "+ housesOnBoard +" "+ rb.getString("housesOnBoard") +" "+ hotelsOnBoard+"\n" +rb.getString("housesOnBoard")+" \n" + rb.getString("pay")+" feeToCharge()";       
				//OLD DESCRIPTION
//				" "+rb.getString("hotelsOnBoard")+" "+chargePerHouse + " "+ rb.getString("forEachHouse")+" " + chargePerHotel + " "+rb.getString("forEachHotel") +"\n\n"+ 

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
			epi.setText(getEventDescription());
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
