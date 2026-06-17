/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doyatama.university.model;

public class Kelas {
    private String idKelas;
    private String namaKelas;
    private School school;

    public Kelas() {
    }

    public Kelas(String idKelas, String namaKelas, School school) {
        this.idKelas = idKelas;
        this.namaKelas = namaKelas;
        this.school = school;
    }

    public String getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(String idKelas) {
        this.idKelas = idKelas;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public boolean isValid() {
        return this.idKelas != null && this.namaKelas != null && this.school != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idKelas":
                this.idKelas = value;
                break;
            case "namaKelas":
                this.namaKelas = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
