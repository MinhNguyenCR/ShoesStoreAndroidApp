package com.example.shoesstoreandroidapp.customer.Request;

public class AddToCartRequest {
    private Long accountId;
    private Long productId;
    private Integer size;
    private int quantity;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public AddToCartRequest(Long accountId, Long productId, Integer size, int quantity) {
        this.accountId = accountId;
        this.productId = productId;
        this.size = size;
        this.quantity = quantity;
    }


}
