package com.example.shoesstoreandroidapp.customer;


public class shoesModel {
    private String shoesName;
    private double price;
    private double rating;
    private String image;

    public shoesModel(String shoesName, double price, double rating, String image) {
        this.shoesName = shoesName;
        this.price = price;
        this.rating = rating;
        this.image = image;
    }

    public String getShoesName() { return shoesName; }
    public void setShoesName(String shoesName) { this.shoesName = shoesName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
