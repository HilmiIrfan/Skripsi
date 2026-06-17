
package com.doyatama.university.repository;

import com.doyatama.university.controller.DepartmentController;
import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.StudyProgram;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.util.*;

public class StudyProgramRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "study_programs";
    DepartmentController departmentController = new DepartmentController();

    public List<StudyProgram> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("department", "department");
        return client.showListTable(tableUsers.toString(), columnMapping, StudyProgram.class, size);
    }

    public StudyProgram save(StudyProgram studyProgram) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString();

        TableName tableStudyProgram = TableName.valueOf(tableName);
        client.insertRecord(tableStudyProgram, rowKey, "main", "id", rowKey);
        client.insertRecord(tableStudyProgram, rowKey, "main", "name", studyProgram.getName());
        client.insertRecord(tableStudyProgram, rowKey, "main", "description", studyProgram.getDescription());
        client.insertRecord(tableStudyProgram, rowKey, "department", "id", studyProgram.getDepartment().getId());
        client.insertRecord(tableStudyProgram, rowKey, "department", "name", studyProgram.getDepartment().getName());
        client.insertRecord(tableStudyProgram, rowKey, "detail", "created_by", "Doyatama");
        return studyProgram;
    }

    public StudyProgram findById(String studyProgramId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("department", "department");

        return client.showDataTable(tableUsers.toString(), columnMapping, studyProgramId, StudyProgram.class);
    }

    public List<StudyProgram> findAllById(List<String> studyProgramIds) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName table = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("department", "department");

        List<StudyProgram> studyPrograms = new ArrayList<>();
        for (String studyProgramId : studyProgramIds) {
            StudyProgram studyProgram = client.showDataTable(table.toString(), columnMapping, studyProgramId,
                    StudyProgram.class);
            if (studyProgram != null) {
                studyPrograms.add(studyProgram);
            }
        }

        return studyPrograms;
    }

    public StudyProgram update(String studyProgramId, StudyProgram studyProgram) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableStudyProgram = TableName.valueOf(tableName);
        client.insertRecord(tableStudyProgram, studyProgramId, "main", "name", studyProgram.getName());
        client.insertRecord(tableStudyProgram, studyProgramId, "main", "description", studyProgram.getDescription());
        client.insertRecord(tableStudyProgram, studyProgramId, "department", "id",
                studyProgram.getDepartment().getId());
        client.insertRecord(tableStudyProgram, studyProgramId, "department", "name",
                studyProgram.getDepartment().getName());
        return studyProgram;
    }

    public boolean deleteById(String studyProgramId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, studyProgramId);
        return true;
    }
}
