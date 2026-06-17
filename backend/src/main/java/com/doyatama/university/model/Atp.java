package com.doyatama.university.model;

public class Atp {
    private String idAtp;
    private String namaAtp;
    private String jumlahJpl;
    private Mapel mapel;
    private TahunAjaran tahunAjaran;
    private Semester semester;
    private Kelas kelas;
    private KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolah;
    private Elemen elemen;
    private Acp acp;
    private School school;

    public Atp() {
    }

    public Atp(String idAtp, String namaAtp, String jumlahJpl, Mapel mapel, TahunAjaran tahunAjaran, Semester semester,
            Kelas kelas,
            KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolah, Elemen elemen, Acp acp, School school) {
        this.idAtp = idAtp;
        this.namaAtp = namaAtp;
        this.jumlahJpl = jumlahJpl;
        this.mapel = mapel;
        this.tahunAjaran = tahunAjaran;
        this.semester = semester;
        this.kelas = kelas;
        this.konsentrasiKeahlianSekolah = konsentrasiKeahlianSekolah;
        this.elemen = elemen;
        this.acp = acp;
        this.school = school;
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

    public Mapel getMapel() {
        return mapel;
    }

    public void setMapel(Mapel mapel) {
        this.mapel = mapel;
    }

    public TahunAjaran getTahunAjaran() {
        return tahunAjaran;
    }

    public void setTahunAjaran(TahunAjaran tahunAjaran) {
        this.tahunAjaran = tahunAjaran;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public Kelas getKelas() {
        return kelas;
    }

    public void setKelas(Kelas kelas) {
        this.kelas = kelas;
    }

    public KonsentrasiKeahlianSekolah getKonsentrasiKeahlianSekolah() {
        return konsentrasiKeahlianSekolah;
    }

    public void setKonsentrasiKeahlianSekolah(KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolah) {
        this.konsentrasiKeahlianSekolah = konsentrasiKeahlianSekolah;
    }

    public Elemen getElemen() {
        return elemen;
    }

    public void setElemen(Elemen elemen) {
        this.elemen = elemen;
    }

    public Acp getAcp() {
        return acp;
    }

    public void setAcp(Acp acp) {
        this.acp = acp;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
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
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
