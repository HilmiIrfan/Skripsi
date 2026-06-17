package com.doyatama.university.model;

public class Subject {
    private String id;
    private String name;
    private String description;
    private Integer credit_point;
    private Integer year_commenced;
    private StudyProgram study_program;
    private SubjectGroup subject_group;

    public Subject() {
    }

    public Subject(String id, String name, String description, Integer credit_point, Integer year_commenced, StudyProgram study_program, SubjectGroup subject_group) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.credit_point = credit_point;
        this.year_commenced = year_commenced;
        this.study_program = study_program;
        this.subject_group = subject_group;
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

    public Integer getCredit_point() {
        return credit_point;
    }

    public void setCredit_point(Integer credit_point) {
        this.credit_point = credit_point;
    }

    public Integer getYear_commenced() {
        return year_commenced;
    }

    public void setYear_commenced(Integer year_commenced) {
        this.year_commenced = year_commenced;
    }

    public StudyProgram getStudyProgram() {
        return study_program;
    }

    public void setStudyProgram(StudyProgram study_program) {
        this.study_program = study_program;
    }

    public SubjectGroup getSubject_group() {
        return subject_group;
    }

    public void setSubject_group(SubjectGroup subject_group) {
        this.subject_group = subject_group;
    }

    public boolean isValid() {
        return this.id != null && this.name != null && this.description != null && this.credit_point != null && this.year_commenced != null;
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
            case "credit_point":
                this.credit_point = Integer.parseInt(value);
                break;
            case "year_commenced":
                this.year_commenced = Integer.parseInt(value);
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
