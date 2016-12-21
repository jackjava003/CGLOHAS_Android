package com.example.jack.cglohas._05_order;


import java.io.Serializable;

public class OrderItemDAOBean implements Serializable {
	int seqNo;
	int orderNo;
	int itemID;
	int amount;
	double unitPrice;
	double discount;
	String name;
	
	public OrderItemDAOBean(){
		
	}
	
	public OrderItemDAOBean(int itemID, int amount, double unitPrice, double discount) {
		super();
		this.itemID = itemID;
		this.amount = amount;
		this.unitPrice = unitPrice;
		this.discount = discount;
	}
	
	public OrderItemDAOBean(int seqNo,int orderNo, int itemID, int amount, double unitPrice, double discount) {
		super();
		this.seqNo =seqNo;
		this.orderNo = orderNo;
		this.itemID = itemID;
		this.amount = amount;
		this.unitPrice = unitPrice;
		this.discount = discount;
	}

	
	public  String getName(){return  name;}
	public  void setName(String name){this.name=name;}

	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}
	
	
	
	
}

