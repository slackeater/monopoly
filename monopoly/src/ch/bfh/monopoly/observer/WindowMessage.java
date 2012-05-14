package ch.bfh.monopoly.observer;


	/**
	 * Enumerates the code messages for Monopoly
	 *
	 */
	public enum WindowMessage {
	
		MSG_FOR_CHAT(0),
		MSG_FOR_ERROR(1)

		
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
