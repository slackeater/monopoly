package ch.bfh.monopoly.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class NetworkDaemonClient extends Thread{

	private Socket sock;
	int clientID;
	static public boolean connectionOpen = true;
	ObjectInputStream in;
	ObjectOutputStream out;
	private List<NetMessage> queue = new ArrayList<NetMessage>(); 

	public NetworkDaemonClient(Socket sock, int clientID){
		this.sock = sock;
		this.clientID = clientID;
	}

	public void addMsg(NetMessage n){
		this.queue.add(n);
	}


	@Override
	public void run() {
		int msgCounter = 0;
		System.out.println("I'M THE CLIENT THREAD");
		try {
			out = new ObjectOutputStream(sock.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while(connectionOpen){
			if(this.queue.size() > 1){
				try {
					//send a BUY_HOTEL message


					out.writeObject(queue.get(msgCounter));
					queue.remove(msgCounter);
					out.flush();

					System.out.println("WE are sending a message: " + msgCounter);
					sleep(7500);
					//msgCounter++;

					//out.close();

					/*in = new ObjectInputStream(sock.getInputStream());

				NetMessage n = (NetMessage) in.readObject();
				System.out.println(n.toString());*/
					//in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				System.out.println("I'm empty like a bottle.");
			}

		}
	}

}
