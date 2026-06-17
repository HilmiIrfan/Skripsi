package com.doyatama.university.repository;

/**
 * @author alfa
 */

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.ExamType;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.util.*;

public class ExamTypeRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "exam_types";

    public List<ExamType> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableExamTypes = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");

        return client.showListTable(tableExamTypes.toString(), columnMapping, ExamType.class, size);
    }

    public ExamType save(ExamType examType) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString().substring(0, 5);

        TableName tableExamType = TableName.valueOf(tableName);
        client.insertRecord(tableExamType, rowKey, "main", "id", rowKey);
        client.insertRecord(tableExamType, rowKey, "main", "name", examType.getName());
        client.insertRecord(tableExamType, rowKey, "main", "description", examType.getDescription());
        client.insertRecord(tableExamType, rowKey, "detail", "created_by", "Doyatama");
        return examType;
    }

    public ExamType findById(String examTypeId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableExamTypes = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");

        return client.showDataTable(tableExamTypes.toString(), columnMapping, examTypeId, ExamType.class);
    }

    public List<ExamType> findAllById(List<String> examTypeIds) throws IOException {
        if (examTypeIds == null) {
            throw new IllegalArgumentException("examTypeIds cannot be null");
        }

        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName table = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");

        List<ExamType> examTypes = new ArrayList<>();
        for (String examTypeId : examTypeIds) {
            ExamType examType = client.showDataTable(table.toString(), columnMapping, examTypeId, ExamType.class);
            if (examType != null) {
                examTypes.add(examType);
            }
        }

        return examTypes;
    }

    public ExamType update(String examTypeId, ExamType examType) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableExamType = TableName.valueOf(tableName);
        client.insertRecord(tableExamType, examTypeId, "main", "name", examType.getName());
        client.insertRecord(tableExamType, examTypeId, "main", "description", examType.getDescription());
        return examType;
    }

    public boolean deleteById(String examTypeId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, examTypeId);
        return true;
    }
}