package com.jianchen.tradebot;
/**
 * Trade Class
 * 
 * Trade class to represent each single trade
 * 
 * int quantity: quantity that traded
 * float price: price that is traded at
 * 
 * for future extensibility:
 * long buy_id: the buy order that caused this trade
 * long sell_id: the sell order that caused this trade
 * 
 * And their getter and setters
 * 
 * Author: Jian Chen
 * June 6, 2014
 * */

public class Trade{
	private int quantity;
	private float price;

	private long buy_id;
	private long sell_id;

	Trade(){}

	Trade(int quantity, float price){
		this.quantity = quantity;
		this.price = price;
		this.buy_id = 0;
		this.sell_id = 0;
	}

	// this constructor is set for future extensibility
	Trade(int quantity, float price, long buy_id, long sell_id){
		this.quantity = quantity;
		this.price = price;
		this.buy_id = buy_id;
		this.sell_id = sell_id;
	}

	public int getQuantity(){
		return quantity;
	}

	public float getPrice(){
		return price;
	}

	public long getBuyID(){
		return buy_id;
	}

	public long getSellID(){
		return sell_id;
	}

	public void setQuantity(int quantity){
		this.quantity = quantity;
	}

	public void setPrice(float price){
		this.price = price;
	}

	public void setBuyID(long buy_id){
		this.buy_id = buy_id;
	}

	public void setSellID(long sell_id){
		this.sell_id = sell_id;
	}

}