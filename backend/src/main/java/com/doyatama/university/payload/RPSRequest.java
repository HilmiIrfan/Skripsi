package com.doyatama.university.payload;

import java.util.Collections;
import java.util.List;

public class RPSRequest {
    private String name;
    private Integer sks;
    private Integer semester;
    private String cpl_prodi;
    private String cpl_mk;
    private List<String> learning_media_softwares;
    private List<String> learning_media_hardwares;
    private List<String> requirement_subjects;
    private String study_program_id;
    private String subject_id;
    private List<String> dev_lecturers;
    private List<String> teaching_lecturers;
    private List<String> coordinator_lecturers;
    private String ka_study_program;

    // Default constructor
    public RPSRequest() {
    }

    public RPSRequest(String name, Integer sks, Integer semester, String cpl_prodi, String cpl_mk,
            List<String> learning_media_softwares, List<String> learning_media_hardwares,
            List<String> requirement_subjects, String study_program_id, String subject_id, List<String> dev_lecturers,
            List<String> teaching_lecturers, List<String> coordinator_lecturers, String ka_study_program) {
        this.name = name;
        this.sks = sks;
        this.semester = semester;
        this.cpl_prodi = cpl_prodi;
        this.cpl_mk = cpl_mk;
        this.learning_media_softwares = learning_media_softwares;
        this.learning_media_hardwares = learning_media_hardwares;
        this.requirement_subjects = requirement_subjects;
        this.study_program_id = study_program_id;
        this.subject_id = subject_id;
        this.dev_lecturers = dev_lecturers;
        this.teaching_lecturers = teaching_lecturers;
        this.coordinator_lecturers = coordinator_lecturers;
        this.ka_study_program = ka_study_program;
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

    public List<String> getLearning_media_softwares() {
        return learning_media_softwares;
    }

    public void setLearning_media_softwares(List<String> learning_media_softwares) {
        this.learning_media_softwares = learning_media_softwares;
    }

    public List<String> getLearning_media_hardwares() {
        return learning_media_hardwares;
    }

    public void setLearning_media_hardwares(List<String> learning_media_hardwares) {
        this.learning_media_hardwares = learning_media_hardwares;
    }

    public List<String> getRequirement_subjects() {
        return requirement_subjects;
    }

    public void setRequirement_subjects(List<String> requirement_subjects) {
        this.requirement_subjects = requirement_subjects;
    }

    public String getStudy_program_id() {
        return study_program_id;
    }

    public void setStudy_program_id(String study_program_id) {
        this.study_program_id = study_program_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public List<String> getDev_lecturers() {
        return dev_lecturers;
    }

    public void setDev_lecturers(List<String> dev_lecturers) {
        this.dev_lecturers = dev_lecturers;
    }

    public List<String> getTeaching_lecturers() {
        return teaching_lecturers;
    }

    public void setTeaching_lecturers(List<String> teaching_lecturers) {
        this.teaching_lecturers = teaching_lecturers;
    }

    public List<String> getCoordinator_lecturers() {
        return coordinator_lecturers;
    }

    public void setCoordinator_lecturers(List<String> coordinator_lecturers) {
        this.coordinator_lecturers = coordinator_lecturers;
    }

    public String getKa_study_program() {
        return ka_study_program;
    }

    public void setKa_study_program(String ka_study_program) {
        this.ka_study_program = ka_study_program;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "name":
                this.name = value;
                break;
            case "sks":
                this.sks = Integer.parseInt(value);
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
            case "learning_media_softwares":
                this.learning_media_softwares = Collections.singletonList(value);
                break;
            case "learning_media_hardwares":
                this.learning_media_hardwares = Collections.singletonList(value);
                break;
            case "requirement_subjects":
                this.requirement_subjects = Collections.singletonList(value);
                break;
            case "study_program_id":
                this.study_program_id = value;
                break;
            case "subject_id":
                this.subject_id = value;
                break;
            case "dev_lecturers":
                this.dev_lecturers = Collections.singletonList(value);
                break;
            case "teaching_lecturers":
                this.teaching_lecturers = Collections.singletonList(value);
                break;
            case "coordinator_lecturers":
                this.coordinator_lecturers = Collections.singletonList(value);
                break;
            case "ka_study_program":
                this.ka_study_program = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }
}
