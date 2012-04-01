package ch.bfh.monopoly.common;

import java.awt.Color;
import java.net.Socket;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.management.RuntimeErrorException;
import javax.swing.JMenu;

import ch.bfh.monopoly.tile.*;

public class Board {
	Player[] players;
	private Tile[] tiles;
	private Locale loc;
	private int availableHouses;
	private int availableHotels;
	private Player me;
	private Player currentPlayer;

	public Board(Locale loc, GameClient gameClient) {
		// create tiles, cards, and events
		this.loc = loc;

		TileCreator tc = new TileCreator(loc, gameClient);
		tiles = tc.getTilesArray();

		availableHouses = 32;
		availableHotels = 12;
	}

	public void createPlayers(String[] playerNames) {
		players = new Player[playerNames.length];
		String bundleData = ResourceBundle.getBundle("tile", loc).getString(
				"startMoney");
		bundleData = bundleData.trim();
		int startMoney = Integer.parseInt(bundleData);
		for (int i = 0; i < playerNames.length; i++) {
			players[i] = new Player(playerNames[i], startMoney);
		}
	}

	public Player getPlayerByName(String name) {
		Player p = null;
		for (int i = 0; i < players.length; i++) {
			String playerName = players[i].getName();
			if (playerName.equals(name))
				p = players[i];
		}
		if (p == null)
			throw new RuntimeException("Player not found with the given name");
		return p;
	}

	/**
	 * METHODS FOR MODIFYING PLAYER DATA
	 * */

	/**
	 * depositMoneyToPlayer
	 * 
	 * @param String
	 *            p the name of the player whose account balance should be
	 *            modified
	 * @param inv
	 *            value the amount of money to add to the account
	 * */
	public void depositMoneyToPlayer(String playerName, int value) {
		Player p = getPlayerByName(playerName);
		p.depositMoney(value);
	}

	public void withdrawMoneyFromPlayer(String playerName, int value) {
		Player p = getPlayerByName(playerName);
		p.withdawMoney(value);
	}

	public void transferMoney(String fromName, String toName, int price) {
		Player fromPlayer = getPlayerByName(fromName);
		Player toPlayer = getPlayerByName(toName);
		fromPlayer.withdawMoney(price);
		toPlayer.depositMoney(price);
	}

	public void transferProperty(String fromName, String toName, int propertyID) {
		Player fromPlayer = getPlayerByName(fromName);
		Player toPlayer = getPlayerByName(toName);
		Tile t = tiles[propertyID];
		if (t instanceof Property) {
			if (fromPlayer.ownsProperty(t))
				fromPlayer.removeProperty(t);
			else
				throw new RuntimeException(
						"Transfer failed: seller does not own property");
			toPlayer.addProperty(t);
			((Property) t).setOwner(toPlayer);
		}
	}

	public void addPropertyToPlayer(String playerName, int tileID) {
		Tile t = getTileByID(tileID);
		Player p = getPlayerByName(playerName);
		if (t instanceof Property) {
			if (((Property) t).getOwner() == null) {
				p.addProperty(t);
				((Property) t).setOwner(p);
			} else {
				throw new RuntimeException(
						"Cannot add the property because another player owns it, you must call transfer");
			}
		} else
			throw new RuntimeException(
					"The tile to add is not a property, and can't be added to player's list");
	}
	

	public TileInfo getTileInfoByID(int id) {
		TileInfo tileInfo = new TileInfo();
		Tile tile = tiles[id];
		if (tile instanceof Terrain) {
			Terrain t = (Terrain) tile;
			tileInfo.setName(t.getName());
			tileInfo.setPrice(t.getPrice());
			tileInfo.setHouseCost(t.getHouseCost());
			tileInfo.setHotelCost(t.getHotelCost());
			tileInfo.setRent(t.getRentByHouseCount(0));
			tileInfo.setRent1house(t.getRentByHouseCount(1));
			tileInfo.setRent2house(t.getRentByHouseCount(2));
			tileInfo.setRent3house(t.getRentByHouseCount(3));
			tileInfo.setRent4house(t.getRentByHouseCount(4));
			tileInfo.setRenthotel(t.getRentHotel());
			tileInfo.setGroup(t.getGroup());
			tileInfo.setMortgageValue(t.getMortgageValue());
			tileInfo.setCoordX(t.getCoordX());
			tileInfo.setCoordY(t.getCoordY());
		}
		if (tile instanceof Railroad) {
			Railroad t = (Railroad) tile;
			tileInfo.setName(t.getName());
			tileInfo.setPrice(t.getPrice());

			// returning RENT info for Railroads rrequires changing how
			// the rent is calculated in Railroad
			tileInfo.setGroup(t.getGroup());
			tileInfo.setMortgageValue(t.getMortgageValue());
			tileInfo.setCoordX(t.getCoordX());
			tileInfo.setCoordY(t.getCoordY());
		}
		if (tile instanceof Utility) {
			Utility t = (Utility) tile;
			tileInfo.setName(t.getName());
			tileInfo.setPrice(t.getPrice());

			tileInfo.setGroup(t.getGroup());
			tileInfo.setMortgageValue(t.getMortgageValue());
			tileInfo.setCoordX(t.getCoordX());
			tileInfo.setCoordY(t.getCoordY());
		}
		if (tile instanceof CommunityChest) {
			CommunityChest t = (CommunityChest) tile;
			tileInfo.setName(t.getName());

			tileInfo.setCoordX(t.getCoordX());
			tileInfo.setCoordY(t.getCoordY());
		}
		if (tile instanceof Chance) {
			Chance t = (Chance) tile;
			tileInfo.setName(t.getName());

			tileInfo.setCoordX(t.getCoordX());
			tileInfo.setCoordY(t.getCoordY());
		}
		if (tile instanceof NonProperty) {
			NonProperty t = (NonProperty) tile;
			tileInfo.setName(t.getName());

			tileInfo.setCoordX(t.getCoordX());
			tileInfo.setCoordY(t.getCoordY());
		}
		return tileInfo;
	}

	public Tile getTileByID(int tileID) {
		return tiles[tileID];
	}
}
