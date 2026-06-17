package com.doyatama.university.payload;

public class QuestionRequest {
    private String id;
    private String title;
    private String description;
    private String question_type;
    private String answer_type;
    private String rps_detail_id;
    private String examType;
    private String examType2;
    private String examType3;
    private String explanation;

    public QuestionRequest() {
    }

    public QuestionRequest(String id, String title, String description, String question_type, String answer_type,
            String rps_detail_id, String examType, String examType2, String examType3, String explanation) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.question_type = question_type;
        this.answer_type = answer_type;
        this.rps_detail_id = rps_detail_id;
        this.examType = examType;
        this.examType2 = examType2;
        this.examType3 = examType3;
        this.explanation = explanation;
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

    public String getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }

    public String getAnswer_type() {
        return answer_type;
    }

    public void setAnswer_types(String answer_type) {
        this.answer_type = answer_type;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public String getExamType2() {
        return examType2;
    }

    public void setExamType2(String examType2) {
        this.examType2 = examType2;
    }

    public String getExamType3() {
        return examType3;
    }

    public void setExamType3(String examType3) {
        this.examType3 = examType3;
    }

    public String getRps_detail_id() {
        return rps_detail_id;
    }

    public void setRps_detail_id(String rps_detail_id) {
        this.rps_detail_id = rps_detail_id;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "id":
                this.id = value;
                break;
            case "rps_detail_id":
                this.rps_detail_id = value;
                break;
            case "title":
                this.title = value;
                break;
            case "description":
                this.description = value;
                break;
            case "question_type":
                this.question_type = value;
                break;
            case "answer_type":
                this.answer_type = value;
                break;
            case "examType":
                this.examType = value;
                break;
            case "examType2":
                this.examType2 = value;
                break;
            case "examType3":
                this.examType3 = value;
                break;

            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}