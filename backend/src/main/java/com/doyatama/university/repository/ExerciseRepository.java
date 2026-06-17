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

public class ExerciseRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "exercises";

    public List<Exercise> findAll(int size) throws IOException {
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
        columnMapping.put("type_exercise", "type_exercise");
        return client.showListTable(tableUsers.toString(), columnMapping, Exercise.class, size);
    }

    public Exercise save(Exercise exercise) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString();

        TableName tableExercise = TableName.valueOf(tableName);
        client.insertRecord(tableExercise, rowKey, "main", "id", rowKey);
        client.insertRecord(tableExercise, rowKey, "main", "name", exercise.getName());
        client.insertRecord(tableExercise, rowKey, "main", "description", exercise.getDescription().toString());
        client.insertRecord(tableExercise, rowKey, "main", "min_grade", exercise.getMin_grade().toString());
        client.insertRecord(tableExercise, rowKey, "main", "duration", exercise.getDuration().toString());
        client.insertRecord(tableExercise, rowKey, "main", "date_start", exercise.getDate_start().toString());
        client.insertRecord(tableExercise, rowKey, "main", "date_end", exercise.getDate_end().toString());
        client.insertRecord(tableExercise, rowKey, "main", "type_exercise", exercise.getType_exercise());
        if (exercise.getQuestions() != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            for (int i = 0; i < exercise.getQuestions().size(); i++) {
                Question question = exercise.getQuestions().get(i);
                String questionJson = objectMapper.writeValueAsString(question);
                client.insertRecord(tableExercise, rowKey, "questions", "q_" + i, questionJson);
            }
        }

        client.insertRecord(tableExercise, rowKey, "rps", "id", exercise.getRps().getId());
        client.insertRecord(tableExercise, rowKey, "rps", "name", exercise.getRps().getName());

        // Get time now
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        client.insertRecord(tableExercise, rowKey, "detail", "created_by", "Doyatama");
        client.insertRecord(tableExercise, rowKey, "detail", "created_at", instant.toString());
        return exercise;
    }

    public Exercise findById(String exerciseId) throws IOException {
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

        return client.showDataTable(tableUsers.toString(), columnMapping, exerciseId, Exercise.class);
    }

    public Exercise findAnswer(String exerciseId) throws IOException {
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
        return client.showDataTable(tableUsers.toString(), columnMapping, exerciseId, Exercise.class);
    }

    public Exercise update(String exerciseId, Exercise exercise) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableExercise = TableName.valueOf(tableName);
        client.insertRecord(tableExercise, exerciseId, "main", "name", exercise.getName());
        client.insertRecord(tableExercise, exerciseId, "main", "description", exercise.getDescription().toString());
        client.insertRecord(tableExercise, exerciseId, "main", "min_grade", exercise.getMin_grade().toString());
        client.insertRecord(tableExercise, exerciseId, "main", "duration", exercise.getDuration().toString());
        client.insertRecord(tableExercise, exerciseId, "main", "date_start", exercise.getDate_start().toString());
        client.insertRecord(tableExercise, exerciseId, "main", "date_end", exercise.getDate_end().toString());
        client.insertRecord(tableExercise, exerciseId, "main", "type_exercise", exercise.getType_exercise());
        // questions
        if (exercise.getQuestions() != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            for (int i = 0; i < exercise.getQuestions().size(); i++) {
                Question question = exercise.getQuestions().get(i);
                try {
                    String questionJson = objectMapper.writeValueAsString(question);
                    client.insertRecord(tableExercise, exerciseId, "questions", "q_" + i, questionJson);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        client.insertRecord(tableExercise, exerciseId, "rps", "id", exercise.getRps().getId());
        client.insertRecord(tableExercise, exerciseId, "rps", "name", exercise.getRps().getName());

        // Get time now
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        client.insertRecord(tableExercise, exerciseId, "detail", "created_by", "Doyatama");
        client.insertRecord(tableExercise, exerciseId, "detail", "created_at", instant.toString());
        return exercise;
    }

    public boolean deleteById(String exerciseId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, exerciseId);
        return true;
    }
}
