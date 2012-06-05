package ch.bfh.monopoly.observer;


	/**
	 * Enumerates the code messages for Monopoly
	 *
	 */
	public enum WindowMessage {
	
		MSG_FOR_CHAT(0),
		MSG_FOR_ERROR(1),
		MSG_EVENT_COMPLETION(2),
		MSG_TRADE_REQUEST(3),
		MSG_TRADE_ANSWER(4), 
		MSG_FOR_EVENT_INFO(5),
		MSG_KICK_REQUEST(10),
		MSG_KICK_ANSWER(11),
		MSG_KICK(12);

		
		;
		
		//The code of the message
		private int code;
		
		/**
		 * Construct a Message
		 * @param c the code for the message
		 */
		private WindowMessage(int c){
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

