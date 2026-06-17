package com.doyatama.university.payload;

public class ChapterRequest {
    private String name;
    private String description;
    private String course_id;

    public ChapterRequest() {
    }

    public ChapterRequest(String name, String description, String course_id) {
        this.name = name;
        this.description = description;
        this.course_id = course_id;
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

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "name":
                this.name = value;
                break;
            case "description":
                this.description = value;
                break;
            case "course_id":
                this.course_id = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
