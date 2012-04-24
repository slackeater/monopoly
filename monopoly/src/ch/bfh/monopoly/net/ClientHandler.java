package ch.bfh.monopoly.net;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * This class is used to handle the operations of the client
 * over the network
 * @author snake
 *
 */
public class ClientHandler implements IoHandler {

	private boolean sessionOpened = false;
	
	@Override
	public void exceptionCaught(IoSession arg0, Throwable arg1)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	public boolean isSessionOpened(){
		return sessionOpened;
	}
	
	@Override
	public void messageReceived(IoSession arg0, Object arg1) throws Exception {
		NetMessage n = (NetMessage)arg1;
		
		System.out.println("<====== RCVD MESSAGE: " + n.getMessageCode());
	}

	@Override
	public void messageSent(IoSession arg0, Object arg1) throws Exception {

		NetMessage n = (NetMessage)arg1;
		
		System.out.println("======> SENT MESSAGE: " + n.getMessageCode());
	}

	@Override
	public void sessionClosed(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionCreated(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionOpened(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("session opened with the server");
		this.sessionOpened = true;
		
	}

}
