package ch.bfh.monopoly.common;

import java.io.IOException;
import java.net.UnknownHostException;


import ch.bfh.monopoly.net.ClientHandler;
import ch.bfh.monopoly.net.ClientNetwork;
import ch.bfh.monopoly.net.NetMessage;

public class ClientNetworkController {

	private ClientNetwork net;
	private ClientHandler cliHandler;
	
	/**
	 * Construct a new NetworkClientController
	 * @param gc GameClient
	 * 			the game client to use when we receive a message
	 * @param localPlayerName String
	 * 			the name of the local player
	 * @param rollOrderValue int
	 * 			the value of the roll for order
	 */
	public ClientNetworkController(GameClient gc, String localPlayerName, int rollOrderValue){
		this.cliHandler = new ClientHandler(gc, localPlayerName, rollOrderValue);
		this.net = new ClientNetwork(cliHandler);
	}

	
	/**
	 * Start a client and connect it to the server
	 * @param ip the IP of the server
	 * @param port the port of the server
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @return an IoSession used to communicate with the server
	 */
	public void startClient(String ip, int port){
		 net.startClient(ip, port);
	}
	
	/**
	 * Check if a session from the client to the server is opened
	 * @return a boolean value, true if the session is opened
	 */
	public boolean gameCanBegin(){
		return cliHandler.gameCanBegin();
	}
	
	/**
	 * Close the currently opened session with the server
	 */
	public void closeConnection(){
		net.closeConnection();
	}
	
	/**
	 * Send a message to the server
	 * @param nm NetMessage
	 * 		the Netmessage to send to the server
	 */
	public void sendMessage(NetMessage nm){
		net.sendMessage(nm);
	}
	
	
	
	
	
}
