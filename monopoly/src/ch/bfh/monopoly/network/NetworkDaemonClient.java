package ch.bfh.monopoly.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NetworkDaemonClient extends Thread{

	private Socket sock;
	private int clientID;
	static public boolean connectionOpen = true;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private List<NetMessage> queue = new ArrayList<NetMessage>(); 

	/**
	 * Construct a thread for a client
	 * @param sock the socket for server communication
	 * @param clientID the ID of this thread
	 */
	public NetworkDaemonClient(Socket sock, int clientID){
		this.sock = sock;
		this.clientID = clientID;
	}

	/**
	 * Add a message to the queue of this thread
	 * that will be sent to the server
	 * @param n
	 */
	public void addMsg(NetMessage n){
		this.queue.add(n);
	}


	@Override
	public void run() {
		System.out.println("I'M THE CLIENT THREAD " + clientID + " port " + sock.getLocalPort());
		try {
			out = new ObjectOutputStream(sock.getOutputStream());
			in = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while(connectionOpen){
			
				//if there are messages in the queue, send it to the server
				if(this.queue.size() > 1){
					try {
						System.out.println("Queue size " + queue.size());
						
						out.writeObject(queue.get(0));
						out.flush();

						System.out.println("We are sending a message: " + queue.get(0).getMessageCode());
						
						queue.remove(0);
						
						NetMessage n = (NetMessage) in.readObject();

						if(Messages.ACKNOWLEDGE == n.getMessageType()){
							System.out.println(" ========= Acknowledge ======== : : " + Messages.ACKNOWLEDGE.getInt() + " (999 indicates acknowledge)");
						}
						else{
							System.out.println(" ========= Broadcast ========== :" + n.getMessageCode());
						}

					} 
					catch (IOException e) { connectionOpen = false; e.printStackTrace(); } 
					catch (ClassNotFoundException e) {e.printStackTrace();}
				}
		}

		try {
			in.close();
			out.close();
		} catch (IOException e) {e.printStackTrace(); }
	}
}
