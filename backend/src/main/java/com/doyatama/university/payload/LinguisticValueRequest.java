package com.doyatama.university.payload;

public class LinguisticValueRequest {
    private String name;
    private float value1;
    private float value2;
    private float value3;
    private float value4;
    private float avg;
    private String average;

    public LinguisticValueRequest() {
    }

    public LinguisticValueRequest(String name, float value1, float value2, float value3, float value4) {
        this.name = name;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.avg = (value1 + value2 + value3 + value4) / 4;

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
        this.avg = (value1 + value2 + value3 + value4) / 4;
    }

    public String getAvarage() {
        float average = (this.value1 + this.value2 + this.value3 + this.value4) / 4.0f;
        return Float.toString(average);
    }
    // public void setAvarage(String average) {
    // this.average = average;
    // }

    public void set(String fieldName, String value) {
        switch (fieldName) {
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

            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }

}
