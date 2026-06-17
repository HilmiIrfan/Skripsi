/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.KonsentrasiKeahlian;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.springframework.stereotype.Repository;

/**
 *
 * @author senja
 */
@Repository
public class KonsentrasiKeahlianRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "konsentrasiKeahlians";

    public List<KonsentrasiKeahlian> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableKonsentrasiKeahlian = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("konsentrasi", "konsentrasi");
        columnMapping.put("programKeahlian", "programKeahlian");
        return client.showListTable(tableKonsentrasiKeahlian.toString(), columnMapping, KonsentrasiKeahlian.class,
                size);
    }

    public KonsentrasiKeahlian save(KonsentrasiKeahlian konsentrasiKeahlian) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = konsentrasiKeahlian.getId();
        TableName tableKonsentrasiKeahlian = TableName.valueOf(tableName);
        client.insertRecord(tableKonsentrasiKeahlian, rowKey, "main", "id", rowKey);
        client.insertRecord(tableKonsentrasiKeahlian, rowKey, "main", "konsentrasi",
                konsentrasiKeahlian.getKonsentrasi());
        client.insertRecord(tableKonsentrasiKeahlian, rowKey, "programKeahlian", "id",
                konsentrasiKeahlian.getProgramKeahlian().getId());
        client.insertRecord(tableKonsentrasiKeahlian, rowKey, "programKeahlian", "program",
                konsentrasiKeahlian.getProgramKeahlian().getProgram());

        client.insertRecord(tableKonsentrasiKeahlian, rowKey, "detail", "created_by", "Doyatama");
        return konsentrasiKeahlian;
    }

    public KonsentrasiKeahlian findById(String BDGid) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableKonsentrasiKeahlian = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("konsentrasi", "konsentrasi");
        columnMapping.put("programKeahlian", "programKeahlian");

        return client.showDataTable(tableKonsentrasiKeahlian.toString(), columnMapping, BDGid,
                KonsentrasiKeahlian.class);
    }

    public List<KonsentrasiKeahlian> findAllById(List<String> BDGids) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableKonsentrasiKeahlian = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("konsentrasi", "konsentrasi");
        columnMapping.put("programKeahlian", "programKeahlian");

        List<KonsentrasiKeahlian> konsentrasiKeahlians = new ArrayList<>();
        for (String BDGid : BDGids) {
            KonsentrasiKeahlian konsentrasiKeahlian = client.showDataTable(tableKonsentrasiKeahlian.toString(),
                    columnMapping, BDGid, KonsentrasiKeahlian.class);
            if (konsentrasiKeahlian != null) {
                konsentrasiKeahlians.add(konsentrasiKeahlian);
            }
        }

        return konsentrasiKeahlians;
    }

    public List<KonsentrasiKeahlian> findKonsentrasiByProgram(String programId, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableProfile = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("id", "id");
        columnMapping.put("konsentrasi", "konsentrasi");
        columnMapping.put("programKeahlian", "programKeahlian");

        List<KonsentrasiKeahlian> konsentrasi = client.getDataListByColumn(tableProfile.toString(), columnMapping,
                "programKeahlian", "id", programId, KonsentrasiKeahlian.class, size);

        return konsentrasi;
    }

    public KonsentrasiKeahlian update(String BDGid, KonsentrasiKeahlian konsentrasiKeahlian) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableKonsentrasiKeahlian = TableName.valueOf(tableName);
        client.insertRecord(tableKonsentrasiKeahlian, BDGid, "main", "konsentrasi",
                konsentrasiKeahlian.getKonsentrasi());
        client.insertRecord(tableKonsentrasiKeahlian, BDGid, "programKeahlian", "id",
                konsentrasiKeahlian.getProgramKeahlian().getId());
        client.insertRecord(tableKonsentrasiKeahlian, BDGid, "programKeahlian", "program",
                konsentrasiKeahlian.getProgramKeahlian().getProgram());
        client.insertRecord(tableKonsentrasiKeahlian, BDGid, "detail", "created_by", "Doyatama");

        return konsentrasiKeahlian;
    }

    public boolean deleteById(String BDGid) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, BDGid);
        return true;
    }
}
