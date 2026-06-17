package com.doyatama.university.payload;

public class TeamTeachingRequest {
    private String id;
    private String name;
    private String description;

    private String lecture;
    private String lecture2;
    private String lecture3;

    // Default constructor
    public TeamTeachingRequest() {
    }

    // Constructor with parameters
    public TeamTeachingRequest(String id, String name, String description, String lecture, String lecture2,
            String lecture3) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.lecture = lecture;
        this.lecture2 = lecture2;
        this.lecture3 = lecture3;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getLecture() {
        return lecture;
    }

    public void setLecture(String lecture) {
        this.lecture = lecture;
    }

    public String getLecture2() {
        return lecture2;
    }

    public void setLecture2(String lecture2) {
        this.lecture2 = lecture2;
    }

    public String getLecture3() {
        return lecture3;
    }

    public void setLecture3(String lecture3) {
        this.lecture3 = lecture3;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "id":
                this.id = value;
                break;
            case "name":
                this.name = value;
                break;
            case "description":
                this.description = value;
                break;
            case "lecture":
                this.lecture = value;
                break;
            case "lecture2":
                this.lecture2 = value;
                break;
            case "lecture3":
                this.lecture3 = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}