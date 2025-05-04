package com.example.shoesstoreandroidapp.customer.Response;

import com.example.shoesstoreandroidapp.customer.Model.ProductDetailModel;
import com.example.shoesstoreandroidapp.customer.Model.ProductModel;

import java.util.List;

public class ProductDetailResponse {
    private int code;
    private ProductDetailModel result;

    public int getCode() {
        return code;
    }

    public ProductDetailModel getResult() {
        return result;
    }
}
