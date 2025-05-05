package com.example.shoesstoreandroidapp.customer.Response;

import com.example.shoesstoreandroidapp.customer.Model.FeedbackModel;
import com.example.shoesstoreandroidapp.customer.Model.ProductModel;

import java.util.List;

public class FeedbackResponse {
    private int code;
    private List<FeedbackModel> result;

    public int getCode() { return code; }
    public List<FeedbackModel> getResult() { return result; }

}
