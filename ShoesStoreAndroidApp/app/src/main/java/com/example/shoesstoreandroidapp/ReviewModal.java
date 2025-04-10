package com.example.shoesstoreandroidapp;

import java.io.Serializable;

public class ReviewModal implements Serializable {
    private String userName;
    private Float rating;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    private String comment;

    public ReviewModal(String userName, Float rating, String comment) {
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
    }
}
