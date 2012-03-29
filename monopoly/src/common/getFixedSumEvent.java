package common;

public class getFixedSumEvent extends AbstractTileEvent{
	
	int fixedSum;
	
	public getFixedSumEvent(String name, int fixedSum, Board board){
		super(board);
		this.fixedSum=fixedSum;
		this.name=name;
	}
	
	public void run(){
		//get currentPlayer
		//currentPlayer.setAccountBalance(fixedSum);
	}
}