package com.example.finalmobile2025.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ParticipantData {
    @SerializedName("participant")
    private Participant participant;

    @SerializedName("test")
    private TestDetailsResponse test;

    @SerializedName("questions")
    private List<ApiQuestion> questions;

    // Getters and Setters
    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public TestDetailsResponse getTest() {
        return test;
    }

    public void setTest(TestDetailsResponse test) {
        this.test = test;
    }

    public List<ApiQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<ApiQuestion> questions) {
        this.questions = questions;
    }
    
    // Helper methods for backward compatibility
    public String getParticipantId() {
        return participant != null ? participant.getId() : null;
    }
    
    public String getUsername() {
        return participant != null ? participant.getUsername() : null;
    }
    
    public List<Answer> getAnswers() {
        // Extract answers from questions
        List<Answer> answers = new java.util.ArrayList<>();
        if (questions != null) {
            for (ApiQuestion question : questions) {
                if (question.getEssay() != null && question.getEssay().getAnswer() != null) {
                    Answer answer = new Answer();
                    answer.setQuestionId(question.getId());
                    answer.setAnswerText(question.getEssay().getAnswer().getAnswerText());
                    answer.setMarked(false); // Default value
                    answers.add(answer);
                }
            }
        }
        return answers;
    }
    
    public int getTimeRemaining() {
        // Calculate based on test duration (in minutes) * 60 seconds
        return test != null ? test.getTestDuration() * 60 : 1800; // Default 30 minutes
    }
}
