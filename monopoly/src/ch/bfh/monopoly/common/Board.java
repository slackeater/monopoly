package ch.bfh.monopoly.common;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JPanel;
import ch.bfh.monopoly.exception.TransactionException;
import ch.bfh.monopoly.gui.MonopolyGUI;
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
	private PlayerSubject playerSubject, movementSubject;
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
//				System.err.println("board: PLAYER SUBJECT: LOOOOOOPED");
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
//			System.err
//					.println("board: PLAYER SUBJECT:  size of playerState LIST: "
//							+ playerStates.size());
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

	public PlayerSubject getSubjectForMovement() {
		return movementSubject;
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
		movementSubject = new ConcretePlayerSubject();
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
	 * get the name of the event
	 * 
	 * @return the name of the event
	 */
	public String getTileEventName(int tileId) {
		Tile t = getTileById(tileId);
		return t.getName();
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
		System.err.println("\t board: updateTurnTokens: received player "
				+ playerBeginningTurn);
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
			throw new TransactionException(rb.getString("ownAllBeforeBuild"));
		Tile t = tiles[tileId];
		Terrain terrain = castTileToTerrain(t);
		if (availableHouses < 1)
			throw new TransactionException(rb.getString("noHousesAvailable"));
		if (terrain.getHouseCount() >= 4)
			throw new TransactionException(rb.getString("maxNumberHouses"));
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
	 * @return the cost of building
	 * @throws TransactionException
	 */
	public int buyHouseRow(String playerName, int tileId)
			throws TransactionException {
		Tile t = tiles[tileId];
		Player plyr = getPlayerByName(playerName);
		Terrain terrain = castTileToTerrain(t);
		String groupName = terrain.getGroup();
		List<Terrain> groupMembers = getGroupMembers(groupName);
		int costToBuild = terrain.getHouseCost() * groupMembers.size();
		// Check if player is owner of all the properties in the group
		if (!playerOwnsTileGroup(playerName, tileId))
			throw new TransactionException(rb.getString("ownAllBeforeBuild"));
		playerHasSufficientFunds(playerName, costToBuild);
		if (availableHouses < groupMembers.size())
			throw new TransactionException(
					rb.getString("notEnoughHousesAvailable"));
		// check if on any of the tiles there are already 4 houses
		for (Terrain groupMember : groupMembers) {
			if (groupMember.getHouseCount() >= 4)
				throw new TransactionException(rb.getString("maxNumberHouses"));
		}
		for (Terrain groupMember : groupMembers) {
			groupMember.buildHouse();
			availableHouses--;
			tileSubjects[groupMember.getTileId()].notifyListeners();
		}
		plyr.withdawMoney(costToBuild);
		playerSubject.notifyListeners();
		return costToBuild;
	}

	/**
	 * buy 1 hotel for each property belonging to a group
	 * 
	 * @param tileId
	 *            the id of any tile in the group to build on
	 * @throws TransactionException
	 */
	public int buyHotelRow(String playerName, int tileId)
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
			throw new TransactionException(rb.getString("ownAllBeforeBuild"));
		playerHasSufficientFunds(playerName, costToBuild);
		if (availableHotels < groupMembers.size())
			throw new TransactionException(
					rb.getString("notEnoughHotelsAvailable"));
		// check if on any of the tiles there are already 4 houses
		for (Terrain groupMember : groupMembers) {
			if (groupMember.getHotelCount() >= 1)
				throw new TransactionException(
						rb.getString("hotelAlreadyPresent"));
		}
		for (Terrain groupMember : groupMembers) {
			groupMember.buildHotel();
			groupMember.removeAllHouses();
			availableHotels--;
			tileSubjects[groupMember.getTileId()].notifyListeners();
		}
		plyr.withdawMoney(costToBuild);
		playerSubject.notifyListeners();
		return costToBuild;
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
			throw new TransactionException(rb.getString("ownAllBeforeBuild"));
		Terrain terrain = castTileToTerrain(t);
		if (availableHotels < 1)
			throw new TransactionException(rb.getString("noHotelsAvailable"));
		if (terrain.getHouseCount() != 4)
			throw new TransactionException(
					rb.getString("mustHave4HousesForHotel"));
		if (terrain.getHotelCount() >= 1)
			throw new TransactionException(rb.getString("maxNumberHotels"));
		int costToBuild = terrain.getHotelCost();
		playerHasSufficientFunds(playerName, costToBuild);
		terrain.buildHotel();
		terrain.removeAllHouses();
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
					rb.getString("noHousesPresentOnTile") + " "
							+ getTileById(tileId).getName());
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
	public int sellHouseRow(String playerName, int tileId)
			throws TransactionException {
		Tile t = tiles[tileId];
		Terrain terrain = castTileToTerrain(t);
		Player plyr = getPlayerByName(playerName);
		String groupName = terrain.getGroup();
		List<Terrain> groupMembers = getGroupMembers(groupName);
		int amountOfSale = terrain.getHouseCost() * groupMembers.size();
		// Check if player is owner of all the properties in the group
		if (!playerOwnsTileGroup(playerName, tileId))
			throw new TransactionException(rb.getString("youDontOwnAllProp"));
		// check that all tiles in the group have a house to sell
		for (Terrain groupMember : groupMembers) {
			if (groupMember.getHouseCount() < 1)
				throw new TransactionException(
						rb.getString("atLeastOneTileNoHouse"));
		}
		for (Terrain groupMember : groupMembers) {
			groupMember.removeHouse();
			availableHouses++;
			tileSubjects[groupMember.getTileId()].notifyListeners();
		}
		plyr.depositMoney(amountOfSale);
		playerSubject.notifyListeners();
		return amountOfSale;
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
					rb.getString("noHotelsPresentOnTile") + " "
							+ getTileById(tileId).getName());
		terrain.removeHotel();
		terrain.setHousesTo4();
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
	public int sellHotelRow(String playerName, int tileId)
			throws TransactionException {
		Tile t = tiles[tileId];
		Terrain terrain = castTileToTerrain(t);
		Player plyr = getPlayerByName(playerName);
		String groupName = terrain.getGroup();
		List<Terrain> groupMembers = getGroupMembers(groupName);
		int amountOfSale = terrain.getHotelCost() * groupMembers.size();
		// Check if player is owner of all the properties in the group
		if (!playerOwnsTileGroup(playerName, tileId))
			throw new TransactionException(rb.getString("youDontOwnAllProp"));
		// check that all tiles in the group have a house to sell
		for (Terrain groupMember : groupMembers) {
			if (groupMember.getHotelCount() < 1)
				throw new TransactionException(
						rb.getString("atLeastOneTileNoHotele"));
		}
		for (Terrain groupMember : groupMembers) {
			groupMember.removeHotel();
			groupMember.setHousesTo4();
			availableHotels++;
			tileSubjects[groupMember.getTileId()].notifyListeners();
		}
		plyr.depositMoney(amountOfSale);
		playerSubject.notifyListeners();
		return amountOfSale;

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
	public int toggleMortgageStatus(String playerName, int tileId)
			throws RuntimeException, TransactionException {
		Tile t = tiles[tileId];
		checkPlayerIsOwnerOfTile(playerName, tileId);
		Property prop = castTileToProperty(t);
		// if mortgage is active
		int amount;
		if (prop.isMortgageActive()) {
			// try to withdraw the sum required to unmortgage the property
			amount = (prop.getMortgageValue() / 10) + prop.getMortgageValue();
			playerHasSufficientFunds(playerName, amount);
			prop.getOwner().withdawMoney(amount);
			prop.setMortgageActive(false);
		}
		// if mortgage is not active
		else {
			if (prop instanceof Terrain) {
				Terrain terrain = (Terrain) prop;

				// check that there are not hotels or houses on the property
				if (terrain.getHotelCount() != 0
						|| terrain.getHouseCount() != 0)
					throw new TransactionException(
							rb.getString("inOrderToMortgage"));
			}
			// credit the owner with the mortgage value
			amount = prop.getMortgageValue();
			prop.getOwner().depositMoney(amount);
			prop.setMortgageActive(true);
		}
		tileSubjects[tileId].notifyListeners();
		playerSubject.notifyListeners();
		return amount;
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
					rb.getString("playerDoesntOwnPropToTransfer"));
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
			throw new TransactionException(
					rb.getString("cantCompleteTransaction") + " "
							+ fromPlayer.getName()
							+ rb.getString("hasntEnoughMoney-Requested") + " "
							+ +price + " " + rb.getString("accountBalance")
							+ " " + fromPlayer.getAccount());
		System.out.println("Board: transferMoney: to " + toName + " from "
				+ fromPlayer.getName() + " " + +price + " "
				+ rb.getString("accountBalance") + " "
				+ fromPlayer.getAccount());
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
					rb.getString("cantCompleteTransaction") + " "
							+ fromPlayer.getName() + " "
							+ rb.getString("hasntEnoughJailCards") + " "
							+ quantity + " "
							+ rb.getString("quantityAvailable") + " "
							+ +fromPlayer.getJailCard());
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
			Player plyr = new Player(playerNames.get(i), startMoney, tokens[i],
					loc);
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
	public int freeParking(String playerName) {
		Player plyr = getPlayerByName(playerName);
		plyr.depositMoney(freeParking);
		int oldFreeParking = freeParking;
		freeParking = 0;
		playerSubject.notifyListeners();
		return oldFreeParking;
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
			throw new RuntimeException("No player with the name �" + name
					+ "� could be found");
		return p;
	}

	public void checkPlayerIsOwnerOfTile(String playerName, int tileId)
			throws TransactionException {
		Property prop = castTileToProperty(tiles[tileId]);
		String ownerName = prop.getOwner().getName();
		if (!playerName.equals(ownerName))
			throw new TransactionException(playerName + " "
					+ rb.getString("doesNotOwn") + " " + prop.getName() + " "
					+ rb.getString("ownedBy") + " " + prop.getOwner().getName());
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
		// System.out.println("board.castTileToProperty received: tileId:"
		// + t.getTileId() + "which is:" + t.getName());

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
					"the tile in question is not a Terrain: this transaction cannot be completed");
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
			throw new TransactionException(playerName + " "
					+ rb.getString("notEnoughMoney"));
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
	public boolean advancePlayerNSpacesInDirection(String playerName, int n,
			MonopolyGUI.Direction dir) {
		boolean passedGo = false;
		Player plyr = getPlayerByName(playerName);
		String direction = "forwards";
		if (dir == MonopolyGUI.Direction.BACKWARDS)
			direction = "backwards";
		// System.out.println("BOARDadvance: received advancePlayer: " + n
		// + "in direction " + direction);
		int previousPosition = plyr.getPosition();

		int currentPos = plyr.getPosition();
		if (dir == MonopolyGUI.Direction.BACKWARDS)
			plyr.setPosition((currentPos - n) % 40);
		else
			plyr.setPosition((currentPos + n) % 40);
		plyr.setRollValue(n);
		plyr.setDir(dir);
		int newPosition = plyr.getPosition();
		// System.out.println("BOARDadvance:" + playerName
		// + "'s new position is: " + newPosition);
		// if passes go
		if (newPosition < previousPosition && !plyr.isInJail() && dir==MonopolyGUI.Direction.FORWARDS) {
			passedGo = true;
			passGo(plyr);
		}
		movementSubject.notifyListeners();
		plyr.resetRollValue();
		return passedGo;
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

	public void dividePlayerAssets(String playerWhoLeft) {
		System.err.println("board: dividePlayerAssets:  size of PLAYER LIST: "
				+ players.size());
		// put in a new list all the players except the one who is leaving
		ArrayList<Player> playersMinusOne = new ArrayList<Player>();
		for (Player p : players) {
			if (!p.getName().equals(playerWhoLeft))
				playersMinusOne.add(p);
		}
		Player plyrGone = getPlayerByName(playerWhoLeft);

		// divide up properties
		ArrayList<Tile> propToDivide = plyrGone.getProperties();
		int playerListIndex = 0;
		int playerListSize = playersMinusOne.size();
		int moneyChunks = plyrGone.getAccount() / playersMinusOne.size();
		// make a list of the properties with the tileIds in order to avoir the
		// concurrentModificationException
		int[] listOfTileIds = new int[propToDivide.size()];
		for (int i = 0; i < propToDivide.size(); i++) {
			listOfTileIds[i] = propToDivide.get(i).getTileId();
		}
		for (int i = 0; i < listOfTileIds.length; i++) {
			int modularIndex = i % playersMinusOne.size();
			Player plyrToGetProperty = playersMinusOne.get(modularIndex);
			try {
				transferProperty(playerWhoLeft, plyrToGetProperty.getName(),
						listOfTileIds[i], 0);
			} catch (TransactionException e) {
				// this error is if the to player doesn't have enough money...
				// which isn't the case with price 0
				e.printStackTrace();
			}
			playerListIndex = playerListIndex++ % playerListSize;
		}
		// divide money
		for (Player p : playersMinusOne) {
			try {
				transferMoney(playerWhoLeft, p.getName(), moneyChunks);
			} catch (TransactionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// divide jail cards
		int jailCards = plyrGone.getJailCard();
		for (int i = 0; i < jailCards; i++) {
			try {
				transferJailCards(playerWhoLeft, playersMinusOne.get(i)
						.getName(), 1, 0);
			} catch (TransactionException e) {
				// this error is if the to player doesn't have enough money...
				// which isn't the case with price 0
				e.printStackTrace();
			}
		}
		System.err
				.println("board: LEAVING dividePlayerAssets:  size of PLAYER LIST: "
						+ players.size());
		// players.remove(plyrGone);
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

			tileInfo.setOwner(t.getOwner().getName());

			tileInfo.setGroup(t.getGroup());
			tileInfo.setMortgageValue(t.getMortgageValue());
			tileInfo.setCoordX(t.getCoordX());
			tileInfo.setCoordY(t.getCoordY());
			tileInfo.setTileId(t.getTileId());
			// tileInfo.setRent(t.feeToCharge());
			// tileInfo.setRGB(t.getRG)
		}
		if (tile instanceof Utility) {
			Utility t = (Utility) tile;
			tileInfo.setName(t.getName());
			tileInfo.setPrice(t.getPrice());

			tileInfo.setOwner(t.getOwner().getName());
			tileInfo.setRent(t.feeToCharge());
			tileInfo.setGroup(t.getGroup());
			tileInfo.setMortgageValue(t.getMortgageValue());
			tileInfo.setCoordX(t.getCoordX());
			tileInfo.setCoordY(t.getCoordY());
			tileInfo.setTileId(t.getTileId());
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
		movementSubject.notifyListeners();
	}

	public int getBail() {
		return bail;
	}

	public void distributeProperties() {
		int playerIndex = 0;
		for (Player p : players) {
			p.depositMoney(105000);
		}
		for (int i = 0; i < tiles.length; i++) {
			if (tiles[i] instanceof Terrain) {
//				Player plyr = players.get(playerIndex++ % players.size());
				Player plyr = players.get(0);
				System.out.println("DISTRIBUTE " + tiles[i].getName() + " GOES TO  "+ plyr.getName());
				plyr.addProperty(tiles[i]);
				castTileToTerrain(tiles[i]).setOwner(plyr);

			}
		}
	}

}
