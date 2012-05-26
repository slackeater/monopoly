package ch.bfh.monopoly.tile;

import java.util.Locale;
import java.util.ResourceBundle;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.event.BoardEvent;
import ch.bfh.monopoly.event.EventManager;
import ch.bfh.monopoly.event.GoToJailEvent;
import ch.bfh.monopoly.event.RepairsEvent;

public class TileCreator {

	public Tile[] tiles;
	private Locale loc;
	private EventManager em;
	GameClient gameClient;

	public TileCreator(GameClient gameClient) {
		// create tiles, cards, and events
		this.loc = gameClient.getLoc();
		tiles = new Tile[40];
		em = new EventManager(gameClient);
		createTiles(gameClient);
		this.gameClient = gameClient;
	}

	public Tile[] getTilesArray() {
		return tiles;
	}

	public void createTiles(GameClient gameClient) {
		Tile t = null;
		ResourceBundle rbTile = ResourceBundle.getBundle(
				"ch.bfh.monopoly.resources.tile", loc);

		for (int i = 0; i < 40; i++) {

			String name = rbTile.getString("tile" + i + "-name");
			name = name.trim();

			String toParse = rbTile.getString("tile" + i + "-price");
			int price = parseResource(toParse);

			toParse = rbTile.getString("tile" + i + "-houseCost");
			int houseCost = parseResource(toParse);

			toParse = rbTile.getString("tile" + i + "-hotelCost");
			int hotelCost = parseResource(toParse);

			toParse = rbTile.getString("tile" + i + "-rent");
			int rent = parseResource(toParse);

			toParse = rbTile.getString("tile" + i + "-rent1house");
			int rent1house = parseResource(toParse);

			toParse = rbTile.getString("tile" + i + "-rent2house");
			int rent2house = parseResource(toParse);

			toParse = rbTile.getString("tile" + i + "-rent3house");
			int rent3house = parseResource(toParse);

			toParse = rbTile.getString("tile" + i + "-rent4house");
			int rent4house = parseResource(toParse);

			toParse = rbTile.getString("tile" + i + "-renthotel");
			int renthotel = parseResource(toParse);

			String group = rbTile.getString("tile" + i + "-group");
			group = group.trim();

			toParse = rbTile.getString("tile" + i + "-mortgageValue");
			int mortgageValue = parseResource(toParse);

			toParse = rbTile.getString("tile" + i + "-coordX");
			int coordX = parseResource(toParse);

			toParse = rbTile.getString("tile" + i + "-coordY");
			int coordY = parseResource(toParse);

			String rgb = rbTile.getString("tile" + i + "-rgb");

			rgb = rgb.trim();

			if (group.equals("railroad"))
				t = new Railroad(name, price, rent, group, mortgageValue,
						coordX, coordY, i, em, gameClient.getBankPlayer(),
						gameClient, rbTile);
			else if (group.equals("utility"))
				t = new Utility(name, price, group, mortgageValue, coordX,
						coordY, i, em, gameClient.getBankPlayer(), gameClient,
						rbTile);
			else if (group.equalsIgnoreCase("Chance"))
				t = new Chance(name, coordX, coordY, i, gameClient, em);
			else if (group.equalsIgnoreCase("Community Chest"))
				t = new CommunityChest(name, coordX, coordY, i, gameClient, em);
			else if (i==0)
				t = new Go(name, coordX, coordY, i, em, gameClient);
			else if (i==10)
				t = new Jail(name, coordX, coordY, i, em, gameClient);
			else if (i==20)
				t = new FreeParking(name, coordX, coordY, i, em, gameClient);
			else if (i==30)
				t = new GoToJail(name, coordX, coordY, i, em, gameClient);
			else if (i==4 || i==38)
				t = new NonProperty(name, coordX, coordY, i, em, gameClient);
			else
				t = new Terrain(name, price, houseCost, hotelCost, rent,
						rent1house, rent2house, rent3house, rent4house,
						renthotel, group, mortgageValue, coordX, coordY, i,
						rgb, em, gameClient.getBankPlayer(), gameClient, rbTile);
			tiles[i] = t;
		}
	}

	public int parseResource(String str) {
		int value;
		str = str.trim();
		if (str.equals("NULL"))
			value = -1;
		else {
			value = Integer.parseInt(str);
		}
		return value;
	}

}
