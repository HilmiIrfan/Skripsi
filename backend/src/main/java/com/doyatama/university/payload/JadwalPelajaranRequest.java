/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doyatama.university.payload;

/**
 *
 * @author Nifan
 */
public class JadwalPelajaranRequest {
    private String idJadwal;
    private String lecture_id;
    private String jabatan;
    private String mapel_id;
    private String jmlJam;

  
    
    public JadwalPelajaranRequest() {}
    public JadwalPelajaranRequest(String idJadwal, String lecture_id, String jabatan, String mapel_id, String jmlJam) {
        this.idJadwal = idJadwal;
        this.lecture_id = lecture_id;
        this.jabatan = jabatan;
        this.mapel_id = mapel_id;
        this.jmlJam = jmlJam;
    }

    public String getIdJadwal() {
        return idJadwal;
    }

    public void setIdJadwal(String idJadwal) {
        this.idJadwal = idJadwal;
    }

    public String getLecture_id() {
        return lecture_id;
    }

    public void setLecture_id(String lecture_id) {
        this.lecture_id = lecture_id;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getMapel_id() {
        return mapel_id;
    }

    public void setMapel_id(String mapel_id) {
        this.mapel_id = mapel_id;
    }

    public String getJmlJam() {
        return jmlJam;
    }

    public void setJmlJam(String jmlJam) {
        this.jmlJam = jmlJam;
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
            case "mapel_id":
                this.mapel_id = value;  
                break;
            case "lecture_id":
                this.lecture_id = value;  
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
