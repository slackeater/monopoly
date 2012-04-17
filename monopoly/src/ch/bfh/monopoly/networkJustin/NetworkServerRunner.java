package ch.bfh.monopoly.networkJustin;
import java.io.IOException;




public class NetworkServerRunner {

	/**
	 * @param args
	 */

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String IP_AT_HOME = "192.168.1.4";
		String IP_AT_SCHOOL = "147.87.123.234";

		Network communicate = new Network();

		System.out.println("Starting the server...");

		try {
			communicate.startServer(IP_AT_SCHOOL, 1234, 2);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

	}

}
