package ch.bfh.monopoly.networkJustin;
import java.io.IOException;






public class NetworkClientRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub


		String IP_AT_HOME = "192.168.1.4";
		String IP_AT_SCHOOL = "147.87.123.234";

		Network connection = new Network();
		connection.startClient(IP_AT_SCHOOL, 1234);
//		NetworkClient connection = new NetworkClient("192.168.1.8", 1235);
//		connection.openConnection();
//		connection.start();

	}

}
