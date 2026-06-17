package com.doyatama.university.payload;

public class TaksonomiRequest {
    private String idTaksonomi;
    private String namaTaksonomi;
    private String deskripsiTaksonomi;
    private String idSekolah;

    public TaksonomiRequest() {
    }

    public TaksonomiRequest(String idTaksonomi, String namaTaksonomi, String deskripsiTaksonomi, String idSekolah) {
        this.idTaksonomi = idTaksonomi;
        this.namaTaksonomi = namaTaksonomi;
        this.deskripsiTaksonomi = deskripsiTaksonomi;
        this.idSekolah = idSekolah;
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

    public String getIdSekolah() {
        return idSekolah;
    }

    public void setIdSekolah(String idSekolah) {
        this.idSekolah = idSekolah;
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
            case "idSekolah":
                this.idSekolah = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
