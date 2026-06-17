package com.doyatama.university.payload;

public class ModulRequest {
    private String idModul;
    private String namaModul;
    private String idTahun;
    private String idSemester;
    private String idKelas;
    private String idMapel;
    private String idSekolah;

    public ModulRequest() {
    }

    public ModulRequest(String idModul, String namaModul, String idTahun, String idSemester,
            String idKelas, String idMapel, String idSekolah) {
        this.idModul = idModul;
        this.namaModul = namaModul;
        this.idTahun = idTahun;
        this.idSemester = idSemester;
        this.idKelas = idKelas;
        this.idMapel = idMapel;
        this.idSekolah = idSekolah;
    }

    public String getIdModul() {
        return idModul;
    }

    public void setIdModul(String idModul) {
        this.idModul = idModul;
    }

    public String getNamaModul() {
        return namaModul;
    }

    public void setNamaModul(String namaModul) {
        this.namaModul = namaModul;
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

    public String getIdMapel() {
        return idMapel;
    }

    public void setIdMapel(String idMapel) {
        this.idMapel = idMapel;
    }

    public String getIdSekolah() {
        return idSekolah;
    }

    public void setIdSekolah(String idSekolah) {
        this.idSekolah = idSekolah;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idModul":
                this.idModul = value;
                break;
            case "namaModul":
                this.namaModul = value;
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
            case "idMapel":
                this.idMapel = value;
                break;
            case "idSekolah":
                this.idSekolah = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
