package com.doyatama.university.payload;

import java.util.List;

public class SeasonRequest {
    private String idSeason;
    private String bidangKeahlian_id;
    private String programKeahlian_id;
    private String konsentrasiKeahlian_id;
    private String kelas_id;
    private String lecture_id;
    private String tahunAjaran_id;
    // private String jadwalPelajaran_id;
    private List<String> student_id;
    private List<String> jadwalPelajaran_id;

    public SeasonRequest() {
    }

    public SeasonRequest(String idSeason, String bidangKeahlian_id, String programKeahlian_id,
            String konsentrasiKeahlian_id, String kelas_id, String lecture_id,
            String tahunAjaran_id,
            // String jadwalPelajaran_id

            List<String> student_id,
            List<String> jadwalPelajaran_id) {
        this.idSeason = idSeason;
        this.bidangKeahlian_id = bidangKeahlian_id;
        this.programKeahlian_id = programKeahlian_id;
        this.konsentrasiKeahlian_id = konsentrasiKeahlian_id;
        this.kelas_id = kelas_id;
        this.lecture_id = lecture_id;
        this.tahunAjaran_id = tahunAjaran_id;
        this.student_id = student_id;
        this.jadwalPelajaran_id = jadwalPelajaran_id;
    }

    public String getIdSeason() {
        return idSeason;
    }

    public void setIdSeason(String idSeason) {
        this.idSeason = idSeason;
    }

    public String getBidangKeahlian_id() {
        return bidangKeahlian_id;
    }

    public void setBidangKeahlian_id(String bidangKeahlian_id) {
        this.bidangKeahlian_id = bidangKeahlian_id;
    }

    public String getProgramKeahlian_id() {
        return programKeahlian_id;
    }

    public void setProgramKeahlian_id(String programKeahlian_id) {
        this.programKeahlian_id = programKeahlian_id;
    }

    public String getKonsentrasiKeahlian_id() {
        return konsentrasiKeahlian_id;
    }

    public void setKonsentrasiKeahlian_id(String konsentrasiKeahlian_id) {
        this.konsentrasiKeahlian_id = konsentrasiKeahlian_id;
    }

    public String getKelas_id() {
        return kelas_id;
    }

    public void setKelas_id(String kelas_id) {
        this.kelas_id = kelas_id;
    }

    public String getLecture_id() {
        return lecture_id;
    }

    public void setLecture_id(String lecture_id) {
        this.lecture_id = lecture_id;
    }

    public String getTahunAjaran_id() {
        return tahunAjaran_id;
    }

    public void setTahunAjaran_id(String tahunAjaran_id) {
        this.tahunAjaran_id = tahunAjaran_id;
    }

    public List<String> getJadwalPelajaran_id() {
        return jadwalPelajaran_id;
    }

    public void setJadwalPelajaran_id(List<String> jadwalPelajaran_id) {
        this.jadwalPelajaran_id = jadwalPelajaran_id;
    }

    public List<String> getStudent_id() {
        return student_id;
    }

    public void setStudent_id(List<String> student_id) {
        this.student_id = student_id;
    }

    // public String getJadwalPelajaran_id() {
    // return jadwalPelajaran_id;
    // }
    //
    // public void setJadwalPelajaran_id(String jadwalPelajaran_id) {
    // this.jadwalPelajaran_id = jadwalPelajaran_id;
    // }

    // Metode set dengan penambahan lecture_id, student_id, dan jadwalPelajaran_id
    public void set(String fieldName, Object value) {
        switch (fieldName) {
            case "idSeason":
                this.idSeason = (String) value;
                break;
            case "bidangKeahlian_id":
                this.bidangKeahlian_id = (String) value;
                break;
            case "programKeahlian_id":
                this.programKeahlian_id = (String) value;
                break;
            case "konsentrasiKeahlian_id":
                this.konsentrasiKeahlian_id = (String) value;
                break;
            case "kelas_id":
                this.kelas_id = (String) value;
                break;
            case "lecture_id":
                this.lecture_id = (String) value;
                break;
            case "tahunAjaran_id":
                this.tahunAjaran_id = (String) value;
                break;
            // case "student_id":
            // this.student_id = (List<String>) value;
            // break;
            // case "jadwalPelajaran_id":
            // this.jadwalPelajaran_id = (List<String>) value;
            // break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
