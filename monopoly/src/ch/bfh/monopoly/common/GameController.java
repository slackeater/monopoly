package ch.bfh.monopoly.common;

public class GameController {

	GameClient gameClient;
	
	public GameController(GameClient gc){
		this.gameClient = gc;
	}
	public void buyHouse(int tileID){
		gameClient.buyHouse(tileID);
	}
	
	public void advancePlayerNSpaces(int n){
		gameClient.advanceCurrentPlayerNSpaces(n);
	}
}
