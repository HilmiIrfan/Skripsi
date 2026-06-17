package com.doyatama.university.model;


public class JadwalPelajaran {
    private String idJadwal;
    private Lecture lecture;
    private String jabatan;
    private Mapel mapel;
    private String jmlJam;

    public JadwalPelajaran() {}

    public JadwalPelajaran(String idJadwal, Lecture lecture, String jabatan, Mapel mapel, String jmlJam) {
        this.idJadwal = idJadwal;
        this.lecture = lecture;
        this.jabatan = jabatan;
        this.mapel = mapel;
        this.jmlJam = jmlJam;
    }

    public String getIdJadwal() {
        return idJadwal;
    }

    public void setIdJadwal(String idJadwal) {
        this.idJadwal = idJadwal;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public Mapel getMapel() {
        return mapel;
    }

    public void setMapel(Mapel mapel) {
        this.mapel = mapel;
    }

    public String getJmlJam() {
        return jmlJam;
    }

    public void setJmlJam(String jmlJam) {
        this.jmlJam = jmlJam;
    }
    
    public boolean isValid() {
        return this.idJadwal != null &&
               this.jabatan != null &&
               this.jmlJam != null;
    }
    
    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idJadwal":
                this.idJadwal = value;
                break;
            case "jabatan":
                this.jabatan = value;  
                break;
            case "jmlJam":
                this.jmlJam = value;  
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
