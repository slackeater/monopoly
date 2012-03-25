package common;

import java.awt.Color;
import java.net.Socket;
import java.util.ArrayList;

public class Player {

	private String name;
	private int account;
	private ArrayList<Tile> properties;
	//private Color color;
	private int position;
	private Socket socket;
	private boolean isInJail;
	private boolean turnToken;
	private int jailCard;
	
	//start value of money changes with the version of the game played.  US version 5000, Swiss version 200,000
	public Player (String name, Color color, int accout, Socket socket){
		//assumed that 'Go' is position 0
		this.name = name;
		//this.color = color;
		this.account = account;
		position = 0;
		this.socket = socket;
		isInJail = false;
		turnToken = false;
		jailCard = 0;
		
		
	}
	
	
}
