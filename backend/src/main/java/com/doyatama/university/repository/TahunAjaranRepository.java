
package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.TahunAjaran;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.springframework.stereotype.Repository;

@Repository
public class TahunAjaranRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "tahunAjaran";

    public List<TahunAjaran> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableTahun = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idTahun", "idTahun");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("school", "school");
        return client.showListTable(tableTahun.toString(), columnMapping, TahunAjaran.class, size);
    }

    public TahunAjaran save(TahunAjaran tahun) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = tahun.getIdTahun();
        TableName tableTahun = TableName.valueOf(tableName);
        client.insertRecord(tableTahun, rowKey, "main", "idTahun", rowKey);
        client.insertRecord(tableTahun, rowKey, "main", "tahunAjaran", tahun.getTahunAjaran());

        // Sekolah
        client.insertRecord(tableTahun, rowKey, "school", "idSchool", tahun.getSchool().getIdSchool());
        client.insertRecord(tableTahun, rowKey, "school", "nameSchool", tahun.getSchool().getNameSchool());

        client.insertRecord(tableTahun, rowKey, "detail", "created_by", "Doyatama");
        return tahun;
    }

    public TahunAjaran findTahunById(String tahunId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableTahun = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idTahun", "idTahun");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("school", "school");

        return client.showDataTable(tableTahun.toString(), columnMapping, tahunId, TahunAjaran.class);
    }

    public TahunAjaran findById(String tahunId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableTahun = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idTahun", "idTahun");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("school", "school");

        return client.showDataTable(tableTahun.toString(), columnMapping, tahunId, TahunAjaran.class);
    }

    public List<TahunAjaran> findAllById(List<String> tahunIds) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableTahun = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idTahun", "idTahun");
        columnMapping.put("tahunAjaran", "tahunAjaran");

        List<TahunAjaran> tahuns = new ArrayList<>();
        for (String tahunId : tahunIds) {
            TahunAjaran tahun = client.showDataTable(tableTahun.toString(), columnMapping, tahunId, TahunAjaran.class);
            if (tahun != null) {
                tahuns.add(tahun);
            }
        }

        return tahuns;
    }

    public List<TahunAjaran> findTahunAjaranBySekolah(String schoolID, int size) throws IOException {
        System.out.println("📡 [Repository] Filter berdasarkan schoolID: " + schoolID);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableTahun = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idTahun", "idTahun");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("school", "school");

        List<TahunAjaran> tahun = client.getDataListByColumn(tableTahun.toString(), columnMapping, "school",
                "idSchool", schoolID, TahunAjaran.class, size);

        return tahun;
    }

    public TahunAjaran update(String tahunId, TahunAjaran tahun) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableTahun = TableName.valueOf(tableName);

        if (tahun.getTahunAjaran() != null) {
            client.insertRecord(tableTahun, tahunId, "main", "tahunAjaran", tahun.getTahunAjaran());
        }

        // Sekolah
        if (tahun.getSchool() != null) {
            client.insertRecord(tableTahun, tahunId, "school", "idSchool", tahun.getSchool().getIdSchool());
            client.insertRecord(tableTahun, tahunId, "school", "nameSchool", tahun.getSchool().getNameSchool());
        }

        client.insertRecord(tableTahun, tahunId, "detail", "created_by", "Doyatama");
        return tahun;
    }

    public boolean deleteById(String tahunId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, tahunId);
        return true;
    }

    public boolean existsById(String tahunId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableTahun = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("idTahun", "idTahun");

        TahunAjaran tahun = client.getDataByColumn(tableTahun.toString(), columnMapping, "main", "idTahun", tahunId,
                TahunAjaran.class);

        return tahun.getIdTahun() != null;
    }
}
