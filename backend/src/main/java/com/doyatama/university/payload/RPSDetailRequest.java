package com.doyatama.university.payload;

import java.util.Collections;
import java.util.List;

public class RPSDetailRequest {
    private String rps_id;
    private Integer week;
    private String sub_cp_mk;
    private String weekLabel;
    private List<String> learning_materials;
    private String form_learning_id;
    private List<String> learning_methods;
    private List<String> assignments;
    private List<String> estimated_times;
    private List<String> student_learning_experiences;
    private List<String> assessment_criterias;
    private List<String> appraisal_forms;
    private List<String> assessment_indicators;
    private Float weight;

    public RPSDetailRequest() {
    }

    public RPSDetailRequest(String rps_id, Integer week, String sub_cp_mk, String weekLabel,
            List<String> learning_materials, String form_learning_id, List<String> learning_methods,
            List<String> assignments, List<String> estimated_times, List<String> student_learning_experiences,
            List<String> assessment_criterias, List<String> appraisal_forms, List<String> assessment_indicators,
            Float weight) {
        this.rps_id = rps_id;
        this.week = week;
        this.sub_cp_mk = sub_cp_mk;
        this.weekLabel = weekLabel;
        this.learning_materials = learning_materials;
        this.form_learning_id = form_learning_id;
        this.learning_methods = learning_methods;
        this.assignments = assignments;
        this.estimated_times = estimated_times;
        this.student_learning_experiences = student_learning_experiences;
        this.assessment_criterias = assessment_criterias;
        this.appraisal_forms = appraisal_forms;
        this.assessment_indicators = assessment_indicators;
        this.weight = weight;
    }

    public String getRps_id() {
        return rps_id;
    }

    public void setRps_id(String rps_id) {
        this.rps_id = rps_id;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public String getSub_cp_mk() {
        return sub_cp_mk;
    }

    public void setSub_cp_mk(String sub_cp_mk) {
        this.sub_cp_mk = sub_cp_mk;
    }

    public String getWeekLabel() {
        return weekLabel;
    }

    public void setWeekLabel(String weekLabel) {
        this.weekLabel = weekLabel;
    }

    public List<String> getLearning_materials() {
        return learning_materials;
    }

    public void setLearning_materials(List<String> learning_materials) {
        this.learning_materials = learning_materials;
    }

    public String getForm_learning_id() {
        return form_learning_id;
    }

    public void setForm_learning_id(String form_learning_id) {
        this.form_learning_id = form_learning_id;
    }

    public List<String> getLearning_methods() {
        return learning_methods;
    }

    public void setLearning_methods(List<String> learning_methods) {
        this.learning_methods = learning_methods;
    }

    public List<String> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<String> assignments) {
        this.assignments = assignments;
    }

    public List<String> getEstimated_times() {
        return estimated_times;
    }

    public void setEstimated_times(List<String> estimated_times) {
        this.estimated_times = estimated_times;
    }

    public List<String> getStudent_learning_experiences() {
        return student_learning_experiences;
    }

    public void setStudent_learning_experiences(List<String> student_learning_experiences) {
        this.student_learning_experiences = student_learning_experiences;
    }

    public List<String> getAssessment_criterias() {
        return assessment_criterias;
    }

    public void setAssessment_criterias(List<String> assessment_criterias) {
        this.assessment_criterias = assessment_criterias;
    }

    public List<String> getAppraisal_forms() {
        return appraisal_forms;
    }

    public void setAppraisal_forms(List<String> appraisal_forms) {
        this.appraisal_forms = appraisal_forms;
    }

    public List<String> getAssessment_indicators() {
        return assessment_indicators;
    }

    public void setAssessment_indicators(List<String> assessment_indicators) {
        this.assessment_indicators = assessment_indicators;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "rps_id":
                this.rps_id = value;
                break;
            case "week":
                this.week = Integer.parseInt(value);
                break;
            case "sub_cp_mk":
                this.sub_cp_mk = value;
                break;
            case "learning_materials":
                this.learning_materials = Collections.singletonList(value);
                break;
            case "form_learning_id":
                this.form_learning_id = value;
                break;
            case "learning_methods":
                this.learning_methods = Collections.singletonList(value);
                break;
            case "assignments":
                this.assignments = Collections.singletonList(value);
                break;
            case "estimated_times":
                this.estimated_times = Collections.singletonList(value);
                break;
            case "student_learning_experiences":
                this.student_learning_experiences = Collections.singletonList(value);
                break;
            case "assessment_criterias":
                this.assessment_criterias = Collections.singletonList(value);
                break;
            case "appraisal_forms":
                this.appraisal_forms = Collections.singletonList(value);
                break;
            case "weight":
                this.weight = Float.parseFloat(value);
                break;
            case "weekLabel":
                this.weekLabel = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
