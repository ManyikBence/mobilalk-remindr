package com.example.remindr;

public class SubscriptionItem {
    private String id;
    private String userId;
    private String name;
    private double price;
    private long expirationDate;

    public SubscriptionItem() {
    }

    public SubscriptionItem(String id, String userId, String name, double price, long expirationDate) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.price = price;
        this.expirationDate = expirationDate;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public long getExpirationDate() {
        return expirationDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setExpirationDate(long expirationDate) {
        this.expirationDate = expirationDate;
    }
}

