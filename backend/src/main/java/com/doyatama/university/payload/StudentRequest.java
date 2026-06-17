package com.doyatama.university.payload;

public class StudentRequest {
    private String id;
    private String nisn;
    private String name;
    private String gender;
    private String phone;
    private String religion_id;
    private String bidangKeahlian_id;
    private String programKeahlian_id;
    private String konsentrasiKeahlian_id;
    private String birth_date;
    private String place_born;
    private String address;

    public StudentRequest() {
    }

    public StudentRequest(String id, String nisn, String name, String gender, String phone,
            String religion_id, String bidangKeahlian_id, String programKeahlian_id,
            String konsentrasiKeahlian_id, String birth_date, String place_born, String address) {
        this.id = id;
        this.nisn = nisn;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.religion_id = religion_id;
        this.bidangKeahlian_id = bidangKeahlian_id;
        this.programKeahlian_id = programKeahlian_id;
        this.konsentrasiKeahlian_id = konsentrasiKeahlian_id;
        this.birth_date = birth_date;
        this.place_born = place_born;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNisn() {
        return nisn;
    }

    public void setNisn(String nisn) {
        this.nisn = nisn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReligion_id() {
        return religion_id;
    }

    public void setReligion_id(String religion_id) {
        this.religion_id = religion_id;
    }

    public String getBidangKeahlian_id() {
        return bidangKeahlian_id;
    }

    public void setBidangKeahlian_id(String bidangKeahlian_id) {
        this.bidangKeahlian_id = bidangKeahlian_id;
    }

    public String getProgramKeahlian_id() {
        return programKeahlian_id;
    }

    public void setProgramKeahlian_id(String programKeahlian_id) {
        this.programKeahlian_id = programKeahlian_id;
    }

    public String getKonsentrasiKeahlian_id() {
        return konsentrasiKeahlian_id;
    }

    public void setKonsentrasiKeahlian_id(String konsentrasiKeahlian_id) {
        this.konsentrasiKeahlian_id = konsentrasiKeahlian_id;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getPlace_born() {
        return place_born;
    }

    public void setPlace_born(String place_born) {
        this.place_born = place_born;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "id":
                this.id = value;
                break;
            case "nisn":
                this.nisn = value;
                break;
            case "name":
                this.name = value;
                break;
            case "gender":
                this.gender = value;
                break;
            case "phone":
                this.phone = value;
                break;
            case "birth_date":
                this.birth_date = value;
                break;
            case "place_born":
                this.place_born = value;
                break;
            case "address":
                this.address = value;
                break;
            case "religion_id":
                this.religion_id = value;
                break;
            case "bidangKeahlian_id":
                this.bidangKeahlian_id = value;
                break;
            case "programKeahlian_id":
                this.programKeahlian_id = value;
                break;
            case "konsentrasiKeahlian_id":
                this.konsentrasiKeahlian_id = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
