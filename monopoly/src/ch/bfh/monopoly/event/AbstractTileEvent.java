package ch.bfh.monopoly.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.bfh.monopoly.common.GameClient;

public abstract class AbstractTileEvent implements BoardEvent{

		protected String name;
		protected String eventDescription;
		protected GameClient gameClient;
		protected boolean sendNetMessage=true;
		protected JPanel jpanel = new JPanel();
		protected JButton buttonRight = new JButton();
		protected JButton buttonLeft = new JButton();
		protected JLabel eventInfoLabel = new JLabel();
		
		public AbstractTileEvent(String name, String eventDescription, GameClient gameClient){
			this.name=name;
			this.eventDescription = eventDescription;
			this.gameClient = gameClient;
		}
		
		public String getName() {
			return name;
		}


		public String getEventDescription() {
			return eventDescription;
		}
		
		public JPanel getTileEventPanel() {
			
			buttonRight.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					
					performEvent();
					
					gameClient.sendTransactionSuccesToGUI(sendNetMessage);
				}
			});
			buttonRight.setText("ok");
			eventInfoLabel.setText(eventDescription);

			jpanel.add(eventInfoLabel);
			jpanel.add(buttonRight);

			return jpanel;
		}

		/**
		 * sets the sendNetMessage to a different boolean value
		 * used to test the events to prevent a net message from being sent
		 */
		public void setSendNetMessage(boolean newValue){
			sendNetMessage=newValue;
		}
}
