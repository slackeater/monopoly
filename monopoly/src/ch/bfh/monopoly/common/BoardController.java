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
	
	public PlayerSubject getSubjectForPlayer(String playerName){
		return board.getSubjectForPlayer(playerName);
	}
}
