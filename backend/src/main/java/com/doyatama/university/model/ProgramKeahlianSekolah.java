package com.doyatama.university.model;

public class ProgramKeahlianSekolah {

    private String idProgramSekolah;
    private String namaProgramSekolah;
    private School school;
    private ProgramKeahlian programKeahlian;

    public ProgramKeahlianSekolah() {
    }

    public ProgramKeahlianSekolah(String idProgramSekolah, String namaProgramSekolah, School school,
            ProgramKeahlian programKeahlian) {
        this.idProgramSekolah = idProgramSekolah;
        this.namaProgramSekolah = namaProgramSekolah;
        this.school = school;
        this.programKeahlian = programKeahlian;
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

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public ProgramKeahlian getProgramKeahlian() {
        return programKeahlian;
    }

    public void setProgramKeahlian(ProgramKeahlian programKeahlian) {
        this.programKeahlian = programKeahlian;
    }

    public boolean isValid() {
        return this.idProgramSekolah != null &&
                this.namaProgramSekolah != null &&
                this.school != null &&
                this.programKeahlian != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idProgramSekolah":
                this.idProgramSekolah = value;
                break;
            case "namaProgramSekolah":
                this.namaProgramSekolah = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }

}
