package com.doyatama.university.payload;

/**
 * @author alfa
 */
public class QuestionCriteriaRequest {
    private String name;
    private String description;
    private String category;

    public QuestionCriteriaRequest() {
    }
    public QuestionCriteriaRequest(String name, String description, String category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "name":
                this.name = value;
                break;
            case "description":
                this.description = value;
                break;
            case "category":
                this.category = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
