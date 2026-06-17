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
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author alfa
 */
public class QuizAnnouncementRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "quizzes_announcement";

    public List<QuizAnnouncement> findAll(int size) throws IOException {
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
        return client.showListTable(tableUsers.toString(), columnMapping, QuizAnnouncement.class, size);
    }

    public QuizAnnouncement save(QuizAnnouncement quizAnnouncement) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString();

        TableName tableQuizAnnouncement = TableName.valueOf(tableName);
        client.insertRecord(tableQuizAnnouncement, rowKey, "main", "id", rowKey);
        client.insertRecord(tableQuizAnnouncement, rowKey, "main", "name", quizAnnouncement.getName());
        client.insertRecord(tableQuizAnnouncement, rowKey, "main", "description",
                quizAnnouncement.getDescription().toString());
        client.insertRecord(tableQuizAnnouncement, rowKey, "main", "min_grade",
                quizAnnouncement.getMin_grade().toString());
        client.insertRecord(tableQuizAnnouncement, rowKey, "main", "duration",
                quizAnnouncement.getDuration().toString());
        client.insertRecord(tableQuizAnnouncement, rowKey, "main", "date_start",
                quizAnnouncement.getDate_start().toString());
        client.insertRecord(tableQuizAnnouncement, rowKey, "main", "date_end",
                quizAnnouncement.getDate_end().toString());
        client.insertRecord(tableQuizAnnouncement, rowKey, "main", "type_quiz", quizAnnouncement.getType_quiz());

        if (quizAnnouncement.getQuestions() != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            for (int i = 0; i < quizAnnouncement.getQuestions().size(); i++) {
                Question question = quizAnnouncement.getQuestions().get(i);
                String questionJson = objectMapper.writeValueAsString(question);
                client.insertRecord(tableQuizAnnouncement, rowKey, "questions", "q_" + i, questionJson);
            }
        }
        client.insertRecord(tableQuizAnnouncement, rowKey, "main", "message", quizAnnouncement.getMessage().toString());
        client.insertRecord(tableQuizAnnouncement, rowKey, "rps", "id", quizAnnouncement.getRps().getId());
        client.insertRecord(tableQuizAnnouncement, rowKey, "rps", "name", quizAnnouncement.getRps().getName());

        // Get time now
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        client.insertRecord(tableQuizAnnouncement, rowKey, "detail", "created_by", "Doyatama");
        client.insertRecord(tableQuizAnnouncement, rowKey, "detail", "created_at", instant.toString());

        return quizAnnouncement;
    }

    public QuizAnnouncement findById(String quizId) throws IOException {
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
        return client.showDataTable(tableUsers.toString(), columnMapping, quizId, QuizAnnouncement.class);
    }

    public QuizAnnouncement findAnswer(String quizId) throws IOException {
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
        return client.showDataTable(tableUsers.toString(), columnMapping, quizId, QuizAnnouncement.class);
    }

    public QuizAnnouncement update(String quizId, QuizAnnouncement quiz) throws IOException {
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
