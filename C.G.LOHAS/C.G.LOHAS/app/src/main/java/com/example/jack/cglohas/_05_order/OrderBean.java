package com.example.jack.cglohas._05_order;

import java.io.Serializable;
import java.sql.Date;
import java.util.*;



// 本類別存放訂單資料

public class OrderBean implements Serializable{
	private int 	orderID;
	private int 	userID;
	private double	totalAmount;
	private String cellphone;
	private String	shippingAddress;
	private java.sql.Timestamp  orderDate;
	private Date  shippingDate;
	private String	cancelTag;
	private String description;
	private int discount;
	private List<OrderItemDAOBean> items = new ArrayList<OrderItemDAOBean>();
	
	public OrderBean() {
		super();
	}
	
	public OrderBean(int userID,String shippingAddress, double totalAmount, String cellphone,String cancelTag
			,String description, int discount, List<OrderItemDAOBean> items) {
		super();
		this.userID = userID;
		this.totalAmount = totalAmount;
		this.shippingAddress = shippingAddress;
		this.cancelTag=cancelTag;
		this.description=description;
		this.cellphone=cellphone;
		this.discount = discount;
		this.items=items;
	}

	public OrderBean(int userID,String shippingAddress, double totalAmount, String cellphone,  java.sql.Timestamp orderDate,String cancelTag,
			Date shippingDate,String description, int discount, List<OrderItemDAOBean> items) {
		super();
		this.userID = userID;
		this.totalAmount = totalAmount;
		this.shippingAddress = shippingAddress;
		this.orderDate = orderDate;
		this.shippingDate = shippingDate;
		this.cancelTag=cancelTag;
		this.description=description;
		this.cellphone=cellphone;
		this.discount = discount;
		this.items=items;
	}

	public OrderBean(int orderID, int userID, int totalAmount, String cellphone, String shippingAddress,
			java.sql.Timestamp orderDate, Date shippingDate, String cancelTag, String description, int discount,
			List<OrderItemDAOBean> items) {
		super();
		this.orderID = orderID;
		this.userID = userID;
		this.totalAmount = totalAmount;
		this.cellphone = cellphone;
		this.shippingAddress = shippingAddress;
		this.orderDate = orderDate;
		this.shippingDate = shippingDate;
		this.cancelTag = cancelTag;
		this.description = description;
		this.discount = discount;
		this.items = items;
	}
	
	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}


	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	public int getUserID() {
		return userID;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}


	public java.sql.Timestamp getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(java.sql.Timestamp orderDate) {
		this.orderDate = orderDate;
	}

	public Date getShippingDate() {
		return shippingDate;
	}

	public void setShippingDate(Date shippingDate) {
		this.shippingDate = shippingDate;
	}

	public String getCancelTag() {
		return cancelTag;
	}

	public void setCancelTag(String cancelTag) {
		this.cancelTag = cancelTag;
	}

	public List<OrderItemDAOBean> getItems() {
		return items;
	}

	public void setItems(List<OrderItemDAOBean> items) {
		this.items = items;
	}

}
