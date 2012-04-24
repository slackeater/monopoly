package ch.bfh.monopoly.common;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.mina.core.session.IoSession;

import ch.bfh.monopoly.net.ClientHandler;
import ch.bfh.monopoly.net.Network;
import ch.bfh.monopoly.net.ServerHandler;


public class NetworkController {

	private Network n;
	private ServerHandler srvHandler;
	
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
		this.srvHandler = new ServerHandler();
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
	public IoSession startClient(String ip, int port, ClientHandler cliHandler) throws UnknownHostException, IOException{
		return n.startClient(ip, port, cliHandler);
	}
	
	/**
	 * Get the number of sessions opened after calling startServer
	 * @return
	 */
	public int getServerOpenedSession() {
		int sessions = this.srvHandler.getOpenedSessions();
		return sessions;
		
	}
}
