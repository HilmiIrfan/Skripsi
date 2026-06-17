/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.BidangKeahlian;
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
public class BidangKeahlianRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "bidangKeahlians";

    public List<BidangKeahlian> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableBidangKeahlian = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("bidang", "bidang");
        return client.showListTable(tableBidangKeahlian.toString(), columnMapping, BidangKeahlian.class, size);
    }

    public BidangKeahlian save(BidangKeahlian bidangKeahlian) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = bidangKeahlian.getId();
        TableName tableBidangKeahlian = TableName.valueOf(tableName);
        client.insertRecord(tableBidangKeahlian, rowKey, "main", "id", rowKey);
        client.insertRecord(tableBidangKeahlian, rowKey, "main", "bidang", bidangKeahlian.getBidang());

        client.insertRecord(tableBidangKeahlian, rowKey, "detail", "created_by", "Doyatama");
        return bidangKeahlian;
    }

    public BidangKeahlian findById(String BDGid) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableBidangKeahlian = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("bidang", "bidang");

        return client.showDataTable(tableBidangKeahlian.toString(), columnMapping, BDGid, BidangKeahlian.class);
    }

    public List<BidangKeahlian> findAllById(List<String> BDGids) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableBidangKeahlian = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("bidang", "bidang");

        List<BidangKeahlian> bidangKeahlians = new ArrayList<>();
        for (String BDGid : BDGids) {
            BidangKeahlian bidangKeahlian = client.showDataTable(tableBidangKeahlian.toString(), columnMapping, BDGid,
                    BidangKeahlian.class);
            if (bidangKeahlian != null) {
                bidangKeahlians.add(bidangKeahlian);
            }
        }

        return bidangKeahlians;
    }

    public BidangKeahlian update(String BDGid, BidangKeahlian bidangKeahlian) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableBidangKeahlian = TableName.valueOf(tableName);
        client.insertRecord(tableBidangKeahlian, BDGid, "main", "bidang", bidangKeahlian.getBidang());
        client.insertRecord(tableBidangKeahlian, BDGid, "detail", "created_by", "Doyatama");

        return bidangKeahlian;
    }

    public boolean deleteById(String BDGid) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, BDGid);
        return true;
    }
}
