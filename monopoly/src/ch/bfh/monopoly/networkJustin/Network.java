package ch.bfh.monopoly.networkJustin;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Network {

	private ServerSocket srv;
	private Socket client;
	private NetworkClientThread netClient;
	final int QUEUE_LENGTH = 20;
	Socket sock;
	
	public static boolean serverRunning = true;
	int maxPlayers;
	ArrayList<Thread> connections;

	public Network() {
		connections = new ArrayList<Thread>();

	}
	
	public void startClient(String ip, int port){
		
		
			client=null;
			try {
				client = new Socket(ip, port);
			} catch (UnknownHostException e) {
				System.out.println(e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e);
			}
			if (client==null) throw new RuntimeException("Socket creation failed");
			netClient = new NetworkClientThread(client);
			netClient.start();
		
	}

	/**
	 * Start the server
	 * 
	 * @param dottedIP
	 *            the IP to listen on
	 * @param port
	 *            the port to listen on
	 * @throws IOException
	 */
	public void startServer(String dottedIP, int port, int maxPlayers)
			throws IOException {

		this.maxPlayers = maxPlayers;
		InetAddress ip = Inet4Address.getByName(dottedIP);
		this.srv = new ServerSocket(port, QUEUE_LENGTH, ip);

		// communicateNoThread(sock);
		communicateWithThread();
	}

	public void communicateWithThread() {
		while (serverRunning) {
			try {
				if (connections.size() <= maxPlayers) {
					sock = this.srv.accept();
					System.out.println("Accepted client");
					Thread t = new NetworkServerThread(sock, 1);
					connections.add(t);
					t.start();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// remove any threads if the sockets are dead
			for (Thread t : connections) {
				if (!((NetworkServerThread) t).connectionIsAlive()) {
					//t.interrupt();
					connections.remove(t);
				}

			}
		}
	}

	public void communicateNoThread(Socket sock) {

		OutputStream os;
		try {
			sock = this.srv.accept();
			System.out.println("Accepted client");
			os = sock.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(new String("Hello There"));

			oos.close();
			os.close();
			sock.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
