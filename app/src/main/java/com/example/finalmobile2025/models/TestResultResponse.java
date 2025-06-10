package com.example.finalmobile2025.models;

import com.google.gson.annotations.SerializedName;

public class TestResultResponse {
    @SerializedName("participant")
    private ParticipantResult participant;

    @SerializedName("test")
    private TestResult test;

    // Getters and Setters
    public ParticipantResult getParticipant() {
        return participant;
    }

    public void setParticipant(ParticipantResult participant) {
        this.participant = participant;
    }

    public TestResult getTest() {
        return test;
    }

    public void setTest(TestResult test) {
        this.test = test;
    }

    public static class ParticipantResult {
        @SerializedName("username")
        private String username;

        @SerializedName("score")
        private double score;

        // Getters and Setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }
    }

    public static class TestResult {
        @SerializedName("title")
        private String title;

        @SerializedName("showDetailedScore")
        private boolean showDetailedScore;

        // Getters and Setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isShowDetailedScore() {
            return showDetailedScore;
        }

        public void setShowDetailedScore(boolean showDetailedScore) {
            this.showDetailedScore = showDetailedScore;
        }
    }
}
