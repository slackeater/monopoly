package common;

public abstract class AbstractTileEvent implements TileEvent{

		String name;
		Board sm;
		public AbstractTileEvent(Board sm){
			this.sm= sm;
		}
		//should execute the code of whatever the event is supposed to do
		public abstract void run();
}
