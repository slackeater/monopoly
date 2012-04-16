package ch.bfh.monopoly.common;
import ch.bfh.monopoly.tile.TileInfo;

public class BoardController {

	Board board;
	
	public BoardController(Board board){
		this.board = board;
	}
	public void buyHouse(int tileID){
		board.buyHouse(tileID);
	}
	
	public int getXCoordForTileAtIndex(int index){
		return board.tiles[index].getCoordX();
	}
	
	public int getYCoordForTileAtIndex(int index){
		return board.tiles[index].getCoordY();
	}
	
	public TileInfo getTileInfoByID(int id){
		return board.getTileInfoByID(id);
	}
	
	public Subject[] getTileSubjectList(){
		return board.getTileSubjectList();
	}
	
	public Subject getTileSubjectAtIndex(int i){
		return board.getTileSubjectAtIndex(i);
	}
}
