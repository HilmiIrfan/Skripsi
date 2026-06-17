package com.doyatama.university.model;

public class JenisSoal {
    private String idJenisSoal;
    private String namaJenisSoal;
    private School school;

    public JenisSoal() {
    }

    public JenisSoal(String idJenisSoal, String namaJenisSoal, School school) {
        this.idJenisSoal = idJenisSoal;
        this.namaJenisSoal = namaJenisSoal;
        this.school = school;
    }

    public String getIdJenisSoal() {
        return idJenisSoal;
    }

    public void setIdJenisSoal(String idJenisSoal) {
        this.idJenisSoal = idJenisSoal;
    }

    public String getNamaJenisSoal() {
        return namaJenisSoal;
    }

    public void setNamaJenisSoal(String namaJenisSoal) {
        this.namaJenisSoal = namaJenisSoal;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public boolean isValid() {
        return idJenisSoal != null &&
                namaJenisSoal != null &&
                school != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idJenisSoal":
                this.idJenisSoal = value;
                break;
            case "namaJenisSoal":
                this.namaJenisSoal = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
