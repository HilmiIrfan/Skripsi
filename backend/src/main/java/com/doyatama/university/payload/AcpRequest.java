package com.doyatama.university.payload;

public class AcpRequest {
    private String idAcp;
    private String namaAcp;
    private String idMapel;
    private String idTahun;
    private String idSemester;
    private String idKelas;
    private String idKonsentrasiSekolah;
    private String idElemen;
    private String idSchool;

    public AcpRequest() {
    }

    public AcpRequest(String idAcp, String namaAcp, String idMapel, String idTahun, String idSemester, String idKelas,
            String idKonsentrasiSekolah, String idElemen, String idSchool) {
        this.idAcp = idAcp;
        this.namaAcp = namaAcp;
        this.idMapel = idMapel;
        this.idTahun = idTahun;
        this.idSemester = idSemester;
        this.idKelas = idKelas;
        this.idKonsentrasiSekolah = idKonsentrasiSekolah;
        this.idElemen = idElemen;
        this.idSchool = idSchool;
    }

    public String getIdAcp() {
        return idAcp;
    }

    public void setIdAcp(String idAcp) {
        this.idAcp = idAcp;
    }

    public String getNamaAcp() {
        return namaAcp;
    }

    public void setNamaAcp(String namaAcp) {
        this.namaAcp = namaAcp;
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

    public String getIdSchool() {
        return idSchool;
    }

    public void setIdSchool(String idSchool) {
        this.idSchool = idSchool;
    }

    public boolean isValid() {
        return idAcp != null && namaAcp != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idAcp":
                this.idAcp = value;
                break;
            case "namaAcp":
                this.namaAcp = value;
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
            case "idSchool":
                this.idSchool = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
