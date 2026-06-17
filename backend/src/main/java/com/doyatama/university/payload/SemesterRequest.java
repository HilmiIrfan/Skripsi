package com.doyatama.university.payload;

public class SemesterRequest {
    private String idSemester;
    private String namaSemester;
    private String idSekolah;

    public SemesterRequest() {
    }

    public SemesterRequest(String idSemester, String namaSemester, String idSekolah) {
        this.idSemester = idSemester;
        this.namaSemester = namaSemester;
        this.idSekolah = idSekolah;
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

    public String getIdSekolah() {
        return idSekolah;
    }

    public void setIdSekolah(String idSekolah) {
        this.idSekolah = idSekolah;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idSemester":
                this.idSemester = value;
                break;
            case "namaSemester":
                this.namaSemester = value;
                break;
            case "idSekolah":
                this.idSekolah = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
