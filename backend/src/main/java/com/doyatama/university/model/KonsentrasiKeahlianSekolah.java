package com.doyatama.university.model;

public class KonsentrasiKeahlianSekolah {
    private String idKonsentrasiSekolah;
    private String namaKonsentrasiSekolah;
    private School school;
    private KonsentrasiKeahlian konsentrasiKeahlian;

    public KonsentrasiKeahlianSekolah() {
    }

    public KonsentrasiKeahlianSekolah(String idKonsentrasiSekolah, String namaKonsentrasiSekolah, School school,
            KonsentrasiKeahlian konsentrasiKeahlian) {
        this.idKonsentrasiSekolah = idKonsentrasiSekolah;
        this.namaKonsentrasiSekolah = namaKonsentrasiSekolah;
        this.school = school;
        this.konsentrasiKeahlian = konsentrasiKeahlian;
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

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public KonsentrasiKeahlian getKonsentrasiKeahlian() {
        return konsentrasiKeahlian;
    }

    public void setKonsentrasiKeahlian(KonsentrasiKeahlian konsentrasiKeahlian) {
        this.konsentrasiKeahlian = konsentrasiKeahlian;
    }

    public boolean isValid() {
        return this.idKonsentrasiSekolah != null &&
                this.namaKonsentrasiSekolah != null &&
                this.school != null &&
                this.konsentrasiKeahlian != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idKonsentrasiSekolah":
                this.idKonsentrasiSekolah = value;
                break;
            case "namaKonsentrasiSekolah":
                this.namaKonsentrasiSekolah = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
