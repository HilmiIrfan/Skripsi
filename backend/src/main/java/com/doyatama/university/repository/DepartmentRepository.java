package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.Department;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DepartmentRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "departments";

    public List<Department> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableDepartments = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");

        return client.showListTable(tableDepartments.toString(), columnMapping, Department.class, size);
    }

    public List<Department> findBySchoolId(String schoolId, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableDepartment = TableName.valueOf(tableName);

        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("schoolID", "schoolID");

        String familyName = "main";

        return client.showListTableFiltered(tableDepartment.toString(), columnMapping, Department.class, size,
                familyName, "schoolID", schoolId);
    }

    // public List<Department> findBySchoolId(String schoolId, int size) throws
    // IOException {
    // HBaseCustomClient client = new HBaseCustomClient(conf);
    // TableName tableDepartment = TableName.valueOf(tableName);
    //
    // Map<String, String> columnMapping = new HashMap<>();
    // columnMapping.put("id", "id");
    // columnMapping.put("name", "name");
    // columnMapping.put("description", "description");
    // columnMapping.put("schoolID", "schoolID");
    //
    // // Filter berdasarkan schoolId
    // return client.showListTableFiltered(tableDepartment.toString(),
    // columnMapping, "schoolID", schoolId, Department.class, size);
    // }

    public Department save(Department department) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString();

        TableName tableDepartment = TableName.valueOf(tableName);
        client.insertRecord(tableDepartment, rowKey, "main", "id", rowKey);
        client.insertRecord(tableDepartment, rowKey, "main", "schoolID", department.getSchoolID());
        client.insertRecord(tableDepartment, rowKey, "main", "name", department.getName());
        client.insertRecord(tableDepartment, rowKey, "main", "description", department.getDescription());
        client.insertRecord(tableDepartment, rowKey, "detail", "created_by", "Doyatama");
        return department;
    }

    public Department findById(String departmentId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableDepartments = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");

        return client.showDataTable(tableDepartments.toString(), columnMapping, departmentId, Department.class);
    }

    public Department update(String departmentId, Department department) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableDepartment = TableName.valueOf(tableName);
        client.insertRecord(tableDepartment, departmentId, "main", "name", department.getName());
        client.insertRecord(tableDepartment, departmentId, "main", "description", department.getDescription());
        return department;
    }

    public boolean deleteById(String departmentId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, departmentId);
        return true;
    }
}
