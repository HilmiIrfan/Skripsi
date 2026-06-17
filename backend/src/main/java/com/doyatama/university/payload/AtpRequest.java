package com.doyatama.university.payload;

public class AtpRequest {
    private String idAtp;
    private String namaAtp;
    private String jumlahJpl;
    private String idMapel;
    private String idTahun;
    private String idSemester;
    private String idKelas;
    private String idKonsentrasiSekolah;
    private String idElemen;
    private String idAcp;
    private String idSekolah;

    public AtpRequest() {
    }

    public AtpRequest(String idAtp, String namaAtp, String jumlahJpl, String idMapel, String idTahun, String idSemester,
            String idKelas,
            String idKonsentrasiSekolah, String idElemen, String idAcp, String idSekolah) {
        this.idAtp = idAtp;
        this.namaAtp = namaAtp;
        this.jumlahJpl = jumlahJpl;
        this.idMapel = idMapel;
        this.idTahun = idTahun;
        this.idSemester = idSemester;
        this.idKelas = idKelas;
        this.idKonsentrasiSekolah = idKonsentrasiSekolah;
        this.idElemen = idElemen;
        this.idAcp = idAcp;
        this.idSekolah = idSekolah;
    }

    public String getIdAtp() {
        return idAtp;
    }

    public void setIdAtp(String idAtp) {
        this.idAtp = idAtp;
    }

    public String getNamaAtp() {
        return namaAtp;
    }

    public void setNamaAtp(String namaAtp) {
        this.namaAtp = namaAtp;
    }

    public String getJumlahJpl() {
        return jumlahJpl;
    }

    public void setJumlahJpl(String jumlahJpl) {
        this.jumlahJpl = jumlahJpl;
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

    public String getIdElemen() {
        return idElemen;
    }

    public void setIdElemen(String idElemen) {
        this.idElemen = idElemen;
    }

    public String getIdAcp() {
        return idAcp;
    }

    public void setIdAcp(String idAcp) {
        this.idAcp = idAcp;
    }

    public String getIdSekolah() {
        return idSekolah;
    }

    public void setIdSekolah(String idSekolah) {
        this.idSekolah = idSekolah;
    }

    public boolean isValid() {
        return this.idAtp != null && this.namaAtp != null && this.jumlahJpl != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idAtp":
                this.idAtp = value;
                break;
            case "namaAtp":
                this.namaAtp = value;
                break;
            case "jumlahJpl":
                this.jumlahJpl = value;
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
            case "idElemen":
                this.idElemen = value;
                break;
            case "idAcp":
                this.idAcp = value;
                break;
            case "idSekolah":
                this.idSekolah = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
