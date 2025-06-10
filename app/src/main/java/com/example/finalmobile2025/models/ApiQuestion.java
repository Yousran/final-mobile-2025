package com.example.finalmobile2025.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ApiQuestion implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("testId")
    private String testId;

    @SerializedName("questionText")
    private String questionText;

    @SerializedName("type")
    private String type;

    @SerializedName("order")
    private int order;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("essay")
    private EssayData essay;

    @SerializedName("choice")
    private Object choice;

    @SerializedName("multipleSelect")
    private Object multipleSelect;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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

    public EssayData getEssay() {
        return essay;
    }

    public void setEssay(EssayData essay) {
        this.essay = essay;
    }

    public Object getChoice() {
        return choice;
    }

    public void setChoice(Object choice) {
        this.choice = choice;
    }

    public Object getMultipleSelect() {
        return multipleSelect;
    }

    public void setMultipleSelect(Object multipleSelect) {
        this.multipleSelect = multipleSelect;
    }
}
