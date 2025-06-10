package com.example.finalmobile2025.models;

import com.google.gson.annotations.SerializedName;

public class TestDetailsResponse {
    @SerializedName("id")
    private String id;
    
    @SerializedName("title")
    private String title;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("joinCode")
    private String joinCode;
    
    @SerializedName("testDuration")
    private int testDuration;
    
    @SerializedName("startTime")
    private String startTime;
    
    @SerializedName("endTime")
    private String endTime;
    
    @SerializedName("acceptResponses")
    private boolean acceptResponses;
    
    @SerializedName("showDetailedScore")
    private boolean showDetailedScore;
    
    @SerializedName("showCorrectAnswers")
    private boolean showCorrectAnswers;
    
    @SerializedName("isOrdered")
    private boolean isOrdered;
    
    @SerializedName("createdAt")
    private String createdAt;

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getJoinCode() { return joinCode; }
    public int getTestDuration() { return testDuration; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public boolean isAcceptResponses() { return acceptResponses; }
    public boolean isShowDetailedScore() { return showDetailedScore; }
    public boolean isShowCorrectAnswers() { return showCorrectAnswers; }
    public boolean isOrdered() { return isOrdered; }
    public String getCreatedAt() { return createdAt; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setJoinCode(String joinCode) { this.joinCode = joinCode; }
    public void setTestDuration(int testDuration) { this.testDuration = testDuration; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public void setAcceptResponses(boolean acceptResponses) { this.acceptResponses = acceptResponses; }
    public void setShowDetailedScore(boolean showDetailedScore) { this.showDetailedScore = showDetailedScore; }
    public void setShowCorrectAnswers(boolean showCorrectAnswers) { this.showCorrectAnswers = showCorrectAnswers; }
    public void setOrdered(boolean ordered) { isOrdered = ordered; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
