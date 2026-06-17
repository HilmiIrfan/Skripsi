package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.LearningMedia;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.util.*;

public class LearningMediaRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "learning_medias";

    public List<LearningMedia> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableLearningMedias = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("type", "type");

        return client.showListTable(tableLearningMedias.toString(), columnMapping, LearningMedia.class, size);
    }

    public LearningMedia save(LearningMedia learningMedia) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString();

        TableName tableLearningMedia = TableName.valueOf(tableName);
        client.insertRecord(tableLearningMedia, rowKey, "main", "id", rowKey);
        client.insertRecord(tableLearningMedia, rowKey, "main", "name", learningMedia.getName());
        client.insertRecord(tableLearningMedia, rowKey, "main", "description", learningMedia.getDescription());
        client.insertRecord(tableLearningMedia, rowKey, "main", "type", learningMedia.getType().toString());
        client.insertRecord(tableLearningMedia, rowKey, "detail", "created_by", "Doyatama");
        return learningMedia;
    }

    public LearningMedia findById(String learningMediaId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableLearningMedias = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("type", "type");

        return client.showDataTable(tableLearningMedias.toString(), columnMapping, learningMediaId,
                LearningMedia.class);
    }

    public List<LearningMedia> findByType(String type, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName table = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("type", "type");

        return client.getDataListByColumn(table.toString(), columnMapping, "main", "type", type, LearningMedia.class,
                size);
    }

    public List<LearningMedia> findAllById(List<String> learningMediaIds) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName table = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("type", "type");

        List<LearningMedia> learningMedias = new ArrayList<>();
        for (String learningMediaId : learningMediaIds) {
            LearningMedia learningMedia = client.showDataTable(table.toString(), columnMapping, learningMediaId,
                    LearningMedia.class);
            if (learningMedia != null) {
                learningMedias.add(learningMedia);
            }
        }

        return learningMedias;
    }

    public LearningMedia update(String learningMediaId, LearningMedia learningMedia) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableLearningMedia = TableName.valueOf(tableName);
        client.insertRecord(tableLearningMedia, learningMediaId, "main", "name", learningMedia.getName());
        client.insertRecord(tableLearningMedia, learningMediaId, "main", "description", learningMedia.getDescription());
        client.insertRecord(tableLearningMedia, learningMediaId, "main", "type", learningMedia.getType().toString());
        return learningMedia;
    }

    public boolean deleteById(String learningMediaId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, learningMediaId);
        return true;
    }
}
