package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.Modul;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

public class ModulRepository {

    Configuration conf = HBaseConfiguration.create();
    String tableName = "modul";

    public List<Modul> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableModul = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idModul", "idModul");
        columnMapping.put("namaModul", "namaModul");

        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("school", "school");

        return client.showListTable(tableModul.toString(), columnMapping, Modul.class, size);
    }

    public Modul save(Modul modul) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = modul.getIdModul();
        TableName tableModul = TableName.valueOf(tableName);
        client.insertRecord(tableModul, rowKey, "main", "idModul", rowKey);
        client.insertRecord(tableModul, rowKey, "main", "namaModul", modul.getNamaModul());

        // Tahun Ajaran
        client.insertRecord(tableModul, rowKey, "tahunAjaran", "idTahun", modul.getTahunAjaran().getIdTahun());
        client.insertRecord(tableModul, rowKey, "tahunAjaran", "tahunAjaran",
                modul.getTahunAjaran().getTahunAjaran());

        // Semester
        client.insertRecord(tableModul, rowKey, "semester", "idSemester", modul.getSemester().getIdSemester());
        client.insertRecord(tableModul, rowKey, "semester", "namaSemester",
                modul.getSemester().getNamaSemester());

        // Kelas
        client.insertRecord(tableModul, rowKey, "kelas", "idKelas", modul.getKelas().getIdKelas());
        client.insertRecord(tableModul, rowKey, "kelas", "namaKelas", modul.getKelas().getNamaKelas());

        // Mapel
        client.insertRecord(tableModul, rowKey, "mapel", "idMapel", modul.getMapel().getIdMapel());
        client.insertRecord(tableModul, rowKey, "mapel", "name", modul.getMapel().getName());
        // Sekolah
        client.insertRecord(tableModul, rowKey, "school", "idSchool", modul.getSchool().getIdSchool());
        client.insertRecord(tableModul, rowKey, "school", "nameSchool", modul.getSchool().getNameSchool());

        return modul;
    }

    public Modul findById(String modulId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableModul = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idModul", "idModul");
        columnMapping.put("namaModul", "namaModul");

        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("school", "school");

        return client.showDataTable(tableModul.toString(), columnMapping, modulId, Modul.class);
    }

    public Modul findModulById(String modulId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableModul = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idModul", "idModul");
        columnMapping.put("namaModul", "namaModul");

        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("school", "school");

        return client.showDataTable(tableModul.toString(), columnMapping, modulId, Modul.class);
    }

    public List<Modul> findModulBySekolah(String schoolID, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableModul = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idModul", "idModul");
        columnMapping.put("namaModul", "namaModul");

        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("school", "school");

        List<Modul> modulList = client.getDataListByColumn(tableModul.toString(), columnMapping, "school", "idSchool",
                schoolID, Modul.class, size);

        return modulList;
    }

    public Modul update(String modulId, Modul modul) throws IOException {

        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableModul = TableName.valueOf(tableName);

        if (modul.getNamaModul() != null) {
            client.insertRecord(tableModul, modulId, "main", "namaModul", modul.getNamaModul());
        }

        // Tahun Ajaran
        if (modul.getTahunAjaran() != null) {
            client.insertRecord(tableModul, modulId, "tahunAjaran", "idTahun", modul.getTahunAjaran().getIdTahun());
            client.insertRecord(tableModul, modulId, "tahunAjaran", "tahunAjaran",
                    modul.getTahunAjaran().getTahunAjaran());
        }

        // Semester
        if (modul.getSemester() != null) {
            client.insertRecord(tableModul, modulId, "semester", "idSemester", modul.getSemester().getIdSemester());
            client.insertRecord(tableModul, modulId, "semester", "namaSemester",
                    modul.getSemester().getNamaSemester());
        }

        // Kelas
        if (modul.getKelas() != null) {
            client.insertRecord(tableModul, modulId, "kelas", "idKelas", modul.getKelas().getIdKelas());
            client.insertRecord(tableModul, modulId, "kelas", "namaKelas", modul.getKelas().getNamaKelas());
        }

        // Mapel
        if (modul.getMapel() != null) {
            client.insertRecord(tableModul, modulId, "mapel", "idMapel", modul.getMapel().getIdMapel());
            client.insertRecord(tableModul, modulId, "mapel", "name", modul.getMapel().getName());
        }

        // Sekolah
        if (modul.getSchool() != null) {
            client.insertRecord(tableModul, modulId, "school", "idSchool", modul.getSchool().getIdSchool());
            client.insertRecord(tableModul, modulId, "school", "nameSchool", modul.getSchool().getNameSchool());
        }

        return modul;
    }

    public boolean deleteById(String modulId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, modulId);
        return true;
    }

    public boolean existsById(String modulId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableModul = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idModul", "idModul");

        Modul modul = client.getDataByColumn(tableModul.toString(), columnMapping, "main", "idModul", modulId,
                Modul.class);
        return modul.getIdModul() != null;
    }
}
