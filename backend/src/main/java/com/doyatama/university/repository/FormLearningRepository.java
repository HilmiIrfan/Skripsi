package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.FormLearning;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FormLearningRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "form_learnings";

    public List<FormLearning> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableFormLearnings = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");

        return client.showListTable(tableFormLearnings.toString(), columnMapping, FormLearning.class, size);
    }

    public FormLearning save(FormLearning department) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString();

        TableName tableFormLearning = TableName.valueOf(tableName);
        client.insertRecord(tableFormLearning, rowKey, "main", "id", rowKey);
        client.insertRecord(tableFormLearning, rowKey, "main", "name", department.getName());
        client.insertRecord(tableFormLearning, rowKey, "main", "description", department.getDescription());
        client.insertRecord(tableFormLearning, rowKey, "detail", "created_by", "Doyatama");
        return department;
    }

    public FormLearning findById(String departmentId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableFormLearnings = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");

        return client.showDataTable(tableFormLearnings.toString(), columnMapping, departmentId, FormLearning.class);
    }

    public FormLearning update(String departmentId, FormLearning department) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableFormLearning = TableName.valueOf(tableName);
        client.insertRecord(tableFormLearning, departmentId, "main", "name", department.getName());
        client.insertRecord(tableFormLearning, departmentId, "main", "description", department.getDescription());
        return department;
    }

    public boolean deleteById(String departmentId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, departmentId);
        return true;
    }
}
