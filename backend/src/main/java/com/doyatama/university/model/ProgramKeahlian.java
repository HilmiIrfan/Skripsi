package com.doyatama.university.model;




public class ProgramKeahlian {
    

    private String id;
    private String program; 
    private BidangKeahlian bidangKeahlian;
    
    public ProgramKeahlian() {
    }
    
    public ProgramKeahlian(String id, String program, BidangKeahlian bidangKeahlian){
        this.id = id;
        this.program = program;
        this.bidangKeahlian = bidangKeahlian;
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

    public BidangKeahlian getBidangKeahlian() {
        return bidangKeahlian;
    }

    public void setBidangKeahlian(BidangKeahlian bidangKeahlian) {
        this.bidangKeahlian = bidangKeahlian;
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
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
