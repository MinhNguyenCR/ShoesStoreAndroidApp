package com.example.shoesstoreandroidapp.customer;

import java.time.LocalDate;

public class AccountDetailRequest {
    private String fullname;
    private String phone;
    //private LocalDate birthday;

//    public AccountDetailRequest(String fullname, String phone, LocalDate birthday) {
//        this.fullname = fullname;
//        this.phone = phone;
//        this.birthday = birthday;
//    }
    public AccountDetailRequest(String fullname, String phone) {
        this.fullname = fullname;
        this.phone = phone;
    }
    // Getter/Setter
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

//    public LocalDate getBirthday() {
//        return birthday;
//    }
//
//    public void setBirthday(LocalDate birthday) {
//        this.birthday = birthday;
//    }
}

