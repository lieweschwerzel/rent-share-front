package com.example.rentshare.model;

import java.time.LocalDateTime;

public class Advert {

    private int id;
    private String title;
    private String description;
    private long price;
    private String imageUrl;
    private String latitude;
    private String longitude;
    private long userId;
    private String createdOn;
    private int duration;

    public Advert(String title, String description, long price, String imageUrl, String createdOn) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.createdOn = createdOn;
    }

    public Advert(String title, String description, long price, String imageUrl, String latitude, String longitude, long userId, String createdOn, int duration) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
        this.createdOn = createdOn;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLatitude() { return latitude; }

    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() { return longitude; }

    public void setLongitude(String longitude) { this.longitude = longitude; }

    public long getUserId() { return userId; }

    public void setUserId(long userId) { this.userId = userId; }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
