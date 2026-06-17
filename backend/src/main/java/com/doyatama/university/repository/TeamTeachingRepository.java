package com.doyatama.university.repository;

/**
 * @author alfa
 */

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.TeamTeaching;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.util.*;

public class TeamTeachingRepository {

    Configuration conf = HBaseConfiguration.create();
    String tableName = "team_teachings";

    public List<TeamTeaching> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("lecture", "lecture");
        columnMapping.put("lecture2", "lecture2");
        columnMapping.put("lecture3", "lecture3");

        return client.showListTable(tableUsers.toString(), columnMapping, TeamTeaching.class, size);
    }

    public TeamTeaching save(TeamTeaching teamTeaching) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = UUID.randomUUID().toString().substring(0, 5);

        TableName tableTeamTeaching = TableName.valueOf(tableName);

        client.insertRecord(tableTeamTeaching, rowKey, "main", "id", rowKey);

        client.insertRecord(tableTeamTeaching, rowKey, "main", "name", teamTeaching.getName());
        client.insertRecord(tableTeamTeaching, rowKey, "main", "description", teamTeaching.getDescription());

        client.insertRecord(tableTeamTeaching, rowKey, "lecture", "id", teamTeaching.getLecture().getId());
        client.insertRecord(tableTeamTeaching, rowKey, "lecture", "name", teamTeaching.getLecture().getName());

        client.insertRecord(tableTeamTeaching, rowKey, "lecture2", "id", teamTeaching.getLecture2().getId());
        client.insertRecord(tableTeamTeaching, rowKey, "lecture2", "name", teamTeaching.getLecture2().getName());

        client.insertRecord(tableTeamTeaching, rowKey, "lecture3", "id", teamTeaching.getLecture3().getId());
        client.insertRecord(tableTeamTeaching, rowKey, "lecture3", "name", teamTeaching.getLecture3().getName());

        return teamTeaching;
    }

    public TeamTeaching findById(String teamTeachingId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("lecture", "lecture");
        columnMapping.put("lecture2", "lecture2");
        columnMapping.put("lecture3", "lecture3");

        return client.showDataTable(tableUsers.toString(), columnMapping, teamTeachingId, TeamTeaching.class);
    }

    public TeamTeaching update(String teamTeachingId, TeamTeaching teamTeaching) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableTeamTeaching = TableName.valueOf(tableName);
        client.insertRecord(tableTeamTeaching, teamTeachingId, "main", "name", teamTeaching.getName());
        client.insertRecord(tableTeamTeaching, teamTeachingId, "main", "description", teamTeaching.getDescription());

        client.insertRecord(tableTeamTeaching, teamTeachingId, "lecture", "id", teamTeaching.getLecture().getId());
        client.insertRecord(tableTeamTeaching, teamTeachingId, "lecture", "name", teamTeaching.getLecture().getName());

        client.insertRecord(tableTeamTeaching, teamTeachingId, "lecture2", "id", teamTeaching.getLecture2().getId());
        client.insertRecord(tableTeamTeaching, teamTeachingId, "lecture2", "name",
                teamTeaching.getLecture2().getName());

        client.insertRecord(tableTeamTeaching, teamTeachingId, "lecture3", "id", teamTeaching.getLecture3().getId());
        client.insertRecord(tableTeamTeaching, teamTeachingId, "lecture3", "name",
                teamTeaching.getLecture3().getName());

        return teamTeaching;
    }

    public boolean deleteById(String teamTeachingId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, teamTeachingId);
        return true;
    }

    public List<TeamTeaching> findAllById(List<String> TeamTeachingIds) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName table = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("lecture", "lecture");
        columnMapping.put("lecture2", "lecture2");
        columnMapping.put("lecture3", "lecture3");

        List<TeamTeaching> teamTeachings = new ArrayList<>();
        for (String teamTeachingId : TeamTeachingIds) {
            TeamTeaching teamTeaching = client.showDataTable(table.toString(), columnMapping, teamTeachingId,
                    TeamTeaching.class);
            if (teamTeaching != null) {
                teamTeachings.add(teamTeaching);
            }
        }
        return teamTeachings;
    }

    public List<TeamTeaching> findRelationById(List<String> TeamTeachingIds) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName table = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("name", "name");
        columnMapping.put("description", "description");
        columnMapping.put("lecture", "lecture");
        columnMapping.put("lecture2", "lecture2");
        columnMapping.put("lecture3", "lecture3");

        List<TeamTeaching> teamTeachings = new ArrayList<>();
        for (String teamTeachingId : TeamTeachingIds) {
            TeamTeaching teamTeaching = client.showDataTable(table.toString(), columnMapping, teamTeachingId,
                    TeamTeaching.class);
            if (teamTeaching != null) {
                teamTeachings.add(teamTeaching);
            }
        }
        return teamTeachings;
    }

}
