package com.example.rentshare.model;

import java.time.LocalDateTime;

public class Bid {

    private Long id;

    private String username;

    private long advertId;

    private double amount;

    private String createdOn;

    public Bid() { }

    public Bid(String username, long advertId, double amount, String createdOn) {
        this.username = username;
        this.advertId = advertId;
        this.amount = amount;
        this.createdOn = createdOn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getAdvertId() {
        return advertId;
    }

    public void setAdvertId(long advertId) {
        this.advertId = advertId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
}
