package ch.bfh.monopoly.common;

import java.io.IOException;
import java.net.UnknownHostException;

import ch.bfh.monopoly.network.Messages;
import ch.bfh.monopoly.network.NetMessage;
import ch.bfh.monopoly.network.Network;


public class NetworkController {

	private Network n;
	
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
	public void startServer(String ip, int port, int players) throws IOException{
		n.startServer(ip, port, players);
	}
	
	/**
	 * Start a client and connect it to the server
	 * @param ip the IP of the server
	 * @param port the port of the server
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public void startClient(String ip, int port) throws UnknownHostException, IOException{
		n.startClient(ip, port);
	}
	
	/**
	 * Add a message to the thread queue. This will be sent to the server
	 * @param msg the NetMessage to send
	 */
	public void enqueueMessage(NetMessage msg){
		n.addMsg(msg);
	}
	
	
	/**
	 * Send a chat message
	 * @param p the player who sent the message 
	 * @param s the message
	 */
	public void sendChatMessage(Player p, String s){
		n.addMsg(new NetMessage(p, s, Messages.CHAT_MSG));
	}
	
}
