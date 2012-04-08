package ch.bfh.monopoly.network;

import java.io.IOException;

public class TestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Network n = new Network();
	
		NetMessage one = new NetMessage(Messages.AUCTION);
		NetMessage two = new NetMessage(Messages.BUY_HOTEL);
		NetMessage three = new NetMessage(Messages.BUY_HOUSE);
		NetMessage four = new NetMessage(Messages.BUY_HOUSEROW);
		NetMessage five = new NetMessage(Messages.SELL_CARD);
				
		n.startClient("192.168.1.2", 1234);
		
		n.addMsg(one);
		n.addMsg(two);
		n.addMsg(three);
		n.addMsg(four);
		n.addMsg(five);
		
		
		System.out.println("I'm sleeping for 30 seconds");
		try {
			Thread.sleep(30000);
			System.out.println("Ehi I wake up!!");
			Thread.sleep(5000);
			n.addMsg(one);
			n.addMsg(two);
			n.addMsg(three);
			n.addMsg(four);
			n.addMsg(five);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
