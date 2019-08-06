package com.lebelleami.travelmantics;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class TravelDeal implements Serializable {
    private String id;
    private String destination;
    private String description;
    private String amount;
    private String currency;
    private String imageUrl;
    private String imageName;

    public TravelDeal(){}

    public TravelDeal(String destination, String description, String amount, String currency, String imageUrl, String imageName) {
        this.id = id;
        this.destination = destination;
        this.description = description;
        this.amount = amount;
        this.currency = currency;
        this.imageUrl = imageUrl;
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
