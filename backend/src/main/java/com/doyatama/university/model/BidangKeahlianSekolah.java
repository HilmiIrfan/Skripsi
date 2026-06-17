package com.doyatama.university.model;

public class BidangKeahlianSekolah {

    private String idBidangSekolah;
    private String namaBidangSekolah;
    private School school;
    private BidangKeahlian bidangKeahlian;

    public BidangKeahlianSekolah() {
    }

    public BidangKeahlianSekolah(String idBidangSekolah, String namaBidangSekolah, School school,
            BidangKeahlian bidangKeahlian) {
        this.idBidangSekolah = idBidangSekolah;
        this.namaBidangSekolah = namaBidangSekolah;
        this.school = school;
        this.bidangKeahlian = bidangKeahlian;
    }

    public String getIdBidangSekolah() {
        return idBidangSekolah;
    }

    public void setIdBidangSekolah(String idBidangSekolah) {
        this.idBidangSekolah = idBidangSekolah;
    }

    public String getNamaBidangSekolah() {
        return namaBidangSekolah;
    }

    public void setNamaBidangSekolah(String namaBidangSekolah) {
        this.namaBidangSekolah = namaBidangSekolah;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public BidangKeahlian getBidangKeahlian() {
        return bidangKeahlian;
    }

    public void setBidangKeahlian(BidangKeahlian bidangKeahlian) {
        this.bidangKeahlian = bidangKeahlian;
    }

    public boolean isValid() {
        return this.idBidangSekolah != null &&
                this.namaBidangSekolah != null &&
                this.school != null &&
                this.bidangKeahlian != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idBidangSekolah":
                this.idBidangSekolah = value;
                break;
            case "namaBidangSekolah":
                this.namaBidangSekolah = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }

}
