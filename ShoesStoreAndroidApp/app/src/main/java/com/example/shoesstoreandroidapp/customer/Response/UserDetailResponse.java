package com.example.shoesstoreandroidapp.customer.Response;

import java.time.LocalDate;

public class UserDetailResponse {
    private String name;
    private String number;
    private String birthday;
    private String image;

    public UserDetailResponse(String name, String number, String birthday, String image) {
        this.name = name;
        this.number = number;
        this.birthday = birthday;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
