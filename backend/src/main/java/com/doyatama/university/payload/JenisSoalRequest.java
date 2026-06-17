package com.doyatama.university.payload;

public class JenisSoalRequest {
    private String idJenisSoal;
    private String namaJenisSoal;
    private String idSekolah;

    public JenisSoalRequest() {
    }

    public JenisSoalRequest(String idJenisSoal, String namaJenisSoal, String idSekolah) {
        this.idJenisSoal = idJenisSoal;
        this.namaJenisSoal = namaJenisSoal;
        this.idSekolah = idSekolah;
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

    public String getIdSekolah() {
        return idSekolah;
    }

    public void setIdSekolah(String idSekolah) {
        this.idSekolah = idSekolah;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idJenisSoal":
                this.idJenisSoal = value;
                break;
            case "namaJenisSoal":
                this.namaJenisSoal = value;
                break;
            case "idSekolah":
                this.idSekolah = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
