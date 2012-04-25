package ch.bfh.monopoly.common;

import java.util.Locale;

import ch.bfh.monopoly.network.Network;
import ch.bfh.monopoly.tile.IProperty;


public class GameClient {
	
	Player localPlayer;
	Player currentPlayer;
	private Player bank;
	Network communicate;
	Locale loc;
	Board board;
	
	public GameClient (Locale loc) {
		bank = new Player("bank", 100000000);
		//loc should be received from a netmessage sent when the server calls startGame();
		this.loc = loc;
		
		//TODO this list must be received from a netMessage when the game starts
		String[] playerNames = {"Justin","Giuseppe","Damien","Cyril","Elie"};
		board = new Board(this);
	}
	
	/**
	 * performs tasks necessary when the program first sets up the board and GUI
	 * TODO this is poorly named, and maybe misplaced?
	 */
	public void initBoard(String[] playerNames){
		//a net message should come with the list of player names
		//until then we have a mock object full of player names
		board.createPlayers(playerNames, loc);
	}

	public Locale getLoc(){
		return loc;
	}
	
	/**
	 * calculate the rent of a property, if a player lands on it
	 * @param tileID the tile number of the property
	 */
	public int getFeeForTileAtId(int tileId){
		IProperty p = (IProperty)board.getTileById(tileId);
		return p.feeToCharge();
	}

	public Board getBoard(){
		return this.board;
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public void setCurrentPlayer(Player p){
		currentPlayer = p;
	}
	
	/**
	 * advance the current player a given number n spaces forward
	 */
	public void advanceCurrentPlayerNSpaces(int n){
		int currentPos = currentPlayer.getPosition();
		currentPlayer.setPosition((currentPos + n)%40);
	}
	
	
	/**
	 * buy a house for a given property
	 * @param tileID the tile number of the property to build a house on
	 */
	public void buyHouse(int tileID){
		board.buyHouse(tileID);
	}
	
	/**
	 * transfers a given property from one player to another
	 * @param fromName name of the player to take property from
	 * @param toName name of the player to give the property to
	 * @param price the price the property has been sold for
	 * @param propertyID the tile number of the property that was sold
	 */
	public void transferPropertyForPrice(String fromName, String toName, int propertyID, int price){
		board.transferProperty(fromName, toName, propertyID);
		board.transferMoney(fromName, toName, price);
	}

	
	/**
	 * get the description of the event for the tile on which the current player resides
	 */
	public String getEventDescription(){
		int currentPos = currentPlayer.getPosition();
		String eventDescription =board.getTileById(currentPos).getEventDescription();
		return eventDescription;
	}
	
	/**
	 * perform the event for the tile that the current player occupies
	 */
	public void performEvent(){
		int currentPos = currentPlayer.getPosition();
		board.getTileById(currentPos).performEvent();
	}
	
	/**
	 * checks if a given player is the owner of a given tile
	 * @param playerName  the player's name to check
	 * @param tileId  tile to check ownership of
	 */
	public boolean playerIsOwnerOfTile(String playerName, int tileId){
		return board.playerIsOwnerOfTile(playerName, tileId);
	}
	
	/**
	 * checks if the current player has sufficient funds to pay a fee
	 * @param fee  the amount of the fee to be paid
	 */
	public boolean hasSufficientFunds(int fee){
		return (currentPlayer.getAccount()>fee);
	}
	
	/**
	 * checks if the current player has sufficient funds to pay a fee
	 * @param playerName the player to check the account of
	 * @param fee  the amount of the fee to be paid
	 */
	public boolean playerHasSufficientFunds(String playerName, int fee){
		return board.getPlayerByName(playerName).getAccount() > fee;
	}
	
	/**
	 * get the bank player
	 * @return bank player
	 */
	public Player getBankPlayer(){
		return bank;
	}
	
}
