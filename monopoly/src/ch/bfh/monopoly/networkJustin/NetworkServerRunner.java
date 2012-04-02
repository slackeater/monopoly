package ch.bfh.monopoly.networkJustin;
import java.io.IOException;




public class NetworkServerRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Network communicate = new Network();

		System.out.println("Starting the server...");

		try {
			communicate.startServer("192.168.1.8", 1235, 2);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

	}

}
