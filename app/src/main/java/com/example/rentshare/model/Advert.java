package com.example.rentshare.model;

import java.time.LocalDateTime;

public class Advert {

    private Long id;
    private String title;
    private String description;
    private long price;
    private String imageUrl;
    private double latitude;
    private double longitude;
    private long userId;
    private String advertOwner;
    private String createdOn;
    private int duration;

    public Advert(String title, String description, long price, String imageUrl, String createdOn, String advertOwner) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.createdOn = createdOn;
        this.advertOwner = advertOwner;
    }

    public Advert(String title, String description, long price, String imageUrl, String createdOn, String advertOwner, double latitude, double longitude) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.createdOn = createdOn;
        this.advertOwner = advertOwner;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Advert(String title, String description, long price, String imageUrl, double latitude, double longitude, long userId, String createdOn, int duration) {
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


    public String getAdvertOwner() {
        return advertOwner;
    }

    public void setAdvertOwner(String advertOwner) {
        this.advertOwner = advertOwner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public double getLatitude() { return latitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

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
