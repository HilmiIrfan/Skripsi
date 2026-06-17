package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.JenisSoal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

public class JenisSoalRepository {

    Configuration conf = HBaseConfiguration.create();
    String tableName = "jenis_soal";

    public List<JenisSoal> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableJenisSoal = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idJenisSoal", "idJenisSoal");
        columnMapping.put("namaJenisSoal", "namaJenisSoal");
        columnMapping.put("school", "school");
        return client.showListTable(tableJenisSoal.toString(), columnMapping, JenisSoal.class, size);
    }

    public JenisSoal save(JenisSoal jenisSoal) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = jenisSoal.getIdJenisSoal();
        TableName tableJenisSoal = TableName.valueOf(tableName);
        client.insertRecord(tableJenisSoal, rowKey, "main", "idJenisSoal", rowKey);
        client.insertRecord(tableJenisSoal, rowKey, "main", "namaJenisSoal", jenisSoal.getNamaJenisSoal());
        // School
        client.insertRecord(tableJenisSoal, rowKey, "school", "idSchool", jenisSoal.getSchool().getIdSchool());
        client.insertRecord(tableJenisSoal, rowKey, "school", "nameSchool", jenisSoal.getSchool().getNameSchool());

        client.insertRecord(tableJenisSoal, rowKey, "detail", "created_by", "Polinema");
        return jenisSoal;
    }

    public JenisSoal findById(String jenisSoalId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableJenisSoal = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idJenisSoal", "idJenisSoal");
        columnMapping.put("namaJenisSoal", "namaJenisSoal");
        columnMapping.put("school", "school");

        return client.showDataTable(tableJenisSoal.toString(), columnMapping, jenisSoalId, JenisSoal.class);
    }

    public List<JenisSoal> findAllById(List<String> jenisSoalIds) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableJenisSoal = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idJenisSoal", "idJenisSoal");
        columnMapping.put("namaJenisSoal", "namaJenisSoal");
        columnMapping.put("school", "school");

        List<JenisSoal> jenisSoalList = new ArrayList<>();
        for (String jenisSoalId : jenisSoalIds) {
            JenisSoal jenisSoal = client.showDataTable(tableJenisSoal.toString(), columnMapping, jenisSoalId,
                    JenisSoal.class);
            if (jenisSoal != null) {
                jenisSoalList.add(jenisSoal);
            }
        }
        return jenisSoalList;
    }

    public List<JenisSoal> findJenisSoalBySekolah(String schoolId, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableJenisSoal = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idJenisSoal", "idJenisSoal");
        columnMapping.put("namaJenisSoal", "namaJenisSoal");
        columnMapping.put("school", "school");

        List<JenisSoal> jenisSoalList = client.getDataListByColumn(tableJenisSoal.toString(), columnMapping, "school",
                "idSchool", schoolId, JenisSoal.class, size);
        return jenisSoalList;
    }

    public JenisSoal update(String jenisSoalId, JenisSoal jenisSoal) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableJenisSoal = TableName.valueOf(tableName);

        if (jenisSoal.getNamaJenisSoal() != null) {
            client.insertRecord(tableJenisSoal, jenisSoalId, "main", "namaJenisSoal", jenisSoal.getNamaJenisSoal());
        }

        // School
        if (jenisSoal.getSchool() != null) {
            client.insertRecord(tableJenisSoal, jenisSoalId, "school", "idSchool", jenisSoal.getSchool().getIdSchool());
            client.insertRecord(tableJenisSoal, jenisSoalId, "school", "nameSchool",
                    jenisSoal.getSchool().getNameSchool());
        }

        return jenisSoal;
    }

    public boolean deleteById(String jenisSoalId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        client.deleteRecord(tableName, jenisSoalId);
        return true;
    }

    public boolean existById(String jenisSoalId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableJenisSoal = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idJenisSoal", "idJenisSoal");

        JenisSoal jenisSoal = client.getDataByColumn(tableJenisSoal.toString(), columnMapping, "main",
                "idJenisSoal", jenisSoalId, JenisSoal.class);
        return jenisSoal.getIdJenisSoal() != null;
    }
}
