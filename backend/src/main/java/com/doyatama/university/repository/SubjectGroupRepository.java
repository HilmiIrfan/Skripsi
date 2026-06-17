package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.SubjectGroup;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SubjectGroupRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "subject_groups";

    public List<SubjectGroup> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableSubjectGroups = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");

        return client.showListTable(tableSubjectGroups.toString(), columnMapping, SubjectGroup.class, size);
    }

    public SubjectGroup save(SubjectGroup subjectGroup) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString();

        TableName tableSubjectGroup = TableName.valueOf(tableName);
        client.insertRecord(tableSubjectGroup, rowKey, "main", "id", rowKey);
        client.insertRecord(tableSubjectGroup, rowKey, "main", "name", subjectGroup.getName());
        client.insertRecord(tableSubjectGroup, rowKey, "main", "description", subjectGroup.getDescription());
        client.insertRecord(tableSubjectGroup, rowKey, "detail", "created_by", "Doyatama");
        return subjectGroup;
    }

    public SubjectGroup findById(String subjectGroupId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableSubjectGroups = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");

        return client.showDataTable(tableSubjectGroups.toString(), columnMapping, subjectGroupId, SubjectGroup.class);
    }

    public SubjectGroup update(String subjectGroupId, SubjectGroup subjectGroup) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableSubjectGroup = TableName.valueOf(tableName);
        client.insertRecord(tableSubjectGroup, subjectGroupId, "main", "name", subjectGroup.getName());
        client.insertRecord(tableSubjectGroup, subjectGroupId, "main", "description", subjectGroup.getDescription());
        return subjectGroup;
    }

    public boolean deleteById(String subjectGroupId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, subjectGroupId);
        return true;
    }
}
