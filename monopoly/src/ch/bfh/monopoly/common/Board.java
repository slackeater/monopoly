package ch.bfh.monopoly.common;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import ch.bfh.monopoly.observer.PlayerListener;
import ch.bfh.monopoly.observer.PlayerStateEvent;
import ch.bfh.monopoly.observer.PlayerSubject;
import ch.bfh.monopoly.observer.TileSubject;
import ch.bfh.monopoly.observer.TileListener;
import ch.bfh.monopoly.observer.TileStateEvent;
import ch.bfh.monopoly.tile.*;

public class Board {
	private List<Player> players;
	private Player localPlayer;
	private Tile[] tiles;
	private int availableHouses = 32;
	private int availableHotels = 12;
	private TileSubject[] tileSubjects;
	private Token[] tokens = new Token[8];
	private int freeParking;
	private PlayerSubject playerSubject;

	/**
	 * this inner class is connected to the GUI through an observer pattern. The
	 * GUI registers its listeners with this inner class and when instance
	 * variable for this player change, this class calls the appropriate method
	 * in the GUI object
	 */
	private class ConcretePlayerSubject implements PlayerSubject {

		public ConcretePlayerSubject() {
		}

		ArrayList<PlayerListener> listeners = new ArrayList<PlayerListener>();

		@Override
		public void addListener(PlayerListener tl) {
			listeners.add(tl);
		}

		@Override
		public void removeListener(PlayerListener tl) {
			listeners.remove(tl);
		}

		@Override
		public void notifyListeners() {
			ArrayList<PlayerStateEvent> playerStates = new ArrayList<PlayerStateEvent>();
			for (Player plyr : players) {
				//generate a list of booleans that represent the terrains that the player owns
				boolean[] terrains = new boolean[40];
				for(Tile t:plyr.getProperties()) {
					terrains[t.getId()]=true;
				}
				PlayerStateEvent pse = new PlayerStateEvent(plyr.getPosition(), plyr.getName(),
						plyr.isInJail(), plyr.getAccount(), plyr.isTurnToken(), plyr.getJailCard(), terrains, plyr.getToken());
				playerStates.add(pse);
			}
			for (PlayerListener pl : listeners) {
				pl.updatePlayer(playerStates);
			}
		}
	}
	
	/**
	 * Method used by PlayerListeners in the GUI in an observer pattern if
	 * something changes in the Player data, it is through the PlayerSubject
	 * that the GUI is notified of the changes
	 * 
	 * @return the playerSubject for this player
	 */
	public PlayerSubject getSubjectForPlayer() {
		return playerSubject;
	}
	

	/**
	 * inner class responsible for registering listeners from the GUI and
	 * notifying these listeners when changes to the instance variables of the
	 * outer class are changed
	 */
	private class ConcreteSubject implements TileSubject {
		private int tileListenerId;

		public ConcreteSubject(int tileId) {
			this.tileListenerId = tileId;
		}

		ArrayList<TileListener> listeners = new ArrayList<TileListener>();

		public void addListener(TileListener tl) {
			listeners.add(tl);
		}

		public void removeListener(TileListener tl) {
			listeners.remove(tl);
		}

		public void notifyListeners() {
			Tile t = getTileById(tileListenerId);
			Terrain terrain = ((Terrain) t);
			TileStateEvent tsi = new TileStateEvent(terrain.getHouseCount(),
					terrain.getHotelCount(), "bob",// terrain.getOwner().getName(),
					terrain.isMortgageActive());
			for (TileListener tl : listeners) {
				tl.updateTile(tsi);
			}
		}
	}

	public Board(GameClient gameClient) {
		// create tiles, cards, and events and tokens
		TileCreator tc = new TileCreator(gameClient);
		tiles = tc.getTilesArray();
		availableHouses = 32;
		availableHotels = 12;
		tileSubjects = new TileSubject[40];
		playerSubject = new ConcretePlayerSubject();
		createTileSubjects();
		
		//token initialization
		tokens[0] = new Token(Color.RED,0.1,0.375);
		tokens[1] = new Token(Color.GREEN, 0.3, 0.375);
		tokens[2] = new Token(Color.BLUE, 0.5, 0.375);
		tokens[3] = new Token(Color.YELLOW, 0.7, 0.375);
		tokens[4] = new Token(Color.BLACK, 0.1, 0.700);
		tokens[5] = new Token(Color.CYAN, 0.3, 0.700);
		tokens[6] = new Token(Color.GRAY, 0.5, 0.700);
		tokens[7] = new Token(Color.ORANGE, 0.7, 0.700);
	}


	/**
	 * deposit money to player's account
	 * @param the player to deposit money to
	 * @param amount the amount to deposit
	 */
	public void depositToPlayer(String player, int amount){
		getPlayerByName(player).depositMoney(amount);
		playerSubject.notifyListeners();
	}
	
	/**
	 * withdraw money from a player's account
	 * @param the player to withdraw money from
	 * @param amount the amount to withdraw
	 */
	public void withdrawPlayer(String player, int amount){
		getPlayerByName(player).withdawMoney(amount);
		playerSubject.notifyListeners();
	}
	
	
	/**
	 * returns a Subject / Concreted Subject which corresponds to a tile at the
	 * given index
	 */
	public TileSubject getTileSubjectAtIndex(int index) {
		return tileSubjects[index];
	}

	/**
	 * creates 40 ConreceteSubject instances, 1 for every tile on the board each
	 * is associated with a given tile through the index number and creates a
	 * Subject for each player participating in the game
	 */
	public void createTileSubjects() {
		// create list of "Tile" Subjects
		for (int i = 0; i < 40; i++) {
			tileSubjects[i] = new ConcreteSubject(i);
		}
	}

	/**
	 * buy a house for a given property checks that the tileId provIded refers
	 * to a terrain
	 * 
	 * @param tileId
	 *            the tile number of the property to build a house on
	 */
	public void buyHouse(int tileId) {
		if (availableHouses < 1)
			throw new RuntimeException(
					"No houses available to complete the transaction");
		Tile t = tiles[tileId];
		if (!(t instanceof Terrain))
			throw new RuntimeException(
					"Tile must be a terrain in order to build houses on it");
		Terrain terrain = (Terrain) t;
		terrain.buildHouse();
		int id = terrain.getId();
		availableHouses--;
		tileSubjects[id].notifyListeners();
	}

	/**
	 * transfers a given property from one player to another checks if property
	 * to transfer is a Property
	 * 
	 * @param fromName
	 *            name of the player to take property from
	 * @param toName
	 *            name of the player to give the property to
	 * @param propertyId
	 *            the tile number of the property that was sold
	 */
	public void transferProperty(String fromName, String toName, int tileId) {
		Tile t = tiles[tileId];
		if (t instanceof Property) {
			Player fromPlayer = getPlayerByName(fromName);
			Player toPlayer = getPlayerByName(toName);
			((Property) t).setOwner(toPlayer);
			fromPlayer.removeProperty(t);
			toPlayer.addProperty(t);
			tileSubjects[tileId].notifyListeners();
			playerSubject.notifyListeners();
		} else
			throw new RuntimeException(
					"cannot complete transfer: object to transfer is not a property");
	}

	public Tile getTileById(int tileId) {
		return tiles[tileId];
	}

	/**
	 * adds a given property to a player's list of properties tileId must refer
	 * to a property the property must have no OWNER, meaning this property
	 * comes from the bank TODO the precondition that the property be owned by
	 * the bank make this method a bit limited, reevaluate the condition
	 * 
	 * @param fromName
	 *            name of the player to take property from
	 * @param toName
	 *            name of the player to give the property to
	 * @param price
	 *            the price the property has been sold for
	 * @param propertyId
	 *            the tile number of the property that was sold
	 */
	public void addPropertyToPlayer(String toName, int tileId) {
		Tile t = tiles[tileId];
		Property p = castTileToProperty(t);
		if (p.getOwner() != null) {
			throw new RuntimeException(
					"Property is already owned: cannot add properyt to player -> use transferProperty");
		} else {
			Player toPlayer = getPlayerByName(toName);
			toPlayer.addProperty(p);
			p.setOwner(toPlayer);
			playerSubject.notifyListeners();
			tileSubjects[tileId].notifyListeners();
		}

	}

	/**
	 * transfers a given amount of money from one player to another
	 * 
	 * @param fromName
	 *            name of the player to take money from
	 * @param toName
	 *            name of the player to give the money to
	 * @param price
	 *            the amount of money to transfer
	 */
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
		playerSubject.notifyListeners();
	}

	/**
	 * create the player objects from the list of strings that comes from the
	 * server when the game begins
	 * 
	 * @param playerNames
	 *            this list of player names of hte player participating in this
	 *            game
	 * @param the
	 *            locale that was chosen: needed to give the players the correct
	 *            starting balance in there account
	 * 
	 */
	public void createPlayers(List<String> playerNames, Locale loc	) {
		
		players = new ArrayList<Player>();
		String bundleData = ResourceBundle.getBundle("tile", loc).getString(
				"startMoney");
		bundleData = bundleData.trim();
		int startMoney = Integer.parseInt(bundleData);
		for (int i = 0; i < playerNames.size(); i++) {
			Player plyr = new Player(playerNames.get(i), startMoney, tokens[i]);
			players.add(plyr);

		}
	}

	/**
	 * get the player object whose name field corresponds to a given name
	 * 
	 * @param name
	 *            the string of the name of the player
	 */
	public Player getPlayerByName(String name) {
		Player p = null;
		for (int i = 0; i < players.size(); i++) {
			String playerName = players.get(i).getName();
			if (playerName.equals(name))
				p = players.get(i);
		}
		if (p == null)
			throw new RuntimeException("Player not found with the given name");
		return p;
	}

	public boolean playerIsOwnerOfTile(String playerName, int tileId) {
		Property p = castTileToProperty(tiles[tileId]);
		String ownerName = p.getOwner().getName();
		return (playerName.equals(ownerName));
	}

	/**
	 * casts a tile to a property if it is a property, otherwise throw an
	 * exception
	 * 
	 * @param t
	 *            the tile to cast
	 * @return the tile casted to a property
	 */
	public Property castTileToProperty(Tile t) {
		if (!(t instanceof Property))
			throw new RuntimeException(
					"cannot complete transfer: object to transfer is not a property");
		return ((Property) t);
	}

	/**
	 * buy a property that is currently owned by the bank at the price that is
	 * written on the card
	 * 
	 * @param tileId
	 *            the id of the property to be bought
	 */
	public void buyPropertyFromBank(String currentPlayer, int tileId) {
		Tile t = tiles[tileId];
		Property p = castTileToProperty(t);
		if (!(p.getOwner().getName().equals("bank")))
			throw new RuntimeException(
					"The property to be bought is not owned by the bank, use transfer property instead");
		int priceOfProperty = p.getPrice();
		if (!playerHasSufficientFunds(currentPlayer, priceOfProperty))
			throw new RuntimeException(
					"Player Does not have enough money to by the property from the bank");
		Player player = getPlayerByName(currentPlayer);
		player.addProperty(p);
		p.setOwner(player);
		player.withdawMoney(p.getPrice());
		playerSubject.notifyListeners();
	}

	/**
	 * checks if the current player has sufficient funds to pay a fee
	 * 
	 * @param playerName
	 *            the player to check the account of
	 * @param fee
	 *            the amount of the fee to be paid
	 */
	public boolean playerHasSufficientFunds(String playerName, int fee) {
		return (getPlayerByName(playerName).getAccount() > fee);
	}

	/**
	 * creates an object with all the static tile information to be sent to the
	 * GUI
	 * 
	 * @param Id
	 *            is the Id of the tile for which we want to get information
	 */
	public TileInfo getTileInfoById(int Id) {
		TileInfo tileInfo = new TileInfo();
		Tile tile = tiles[Id];
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
			tileInfo.setId(t.getId());
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

	public int getFreeParking() {
		return freeParking;
	}

	public Player getLocalPlayer() {
		return localPlayer;
	}

	public void setFreeParking(int amount) {
		this.freeParking = amount;
	}

	public int getAvailableHouses() {
		return availableHouses;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public int getAvailableHotels() {
		return availableHotels;
	}
	
	public void initGUI(){
		playerSubject.notifyListeners();
	}

}
