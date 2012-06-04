package ch.bfh.monopoly.tile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.event.BoardEvent;
import ch.bfh.monopoly.event.EventManager;

public class IncomeTax extends AbstractTile implements EventPanelSource{

	BoardEvent be;
	ResourceBundle rb = ResourceBundle.getBundle(
			"ch.bfh.monopoly.resources.tile", gameClient.getLoc());;
	String description;
	int fee;
	EventPanelFactory epf;
	
	public IncomeTax(String name, int fee, int coordX, int coordY, int tileId,
			EventManager em, GameClient gameClient) {
		super(name, coordX, coordY, tileId, em, gameClient);
		this.description = rb.getString("incomeTax-cardText");
		this.fee=fee;
		epf = new EventPanelFactory(this);
	}

	
	
	
	public EventPanelInfo getEventPanelInfoForStep(Step step) {
		String labelText;
		String buttonText;
		ActionListener al;
		EventPanelInfo epi;

		switch (step) {
		case GET_EVENT:	
			epi = new EventPanelInfo();


			final int amount = Integer.parseInt(rb.getString("tile4-rent"));
			
			ActionListener tenPercent = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					gameClient.payIncome10Percent(sendNetMessage);
					buttonLeft.setEnabled(false);
					gameClient.sendTransactionSuccesToGUI(sendNetMessage);
					epf.disableAfterClick();
				}
			};

			ActionListener payFlat = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					gameClient.payFee(amount, sendNetMessage);
					buttonRight.setEnabled(false);
					gameClient.sendTransactionSuccesToGUI(sendNetMessage);
					epf.disableAfterClick();
				}
			};
			
			epi.setText(description);
			epi.addButtonText("10%");
			epi.addButtonText(String.valueOf(amount));
			epi.addActionListener(tenPercent);
			epi.addActionListener(payFlat);
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
