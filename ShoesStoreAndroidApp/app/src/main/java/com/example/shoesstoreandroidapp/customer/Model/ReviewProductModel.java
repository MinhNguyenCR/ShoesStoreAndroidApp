package com.example.shoesstoreandroidapp.customer.Model;

public class ReviewProductModel {
    private String productName;
    private String imageUrl;
    private float rating;
    private String comment;

    public ReviewProductModel(String productName, String imageUrl) {
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.rating = 0;
        this.comment = "";
    }

    public String getProductName() { return productName; }
    public String getImageUrl() { return imageUrl; }
    public float getRating() { return rating; }
    public String getComment() { return comment; }

    public void setRating(float rating) { this.rating = rating; }
    public void setComment(String comment) { this.comment = comment; }
}

