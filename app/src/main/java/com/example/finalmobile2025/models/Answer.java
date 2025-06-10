package com.example.finalmobile2025.models;

import com.google.gson.annotations.SerializedName;

public class Answer {
    @SerializedName("questionId")
    private String questionId;

    @SerializedName("answerText")
    private String answerText;

    @SerializedName("isMarked")
    private boolean isMarked;

    // Getters and Setters
    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }
}
