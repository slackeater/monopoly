package ch.bfh.monopoly.exception;

public class TransactionException extends Exception{

		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
		String errorMsg;
		
		public TransactionException(String errorMsg){
			this.errorMsg = errorMsg;
		}
		
		public String getErrorMsg(){
			return errorMsg;
		}

}

