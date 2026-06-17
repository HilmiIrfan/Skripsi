package com.doyatama.university.payload;

import com.doyatama.university.model.RPSDetail;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

public class ExamRequest {
    private String name;
    private String description;
    private List<String> questions;
    private String rps_id;
    private Integer min_grade;
    private Integer duration;
    private Instant date_start;
    private Instant date_end;
    private String type_exercise;
    private RPSDetail rps_detail_id;

    public ExamRequest() {
    }

    public ExamRequest(String name, String description, List<String> questions, String rps_id, Integer min_grade, Integer duration, Instant date_start, Instant date_end, String type_exercise, String rps_detail_id) {
        this.name = name;
        this.description = description;
        this.questions = questions;
        this.rps_id = rps_id;
        this.min_grade = min_grade;
        this.duration = duration;
        this.date_start = date_start;
        this.date_end = date_end;
        this.type_exercise = type_exercise;
    }
     public RPSDetail getRpsDetail() {
        return this.rps_detail_id;
    }


   
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    public String getRps_id() {
        return rps_id;
    }

    public void setRps_id(String rps_id) {
        this.rps_id = rps_id;
    }

    public Integer getMin_grade() {
        return min_grade;
    }

    public void setMin_grade(Integer min_grade) {
        this.min_grade = min_grade;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Instant getDate_start() {
        return date_start;
    }

    public void setDate_start(Instant date_start) {
        this.date_start = date_start;
    }

    public Instant getDate_end() {
        return date_end;
    }

    public void setDate_end(Instant date_end) {
        this.date_end = date_end;
    }

    public String getType_exercise() {
        return type_exercise;
    }

    public void setType_exercise(String type_exercise) {
        this.type_exercise = type_exercise;
    }


    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "name":
                this.name = value;
                break;
            case "description":
                this.description = value;
                break;
            case "questions":
                this.questions = Collections.singletonList(value);
                break;
            case "rps_id":
                this.rps_id = value;
                break;
            case "min_grade":
                this.min_grade = Integer.parseInt(value);
                break;
            case "duration":
                this.duration = Integer.parseInt(value);
                break;
            case "date_start":
                this.date_start = Instant.parse(value);
                break;
            case "date_end":
                this.date_end = Instant.parse(value);
                break;
            case "type_exercise":
                this.type_exercise = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
