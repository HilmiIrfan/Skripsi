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
import org.springframework.stereotype.Repository;

@Repository
public class QuizRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "quizzes";
    private String lastSavedRowKey;

    public List<Quiz> findAll(int size) throws IOException {
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
        columnMapping.put("message", "message");
        columnMapping.put("type_quiz", "type_quiz");
        columnMapping.put("devLecturerIds", "devLecturerIds");
        return client.showListTable(tableUsers.toString(), columnMapping, Quiz.class, size);
    }

    public Quiz save(Quiz quiz) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        lastSavedRowKey = UUID.randomUUID().toString();
        TableName tableQuiz = TableName.valueOf(tableName);

        saveCommonAttributes(quiz, client, lastSavedRowKey, tableQuiz);

        return quiz;
    }

    public String getLastSavedRowKey() {
        return lastSavedRowKey;
    }

    public Quiz saveWithQuestions(Quiz quiz, String rowKey) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableQuiz = TableName.valueOf(tableName);

        saveCommonAttributes(quiz, client, rowKey, tableQuiz);

        // questions
        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            Question question = quiz.getQuestions().get(i);
            client.insertRecord(tableQuiz, rowKey, "questions", "q_" + i, new Gson().toJson(question));
        }

        return quiz;
    }

    private void saveCommonAttributes(Quiz quiz, HBaseCustomClient client, String rowKey, TableName tableQuiz)
            throws IOException {
        client.insertRecord(tableQuiz, rowKey, "main", "id", rowKey);
        client.insertRecord(tableQuiz, rowKey, "main", "name", quiz.getName());
        client.insertRecord(tableQuiz, rowKey, "main", "description", quiz.getDescription().toString());
        client.insertRecord(tableQuiz, rowKey, "main", "min_grade", quiz.getMin_grade().toString());
        client.insertRecord(tableQuiz, rowKey, "main", "duration", quiz.getDuration().toString());
        client.insertRecord(tableQuiz, rowKey, "main", "date_start", quiz.getDate_start().toString());
        client.insertRecord(tableQuiz, rowKey, "main", "date_end", quiz.getDate_end().toString());
        // client.insertRecord(tableQuiz, rowKey, "main", "devLecturerIds",
        // quiz.getRps().getDev_lecturers().toString());
        client.insertRecord(tableQuiz, rowKey, "main", "message", quiz.getMessage().toString());
        client.insertRecord(tableQuiz, rowKey, "rps", "id", quiz.getRps().getId());
        client.insertRecord(tableQuiz, rowKey, "rps", "name", quiz.getRps().getName());
        client.insertRecord(tableQuiz, rowKey, "main", "type_quiz", quiz.getType_quiz());
        // Get time now
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        client.insertRecord(tableQuiz, rowKey, "detail", "created_by", "Doyatama");
        client.insertRecord(tableQuiz, rowKey, "detail", "created_at", instant.toString());
    }

    public Quiz findById(String quizId) throws IOException {
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
        return client.showDataTable(tableUsers.toString(), columnMapping, quizId, Quiz.class);
    }

    public Quiz findAnswer(String quizId) throws IOException {
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
        return client.showDataTable(tableUsers.toString(), columnMapping, quizId, Quiz.class);
    }

    public Quiz update(String quizId, Quiz quiz) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableQuiz = TableName.valueOf(tableName);
        client.insertRecord(tableQuiz, quizId, "main", "name", quiz.getName());
        client.insertRecord(tableQuiz, quizId, "main", "description", quiz.getDescription().toString());
        client.insertRecord(tableQuiz, quizId, "main", "min_grade", quiz.getMin_grade().toString());
        client.insertRecord(tableQuiz, quizId, "main", "duration", quiz.getDuration().toString());
        client.insertRecord(tableQuiz, quizId, "main", "date_start", quiz.getDate_start().toString());
        client.insertRecord(tableQuiz, quizId, "main", "date_end", quiz.getDate_end().toString());

        // questions
        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            Question question = quiz.getQuestions().get(i);
            client.insertRecord(tableQuiz, quizId, "questions", "q_" + i, new Gson().toJson(question));
        }

        client.insertRecord(tableQuiz, quizId, "rps", "id", quiz.getRps().getId());
        client.insertRecord(tableQuiz, quizId, "rps", "name", quiz.getRps().getName());

        // Get time now
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        client.insertRecord(tableQuiz, quizId, "detail", "created_by", "Doyatama");
        client.insertRecord(tableQuiz, quizId, "detail", "created_at", instant.toString());
        return quiz;
    }

    public boolean deleteById(String quizId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, quizId);
        return true;
    }
}
