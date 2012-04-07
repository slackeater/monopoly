package ch.bfh.monopoly.network;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;


public class Network {

	private ServerSocketChannel srv;
	NetworkListener[] clients;

	/**
	 * Start the server
	 * @param dottedIP the IP to listen on
	 * @param port the port to listen on
	 * @throws IOException 
	 */
	public void startServer(String dottedIP, int port, int maxPlayers) throws IOException{
		InetAddress ip;
		SocketChannel clientConnection;
		int ctr = 0;

		ip = Inet4Address.getByName(dottedIP);
		clients = new NetworkListener[maxPlayers];

		srv = ServerSocketChannel.open();
		srv.socket().bind(new InetSocketAddress(ip,port));
		
		//fill the array of client threads
		while(ctr < maxPlayers){
			System.out.println("Waiting for players");
			clientConnection = srv.accept();
			//System.out.println("Accepted client, now starting thread.");

			clients[ctr] = new NetworkListener(clientConnection.socket(), ctr+1);
			clients[ctr].start();
			ctr++;
			
			System.out.println("We have " + ctr + " players connected");
		}
	}

	/**
	 * Stop the previously started server
	 */
	public void stopServer(){
		try {
			NetworkListener.connectionOpen = false;
			this.srv.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
