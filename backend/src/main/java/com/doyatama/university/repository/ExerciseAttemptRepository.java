package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Repository;

@Repository
public class ExerciseAttemptRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "exercise_attempts";
    // private QuestionRepository questionRepository = new QuestionRepository();

    // private final ObjectMapper objectMapper;

    public ExerciseAttemptRepository(ObjectMapper objectMapper) {

    }

    public List<ExerciseAttempt> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("grade", "grade");
        columnMapping.put("total_right", "total_right");
        columnMapping.put("state", "state");
        columnMapping.put("student_answers", "student_answers");
        columnMapping.put("exercise", "exercise");
        columnMapping.put("user", "user");
        columnMapping.put("student", "student");
        columnMapping.put("duration", "duration");
        columnMapping.put("created_at", "created_at");
        return client.showListTable(tableUsers.toString(), columnMapping, ExerciseAttempt.class, size);
    }

    public List<ExerciseAttempt> findByUser(String userID, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("grade", "grade");
        columnMapping.put("total_right", "total_right");
        columnMapping.put("state", "state");
        columnMapping.put("student_answers", "student_answers");
        columnMapping.put("exercise", "exercise");
        columnMapping.put("user", "user");
        columnMapping.put("student", "student");
        columnMapping.put("duration", "duration");
        columnMapping.put("created_at", "created_at");
        return client.getDataListByColumn(tableUsers.toString(), columnMapping, "user", "id", userID,
                ExerciseAttempt.class, size);
    }

    public List<ExerciseAttempt> findByExercise(String exerciseID, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("grade", "grade");
        columnMapping.put("total_right", "total_right");
        columnMapping.put("state", "state");
        columnMapping.put("student_answers", "student_answers");
        columnMapping.put("exercise", "exercise");
        columnMapping.put("user", "user");
        columnMapping.put("student", "student");
        columnMapping.put("duration", "duration");
        columnMapping.put("created_at", "created_at");
        return client.getDataListByColumn(tableUsers.toString(), columnMapping, "exercise", "id", exerciseID,
                ExerciseAttempt.class, size);
    }

    public List<ExerciseAttempt> findByExerciseAttemptId(String exerciseAttemptId, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("grade", "grade");
        columnMapping.put("total_right", "total_right");
        columnMapping.put("state", "state");
        columnMapping.put("student_answers", "student_answers");
        columnMapping.put("exercise", "exercise");
        columnMapping.put("user", "user");
        columnMapping.put("student", "student");
        columnMapping.put("duration", "duration");
        columnMapping.put("created_at", "created_at");

        // Assuming that getDataListByColumn supports filtering by exerciseAttempt id
        return client.getDataListByColumn(tableUsers.toString(), columnMapping, "exercise_attempt", "id",
                exerciseAttemptId, ExerciseAttempt.class, size);
    }

    public ExerciseAttempt save(ExerciseAttempt exerciseAttempt) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString();

        TableName tableExerciseAttempt = TableName.valueOf(tableName);
        client.insertRecord(tableExerciseAttempt, rowKey, "main", "id", rowKey);
        client.insertRecord(tableExerciseAttempt, rowKey, "main", "grade", String.valueOf(exerciseAttempt.getGrade()));
        client.insertRecord(tableExerciseAttempt, rowKey, "main", "total_right",
                exerciseAttempt.getTotal_right().toString());
        client.insertRecord(tableExerciseAttempt, rowKey, "main", "state", exerciseAttempt.getState());
        client.insertRecord(tableExerciseAttempt, rowKey, "main", "duration", exerciseAttempt.getDuration().toString());

        // // questions
        // for (int i = 0; i < exerciseAttempt.getStudent_answers().size(); i++) {
        // StudentAnswer studentAnswer = exerciseAttempt.getStudent_answers().get(i);
        // client.insertRecord(tableExerciseAttempt, rowKey, "student_answers", "sa_" +
        // i, new Gson().toJson(studentAnswer));
        // }
        // questions
        ObjectMapper mapper = new ObjectMapper(); // Define ObjectMapper instance
        mapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule
        for (int i = 0; i < exerciseAttempt.getStudent_answers().size(); i++) {
            StudentAnswer studentAnswer = exerciseAttempt.getStudent_answers().get(i);
            String studentAnswerJson = mapper.writeValueAsString(studentAnswer);
            client.insertRecord(tableExerciseAttempt, rowKey, "student_answers", "sa_" + i, studentAnswerJson);
        }

        client.insertRecord(tableExerciseAttempt, rowKey, "exercise", "id", exerciseAttempt.getExercise().getId());
        client.insertRecord(tableExerciseAttempt, rowKey, "exercise", "name", exerciseAttempt.getExercise().getName());
        client.insertRecord(tableExerciseAttempt, rowKey, "exercise", "min_grade",
                exerciseAttempt.getExercise().getMin_grade().toString());
        client.insertRecord(tableExerciseAttempt, rowKey, "exercise", "duration",
                exerciseAttempt.getExercise().getDuration().toString());

        client.insertRecord(tableExerciseAttempt, rowKey, "user", "id", exerciseAttempt.getUser().getId());
        client.insertRecord(tableExerciseAttempt, rowKey, "user", "name", exerciseAttempt.getUser().getName());

        client.insertRecord(tableExerciseAttempt, rowKey, "student", "id", exerciseAttempt.getStudent().getId());
        client.insertRecord(tableExerciseAttempt, rowKey, "student", "name", exerciseAttempt.getStudent().getName());

        // Get time now
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        client.insertRecord(tableExerciseAttempt, rowKey, "detail", "created_by", "Doyatama");
        client.insertRecord(tableExerciseAttempt, rowKey, "detail", "created_at", instant.toString());
        return exerciseAttempt;
    }

    public ExerciseAttempt findById(String exerciseAttemptId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("grade", "grade");
        columnMapping.put("total_right", "total_right");
        columnMapping.put("state", "state");
        columnMapping.put("student_answers", "student_answers");
        columnMapping.put("exercise", "exercise");
        columnMapping.put("user", "user");
        columnMapping.put("student", "student");
        columnMapping.put("duration", "duration");
        columnMapping.put("created_at", "created_at");

        ExerciseAttempt exerciseAttempt = client.showDataTable(tableUsers.toString(), columnMapping, exerciseAttemptId,
                ExerciseAttempt.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Convert the student_answers field into a List<StudentAnswer>
        List<StudentAnswer> studentAnswers = objectMapper.convertValue(
                exerciseAttempt.getStudentAnswers(),
                new TypeReference<List<StudentAnswer>>() {
                });

        // Set the converted student_answers field in the ExerciseAttempt object
        exerciseAttempt.setStudent_answers(studentAnswers);

        return exerciseAttempt;
    }

    public ExerciseAttempt findAnswer(String exerciseAttemptId) throws IOException {
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
        return client.showDataTable(tableUsers.toString(), columnMapping, exerciseAttemptId, ExerciseAttempt.class);
    }

    public ExerciseAttempt update(String exerciseAttemptId, ExerciseAttempt exerciseAttempt) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableExerciseAttempt = TableName.valueOf(tableName);
        client.insertRecord(tableExerciseAttempt, exerciseAttemptId, "main", "grade",
                String.valueOf(exerciseAttempt.getGrade()));
        client.insertRecord(tableExerciseAttempt, exerciseAttemptId, "main", "state", exerciseAttempt.getState());
        client.insertRecord(tableExerciseAttempt, exerciseAttemptId, "main", "duration",
                exerciseAttempt.getDuration().toString());

        // questions
        for (int i = 0; i < exerciseAttempt.getStudent_answers().size(); i++) {
            StudentAnswer studentAnswer = exerciseAttempt.getStudent_answers().get(i);
            client.insertRecord(tableExerciseAttempt, exerciseAttemptId, "student_answers", "sa_" + i,
                    new Gson().toJson(studentAnswer));
        }

        client.insertRecord(tableExerciseAttempt, exerciseAttemptId, "exercise", "id",
                exerciseAttempt.getExercise().getId());
        client.insertRecord(tableExerciseAttempt, exerciseAttemptId, "exercise", "name",
                exerciseAttempt.getExercise().getName());
        client.insertRecord(tableExerciseAttempt, exerciseAttemptId, "exercise", "min_grade",
                exerciseAttempt.getExercise().getMin_grade().toString());
        client.insertRecord(tableExerciseAttempt, exerciseAttemptId, "exercise", "duration",
                exerciseAttempt.getExercise().getDuration().toString());

        client.insertRecord(tableExerciseAttempt, exerciseAttemptId, "user", "id", exerciseAttempt.getUser().getId());
        client.insertRecord(tableExerciseAttempt, exerciseAttemptId, "user", "name",
                exerciseAttempt.getUser().getName());

        client.insertRecord(tableExerciseAttempt, exerciseAttemptId, "student", "id",
                exerciseAttempt.getStudent().getId());
        client.insertRecord(tableExerciseAttempt, exerciseAttemptId, "student", "name",
                exerciseAttempt.getStudent().getName());

        // Get time now
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        client.insertRecord(tableExerciseAttempt, exerciseAttemptId, "detail", "created_by", "Doyatama");
        client.insertRecord(tableExerciseAttempt, exerciseAttemptId, "detail", "created_at", instant.toString());
        return exerciseAttempt;
    }

    public boolean deleteById(String exerciseAttemptId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, exerciseAttemptId);
        return true;
    }
}
