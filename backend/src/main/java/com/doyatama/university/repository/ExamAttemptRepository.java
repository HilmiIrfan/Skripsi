package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.*;
import com.google.gson.Gson;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class ExamAttemptRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "exam_attempts";

    public List<ExamAttempt> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("grade", "grade");
        columnMapping.put("total_right", "total_right");
        columnMapping.put("state", "state");
        columnMapping.put("student_answers", "student_answers");
        columnMapping.put("exam", "exam");
        columnMapping.put("user", "user");
        columnMapping.put("student", "student");
        columnMapping.put("duration", "duration");
        columnMapping.put("created_at", "created_at");
        return client.showListTable(tableUsers.toString(), columnMapping, ExamAttempt.class, size);
    }

    public List<ExamAttempt> findByUser(String userID, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("grade", "grade");
        columnMapping.put("total_right", "total_right");
        columnMapping.put("state", "state");
        columnMapping.put("student_answers", "student_answers");
        columnMapping.put("exam", "exam");
        columnMapping.put("user", "user");
        columnMapping.put("student", "student");
        columnMapping.put("duration", "duration");
        columnMapping.put("created_at", "created_at");
        return client.getDataListByColumn(tableUsers.toString(), columnMapping, "user", "id", userID, ExamAttempt.class, size);
    }

    public List<ExamAttempt> findByExam(String examID, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("grade", "grade");
        columnMapping.put("total_right", "total_right");
        columnMapping.put("state", "state");
        columnMapping.put("student_answers", "student_answers");
        columnMapping.put("exam", "exam");
        columnMapping.put("user", "user");
        columnMapping.put("student", "student");
        columnMapping.put("duration", "duration");
        columnMapping.put("created_at", "created_at");
        return client.getDataListByColumn(tableUsers.toString(), columnMapping, "exam", "id", examID, ExamAttempt.class, size);
    }

    public ExamAttempt save(ExamAttempt examAttempt) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString();

        TableName tableExamAttempt = TableName.valueOf(tableName);
        client.insertRecord(tableExamAttempt, rowKey, "main", "id", rowKey);
        client.insertRecord(tableExamAttempt, rowKey, "main", "grade", String.valueOf(examAttempt.getGrade()));
        client.insertRecord(tableExamAttempt, rowKey, "main", "total_right", examAttempt.getTotal_right().toString());
        client.insertRecord(tableExamAttempt, rowKey, "main", "state", examAttempt.getState());
        client.insertRecord(tableExamAttempt, rowKey, "main", "duration", examAttempt.getDuration().toString());

        // questions
        for (int i = 0; i < examAttempt.getStudent_answers().size(); i++) {
            StudentAnswer studentAnswer = examAttempt.getStudent_answers().get(i);
            client.insertRecord(tableExamAttempt, rowKey, "student_answers", "sa_" + i, new Gson().toJson(studentAnswer));
        }

        client.insertRecord(tableExamAttempt, rowKey, "exam", "id", examAttempt.getExam().getId());
        client.insertRecord(tableExamAttempt, rowKey, "exam", "name", examAttempt.getExam().getName());
        client.insertRecord(tableExamAttempt, rowKey, "exam", "min_grade", examAttempt.getExam().getMin_grade().toString());
        client.insertRecord(tableExamAttempt, rowKey, "exam", "duration", examAttempt.getExam().getDuration().toString());

        client.insertRecord(tableExamAttempt, rowKey, "user", "id", examAttempt.getUser().getId());
        client.insertRecord(tableExamAttempt, rowKey, "user", "name", examAttempt.getUser().getName());

        client.insertRecord(tableExamAttempt, rowKey, "student", "id", examAttempt.getStudent().getId());
        client.insertRecord(tableExamAttempt, rowKey, "student", "name", examAttempt.getStudent().getName());

        // Get time now
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        client.insertRecord(tableExamAttempt, rowKey, "detail", "created_by", "Doyatama");
        client.insertRecord(tableExamAttempt, rowKey, "detail", "created_at", instant.toString());
        return examAttempt;
    }

    public ExamAttempt findById(String examAttemptId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("questions", "questions");
        columnMapping.put("rps", "rps");
        columnMapping.put("duration", "duration");
        columnMapping.put("date_start", "date_start");
        columnMapping.put("date_end", "date_end");
        columnMapping.put("created_at", "created_at");
        return client.showDataTable(tableUsers.toString(), columnMapping, examAttemptId, ExamAttempt.class);
    }

    public ExamAttempt findAnswer(String examAttemptId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("questions", "questions");
        columnMapping.put("rps", "rps");
        columnMapping.put("duration", "duration");
        columnMapping.put("date_start", "date_start");
        columnMapping.put("date_end", "date_end");
        columnMapping.put("created_at", "created_at");
        return client.showDataTable(tableUsers.toString(), columnMapping, examAttemptId, ExamAttempt.class);
    }

    public ExamAttempt update(String examAttemptId, ExamAttempt examAttempt) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableExamAttempt = TableName.valueOf(tableName);
        client.insertRecord(tableExamAttempt, examAttemptId, "main", "grade", String.valueOf(examAttempt.getGrade()));
        client.insertRecord(tableExamAttempt, examAttemptId, "main", "state", examAttempt.getState());
        client.insertRecord(tableExamAttempt, examAttemptId, "main", "duration", examAttempt.getDuration().toString());

        // questions
        for (int i = 0; i < examAttempt.getStudent_answers().size(); i++) {
            StudentAnswer studentAnswer = examAttempt.getStudent_answers().get(i);
            client.insertRecord(tableExamAttempt, examAttemptId, "student_answers", "sa_" + i, new Gson().toJson(studentAnswer));
        }

        client.insertRecord(tableExamAttempt, examAttemptId, "exam", "id", examAttempt.getExam().getId());
        client.insertRecord(tableExamAttempt, examAttemptId, "exam", "name", examAttempt.getExam().getName());
        client.insertRecord(tableExamAttempt, examAttemptId, "exam", "min_grade", examAttempt.getExam().getMin_grade().toString());
        client.insertRecord(tableExamAttempt, examAttemptId, "exam", "duration", examAttempt.getExam().getDuration().toString());

        client.insertRecord(tableExamAttempt, examAttemptId, "user", "id", examAttempt.getUser().getId());
        client.insertRecord(tableExamAttempt, examAttemptId, "user", "name", examAttempt.getUser().getName());

        client.insertRecord(tableExamAttempt, examAttemptId, "student", "id", examAttempt.getStudent().getId());
        client.insertRecord(tableExamAttempt, examAttemptId, "student", "name", examAttempt.getStudent().getName());

        // Get time now
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        client.insertRecord(tableExamAttempt, examAttemptId, "detail", "created_by", "Doyatama");
        client.insertRecord(tableExamAttempt, examAttemptId, "detail", "created_at", instant.toString());
        return examAttempt;
    }

    public boolean deleteById(String examAttemptId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, examAttemptId);
        return true;
    }
}
