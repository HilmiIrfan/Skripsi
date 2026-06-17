package com.doyatama.university.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class RPS {
    private String id;
    private String name;
    private Integer sks;
    private Integer semester;
    private String cpl_prodi;
    private String cpl_mk;
    private List<LearningMedia> learning_media_softwares;
    private List<LearningMedia> learning_media_hardwares;
    private List<Subject> requirement_subjects;
    private StudyProgram study_program;
    private Subject subject;
    private List<Lecture> dev_lecturers;
    private List<Lecture> teaching_lecturers;
    private List<Lecture> coordinator_lecturers;
    private Lecture ka_study_program;
    private Instant created_at;
    private List<RPSDetail> rpsDetails; // Add this line

    public RPS() {
    }

    public RPS(String id, String name, Integer sks, Integer semester, String cpl_prodi, String cpl_mk,
            List<LearningMedia> learning_media_softwares, List<LearningMedia> learning_media_hardwares,
            List<Subject> requirement_subjects, StudyProgram study_program, Subject subject,
            List<Lecture> dev_lecturers, List<Lecture> teaching_lecturers, List<Lecture> coordinator_lecturers,
            Lecture ka_study_program, Instant created_at) {
        this.id = id;
        this.name = name;
        this.sks = sks;
        this.semester = semester;
        this.cpl_prodi = cpl_prodi;
        this.cpl_mk = cpl_mk;
        this.learning_media_softwares = learning_media_softwares;
        this.learning_media_hardwares = learning_media_hardwares;
        this.requirement_subjects = requirement_subjects;
        this.study_program = study_program;
        this.subject = subject;
        this.dev_lecturers = dev_lecturers;
        this.teaching_lecturers = teaching_lecturers;
        this.coordinator_lecturers = coordinator_lecturers;
        this.ka_study_program = ka_study_program;
        this.created_at = created_at;
    }

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

    public Integer getSks() {
        return sks;
    }

    public void setSks(Integer sks) {
        this.sks = sks;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getCpl_prodi() {
        return cpl_prodi;
    }

    public void setCpl_prodi(String cpl_prodi) {
        this.cpl_prodi = cpl_prodi;
    }

    public String getCpl_mk() {
        return cpl_mk;
    }

    public void setCpl_mk(String cpl_mk) {
        this.cpl_mk = cpl_mk;
    }

    public List<LearningMedia> getLearning_media_softwares() {
        return learning_media_softwares != null ? learning_media_softwares : new ArrayList<>();
    }

    public void setLearning_media_softwares(List<LearningMedia> learning_media_softwares) {
        this.learning_media_softwares = learning_media_softwares != null ? learning_media_softwares : new ArrayList<>();
    }

    public List<LearningMedia> getLearning_media_hardwares() {
        return learning_media_hardwares != null ? learning_media_hardwares : new ArrayList<>();
    }

    public void setLearning_media_hardwares(List<LearningMedia> learning_media_hardwares) {
        this.learning_media_hardwares = learning_media_hardwares != null ? learning_media_hardwares : new ArrayList<>();
    }

    public List<Subject> getRequirement_subjects() {
        return requirement_subjects != null ? requirement_subjects : new ArrayList<>();
    }

    public void setRequirement_subjects(List<Subject> requirement_subjects) {
        this.requirement_subjects = requirement_subjects;
    }

    public StudyProgram getStudy_program() {
        return study_program;
    }

    public void setStudy_program(StudyProgram study_program) {
        this.study_program = study_program;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<Lecture> getDev_lecturers() {
        return dev_lecturers != null ? dev_lecturers : new ArrayList<>();
    }

    public void setDev_lecturers(List<Lecture> dev_lecturers) {
        this.dev_lecturers = dev_lecturers;
    }

    public List<Lecture> getTeaching_lecturers() {
        return teaching_lecturers != null ? teaching_lecturers : new ArrayList<>();
    }

    public void setTeaching_lecturers(List<Lecture> teaching_lecturers) {
        this.teaching_lecturers = teaching_lecturers;
    }

    public List<Lecture> getCoordinator_lecturers() {
        return coordinator_lecturers != null ? coordinator_lecturers : new ArrayList<>();
    }

    public void setCoordinator_lecturers(List<Lecture> coordinator_lecturers) {
        this.coordinator_lecturers = coordinator_lecturers != null ? coordinator_lecturers : new ArrayList<>();
    }

    public Lecture getKa_study_program() {
        return ka_study_program;
    }

    public void setKa_study_program(Lecture ka_study_program) {
        this.ka_study_program = ka_study_program;
    }

    public Instant getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Instant created_at) {
        this.created_at = created_at;
    }

    // Add getter and setter for rpsDetails
    public List<RPSDetail> getRpsDetails() {
        return rpsDetails != null ? rpsDetails : new ArrayList<>();
    }

    public void setRpsDetails(List<RPSDetail> rpsDetails) {
        this.rpsDetails = rpsDetails != null ? rpsDetails : new ArrayList<>();
    }

    public boolean isValid() {
        return this.id != null &&
                this.name != null &&
                this.sks != null &&
                this.semester != null &&
                this.cpl_prodi != null &&
                this.cpl_mk != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "id":
                this.id = value;
                break;
            case "name":
                this.name = value;
                break;
            case "semester":
                this.semester = Integer.parseInt(value);
                break;
            case "cpl_prodi":
                this.cpl_prodi = value;
                break;
            case "cpl_mk":
                this.cpl_mk = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
