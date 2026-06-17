package com.doyatama.university.repository;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.SchoolProfile;
import java.io.IOException;
import java.util.*;

public class SchoolProfileRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "school-profiles";

    public List<SchoolProfile> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName profile = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("npsn", "npsn");
        columnMapping.put("status", "status");
        columnMapping.put("bentukKependidikan", "bentukKependidikan");
        columnMapping.put("kepemilikan", "kepemilikan");
        columnMapping.put("SKPendirianSekolah", "SKPendirianSekolah");
        columnMapping.put("tglSKPendirian", "tglSKPendirian");
        columnMapping.put("SKIzinOperasional", "SKIzinOperasional");
        columnMapping.put("tglSKOperasional", "tglSKOperasional");
        columnMapping.put("school", "school");
        columnMapping.put("file_path", "file_path");
        return client.showListTable(profile.toString(), columnMapping, SchoolProfile.class, size);
    }

    public SchoolProfile save(SchoolProfile profile) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString();
        TableName tableProfile = TableName.valueOf(tableName);
        client.insertRecord(tableProfile, rowKey, "main", "id", rowKey);
        client.insertRecord(tableProfile, rowKey, "main", "npsn", profile.getNpsn());
        client.insertRecord(tableProfile, rowKey, "main", "status", profile.getStatus());
        client.insertRecord(tableProfile, rowKey, "main", "bentukKependidikan", profile.getBentukKependidikan());
        client.insertRecord(tableProfile, rowKey, "main", "kepemilikan", profile.getKepemilikan());
        client.insertRecord(tableProfile, rowKey, "main", "SKPendirianSekolah", profile.getSKPendirianSekolah());
        client.insertRecord(tableProfile, rowKey, "main", "tglSKPendirian", profile.getTglSKPendirian());
        client.insertRecord(tableProfile, rowKey, "main", "SKIzinOperasional", profile.getSKIzinOperasional());
        client.insertRecord(tableProfile, rowKey, "main", "tglSKOperasional", profile.getTglSKOperasional());
        client.insertRecord(tableProfile, rowKey, "school", "idSchool", profile.getSchool().getIdSchool());
        client.insertRecord(tableProfile, rowKey, "school", "nameSchool", profile.getSchool().getNameSchool());
        client.insertRecord(tableProfile, rowKey, "main", "file_path", profile.getFile_path());
        client.insertRecord(tableProfile, rowKey, "detail", "created_by", "Doyatama");
        return profile;
    }

    public SchoolProfile findById(String profileId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableProfile = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("npsn", "npsn");
        columnMapping.put("status", "status");
        columnMapping.put("bentukKependidikan", "bentukKependidikan");
        columnMapping.put("kepemilikan", "kepemilikan");
        columnMapping.put("SKPendirianSekolah", "SKPendirianSekolah");
        columnMapping.put("tglSKPendirian", "tglSKPendirian");
        columnMapping.put("SKIzinOperasional", "SKIzinOperasional");
        columnMapping.put("tglSKOperasional", "tglSKOperasional");
        columnMapping.put("school", "school");
        columnMapping.put("file_path", "file_path");

        return client.showDataTable(tableProfile.toString(), columnMapping, profileId, SchoolProfile.class);
    }

    public List<SchoolProfile> findAllById(List<String> profileIds) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableProfile = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("npsn", "npsn");
        columnMapping.put("status", "status");
        columnMapping.put("bentukKependidikan", "bentukKependidikan");
        columnMapping.put("kepemilikan", "kepemilikan");
        columnMapping.put("SKPendirianSekolah", "SKPendirianSekolah");
        columnMapping.put("tglSKPendirian", "tglSKPendirian");
        columnMapping.put("SKIzinOperasional", "SKIzinOperasional");
        columnMapping.put("tglSKOperasional", "tglSKOperasional");
        columnMapping.put("school", "school");
        columnMapping.put("file_path", "file_path");
        List<SchoolProfile> profiles = new ArrayList<>();
        for (String profileId : profileIds) {
            SchoolProfile profile = client.showDataTable(tableProfile.toString(), columnMapping, profileId,
                    SchoolProfile.class);
            if (profile != null) {
                profiles.add(profile);
            }
        }

        return profiles;
    }

    public List<SchoolProfile> findSchoolProfileBySchool(String schoolId, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableProfile = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("id", "id");
        columnMapping.put("npsn", "npsn");
        columnMapping.put("status", "status");
        columnMapping.put("bentukKependidikan", "bentukKependidikan");
        columnMapping.put("kepemilikan", "kepemilikan");
        columnMapping.put("SKPendirianSekolah", "SKPendirianSekolah");
        columnMapping.put("tglSKPendirian", "tglSKPendirian");
        columnMapping.put("SKIzinOperasional", "SKIzinOperasional");
        columnMapping.put("tglSKOperasional", "tglSKOperasional");
        columnMapping.put("school", "school");
        columnMapping.put("file_path", "file_path");

        List<SchoolProfile> profile = client.getDataListByColumn(tableProfile.toString(), columnMapping, "school", "id",
                schoolId, SchoolProfile.class, size);

        return profile;
    }

    public SchoolProfile update(String profileId, SchoolProfile profile) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableProfile = TableName.valueOf(tableName);
        client.insertRecord(tableProfile, profileId, "main", "npsn", profile.getNpsn());
        client.insertRecord(tableProfile, profileId, "main", "status", profile.getStatus());
        client.insertRecord(tableProfile, profileId, "main", "bentukKependidikan", profile.getBentukKependidikan());
        client.insertRecord(tableProfile, profileId, "main", "kepemilikan", profile.getKepemilikan());
        client.insertRecord(tableProfile, profileId, "main", "SKPendirianSekolah", profile.getSKPendirianSekolah());
        client.insertRecord(tableProfile, profileId, "main", "tglSKPendirian", profile.getTglSKPendirian());
        client.insertRecord(tableProfile, profileId, "main", "SKIzinOperasional", profile.getSKIzinOperasional());
        client.insertRecord(tableProfile, profileId, "main", "tglSKOperasional", profile.getTglSKOperasional());
        client.insertRecord(tableProfile, profileId, "school", "idSchool", profile.getSchool().getIdSchool());
        client.insertRecord(tableProfile, profileId, "school", "nameSchool", profile.getSchool().getNameSchool());
        client.insertRecord(tableProfile, profileId, "main", "file_path", profile.getFile_path());
        client.insertRecord(tableProfile, profileId, "detail", "created_by", "Doyatama");
        return profile;
    }

    public boolean deleteById(String profileId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, profileId);
        return true;
    }
}
