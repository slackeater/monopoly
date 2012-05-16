package ch.bfh.monopoly.common;

public class Dice {

	int maxValDiceOne;
	int maxValDiceTwo;
	int throwValueOne = 0;
	int throwValueTwo = 0;
	
	/**
	 * Construct a dice object
	 * @param maxValDiceOne the maximal value for the die one
	 * @param maxValDiceTwo the maximal value for the die two
	 */
	public Dice(int maxValDiceOne, int maxValDiceTwo){
		this.maxValDiceOne = maxValDiceOne;
		this.maxValDiceTwo = maxValDiceTwo;
	}
	
	/**
	 * Throw the dice 
	 * @return int
	 * 			an integer representing the throw of the two dice
	 */
	public int throwDice(){
		//generates number between 1 and 12
		//the one is added because Math.random() generates
		//number between 0.0 and 1.0. But 1.0 is not included.
		//so in the case where:
		//maxValDice = 6 ; Math.random = 0.99
		//6*0.99 = 5.94 rounded => 5 + 1 = 6
		throwValueOne = (int)(maxValDiceOne*Math.random())+1;
		throwValueTwo = (int)(maxValDiceTwo*Math.random())+1;
		
		return throwValueOne+throwValueTwo;
	}
	
	/**
	 * Get the single value of both dice
	 * @return String
	 * 			a string representing the two values, for example 3, 5
	 */
	public String getDiceValues(){
		return throwValueOne + ", " + throwValueTwo;
	}
}
