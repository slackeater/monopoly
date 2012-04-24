package ch.bfh.monopoly.network;

import java.io.IOException;
import java.net.UnknownHostException;

import ch.bfh.monopoly.net.Messages;
import ch.bfh.monopoly.net.NetMessage;

public class TestClient {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Network n = new Network();

		NetMessage one = new NetMessage(Messages.AUCTION);
		NetMessage two = new NetMessage(Messages.BUY_HOTEL);
		NetMessage three = new NetMessage(Messages.BUY_HOUSE);
		NetMessage four = new NetMessage(Messages.BUY_HOUSEROW);
		NetMessage five = new NetMessage(Messages.SELL_CARD);
				
		try {
			n.startClient("147.87.123.220", 1234);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		n.addMsg(one);
		Thread.sleep(5000);
		n.addMsg(two);
		Thread.sleep(5000);
		n.addMsg(three);
		Thread.sleep(5000);
		n.addMsg(four);
		Thread.sleep(5000);
		n.addMsg(five);
		
		
		System.out.println("I'm sleeping for 30 seconds");
		try {
			Thread.sleep(30000);
			System.out.println("Ehi I wake up!!");
			n.addMsg(one);
			Thread.sleep(5000);
			n.addMsg(two);
			Thread.sleep(5000);
			n.addMsg(three);
			Thread.sleep(5000);
			n.addMsg(four);
			Thread.sleep(5000);
			n.addMsg(five);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
