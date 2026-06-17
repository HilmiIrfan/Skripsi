package com.doyatama.university.model;

public class TahunAjaran {
    private String idTahun;
    private String tahunAjaran;
    private School school;

    public TahunAjaran() {
    }

    public TahunAjaran(String idTahun, String tahunAjaran, School school) {
        this.idTahun = idTahun;
        this.tahunAjaran = tahunAjaran;
        this.school = school;
    }

    public String getIdTahun() {
        return idTahun;
    }

    public void setIdTahun(String idTahun) {
        this.idTahun = idTahun;
    }

    public String getTahunAjaran() {
        return tahunAjaran;
    }

    public void setTahunAjaran(String tahunAjaran) {
        this.tahunAjaran = tahunAjaran;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public boolean isValid() {
        return this.idTahun != null && this.tahunAjaran != null && this.school != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idTahun":
                this.idTahun = value;
                break;
            case "tahunAjaran":
                this.tahunAjaran = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
