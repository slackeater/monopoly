package ch.bfh.monopoly.common;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.mina.core.session.IoSession;

import ch.bfh.monopoly.net.ClientHandler;
import ch.bfh.monopoly.net.NetMessage;
import ch.bfh.monopoly.net.Network;
import ch.bfh.monopoly.net.ServerHandler;


public class NetworkController {

	private Network n;
	private ServerHandler srvHandler = new ServerHandler();
	
	private ClientHandler cliHandler = new ClientHandler();
	
	/**
	 * Construct a network controller
	 */
	public NetworkController(){
		this.n = new Network();
	}
	
	/**
	 * Start the server
	 * @param ip the ip to listen for incoming connections
	 * @param port the port associated with the IP
	 * @param players the number of players
	 * @throws IOException When the fields are not correctly filled
	 */
	public void startServer(String ip, int port) throws IOException{
		n.startServer(ip, port, srvHandler);
	}
	
	/**
	 * Start a client and connect it to the server
	 * @param ip the IP of the server
	 * @param port the port of the server
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @return an IoSession used to communicate with the server
	 */
	public IoSession startClient(String ip, int port) throws UnknownHostException, IOException{
		
		return n.startClient(ip, port, this.cliHandler);
	}
	
	/**
	 * Check if a session from the client to the server is opened
	 * @return a boolean value, true if the session is opened
	 */
	public boolean gameCanBegin(){
		return cliHandler.gameCanBegin();
	}
	
	/**
	 * Get the number of sessions opened after calling startServer
	 * @return the number of sessions opened on this server
	 */
	public int getServerOpenedSession() {
		return srvHandler.getOpenedSessions();
	}
	
	/**
	 * Send a broadcast to the connected client
	 */
	public void sendBroadcast(NetMessage n){
		srvHandler.sendBroadcast(n,null);
	}
	
}
