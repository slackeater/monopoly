package ch.bfh.monopoly.network;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class Network {

	private ServerSocket srv;
	final int QUEUE_LENGTH = 20;
	NetworkThread[] clients;

	/**
	 * Start the server
	 * @param dottedIP the IP to listen on
	 * @param port the port to listen on
	 * @throws IOException 
	 */
	public void startServer(String dottedIP, int port, int maxPlayers) throws IOException{
		InetAddress ip;
		Socket clientConnection;
		int ctr = 0;

		ip = Inet4Address.getByName(dottedIP);
		clients = new NetworkThread[maxPlayers];

		this.srv = new ServerSocket(port, QUEUE_LENGTH, ip);

		//fill the array of client threads
		while(ctr < maxPlayers){
			//accept is blocking
			clientConnection = this.srv.accept();
			System.out.println("Accepted client, now starting thread.");

			clients[ctr] = new NetworkThread(clientConnection, ctr+1);
			clients[ctr].start();
			ctr++;
		}
	}

	/**
	 * Stop the previously started server
	 */
	public void stopServer(){
		try {
			NetworkThread.connectionOpen = false;
			this.srv.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
