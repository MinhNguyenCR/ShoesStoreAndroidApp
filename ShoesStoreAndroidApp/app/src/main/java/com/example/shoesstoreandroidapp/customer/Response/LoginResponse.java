package com.example.shoesstoreandroidapp.customer.Response;

public class LoginResponse {
    private int code;
    private Result result;

    public int getCode() {
        return code;
    }

    public Result getResult() {
        return result;
    }

    public static class Result {
        private long userId;

        public long getUserId() {
            return userId;
        }
    }
}
