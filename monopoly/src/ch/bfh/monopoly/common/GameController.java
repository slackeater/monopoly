package ch.bfh.monopoly.common;

public class GameController {

	GameClient gc;
	
	public GameController(GameClient gc){
		this.gc = gc;
	}
	public void buyHouse(int tileID){
		gc.buyHouse(tileID);
	}
}
