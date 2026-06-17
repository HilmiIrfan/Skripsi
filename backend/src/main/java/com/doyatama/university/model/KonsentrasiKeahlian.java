package com.doyatama.university.model;

public class KonsentrasiKeahlian {

    private String id;
    private String konsentrasi;
    private ProgramKeahlian programKeahlian;

    public KonsentrasiKeahlian() {
    }

    public KonsentrasiKeahlian(String id, String konsentrasi, ProgramKeahlian programKeahlian) {
        this.id = id;
        this.konsentrasi = konsentrasi;
        this.programKeahlian = programKeahlian;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKonsentrasi() {
        return konsentrasi;
    }

    public void setKonsentrasi(String konsentrasi) {
        this.konsentrasi = konsentrasi;
    }

    public ProgramKeahlian getProgramKeahlian() {
        return programKeahlian;
    }

    public void setProgramKeahlian(ProgramKeahlian programKeahlian) {
        this.programKeahlian = programKeahlian;
    }

    public boolean isValid() {
        return this.id != null && this.konsentrasi != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "id":
                this.id = value;
                break;
            case "konsentrasi":
                this.konsentrasi = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
