package ch.bfh.monopoly.common;
import ch.bfh.monopoly.observer.PlayerSubject;
import ch.bfh.monopoly.observer.TileSubject;
import ch.bfh.monopoly.tile.TileInfo;

public class BoardController {

	Board board;
	
	public BoardController(Board board){
		this.board = board;
	}
	
	public TileInfo getTileInfoById(int id){
		return board.getTileInfoById(id);
	}
	
	public TileSubject getTileSubjectAtIndex(int i){
		return board.getTileSubjectAtIndex(i);
	}
	
	public PlayerSubject getSubjectForPlayer(){
		return board.getSubjectForPlayer();
	}
	
	public PlayerSubject getSubjectForMovement(){
		return board.getSubjectForMovement();
	}
	
	/**
	 * gets the number of players that are participating in this game
	 * @return int number of players in game
	 */
	public int getPlayerCount(){
		return board.getPlayers().size();
	}
	
	/**
	 * used to get the player information to the GUI .  method calls notifyListeners in board
	 */
	public void initGUI(){
		board.initGUI();
	}
}
