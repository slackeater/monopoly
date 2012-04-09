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

import javax.swing.JComponent;
import javax.swing.JTextArea;


public class Network {

	private ServerSocketChannel srv;
	NetworkDaemonServer[] clients;
	private NetworkDaemonClient n;

	//very ugly
	private JTextArea cmp;

	public void setNotificationArea(JTextArea cmp){
		this.cmp = cmp;
	}

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
		clients = new NetworkDaemonServer[maxPlayers];

		srv = ServerSocketChannel.open();
		srv.socket().bind(new InetSocketAddress(ip,port));
		srv.configureBlocking(false);

		//fill the array of client threads
		while(ctr < maxPlayers){
			cmp.append("Waiting for clients...\n");
			
			
			clientConnection = srv.accept();

			if(clientConnection != null){
				clients[ctr] = new NetworkDaemonServer(clientConnection.socket(), ctr+1);
				clients[ctr].start();
				ctr++;

				cmp.append("One client connected\n");

			}
			else{
				try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				cmp.append("....\n");
			}
			
			cmp.repaint();
			cmp.validate();
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
	 */
	public void startClient(String ip, int port){
		try {
			Socket s = new Socket(ip, port);

			//start a thread for the client
			n = new NetworkDaemonClient(s, 1);
			n.start();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
