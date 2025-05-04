package com.example.shoesstoreandroidapp.customer.Model;

import java.util.List;

public class ProductDetailModel {
    private int id;
    private String name;
    private String image;
    private double price;
    private String description;
    private float feedbackStar;
    private List<Integer> size;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getFeedbackStar() {
        return feedbackStar;
    }

    public void setFeedbackStar(float feedbackStar) {
        this.feedbackStar = feedbackStar;
    }

    public List<Integer> getSize() {
        return size;
    }

    public void setSize(List<Integer> size) {
        this.size = size;
    }
}
