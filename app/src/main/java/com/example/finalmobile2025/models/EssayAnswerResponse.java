package com.example.finalmobile2025.models;

import com.google.gson.annotations.SerializedName;

public class EssayAnswerResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("answer")
    private AnswerResponseData answer;

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public AnswerResponseData getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerResponseData answer) {
        this.answer = answer;
    }

    public static class AnswerResponseData {
        @SerializedName("id")
        private String id;

        @SerializedName("answerText")
        private String answerText;

        @SerializedName("score")
        private int score;

        @SerializedName("scoreExplanation")
        private String scoreExplanation;

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

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getScoreExplanation() {
            return scoreExplanation;
        }

        public void setScoreExplanation(String scoreExplanation) {
            this.scoreExplanation = scoreExplanation;
        }
    }
}
