package ch.bfh.monopoly.event;

import ch.bfh.monopoly.common.GameClient;

public abstract class AbstractTileEvent implements BoardEvent{

		protected String name;
		protected String eventDescription;
		protected GameClient gameClient;
		protected boolean sendNetMessage=true;
		
		public AbstractTileEvent(String name, String eventDescription, GameClient gameClient){
			this.gameClient = gameClient;
			this.eventDescription = eventDescription;
		}
		
		public String getName() {
			return name;
		}


		public String getEventDescription() {
			return eventDescription;
		}

		/**
		 * sets the sendNetMessage to a different boolean value
		 * used to test the events to prevent a net message from being sent
		 */
		public void setSendNetMessage(boolean newValue){
			sendNetMessage=newValue;
		}
}
