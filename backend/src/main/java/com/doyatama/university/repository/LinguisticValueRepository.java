package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.LinguisticValue;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author alfa
 */


public class LinguisticValueRepository {

    Configuration conf = HBaseConfiguration.create();
    String tableName = "linguistic_values";

    public List<LinguisticValue> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("value1", "value1");
        columnMapping.put("value2", "value2");
        columnMapping.put("value3", "value3");
        columnMapping.put("value4", "value4");
        columnMapping.put("avg", "avg");
        columnMapping.put("file_path", "file_path");
        



        return client.showListTable(tableUsers.toString(), columnMapping, LinguisticValue.class, size);
    }

    public LinguisticValue save(LinguisticValue linguisticValue) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString().substring(0, 5);

        TableName tableLinguisticValue = TableName.valueOf(tableName);

        client.insertRecord(tableLinguisticValue, rowKey, "main", "id", rowKey);

        if (linguisticValue.getName() != null) {
            client.insertRecord(tableLinguisticValue, rowKey, "main", "name", linguisticValue.getName());
        }

        if (linguisticValue.getValue1() != 0) {
            client.insertRecord(tableLinguisticValue, rowKey, "main", "value1", String.valueOf(linguisticValue.getValue1()));
        }

        if (linguisticValue.getValue2() != 0) {
            client.insertRecord(tableLinguisticValue, rowKey, "main", "value2", String.valueOf(linguisticValue.getValue2()));
        }

        if (linguisticValue.getValue3() != 0) {
            client.insertRecord(tableLinguisticValue, rowKey, "main", "value3", String.valueOf(linguisticValue.getValue3()));
        }

        if (linguisticValue.getValue4() != 0) {
            client.insertRecord(tableLinguisticValue, rowKey, "main", "value4", String.valueOf(linguisticValue.getValue4()));
        }

        if (linguisticValue.getAvg() != 0) {
            client.insertRecord(tableLinguisticValue, rowKey, "main", "avg", String.valueOf(linguisticValue.getAvg()));
        }
        if (linguisticValue.getFile_path() != null) {
            client.insertRecord(tableLinguisticValue, rowKey, "main", "file_path", linguisticValue.getFile_path());
        }

        return linguisticValue;
    }

    public LinguisticValue findById(String linguisticValueId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        if (linguisticValueId == null) {
            throw new IllegalArgumentException("LinguisticValue ID cannot be null");
        }
        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("value1", "value1");
        columnMapping.put("value2", "value2");
        columnMapping.put("value3", "value3");
        columnMapping.put("value4", "value4");
        columnMapping.put("avg", "avg");
        columnMapping.put("file_path", "file_path");

        return client.showDataTable(tableUsers.toString(), columnMapping, linguisticValueId, LinguisticValue.class);
    }

    public LinguisticValue update(LinguisticValue linguisticValue) throws IOException {
        if (linguisticValue == null || linguisticValue.getId() == null) {
            throw new IllegalArgumentException("LinguisticValue and LinguisticValue ID cannot be null");
        }

        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableLinguisticValue = TableName.valueOf(this.tableName);
        String linguisticValueId = linguisticValue.getId();

        if (linguisticValue.getName() != null) {
            client.insertRecord(tableLinguisticValue, linguisticValueId, "main", "name", linguisticValue.getName());
        }
        if (String.valueOf(linguisticValue.getValue1()) != null) {
            client.insertRecord(tableLinguisticValue, linguisticValueId, "main", "value1", String.valueOf(linguisticValue.getValue1()));
        }
        if (String.valueOf(linguisticValue.getValue2()) != null) {
            client.insertRecord(tableLinguisticValue, linguisticValueId, "main", "value2", String.valueOf(linguisticValue.getValue2()));
        }
        if (String.valueOf(linguisticValue.getValue3()) != null) {
            client.insertRecord(tableLinguisticValue, linguisticValueId, "main", "value3", String.valueOf(linguisticValue.getValue3()));
        }
        if (String.valueOf(linguisticValue.getValue4()) != null) {
            client.insertRecord(tableLinguisticValue, linguisticValueId, "main", "value4", String.valueOf(linguisticValue.getValue4()));
        }
        if (String.valueOf(linguisticValue.getAvg()) != null) {
            client.insertRecord(tableLinguisticValue, linguisticValueId, "main", "avg", String.valueOf(linguisticValue.getAvg()));
        }
        
        return linguisticValue;
    }
    public boolean deleteById(String linguisticValueId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, linguisticValueId);
        return true;
    }
    
}
