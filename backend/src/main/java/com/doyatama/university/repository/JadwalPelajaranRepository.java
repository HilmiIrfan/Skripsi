package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.JadwalPelajaran;
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
public class JadwalPelajaranRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "jadwalPelajarans";
    
    public List<JadwalPelajaran> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableJadPel = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idJadwal", "idJadwal");
        columnMapping.put("lecture", "lecture");
        columnMapping.put("jabatan", "jabatan");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("jmlJam", "jmlJam");
        return client.showListTable(tableJadPel.toString(), columnMapping, JadwalPelajaran.class, size);
    }
     
    public JadwalPelajaran save(JadwalPelajaran jadpel) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = jadpel.getIdJadwal();
        TableName tableJadPel = TableName.valueOf(tableName);
        client.insertRecord(tableJadPel, rowKey, "main", "idJadwal", rowKey);
        client.insertRecord(tableJadPel, rowKey, "main", "jabatan", jadpel.getJabatan());
        client.insertRecord(tableJadPel, rowKey, "main", "jmlJam", jadpel.getJmlJam());
        client.insertRecord(tableJadPel, rowKey, "lecture", "id", jadpel.getLecture().getId());
        client.insertRecord(tableJadPel, rowKey, "lecture", "name", jadpel.getLecture().getName());
        client.insertRecord(tableJadPel, rowKey, "lecture", "nip", jadpel.getLecture().getNip());
        client.insertRecord(tableJadPel, rowKey, "mapel", "idMapel", jadpel.getMapel().getIdMapel());
        client.insertRecord(tableJadPel, rowKey, "mapel", "name", jadpel.getMapel().getName());
        client.insertRecord(tableJadPel, rowKey, "detail", "created_by", "Doyatama");
        return jadpel;
    } 
     
    public JadwalPelajaran findById(String jadwalId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableJadPel = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idJadwal", "idJadwal");
        columnMapping.put("lecture", "lecture");
        columnMapping.put("jabatan", "jabatan");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("jmlJam", "jmlJam");

        return client.showDataTable(tableJadPel.toString(), columnMapping, jadwalId, JadwalPelajaran.class);
    }
   

         
    public List<JadwalPelajaran> findAllById(List<String> jadwalIds) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableJadPel = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idJadwal", "idJadwal");
        columnMapping.put("lecture", "lecture");
        columnMapping.put("jabatan", "jabatan");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("jmlJam", "jmlJam");
        
        List<JadwalPelajaran> jadpels = new ArrayList<>();
        for (String jadwalId : jadwalIds) {
            JadwalPelajaran jadpel = client.showDataTable(tableJadPel.toString(), columnMapping, jadwalId, JadwalPelajaran.class);
            if (jadpel != null) {
                jadpels.add(jadpel);
            }
        }

        return jadpels;
    }
    

             
    public JadwalPelajaran update(String jadwalId, JadwalPelajaran jadpel) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableJadPel = TableName.valueOf(tableName);
        client.insertRecord(tableJadPel, jadwalId, "main", "jabatan", jadpel.getJabatan());
        client.insertRecord(tableJadPel, jadwalId, "main", "jmlJam", jadpel.getJmlJam());
        client.insertRecord(tableJadPel, jadwalId, "lecture", "id", jadpel.getLecture().getId());
        client.insertRecord(tableJadPel, jadwalId, "lecture", "name", jadpel.getLecture().getName());
        client.insertRecord(tableJadPel, jadwalId, "lecture", "nip", jadpel.getLecture().getNip());
        client.insertRecord(tableJadPel, jadwalId, "mapel", "idMapel", jadpel.getMapel().getIdMapel());
        client.insertRecord(tableJadPel, jadwalId, "mapel", "name", jadpel.getMapel().getName());
        client.insertRecord(tableJadPel, jadwalId, "detail", "created_by", "Doyatama");
        return jadpel;
    }
    
    public boolean deleteById(String jadwalId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, jadwalId);
        return true;
    }  
}
