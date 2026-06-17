package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.AssessmentCriteria;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.util.*;

public class AssessmentCriteriaRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "assessment_criterias";


    public List<AssessmentCriteria> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableAssessmentCriterias = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");

        return client.showListTable(tableAssessmentCriterias.toString(), columnMapping, AssessmentCriteria.class, size);
    }

    public AssessmentCriteria save(AssessmentCriteria assessmentCriteria) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString();

        TableName tableAssessmentCriteria = TableName.valueOf(tableName);
        client.insertRecord(tableAssessmentCriteria, rowKey, "main", "id", rowKey);
        client.insertRecord(tableAssessmentCriteria, rowKey, "main", "name", assessmentCriteria.getName());
        client.insertRecord(tableAssessmentCriteria, rowKey, "main", "description", assessmentCriteria.getDescription());
        client.insertRecord(tableAssessmentCriteria, rowKey, "detail", "created_by", "Doyatama");
        return assessmentCriteria;
    }

    public AssessmentCriteria findById(String assessmentCriteriaId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableAssessmentCriterias = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");

        return client.showDataTable(tableAssessmentCriterias.toString(), columnMapping, assessmentCriteriaId, AssessmentCriteria.class);
    }

    public List<AssessmentCriteria> findAllById(List<String> assessmentCriteriaIds) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName table = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");

        List<AssessmentCriteria> assessmentCriterias = new ArrayList<>();
        for (String assessmentCriteriaId : assessmentCriteriaIds) {
            AssessmentCriteria assessmentCriteria = client.showDataTable(table.toString(), columnMapping, assessmentCriteriaId, AssessmentCriteria.class);
            if (assessmentCriteria != null) {
                assessmentCriterias.add(assessmentCriteria);
            }
        }

        return assessmentCriterias;
    }

    public AssessmentCriteria update(String assessmentCriteriaId, AssessmentCriteria assessmentCriteria) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableAssessmentCriteria = TableName.valueOf(tableName);
        client.insertRecord(tableAssessmentCriteria, assessmentCriteriaId, "main", "name", assessmentCriteria.getName());
        client.insertRecord(tableAssessmentCriteria, assessmentCriteriaId, "main", "description", assessmentCriteria.getDescription());
        return assessmentCriteria;
    }

    public boolean deleteById(String assessmentCriteriaId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, assessmentCriteriaId);
        return true;
    }
}
