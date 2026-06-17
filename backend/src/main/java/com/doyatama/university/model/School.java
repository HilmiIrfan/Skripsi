package com.doyatama.university.model;

public class School {
    private String idSchool;
    private String nameSchool;
    private String address;

    public School() {
    }

    public School(String idSchool, String nameSchool, String address) {
        this.idSchool = idSchool;
        this.nameSchool = nameSchool;
        this.address = address;
    }

    public String getIdSchool() {
        return idSchool;
    }

    public void setIdSchool(String idSchool) {
        this.idSchool = idSchool;
    }

    public String getNameSchool() {
        return nameSchool;
    }

    public void setNameSchool(String nameSchool) {
        this.nameSchool = nameSchool;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isValid() {
        return this.idSchool != null &&
                this.nameSchool != null &&
                this.address != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idSchool":
                this.idSchool = value;
                break;
            case "nameSchool":
                this.nameSchool = value;
                break;
            case "address":
                this.address = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
