package com.doyatama.university.model;

/**
 * @author alfa
 */

public class LinguisticValue {
    private String id;
    private String name;
    private float value1;
    private float value2;
    private float value3;
    private float value4;
    private float avg;
    private String average;
    private String file_path;

    private static final int MAX_ID_LENGTH = 6; // replace 16 with your desired maximum length

    // Default constructor
    public LinguisticValue() {
    }

    // Constructor
    public LinguisticValue(String id, String name, float value1, float value2, float value3, float value4,
            String file_path) {
        this.id = id;
        this.name = name;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.file_path = file_path;
        updateAvg();
    }

    // Getters and setters for all fields
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue1() {
        return value1;
    }

    public void setValue1(float value1) {
        this.value1 = value1;
        updateAvg();
    }

    public float getValue2() {
        return value2;
    }

    public void setValue2(float value2) {
        this.value2 = value2;
        updateAvg();
    }

    public float getValue3() {
        return value3;
    }

    public void setValue3(float value3) {
        this.value3 = value3;
        updateAvg();
    }

    public float getValue4() {
        return value4;
    }

    public void setValue4(float value4) {
        this.value4 = value4;
        updateAvg();
    }

    public float getAvg() {
        return avg;
    }

    private void updateAvg() {
        float sum = this.value1 + this.value2 + this.value3 + this.value4;
        this.avg = sum != 0 ? sum / 4.0f : 0;
    }

    public void setAverage(String average) {
        this.avg = Float.parseFloat(average);
    }

    public String getAverage() {
        this.avg = (this.value1 + this.value2 + this.value3 + this.value4) / 4.0f;
        this.average = Float.toString(this.avg);
        return this.average;
    }

    public void setAvarage(String average) {
        this.avg = Float.parseFloat(average);
        this.average = average;

    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public boolean isValid() {
        return this.id != null && this.name != null && this.value1 != 0 && this.value2 != 0 && this.value3 != 0
                && this.value4 != 0;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "id":
                String idValue = String.valueOf(value);
                if (idValue.length() > MAX_ID_LENGTH) {
                    throw new IllegalArgumentException("ID must be no more than " + MAX_ID_LENGTH + " characters long");
                }
            case "name":
                this.name = value;
                break;
            case "value1":
                this.value1 = Float.parseFloat(value);
                break;
            case "value2":
                this.value2 = Float.parseFloat(value);
                break;
            case "value3":
                this.value3 = Float.parseFloat(value);
                break;
            case "value4":
                this.value4 = Float.parseFloat(value);
                break;
            case "file_path":
                this.file_path = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}