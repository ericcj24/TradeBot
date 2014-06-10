package com.jianchen.tradebot;
/**
 *  OrderBook class
 *  
 *  This class helps track of the orders 
 *  that have been placed
 * 
 * 	LinkedList buys: buy orders
 * 	LinkedList sells: sell orders
 * 
 * 	Customized comparators are set up to 
 *  sort price list in ascending order, and
 *  sort buy list in descending order 
 * 
 * 	some wrapper functions to help with
 * 	accessing and changing elements in 
 * 	each LinkedList respectfully
 * 
 * 
 *  author: Jian Chen
 *  June 6, 2014
 * */
import java.util.*;

public class OrderBook{

	private LinkedList<Order> buys = new LinkedList<Order>();
	private LinkedList<Order> sells = new LinkedList<Order>();

	OrderBook(){}

	public LinkedList<Order> getSells(){
		return sells;
	}

	public LinkedList<Order> getBuys(){
		return buys;
	}

	public void setSells(LinkedList<Order> sells){
		this.sells = sells;
	}

	public void setBuys(LinkedList<Order> buys){
		this.buys = buys;
	}


	// sort sell list in price ascending order
	class SellOrderCompare implements Comparator<Order>{
		public int compare(Order one, Order two){
			return Float.compare(one.getPrice(), two.getPrice());
		}
	}

	// sort buy list in price descending order
	class BuyOrderCompare implements Comparator<Order>{
		public int compare(Order one, Order two){
			return Float.compare(two.getPrice(), one.getPrice());
		}
	}

	public void sortBuysOrder(){
		BuyOrderCompare buyOrderCompare = new BuyOrderCompare();
		Collections.sort(buys, buyOrderCompare);
	}

	public void sortSellsOrder(){
		SellOrderCompare sellOrderCompare = new SellOrderCompare();
		Collections.sort(sells, sellOrderCompare);
	}

	public boolean addBuy(Order order){
		buys.add(order);
		return true;
	}

	public boolean addSell(Order order){
		sells.add(order);
		return true;
	}

	public boolean removeSell(int idx){
		sells.remove(idx);
		return true;
	}

	public boolean removeBuy(int idx){
		buys.remove(idx);
		return true;
	}

	public Order getSell(int idx){
		return sells.get(idx);
	}

	public Order getBuy(int idx){
		return buys.get(idx);
	}

	public void setBuy(int idx, Order order){
		buys.set(idx, order);
	}

	public void setSell(int idx, Order order){
		sells.set(idx, order);
	}

	public int buySize(){
		return buys.size();
	}

	public int sellSize(){
		return sells.size();
	}


}