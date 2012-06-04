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
	KICK_REQUEST(150),
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
	
	TRANSFER_JAILCARD(350),
	TRANSFER_MONEY(360),
	TRANSFER_PROPERTY(370),
	
	TOGGLE_MORTGAGE(400),
	
	PAY_RENT(500),
	TURN_TOKEN(510),
	START_TURN_PANEL(525),
	END_TURN(520),
	PAY_FEE(530),
	PAY_UTILITY_FEE(535),
	FREE_PARKING(550),
	BIRTHDAY_EVENT(555),
	WIN_JAIL_CARD(560),
	
	//when chance cards must be shuffled
	UPDATE_CHANCE_ORDER(600),
	UPDATE_COMMCHEST_ORDER(610),
	//WHEN GUI CALLS FOR THE EVENT WINDOW
	GET_EVENT_WINDOW(620),
	GO_TO_JAIL(666),
	GET_OUT_OF_JAIL_ROLL(667),
	GET_OUT_OF_JAIL_PAY(668),
	GET_OUT_OF_JAIL_USECARD(669),
	
	TRADE_REQUEST(700),
	TRADE_ANSWER(701),
	PERFORM_TRADE(705),
	
	
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
