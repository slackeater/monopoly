package ch.bfh.monopoly.network;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Network {

	private ServerSocketChannel srv;
	private NetworkDaemonServer[] serverSideThread;
	private NetworkDaemonClient n;

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
		serverSideThread = new NetworkDaemonServer[maxPlayers];

		srv = ServerSocketChannel.open();
		srv.socket().bind(new InetSocketAddress(ip,port));
		srv.configureBlocking(false);

		//fill the array of client threads
		while(ctr < maxPlayers){
			System.out.println("Waiting for clients...");
			
			clientConnection = srv.accept();

			if(clientConnection != null){
				serverSideThread[ctr] = new NetworkDaemonServer(clientConnection.socket(), ctr+1, serverSideThread);
				serverSideThread[ctr].start();
				ctr++;

				System.out.println("One client connected");

			}
			else{
				try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				System.out.println("....");
			}
			
		}
	}

	/**
	 * Stop the previously started server
	 */
	public void stopServer(){
		try {
			NetworkDaemonServer.connectionOpen = false;
			this.srv.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * PROBABLY ONLY FOR TEST
	 * Add a message to the queue of this client
	 * @param m
	 */
	public void addMsg(NetMessage m){
		n.addMsg(m);
	}


	/**
	 * Start a client
	 * @param ip the ip of the server
	 * @param port the port of the server
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public void startClient(String ip, int port) throws UnknownHostException, IOException{
			Socket s = new Socket(ip, port);

			//start a thread for the client
			n = new NetworkDaemonClient(s, 1);
			n.start();
	}


}
