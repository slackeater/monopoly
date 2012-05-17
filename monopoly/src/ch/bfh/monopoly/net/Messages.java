package ch.bfh.monopoly.net;

/**
 * Enumerates the code messages for Monopoly
 * @author snake
 *
 */
public enum Messages {
	DICE_ROLL(100),
	AUCTION(110),
	AUCTION_OFFER(120),
	JOIN_GAME(130),
	PAUSE(140),
	KICK_PLAYER(150),
	KICK_ANSWER(160),
	QUIT_GAME(170),
	CHAT_MSG(180),
	GAME_START(190),
	SEND_USERNAME(195),
	
	BUY_HOUSE(200),
	BUY_HOUSEROW(205),
	BUY_HOTEL(210),
	BUY_HOTELROW(215),
	BUY_PROPERTY(220),
	
	SELL_HOUSE(300),
	SELL_HOUSEROW(305),
	SELL_HOTEL(310),
	SELL_HOTELROW(315),
	SELL_PROPERTY(320),
	SELL_CARD(330),
	
	MORTGAGE(400),
	UNMORTGAGE(410),
	
	PAY_RENT(500),
	TURN_TOKEN(510),
	END_TURN(520),
	
	//probably only for test
	ACKNOWLEDGE(999)
	
	;
	
	//The code of the message
	private int code;
	
	/**
	 * Construct a Message
	 * @param c the code for the message
	 */
	private Messages(int c){
		code = c;
	}
	
	/**
	 * Get the code of the message
	 * @return an integer with the code of this message
	 */
	public int getInt(){
		return code;
	}
	
	
	
	
}
