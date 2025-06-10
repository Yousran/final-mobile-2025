package com.example.finalmobile2025.models;

import com.google.gson.annotations.SerializedName;

public class PoolingResponse {
    @SerializedName("acceptResponses")
    private boolean acceptResponses;
    
    @SerializedName("participantCount")
    private int participantCount;
    
    @SerializedName("questionCount")
    private int questionCount;

    // Getters
    public boolean isAcceptResponses() { return acceptResponses; }
    public int getParticipantCount() { return participantCount; }
    public int getQuestionCount() { return questionCount; }

    // Setters
    public void setAcceptResponses(boolean acceptResponses) { this.acceptResponses = acceptResponses; }
    public void setParticipantCount(int participantCount) { this.participantCount = participantCount; }
    public void setQuestionCount(int questionCount) { this.questionCount = questionCount; }
}
