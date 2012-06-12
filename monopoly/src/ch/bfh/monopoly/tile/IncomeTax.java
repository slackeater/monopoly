package ch.bfh.monopoly.tile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.event.BoardEvent;
import ch.bfh.monopoly.event.EventManager;
import ch.bfh.monopoly.gui.EventPanelFactory;

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
	}

	
	
	
	public EventPanelInfo getEventPanelInfoForStep(Step step) {
		String labelText;
		String buttonText;
		ActionListener al;
		EventPanelInfo epi;

		switch (step) {
		case GET_EVENT:	
			epi = new EventPanelInfo(gameClient);
			final int amount = Integer.parseInt(rb.getString("tile4-rent"));
			ActionListener tenPercent = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gameClient.payIncome10Percent(sendNetMessage);
					epf.changePanel(Step.PAID_INCOME_TAX);
				}
			};

			ActionListener payFlat = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					gameClient.payFee(amount, sendNetMessage);
					epf.changePanel(Step.PAID_INCOME_TAX);
				}
			};
			
			epi.setText(description);
			int tenPercentAmount =gameClient.getCurrentPlayer().getAccount()/10;

			epi.addButton("10%", tenPercentAmount, tenPercent);
			epi.addButton(String.valueOf(amount), fee, payFlat);
			break;
		case PAID_INCOME_TAX:
			epi = new EventPanelInfo(gameClient);
			buttonText = "ok";
			al = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					if (gameClient.isDoublesRoll()) {
						epf.setEventPanelSource(gameClient.getDice());
						epf.changePanel(Step.DOUBLES_TRANSITION);
					} else {
						epf.disableAfterClick();
						gameClient.sendTransactionSuccesToGUI(sendNetMessage);
					}
				}
			};
			epi.setText(rb.getString("thanks"));
			epi.addButton(buttonText, 0, al);
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
