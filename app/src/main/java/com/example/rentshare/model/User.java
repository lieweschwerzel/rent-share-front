package com.example.rentshare.model;

public class User {

    private int id;
    private String username;
//  private String email;
    private String token;
    private String latitude;
    private String longitude;

    public User(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public User(String username, String token, String latitude, String longitude) {
        this.username = username;
        this.token = token;
        this.latitude = latitude;
        this.longitude = longitude;
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
}
