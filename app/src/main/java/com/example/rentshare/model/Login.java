package com.example.rentshare.model;

import java.time.LocalDateTime;

public class Login {

    String username;
    String password;
    String zipcode;
    String streetName;
    int houseNumber;
    String createdOn;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Login(String username, String password, String zipcode, String streetName, int houseNumber, String createdOn) {
        this.username = username;
        this.password = password;
        this.zipcode = zipcode;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.createdOn = createdOn;
    }

    public Login(String username, String zipcode, String streetName, int houseNumber) {
        this.username = username;
        this.zipcode = zipcode;
        this.streetName = streetName;
        this.houseNumber = houseNumber;

    }
}
