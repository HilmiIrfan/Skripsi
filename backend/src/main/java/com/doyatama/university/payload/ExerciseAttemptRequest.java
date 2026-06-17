package com.doyatama.university.payload;

import java.util.Collections;
import java.util.List;

public class ExerciseAttemptRequest {
    private List<String> studentAnswers;
    private String exercise_id;
    private String user_id;
    private String student_id;
    private Integer duration;

    public ExerciseAttemptRequest() {
    }

    public ExerciseAttemptRequest(List<String> studentAnswers, String exercise_id, String user_id, String student_id,
            Integer duration) {
        this.studentAnswers = studentAnswers;
        this.exercise_id = exercise_id;
        this.user_id = user_id;
        this.student_id = student_id;
        this.duration = duration;
    }

    public List<String> getStudentAnswers() {
        return studentAnswers;
    }

    public void setStudentAnswers(List<String> studentAnswers) {
        this.studentAnswers = studentAnswers;
    }

    public String getExercise_id() {
        return exercise_id;
    }

    public void setExercise_id(String exercise_id) {
        this.exercise_id = exercise_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "studentAnswers":
                this.studentAnswers = Collections.singletonList(value);
                break;
            case "exercise_id":
                this.exercise_id = value;
                break;
            case "user_id":
                this.user_id = value;
                break;
            case "student_id":
                this.student_id = value;
                break;
            case "duration":
                this.duration = Integer.parseInt(value);
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
