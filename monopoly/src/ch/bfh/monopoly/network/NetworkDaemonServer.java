package ch.bfh.monopoly.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class NetworkDaemonServer extends Thread{

	private Socket sock;
	private int clientID;
	static public boolean connectionOpen = true;
	private ObjectInputStream in;
	private ObjectOutputStream out;


	public NetworkDaemonServer(Socket sock, int clientID){
		this.sock = sock;
		this.clientID = clientID;
	}


	@Override
	public void run() {
		System.out.println("THREAD FOR THE CLIENT " + clientID);
		System.out.println("FROM PORT: " + sock.getPort());

		try {
			in = new ObjectInputStream(sock.getInputStream());
			out = new ObjectOutputStream(sock.getOutputStream());
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		while(connectionOpen){
			try {
				NetMessage n = (NetMessage) in.readObject();
				System.out.println(n.getMessageCode());
							
				NetMessage ack = new NetMessage(Messages.ACKNOWLEDGE);
				
				out.writeObject(ack);
				out.flush();
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
