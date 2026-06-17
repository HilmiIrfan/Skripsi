package com.doyatama.university.model;

public class Mapel {
    private String idMapel;
    private String name;
    private School school;
    private Kelas kelas;
    private Semester semester;
    private TahunAjaran tahunAjaran;

    public Mapel() {
    }

    public Mapel(String idMapel, String name, School school, Kelas kelas, Semester semester, TahunAjaran tahunAjaran) {
        this.idMapel = idMapel;
        this.name = name;
        this.school = school;
        this.kelas = kelas;
        this.semester = semester;
        this.tahunAjaran = tahunAjaran;
    }

    public String getIdMapel() {
        return idMapel;
    }

    public void setIdMapel(String idMapel) {
        this.idMapel = idMapel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Kelas getKelas() {
        return kelas;
    }

    public void setKelas(Kelas kelas) {
        this.kelas = kelas;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public TahunAjaran getTahunAjaran() {
        return tahunAjaran;
    }

    public void setTahunAjaran(TahunAjaran tahunAjaran) {
        this.tahunAjaran = tahunAjaran;
    }

    public boolean isValid() {
        return this.idMapel != null &&
                this.name != null &&
                this.school != null &&
                this.kelas != null &&
                this.semester != null &&
                this.tahunAjaran != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idMapel":
                this.idMapel = value;
                break;
            case "name":
                this.name = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }

}
