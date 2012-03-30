package ch.bfh.monopoly.common;

public abstract class AbstractTileEvent implements TileEvent{

		protected String name;
		protected String cardText;
		protected GameClient gameClient;
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCardText() {
			return cardText;
		}

		public void setCardText(String cardText) {
			this.cardText = cardText;
		}

		public AbstractTileEvent(GameClient gameClient){
			this.gameClient = gameClient;
		}
		
		//should execute the code of whatever the event is supposed to do
		public abstract void run();
}
