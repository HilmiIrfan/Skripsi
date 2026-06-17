package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.Taksonomi;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

public class TaksonomiRepository {

    Configuration conf = HBaseConfiguration.create();
    String tableName = "taksonomi";

    public List<Taksonomi> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableTaksonomi = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idTaksonomi", "idTaksonomi");
        columnMapping.put("namaTaksonomi", "namaTaksonomi");
        columnMapping.put("deskripsiTaksonomi", "deskripsiTaksonomi");
        columnMapping.put("school", "school");
        return client.showListTable(tableTaksonomi.toString(), columnMapping, Taksonomi.class, size);
    }

    public Taksonomi save(Taksonomi taksonomi) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = taksonomi.getIdTaksonomi();
        TableName tableTaksonomi = TableName.valueOf(tableName);
        client.insertRecord(tableTaksonomi, rowKey, "main", "idTaksonomi", rowKey);
        client.insertRecord(tableTaksonomi, rowKey, "main", "namaTaksonomi", taksonomi.getNamaTaksonomi());
        client.insertRecord(tableTaksonomi, rowKey, "main", "deskripsiTaksonomi", taksonomi.getDeskripsiTaksonomi());

        // School
        client.insertRecord(tableTaksonomi, rowKey, "school", "idSchool", taksonomi.getSchool().getIdSchool());
        client.insertRecord(tableTaksonomi, rowKey, "school", "nameSchool", taksonomi.getSchool().getNameSchool());

        client.insertRecord(tableTaksonomi, rowKey, "detail", "created_by", "Doyatama");
        return taksonomi;
    }

    public Taksonomi findById(String taksonomiId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableTaksonomi = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idTaksonomi", "idTaksonomi");
        columnMapping.put("namaTaksonomi", "namaTaksonomi");
        columnMapping.put("deskripsiTaksonomi", "deskripsiTaksonomi");
        columnMapping.put("school", "school");

        return client.showDataTable(tableTaksonomi.toString(), columnMapping, taksonomiId, Taksonomi.class);
    }

    public List<Taksonomi> findTaksonomiBySekolah(String schoolId, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableTaksonomi = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idTaksonomi", "idTaksonomi");
        columnMapping.put("namaTaksonomi", "namaTaksonomi");
        columnMapping.put("deskripsiTaksonomi", "deskripsiTaksonomi");
        columnMapping.put("school", "school");

        List<Taksonomi> taksonomiList = client.getDataListByColumn(tableTaksonomi.toString(), columnMapping, "school",
                "idSchool", schoolId, Taksonomi.class, size);

        return taksonomiList;
    }

    public Taksonomi update(String taksonomiId, Taksonomi taksonomi) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableTaksonomi = TableName.valueOf(tableName);

        if (taksonomi.getNamaTaksonomi() != null) {
            client.insertRecord(tableTaksonomi, taksonomiId, "main", "namaTaksonomi", taksonomi.getNamaTaksonomi());
        }
        if (taksonomi.getDeskripsiTaksonomi() != null) {
            client.insertRecord(tableTaksonomi, taksonomiId, "main", "deskripsiTaksonomi",
                    taksonomi.getDeskripsiTaksonomi());
        }

        // School
        if (taksonomi.getSchool() != null) {
            client.insertRecord(tableTaksonomi, taksonomiId, "school", "idSchool",
                    taksonomi.getSchool().getIdSchool());
            client.insertRecord(tableTaksonomi, taksonomiId, "school", "nameSchool",
                    taksonomi.getSchool().getNameSchool());

        }

        return taksonomi;
    }

    public boolean deleteById(String taksonomiId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        client.deleteRecord(tableName, taksonomiId);
        return true;
    }

    public boolean existById(String taksonomiId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableTaksonomi = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idTaksonomi", "idTaksonomi");

        Taksonomi taksonomi = client.getDataByColumn(tableTaksonomi.toString(), columnMapping, "main", "idTaksonomi",
                taksonomiId, Taksonomi.class);

        return taksonomi.getIdTaksonomi() != null;
    }

}
