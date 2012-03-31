package ch.bfh.monopoly.common;

public class BoardController {

	Board board;
	
	public BoardController(Board board){
		this.board = board;
	}
	
	public int getXCoordForTileAtIndex(int index){
		return board.tiles[index].getCoordX();
	}
	
	public int getYCoordForTileAtIndex(int index){
		return board.tiles[index].getCoordY();
	}
	
}
