package com.doyatama.university.payload;

public class ElemenRequest {
    private String idElemen;
    private String namaElemen;
    private String idMapel;
    private String idTahun;
    private String idSemester;
    private String idKelas;
    private String idKonsentrasiSekolah;
    private String idSekolah;

    public ElemenRequest() {
    }

    public ElemenRequest(String idElemen, String namaElemen, String idMapel, String idTahun, String idSemester,
            String idKelas, String idKonsentrasiSekolah, String idSekolah) {
        this.idElemen = idElemen;
        this.namaElemen = namaElemen;
        this.idMapel = idMapel;
        this.idTahun = idTahun;
        this.idSemester = idSemester;
        this.idKelas = idKelas;
        this.idKonsentrasiSekolah = idKonsentrasiSekolah;
        this.idSekolah = idSekolah;
    }

    public String getIdElemen() {
        return idElemen;
    }

    public void setIdElemen(String idElemen) {
        this.idElemen = idElemen;
    }

    public String getNamaElemen() {

        return namaElemen;
    }

    public void setNamaElemen(String namaElemen) {
        this.namaElemen = namaElemen;
    }

    public String getIdMapel() {
        return idMapel;
    }

    public void setIdMapel(String idMapel) {
        this.idMapel = idMapel;
    }

    public String getIdTahun() {
        return idTahun;
    }

    public void setIdTahun(String idTahun) {
        this.idTahun = idTahun;
    }

    public String getIdSemester() {
        return idSemester;
    }

    public void setIdSemester(String idSemester) {
        this.idSemester = idSemester;
    }

    public String getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(String idKelas) {
        this.idKelas = idKelas;
    }

    public String getIdKonsentrasiSekolah() {
        return idKonsentrasiSekolah;
    }

    public void setIdKonsentrasiSekolah(String idKonsentrasiSekolah) {
        this.idKonsentrasiSekolah = idKonsentrasiSekolah;
    }

    public String getIdSekolah() {
        return idSekolah;
    }

    public void setIdSekolah(String idSekolah) {
        this.idSekolah = idSekolah;
    }

    public boolean isValid() {
        return this.idElemen != null && this.namaElemen != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idElemen":
                this.idElemen = value;
                break;
            case "namaElemen":
                this.namaElemen = value;
                break;
            case "idMapel":
                this.idMapel = value;
                break;
            case "idTahun":
                this.idTahun = value;
                break;
            case "idSemester":
                this.idSemester = value;
                break;
            case "idKelas":
                this.idKelas = value;
                break;
            case "idKonsentrasiSekolah":
                this.idKonsentrasiSekolah = value;
                break;
            case "idSekolah":
                this.idSekolah = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }

}