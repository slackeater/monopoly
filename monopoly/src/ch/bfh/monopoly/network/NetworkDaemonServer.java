package ch.bfh.monopoly.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ch.bfh.monopoly.net.Messages;
import ch.bfh.monopoly.net.NetMessage;

public class NetworkDaemonServer extends Thread{

	private Socket sock;
	private int clientID;
	static public boolean connectionOpen = true;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	//used to communicate with the other server side thread 
	//for example when we want to send a broadcast
	private NetworkDaemonServer[] srvThread;

	/**
	 * Construct a thread that will act as a server for the client
	 * @param sock the socket used to exchange information with the player
	 * @param clientID the ID of this thread on the server
	 */
	public NetworkDaemonServer(Socket sock, int clientID, NetworkDaemonServer[] srvThread){
		this.sock = sock;
		this.clientID = clientID;
		this.srvThread = srvThread;
	}

	/**
	 * Get the ID of this thread
	 * @return an int representing the id
	 */
	private int getID(){
		return this.clientID;
	}
	
	/**
	 * Get the socket of this thread
	 * @return a Socket representing the connection with the client
	 */
	private Socket getSocket(){
		return this.sock;
	}

	private synchronized void sendMessage(NetMessage n) throws IOException{
		out.writeObject(n);
		out.flush();
	}
	
	@Override
	public void run() {
		System.out.println("THREAD FOR THE CLIENT " + clientID);
		System.out.println("FROM PORT: " + sock.getPort());

		try {
			in = new ObjectInputStream(this.getSocket().getInputStream());
			out = new ObjectOutputStream(this.getSocket().getOutputStream());
		}
		catch (IOException e1) {e1.printStackTrace(); }
		
		while(connectionOpen){
			try {
				//read an incoming message
				NetMessage n = (NetMessage) in.readObject();
				
				System.out.println("From client: " + clientID + " has sent " + n.getMessageCode());
						
				//send an acknowledge to the client
				NetMessage ack = new NetMessage(Messages.ACKNOWLEDGE);

				//send a message of acknowledgment
				sendMessage(ack);
				
				//now update all the client except the one that has sent the message
				sendBroadcast(n);
				
			}
			catch (IOException e) { connectionOpen = false; e.printStackTrace(); }
			catch (ClassNotFoundException e) { connectionOpen = false; e.printStackTrace(); }
		}
		
		try {
			in.close();
			out.close();
		} catch (IOException e) {e.printStackTrace(); }
	}
	
	/**
	 * Send a broadcast to the other client
	 * @param n
	 */
	private void sendBroadcast(NetMessage n){
		for(int j = 0 ; j < srvThread.length ; j++){
			if(srvThread[j] != null && srvThread[j].getID() != this.clientID){
				try {
					Socket s = srvThread[j].getSocket();
					
					System.out.println("=== BROADCAST TO: " + s.getInetAddress());
					System.out.println("=== NETMESSAGE: " + n.getMessageCode());
					
					srvThread[j].sendMessage(n);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
}
