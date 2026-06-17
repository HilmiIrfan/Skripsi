/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doyatama.university.payload;

/**
 *
 * @author Nifan
 */
public class TahunAjaranRequest {
    private String idTahun;
    private String tahunAjaran;
    private String idSekolah;

    public TahunAjaranRequest() {
    }

    public TahunAjaranRequest(String idTahun, String tahunAjaran, String idSekolah) {
        this.idTahun = idTahun;
        this.tahunAjaran = tahunAjaran;
        this.idSekolah = idSekolah;
    }

    public String getIdTahun() {
        return idTahun;
    }

    public void setIdTahun(String idTahun) {
        this.idTahun = idTahun;
    }

    public String getTahunAjaran() {
        return tahunAjaran;
    }

    public void setTahunAjaran(String tahunAjaran) {
        this.tahunAjaran = tahunAjaran;
    }

    public String getIdSekolah() {
        return idSekolah;
    }

    public void setIdSekolah(String idSekolah) {
        this.idSekolah = idSekolah;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idTahun":
                this.idTahun = value;
                break;
            case "tahunAjaran":
                this.tahunAjaran = value;
                break;
            case "idSekolah":
                this.idSekolah = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
