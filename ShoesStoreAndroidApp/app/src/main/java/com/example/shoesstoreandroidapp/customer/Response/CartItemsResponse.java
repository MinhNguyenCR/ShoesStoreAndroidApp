package com.example.shoesstoreandroidapp.customer.Response;

import com.example.shoesstoreandroidapp.customer.Model.CartItemModel;

import java.util.List;

public class CartItemsResponse {
    private int code;
    private List<CartItemModel> result;

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }

    public List<CartItemModel> getResult() { return result; }
    public void setResult(List<CartItemModel> result) { this.result = result; }
}
