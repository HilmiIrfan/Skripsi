package com.doyatama.university.model;

public class TeamTeaching {

    private String id;
    private String name;
    private String description;

    private Lecture lecture;

    private Lecture lecture2;

    private Lecture lecture3;

    public TeamTeaching() {
    }

    public TeamTeaching(String id, String name, String description, Lecture lecture, Lecture lecture2,
            Lecture lecture3) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.lecture = lecture;
        this.lecture2 = lecture2;
        this.lecture3 = lecture3;
    }

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

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    public Lecture getLecture2() {
        return lecture2;
    }

    public void setLecture2(Lecture lecture2) {
        this.lecture2 = lecture2;
    }

    public Lecture getLecture3() {
        return lecture3;
    }

    public void setLecture3(Lecture lecture3) {
        this.lecture3 = lecture3;
    }

    public boolean isValid() {
        return this.id != null && this.name != null && this.description != null;
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
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
