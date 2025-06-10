package com.example.finalmobile2025.models;

import com.google.gson.annotations.SerializedName;

public class EssayAnswerRequest {
    @SerializedName("answerId")
    private String answerId;

    @SerializedName("participantId")
    private String participantId;

    @SerializedName("answerText")
    private String answerText;

    public EssayAnswerRequest(String answerId, String participantId, String answerText) {
        this.answerId = answerId;
        this.participantId = participantId;
        this.answerText = answerText;
    }

    // Getters and Setters
    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
}
