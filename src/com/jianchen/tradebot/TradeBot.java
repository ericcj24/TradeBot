package com.jianchen.tradebot;
/**
  * TradeBot class
  * 
  *	including:
  * main function: 
  *	public static void main(String...args){}
  *
  * driver function:
  * private void start(){}
  *
  * userInput validity check function:
  * private void checkUserInputValidity(String userInput){}
  *
  * author: Jian Chen
  * June 6, 2014
*/


public class TradeBot{

	private TradeBotHelper helper = new TradeBotHelper();
	private StringBuffer err = new StringBuffer("error: ");
	private boolean errorFlag = false;
	private char side = ' ';
	private int quantity_globe = 0;
	private float price_globe = 0;

	/**
	*   this is the driver function of the program
	*	each loop gets a user input line
	*	keep looping until user input 'quit'
	*/
	private void start(){		
		while(true){
			System.out.println("");
			System.out.println("Type 'quit' to exit the program");
			System.out.println("Press [enter] to display current order book");
			System.out.println("Enter order: ");

			// get each line of input
			String userInput = helper.getUserInput();

			// if user only press enter
			if(userInput == null){
				helper.updateScreen();
			}
			else{
				// if user wants to quit program, break loop
				if (userInput.equals("quit")) {
					break;
				}
				// otherwise check for user's input
				errorFlag = checkUserInputValidity(userInput);
				
				// if there is error
				if (errorFlag) {
					// print the error
					System.out.println(err.toString());
					err.delete(7, err.length());
				}
				else{
					if (side == 'B') {
						helper.addBuy(quantity_globe, price_globe);
					}
					if (side == 'S') {
						helper.addSell(quantity_globe, price_globe);
					}
					helper.updateScreen();
				}
			}	
		}
	}

	/**
	 * this is the check function that checks user's input
	 * if no invalid input, return false
	 * otherwise it would set error message in err global variable
	 * and return true 
	 * @param userInput: String representation of user input
	 * @return true/false: boolean to indicate if there is a error in user input
	 * 
	 * */
	private boolean checkUserInputValidity(String userInput){
		//analysis input
		String[] result = userInput.split(" ");

		// if input is less than 3 segment, return not enough arguments
		if (result.length < 3) {
			err.append("not enough arguments");
			return true;
		}

		// if input is more than 3 segment, return too many arguments
		if (result.length > 3) {
			err.append("too many arguments");
			return true;
		}

		// if input side is not B nor S
		if( !result[0].equals("B") && !result[0].equals("S") ){
			err.append("not valid side, please use B for Buy, S for Sell");
			return true;
		}

		// check quantity validity
		int quantity = 0;
		try{
			quantity = Integer.parseInt(result[1]);
		}catch(NumberFormatException e){
			err.append("quantity is not a valid integer");
			return true;
		}

		if(quantity<=0){
			err.append("quantity is not bigger than 0");
			return true;
		}

		// in this design, the biggest quantity could deal with is 999,999,999
		// larger number could be arranged by altering code
		if (quantity>999999999) {
			err.append("are you sure you want to deal with such huge amount? >999,999,999");
			return true;
		}

		// check price validity, check decimal!!!
		float price = 0;
		try{
			price = Float.parseFloat(result[2]);
		}catch(NumberFormatException e){
			err.append("price is not a valid float");
			return true;
		}

		if(price<=0){
			err.append("price is not bigger than 0");
			return true;
		}

		float price_tmp = price;
		price_tmp = price_tmp*1000 - (int)(price_tmp*1000);
		if(price_tmp != 0){
			err.append("price has more than three decimal");
			return true;
		}

		// check for side
		if (result[0].equals("B")) {
			side = 'B';
			price_globe = price;
			quantity_globe = quantity;
			return false;
		}

		if (result[0].equals("S")) {
			side = 'S';
			price_globe = price;
			quantity_globe = quantity;
			return false;
		}
		
		err.append("Error! never considered case!!");
		return true;
	}


	public static void main(String...args){
		// driver function
		TradeBot sim = new TradeBot();
		sim.start();
	}
}