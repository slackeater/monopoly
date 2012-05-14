package ch.bfh.monopoly.net;

import java.awt.Color;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.GameController;

/**
 * This class is used to handle the operations of the client
 * over the network
 * @author snake
 *
 */
public class ClientHandler implements IoHandler {

	private boolean gameCanBegin = false;
	private GameClient gc;
	private String localPlayerName;
	
	public ClientHandler(GameClient gc, String localPlayerName){
		this.gc = gc;
		this.localPlayerName = localPlayerName;
	}

	@Override
	public void exceptionCaught(IoSession arg0, Throwable arg1)
	throws Exception {
		// TODO Auto-generated method stub
	}

	/**
	 * Check wehter the game can begin or not
	 * @return a boolean , true if the game can begin, false otherwise
	 */
	public boolean gameCanBegin(){
		return gameCanBegin;
	}

	@Override
	public void messageReceived(IoSession arg0, Object arg1) throws Exception {
		NetMessage n = (NetMessage)arg1;

		System.out.println(n.getMessageType());

		switch(n.getMessageType()){
			case GAME_START:
				//create the board for this client by passing the locale the users and the name
				//of the locaplayer
				gc.createBoard(n.getLocale(), n.getUserNameList(), this.localPlayerName);
							
				//TODO only for test
				System.out.println("Client list:" + n.getUserNameList().size());
				System.out.println("User size: " + n.getUserNameList());
	
				gameCanBegin = true;
				break;
			case CHAT_MSG:
				gc.displayChat(n.getText());
				break;
			case DICE_ROLL:
				int rollValue = n.getInt();
				//gc.roll(rollValue, c);
				break;
		}


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
		System.out.println("session opened");

		//as first, send the username
		NetMessage n = new NetMessage(this.localPlayerName, Messages.SEND_USERNAME);
		arg0.write(n);
	}

}
