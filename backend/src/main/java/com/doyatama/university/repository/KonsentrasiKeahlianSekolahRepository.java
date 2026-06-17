package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.KonsentrasiKeahlianSekolah;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

public class KonsentrasiKeahlianSekolahRepository {

    Configuration conf = HBaseConfiguration.create();
    String tableName = "konsentrasiKeahlianSekolah";

    public List<KonsentrasiKeahlianSekolah> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableKonsentrasiKeahlianSekolah = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idKonsentrasiSekolah", "idKonsentrasiSekolah");
        columnMapping.put("namaKonsentrasiSekolah", "namaKonsentrasiSekolah");
        columnMapping.put("school", "school");
        columnMapping.put("konsentrasiKeahlian", "konsentrasiKeahlian");

        return client.showListTable(tableKonsentrasiKeahlianSekolah.toString(), columnMapping,
                KonsentrasiKeahlianSekolah.class,
                size);
    }

    public KonsentrasiKeahlianSekolah save(KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolah) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = konsentrasiKeahlianSekolah.getIdKonsentrasiSekolah();
        TableName tableKonsentrasiKeahlianSekolah = TableName.valueOf(tableName);
        // Map<String, String> columnMapping = new HashMap<>();

        client.insertRecord(tableKonsentrasiKeahlianSekolah, rowKey, "main", "idKonsentrasiSekolah",
                konsentrasiKeahlianSekolah.getIdKonsentrasiSekolah());
        client.insertRecord(tableKonsentrasiKeahlianSekolah, rowKey, "main", "namaKonsentrasiSekolah",
                konsentrasiKeahlianSekolah.getNamaKonsentrasiSekolah());

        // Sekolah
        client.insertRecord(tableKonsentrasiKeahlianSekolah, rowKey, "school", "idSchool",
                konsentrasiKeahlianSekolah.getSchool().getIdSchool());
        client.insertRecord(tableKonsentrasiKeahlianSekolah, rowKey, "school", "nameSchool",
                konsentrasiKeahlianSekolah.getSchool().getNameSchool());

        // Konsentrasi Keahlian
        client.insertRecord(tableKonsentrasiKeahlianSekolah, rowKey, "konsentrasiKeahlian", "id",
                konsentrasiKeahlianSekolah.getKonsentrasiKeahlian().getId());
        client.insertRecord(tableKonsentrasiKeahlianSekolah, rowKey, "konsentrasiKeahlian", "konsentrasi",
                konsentrasiKeahlianSekolah.getKonsentrasiKeahlian().getKonsentrasi());

        return konsentrasiKeahlianSekolah;
    }

    public KonsentrasiKeahlianSekolah findKonsentrasiKeahlianSekolahById(String konsentrasiKeahlianSekolahId)
            throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableKonsentrasiKeahlianSekolah = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idKonsentrasiSekolah", "idKonsentrasiSekolah");
        columnMapping.put("namaKonsentrasiSekolah", "namaKonsentrasiSekolah");
        columnMapping.put("school", "school");
        columnMapping.put("konsentrasiKeahlian", "konsentrasiKeahlian");

        return client.showDataTable(tableKonsentrasiKeahlianSekolah.toString(), columnMapping,
                konsentrasiKeahlianSekolahId, KonsentrasiKeahlianSekolah.class);
    }

    public KonsentrasiKeahlianSekolah findById(String konsentrasiKeahlianSekolahId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableKonsentrasiKeahlianSekolah = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idKonsentrasiSekolah", "idKonsentrasiSekolah");
        columnMapping.put("namaKonsentrasiSekolah", "namaKonsentrasiSekolah");
        columnMapping.put("school", "school");
        columnMapping.put("konsentrasiKeahlian", "konsentrasiKeahlian");

        return client.showDataTable(tableKonsentrasiKeahlianSekolah.toString(), columnMapping,
                konsentrasiKeahlianSekolahId, KonsentrasiKeahlianSekolah.class);
    }

    public List<KonsentrasiKeahlianSekolah> findKonsentrasiKeahlianSekolahBySekolah(String schoolID, int size)
            throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableKonsentrasiKeahlianSekolah = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idKonsentrasiSekolah", "idKonsentrasiSekolah");
        columnMapping.put("namaKonsentrasiSekolah", "namaKonsentrasiSekolah");
        columnMapping.put("school", "school");
        columnMapping.put("konsentrasiKeahlian", "konsentrasiKeahlian");

        List<KonsentrasiKeahlianSekolah> konsentrasiKeahlianSekolah = client.getDataListByColumn(
                tableKonsentrasiKeahlianSekolah.toString(), columnMapping,
                "school", "idSchool",
                schoolID, KonsentrasiKeahlianSekolah.class,
                size);

        return konsentrasiKeahlianSekolah;
    }

    public KonsentrasiKeahlianSekolah update(String konsentrasiKeahlianSekolahId,
            KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolah) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableKonsentrasiKeahlianSekolah = TableName.valueOf(tableName);
        // Map<String, String> columnMapping = new HashMap<>();

        if (konsentrasiKeahlianSekolah.getNamaKonsentrasiSekolah() != null) {
            client.insertRecord(tableKonsentrasiKeahlianSekolah, konsentrasiKeahlianSekolahId, "main",
                    "namaKonsentrasiSekolah", konsentrasiKeahlianSekolah.getNamaKonsentrasiSekolah());
        }

        // Sekolah
        if (konsentrasiKeahlianSekolah.getSchool() != null) {
            client.insertRecord(tableKonsentrasiKeahlianSekolah, konsentrasiKeahlianSekolahId, "school",
                    "idSchool", konsentrasiKeahlianSekolah.getSchool().getIdSchool());
            client.insertRecord(tableKonsentrasiKeahlianSekolah, konsentrasiKeahlianSekolahId, "school",
                    "nameSchool", konsentrasiKeahlianSekolah.getSchool().getNameSchool());
        }

        // Konsentrasi Keahlian
        if (konsentrasiKeahlianSekolah.getKonsentrasiKeahlian() != null) {
            client.insertRecord(tableKonsentrasiKeahlianSekolah, konsentrasiKeahlianSekolahId, "konsentrasiKeahlian",
                    "id", konsentrasiKeahlianSekolah.getKonsentrasiKeahlian().getId());
            client.insertRecord(tableKonsentrasiKeahlianSekolah, konsentrasiKeahlianSekolahId, "konsentrasiKeahlian",
                    "konsentrasi", konsentrasiKeahlianSekolah.getKonsentrasiKeahlian().getKonsentrasi());
        }

        return konsentrasiKeahlianSekolah;
    }

    public boolean deleteById(String konsentrasiKeahlianSekolahId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, konsentrasiKeahlianSekolahId);
        return true;
    }

    public boolean existsById(String konsentrasiKeahlianSekolahId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableKonsentrasiKeahlianSekolah = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("idKonsentrasiSekolah", "idKonsentrasiSekolah");

        KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolah = client.getDataByColumn(
                tableKonsentrasiKeahlianSekolah.toString(),
                columnMapping, "main", "idKonsentrasiSekolah", konsentrasiKeahlianSekolahId,
                KonsentrasiKeahlianSekolah.class);

        return konsentrasiKeahlianSekolah.getIdKonsentrasiSekolah() != null;
    }

}
