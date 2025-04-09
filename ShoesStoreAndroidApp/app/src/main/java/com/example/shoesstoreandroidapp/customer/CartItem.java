package com.example.shoesstoreandroidapp.customer;

import java.io.Serializable;

public class CartItem implements Serializable {
    private cartModel product;
    private int quantity;

    public CartItem(cartModel product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public cartModel getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}

