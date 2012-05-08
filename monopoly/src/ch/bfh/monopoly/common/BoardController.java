package ch.bfh.monopoly.common;
import ch.bfh.monopoly.observer.PlayerSubject;
import ch.bfh.monopoly.observer.TileSubject;
import ch.bfh.monopoly.tile.TileInfo;

public class BoardController {

	Board board;
	
	public BoardController(Board board){
		this.board = board;
	}
	
//TODO SHOULD WE DELETE THESE METHODS?
//	public int getXCoordForTileAtIndex(int index){
//		return board.getTileById(index).getCoordX();
//	}
//	
//	public int getYCoordForTileAtIndex(int index){
//		return board.getTileById(index).getCoordY();
//	}
	
	public TileInfo getTileInfoById(int id){
		return board.getTileInfoById(id);
	}
	
	public TileSubject getTileSubjectAtIndex(int i){
		return board.getTileSubjectAtIndex(i);
	}
	
	public PlayerSubject getSubjectForPlayer(){
		return board.getSubjectForPlayer();
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
