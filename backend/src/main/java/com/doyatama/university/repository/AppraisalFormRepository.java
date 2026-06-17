package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.AppraisalForm;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.util.*;

public class AppraisalFormRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "appraisal_forms";

    public List<AppraisalForm> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableAppraisalForms = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");

        return client.showListTable(tableAppraisalForms.toString(), columnMapping, AppraisalForm.class, size);
    }

    public AppraisalForm save(AppraisalForm appraisalForm) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString();

        TableName tableAppraisalForm = TableName.valueOf(tableName);
        client.insertRecord(tableAppraisalForm, rowKey, "main", "id", rowKey);
        client.insertRecord(tableAppraisalForm, rowKey, "main", "name", appraisalForm.getName());
        client.insertRecord(tableAppraisalForm, rowKey, "main", "description", appraisalForm.getDescription());
        client.insertRecord(tableAppraisalForm, rowKey, "detail", "created_by", "Doyatama");
        return appraisalForm;
    }

    public AppraisalForm findById(String appraisalFormId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableAppraisalForms = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");

        return client.showDataTable(tableAppraisalForms.toString(), columnMapping, appraisalFormId,
                AppraisalForm.class);
    }

    public List<AppraisalForm> findAllById(List<String> appraisalFormIds) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName table = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");

        List<AppraisalForm> appraisalForms = new ArrayList<>();
        for (String appraisalFormId : appraisalFormIds) {
            AppraisalForm appraisalForm = client.showDataTable(table.toString(), columnMapping, appraisalFormId,
                    AppraisalForm.class);
            if (appraisalForm != null) {
                appraisalForms.add(appraisalForm);
            }
        }

        return appraisalForms;
    }

    public AppraisalForm update(String appraisalFormId, AppraisalForm appraisalForm) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableAppraisalForm = TableName.valueOf(tableName);
        client.insertRecord(tableAppraisalForm, appraisalFormId, "main", "name", appraisalForm.getName());
        client.insertRecord(tableAppraisalForm, appraisalFormId, "main", "description", appraisalForm.getDescription());
        return appraisalForm;
    }

    public boolean deleteById(String appraisalFormId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, appraisalFormId);
        return true;
    }
}
