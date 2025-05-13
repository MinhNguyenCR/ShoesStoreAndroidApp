package com.example.shoesstoreandroidapp.customer.Response;

import com.example.shoesstoreandroidapp.customer.Model.ProductModel;

import java.util.List;

public class ProductResponse {

    private int code;
    private List<ProductModel> result;

    public int getCode() {
        return code;
    }

    public List<ProductModel> getResult() {
        return result;
    }


}
