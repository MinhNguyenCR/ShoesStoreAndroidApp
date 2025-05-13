package com.example.shoesstoreandroidapp.customer;

import java.time.LocalDate;

public class AccountDetailRequest {
    private String name;
    private String phone;
    private String birthday;

    public AccountDetailRequest(String name, String phone, String birthday) {
        this.name = name;
        this.phone = phone;
        this.birthday = birthday;
    }
    public AccountDetailRequest(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
    // Getter/Setter
    public String getFullname() {
        return name;
    }

    public void setFullname(String fullname) {
        this.name = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}

