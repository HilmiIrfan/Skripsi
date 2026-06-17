package com.doyatama.university.model;

import java.time.Instant;

public class StudentAnswer {
    private String id;
    private Question question;
    private Answer answer;
    private Integer score;
    private Instant created_at;

    public StudentAnswer() {
    }

    public StudentAnswer(String id, Question question, Answer answer, Integer score, Instant created_at) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.score = score;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Instant getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Instant created_at) {
        this.created_at = created_at;
    }

    public boolean isValid() {
        return this.id != null && this.score != null && this.question != null && this.answer != null;
    }

    @Override
    public String toString() {
        return "StudentAnswer{" +
                "id='" + id + '\'' +
                ", question=" + question +
                ", answer=" + answer +
                ", score=" + score +
                ", created_at=" + created_at +
                '}';
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "id":
                this.id = value;
                break;
            case "score":
                this.score = Integer.parseInt(value);
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
