package com.example.finalmobile2025.models;

import com.google.gson.annotations.SerializedName;

public class StartTestResponse {
    @SerializedName("participantId")
    private String participantId;

    @SerializedName("message")
    private String message;

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
