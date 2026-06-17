package com.doyatama.university.model;

import java.util.ArrayList;
import java.util.List;


public class Season {
    private String idSeason;
    private BidangKeahlian bidangKeahlian;
    private ProgramKeahlian programKeahlian;
    private KonsentrasiKeahlian konsentrasiKeahlian;
    private Kelas kelas;
    private Lecture lecture;
    private TahunAjaran tahunAjaran;
    //private JadwalPelajaran jadwalPelajaran;
     private List<Student> student;
     private List<JadwalPelajaran> jadwalPelajaran = new ArrayList<>();

    public Season() {

    }
    
    

    public Season(String idSeason, BidangKeahlian bidangKeahlian, ProgramKeahlian programKeahlian, 
            KonsentrasiKeahlian konsentrasiKeahlian, Kelas kelas, Lecture lecture, TahunAjaran tahunAjaran, 
       // JadwalPelajaran jadwalPelajaran
            List<Student> student,
     List<JadwalPelajaran> jadwalPelajaran
    ) {
        this.idSeason = idSeason;
        this.bidangKeahlian = bidangKeahlian;
        this.programKeahlian = programKeahlian;
        this.konsentrasiKeahlian = konsentrasiKeahlian;
        this.kelas = kelas;
        this.lecture = lecture;
        this.student = student;
        this.jadwalPelajaran = jadwalPelajaran;
    }

    public String getIdSeason() {
        return idSeason;
    }

    public void setIdSeason(String idSeason) {
        this.idSeason = idSeason;
    }

    public BidangKeahlian getBidangKeahlian() {
        return bidangKeahlian;
    }

    public void setBidangKeahlian(BidangKeahlian bidangKeahlian) {
        this.bidangKeahlian = bidangKeahlian;
    }

    public ProgramKeahlian getProgramKeahlian() {
        return programKeahlian;
    }

    public void setProgramKeahlian(ProgramKeahlian programKeahlian) {
        this.programKeahlian = programKeahlian;
    }

    public KonsentrasiKeahlian getKonsentrasiKeahlian() {
        return konsentrasiKeahlian;
    }

    public void setKonsentrasiKeahlian(KonsentrasiKeahlian konsentrasiKeahlian) {
        this.konsentrasiKeahlian = konsentrasiKeahlian;
    }

    public Kelas getKelas() {
        return kelas;
    }

    public void setKelas(Kelas kelas) {
        this.kelas = kelas;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    public TahunAjaran getTahunAjaran() {
        return tahunAjaran;
    }

    public void setTahunAjaran(TahunAjaran tahunAjaran) {
        this.tahunAjaran = tahunAjaran;
    }

    public List<JadwalPelajaran> getJadwalPelajaran() {
        return jadwalPelajaran;
    }

    public void setJadwalPelajaran(List<JadwalPelajaran> jadwalPelajaran) {
        this.jadwalPelajaran = jadwalPelajaran;
    }
    
    public void addJadwalPelajaran(JadwalPelajaran jadwal) {
        this.jadwalPelajaran.add(jadwal);
    }

    public List<Student> getStudent() {
        return student;
    }

    public void setStudent(List<Student> student) {
        this.student = student;
    }
    
    public void addStudent(Student student) {
        this.student.add(student);
    }

    
    public boolean isValid() {
        return this.idSeason != null;
    }
    
    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idSeason":
                this.idSeason = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
