package com.doyatama.university.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Lecture {
    private String id;
    private String nip;
    private String name;
    private String place_born;
    private String date_born;
    private String gender;
    private String status;
    private String address;
    private String phone;
    
    private Religion religion;
    private BidangKeahlian bidangKeahlian;
    private ProgramKeahlian programKeahlian;
    private KonsentrasiKeahlian konsentrasiKeahlian;
    public Lecture() {
    }

    public Lecture(String id, String nip, String name, String place_born, String date_born, 
            String gender, String status, String address, String phone, Religion religion, 
            BidangKeahlian bidangKeahlian, ProgramKeahlian programKeahlian, KonsentrasiKeahlian konsentrasiKeahlian) {
        this.id = id;
        this.nip = nip;
        this.name = name;
        this.place_born = place_born;
        this.date_born = date_born;
        this.gender = gender;
        this.status = status;
        this.address = address;
        this.phone = phone;
        this.religion = religion;
        this.bidangKeahlian = bidangKeahlian;
        this.programKeahlian = programKeahlian;
        this.konsentrasiKeahlian = konsentrasiKeahlian;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace_born() {
        return place_born;
    }

    public void setPlace_born(String place_born) {
        this.place_born = place_born;
    }

    public String getDate_born() {
        return date_born;
    }

    public void setDate_born(String date_born) {
        this.date_born = date_born;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Religion getReligion() {
        return religion;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public BidangKeahlian getBidangKeahlian() {
        return bidangKeahlian;
    }

    public void setBidangKeahlian(BidangKeahlian bidangKeahlian) {
        this.bidangKeahlian = bidangKeahlian;
    }

    public ProgramKeahlian getProgramKeahlian() {
        return programKeahlian;
    }

    public void setProgramKeahlian(ProgramKeahlian programKeahlian) {
        this.programKeahlian = programKeahlian;
    }

    public KonsentrasiKeahlian getKonsentrasiKeahlian() {
        return konsentrasiKeahlian;
    }

    public void setKonsentrasiKeahlian(KonsentrasiKeahlian konsentrasiKeahlian) {
        this.konsentrasiKeahlian = konsentrasiKeahlian;
    }

    

    public boolean isValid() {
        return this.id != null && 
               this.nip != null && 
               this.name != null && 
               this.place_born != null && 
               this.date_born != null && 
               this.gender != null && 
               this.status != null && 
               this.address != null && 
               this.phone != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "id":
                this.id = value;
                break;
            case "nip":
                this.nip = value;
                break;
            case "name":
                this.name = value;
                break;
            case "place_born":
                this.place_born = value;
                break;
            case "date_born":
                this.date_born = value;
                break;
            case "gender":
                this.gender = value;
                break;
            case "status":
                this.status = value;
                break;
            case "address":
                this.address = value;
                break;
            case "phone":
                this.phone = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
