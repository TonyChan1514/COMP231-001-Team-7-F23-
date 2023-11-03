
package application;

import java.sql.*;

public class OrderInfo {
    private int orderID;
    private String customerName;
    private String phone;
    private String email;
    private Date orderDate;
    private Date shippingDate;
    private String shippingAddress;
    private String orderStatusDesc;

    // A data model for displaying the order information to the table
    public OrderInfo(int orderID, String customerName, String phone, String email, Date orderDate, Date shippingDate, String shippingAddress, String orderStatusDesc) {
    	this.orderID = orderID;
    	this.customerName = customerName;
    	this.phone = phone;
    	this.email = email;
    	this.orderDate = orderDate;
    	this.shippingDate = shippingDate;
    	this.shippingAddress = shippingAddress;
    	this.orderStatusDesc = orderStatusDesc;
    }
    
    public int getOrderID() {
        return orderID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String getOrderStatusDesc() {
        return orderStatusDesc;
    }
}