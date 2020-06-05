package com.example.rentshare.model;

public class User {

    private int id;
    private String username;
//  private String email;
    private String token;
    private String latitude;
    private String longitude;
    private String zipcode;
    private String streetName;
    private int houseNumber;

    public User(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public User(String username, String token, String latitude, String longitude, String zipcode, String streetName, int houseNumber) {
        this.username = username;
        this.token = token;
        this.latitude = latitude;
        this.longitude = longitude;
        this.zipcode = zipcode;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
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

    public String getLatitude() { return latitude; }

    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() { return longitude; }

    public void setLongitude(String longitude) { this.longitude = longitude; }

    public String getZipcode() { return zipcode; }

    public void setZipcode(String zipcode) { this.zipcode = zipcode; }

    public String getStreetName() { return streetName; }

    public void setStreetName(String streetName) { this.streetName = streetName; }

    public int getHouseNumber() { return houseNumber; }

    public void setHouseNumber(int houseNumber) { this.houseNumber = houseNumber; }
}
