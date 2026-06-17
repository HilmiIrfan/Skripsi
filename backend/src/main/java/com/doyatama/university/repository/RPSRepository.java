package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.LearningMedia;
import com.doyatama.university.model.Lecture;
import com.doyatama.university.model.RPS;
import com.doyatama.university.model.Subject;
import com.google.gson.Gson;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.stereotype.Repository;

@Repository
public class RPSRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "rps";

    public List<RPS> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("sks", "sks");
        columnMapping.put("semester", "semester");
        columnMapping.put("cpl_prodi", "cpl_prodi");
        columnMapping.put("cpl_mk", "cpl_mk");
        columnMapping.put("learning_media_softwares", "learning_media_softwares");
        columnMapping.put("learning_media_hardwares", "learning_media_hardwares");
        columnMapping.put("requirement_subjects", "requirement_subjects");
        columnMapping.put("year_commenced", "year_commenced");
        columnMapping.put("study_program", "study_program");
        columnMapping.put("subject", "subject");
        columnMapping.put("dev_lecturers", "dev_lecturers");
        columnMapping.put("teaching_lecturers", "teaching_lecturers");
        columnMapping.put("coordinator_lecturers", "coordinator_lecturers");
        columnMapping.put("ka_study_program", "ka_study_program");
        columnMapping.put("created_at", "created_at");
        return client.showListTable(tableUsers.toString(), columnMapping, RPS.class, size);
    }

    public RPS save(RPS rps) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey;
        if (rps.getId() != null && !rps.getId().isEmpty()) {
            rowKey = rps.getId();
        } else {
            rowKey = UUID.randomUUID().toString().substring(0, 20);
        }

        TableName tableRPS = TableName.valueOf(tableName);
        client.insertRecord(tableRPS, rowKey, "main", "id", rowKey);
        client.insertRecord(tableRPS, rowKey, "main", "name", rps.getName());
        client.insertRecord(tableRPS, rowKey, "main", "sks", rps.getSks().toString());
        client.insertRecord(tableRPS, rowKey, "main", "semester", rps.getSemester().toString());
        client.insertRecord(tableRPS, rowKey, "main", "cpl_prodi", rps.getCpl_prodi());
        client.insertRecord(tableRPS, rowKey, "main", "cpl_mk", rps.getCpl_mk());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // learning_media_software
        for (int i = 0; i < rps.getLearning_media_softwares().size(); i++) {
            LearningMedia learningMedia = rps.getLearning_media_softwares().get(i);
            String learningMediaJson = objectMapper.writeValueAsString(learningMedia);
            client.insertRecord(tableRPS, rowKey, "learning_media_softwares", "soft_" + i, learningMediaJson);
        }

        // learning_media_hardware
        for (int i = 0; i < rps.getLearning_media_hardwares().size(); i++) {
            LearningMedia learningMedia = rps.getLearning_media_hardwares().get(i);
            String learningMediaJson = objectMapper.writeValueAsString(learningMedia);
            client.insertRecord(tableRPS, rowKey, "learning_media_hardwares", "hard_" + i, learningMediaJson);
        }

        // requirement_subject
        for (int i = 0; i < rps.getRequirement_subjects().size(); i++) {
            Subject subject = rps.getRequirement_subjects().get(i);
            String subjectJson = objectMapper.writeValueAsString(subject);
            client.insertRecord(tableRPS, rowKey, "requirement_subjects", "req_" + i, subjectJson);
        }
        if (rps.getStudy_program() != null) {
            client.insertRecord(tableRPS, rowKey, "study_program", "id", rps.getStudy_program().getId());
            client.insertRecord(tableRPS, rowKey, "study_program", "name", rps.getStudy_program().getName());
        }
        if (rps.getSubject() != null) {
            // client.insertRecord(tableRPS, rowKey, "subject", "id",
            // rps.getSubject().getId());
            client.insertRecord(tableRPS, rowKey, "subject", "name", rps.getSubject().getName());
        }

        // dev_lecturers
        for (int i = 0; i < rps.getDev_lecturers().size(); i++) {
            Lecture lecture = rps.getDev_lecturers().get(i);
            String lectureJson = objectMapper.writeValueAsString(lecture);
            client.insertRecord(tableRPS, rowKey, "dev_lecturers", "lecture_" + i, lectureJson);
        }

        // teaching_lecturers
        for (int i = 0; i < rps.getTeaching_lecturers().size(); i++) {
            Lecture lecture = rps.getTeaching_lecturers().get(i);
            String lectureJson = objectMapper.writeValueAsString(lecture);
            client.insertRecord(tableRPS, rowKey, "teaching_lecturers", "lecture_" + i, lectureJson);
        }

        // coordinator_lecturers
        for (int i = 0; i < rps.getCoordinator_lecturers().size(); i++) {
            Lecture lecture = rps.getCoordinator_lecturers().get(i);
            String lectureJson = objectMapper.writeValueAsString(lecture);
            client.insertRecord(tableRPS, rowKey, "coordinator_lecturers", "lecture_" + i, lectureJson);
        }
        if (rps.getKa_study_program() != null) {

            client.insertRecord(tableRPS, rowKey, "ka_study_program", "id", rps.getKa_study_program().getId());
            client.insertRecord(tableRPS, rowKey, "ka_study_program", "name", rps.getKa_study_program().getName());
        }
        // Get time now
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        client.insertRecord(tableRPS, rowKey, "detail", "created_by", "Doyatama");
        client.insertRecord(tableRPS, rowKey, "detail", "created_at", instant.toString());
        return rps;
    }

    // public RPS getRpsWithDevLecturers(String rpsId) throws IOException {
    // HBaseCustomClient client = new HBaseCustomClient(conf);
    // TableName tableRps = TableName.valueOf(tableName);
    // Map<String, String> columnMapping = new HashMap<>();
    //
    // // Add the mappings to the HashMap
    // columnMapping.put("id", "id");
    // columnMapping.put("name", "name");
    // columnMapping.put("dev_lecturers", "dev_lecturers");
    //
    // RPS rps = client.showDataTable(tableRps.toString(), columnMapping, rpsId,
    // RPS.class);
    //
    // // Check if the dev_lecturers field is null
    // if (rps.getDev_lecturers() == null) {
    // // If it's null, fetch it from the database and set it in the Rps object
    // List<Lecture> devLecturers = fetchDevLecturersFromDatabase(rpsId);
    // rps.setDev_lecturers(devLecturers);
    // }
    //
    // return rps;
    // }

    public RPS findById(String rpsId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("sks", "sks");
        columnMapping.put("semester", "semester");
        columnMapping.put("cpl_prodi", "cpl_prodi");
        columnMapping.put("cpl_mk", "cpl_mk");
        columnMapping.put("learning_media_softwares", "learning_media_softwares");
        columnMapping.put("learning_media_hardwares", "learning_media_hardwares");
        columnMapping.put("requirement_subjects", "requirement_subjects");
        columnMapping.put("year_commenced", "year_commenced");
        columnMapping.put("study_program", "study_program");
        columnMapping.put("subject", "subject");
        columnMapping.put("dev_lecturers", "dev_lecturers");
        columnMapping.put("teaching_lecturers", "teaching_lecturers");
        columnMapping.put("coordinator_lecturers", "coordinator_lecturers");
        columnMapping.put("ka_study_program", "ka_study_program");
        columnMapping.put("created_at", "created_at");
        return client.showDataTable(tableUsers.toString(), columnMapping, rpsId, RPS.class);
    }

    public RPS findByIdLecture(String rpsId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("dev_lecturers", "dev_lecturers");

        // ... add the rest of the mappings ...

        List<RPS> rpsList = client.showListTable(tableUsers.toString(), columnMapping, RPS.class, Integer.MAX_VALUE);
        return rpsList.stream()
                .filter(rps -> rps.getId().equals(rpsId))
                .findFirst()
                .orElse(null);
    }

    public RPS update(String rpsId, RPS rps) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableRPS = TableName.valueOf(tableName);
        client.insertRecord(tableRPS, rpsId, "main", "name", rps.getName());
        client.insertRecord(tableRPS, rpsId, "main", "sks", rps.getSks().toString());
        client.insertRecord(tableRPS, rpsId, "main", "semester", rps.getSemester().toString());
        client.insertRecord(tableRPS, rpsId, "main", "cpl_prodi", rps.getCpl_prodi());
        client.insertRecord(tableRPS, rpsId, "main", "cpl_mk", rps.getCpl_mk());
        // learning_media_software
        for (int i = 0; i < rps.getLearning_media_softwares().size(); i++) {
            LearningMedia learningMedia = rps.getLearning_media_softwares().get(i);
            client.insertRecord(tableRPS, rpsId, "learning_media_softwares", "soft_" + i,
                    new Gson().toJson(learningMedia));
        }

        // learning_media_hardware
        for (int i = 0; i < rps.getLearning_media_hardwares().size(); i++) {
            LearningMedia learningMedia = rps.getLearning_media_hardwares().get(i);
            client.insertRecord(tableRPS, rpsId, "learning_media_hardwares", "hard_" + i,
                    new Gson().toJson(learningMedia));
        }

        // requirement_subject
        for (int i = 0; i < rps.getRequirement_subjects().size(); i++) {
            Subject subject = rps.getRequirement_subjects().get(i);
            client.insertRecord(tableRPS, rpsId, "requirement_subjects", "req_" + i, new Gson().toJson(subject));
        }

        client.insertRecord(tableRPS, rpsId, "study_program", "id", rps.getStudy_program().getId());
        client.insertRecord(tableRPS, rpsId, "study_program", "name", rps.getStudy_program().getName());
        client.insertRecord(tableRPS, rpsId, "subject", "id", rps.getSubject().getId());
        client.insertRecord(tableRPS, rpsId, "subject", "name", rps.getSubject().getName());

        // dev_lecturers
        for (int i = 0; i < rps.getDev_lecturers().size(); i++) {
            Lecture lecture = rps.getDev_lecturers().get(i);
            client.insertRecord(tableRPS, rpsId, "dev_lecturers", "lecture_" + i, new Gson().toJson(lecture));
        }

        // teaching_lecturers
        for (int i = 0; i < rps.getTeaching_lecturers().size(); i++) {
            Lecture lecture = rps.getTeaching_lecturers().get(i);
            client.insertRecord(tableRPS, rpsId, "teaching_lecturers", "lecture_" + i, new Gson().toJson(lecture));
        }

        // coordinator_lecturers
        for (int i = 0; i < rps.getCoordinator_lecturers().size(); i++) {
            Lecture lecture = rps.getCoordinator_lecturers().get(i);
            client.insertRecord(tableRPS, rpsId, "coordinator_lecturers", "lecture_" + i, new Gson().toJson(lecture));
        }

        client.insertRecord(tableRPS, rpsId, "ka_study_program", "id", rps.getKa_study_program().getId());
        client.insertRecord(tableRPS, rpsId, "ka_study_program", "name", rps.getKa_study_program().getName());

        // Get time now
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        client.insertRecord(tableRPS, rpsId, "detail", "created_by", "Doyatama");
        client.insertRecord(tableRPS, rpsId, "detail", "created_at", instant.toString());
        return rps;
    }

    public boolean deleteById(String rpsId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, rpsId);
        return true;
    }
}
