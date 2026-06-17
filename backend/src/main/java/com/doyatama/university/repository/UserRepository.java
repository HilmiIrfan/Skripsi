package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository
public class UserRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "users";

    public List<User> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUserFind = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("username", "username");
        columnMapping.put("email", "email");
        columnMapping.put("password", "password");
        columnMapping.put("school", "school");
        columnMapping.put("roles", "roles");
        return client.showListTable(tableUserFind.toString(), columnMapping, User.class, size);
    }

    public List<User> findUsersNotUsedInLectures(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("username", "username");
        columnMapping.put("email", "email");
        columnMapping.put("password", "password");
        columnMapping.put("school", "school");
        columnMapping.put("roles", "roles");

        // Get the list of all users
        List<User> allUsers = client.showListTable(tableUsers.toString(), columnMapping, User.class, size);

        // Get the list of all user IDs that have been used in lectures
        Set<String> userIdsInLectures = new HashSet<>();
        Scan scan = new Scan();
        ResultScanner scanner = client.getTable("lectures").getScanner(scan);
        for (Result result : scanner) {
            byte[] userIdBytes = result.getValue(Bytes.toBytes("user"), Bytes.toBytes("id"));
            if (userIdBytes != null) {
                String userId = Bytes.toString(userIdBytes);
                userIdsInLectures.add(userId);
            }
        }
        scanner.close();

        // Find all users that have not been used in any lectures
        List<User> unusedUsers = new ArrayList<>();
        for (User user : allUsers) {
            if (!userIdsInLectures.contains(user.getId())) {
                unusedUsers.add(user);
            }
        }

        return unusedUsers;
    }

    public User findByUsername(String username) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("username", "username");
        columnMapping.put("email", "email");
        columnMapping.put("password", "password");
        columnMapping.put("school", "school");
        columnMapping.put("roles", "roles");

        return client.getDataByColumn(tableUsers.toString(), columnMapping, "main", "username", username, User.class);
    }

    public User findById(String id) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("username", "username");
        columnMapping.put("email", "email");
        columnMapping.put("password", "password");
        columnMapping.put("school", "school");
        columnMapping.put("roles", "roles");

        return client.showDataTable(tableUsers.toString(), columnMapping, id, User.class);
    }

    public List<User> findUserBySekolah(String schoolID, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("username", "username");
        columnMapping.put("email", "email");
        columnMapping.put("password", "password");
        columnMapping.put("school", "school");
        columnMapping.put("roles", "roles");

        List<User> users = client.getDataListByColumn(tableUsers.toString(), columnMapping, "school", "idSchool",
                schoolID, User.class, size);

        return users;
    }

    public boolean existsByUsername(String username) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("username", "username");
        columnMapping.put("email", "email");
        columnMapping.put("password", "password");
        columnMapping.put("school", "school");
        columnMapping.put("roles", "roles");

        User user = client.getDataByColumn(tableUsers.toString(), columnMapping, "main", "username", username,
                User.class);
        if (user.getUsername() != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean existsByEmail(String email) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("username", "username");
        columnMapping.put("email", "email");
        columnMapping.put("password", "password");
        columnMapping.put("school", "school");
        columnMapping.put("roles", "roles");

        User user = client.getDataByColumn(tableUsers.toString(), columnMapping, "main", "email", email, User.class);
        if (user.getEmail() != null) {
            return true;
        } else {
            return false;
        }
    }

    public User save(User user) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = user.getId();
        TableName tableUsers = TableName.valueOf(tableName);
        client.insertRecord(tableUsers, rowKey, "main", "id", rowKey);
        client.insertRecord(tableUsers, rowKey, "main", "name", user.getName());
        client.insertRecord(tableUsers, rowKey, "main", "username", user.getUsername());
        client.insertRecord(tableUsers, rowKey, "main", "email", user.getEmail());
        client.insertRecord(tableUsers, rowKey, "main", "password", user.getPassword());
        client.insertRecord(tableUsers, rowKey, "main", "roles", user.getRoles());

        // Sekolah
        if (user.getSchool() != null) {
            client.insertRecord(tableUsers, rowKey, "school", "idSchool", user.getSchool().getIdSchool());
            client.insertRecord(tableUsers, rowKey, "school", "nameSchool", user.getSchool().getNameSchool());
        }

        client.insertRecord(tableUsers, rowKey, "main", "created_at", user.getCreatedAt().toString());
        return user;
    }

    public User update(String userId, User user) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        // Map<String, String> columnMapping = new HashMap<>();

        if (user.getName() != null) {
            client.insertRecord(tableUsers, userId, "main", "name", user.getName());
        }

        if (user.getUsername() != null) {
            client.insertRecord(tableUsers, userId, "main", "username", user.getUsername());
        }

        if (user.getEmail() != null) {
            client.insertRecord(tableUsers, userId, "main", "email", user.getEmail());
        }

        if (user.getPassword() != null) {
            client.insertRecord(tableUsers, userId, "main", "password", user.getPassword());
        }

        if (user.getRoles() != null) {
            client.insertRecord(tableUsers, userId, "main", "roles", user.getRoles());
        }

        // Sekolah
        if (user.getSchool() != null) {
            client.insertRecord(tableUsers, userId, "school", "idSchool", user.getSchool().getIdSchool());
            client.insertRecord(tableUsers, userId, "school", "nameSchool", user.getSchool().getNameSchool());
        }

        return user;
    }

    public boolean deleteById(String userId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, userId);
        return true;
    }

    public boolean existsById(String userId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("id", "id");

        User user = client.getDataByColumn(tableUsers.toString(), columnMapping, "main", "id", userId, User.class);

        return user.getId() != null;
    }
}