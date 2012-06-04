package ch.bfh.monopoly.common;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JPanel;
import ch.bfh.monopoly.exception.TransactionException;
import ch.bfh.monopoly.gui.MonopolyGUI;
import ch.bfh.monopoly.net.Messages;
import ch.bfh.monopoly.net.NetMessage;
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
	private Player bank;
	private TileSubject[] tileSubjects;
	private Token[] tokens = new Token[8];
	private int freeParking = 500;
	private PlayerSubject playerSubject;
	private final int goMoney, bail;
	private boolean testOff;
	private ResourceBundle rb;

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
					terrains[t.getTileId()] = true;
				}
				PlayerStateEvent pse = new PlayerStateEvent(plyr.getPosition(),
						plyr.getPreviousPosition(), plyr.getRollValue(),
						plyr.getName(), plyr.isInJail(), plyr.getAccount(),
						plyr.hasTurnToken(), plyr.getJailCard(), terrains,
						plyr.getToken(), plyr.getDir());
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
						terrain.getOwner().getName(),
						terrain.isMortgageActive());
				for (TileListener tl : listeners) {
					tl.updateTile(tsi);
				}
			}
		}
	}

	public Board(GameClient gameClient, boolean testOff) {
		this.testOff = testOff;
		this.bank = gameClient.getBankPlayer();
		rb = ResourceBundle.getBundle("ch.bfh.monopoly.resources.tile",
				gameClient.getLoc());
		goMoney = Integer.parseInt(rb.getString("goMoney"));
		bail = Integer.parseInt(rb.getString("bail"));
		// create tiles, cards, and events and tokens
		TileCreator tc = new TileCreator(gameClient, testOff);
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
	 * Get the JPanel for the tile's event. Should be called when a player rolls
	 * and lands on a new tile
	 * 
	 * @param the
	 *            id of the tile of which to get the JPanel
	 * @return the JPanel that the GUI will display
	 */
	public JPanel getTileEventPanelForTile(int tileId) {
		Tile t = getTileById(tileId);
		return t.getTileEventPanel();
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
	 * set the turn tokens to true for all players except the one provided as
	 * param
	 * 
	 * @param playerBeginningTurn
	 *            who should have a turn token TRUE
	 * @param playerEndingTurn
	 *            player who should have a turn token set to false
	 */
	public void updateTurnTokens(String playerBeginningTurn,
			String playerEndingTurn) {
		Player plyrBegin = getPlayerByName(playerBeginningTurn);
		plyrBegin.setTurnToken(true);
		if (playerEndingTurn != null) {
			Player plyrEnd = getPlayerByName(playerEndingTurn);
			plyrEnd.setTurnToken(false);
		}
		playerSubject.notifyListeners();
	}

	/**
	 * credit the current player's account his GO money
	 */
	public void passGo(Player currentPlayer) {
		currentPlayer.depositMoney(goMoney);
		playerSubject.notifyListeners();
	}

	/**
	 * buy a property that is currently owned by the bank at the price that is
	 * written on the card
	 * 
	 * @param tileId
	 *            the id of the property to be bought
	 * @throws TransactionException
	 */
	public void buyCurrentPropertyForPlayer(String playerName, int tileId)
			throws TransactionException {
		Tile t = tiles[tileId];
		Property p = castTileToProperty(t);
		if (!(p.getOwner().getName().equals("bank")))
			throw new RuntimeException(
					"The property to be bought is not owned by the bank, use transfer property instead");
		int priceOfProperty = p.getPrice();
		playerHasSufficientFunds(playerName, priceOfProperty);
		Player player = getPlayerByName(playerName);
		player.addProperty(p);
		p.setOwner(player);
		player.withdawMoney(p.getPrice());
		playerSubject.notifyListeners();
	}

	/**
	 * buy a house for a given property checks that the tileId provided refers
	 * to a terrain
	 * 
	 * @param playerName
	 *            the name of the player to build for
	 * @param tileId
	 *            the tile number of the property to build a house on
	 * @throws TransactionException
	 */
	public void buyHouse(String playerName, int tileId)
			throws TransactionException {
		if (!playerOwnsTileGroup(playerName, tileId))
			throw new TransactionException(
					rb.getString("ownAllBeforeBuild"));
		Tile t = tiles[tileId];
		Terrain terrain = castTileToTerrain(t);
		if (availableHouses < 1)
			throw new TransactionException(
					"No houses available to complete the transaction");
		if (terrain.getHouseCount() >= 4)
			throw new TransactionException(
					"The maximum number of houses that may be built on a property is 4");
		int costToBuild = terrain.getHouseCost();
		playerHasSufficientFunds(playerName, costToBuild);
		terrain.buildHouse();
		availableHouses--;
		int price = terrain.getHouseCost();
		terrain.getOwner().withdawMoney(price);
		tileSubjects[tileId].notifyListeners();
		playerSubject.notifyListeners();
	}

	/**
	 * buy 1 house for each property belonging to a group
	 * 
	 * @param tileId
	 *            the id of any tile in the group to build on
	 * @throws TransactionException
	 */
	public void buyHouseRow(String playerName, int tileId)
			throws TransactionException {
		Tile t = tiles[tileId];
		Player plyr = getPlayerByName(playerName);
		Terrain terrain = castTileToTerrain(t);
		String groupName = terrain.getGroup();
		List<Terrain> groupMembers = getGroupMembers(groupName);
		int costToBuild = terrain.getHouseCost() * groupMembers.size();
		// Check if player is owner of all the properties in the group
		if (!playerOwnsTileGroup(playerName, tileId))
			throw new TransactionException(
					"You don't own all the properties in the group.");
		playerHasSufficientFunds(playerName, costToBuild);
		if (availableHouses < groupMembers.size())
			throw new TransactionException(
					"Not enough houses available to complete the transaction");
		// check if on any of the tiles there are already 4 houses
		for (Terrain groupMember : groupMembers) {
			if (groupMember.getHouseCount() >= 4)
				throw new TransactionException(
						"There are already 4 houses on one of the properties");
		}
		for (Terrain groupMember : groupMembers) {
			groupMember.buildHouse();
			availableHouses--;
			tileSubjects[groupMember.getTileId()].notifyListeners();
		}
		plyr.withdawMoney(costToBuild);
		playerSubject.notifyListeners();
	}

	/**
	 * buy 1 hotel for each property belonging to a group
	 * 
	 * @param tileId
	 *            the id of any tile in the group to build on
	 * @throws TransactionException
	 */
	public void buyHotelRow(String playerName, int tileId)
			throws TransactionException {
		Tile t = tiles[tileId];
		Terrain terrain = castTileToTerrain(t);
		String groupName = terrain.getGroup();
		Player plyr = getPlayerByName(playerName);
		List<Terrain> groupMembers = getGroupMembers(groupName);
		for (Terrain ter : groupMembers) {
			System.out.println(ter.getName() + " is in group: "
					+ ter.getGroup());
		}
		int costToBuild = terrain.getHotelCost() * groupMembers.size();
		// Check if player is owner of all the properties in the group
		if (!playerOwnsTileGroup(playerName, tileId))
			throw new TransactionException(
					"You don't own all the properties in the group.");
		playerHasSufficientFunds(playerName, costToBuild);
		if (availableHotels < groupMembers.size())
			throw new TransactionException(
					"Not enough hotels available to complete the transaction");
		// check if on any of the tiles there are already 4 houses
		for (Terrain groupMember : groupMembers) {
			if (groupMember.getHotelCount() >= 1)
				throw new TransactionException(
						"There is already 1 hotel on one of the properties");
		}
		for (Terrain groupMember : groupMembers) {
			groupMember.buildHotel();
			availableHotels--;
			tileSubjects[groupMember.getTileId()].notifyListeners();
		}
		plyr.withdawMoney(costToBuild);
		playerSubject.notifyListeners();
	}

	/**
	 * buy a hotel for a given property checks that the tileId provided refers
	 * to a terrain
	 * 
	 * @param playerName
	 *            the name of the player to build for
	 * @param tileId
	 *            the tile number of the property to build a house on
	 * @throws TransactionException
	 */
	public void buyHotel(String playerName, int tileId)
			throws TransactionException {
		Tile t = tiles[tileId];
		if (!playerOwnsTileGroup(playerName, tileId))
			throw new TransactionException(
					"You don't own all the properties in the group.");
		Terrain terrain = castTileToTerrain(t);
		if (availableHotels < 1)
			throw new TransactionException(
					"No hotels available to complete the transaction");
		if (terrain.getHouseCount() != 4)
			throw new TransactionException(
					"There must be 4 houses present on this property in order to build a hotel");
		if (terrain.getHotelCount() >= 1)
			throw new TransactionException(
					"You can't build more than one hotel on a tile.");
		int costToBuild = terrain.getHotelCost();
		playerHasSufficientFunds(playerName, costToBuild);
		terrain.buildHotel();
		availableHotels--;
		int price = terrain.getHotelCost();
		terrain.getOwner().withdawMoney(price);
		tileSubjects[tileId].notifyListeners();
		playerSubject.notifyListeners();
	}

	/**
	 * sell a house for a given property
	 * 
	 * @param tileID
	 *            the tile number of the property to sell a house from
	 * @throws TransactionException
	 */
	public void sellHouses(int tileId) throws TransactionException {
		Tile t = tiles[tileId];
		Terrain terrain = castTileToTerrain(t);
		if (terrain.getHouseCount() <= 0)
			throw new TransactionException(
					"There are no houses present on tile with tile Id="
							+ tileId);
		terrain.removeHouse();
		availableHotels++;
		int price = terrain.getHouseCost();
		terrain.getOwner().depositMoney(price);
		tileSubjects[tileId].notifyListeners();
		playerSubject.notifyListeners();
	}

	/**
	 * sell 1 house for each property belonging to a group
	 * 
	 * @param tileId
	 *            the id of any tile in the group to sell from
	 * @throws TransactionException
	 */
	public void sellHouseRow(String playerName, int tileId)
			throws TransactionException {
		Tile t = tiles[tileId];
		Terrain terrain = castTileToTerrain(t);
		Player plyr = getPlayerByName(playerName);
		String groupName = terrain.getGroup();
		List<Terrain> groupMembers = getGroupMembers(groupName);
		int amountOfSale = terrain.getHouseCost() * groupMembers.size();
		// Check if player is owner of all the properties in the group
		if (!playerOwnsTileGroup(playerName, tileId))
			throw new TransactionException(
					"You don't own all the properties in the group.");
		// check that all tiles in the group have a house to sell
		for (Terrain groupMember : groupMembers) {
			if (groupMember.getHouseCount() < 1)
				throw new TransactionException(
						"At least one tile does not have a house to sell");
		}
		for (Terrain groupMember : groupMembers) {
			groupMember.removeHouse();
			availableHouses++;
			tileSubjects[groupMember.getTileId()].notifyListeners();
		}
		plyr.depositMoney(amountOfSale);
		playerSubject.notifyListeners();
	}

	/**
	 * sell a hotel for a given property
	 * 
	 * @param tileID
	 *            the tile number of the property to sell a hotel from
	 * @throws TransactionException
	 */
	public void sellHotel(String playerName, int tileId)
			throws TransactionException {
		Tile t = tiles[tileId];
		Player plyr = getPlayerByName(playerName);
		checkPlayerIsOwnerOfTile(playerName, tileId);
		Terrain terrain = castTileToTerrain(t);
		if (terrain.getHotelCount() <= 0)
			throw new TransactionException(
					"No hotels present on tile with tile Id=" + tileId);
		terrain.removeHotel();
		availableHotels++;
		int price = terrain.getHotelCost();
		plyr.depositMoney(price);
		tileSubjects[tileId].notifyListeners();
		playerSubject.notifyListeners();
	}

	/**
	 * sell 1 hotel for each property belonging to a group
	 * 
	 * @param tileId
	 *            the id of any tile in the group to sell from
	 * @throws TransactionException
	 */
	public void sellHotelRow(String playerName, int tileId)
			throws TransactionException {
		Tile t = tiles[tileId];
		Terrain terrain = castTileToTerrain(t);
		Player plyr = getPlayerByName(playerName);
		String groupName = terrain.getGroup();
		List<Terrain> groupMembers = getGroupMembers(groupName);
		int amountOfSale = terrain.getHotelCost() * groupMembers.size();
		// Check if player is owner of all the properties in the group
		if (!playerOwnsTileGroup(playerName, tileId))
			throw new TransactionException(
					"You don't own all the properties in the group.");
		// check that all tiles in the group have a house to sell
		for (Terrain groupMember : groupMembers) {
			if (groupMember.getHotelCount() < 1)
				throw new TransactionException(
						"At least one tile does not have a hotel to sell");
		}
		for (Terrain groupMember : groupMembers) {
			groupMember.removeHotel();
			availableHotels++;
			tileSubjects[groupMember.getTileId()].notifyListeners();
		}
		plyr.depositMoney(amountOfSale);
		playerSubject.notifyListeners();

	}

	/**
	 * Toggles the mortgage status of a given property
	 * 
	 * @param tileId
	 *            the id that corresponds to a tile for which we want to toggle
	 *            the mortgage status.
	 * @throws RuntimeException
	 * @throws TransactionException
	 */
	public void toggleMortgageStatus(String playerName, int tileId)
			throws RuntimeException, TransactionException {
		Tile t = tiles[tileId];
		checkPlayerIsOwnerOfTile(playerName, tileId);
		Property prop = castTileToProperty(t);
		Terrain terrain = castTileToTerrain(prop);
		// if mortgage is active
		int amount;
		if (prop.isMortgageActive()) {
			// try to withdraw the sum required to unmortgage the property
			amount = prop.getPrice() / 10;
			playerHasSufficientFunds(playerName, amount);
			prop.getOwner().withdawMoney(amount);
			prop.setMortgageActive(false);
		}
		// if mortgage is not active
		else {
			// check that there are not hotels or houses on the property
			if (terrain.getHotelCount() != 0 || terrain.getHouseCount() != 0)
				throw new TransactionException(
						"In order to mortgage this property, all houses and hotels must first be sold to the bank");
			// credit the owner with the mortgage value
			amount = prop.getMortgageValue();
			prop.getOwner().depositMoney(amount);
			prop.setMortgageActive(true);
		}
		tileSubjects[tileId].notifyListeners();
		playerSubject.notifyListeners();
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
	 * @throws TransactionException
	 */
	public void transferProperty(String fromName, String toName, int tileId,
			int price) throws TransactionException {
		Tile t = tiles[tileId];
		Property prop = castTileToProperty(t);
		if (!prop.getOwner().getName().equals(fromName))
			throw new TransactionException(
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

	/**
	 * transfers a given amount of money from one player to another
	 * 
	 * @param fromName
	 *            name of the player to take money from
	 * @param toName
	 *            name of the player to give the money to
	 * @param price
	 *            the amount of money to transfer
	 * @throws TransactionException
	 */
	public void transferMoney(String fromName, String toName, int price)
			throws TransactionException {
		Player fromPlayer = getPlayerByName(fromName);
		if (!fromPlayer.hasSufficientFunds(price))
			throw new TransactionException("Cannot complete transaction: \n\t"
					+ fromPlayer.getName()
					+ " has insufficient funds. \n\tReqeusted to transfer: "
					+ price + " account balance: " + fromPlayer.getAccount());
		Player toPlayer = getPlayerByName(toName);
		fromPlayer.withdawMoney(price);
		toPlayer.depositMoney(price);
		playerSubject.notifyListeners();
	}

	/**
	 * transfer jail cards from one player to another. the string
	 * "CurrentPlayer" should be used to represent the currentPlayer.
	 * 
	 * @param fromName
	 *            the name of the player to transfer the card from
	 * @param toName
	 *            the name of the player to transfer the card to
	 * @param tileId
	 *            the integer number which represent the tile to be transfered
	 * @throws TransactionException
	 */
	public void transferJailCards(String fromName, String toName, int quantity,
			int price) throws TransactionException {
		Player fromPlayer = getPlayerByName(fromName);
		if (fromPlayer.getJailCard() < quantity)
			throw new TransactionException(
					"Cannot complete transaction: \n\t"
							+ fromPlayer.getName()
							+ " has insufficient jail cards. \n\tReqeusted to transfer: "
							+ quantity + " quantity available: "
							+ fromPlayer.getJailCard());
		// Switch order of fromName and toName because money goes in opposite
		// direction than do jailCards
		transferMoney(toName, fromName, price);
		removeJailCardFromPlayer(fromName);
		addJailCardToPlayer(toName);
		playerSubject.notifyListeners();
	}

	/**
	 * gives the given player a jail card
	 * 
	 * @param String
	 *            the name of the player to change
	 */
	public void addJailCardToPlayer(String playerName) {
		Player plyr = getPlayerByName(playerName);
		int jailCards = plyr.getJailCard();
		plyr.setJailCard(jailCards + 1);
		playerSubject.notifyListeners();
	}

	/**
	 * removes a jail card from the given player
	 * 
	 * @param String
	 *            the name of the player to change
	 */
	public void removeJailCardFromPlayer(String playerName) {
		Player plyr = getPlayerByName(playerName);
		int jailCards = plyr.getJailCard();
		plyr.setJailCard(jailCards - 1);
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
		String bundleData = ResourceBundle.getBundle(
				"ch.bfh.monopoly.resources.tile", loc).getString("startMoney");
		bundleData = bundleData.trim();
		int startMoney = Integer.parseInt(bundleData);
		for (int i = 0; i < playerNames.size(); i++) {
			Player plyr = new Player(playerNames.get(i), startMoney, tokens[i]);
			players.add(plyr);
		}
	}

	/**
	 * the player is charged a fee and the amount of the fee is withdrawn from
	 * his bank account. This amount is added to the FREE PARKING
	 * 
	 * @param playerName
	 *            the name of the player to charge the fee to
	 * @param fee
	 *            the amount of money to withdraw from the current player's
	 *            account
	 * @throws TransactionException
	 */
	public void payFee(String playerName, int fee) throws TransactionException {
		Player plyr = getPlayerByName(playerName);
		plyr.withdawMoney(fee);
		freeParking += fee;
		playerSubject.notifyListeners();
	}

	/**
	 * called by BirthdayEvent class, transfer a given amount of money from all
	 * players except for the given player to the given player's account
	 * 
	 * @throws TransactionException
	 *             if there is not enough money to withdraw from an account
	 */
	public void birthdayEvent(String playerName, int amount)
			throws TransactionException {
		Player plyr = getPlayerByName(playerName);
		for (Player p : players) {
			if (p != plyr) {
				p.withdawMoney(amount);
				plyr.depositMoney(amount);
			}
		}
		playerSubject.notifyListeners();
	}

	/**
	 * the current player gets all the money in the free parking account
	 * 
	 * @return
	 */
	public void freeParking(String playerName) {
		Player plyr = getPlayerByName(playerName);
		plyr.depositMoney(freeParking);
		freeParking = 0;
		playerSubject.notifyListeners();
	}

	/**
	 * get the tile with the id that is equal to the one passed as a parameter
	 * 
	 * @param tileId
	 *            the id of the tile to find
	 * @return the tile with the id tileId
	 */
	public Tile getTileById(int tileId) {
		return tiles[tileId];
	}

	/**
	 * get a tile by name
	 * 
	 * @param the
	 *            name of the tile you wish to find
	 * @return the tile with the given name
	 */
	public Tile getTileByName(String tileName) {
		for (int i = 0; i < tiles.length; i++) {
			if (tiles[i].getName().equals(tileName)) {
				System.out.println("Board: tileByName found:"
						+ tiles[i].getName());
				return tiles[i];
			}
		}
		return null;
	}

	/**
	 * get a tile id by name
	 * 
	 * @param the
	 *            name of the tile you wish to find
	 * @return the tile with the given name
	 */
	public int getTileIdByName(String tileName) {
		for (int i = 0; i < tiles.length; i++) {
			if (tiles[i].getName().equals(tileName)) {
				System.out.println("Board: tileByName found:"
						+ tiles[i].getName());
				return i;
			}
		}
		return -1;
	}

	/**
	 * get the player object whose name field corresponds to a given name
	 * 
	 * @param name
	 *            the string of the name of the player
	 * @throws TransactionException
	 */
	public Player getPlayerByName(String name) throws RuntimeException {
		Player p = null;
		for (int i = 0; i < players.size(); i++) {
			String playerName = players.get(i).getName();
			if (playerName.equalsIgnoreCase(name))
				p = players.get(i);
		}
		if (name.equalsIgnoreCase("bank"))
			p = bank;
		if (p == null)
			throw new RuntimeException("No player with the name Ç" + name
					+ "È could be found");
		return p;
	}

	public void checkPlayerIsOwnerOfTile(String playerName, int tileId)
			throws TransactionException {
		Property prop = castTileToProperty(tiles[tileId]);
		String ownerName = prop.getOwner().getName();
		if (!playerName.equals(ownerName))
			throw new TransactionException(playerName
					+ "does not own the property " + prop.getName()
					+ " It is owned by " + prop.getOwner().getName());
	}

	/**
	 * 
	 * find the other number of group members for a specific tile
	 */
	public List<Terrain> getGroupMembers(String groupName) {
		List<Terrain> groupMembers = new ArrayList<Terrain>();
		for (int i = 0; i < tiles.length; i++) {
			if (tiles[i] instanceof Terrain) {
				Terrain terrain = castTileToTerrain(tiles[i]);
				if (groupName.equals(terrain.getGroup()))
					// System.out.println(terrain.getName() + "WAS ADDED");
					groupMembers.add(terrain);

			}
		}
		return groupMembers;
	}

	/**
	 * check if a given player is the owner of all the properties in a group
	 */
	public boolean playerOwnsTileGroup(String playerName, int tileId) {
		String groupName = castTileToTerrain(getTileById(tileId)).getGroup();
		List<Terrain> groupMembers = getGroupMembers(groupName);
		for (Terrain groupMember : groupMembers) {
			if (!playerName.equals(groupMember.getOwner().getName()))
				return false;
		}
		return true;
	}

	/**
	 * casts a tile to a property if it is a property, otherwise throw an
	 * exception
	 * 
	 * @param t
	 *            the tile to cast
	 * @return the tile casted to a property
	 * @throws RuntimeException
	 */
	public Property castTileToProperty(Tile t) throws RuntimeException {
		System.out.println("board.castTileToProperty received: tileId:"
				+ t.getTileId() + "which is:" + t.getName());

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
	 * @throws RuntimeException
	 */
	public Terrain castTileToTerrain(Tile t) throws RuntimeException {
		if (!(t instanceof Terrain))
			throw new RuntimeException(
					"the tile in question is not a property: this transaction cannot be completed");
		return ((Terrain) t);
	}

	/**
	 * checks if the current player has sufficient funds to pay a fee
	 * 
	 * @param playerName
	 *            the player to check the account of
	 * @param fee
	 *            the amount of the fee to be paid
	 * @throws TransactionException
	 */
	public void playerHasSufficientFunds(String playerName, int amount)
			throws TransactionException {
		Player plyr = getPlayerByName(playerName);
		if (!plyr.hasSufficientFunds(amount))
			throw new TransactionException(playerName
					+ " does not have enough money to perform the action");
	}

	/**
	 * advance the current player a given number n spaces forward
	 */
	public void advancePlayerNSpaces(String playerName, int n) {
		advancePlayerNSpacesInDirection(playerName, n,
				MonopolyGUI.Direction.FORWARDS);
	}

	/**
	 * advance the current player a given number n spaces forward
	 */
	public void advancePlayerNSpacesInDirection(String playerName, int n,
			MonopolyGUI.Direction dir) {
		Player plyr = getPlayerByName(playerName);
		String direction = "forwards";
		if (dir == MonopolyGUI.Direction.BACKWARDS)
			direction = "backwards";
		System.out.println("BOARDadvance: received advancePlayer: " + n
				+ "in direction " + direction);
		int previousPosition = plyr.getPosition();

		int currentPos = plyr.getPosition();
		if (dir == MonopolyGUI.Direction.BACKWARDS)
			plyr.setPosition((currentPos - n) % 40);
		else
			plyr.setPosition((currentPos + n) % 40);
		plyr.setRollValue(n);
		plyr.setDir(dir);
		int newPosition = plyr.getPosition();
		System.out.println("BOARDadvance:" + playerName
				+ "'s new position is: " + newPosition);
		// if passes go
		if (newPosition < previousPosition)
			passGo(plyr);

		playerSubject.notifyListeners();
		plyr.resetRollValue();
	}

	// /**
	// * advance current player to tile n
	// */
	// public void advancePlayerToTile(String playerName, int tileId) {
	// Player plyr = getPlayerByName(playerName);
	// plyr.setPosition(tileId);
	// playerSubject.notifyListeners();
	// }

	/**
	 * sends a given player to jail
	 * 
	 * @param the
	 *            name of the player to send to jail
	 */
	public void setPlayerJailStatus(String playerName, boolean newStatus) {
		Player plyr = getPlayerByName(playerName);
		plyr.setInJail(newStatus);
		playerSubject.notifyListeners();
	}

	/**
	 * gets the currentPlayer out of jail
	 * 
	 * @throws TransactionException
	 *             if the player does not have enough money
	 */
	public void getOutOfJailByPayment(String playerName)
			throws TransactionException {
		payFee(playerName, bail);
		setPlayerJailStatus(playerName, false);

	}

	/**
	 * gets the currentPlayer out of jail
	 */
	public void getOutOfJailByCard(String playerName) {
		removeJailCardFromPlayer(playerName);
		setPlayerJailStatus(playerName, false);
	}

	/**
	 * gets the currentPlayer out of jail by means of rolling
	 */
	public void getOutOfJailByRoll(String playerName) {
		setPlayerJailStatus(playerName, false);
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
			tileInfo.setOwner(t.getOwner().getName());
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
			tileInfo.setTileId(t.getTileId());
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
		} else {
			tileInfo.setName(tile.getName());

			tileInfo.setCoordX(tile.getCoordX());
			tileInfo.setCoordY(tile.getCoordY());
		}
		return tileInfo;
	}

	public int getFreeParkingAccount() {
		return freeParking;
	}

	public int getAvailableHouses() {
		return availableHouses;
	}

	public int getAvailableHotels() {
		return availableHotels;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void initGUI() {
		playerSubject.notifyListeners();
	}

}
