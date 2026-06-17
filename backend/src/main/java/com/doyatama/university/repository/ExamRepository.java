package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExamRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "exams";

    public List<Exam> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("questions", "questions");
        columnMapping.put("min_grade", "min_grade");
        columnMapping.put("rps", "rps");
        columnMapping.put("duration", "duration");
        columnMapping.put("date_start", "date_start");
        columnMapping.put("date_end", "date_end");
        columnMapping.put("created_at", "created_at");
        columnMapping.put("rps_detail", "rps_detail");
        columnMapping.put("type_exercise", "type_exercise");
        return client.showListTable(tableUsers.toString(), columnMapping, Exam.class, size);
    }

    public Exam save(Exam exam) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString();

        TableName tableExam = TableName.valueOf(tableName);
        client.insertRecord(tableExam, rowKey, "main", "id", rowKey);
        client.insertRecord(tableExam, rowKey, "main", "name", exam.getName());
        client.insertRecord(tableExam, rowKey, "main", "description", exam.getDescription().toString());
        client.insertRecord(tableExam, rowKey, "main", "min_grade", exam.getMin_grade().toString());
        client.insertRecord(tableExam, rowKey, "main", "duration", exam.getDuration().toString());
        client.insertRecord(tableExam, rowKey, "main", "date_start", exam.getDate_start().toString());
        client.insertRecord(tableExam, rowKey, "main", "date_end", exam.getDate_end().toString());
        client.insertRecord(tableExam, rowKey, "main", "type_exercise", exam.getType_exercise());

        if (exam.getQuestions() != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            for (int i = 0; i < exam.getQuestions().size(); i++) {
                Question question = exam.getQuestions().get(i);
                String questionJson = objectMapper.writeValueAsString(question);
                client.insertRecord(tableExam, rowKey, "questions", "q_" + i, questionJson);
            }
        }

        client.insertRecord(tableExam, rowKey, "rps", "id", exam.getRps().getId());
        client.insertRecord(tableExam, rowKey, "rps", "name", exam.getRps().getName());

        // Get time now
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        client.insertRecord(tableExam, rowKey, "detail", "created_by", "Doyatama");
        client.insertRecord(tableExam, rowKey, "detail", "created_at", instant.toString());
        return exam;
    }

    public Exam findById(String examId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("questions", "questions");
        columnMapping.put("rps", "rps");
        columnMapping.put("min_grade", "min_grade");
        columnMapping.put("duration", "duration");
        columnMapping.put("date_start", "date_start");
        columnMapping.put("date_end", "date_end");
        columnMapping.put("created_at", "created_at");
        columnMapping.put("type_exercise", "type_exercise");
        return client.showDataTable(tableUsers.toString(), columnMapping, examId, Exam.class);
    }

    public Exam findAnswer(String examId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("questions", "questions");
        columnMapping.put("rps", "rps");
        columnMapping.put("min_grade", "min_grade");
        columnMapping.put("duration", "duration");
        columnMapping.put("date_start", "date_start");
        columnMapping.put("date_end", "date_end");
        columnMapping.put("created_at", "created_at");
        columnMapping.put("type_exercise", "type_exercise");
        return client.showDataTable(tableUsers.toString(), columnMapping, examId, Exam.class);
    }

    public Exam update(String examId, Exam exam) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableExam = TableName.valueOf(tableName);
        client.insertRecord(tableExam, examId, "main", "name", exam.getName());
        client.insertRecord(tableExam, examId, "main", "description", exam.getDescription().toString());
        client.insertRecord(tableExam, examId, "main", "min_grade", exam.getMin_grade().toString());
        client.insertRecord(tableExam, examId, "main", "duration", exam.getDuration().toString());
        client.insertRecord(tableExam, examId, "main", "date_start", exam.getDate_start().toString());
        client.insertRecord(tableExam, examId, "main", "date_end", exam.getDate_end().toString());

        // questions
        if (exam.getQuestions() != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            for (int i = 0; i < exam.getQuestions().size(); i++) {
                Question question = exam.getQuestions().get(i);
                try {
                    String questionJson = objectMapper.writeValueAsString(question);
                    client.insertRecord(tableExam, examId, "questions", "q_" + i, questionJson);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        client.insertRecord(tableExam, examId, "rps", "id", exam.getRps().getId());
        client.insertRecord(tableExam, examId, "rps", "name", exam.getRps().getName());

        // Get time now
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        client.insertRecord(tableExam, examId, "detail", "created_by", "Doyatama");
        client.insertRecord(tableExam, examId, "detail", "created_at", instant.toString());
        return exam;
    }

    public boolean deleteById(String examId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, examId);
        return true;
    }
}
