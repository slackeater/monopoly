package ch.bfh.monopoly.networkJustin;
import java.io.IOException;






public class NetworkClientRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Network connection = new Network();
		connection.startClient("89.236.128.227", 1235);
//		NetworkClient connection = new NetworkClient("192.168.1.8", 1235);
//		connection.openConnection();
//		connection.start();

	}

}
