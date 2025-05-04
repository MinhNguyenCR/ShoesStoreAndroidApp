package com.example.shoesstoreandroidapp.customer;

public class Product {
    private String name;
    private Integer size;

    private int quantity;
    private long price;

    public Product(String name, Integer size, int quantity, long price) {
        this.name = name;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() { return name; }
    public Integer getSize() {
        return size;
    }

    public int getQuantity() { return quantity; }
    public long getPrice() { return price; }
}

