package com.example.shoesstoreandroidapp.customer;

import com.example.shoesstoreandroidapp.customer.Model.CartItemModel;

import java.io.Serializable;

public class CartItem implements Serializable {
    private CartItemModel product;
    private int quantity;

    public CartItem(CartItemModel product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public CartItemModel getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}

