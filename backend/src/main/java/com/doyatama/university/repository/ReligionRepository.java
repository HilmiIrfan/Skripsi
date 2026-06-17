package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.Religion;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReligionRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "religions";

    public List<Religion> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");

        return client.showListTable(tableUsers.toString(), columnMapping, Religion.class, size);
    }

    public Religion save(Religion religion) throws IOException {
    HBaseCustomClient client = new HBaseCustomClient(conf);

    String rowKey = UUID.randomUUID().toString();

    TableName tableReligion = TableName.valueOf(tableName);
    client.insertRecord(tableReligion, rowKey, "main", "id", rowKey);

    if (religion.getName() != null) {
        client.insertRecord(tableReligion, rowKey, "main", "name", religion.getName());
    }

    if (religion.getDescription() != null) {
        client.insertRecord(tableReligion, rowKey, "main", "description", religion.getDescription());
    }

    client.insertRecord(tableReligion, rowKey, "detail", "created_by", "Doyatama");
    return religion;
}

    public Religion findById(String religionId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");

        return client.showDataTable(tableUsers.toString(), columnMapping, religionId, Religion.class);
    }

    public Religion update(String religionId, Religion religion) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableReligion = TableName.valueOf(tableName);
        client.insertRecord(tableReligion, religionId, "main", "name", religion.getName());
        client.insertRecord(tableReligion, religionId, "main", "description", religion.getDescription());
        return religion;
    }

    public boolean deleteById(String religionId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, religionId);
        return true;
    }
}
