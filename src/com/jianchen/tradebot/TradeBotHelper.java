package com.jianchen.tradebot;
/**
  *	TradeBotHelper class
  *
  *	TradeBotHelper class helper functions to get user input
  *	also includes core trading engines for buy and sell orders
  * and printing engine to display result
  * 
  * variable:
  * OrderBook: order book keep track of orders
  * LinkedList<Trade>: keep track of trades happened
  * 
  * 
  *
  * author: Jian Chen
  *	June 6, 2014
*/

import java.io.*;
import java.util.*;

public class TradeBotHelper{
	
	private OrderBook order_book = new OrderBook();
	private LinkedList<Trade> trade = new LinkedList<Trade>();
	
	ArrayList<Order> buys_no_dup = new ArrayList<Order>();
	ArrayList<Order> sells_no_dup = new ArrayList<Order>();
	
	
	// sort sell array in price ascending order
	class SellOrderCompare implements Comparator<Order>{
		public int compare(Order one, Order two){
			return Float.compare(one.getPrice(), two.getPrice());
		}
	}

	// sort buy array in price descending order
	class BuyOrderCompare implements Comparator<Order>{
		public int compare(Order one, Order two){
			return Float.compare(two.getPrice(), one.getPrice());
		}
	}

	/** 
	 * get user input from terminal
	 * 
	 * @return inputLine, a String representation of user input
	 * 
	 * */
	public String getUserInput(){
		String inputLine = null;
		try{
			BufferedReader is = new BufferedReader(
				new InputStreamReader(System.in));
			inputLine = is.readLine();
			if(inputLine.length() == 0) return null;
		}catch(IOException e){
			System.out.println("IOException: "+e);
		}
		return inputLine;
	}

	/** 
	 * if user input buy order, use this engine to analysis to
	 * see if there is a trade
	 * @param buyquantity: quantity of buy order
	 * @param buyprice: price of buy order
	 * 
	 * generates trades, add to trade list
	 * if no trade generates, or there are buy order remain, add to order book
	 * 
	 * */
	public void addBuy(int buyquantity, float buyprice){
		while(!order_book.getSells().isEmpty()){

			// if all buy order has been filled
			if(buyquantity<=0){
				break;
			}

			// if there are buy to fulfill
			if(order_book.getSell(0).getPrice() > buyprice ){
				break;
			}
			
			//sells[0] <= buyprice
			//check sells[0] quantity
			if(order_book.getSell(0).getQuantity() <= buyquantity){
				buyquantity = buyquantity - order_book.getSell(0).getQuantity();
				Trade newTrade = new Trade(order_book.getSell(0).getQuantity(), buyprice);
				trade.add(newTrade);
				order_book.removeSell(0);
			}
			else{

				Order updateSellQuantity = new Order((order_book.getSell(0).getQuantity()-buyquantity), order_book.getSell(0).getPrice());
				order_book.setSell(0, updateSellQuantity);
				Trade newTrade = new Trade(buyquantity, buyprice);
				trade.add(newTrade);
				buyquantity = 0;
				break;
			}
		}
		// in case buy remain, add to buys array, sort the buys array
		if(buyquantity!=0){
			order_book.addBuy(new Order(buyquantity, buyprice));
			order_book.sortBuysOrder();
		}
	}

	/** 
	 * if user input sell order, use this engine to analysis to
	 * see if there is a trade
	 * @param sellquantity: quantity of sell order
	 * @param sellprice: price of sell order
	 * 
	 * generates trades, add to trade list
	 * if no trade generates, or there are sell order remain, add to order book
	 * 
	 * */
	public void addSell(int sellquantity, float sellprice){
		while(!order_book.getBuys().isEmpty()){
			// if all sell order has been filled
			if(sellquantity <= 0){
				break;
			}
			
			// the highest buy is small than sell price, no more sell to fulfill
			if (order_book.getBuy(0).getPrice()<sellprice) {
				break;
			}

			// >= sellprice
			// there are potential sell to fulfill
			// check quantity
			if (order_book.getBuy(0).getQuantity() <= sellquantity) {
				sellquantity = sellquantity - order_book.getBuy(0).getQuantity();
				Trade newTrade = new Trade(order_book.getBuy(0).getQuantity(), order_book.getBuy(0).getPrice());
				trade.add(newTrade);
				order_book.removeBuy(0);
			}
			else{
				Order updateBuyQuantity = new Order((order_book.getBuy(0).getQuantity()-sellquantity), order_book.getBuy(0).getPrice());
				order_book.setBuy(0, updateBuyQuantity);
				Trade newTrade = new Trade(sellquantity, order_book.getBuy(0).getPrice());
				trade.add(newTrade);
				sellquantity = 0;
				break;
			}
		}

		// in case there are sell remain, add to sell array, and sort it
		if(sellquantity!=0){
			order_book.addSell(new Order(sellquantity, sellprice));
			order_book.sortSellsOrder();
		}
	}

	/**
	 * function to display result:
	 * ex:
	 *    trade:
	 *    5@3.111
	 *    Buy    |   Sell
	 *    4@2    |   10@10
	 * */
	public void updateScreen(){
		// print all trade
		System.out.println("trade: ");

		if (trade.isEmpty()) {
			System.out.println("");
		}

		for (Trade tr : trade) {
			float tradeprice_tmp = tr.getPrice();
			if (tradeprice_tmp == (int)tradeprice_tmp) {
				System.out.println(""+tr.getQuantity()+"@"+(int)tradeprice_tmp);
			}else{
				System.out.println(""+tr.getQuantity()+"@"+tradeprice_tmp);
			}
			
		}
		/** clear trade for next round use
		 *  for future extension, a permanent arraylist<Trade> could be used
		 *  to keep track of trading history. just insert those temp trade into
		 *  the permanent structure
		 * */
		trade.clear();
		
		System.out.println("order book: ");
		
		//ArrayList<Order> buys_no_dup = new ArrayList<Order>();
		//ArrayList<Order> sells_no_dup = new ArrayList<Order>();
		
		int buySize = order_book.buySize();
		int sellSize = order_book.sellSize();

		if (buySize == 1) {
			buys_no_dup.add(order_book.getBuy(0));
		}
		else if (buySize > 1) {
			int i = 0;
			int j = 1;
			//buys_no_dup.add(buys.get(0));
			outloop:
			while(true){
				if(order_book.getBuy(i).getPrice() == order_book.getBuy(j).getPrice()){
					int tempQuantity = order_book.getBuy(i).getQuantity();
					while(order_book.getBuy(i).getPrice() == order_book.getBuy(j).getPrice()){
						tempQuantity = tempQuantity + order_book.getBuy(j).getQuantity();
						j++;
						if (j==buySize) {
							buys_no_dup.add(new Order(tempQuantity, order_book.getBuy(i).getPrice()));
							break outloop;
						}
					}
					buys_no_dup.add(new Order(tempQuantity, order_book.getBuy(i).getPrice()));
					i=j;
					j++;
					if (j==buySize) {
						buys_no_dup.add(order_book.getBuy(i));
						break outloop;
					}
				}else{
					buys_no_dup.add(order_book.getBuy(i));
					i=j;
					j++;
					if (j==buySize) {
						buys_no_dup.add(order_book.getBuy(i));
						break outloop;
					}
				}
			}
		}

		if (sellSize == 1) {
			sells_no_dup.add(order_book.getSell(0));
		}
		else if (sellSize > 1) {
			int i = 0;
			int j = 1;
			//buys_no_dup.add(buys.get(0));
			outloop:
			while(true){
				if(order_book.getSell(i).getPrice() == order_book.getSell(j).getPrice()){
					int tempQuantity = order_book.getSell(i).getQuantity();
					while(order_book.getSell(i).getPrice() == order_book.getSell(j).getPrice()){
						tempQuantity = tempQuantity + order_book.getSell(j).getQuantity();
						j++;
						if (j==sellSize) {
							sells_no_dup.add(new Order(tempQuantity, order_book.getSell(i).getPrice()));
							break outloop;
						}
					}
					sells_no_dup.add(new Order(tempQuantity, order_book.getSell(i).getPrice()));
					i=j;
					j++;
					if (j==sellSize) {
						sells_no_dup.add(order_book.getSell(i));
						break outloop;
					}
				}else{
					sells_no_dup.add(order_book.getSell(i));
					i=j;
					j++;
					if (j==sellSize) {
						sells_no_dup.add(order_book.getSell(i));
						break outloop;
					}
				}
			}
		}
		

		// sort them -.-
		BuyOrderCompare buyOrderCompare = new BuyOrderCompare();
		Collections.sort(buys_no_dup, buyOrderCompare);

		SellOrderCompare sellOrderCompare = new SellOrderCompare();
		Collections.sort(sells_no_dup, sellOrderCompare);


		System.out.println("buy             |   sell"); //16, 3

		int n = buys_no_dup.size() < sells_no_dup.size() ? buys_no_dup.size() : sells_no_dup.size();
		int l = buys_no_dup.size() > sells_no_dup.size() ? buys_no_dup.size() : sells_no_dup.size();
		boolean flag = buys_no_dup.size() < sells_no_dup.size() ? true : false;
		int i = 0;
		for (i=0; i<n; i++) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(buys_no_dup.get(i).getQuantity());
			buffer.append('@');
			
			float buyprice_tmp = buys_no_dup.get(i).getPrice();
			if (buyprice_tmp == (int)buyprice_tmp) {
				buffer.append((int)buyprice_tmp);
			}else{
				buffer.append(buyprice_tmp);
			}

			int spaceLength = buffer.length();
			for (int j=0; j<16-spaceLength; j++) {
				buffer.append(' ');
			}
			buffer.append("|   ");
			buffer.append(sells_no_dup.get(i).getQuantity());
			buffer.append('@');

			float sellprice_tmp = sells_no_dup.get(i).getPrice();
			if (sellprice_tmp == (int)sellprice_tmp) {
				buffer.append((int)sellprice_tmp);
			}else{
				buffer.append(sellprice_tmp);
			}

			
			System.out.println(buffer.toString());
		}
		if (flag) {
			for (; i<l; i++) {
				StringBuffer buffer = new StringBuffer();
				buffer.append("                |   ");
				buffer.append(sells_no_dup.get(i).getQuantity());
				buffer.append('@');
				
				float sellprice_tmp = sells_no_dup.get(i).getPrice();
				if (sellprice_tmp == (int)sellprice_tmp) {
					buffer.append((int)sellprice_tmp);
				}else{
					buffer.append(sellprice_tmp);
				}

				System.out.println(buffer.toString());
			}
		}
		else{
			for (; i<l; i++) {
				StringBuffer buffer = new StringBuffer();
				buffer.append(buys_no_dup.get(i).getQuantity());
				buffer.append('@');

				float buyprice_tmp = buys_no_dup.get(i).getPrice();
				if (buyprice_tmp == (int)buyprice_tmp) {
					buffer.append((int)buyprice_tmp);
				}else{
					buffer.append(buyprice_tmp);
				}

				int spaceLength = buffer.length();
				
				for (int j=0; j<16-spaceLength; j++) {
					buffer.append(' ');
				}
				buffer.append("|");
				System.out.println(buffer.toString());
			}
		}
		// clear those list for next round use
		sells_no_dup.clear();
		buys_no_dup.clear();
	}
}