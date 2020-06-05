package com.example.rentshare.model;

public class Login {

    String username;
    String password;
    String zipcode;
    String streetName;
    int houseNumber;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Login(String username, String password, String zipcode, String streetName, int houseNumber) {
        this.username = username;
        this.password = password;
        this.zipcode = zipcode;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
    }
}
