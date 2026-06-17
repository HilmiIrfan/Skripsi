package com.doyatama.university.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class Question {
    private String id;
    private String title;
    private String description;
    private QuestionType question_type;
    private AnswerType answer_type;
    private String file_path;
    private RPSDetail rps_detail;
    private ExamType examType;
    private ExamType2 examType2;
    private ExamType3 examType3;
    private String explanation;
    // private ExerciseAttempt exerciseAttempt;

    // untuk hitung ivihf vikor tahap matrix
    private List<CriteriaValue> criteria_values;
    private Double averageValue1;
    private Double averageValue2;
    private Double averageValue3;
    private Double averageValue4;
    private Double averageValue5;
    private Double averageValue6;
    private Double averageValue7;
    private Double averageValue8;
    private Double averageValue9;

    public enum QuestionType {
        IMAGE,
        AUDIO,
        VIDEO,
        NORMAL,
    }

    public enum AnswerType {
        MULTIPLE_CHOICE,
        BOOLEAN,
        COMPLETION,
        ESSAY,
        MATCHING,
    }

    public enum ExamType {
        EXERCISE,
        NOTHING,
    }

    public enum ExamType2 {
        QUIZ,
        NOTHING,
    }

    public enum ExamType3 {
        EXAM,
        NOTHING,
    }

    public Question() {
    }

    public Question(String id, String title, String description, QuestionType questionType, AnswerType answerType,
            String file_path, RPSDetail rps_detail, ExamType examType, ExamType2 examType2, ExamType3 examType3,
            String explanation, List<CriteriaValue> criteria_values, Double averageValue1, Double averageValue2,
            Double averageValue3, Double averageValue4, Double averageValue5, Double averageValue6,
            Double averageValue7, Double averageValue8, Double averageValue9) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.question_type = questionType;
        this.answer_type = answerType;
        this.file_path = file_path;
        this.rps_detail = rps_detail;
        this.examType = examType;
        this.examType2 = examType2;
        this.examType3 = examType3;
        this.explanation = explanation;

        this.criteria_values = criteria_values;
        this.averageValue1 = averageValue1;
        this.averageValue2 = averageValue2;
        this.averageValue3 = averageValue3;
        this.averageValue4 = averageValue4;
        this.averageValue5 = averageValue5;
        this.averageValue6 = averageValue6;
        this.averageValue7 = averageValue7;
        this.averageValue8 = averageValue8;
        this.averageValue9 = averageValue9;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public QuestionType getQuestionType() {
        return question_type;
    }

    public void setQuestionType(QuestionType questionType) {
        this.question_type = questionType;
    }

    public AnswerType getAnswerType() {
        return answer_type;
    }

    public void setAnswerType(AnswerType answerType) {
        this.answer_type = answerType;
    }

    public ExamType getExamType() {
        return this.examType != null ? this.examType : ExamType.NOTHING;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }

    public ExamType2 getExamType2() {
        return this.examType2 != null ? this.examType2 : ExamType2.NOTHING;
    }

    public void setExamType2(ExamType2 examType2) {
        this.examType2 = examType2;
    }

    public ExamType3 getExamType3() {
        return this.examType3 != null ? this.examType3 : ExamType3.NOTHING;
    }

    public void setExamType3(ExamType3 examType3) {
        this.examType3 = examType3;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public RPSDetail getRps_detail() {
        return rps_detail;
    }

    public void setRps_detail(RPSDetail rps_detail) {
        this.rps_detail = rps_detail;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    // Getters and Setters untuk ivihf vikor tahap matrix
    public List<CriteriaValue> getCriteriaValues() {
        return criteria_values;
    }

    public void setCriteriaValues(List<CriteriaValue> criteria_values) {
        this.criteria_values = criteria_values;
    }

    public Double getAverageValue1() {
        return averageValue1;
    }

    public void setAverageValue1(Double averageValue1) {
        this.averageValue1 = averageValue1;
    }

    public Double getAverageValue2() {
        return averageValue2;
    }

    public void setAverageValue2(Double averageValue2) {
        this.averageValue2 = averageValue2;
    }

    public Double getAverageValue3() {
        return averageValue3;
    }

    public void setAverageValue3(Double averageValue3) {
        this.averageValue3 = averageValue3;
    }

    public Double getAverageValue4() {
        return averageValue4;
    }

    public void setAverageValue4(Double averageValue4) {
        this.averageValue4 = averageValue4;
    }

    public Double getAverageValue5() {
        return averageValue5;
    }

    public void setAverageValue5(Double averageValue5) {
        this.averageValue5 = averageValue5;
    }

    public Double getAverageValue6() {
        return averageValue6;
    }

    public void setAverageValue6(Double averageValue6) {
        this.averageValue6 = averageValue6;
    }

    public Double getAverageValue7() {
        return averageValue7;
    }

    public void setAverageValue7(Double averageValue7) {
        this.averageValue7 = averageValue7;
    }

    public Double getAverageValue8() {
        return averageValue8;
    }

    public void setAverageValue8(Double averageValue8) {
        this.averageValue8 = averageValue8;
    }

    public Double getAverageValue9() {
        return averageValue9;
    }

    public void setAverageValue9(Double averageValue9) {
        this.averageValue9 = averageValue9;
    }

    public boolean isValid() {
        return this.id != null && this.title != null && this.description != null && this.question_type != null
                && this.answer_type != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "id":
                this.id = value;
                break;
            case "title":
                this.title = value;
                break;
            case "description":
                this.description = value;
                break;
            case "question_type":
                this.question_type = QuestionType.valueOf(value);
                break;
            case "answer_type":
                this.answer_type = AnswerType.valueOf(value);
                break;
            case "file_path":
                this.file_path = value;
                break;

            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}