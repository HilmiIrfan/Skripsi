package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.Elemen;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

public class ElemenRepository {

    Configuration conf = HBaseConfiguration.create();
    String tableName = "elemen";

    public List<Elemen> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableElemen = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idElemen", "idElemen");
        columnMapping.put("namaElemen", "namaElemen");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("school", "school");

        return client.showListTable(tableElemen.toString(), columnMapping, Elemen.class, size);
    }

    public Elemen save(Elemen elemen) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = elemen.getIdElemen();
        TableName tableElemen = TableName.valueOf(tableName);
        // Map<String, String> columnMapping = new HashMap<>();

        client.insertRecord(tableElemen, rowKey, "main", "idElemen", elemen.getIdElemen());
        client.insertRecord(tableElemen, rowKey, "main", "namaElemen", elemen.getNamaElemen());

        // Tahun Ajaran
        client.insertRecord(tableElemen, rowKey, "tahunAjaran", "idTahun", elemen.getTahunAjaran().getIdTahun());
        client.insertRecord(tableElemen, rowKey, "tahunAjaran", "tahunAjaran",
                elemen.getTahunAjaran().getTahunAjaran());
        // Semester
        client.insertRecord(tableElemen, rowKey, "semester", "idSemester", elemen.getSemester().getIdSemester());
        client.insertRecord(tableElemen, rowKey, "semester", "namaSemester",
                elemen.getSemester().getNamaSemester());
        // Kelas
        client.insertRecord(tableElemen, rowKey, "kelas", "idKelas", elemen.getKelas().getIdKelas());
        client.insertRecord(tableElemen, rowKey, "kelas", "namaKelas", elemen.getKelas().getNamaKelas());
        // Mapel
        if (elemen.getMapel() != null) {
            client.insertRecord(tableElemen, rowKey, "mapel", "idMapel", elemen.getMapel().getIdMapel());
            client.insertRecord(tableElemen, rowKey, "mapel", "name", elemen.getMapel().getName());
        }
        // Konsentrasi Keahlian
        client.insertRecord(tableElemen, rowKey, "konsentrasiKeahlianSekolah", "idKonsentrasiSekolah",
                elemen.getKonsentrasiKeahlianSekolah().getIdKonsentrasiSekolah());
        client.insertRecord(tableElemen, rowKey, "konsentrasiKeahlianSekolah", "namaKonsentrasiSekolah",
                elemen.getKonsentrasiKeahlianSekolah().getNamaKonsentrasiSekolah());
        // Sekolah
        client.insertRecord(tableElemen, rowKey, "school", "idSchool", elemen.getSchool().getIdSchool());
        client.insertRecord(tableElemen, rowKey, "school", "nameSchool", elemen.getSchool().getNameSchool());

        client.insertRecord(tableElemen, rowKey, "detail", "created_by", "Polinema");

        return elemen;

    }

    public Elemen findElemenById(String elemenId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableElemen = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("idElemen", "idElemen");
        columnMapping.put("namaElemen", "namaElemen");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("school", "school");

        return client.showDataTable(tableElemen.toString(), columnMapping, elemenId, Elemen.class);
    }

    public Elemen findById(String elemenId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableElemen = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("idElemen", "idElemen");
        columnMapping.put("namaElemen", "namaElemen");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("school", "school");

        return client.showDataTable(tableElemen.toString(), columnMapping, elemenId, Elemen.class);
    }

    public List<Elemen> findElemenByMapel(String mapelID, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableElemen = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("idElemen", "idElemen");
        columnMapping.put("namaElemen", "namaElemen");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("school", "school");

        List<Elemen> elemen = client.getDataListByColumn(tableElemen.toString(), columnMapping, "mapel", "idMapel",
                mapelID, Elemen.class, size);

        return elemen;
    }

    public List<Elemen> findElemenBySekolah(String schoolID, int size) throws IOException {

        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableElemen = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("idElemen", "idElemen");
        columnMapping.put("namaElemen", "namaElemen");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("school", "school");

        List<Elemen> elemen = client.getDataListByColumn(tableElemen.toString(), columnMapping, "school", "idSchool",
                schoolID, Elemen.class, size);

        return elemen;
    }

    public Elemen update(String elemenId, Elemen elemen) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableElemen = TableName.valueOf(tableName);
        // Map<String, String> columnMapping = new HashMap<>();

        if (elemen.getNamaElemen() != null) {
            client.insertRecord(tableElemen, elemenId, "main", "namaElemen", elemen.getNamaElemen());
        }

        // Tahun Ajaran
        if (elemen.getTahunAjaran() != null) {
            client.insertRecord(tableElemen, elemenId, "tahunAjaran", "idTahun", elemen.getTahunAjaran().getIdTahun());
            client.insertRecord(tableElemen, elemenId, "tahunAjaran", "tahunAjaran",
                    elemen.getTahunAjaran().getTahunAjaran());
        }

        // Semester
        if (elemen.getSemester() != null) {
            client.insertRecord(tableElemen, elemenId, "semester", "idSemester", elemen.getSemester().getIdSemester());
            client.insertRecord(tableElemen, elemenId, "semester", "namaSemester",
                    elemen.getSemester().getNamaSemester());
        }

        // Kelas
        if (elemen.getKelas() != null) {
            client.insertRecord(tableElemen, elemenId, "kelas", "idKelas", elemen.getKelas().getIdKelas());
            client.insertRecord(tableElemen, elemenId, "kelas", "namaKelas", elemen.getKelas().getNamaKelas());
        }

        // Mapel
        if (elemen.getMapel() != null) {
            client.insertRecord(tableElemen, elemenId, "mapel", "idMapel", elemen.getMapel().getIdMapel());
            client.insertRecord(tableElemen, elemenId, "mapel", "name", elemen.getMapel().getName());
        }

        // Konsentrasi Keahlian
        if (elemen.getKonsentrasiKeahlianSekolah() != null) {
            client.insertRecord(tableElemen, elemenId, "konsentrasiKeahlianSekolah", "idKonsentrasiSekolah",
                    elemen.getKonsentrasiKeahlianSekolah().getIdKonsentrasiSekolah());
            client.insertRecord(tableElemen, elemenId, "konsentrasiKeahlianSekolah", "namaKonsentrasiSekolah",
                    elemen.getKonsentrasiKeahlianSekolah().getNamaKonsentrasiSekolah());
        }

        client.insertRecord(tableElemen, elemenId, "detail", "updated_by", "Polinema");

        return elemen;
    }

    public boolean deleteById(String elemenId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, elemenId);
        return true;
    }

    public boolean existsById(String elemenId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableElemen = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idElemen", "idElemen");

        Elemen elemen = client.getDataByColumn(tableElemen.toString(), columnMapping, "main", "idElemen", elemenId,
                Elemen.class);
        return elemen.getIdElemen() != null;
    }
}
