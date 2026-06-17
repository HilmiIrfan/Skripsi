package com.doyatama.university.model;

public class Modul {
    private String idModul;
    private String namaModul;
    private TahunAjaran tahunAjaran;
    private Semester semester;
    private Kelas kelas;
    private Mapel mapel;
    private School school;

    public Modul() {
    }

    public Modul(String idModul, String namaModul, TahunAjaran tahunAjaran, Semester semester,
            Kelas kelas, Mapel mapel, School school) {
        this.idModul = idModul;
        this.namaModul = namaModul;
        this.tahunAjaran = tahunAjaran;
        this.semester = semester;
        this.kelas = kelas;
        this.mapel = mapel;
        this.school = school;
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

    public Mapel getMapel() {
        return mapel;
    }

    public void setMapel(Mapel mapel) {
        this.mapel = mapel;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public boolean isValid() {
        return idModul != null &&
                namaModul != null &&
                tahunAjaran != null &&
                semester != null &&
                kelas != null &&
                mapel != null &&
                school != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idModul":
                this.idModul = value;
                break;
            case "namaModul":
                this.namaModul = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }

}
