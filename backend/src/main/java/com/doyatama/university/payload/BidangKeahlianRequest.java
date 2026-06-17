package com.doyatama.university.payload;

public class BidangKeahlianRequest {

    private String id;
    private String bidang;

    public BidangKeahlianRequest() {
    }

    public BidangKeahlianRequest(String id, String bidang) {
        this.id = id;
        this.bidang = bidang;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBidang() {
        return bidang;
    }

    public void setBidang(String bidang) {
        this.bidang = bidang;
    }

    public boolean isValid() {
        return this.id != null && this.bidang != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "id":
                this.id = value;
                break;
            case "bidang":
                this.bidang = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
