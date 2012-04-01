package ch.bfh.monopoly.common;

import java.awt.Color;
import java.net.Socket;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JMenu;

import ch.bfh.monopoly.tile.*;

public class Board {
	Player[] players;
	public Tile[] tiles;
	private Locale loc;

	private Player me;
	private Player currentPlayer;

	public Board(Locale loc, GameClient gameClient) {
		// create tiles, cards, and events
		this.loc = loc;

		TileCreator tc = new TileCreator(loc, gameClient);
		tiles = tc.getTilesArray();
	}
	
	public TileInfo getTileInfoByID(int id){
		TileInfo tileInfo = new TileInfo();
		Tile tile = tiles[id];
		if (tile instanceof Terrain){
			Terrain t = (Terrain)tile;
			tileInfo.setName(t.getName());
			tileInfo.setPrice( t.getPrice());
			tileInfo.setHouseCost( t.getHouseCost());
			tileInfo.setHotelCost( t.getHotelCost());
			tileInfo.setRent( t.getRentByHouseCount(0));
			tileInfo.setRent1house( t.getRentByHouseCount(1));
			tileInfo.setRent2house( t.getRentByHouseCount(2));
			tileInfo.setRent3house( t.getRentByHouseCount(3));
			tileInfo.setRent4house( t.getRentByHouseCount(4));
			tileInfo.setRenthotel( t.getRentHotel());
			tileInfo.setGroup( t.getGroup());
			tileInfo.setMortgageValue(t.getMortgageValue());
			tileInfo.setCoordX(t.getCoordX());
			tileInfo.setCoordY( t.getCoordY());
		}
		if (tile instanceof Railroad){
			Railroad t = (Railroad)tile;
			tileInfo.setName(t.getName());
			tileInfo.setPrice( t.getPrice());

			//returning RENT info for Railroads rrequires changing how
			//the rent is calculated in Railroad
			tileInfo.setGroup( t.getGroup());
			tileInfo.setMortgageValue(t.getMortgageValue());
			tileInfo.setCoordX(t.getCoordX());
			tileInfo.setCoordY( t.getCoordY());
		}
		if (tile instanceof Utility){
			Utility t = (Utility)tile;
			tileInfo.setName(t.getName());
			tileInfo.setPrice( t.getPrice());

			tileInfo.setGroup( t.getGroup());
			tileInfo.setMortgageValue(t.getMortgageValue());
			tileInfo.setCoordX(t.getCoordX());
			tileInfo.setCoordY( t.getCoordY());
		}
		if (tile instanceof CommunityChest){
			CommunityChest t = (CommunityChest)tile;
			tileInfo.setName(t.getName());
	
			tileInfo.setCoordX(t.getCoordX());
			tileInfo.setCoordY( t.getCoordY());
		}
		if (tile instanceof Chance){
			Chance t = (Chance)tile;
			tileInfo.setName(t.getName());

			tileInfo.setCoordX(t.getCoordX());
			tileInfo.setCoordY( t.getCoordY());
		}
		if (tile instanceof NonProperty){
			NonProperty t = (NonProperty)tile;
			tileInfo.setName(t.getName());

			tileInfo.setCoordX(t.getCoordX());
			tileInfo.setCoordY( t.getCoordY());
		}
		
		

		return tileInfo;
	}
}
