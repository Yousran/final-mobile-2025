package com.example.finalmobile2025.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class EssayData {
    @SerializedName("id")
    private String id;

    @SerializedName("answerText")
    private String answerText;

    @SerializedName("isExactAnswer")
    private boolean isExactAnswer;

    @SerializedName("maxScore")
    private int maxScore;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("answers")
    private List<AnswerData> answers;

    @SerializedName("answer")
    private AnswerData answer;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public boolean isExactAnswer() {
        return isExactAnswer;
    }

    public void setExactAnswer(boolean exactAnswer) {
        isExactAnswer = exactAnswer;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<AnswerData> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerData> answers) {
        this.answers = answers;
    }

    public AnswerData getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerData answer) {
        this.answer = answer;
    }
}
