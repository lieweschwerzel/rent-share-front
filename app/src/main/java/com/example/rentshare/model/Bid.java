package com.example.rentshare.model;

public class Bid {

    private Long id;

    private String fk_username;

    private Long fk_advertId;

    public Bid() { }

    public Bid(String fk_username, Long fk_advertId) {
        this.fk_username = fk_username;
        this.fk_advertId = fk_advertId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFk_username() {
        return fk_username;
    }

    public void setFk_username(String fk_username) {
        this.fk_username = fk_username;
    }

    public Long getFk_advertId() {
        return fk_advertId;
    }

    public void setFk_advertId(Long fk_advertId) {
        this.fk_advertId = fk_advertId;
    }
}
