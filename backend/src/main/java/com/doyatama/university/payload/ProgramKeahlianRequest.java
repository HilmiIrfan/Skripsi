package com.doyatama.university.payload;

public class ProgramKeahlianRequest {
    
    private String id;
    private String program; 
    private String bidangKeahlian_id;
    
    public ProgramKeahlianRequest() {
    }
    
    public ProgramKeahlianRequest(String id, String program, String bidangKeahlian_id) {
        this.id = id;
        this.program = program;
        this.bidangKeahlian_id = bidangKeahlian_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getBidangKeahlian_id() {
        return bidangKeahlian_id;
    }

    public void setBidangKeahlian_id(String bidangKeahlian_id) {
        this.bidangKeahlian_id = bidangKeahlian_id;
    }

    public boolean isValid() {
        return this.id != null && this.program != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "id":
                this.id = value;
                break;
            case "program":
                this.program = value;  
                break;
            case "bidangKeahlian_id":
                this.bidangKeahlian_id = value;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
