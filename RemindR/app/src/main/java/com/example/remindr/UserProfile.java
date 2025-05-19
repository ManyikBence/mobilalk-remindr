package com.example.remindr;

public class UserProfile {
    private String username;
    private String accountType;

    public UserProfile() {}

    public UserProfile(String username, String accountType) {
        this.username = username;
        this.accountType = accountType;
    }

    public String getUsername() {
        return username;
    }

    public String getAccountType() {
        return accountType;
    }
}
