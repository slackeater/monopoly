package ch.bfh.monopoly.net;


import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;


public class TestClient {

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
	
		IoConnector ic = new NioSocketConnector();
		ic.getFilterChain().addLast("logger", new LoggingFilter());
		ic.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		ic.setHandler(new ClientHandler());

		ConnectFuture cf = ic.connect(new InetSocketAddress("192.168.2.4", 1234));
		//ConnectFuture cf = ic.connect(new InetSocketAddress("147.87.123.220", 1234));

		IoSession io = null;
		
		//Thread.sleep(7000);
		System.out.println("ciao");
		try {
			cf.awaitUninterruptibly();
			io = cf.getSession();
			
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		NetMessage one = new NetMessage(Messages.AUCTION);
		NetMessage two = new NetMessage(Messages.BUY_HOTEL);
		NetMessage three = new NetMessage(Messages.BUY_HOUSE);
		NetMessage four = new NetMessage(Messages.BUY_HOUSEROW);
		NetMessage five = new NetMessage(Messages.SELL_CARD);

		
		
		io.write(one);
		Thread.sleep(2000);
		io.write(two);
		Thread.sleep(2000);
		io.write(three);
		Thread.sleep(2000);
		io.write(four);
		Thread.sleep(2000);
		io.write(five);
		Thread.sleep(2000);
		
//		try {
//			n.startClient("147.87.123.220", 1234);
//		} catch (UnknownHostException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//
//		n.addMsg(one);
//		Thread.sleep(5000);
//		n.addMsg(two);
//		Thread.sleep(5000);
//		n.addMsg(three);
//		Thread.sleep(5000);
//		n.addMsg(four);
//		Thread.sleep(5000);
//		n.addMsg(five);
		
		
		System.out.println("I'm sleeping for 30 seconds");
		try {
			Thread.sleep(10000);
			System.out.println("Ehi I wake up!!");
			io.write(one);
			Thread.sleep(2000);
			io.write(two);
			Thread.sleep(2000);
			io.write(three);
			Thread.sleep(2000);
			io.write(four);
			Thread.sleep(2000);
			io.write(five);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
