package com.doyatama.university.payload;

public class KonsentrasiKeahlianRequest {
    
    private String id;
    private String konsentrasi; 
    private String programKeahlian_id;
    
    public KonsentrasiKeahlianRequest() {
    }
    
    public KonsentrasiKeahlianRequest(String id, String konsentrasi, String programKeahlian_id) {
        this.id = id;
        this.konsentrasi = konsentrasi;
        this.programKeahlian_id = programKeahlian_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKonsentrasi() {
        return konsentrasi;
    }

    public void setKonsentrasi(String konsentrasi) {
        this.konsentrasi = konsentrasi;
    }

    public String getProgramKeahlian_id() {
        return programKeahlian_id;
    }

    public void setProgramKeahlian_id(String programKeahlian_id) {
        this.programKeahlian_id = programKeahlian_id;
    }

    public boolean isValid() {
        return this.id != null && this.konsentrasi != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "id":
                this.id = value;
                break;
            case "konsentrasi":
                this.konsentrasi = value;  
                break;
            case "programKeahlian_id":
                this.programKeahlian_id = value;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
