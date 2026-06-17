package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.Chapter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChapterRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "chapters";

    public List<Chapter> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("course_id", "course_id");

        return client.showListTable(tableUsers.toString(), columnMapping, Chapter.class, size);
    }

    public Chapter save(Chapter chapter) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString();

        TableName tableSubject = TableName.valueOf(tableName);
        client.insertRecord(tableSubject, rowKey, "main", "id", rowKey);
        client.insertRecord(tableSubject, rowKey, "main", "name", chapter.getName());
        client.insertRecord(tableSubject, rowKey, "main", "description", chapter.getDescription());
        client.insertRecord(tableSubject, rowKey, "main", "course_id", chapter.getCourse_id());
        client.insertRecord(tableSubject, rowKey, "detail", "created_by", "Doyatama");
        return chapter;
    }

    public Chapter findById(String chapterId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("course_id", "course_id");

        return client.showDataTable(tableUsers.toString(), columnMapping, chapterId, Chapter.class);
    }

    public Chapter update(String chapterId, Chapter chapter) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableSubject = TableName.valueOf(tableName);
        client.insertRecord(tableSubject, chapterId, "main", "name", chapter.getName());
        client.insertRecord(tableSubject, chapterId, "main", "description", chapter.getDescription());
        client.insertRecord(tableSubject, chapterId, "main", "course_id", chapter.getCourse_id());
        return chapter;
    }

    public boolean deleteById(String chapterId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, chapterId);
        return true;
    }
}
