package com.example.finalmobile2025.models;

import com.google.gson.annotations.SerializedName;

public class StartTestRequest {
    @SerializedName("username")
    private String username;

    public StartTestRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
