package com.doyatama.university.payload;

public class BidangKeahlianSekolahRequest {

    private String idBidangSekolah;
    private String namaBidangSekolah;
    private String idSekolah;
    private String idBidangKeahlian;

    public BidangKeahlianSekolahRequest() {
    }

    public BidangKeahlianSekolahRequest(String idBidangSekolah, String namaBidangSekolah, String idSekolah,
            String idBidangKeahlian) {
        this.idBidangSekolah = idBidangSekolah;
        this.namaBidangSekolah = namaBidangSekolah;
        this.idSekolah = idSekolah;
        this.idBidangKeahlian = idBidangKeahlian;
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

    public String getIdSekolah() {
        return idSekolah;
    }

    public void setIdSekolah(String idSekolah) {
        this.idSekolah = idSekolah;
    }

    public String getIdBidangKeahlian() {
        return idBidangKeahlian;
    }

    public void setIdBidangKeahlian(String idBidangKeahlian) {
        this.idBidangKeahlian = idBidangKeahlian;
    }

    public boolean isValid() {
        return this.idBidangSekolah != null &&
                this.namaBidangSekolah != null &&
                this.idSekolah != null &&
                this.idBidangKeahlian != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idBidangSekolah":
                this.idBidangSekolah = value;
                break;
            case "namaBidangSekolah":
                this.namaBidangSekolah = value;
                break;
            case "idSekolah":
                this.idSekolah = value;
                break;
            case "idBidangKeahlian":
                this.idBidangKeahlian = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
