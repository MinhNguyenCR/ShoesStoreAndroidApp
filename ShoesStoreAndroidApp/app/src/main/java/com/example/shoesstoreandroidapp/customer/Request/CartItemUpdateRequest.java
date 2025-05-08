package com.example.shoesstoreandroidapp.customer.Request;

public class CartItemUpdateRequest {
    Long cartItemId;
    Integer quantity;

    public CartItemUpdateRequest(Long cartItemId, Integer quantity) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }
}
