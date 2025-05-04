package com.example.shoesstoreandroidapp.customer;


public class shoesModel {
    private String name;
    private double price;
    private double feedbackStar;
    private String image;

    public shoesModel(String name, double price, double feedbackStar, String image) {
        this.name = name;
        this.price = price;
        this.feedbackStar = feedbackStar;
        this.image = image;
    }

    public String getShoesName() { return name; }
    public void setShoesName(String shoesName) { this.name = shoesName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getRating() { return feedbackStar; }
    public void setRating(double rating) { this.feedbackStar = rating; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
