package com.doyatama.university.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Answer {
    private String id;
    private Question question;
    private String title;
    private String description;
    private AnswerType type;
    private Boolean is_right;
    private String file_path;

    public enum AnswerType {
        IMAGE,
        AUDIO,
        VIDEO,
        NORMAL,
    }

    public Answer() {
    }

    public Answer(String id, Question question, String title, String description, AnswerType type, Boolean is_right, String file_path) {
        this.id = id;
        this.question = question;
        this.title = title;
        this.description = description;
        this.type = type;
        this.is_right = is_right;
        this.file_path = file_path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AnswerType getType() {
        return type;
    }

    public void setType(AnswerType type) {
        this.type = type;
    }

    public Boolean getIs_right() {
        return is_right;
    }

    public void setIs_right(Boolean is_right) {
        this.is_right = is_right;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public boolean isValid() {
        return this.id != null && this.title != null && this.description != null && this.type != null && this.is_right != null && this.file_path != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "id":
                this.id = value;
                break;
            case "title":
                this.title = value;
                break;
            case "description":
                this.description = value;
                break;
            case "type":
                this.type = AnswerType.valueOf(value);
                break;
            case "is_right":
                this.is_right = Boolean.valueOf(value);
                break;
            case "file_path":
                this.file_path = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}