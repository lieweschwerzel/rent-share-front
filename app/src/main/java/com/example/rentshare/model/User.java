package com.example.rentshare.model;

import java.time.LocalDateTime;

public class User {

    private int id;
    private String username;
//  private String email;
    private String token;
    private String zipcode;
    private String streetName;
    private int houseNumber;
    private String createdOn;

    public User(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public User(int id, String username, String token, String zipcode, String streetName, int houseNumber, String createdOn) {
        this.id = id;
        this.username = username;
        this.token = token;
        this.zipcode = zipcode;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.createdOn = createdOn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getZipcode() { return zipcode; }

    public void setZipcode(String zipcode) { this.zipcode = zipcode; }

    public String getStreetName() { return streetName; }

    public void setStreetName(String streetName) { this.streetName = streetName; }

    public int getHouseNumber() { return houseNumber; }

    public void setHouseNumber(int houseNumber) { this.houseNumber = houseNumber; }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
}
