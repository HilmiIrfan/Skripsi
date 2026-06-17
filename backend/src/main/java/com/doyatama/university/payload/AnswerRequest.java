package com.doyatama.university.payload;
public class AnswerRequest {
    private String id;
    private String question_id;
    private String title;
    private String description;
    private String type;
    private Boolean is_right;

    public AnswerRequest() {
    }

    public AnswerRequest(String id, String question_id, String title, String description, String type, Boolean is_right) {
        this.id = id;
        this.question_id = question_id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.is_right = is_right;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIs_right() {
        return is_right;
    }

    public void setIs_right(Boolean is_right) {
        this.is_right = is_right;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "id":
                this.id = value;
                break;
            case "question_id":
                this.question_id = value;
                break;
            case "title":
                this.title = value;
                break;
            case "description":
                this.description = value;
                break;
            case "type":
                this.type = value;
                break;
            case "is_right":
                this.is_right = Boolean.valueOf(value);
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}