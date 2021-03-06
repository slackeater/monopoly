package ch.bfh.monopoly.tile;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.event.BoardEvent;
import ch.bfh.monopoly.event.EventManager;
import ch.bfh.monopoly.gui.EventPanelFactory;

public class GoToJail extends AbstractTile implements EventPanelSource {

	BoardEvent be;
	ResourceBundle rb = ResourceBundle.getBundle(
			"ch.bfh.monopoly.resources.tile", gameClient.getLoc());;
	String description;
	EventPanelFactory epf;
	
	public GoToJail(String name, int coordX, int coordY, int tileId,
			EventManager em, GameClient gameClient) {
		super(name, coordX, coordY, tileId, em, gameClient);
		this.description = rb.getString("goToJail-cardText");
	}

	/**
	 * get the JPanel to show in the GUI for this tile's event
	 */
	
	
	public EventPanelInfo getEventPanelInfoForStep(Step step) {
		String labelText;
		String buttonText;
		ActionListener al;
		EventPanelInfo epi;

		switch (step) {
		case GET_EVENT:	
			epi = new EventPanelInfo(gameClient);
			buttonText="ok";
			al =new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					epf.disableAfterClick();
					gameClient.goToJail(sendNetMessage);
					gameClient.sendTransactionSuccesToGUI(sendNetMessage);
					
					System.out.println("The current player is at tile :"
							+ gameClient.getCurrentPlayer().getPosition());
					System.out.println("The current player jail status is :"
							+ gameClient.getCurrentPlayer().isInJail());
				}
			};
			epi.setText(description);
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
