package com.example.shoesstoreandroidapp.customer;

public class OrderHistoryModel {
    private String orderId;
    private String price;
    private String date;
    private String description;
    private String address;

    public OrderHistoryModel(String orderId, String price, String date, String description, String address) {
        this.orderId = orderId;
        this.price = price;
        this.date = date;
        this.description = description;
        this.address = address;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }
}

