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

public class QuizAttemptRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "quiz_attempts";

    public List<QuizAttempt> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("grade", "grade");
        columnMapping.put("total_right", "total_right");
        columnMapping.put("state", "state");
        columnMapping.put("student_answers", "student_answers");
        columnMapping.put("quiz", "quiz");
        columnMapping.put("user", "user");
        columnMapping.put("student", "student");
        columnMapping.put("duration", "duration");
        columnMapping.put("created_at", "created_at");
        return client.showListTable(tableUsers.toString(), columnMapping, QuizAttempt.class, size);
    }

    public List<QuizAttempt> findByUser(String userID, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("grade", "grade");
        columnMapping.put("total_right", "total_right");
        columnMapping.put("state", "state");
        columnMapping.put("student_answers", "student_answers");
        columnMapping.put("quiz", "quiz");
        columnMapping.put("user", "user");
        columnMapping.put("student", "student");
        columnMapping.put("duration", "duration");
        columnMapping.put("created_at", "created_at");
        return client.getDataListByColumn(tableUsers.toString(), columnMapping, "user", "id", userID, QuizAttempt.class, size);
    }

    public List<QuizAttempt> findByQuiz(String quizID, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("grade", "grade");
        columnMapping.put("total_right", "total_right");
        columnMapping.put("state", "state");
        columnMapping.put("student_answers", "student_answers");
        columnMapping.put("quiz", "quiz");
        columnMapping.put("user", "user");
        columnMapping.put("student", "student");
        columnMapping.put("duration", "duration");
        columnMapping.put("created_at", "created_at");
        return client.getDataListByColumn(tableUsers.toString(), columnMapping, "quiz", "id", quizID, QuizAttempt.class, size);
    }

    public QuizAttempt save(QuizAttempt quizAttempt) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString();

        TableName tableQuizAttempt = TableName.valueOf(tableName);
        client.insertRecord(tableQuizAttempt, rowKey, "main", "id", rowKey);
        client.insertRecord(tableQuizAttempt, rowKey, "main", "grade", String.valueOf(quizAttempt.getGrade()));
        client.insertRecord(tableQuizAttempt, rowKey, "main", "total_right", quizAttempt.getTotal_right().toString());
        client.insertRecord(tableQuizAttempt, rowKey, "main", "state", quizAttempt.getState());
        client.insertRecord(tableQuizAttempt, rowKey, "main", "duration", quizAttempt.getDuration().toString());

        // questions
        for (int i = 0; i < quizAttempt.getStudent_answers().size(); i++) {
            StudentAnswer studentAnswer = quizAttempt.getStudent_answers().get(i);
            client.insertRecord(tableQuizAttempt, rowKey, "student_answers", "sa_" + i, new Gson().toJson(studentAnswer));
        }

        client.insertRecord(tableQuizAttempt, rowKey, "quiz", "id", quizAttempt.getQuiz().getId());
        client.insertRecord(tableQuizAttempt, rowKey, "quiz", "name", quizAttempt.getQuiz().getName());
        client.insertRecord(tableQuizAttempt, rowKey, "quiz", "min_grade", quizAttempt.getQuiz().getMin_grade().toString());
        client.insertRecord(tableQuizAttempt, rowKey, "quiz", "duration", quizAttempt.getQuiz().getDuration().toString());

        client.insertRecord(tableQuizAttempt, rowKey, "user", "id", quizAttempt.getUser().getId());
        client.insertRecord(tableQuizAttempt, rowKey, "user", "name", quizAttempt.getUser().getName());

        client.insertRecord(tableQuizAttempt, rowKey, "student", "id", quizAttempt.getStudent().getId());
        client.insertRecord(tableQuizAttempt, rowKey, "student", "name", quizAttempt.getStudent().getName());

        // Get time now
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        client.insertRecord(tableQuizAttempt, rowKey, "detail", "created_by", "Doyatama");
        client.insertRecord(tableQuizAttempt, rowKey, "detail", "created_at", instant.toString());
        return quizAttempt;
    }

    public QuizAttempt findById(String quizAttemptId) throws IOException {
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
        return client.showDataTable(tableUsers.toString(), columnMapping, quizAttemptId, QuizAttempt.class);
    }

    public QuizAttempt findAnswer(String quizAttemptId) throws IOException {
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
        return client.showDataTable(tableUsers.toString(), columnMapping, quizAttemptId, QuizAttempt.class);
    }

    public QuizAttempt update(String quizAttemptId, QuizAttempt quizAttempt) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableQuizAttempt = TableName.valueOf(tableName);
        client.insertRecord(tableQuizAttempt, quizAttemptId, "main", "grade", String.valueOf(quizAttempt.getGrade()));
        client.insertRecord(tableQuizAttempt, quizAttemptId, "main", "state", quizAttempt.getState());
        client.insertRecord(tableQuizAttempt, quizAttemptId, "main", "duration", quizAttempt.getDuration().toString());

        // questions
        for (int i = 0; i < quizAttempt.getStudent_answers().size(); i++) {
            StudentAnswer studentAnswer = quizAttempt.getStudent_answers().get(i);
            client.insertRecord(tableQuizAttempt, quizAttemptId, "student_answers", "sa_" + i, new Gson().toJson(studentAnswer));
        }

        client.insertRecord(tableQuizAttempt, quizAttemptId, "quiz", "id", quizAttempt.getQuiz().getId());
        client.insertRecord(tableQuizAttempt, quizAttemptId, "quiz", "name", quizAttempt.getQuiz().getName());
        client.insertRecord(tableQuizAttempt, quizAttemptId, "quiz", "min_grade", quizAttempt.getQuiz().getMin_grade().toString());
        client.insertRecord(tableQuizAttempt, quizAttemptId, "quiz", "duration", quizAttempt.getQuiz().getDuration().toString());

        client.insertRecord(tableQuizAttempt, quizAttemptId, "user", "id", quizAttempt.getUser().getId());
        client.insertRecord(tableQuizAttempt, quizAttemptId, "user", "name", quizAttempt.getUser().getName());

        client.insertRecord(tableQuizAttempt, quizAttemptId, "student", "id", quizAttempt.getStudent().getId());
        client.insertRecord(tableQuizAttempt, quizAttemptId, "student", "name", quizAttempt.getStudent().getName());

        // Get time now
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        client.insertRecord(tableQuizAttempt, quizAttemptId, "detail", "created_by", "Doyatama");
        client.insertRecord(tableQuizAttempt, quizAttemptId, "detail", "created_at", instant.toString());
        return quizAttempt;
    }

    public boolean deleteById(String quizAttemptId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, quizAttemptId);
        return true;
    }
}
