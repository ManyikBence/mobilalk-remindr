package com.example.remindr;

public class SubscriptionItem {
    private String name;
    private int imageResId;
    private long expiryTimeMillis;

    public SubscriptionItem(String name, int imageResId, long expiryTimeMillis) {
        this.name = name;
        this.imageResId = imageResId;
        this.expiryTimeMillis = expiryTimeMillis;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public long getExpiryTimeMillis() {
        return expiryTimeMillis;
    }
}

