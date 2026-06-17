
package com.doyatama.university.repository;

import com.doyatama.university.controller.DepartmentController;
import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "questions";
    DepartmentController departmentController = new DepartmentController();

    public List<Question> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("title", "title");
        columnMapping.put("description", "description");
        columnMapping.put("question_type", "question_type");
        columnMapping.put("answer_type", "answer_type");
        columnMapping.put("file_path", "file_path");
        columnMapping.put("rps_detail", "rps_detail");
        columnMapping.put("examType", "examType");
        columnMapping.put("examType2", "examType2");
        columnMapping.put("examType3", "examType3");
        columnMapping.put("explanation", "explanation");

        return client.showListTable(tableUsers.toString(), columnMapping, Question.class, size);
    }

    public List<Question> findAllByRPSDetail(String rpsDetailID, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("title", "title");
        columnMapping.put("description", "description");
        columnMapping.put("question_type", "question_type");
        columnMapping.put("answer_type", "answer_type");
        columnMapping.put("file_path", "file_path");
        columnMapping.put("rps_detail", "rps_detail");
        columnMapping.put("exam_type", "exam_type");
        columnMapping.put("examType", "examType");
        columnMapping.put("examType2", "examType2");
        columnMapping.put("examType3", "examType3");
        columnMapping.put("explanation", "explanation");

        return client.getDataListByColumn(tableUsers.toString(), columnMapping, "rps_detail", "id", rpsDetailID,
                Question.class, size);
    }

    public List<Question> findAllByRPS(String rpsID, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("title", "title");
        columnMapping.put("description", "description");
        columnMapping.put("question_type", "question_type");
        columnMapping.put("answer_type", "answer_type");
        columnMapping.put("file_path", "file_path");
        columnMapping.put("rps_detail", "rps_detail");
        columnMapping.put("examType", "examType");
        columnMapping.put("examType2", "examType2");
        columnMapping.put("examType3", "examType3");
        columnMapping.put("explanation", "explanation");

        return client.getDataListByColumn(tableUsers.toString(), columnMapping, "detail", "rps_id", rpsID,
                Question.class, size);
    }

    public List<Question> findAllByRPSType(String rpsID, String type_exercise, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("title", "title");
        columnMapping.put("description", "description");
        columnMapping.put("question_type", "question_type");
        columnMapping.put("answer_type", "answer_type");
        columnMapping.put("file_path", "file_path");
        columnMapping.put("rps_detail", "rps_detail");
        columnMapping.put("examType", "examType");
        columnMapping.put("examType2", "examType2");
        columnMapping.put("examType3", "examType3");
        columnMapping.put("explanation", "explanation");
        columnMapping.put("type_exercise", type_exercise); // Add this line

        return client.getDataListByColumn(tableUsers.toString(), columnMapping, "detail", "rps_id", rpsID,
                Question.class, size);
    }

    public Question save(Question question) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString().substring(0, 20);

        TableName tableQuestion = TableName.valueOf(tableName);
        client.insertRecord(tableQuestion, rowKey, "main", "id", rowKey);
        client.insertRecord(tableQuestion, rowKey, "main", "title", question.getTitle());
        client.insertRecord(tableQuestion, rowKey, "main", "description", question.getDescription());
        client.insertRecord(tableQuestion, rowKey, "main", "question_type", question.getQuestionType().toString());
        client.insertRecord(tableQuestion, rowKey, "main", "answer_type", question.getAnswerType().toString());
        client.insertRecord(tableQuestion, rowKey, "main", "examType", question.getExamType().toString());
        client.insertRecord(tableQuestion, rowKey, "main", "examType2", question.getExamType2().toString());
        client.insertRecord(tableQuestion, rowKey, "main", "examType3", question.getExamType3().toString());
        client.insertRecord(tableQuestion, rowKey, "main", "explanation", question.getExplanation());

        if (question.getFile_path() != null && !question.getFile_path().isEmpty()) {
            client.insertRecord(tableQuestion, rowKey, "main", "file_path", question.getFile_path());
        }
        client.insertRecord(tableQuestion, rowKey, "rps_detail", "id", question.getRps_detail().getId());
        client.insertRecord(tableQuestion, rowKey, "rps_detail", "sub_cp_mk", question.getRps_detail().getSub_cp_mk());
        client.insertRecord(tableQuestion, rowKey, "detail", "rps_id", question.getRps_detail().getRps().getId());

        client.insertRecord(tableQuestion, rowKey, "detail", "created_by", "Doyatama");
        return question;
    }

    public Question findById(String questionId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("title", "title");
        columnMapping.put("description", "description");
        columnMapping.put("question_type", "question_type");
        columnMapping.put("answer_type", "answer_type");
        columnMapping.put("file_path", "file_path");
        columnMapping.put("rps_detail", "rps_detail");
        columnMapping.put("examType", "examType");
        columnMapping.put("examType2", "examType2");
        columnMapping.put("examType3", "examType3");
        columnMapping.put("explanation", "explanation");

        return client.showDataTable(tableUsers.toString(), columnMapping, questionId, Question.class);
    }

    public List<Question> findAllById(List<String> questionIds) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName table = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("title", "title");
        columnMapping.put("description", "description");
        columnMapping.put("question_type", "question_type");
        columnMapping.put("answer_type", "answer_type");
        columnMapping.put("file_path", "file_path");
        columnMapping.put("rps_detail", "rps_detail");
        columnMapping.put("examType", "examType");
        columnMapping.put("examType2", "examType2");
        columnMapping.put("examType3", "examType3");
        columnMapping.put("explanation", "explanation");

        List<Question> questions = new ArrayList<>();
        for (String questionId : questionIds) {
            Question question = client.showDataTable(table.toString(), columnMapping, questionId, Question.class);
            questions.add(question);
        }

        return questions;
    }

    public Question update(String questionId, Question question) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = question.getId();

        TableName tableQuestion = TableName.valueOf(tableName);
        client.insertRecord(tableQuestion, rowKey, "main", "id", rowKey);
        client.insertRecord(tableQuestion, rowKey, "main", "title", question.getTitle());
        client.insertRecord(tableQuestion, rowKey, "main", "description", question.getDescription());
        client.insertRecord(tableQuestion, rowKey, "main", "question_type", question.getQuestionType().toString());
        client.insertRecord(tableQuestion, rowKey, "main", "answer_type", question.getAnswerType().toString());
        client.insertRecord(tableQuestion, rowKey, "main", "examType", question.getExamType().toString());
        client.insertRecord(tableQuestion, rowKey, "main", "examType2", question.getExamType2().toString());
        client.insertRecord(tableQuestion, rowKey, "main", "examType3", question.getExamType3().toString());
        client.insertRecord(tableQuestion, rowKey, "main", "explanation", question.getExplanation());

        // if (question.getFile_path() != null && !question.getFile_path().isEmpty()) {
        // client.updateRecord(tableQuestion, rowKey, "main", "file_path",
        // question.getFile_path());
        // }
        client.insertRecord(tableQuestion, rowKey, "rps_detail", "id", question.getRps_detail().getId());
        client.insertRecord(tableQuestion, rowKey, "rps_detail", "sub_cp_mk", question.getRps_detail().getSub_cp_mk());
        client.insertRecord(tableQuestion, rowKey, "detail", "rps_id", question.getRps_detail().getRps().getId());

        client.insertRecord(tableQuestion, rowKey, "detail", "created_by", "Doyatama");
        return question;
    }

    public boolean deleteById(String questionId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, questionId);
        return true;
    }
}
