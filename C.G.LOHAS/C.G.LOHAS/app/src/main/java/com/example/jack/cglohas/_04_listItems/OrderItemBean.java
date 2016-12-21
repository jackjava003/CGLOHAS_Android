package com.example.jack.cglohas._04_listItems;

public class OrderItemBean {

	private String name;
	private int itemid;
	private int qty;
	private double unitPrice;
	private double discount;
	private int storage;

	

	public OrderItemBean(String name, int itemid, int qty, double unitPrice, double discount) {
		super();
		this.name = name;
		this.itemid = itemid;
		this.qty = qty;
		this.unitPrice = unitPrice;
		this.discount = discount;
	}

	public OrderItemBean() {
		
	}

	public String getName(){return name;}

	public void setName(String name){this.name=name;}

	public int getStorage() {
		return storage;
	}

	public void setStorage(int storage) {
		this.storage = storage;
	}
	
	public int getItemid() {
		return itemid;
	}

	public void setItemid(int itemid) {
		this.itemid = itemid;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
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
