package ch.bfh.monopoly.net;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import ch.bfh.monopoly.common.GameClient;

/**
 * This class is used to handle the operations of the client over the network
 * 
 * @author snake
 * 
 */
public class ClientHandler implements IoHandler {

	private boolean gameCanBegin = false;
	private GameClient gc;
	private String localPlayerName;
	private int rollOrderValue;

	public ClientHandler(GameClient gc, String localPlayerName,
			int rollOrderValue) {
		this.gc = gc;
		this.localPlayerName = localPlayerName;
		this.rollOrderValue = rollOrderValue;
	}

	@Override
	public void exceptionCaught(IoSession arg0, Throwable arg1)
			throws Exception {
	}

	/**
	 * Check wehter the game can begin or not
	 * 
	 * @return a boolean , true if the game can begin, false otherwise
	 */
	public boolean gameCanBegin() {
		return gameCanBegin;
	}

	@Override
	public void messageReceived(IoSession arg0, Object arg1) throws Exception {
		NetMessage n = (NetMessage) arg1;

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
				//TODO dice roll function
				System.out.println("ROLL RECEIVED");
				int rollValue = n.getInt();
				gc.advanceCurrentPlayerNSpaces(rollValue, false);
				break;
			case TURN_TOKEN:
				String username = n.getText();
				
				//if the name is equal to the local player, enable buttons and set current player
				if(username.equals(localPlayerName)){
					//TODO
					gc.setCurrentPlayer(localPlayerName, false);
					gc.updateTurnTokens(localPlayerName);

					//function for enabling buttons in the GUI
						System.out.println("IT'S MY TURN : " + username);
				}
				//else set only the current player
				else{
					gc.setCurrentPlayer(username, false);
					//TODO 
					//function for setting the currentPlayer
						System.out.println("IT'S NOT MY TURN :" + localPlayerName);
				}
				
				break;
		}
	}

	@Override
	public void messageSent(IoSession arg0, Object arg1) throws Exception {
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

		// as first, send the username and the roll value
		NetMessage n = new NetMessage(this.localPlayerName,
				this.rollOrderValue, Messages.SEND_USERNAME);
		arg0.write(n);
	}

}
