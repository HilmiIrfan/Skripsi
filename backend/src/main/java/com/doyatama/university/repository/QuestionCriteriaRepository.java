package com.doyatama.university.repository;



import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.QuestionCriteria;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author alfa
 */
public class QuestionCriteriaRepository {
    Configuration conf = HBaseConfiguration.create();

    String tableName = "question_criterias";

    public List<QuestionCriteria> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(this.tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("category", "category");

        return client.showListTable(tableUsers.toString(), columnMapping, QuestionCriteria.class, size);
    }

    public QuestionCriteria save(QuestionCriteria questionCriteria) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString().substring(0, 5);

        TableName tableQuestionCriteria = TableName.valueOf(this.tableName);

        client.insertRecord(tableQuestionCriteria, rowKey, "main", "id", rowKey);

        if (questionCriteria.getName() != null) {
            client.insertRecord(tableQuestionCriteria, rowKey, "main", "name", questionCriteria.getName());
        }

        if (questionCriteria.getDescription() != null) {
            client.insertRecord(tableQuestionCriteria, rowKey, "main", "description", questionCriteria.getDescription());
        }

        if (questionCriteria.getCategory() != null) {
            client.insertRecord(tableQuestionCriteria, rowKey, "main", "category", questionCriteria.getCategory());
        }

        client.insertRecord(tableQuestionCriteria, rowKey, "detail", "created_by", "alfa");

        return questionCriteria;
    }

    public QuestionCriteria findById(String questionCriteriaId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(this.tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("category", "category");

        return client.showDataTable(tableUsers.toString(), columnMapping, questionCriteriaId, QuestionCriteria.class);
    }
    public QuestionCriteria update(QuestionCriteria questionCriteria) throws IOException {
    if (questionCriteria == null || questionCriteria.getId() == null) {
        throw new IllegalArgumentException("QuestionCriteria and QuestionCriteria ID cannot be null");
    }

    HBaseCustomClient client = new HBaseCustomClient(conf);
    TableName tableQuestionCriteria = TableName.valueOf(this.tableName);
    String questionCriteriaId = questionCriteria.getId();

    if (questionCriteria.getName() != null) {
        client.insertRecord(tableQuestionCriteria, questionCriteriaId, "main", "name", questionCriteria.getName());
    }
    if (questionCriteria.getDescription() != null) {
        client.insertRecord(tableQuestionCriteria, questionCriteriaId, "main", "description", questionCriteria.getDescription());
    }
    if (questionCriteria.getCategory() != null) {
        client.insertRecord(tableQuestionCriteria, questionCriteriaId, "main", "category", questionCriteria.getCategory());
    }

    return questionCriteria;
}

    public boolean deleteById(String questionCriteriaId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(this.tableName, questionCriteriaId);
        return true;
    }

}
