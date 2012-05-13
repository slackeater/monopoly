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
				// generate a list of booleans that represent the terrains that
				// the player owns
				boolean[] terrains = new boolean[40];
				for (Tile t : plyr.getProperties()) {
					terrains[t.getId()] = true;
				}
				PlayerStateEvent pse = new PlayerStateEvent(plyr.getPosition(),
						plyr.getName(), plyr.isInJail(), plyr.getAccount(),
						plyr.isTurnToken(), plyr.getJailCard(), terrains,
						plyr.getToken());
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

		/**
		 * notify listeners in the GUI of any changes to a TERRAIN TILE ONLY
		 * other types of tiles are ignore because they don't have a change of
		 * appearance on the game board
		 */
		public void notifyListeners() {
			Tile t = getTileById(tileListenerId);
			if (t instanceof Terrain) {
				Terrain terrain = ((Terrain) t);
				TileStateEvent tsi = new TileStateEvent(
						terrain.getHouseCount(), terrain.getHotelCount(),
						"bob",// terrain.getOwner().getName(),
						terrain.isMortgageActive());
				for (TileListener tl : listeners) {
					tl.updateTile(tsi);
				}
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

		// token initialization
		tokens[0] = new Token(Color.RED, 0.1, 0.375);
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
	 * 
	 * @param the
	 *            player to deposit money to
	 * @param amount
	 *            the amount to deposit
	 */
	public void deposittoName(String player, int amount) {
		getPlayerByName(player).depositMoney(amount);
		playerSubject.notifyListeners();
	}

	/**
	 * withdraw money from a player's account
	 * 
	 * @param the
	 *            player to withdraw money from
	 * @param amount
	 *            the amount to withdraw
	 */
	public void withdrawPlayer(String player, int amount) {
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
	 * buy a house for a given property checks that the tileId provided refers
	 * to a terrain
	 * 
	 * @param tileId
	 *            the tile number of the property to build a house on
	 */
	public void buyHouse(int tileId) {
		Tile t = tiles[tileId];
		Terrain terrain = castTileToTerrain(t);
		if (availableHouses < 1)
			throw new RuntimeException(
					"No houses available to complete the transaction");
		if (terrain.getHouseCount() >= 4)
			throw new RuntimeException(
					"The maximum number of houses that may be built on a property is 4");
		terrain.buildHouse();
		int id = terrain.getId();
		availableHouses--;
		int price = terrain.getHouseCost();
		terrain.getOwner().withdawMoney(price);
		tileSubjects[id].notifyListeners();
	}

	/**
	 * buy a hotel for a given property checks that the tileId provided refers
	 * to a terrain
	 * 
	 * @param tileId
	 *            the tile number of the property to build a house on
	 */
	public void buyHotel(int tileId) {
		Tile t = tiles[tileId];
		Terrain terrain = castTileToTerrain(t);
		if (availableHotels < 1)
			throw new RuntimeException(
					"No hotels available to complete the transaction");
		if (terrain.getHouseCount() != 4)
			throw new RuntimeException(
					"There must be 4 houses present on this property in order to build a hotel");
		if (terrain.getHotelCount() >= 1)
			throw new RuntimeException(
					"It's monopoly, but hey there are still rules!  You can't build more than one hotel on a tile.");
		terrain.buildHotel();
		int id = terrain.getId();
		availableHotels--;
		int price = terrain.getHotelCost();
		terrain.getOwner().withdawMoney(price);
		tileSubjects[id].notifyListeners();
	}

	/**
	 * sell a house for a given property
	 * 
	 * @param tileID
	 *            the tile number of the property to sell a house from
	 */
	public void sellHouses(int tileId) {
		Tile t = tiles[tileId];
		Terrain terrain = castTileToTerrain(t);
		if (terrain.getHouseCount() <= 0)
			throw new RuntimeException(
					"No houses present on tile with tile Id=" + tileId);
		terrain.buildHotel();
		int id = terrain.getId();
		availableHotels++;
		int price = terrain.getHouseCost();
		terrain.getOwner().depositMoney(price);
		tileSubjects[id].notifyListeners();
	}

	/**
	 * sell a hotel for a given property
	 * 
	 * @param tileID
	 *            the tile number of the property to sell a hotel from
	 */
	public void sellHotel(int tileId) {
		Tile t = tiles[tileId];
		Terrain terrain = castTileToTerrain(t);
		if (terrain.getHotelCount() <= 0)
			throw new RuntimeException(
					"No hotels present on tile with tile Id=" + tileId);
		terrain.buildHotel();
		int id = terrain.getId();
		availableHotels++;
		int price = terrain.getHotelCost();
		terrain.getOwner().depositMoney(price);
		tileSubjects[id].notifyListeners();
	}

	/**
	 * Toggles the mortgage status of a given property
	 * 
	 * @param tileId
	 *            the id that corresponds to a tile for which we want to toggle
	 *            the mortgage status.
	 */
	public void toggleMortgageStatus(int tileId) {
		Tile t = tiles[tileId];
		Property prop = castTileToProperty(t);
		if (prop.isMortgageActive())
			prop.setMortgageActive(false);
		else
			prop.setMortgageActive(true);
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
	public void transferProperty(String fromName, String toName, int tileId,
			int price) {
		Tile t = tiles[tileId];
		Property prop = castTileToProperty(t);
		if (!prop.getOwner().getName().equals(fromName))
			throw new RuntimeException(
					"Cannot transfer a property from a player who doesn't own the property");
		// toName and fromName reversed, because money goes in opposite
		// direction than does the property.
		transferMoney(toName, fromName, price);
		Player fromPlayer = getPlayerByName(fromName);
		Player toPlayer = getPlayerByName(toName);
		prop.setOwner(toPlayer);
		fromPlayer.removeProperty(t);
		toPlayer.addProperty(t);
		tileSubjects[tileId].notifyListeners();
		playerSubject.notifyListeners();

	}

	public Tile getTileById(int tileId) {
		return tiles[tileId];
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
		if (!fromPlayer.hasSufficientFunds(price))
			throw new RuntimeException("Cannot complete transaction: \n\t"
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
	public void createPlayers(List<String> playerNames, Locale loc) {
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
					"the tile in question is not a property: this transaction cannot be completed");
		return ((Property) t);
	}

	/**
	 * casts a tile to a terrain if it is a terrain, otherwise throw an
	 * exception
	 * 
	 * @param t
	 *            the tile to cast
	 * @return the tile casted to a terrain
	 */
	public Terrain castTileToTerrain(Tile t) {
		if (!(t instanceof Terrain))
			throw new RuntimeException(
					"the tile in question is not a property: this transaction cannot be completed");
		return ((Terrain) t);
	}

	/**
	 * buy a property that is currently owned by the bank at the price that is
	 * written on the card
	 * 
	 * @param tileId
	 *            the id of the property to be bought
	 */
	public void buyCurrentPropertyForPlayer(String playerName, int tileId) {
		Tile t = tiles[tileId];
		Property p = castTileToProperty(t);
		if (!(p.getOwner().getName().equals("bank")))
			throw new RuntimeException(
					"The property to be bought is not owned by the bank, use transfer property instead");
		int priceOfProperty = p.getPrice();
		if (!playerHasSufficientFunds(playerName, priceOfProperty))
			throw new RuntimeException(
					"Player Does not have enough money to by the property from the bank");
		Player player = getPlayerByName(playerName);
		player.addProperty(p);
		p.setOwner(player);
		player.withdawMoney(p.getPrice());
		playerSubject.notifyListeners();
	}

	
	/**
	 * transfer jail cards from one player to another.  the string "CurrentPlayer" should be used to represent the currentPlayer.
	 * @param fromName the name of the player to transfer the card from
	 * @param toName the name of the player to transfer the card to
	 * @param tileId the integer number which represent the tile to be transfered
	 */
	public void transferJailCards(String fromName, String toName, int quantity, int price){
		Player fromPlayer = getPlayerByName(fromName);
		if (fromPlayer.getJailCard() < quantity)
			throw new RuntimeException("Cannot complete transaction: \n\t"
					+ fromPlayer.getName()
					+ " has insufficient jail cards. \n\tReqeusted to transfer: "
					+ quantity + " quantity available: " + fromPlayer.getJailCard());
		//Switch order of fromName and toName because money goes in opposite direction than do jailCards
		transferMoney(toName, fromName, price);
		removeJailCardFromPlayer(fromName);
		addJailCardToPlayer(toName);
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
	public boolean playerHasSufficientFunds(String playerName, int amount) {
		Player plyr = getPlayerByName(playerName);
		return (plyr.hasSufficientFunds(amount));
	}
	
	
	/**
	 * gives the given player a jail card
	 * @param String the name of the player to change
	 */
	public void addJailCardToPlayer(String playerName){
		Player plyr = getPlayerByName(playerName);
		int jailCards = plyr.getJailCard(); 
		plyr.setJailCard(jailCards+1);
	}
	
	/**
	 * removes a jail card from the given player
	 * @param String the name of the player to change
	 */
	public void removeJailCardFromPlayer(String playerName){
		Player plyr = getPlayerByName(playerName);
		int jailCards = plyr.getJailCard(); 
		plyr.setJailCard(jailCards-1);
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

	public void initGUI() {
		playerSubject.notifyListeners();
	}

}
