package com.example.shoesstoreandroidapp.customer;

import java.io.Serializable;

public class cartModel implements Serializable {
    private String name;
    private Integer size;

    private double price;
    private int image;

    public cartModel(String name, Integer size, double price, int image) {
        this.name = name;
        this.size = size;
        this.price = price;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
