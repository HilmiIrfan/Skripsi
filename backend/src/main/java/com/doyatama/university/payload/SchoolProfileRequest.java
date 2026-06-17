package com.doyatama.university.payload;


public class SchoolProfileRequest {
    private String id;
    
    private String npsn;
    private String status;
    private String bentukKependidikan;
    private String kepemilikan;
    private String SKPendirianSekolah;
    private String tglSKPendirian;
    private String SKIzinOperasional;
    private String tglSKOperasional;
    private String school_id;

    public SchoolProfileRequest() {
    }

    public SchoolProfileRequest(String id, String npsn, String status, String bentukKependidikan, 
            String kepemilikan, String SKPendirianSekolah, String tglSKPendirian, String SKIzinOperasional, 
            String tglSKOperasional, String school_id) {
        this.id = id;
        this.npsn = npsn;
        this.status = status;
        this.bentukKependidikan = bentukKependidikan;
        this.kepemilikan = kepemilikan;
        this.SKPendirianSekolah = SKPendirianSekolah;
        this.tglSKPendirian = tglSKPendirian;
        this.SKIzinOperasional = SKIzinOperasional;
        this.tglSKOperasional = tglSKOperasional;
        this.school_id = school_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNpsn() {
        return npsn;
    }

    public void setNpsn(String npsn) {
        this.npsn = npsn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBentukKependidikan() {
        return bentukKependidikan;
    }

    public void setBentukKependidikan(String bentukKependidikan) {
        this.bentukKependidikan = bentukKependidikan;
    }

    public String getKepemilikan() {
        return kepemilikan;
    }

    public void setKepemilikan(String kepemilikan) {
        this.kepemilikan = kepemilikan;
    }

    public String getSKPendirianSekolah() {
        return SKPendirianSekolah;
    }

    public void setSKPendirianSekolah(String SKPendirianSekolah) {
        this.SKPendirianSekolah = SKPendirianSekolah;
    }

    public String getTglSKPendirian() {
        return tglSKPendirian;
    }

    public void setTglSKPendirian(String tglSKPendirian) {
        this.tglSKPendirian = tglSKPendirian;
    }

    public String getSKIzinOperasional() {
        return SKIzinOperasional;
    }

    public void setSKIzinOperasional(String SKIzinOperasional) {
        this.SKIzinOperasional = SKIzinOperasional;
    }

    public String getTglSKOperasional() {
        return tglSKOperasional;
    }

    public void setTglSKOperasional(String tglSKOperasional) {
        this.tglSKOperasional = tglSKOperasional;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }
    
    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "id":
                this.id = value;
                break;
            case "npsn":
                this.npsn = value;
                break;
            case "status":
                this.status = value;
                break;
            case "bentukKependidikan":
                this.bentukKependidikan = value;
                break;
            case "kepemilikan":
                this.kepemilikan = value;
                break;
            case "SKPendirianSekolah":
                this.SKPendirianSekolah = value;
                break;
            case "tglSKPendirian":
                this.tglSKPendirian = value;
                break;
            case "SKIzinOperasional":
                this.SKIzinOperasional = value;
                break;
            case "tglSKOperasional":
                this.tglSKOperasional = value;
                break;
            case "school_id":
                this.school_id = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
