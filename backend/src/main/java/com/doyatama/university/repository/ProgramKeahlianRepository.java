/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.ProgramKeahlian;
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
public class ProgramKeahlianRepository {
     Configuration conf = HBaseConfiguration.create();
    String tableName = "programKeahlians";
    
     public List<ProgramKeahlian> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableProgramKeahlian = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("program", "program");
        columnMapping.put("bidangKeahlian", "bidangKeahlian");
        return client.showListTable(tableProgramKeahlian.toString(), columnMapping, ProgramKeahlian.class, size);
    }
     
     public ProgramKeahlian save(ProgramKeahlian programKeahlian) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = programKeahlian.getId();
        TableName tableProgramKeahlian = TableName.valueOf(tableName);
        client.insertRecord(tableProgramKeahlian, rowKey, "main", "id", rowKey);
        client.insertRecord(tableProgramKeahlian, rowKey, "main", "program", programKeahlian.getProgram());
        client.insertRecord(tableProgramKeahlian, rowKey, "bidangKeahlian", "id", programKeahlian.getBidangKeahlian().getId());
        client.insertRecord(tableProgramKeahlian, rowKey, "bidangKeahlian", "bidang", programKeahlian.getBidangKeahlian().getBidang());

        client.insertRecord(tableProgramKeahlian, rowKey, "detail", "created_by", "Doyatama");
        return programKeahlian;
    } 
     
         public ProgramKeahlian findById(String BDGid) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableProgramKeahlian = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("program", "program");

        return client.showDataTable(tableProgramKeahlian.toString(), columnMapping, BDGid, ProgramKeahlian.class);
    }
         
             public List<ProgramKeahlian> findAllById(List<String> BDGids) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableProgramKeahlian = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("program", "program");


        List<ProgramKeahlian> programKeahlians = new ArrayList<>();
        for (String BDGid : BDGids) {
            ProgramKeahlian programKeahlian = client.showDataTable(tableProgramKeahlian.toString(), columnMapping, BDGid, ProgramKeahlian.class);
            if (programKeahlian != null) {
                programKeahlians.add(programKeahlian);
            }
        }

        return programKeahlians;
        }
             
        public List<ProgramKeahlian> findProgramByBidang(String bidangId, int size) throws IOException {
            HBaseCustomClient client = new HBaseCustomClient(conf);

            TableName tableProfile = TableName.valueOf(tableName);
            Map<String, String> columnMapping = new HashMap<>();

            columnMapping.put("id", "id");
            columnMapping.put("program", "program");
            columnMapping.put("bidangKeahlian", "bidangKeahlian");

            List<ProgramKeahlian> program = client.getDataListByColumn(tableProfile.toString(), columnMapping, "bidangKeahlian", "id", bidangId, ProgramKeahlian.class, size);

            return program;
        }
             
        public ProgramKeahlian update(String BDGid, ProgramKeahlian programKeahlian) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableProgramKeahlian = TableName.valueOf(tableName);
        client.insertRecord(tableProgramKeahlian, BDGid, "main", "program", programKeahlian.getProgram());
        client.insertRecord(tableProgramKeahlian, BDGid, "bidangKeahlian", "id", programKeahlian.getBidangKeahlian().getId());
        client.insertRecord(tableProgramKeahlian, BDGid, "bidangKeahlian", "bidang", programKeahlian.getBidangKeahlian().getBidang());
        client.insertRecord(tableProgramKeahlian, BDGid, "detail", "created_by", "Doyatama");
        return programKeahlian;
    }
        
        
       public boolean deleteById(String BDGid) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, BDGid);
        return true;
    }  
}
