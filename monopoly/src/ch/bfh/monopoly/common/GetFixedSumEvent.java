package ch.bfh.monopoly.common;

public class GetFixedSumEvent extends AbstractTileEvent{
	
	int fixedSum;
	
	public GetFixedSumEvent(String name, int fixedSum, Board board){
		super(board);
		this.fixedSum=fixedSum;
		this.name=name;
	}
	
	public void run(){
		//get currentPlayer
		//currentPlayer.setAccountBalance(fixedSum);
	}
}