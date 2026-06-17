package com.doyatama.university.model;

public class Semester {
    private String idSemester;
    private String namaSemester;
    private School school;

    public Semester() {
    }

    public Semester(String idSemester, String namaSemester, School school) {
        this.idSemester = idSemester;
        this.namaSemester = namaSemester;
        this.school = school;
    }

    public String getIdSemester() {
        return idSemester;
    }

    public void setIdSemester(String idSemester) {
        this.idSemester = idSemester;
    }

    public String getNamaSemester() {
        return namaSemester;
    }

    public void setNamaSemester(String namaSemester) {
        this.namaSemester = namaSemester;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public boolean isValid() {
        return this.idSemester != null && this.namaSemester != null && this.school != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idSemester":
                this.idSemester = value;
                break;
            case "namaSemester":
                this.namaSemester = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
