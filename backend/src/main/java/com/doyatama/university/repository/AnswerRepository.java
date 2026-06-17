
package com.doyatama.university.repository;

import com.doyatama.university.controller.DepartmentController;
import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.Answer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.util.*;

public class AnswerRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "answers";
    DepartmentController departmentController = new DepartmentController();

    public List<Answer> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("title", "title");
        columnMapping.put("description", "description");
        columnMapping.put("type", "type");
        columnMapping.put("is_right", "is_right");
        columnMapping.put("file_path", "file_path");
        columnMapping.put("question", "question");
        return client.showListTable(tableUsers.toString(), columnMapping, Answer.class, size);
    }

    public Answer save(Answer answer) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString();
        TableName tableAnswer = TableName.valueOf(tableName);
        client.insertRecord(tableAnswer, rowKey, "main", "id", rowKey);
        client.insertRecord(tableAnswer, rowKey, "main", "title", answer.getTitle());
        client.insertRecord(tableAnswer, rowKey, "main", "description", answer.getDescription());
        client.insertRecord(tableAnswer, rowKey, "main", "type", answer.getType().toString());
        client.insertRecord(tableAnswer, rowKey, "main", "is_right", answer.getIs_right().toString());
        client.insertRecord(tableAnswer, rowKey, "main", "file_path", answer.getFile_path());
        client.insertRecord(tableAnswer, rowKey, "question", "id", answer.getQuestion().getId());
        client.insertRecord(tableAnswer, rowKey, "question", "title", answer.getQuestion().getTitle());
        client.insertRecord(tableAnswer, rowKey, "detail", "created_by", "Doyatama");
        return answer;
    }

    public Answer findById(String answerId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("title", "title");
        columnMapping.put("description", "description");
        columnMapping.put("type", "type");
        columnMapping.put("is_right", "is_right");
        columnMapping.put("file_path", "file_path");
        columnMapping.put("question", "question");

        return client.showDataTable(tableUsers.toString(), columnMapping, answerId, Answer.class);
    }

    public List<Answer> findAnswerByQuestion(String questionId, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("title", "title");
        columnMapping.put("description", "description");
        columnMapping.put("type", "type");
        columnMapping.put("is_right", "is_right");
        columnMapping.put("file_path", "file_path");
        columnMapping.put("question", "question");

        List<Answer> answer = client.getDataListByColumn(tableUsers.toString(), columnMapping, "question", "id",
                questionId, Answer.class, size);

        return answer;
    }

    public List<Answer> findAllById(List<String> answerIds) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName table = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("title", "title");
        columnMapping.put("description", "description");
        columnMapping.put("type", "type");
        columnMapping.put("is_right", "is_right");
        columnMapping.put("file_path", "file_path");
        columnMapping.put("question", "question");

        List<Answer> answers = new ArrayList<>();
        for (String answerId : answerIds) {
            Answer answer = client.showDataTable(table.toString(), columnMapping, answerId, Answer.class);
            if (answer != null) {
                answers.add(answer);
            }
        }

        return answers;
    }

    public Answer update(String answerId, Answer answer) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableAnswer = TableName.valueOf(tableName);
        client.insertRecord(tableAnswer, answerId, "main", "title", answer.getTitle());
        client.insertRecord(tableAnswer, answerId, "main", "description", answer.getDescription());
        client.insertRecord(tableAnswer, answerId, "main", "type", answer.getType().toString());
        client.insertRecord(tableAnswer, answerId, "main", "is_right", answer.getIs_right().toString());
        client.insertRecord(tableAnswer, answerId, "main", "file_path", answer.getFile_path());
        client.insertRecord(tableAnswer, answerId, "question", "id", answer.getQuestion().getId());
        client.insertRecord(tableAnswer, answerId, "question", "title", answer.getQuestion().getTitle());
        client.insertRecord(tableAnswer, answerId, "detail", "created_by", "Doyatama");
        return answer;
    }

    public boolean deleteById(String answerId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, answerId);
        return true;
    }
}
