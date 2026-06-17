package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.LearningMethod;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.util.*;

public class LearningMethodRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "learning_methods";

    public List<LearningMethod> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableLearningMethods = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");

        return client.showListTable(tableLearningMethods.toString(), columnMapping, LearningMethod.class, size);
    }

    public LearningMethod save(LearningMethod learningMethod) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString();

        TableName tableLearningMethod = TableName.valueOf(tableName);
        client.insertRecord(tableLearningMethod, rowKey, "main", "id", rowKey);
        client.insertRecord(tableLearningMethod, rowKey, "main", "name", learningMethod.getName());
        client.insertRecord(tableLearningMethod, rowKey, "main", "description", learningMethod.getDescription());
        client.insertRecord(tableLearningMethod, rowKey, "detail", "created_by", "Doyatama");
        return learningMethod;
    }

    public LearningMethod findById(String learningMethodId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableLearningMethods = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");

        return client.showDataTable(tableLearningMethods.toString(), columnMapping, learningMethodId,
                LearningMethod.class);
    }

    public List<LearningMethod> findAllById(List<String> learningMethodIds) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName table = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");

        List<LearningMethod> learningMethods = new ArrayList<>();
        for (String learningMethodId : learningMethodIds) {
            LearningMethod learningMethod = client.showDataTable(table.toString(), columnMapping, learningMethodId,
                    LearningMethod.class);
            if (learningMethod != null) {
                learningMethods.add(learningMethod);
            }
        }

        return learningMethods;
    }

    public LearningMethod update(String learningMethodId, LearningMethod learningMethod) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableLearningMethod = TableName.valueOf(tableName);
        client.insertRecord(tableLearningMethod, learningMethodId, "main", "name", learningMethod.getName());
        client.insertRecord(tableLearningMethod, learningMethodId, "main", "description",
                learningMethod.getDescription());
        return learningMethod;
    }

    public boolean deleteById(String learningMethodId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, learningMethodId);
        return true;
    }
}
