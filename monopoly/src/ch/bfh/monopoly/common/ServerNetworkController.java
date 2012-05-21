package ch.bfh.monopoly.common;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ch.bfh.monopoly.net.ServerHandler;
import ch.bfh.monopoly.net.ServerNetwork;

public class ServerNetworkController {

	private ServerNetwork srvNet;
	private ServerHandler srvHandler;
	
	/**
	 * Consturct a new ServerNetworkController
	 */
	public ServerNetworkController(){
		this.srvHandler = new ServerHandler();
		this.srvNet = new ServerNetwork(srvHandler);
	}
	
	/**
	 * Start the server
	 * @param ip the ip to listen for incoming connections
	 * @param port the port associated with the IP
	 * @param players the number of players
	 * @throws IOException When the fields are not correctly filled
	 */
	public void startServer(String ip, int port) throws IOException{
		srvNet.startServer(ip, port);
	}
	
	/**
	 * Get the number of sessions opened after calling startServer
	 * @return the number of sessions opened on this server
	 */
	public int getServerOpenedSession() {
		return srvHandler.getOpenedSessions();
	}
	
	/**
	 * Stop the server
	 */
	public void stopServer(){
		srvNet.stopServer();
	}
	
	/**
	 * Send the turn token to the next player
	 */
	public void sendTurnToken(){
		srvHandler.sendTurnToken();
	}
	
	/**
	 * Get the server username
	 * @return List
	 * 		the ordered list of player's user names
	 */
	public List<String> getServerUsernames(){
		return srvHandler.getUsernames();
	}
	
	/**
	 * Send a start game message
	 * @param loc Locale
	 * 			the locale chosen for this game
	 */
	public void sendStartGame(Locale loc){
		srvHandler.sendStartGame(loc);
	}
	
	
}
