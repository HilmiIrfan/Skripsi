package com.doyatama.university.model;

public class Taksonomi {
    private String idTaksonomi;
    private String namaTaksonomi;
    private String deskripsiTaksonomi;
    private School school;

    public Taksonomi() {
    }

    public Taksonomi(String idTaksonomi, String namaTaksonomi, String deskripsiTaksonomi, School school) {
        this.idTaksonomi = idTaksonomi;
        this.namaTaksonomi = namaTaksonomi;
        this.deskripsiTaksonomi = deskripsiTaksonomi;
        this.school = school;
    }

    public String getIdTaksonomi() {
        return idTaksonomi;
    }

    public void setIdTaksonomi(String idTaksonomi) {
        this.idTaksonomi = idTaksonomi;
    }

    public String getNamaTaksonomi() {
        return namaTaksonomi;
    }

    public void setNamaTaksonomi(String namaTaksonomi) {
        this.namaTaksonomi = namaTaksonomi;
    }

    public String getDeskripsiTaksonomi() {
        return deskripsiTaksonomi;
    }

    public void setDeskripsiTaksonomi(String deskripsiTaksonomi) {
        this.deskripsiTaksonomi = deskripsiTaksonomi;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public boolean isValid() {
        return idTaksonomi != null &&
                namaTaksonomi != null &&
                deskripsiTaksonomi != null &&
                school != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idTaksonomi":
                this.idTaksonomi = value;
                break;
            case "namaTaksonomi":
                this.namaTaksonomi = value;
                break;
            case "deskripsiTaksonomi":
                this.deskripsiTaksonomi = value;
                break;
            default:
                throw new IllegalArgumentException("Field name not recognized: " + fieldName);
        }
    }
}
