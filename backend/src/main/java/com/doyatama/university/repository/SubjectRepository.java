package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.Subject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.util.*;

public class SubjectRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "subjects";

    public List<Subject> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("credit_point", "credit_point");
        columnMapping.put("year_commenced", "year_commenced");
        columnMapping.put("study_program", "study_program");
        columnMapping.put("subject_group", "subject_group");
        return client.showListTable(tableUsers.toString(), columnMapping, Subject.class, size);
    }

    public Subject save(Subject subject) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString();

        TableName tableSubject = TableName.valueOf(tableName);
        client.insertRecord(tableSubject, rowKey, "main", "id", rowKey);
        client.insertRecord(tableSubject, rowKey, "main", "name", subject.getName());
        client.insertRecord(tableSubject, rowKey, "main", "description", subject.getDescription());
        client.insertRecord(tableSubject, rowKey, "main", "credit_point", subject.getCredit_point().toString());
        client.insertRecord(tableSubject, rowKey, "main", "year_commenced", subject.getYear_commenced().toString());
        client.insertRecord(tableSubject, rowKey, "study_program", "id", subject.getStudyProgram().getId());
        client.insertRecord(tableSubject, rowKey, "study_program", "name", subject.getStudyProgram().getName());
        client.insertRecord(tableSubject, rowKey, "subject_group", "id", subject.getSubject_group().getId());
        client.insertRecord(tableSubject, rowKey, "subject_group", "name", subject.getSubject_group().getName());
        client.insertRecord(tableSubject, rowKey, "detail", "created_by", "Doyatama");
        return subject;
    }

    public Subject findById(String subjectId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("credit_point", "credit_point");
        columnMapping.put("year_commenced", "year_commenced");
        columnMapping.put("study_program", "study_program");
        columnMapping.put("subject_group", "subject_group");
        return client.showDataTable(tableUsers.toString(), columnMapping, subjectId, Subject.class);
    }

    public List<Subject> findAllById(List<String> subjectIds) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName table = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("credit_point", "credit_point");
        columnMapping.put("year_commenced", "year_commenced");
        columnMapping.put("study_program", "study_program");
        columnMapping.put("subject_group", "subject_group");

        List<Subject> subjects = new ArrayList<>();
        for (String subjectId : subjectIds) {
            Subject subject = client.showDataTable(table.toString(), columnMapping, subjectId, Subject.class);
            if (subject != null) {
                subjects.add(subject);
            }
        }

        return subjects;
    }

    public List<Subject> findRelationById(List<String> subjectIds) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName table = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("credit_point", "credit_point");
        columnMapping.put("year_commenced", "year_commenced");

        List<Subject> subjects = new ArrayList<>();
        for (String subjectId : subjectIds) {
            Subject subject = client.showDataTable(table.toString(), columnMapping, subjectId, Subject.class);
            if (subject != null) {
                subjects.add(subject);
            }
        }

        return subjects;
    }

    public Subject update(String subjectId, Subject subject) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableSubject = TableName.valueOf(tableName);
        client.insertRecord(tableSubject, subjectId, "main", "name", subject.getName());
        client.insertRecord(tableSubject, subjectId, "main", "description", subject.getDescription());
        client.insertRecord(tableSubject, subjectId, "main", "credit_point", subject.getDescription().toString());
        client.insertRecord(tableSubject, subjectId, "main", "year_commenced", subject.getDescription().toString());
        client.insertRecord(tableSubject, subjectId, "study_program", "id", subject.getStudyProgram().getId());
        client.insertRecord(tableSubject, subjectId, "study_program", "name", subject.getStudyProgram().getName());
        client.insertRecord(tableSubject, subjectId, "subject_group", "id", subject.getSubject_group().getId());
        client.insertRecord(tableSubject, subjectId, "subject_group", "name", subject.getSubject_group().getName());

        return subject;
    }

    public boolean deleteById(String subjectId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, subjectId);
        return true;
    }
}
