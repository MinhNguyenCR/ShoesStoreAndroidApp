package com.example.shoesstoreandroidapp.customer.Request;

public class FeedbackRequest {
    private String comment;
    private Integer rate;
    private Long user_id;
    private String productName;


    // Getters và Setters
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Integer getRate() { return rate; }
    public void setRate(Integer rate) { this.rate = rate; }

    public Long getUser_id() { return user_id; }
    public void setUser_id(Long user_id) { this.user_id = user_id; }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


}

