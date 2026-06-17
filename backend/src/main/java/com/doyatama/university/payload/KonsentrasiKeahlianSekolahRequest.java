package com.doyatama.university.payload;

public class KonsentrasiKeahlianSekolahRequest {

    private String idKonsentrasiSekolah;
    private String namaKonsentrasiSekolah;
    private String idSekolah;
    private String idKonsentrasiKeahlian;

    public KonsentrasiKeahlianSekolahRequest() {
    }

    public KonsentrasiKeahlianSekolahRequest(String idKonsentrasiSekolah, String namaKonsentrasiSekolah,
            String idSekolah,
            String idKonsentrasiKeahlian) {
        this.idKonsentrasiSekolah = idKonsentrasiSekolah;
        this.namaKonsentrasiSekolah = namaKonsentrasiSekolah;
        this.idSekolah = idSekolah;
        this.idKonsentrasiKeahlian = idKonsentrasiKeahlian;
    }

    public String getIdKonsentrasiSekolah() {
        return idKonsentrasiSekolah;
    }

    public void setIdKonsentrasiSekolah(String idKonsentrasiSekolah) {
        this.idKonsentrasiSekolah = idKonsentrasiSekolah;
    }

    public String getNamaKonsentrasiSekolah() {

        return namaKonsentrasiSekolah;
    }

    public void setNamaKonsentrasiSekolah(String namaKonsentrasiSekolah) {
        this.namaKonsentrasiSekolah = namaKonsentrasiSekolah;
    }

    public String getIdSekolah() {
        return idSekolah;
    }

    public void setIdSekolah(String idSekolah) {
        this.idSekolah = idSekolah;
    }

    public String getIdKonsentrasiKeahlian() {
        return idKonsentrasiKeahlian;
    }

    public void setIdKonsentrasiKeahlian(String idKonsentrasiKeahlian) {
        this.idKonsentrasiKeahlian = idKonsentrasiKeahlian;
    }

    public boolean isValid() {
        return this.idKonsentrasiSekolah != null &&
                this.namaKonsentrasiSekolah != null &&
                this.idSekolah != null &&
                this.idKonsentrasiKeahlian != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idKonsentrasiSekolah":
                this.setIdKonsentrasiSekolah(value);
                break;
            case "namaKonsentrasiSekolah":
                this.setNamaKonsentrasiSekolah(value);
                break;
            case "idSekolah":
                this.setIdSekolah(value);
                break;
            case "idKonsentrasiKeahlian":
                this.setIdKonsentrasiKeahlian(value);
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
