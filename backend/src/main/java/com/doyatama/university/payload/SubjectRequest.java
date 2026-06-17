package com.doyatama.university.payload;

public class SubjectRequest {
    private String name;
    private String description;
    private Integer credit_point;
    private Integer year_commenced;
    private String study_program_id;
    private String subject_group_id;

    public SubjectRequest() {
    }

    public SubjectRequest(String name, String description, Integer credit_point, Integer year_commenced, String study_program_id, String subject_group_id) {
        this.name = name;
        this.description = description;
        this.credit_point = credit_point;
        this.year_commenced = year_commenced;
        this.study_program_id = study_program_id;
        this.subject_group_id = subject_group_id;
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

    public String getStudy_program_id() {
        return study_program_id;
    }

    public void setStudy_program_id(String study_program_id) {
        this.study_program_id = study_program_id;
    }

    public String getSubject_group_id() {
        return subject_group_id;
    }

    public void setSubject_group_id(String subject_group_id) {
        this.subject_group_id = subject_group_id;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
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
            case "study_program_id":
                this.study_program_id = value;
                break;
            case "subject_group_id":
                this.subject_group_id = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
