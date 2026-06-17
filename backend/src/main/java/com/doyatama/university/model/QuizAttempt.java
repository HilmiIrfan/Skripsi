package com.doyatama.university.model;

import java.time.Instant;
import java.util.List;

public class QuizAttempt {
    private String id;
    private double grade;
    private Integer total_right;
    private String state;
    private List<StudentAnswer> student_answers;
    private Quiz quiz;
    private User user;
    private Student student;
    private Integer duration;
    private Instant created_at;

    public QuizAttempt() {
    }

    public QuizAttempt(String id, double grade, Integer total_right, String state, List<StudentAnswer> student_answers,
            Quiz quiz, User user, Student student, Integer duration, Instant created_at) {
        this.id = id;
        this.grade = grade;
        this.total_right = total_right;
        this.state = state;
        this.student_answers = student_answers;
        this.quiz = quiz;
        this.user = user;
        this.student = student;
        this.duration = duration;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public Integer getTotal_right() {
        return total_right;
    }

    public void setTotal_right(Integer total_right) {
        this.total_right = total_right;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<StudentAnswer> getStudent_answers() {
        return student_answers;
    }

    public void setStudent_answers(List<StudentAnswer> student_answers) {
        this.student_answers = student_answers;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Instant getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Instant created_at) {
        this.created_at = created_at;
    }

    public boolean isValid() {
        return this.id != null &&
                this.total_right != null &&
                this.state != null &&
                this.duration != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "id":
                this.id = value;
                break;
            // case "grade":
            // this.grade = grade;
            // break;
            // case "total_right":
            // this.total_right = total_right;
            // break;
            case "state":
                this.state = value;
                break;
            case "duration":
                this.duration = Integer.parseInt(value);
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
