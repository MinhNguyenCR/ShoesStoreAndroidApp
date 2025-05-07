package com.example.shoesstoreandroidapp.customer.Model;

import java.io.Serializable;

public class CartItemModel implements Serializable {
    private Long id;
    private String productName;
    private Integer size;
    private Integer quantity;
    private String image;
    private double total_price;
    public String getImage() {
        return image;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProductName() { return productName; }


    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public double getTotal_price() { return total_price; }

}

