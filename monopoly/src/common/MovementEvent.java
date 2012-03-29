package common;

public class MovementEvent extends AbstractTileEvent{
	
	int newPosition;
	
	public MovementEvent(String name, int newPosition, Board sm){
		super(sm);
		this.newPosition=newPosition;
		this.name=name;
	}
	
	public void run(){
		//get currentPlayer
		//currentPlayer.setPosition(newPosition)
	}
}
