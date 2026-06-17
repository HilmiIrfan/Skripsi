package com.doyatama.university.model;

import java.time.Instant;
import java.util.List;

/**
 * @author alfa
 */
public class QuizAnnouncement {
    private String id;
    private String name;
    private String description;
    private List<Question> questions;
    private RPS rps;
    private Integer min_grade;
    private Integer duration;
    private Instant date_start;
    private Instant date_end;
    private Instant created_at;
    private List<String> devLecturerIds;
    private String type_quiz;

    public QuizAnnouncement() {

    }

    public QuizAnnouncement(String id, String name, String description, List<Question> questions, RPS rps,
            Integer min_grade, String message, String type_quiz, Integer duration, Instant date_start, Instant date_end,
            Instant created_at) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.questions = questions;
        this.rps = rps;
        this.min_grade = min_grade;
        this.duration = duration;
        this.type_quiz = type_quiz;
        this.date_start = date_start;
        this.date_end = date_end;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getDevLecturerIds() {
        return devLecturerIds;
    }

    public void setDevLecturerIds(List<String> devLecturerIds) {
        this.devLecturerIds = devLecturerIds;
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public RPS getRps() {
        return rps;
    }

    public void setRps(RPS rps) {
        this.rps = rps;
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

    public String getMessage() {
        if (this.getRps() == null) {
            return "RPS is not set";
        }
        String message = String.format(
                "Berdasarkan %s Anda diwajibkan menilai soal yang terdapat pada %s dimulai pada tanggal %s ",
                this.getRps().getName(),
                this.getName(),
                this.getDate_start());
        return message;
    }

    public void setMessage(String message) {
    }

    public String getType_quiz() {
        return type_quiz;
    }

    public void setType_quiz(String type_quiz) {
        this.type_quiz = type_quiz;
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

    public Instant getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Instant created_at) {
        this.created_at = created_at;
    }

    public boolean isValid() {
        return this.id != null &&
                this.name != null &&
                this.description != null &&
                this.min_grade != null &&
                this.duration != null &&
                this.date_start != null &&
                this.date_end != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "id":
                this.id = value;
                break;
            case "name":
                this.name = value;
                break;
            case "description":
                this.description = value;
                break;
            case "min_grade":
                this.min_grade = Integer.parseInt(value);
                break;
            case "duration":
                this.duration = Integer.parseInt(value);
                break;
            case "message":
                break;
            case "type_quiz":
                this.type_quiz = value;
                break;
            case "date_start":
                this.date_start = Instant.parse(value);
                break;
            case "date_end":
                this.date_end = Instant.parse(value);
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
