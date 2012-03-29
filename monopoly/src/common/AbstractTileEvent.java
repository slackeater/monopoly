package common;

public abstract class AbstractTileEvent implements TileEvent{

		String name;
		StateManager sm;
		public AbstractTileEvent(StateManager sm){
			this.sm= sm;
		}
		//should execute the code of whatever the event is supposed to do
		public abstract void run();
}
