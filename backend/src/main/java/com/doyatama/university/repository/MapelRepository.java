/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.Mapel;
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
public class MapelRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "mapels";

    public List<Mapel> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableMapel = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idMapel", "idMapel");
        columnMapping.put("name", "name");
        columnMapping.put("school", "school");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");

        return client.showListTable(tableMapel.toString(), columnMapping, Mapel.class, size);
    }

    public Mapel save(Mapel mapel) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = mapel.getIdMapel();
        TableName tableMapel = TableName.valueOf(tableName);
        client.insertRecord(tableMapel, rowKey, "main", "idMapel", rowKey);
        client.insertRecord(tableMapel, rowKey, "main", "name", mapel.getName());

        // Sekolah
        client.insertRecord(tableMapel, rowKey, "school", "idSchool", mapel.getSchool().getIdSchool());
        client.insertRecord(tableMapel, rowKey, "school", "nameSchool", mapel.getSchool().getNameSchool());

        // Tahun Ajaran
        client.insertRecord(tableMapel, rowKey, "tahunAjaran", "idTahun", mapel.getTahunAjaran().getIdTahun());
        client.insertRecord(tableMapel, rowKey, "tahunAjaran", "tahunAjaran", mapel.getTahunAjaran().getTahunAjaran());

        // Semester
        client.insertRecord(tableMapel, rowKey, "semester", "idSemester", mapel.getSemester().getIdSemester());
        client.insertRecord(tableMapel, rowKey, "semester", "namaSemester", mapel.getSemester().getNamaSemester());

        // Kelas
        client.insertRecord(tableMapel, rowKey, "kelas", "idKelas", mapel.getKelas().getIdKelas());
        client.insertRecord(tableMapel, rowKey, "kelas", "namaKelas", mapel.getKelas().getNamaKelas());

        client.insertRecord(tableMapel, rowKey, "detail", "created_by", "Doyatama");
        return mapel;
    }

    public Mapel findById(String mapelId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableMapel = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idMapel", "idMapel");
        columnMapping.put("name", "name");
        columnMapping.put("school", "school");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");

        return client.showDataTable(tableMapel.toString(), columnMapping, mapelId, Mapel.class);
    }

    public List<Mapel> findAllById(List<String> mapelIds) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableMapel = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idMapel", "idMapel");
        columnMapping.put("name", "name");
        columnMapping.put("school", "school");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");

        List<Mapel> mapels = new ArrayList<>();
        for (String mapelId : mapelIds) {
            Mapel mapel = client.showDataTable(tableMapel.toString(), columnMapping, mapelId, Mapel.class);
            if (mapel != null) {
                mapels.add(mapel);
            }
        }

        return mapels;
    }

    public List<Mapel> findAllByIds(List<List<String>> mapelIdsList) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableMapel = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idMapel", "idMapel");
        columnMapping.put("name", "name");
        columnMapping.put("school", "school");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");

        List<Mapel> mapels = new ArrayList<>();

        // Iterate through each List<String> inside List<List<String>>
        for (List<String> mapelIds : mapelIdsList) {
            for (String mapelId : mapelIds) {
                Mapel mapel = client.showDataTable(tableMapel.toString(), columnMapping, mapelId, Mapel.class);
                if (mapel != null) {
                    mapels.add(mapel);
                }
            }
        }

        return mapels;
    }

    public List<Mapel> findMapelBySekolah(String schoolID, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableMapel = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idMapel", "idMapel");
        columnMapping.put("name", "name");
        columnMapping.put("school", "school");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");

        List<Mapel> mapels = client.getDataListByColumn(tableMapel.toString(), columnMapping, "school", "idSchool",
                schoolID, Mapel.class, size);
        return mapels;
    }

    public Mapel update(String mapelId, Mapel mapel) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableMapel = TableName.valueOf(tableName);

        if (mapel.getName() != null) {
            client.insertRecord(tableMapel, mapelId, "main", "name", mapel.getName());
        }

        // Sekolah
        if (mapel.getSchool().getIdSchool() != null) {
            client.insertRecord(tableMapel, mapelId, "school", "idSchool", mapel.getSchool().getIdSchool());
        }
        if (mapel.getSchool().getNameSchool() != null) {
            client.insertRecord(tableMapel, mapelId, "school", "nameSchool", mapel.getSchool().getNameSchool());
        }

        // Tahun Ajaran
        if (mapel.getTahunAjaran().getIdTahun() != null) {
            client.insertRecord(tableMapel, mapelId, "tahunAjaran", "idTahun", mapel.getTahunAjaran().getIdTahun());
        }
        if (mapel.getTahunAjaran().getTahunAjaran() != null) {
            client.insertRecord(tableMapel, mapelId, "tahunAjaran", "tahunAjaran",
                    mapel.getTahunAjaran().getTahunAjaran());
        }

        // Semester
        if (mapel.getSemester().getIdSemester() != null) {
            client.insertRecord(tableMapel, mapelId, "semester", "idSemester", mapel.getSemester().getIdSemester());
        }
        if (mapel.getSemester().getNamaSemester() != null) {
            client.insertRecord(tableMapel, mapelId, "semester", "namaSemester", mapel.getSemester().getNamaSemester());
        }

        // Kelas
        if (mapel.getKelas().getIdKelas() != null) {
            client.insertRecord(tableMapel, mapelId, "kelas", "idKelas", mapel.getKelas().getIdKelas());
        }
        if (mapel.getKelas().getNamaKelas() != null) {
            client.insertRecord(tableMapel, mapelId, "kelas", "namaKelas", mapel.getKelas().getNamaKelas());
        }

        return mapel;
    }

    public boolean deleteById(String mapelId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, mapelId);
        return true;
    }

    public boolean existsById(String mapelId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableMapel = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idMapel", "idMapel");

        Mapel mapel = client.getDataByColumn(tableMapel.toString(), columnMapping, "main", "idMapel", mapelId,
                Mapel.class);
        return mapel.getIdMapel() != null;
    }
}
