package com.doyatama.university.payload;

public class ProgramKeahlianSekolahRequest {

    private String idProgramSekolah;
    private String namaProgramSekolah;
    private String idSekolah;
    private String idProgramKeahlian;

    public ProgramKeahlianSekolahRequest() {
    }

    public ProgramKeahlianSekolahRequest(String idProgramSekolah, String namaProgramSekolah, String idSekolah,
            String idProgramKeahlian) {
        this.idProgramSekolah = idProgramSekolah;
        this.namaProgramSekolah = namaProgramSekolah;
        this.idSekolah = idSekolah;
        this.idProgramKeahlian = idProgramKeahlian;
    }

    public String getIdProgramSekolah() {
        return idProgramSekolah;
    }

    public void setIdProgramSekolah(String idProgramSekolah) {
        this.idProgramSekolah = idProgramSekolah;
    }

    public String getNamaProgramSekolah() {

        return namaProgramSekolah;
    }

    public void setNamaProgramSekolah(String namaProgramSekolah) {
        this.namaProgramSekolah = namaProgramSekolah;
    }

    public String getIdSekolah() {
        return idSekolah;
    }

    public void setIdSekolah(String idSekolah) {
        this.idSekolah = idSekolah;
    }

    public String getIdProgramKeahlian() {
        return idProgramKeahlian;
    }

    public void setIdProgramKeahlian(String idProgramKeahlian) {
        this.idProgramKeahlian = idProgramKeahlian;
    }

    public boolean isValid() {
        return this.namaProgramSekolah != null &&
                this.idSekolah != null &&
                this.idProgramKeahlian != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idProgramSekolah":
                this.idProgramSekolah = value;
                break;
            case "namaProgramSekolah":
                this.namaProgramSekolah = value;
                break;
            case "idSekolah":
                this.idSekolah = value;
                break;
            case "idProgramKeahlian":
                this.idProgramKeahlian = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }

}
