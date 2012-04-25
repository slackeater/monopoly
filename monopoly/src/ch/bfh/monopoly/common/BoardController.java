package ch.bfh.monopoly.common;
import ch.bfh.monopoly.tile.TileInfo;

public class BoardController {

	Board board;
	
	public BoardController(Board board){
		this.board = board;
	}
	
	public int getXCoordForTileAtIndex(int index){
		return board.getTileByID(index).getCoordX();
	}
	
	public int getYCoordForTileAtIndex(int index){
		return board.getTileByID(index).getCoordY();
	}
	
	public TileInfo getTileInfoByID(int id){
		return board.getTileInfoByID(id);
	}
	
	public Subject getTileSubjectAtIndex(int i){
		return board.getTileSubjectAtIndex(i);
	}
}
