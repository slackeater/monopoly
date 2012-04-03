package ch.bfh.monopoly.common;

import java.awt.Color;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.management.RuntimeErrorException;
import javax.swing.JMenu;

import ch.bfh.monopoly.tile.*;

public class Board {
	Player[] players;
	public Tile[] tiles;
	private Locale loc;
	private int availableHouses;
	private int availableHotels;
	private Player me;
	private Player currentPlayer;
	private Subject[] tileSubjects;

	private class ConcreteSubject implements Subject {
		private int tileListenerID;

		public ConcreteSubject(int tileID) {
			this.tileListenerID = tileID;
		}

		ArrayList<TileListener> listeners = new ArrayList<TileListener>();

		public void addListener(TileListener tl) {
			listeners.add(tl);
		}

		public void removeListener(TileListener tl) {
			listeners.remove(tl);
		}

		public void notifyListeners() {
			Tile t = getTileByID(tileListenerID);
			Terrain terrain = ((Terrain) t);
			TileStateInfo tsi = new TileStateInfo(
					terrain.getHouseCount(),
					terrain.getHotelCount(), 
					terrain.getOwner().getName(),
					terrain.isMortgageActive());
			for (TileListener tl : listeners) {
				tl.updateTile(tsi);
			}
		}
	}

	public Board(Locale loc, GameClient gameClient) {
		// create tiles, cards, and events
		this.loc = loc;

		TileCreator tc = new TileCreator(loc, gameClient);
		tiles = tc.getTilesArray();

		availableHouses = 32;
		availableHotels = 12;

		createTileSubjects();

	}

	public void createTileSubjects() {
		// create list of "Tile" Subjects
		for (int i = 0; i < 40; i++) {
			tileSubjects[i] = new ConcreteSubject(i);
		}
	}

	public Subject[] getTileSubjectList() {
		return tileSubjects;
	}

	public void transferProperty(String fromName, String toName, int tileID) {
		Tile t = tiles[tileID];
		if (t instanceof Property) {
			Player fromPlayer = getPlayerByName(fromName);
			Player toPlayer = getPlayerByName(toName);
			((Property) t).setOwner(toPlayer);
			fromPlayer.removeProperty(t);
			toPlayer.addProperty(t);
		} else
			throw new RuntimeException(
					"cannot complete transfer: object to transfer is not a property");
	}

	public Tile getTileByID(int tileID) {
		return tiles[tileID];
	}

	public void addPropertyToPlayer(String toName, int tileID) {
		Tile t = tiles[tileID];
		if (t instanceof Property) {
			Property p = ((Property) t);
			if (p.getOwner() != null) {
				throw new RuntimeException(
						"Property is already owned: cannot add properyt to player -> use transferProperty");
			} else {
				Player toPlayer = getPlayerByName(toName);
				toPlayer.addProperty(p);
				p.setOwner(toPlayer);
			}
		} else
			throw new RuntimeException(
					"cannot complete transfer: object to transfer is not a property");

	}

	public void transferMoney(String fromName, String toName, int price) {
		Player fromPlayer = getPlayerByName(fromName);
		if (price > fromPlayer.getAccount())
			throw new RuntimeException("Cannot complete transfer: \n\t"
					+ fromPlayer.getName()
					+ " has insufficient funds. \n\tReqeusted to transfer: "
					+ price + " account balance: " + fromPlayer.getAccount());
		Player toPlayer = getPlayerByName(toName);
		fromPlayer.withdawMoney(price);
		toPlayer.depositMoney(price);
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
			tileInfo.setRGB(t.getRGB());
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
}
