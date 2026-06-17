package com.doyatama.university.model;

public class Student {
    private String id;
    private String nisn;
    private String name;
    private String gender;
    private String phone;
    private String birth_date;
    private String place_born;
    private String address;

    private Religion religion;
    private BidangKeahlian bidangKeahlian;
    private ProgramKeahlian programKeahlian;
    private KonsentrasiKeahlian konsentrasiKeahlian;

    public Student() {
    }

    public Student(String id, String nisn, String name, String gender, String phone,
            String birth_date, String place_born, String address, Religion religion,
            BidangKeahlian bidangKeahlian, ProgramKeahlian programKeahlian, KonsentrasiKeahlian konsentrasiKeahlian) {
        this.id = id;
        this.nisn = nisn;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.birth_date = birth_date;
        this.place_born = place_born;
        this.address = address;
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
                this.nisn != null &&
                this.name != null &&
                this.gender != null &&
                this.phone != null &&
                this.religion != null &&
                this.birth_date != null &&
                this.place_born != null &&
                this.address != null;
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
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
