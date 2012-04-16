package ch.bfh.monopoly.network;

import java.io.IOException;
import java.net.UnknownHostException;

public class TestClient2 {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Network n = new Network();
	
		NetMessage one = new NetMessage(Messages.MORTGAGE);
		NetMessage two = new NetMessage(Messages.UNMORTGAGE);
		NetMessage three = new NetMessage(Messages.KICK_PLAYER);
		NetMessage four = new NetMessage(Messages.KICK_ANSWER);
		NetMessage five = new NetMessage(Messages.QUIT_GAME);
				
		try {
			n.startClient("127.0.0.1", 1234);
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
