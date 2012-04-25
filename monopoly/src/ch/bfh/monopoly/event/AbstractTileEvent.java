package ch.bfh.monopoly.event;

import ch.bfh.monopoly.common.GameClient;

public abstract class AbstractTileEvent implements BoardEvent{

		protected String name;
		protected String eventDescription;
		protected GameClient gameClient;

		
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

}
