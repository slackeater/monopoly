package ch.bfh.monopoly.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import ch.bfh.monopoly.common.Board;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.Player;
import ch.bfh.monopoly.tile.Property;
import ch.bfh.monopoly.tile.Tile;

public class GameClientTransactionTests {

	Locale loc;
	GameClient gameClient;
	Board board;

	@Before
	public void setup() {
		TestInstanceGenerator tig = new TestInstanceGenerator();
		gameClient= tig.getGameClient();
		board=tig.getBoard();
	}
	
	
	
	/**
	 *  check that the method gameClient.buyCurrentPropertyForPlayer deducts the money from the players account correctly
	 */
	@Test
	public void buyPropertyFromBankWithdrawsCorrectly() {
		int tileId = 39;
		Player p = board.getPlayerByName("Justin");
		int previousBalance = p.getAccount();
		gameClient.setCurrentPlayer(p);
		gameClient.advanceCurrentPlayerNSpaces(39);
		gameClient.buyCurrentPropertyForPlayer(p.getName());
		Tile t = board.getTileById(tileId);
		Property prop =(Property)t;
		int priceOfProperty = prop.getPrice();
		int newBalance = p.getAccount();
		assertTrue(newBalance==(previousBalance-priceOfProperty));
	}
	
	
	
	/**
	 *  check that a player cannot buy a property from the bank, unless he has enough money
	 */
	@Test
	public void propertyFromBankInsufficientFunds() {
		int tileId = 39;
		Player p = board.getPlayerByName("Justin");
		p.withdawMoney(p.getAccount()-1);
		gameClient.setCurrentPlayer(p);
		gameClient.advanceCurrentPlayerNSpaces(39);
		try {
			gameClient.buyCurrentPropertyForPlayer(p.getName());
			fail("FAIL: Player bought a property from the bank, but had insufficient funds");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test 
	public void payFeeDeductsMoneyFromCurrentPlayer(){
		String playerName = "Justin";
		Player plyr = board.getPlayerByName(playerName);
		int playerAccountBefore = plyr.getAccount();
		gameClient.setCurrentPlayer(plyr);
		int fee = 100;
		gameClient.payFee(fee);
		assertTrue(plyr.getAccount()==playerAccountBefore-fee);
	}
	
	@Test 
	public void payFeeCreditsFeeToFreeParking(){
		String playerName = "Justin";
		Player plyr = board.getPlayerByName(playerName);
		gameClient.setCurrentPlayer(plyr);
		int fee = 100;
		int freeParkingAmount = gameClient.getFreeParking();
		gameClient.payFee(fee);
		assertTrue(gameClient.getFreeParking()==freeParkingAmount+fee);
		
	}
	
}
