package com.jianchen.tradebot;
/** Order class
  * Order object to build
  * sells list: LinkedList<Order> sells
  * buys list: LinkedList<Order> buys
  * 
  * this class has two member variables
  * integer quantity: quantity of order
  * float price: the price of order
  * 
  * there are also two member variables
  * char side: Buy or Sell (B/S)
  * long id: a unique number represents each single order placed
  * those variables are not used in this program, but
  * are set up for future extensibility
  * 
  * and their getter and setters
  *
  * author: Jian Chen
  * June 6, 2014
*/
public class Order{

	private int quantity = 0;
	private float price = 0;
	private char side = ' ';
	private long id = 0;

	Order(){}

	Order(int quantity, float price){
		this.side = ' ';
		this.id = 0;
		this.quantity = quantity;
		this.price = price;
	}

	// this constructor is for future extensibility
	Order(int quantity, float price, char side, long id){
		this.side = side;
		this.id = id;
		this.quantity = quantity;
		this.price = price;
	}

	public int getQuantity(){
		return quantity;
	}

	public float getPrice(){
		return price;
	}

	public char getSide(){
		return side;
	}

	public long getID(){
		return id;
	}
	
	public void setQuantity(int quantity){
		this.quantity = quantity;
	}

	public void setPrice(float price){
		this.price = price;
	}

	public void setSide(char side){
		this.side = side;
	}

	public void setID(long id){
		this.id = id;
	}
}