package com.example.shoesstoreandroidapp.customer;

public class ApiResponse<T> {
    private int code;
    private T result;

    public int getCode() { return code; }
    public T getResult() { return result; }
}

