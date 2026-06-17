package create_structure;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;

public class HBaseClientStructureTest {

    public static void main(String[] args) throws IOException {

        Configuration conf = HBaseConfiguration.create();
        HBaseCustomClient client = new HBaseCustomClient(conf);

        // ==============================================================================================
        // CREATE COLLECTION
        // ==============================================================================================

        // Create Table Users
        TableName tableUser = TableName.valueOf("users");
        String[] users = { "main", "detail" };
        client.deleteTable(tableUser);
        client.createTable(tableUser, users);

        // ==============================================================================================
        // INSERT DATA
        // ==============================================================================================

        // Insert Users
        client.insertRecord(tableUser, "USR001", "main", "id", "DP001");
        client.insertRecord(tableUser, "USR001", "main", "email", "test@gmail.com");
        client.insertRecord(tableUser, "USR001", "main", "username", "admin");
        client.insertRecord(tableUser, "USR001", "main", "password", "password");
        client.insertRecord(tableUser, "USR001", "main", "role", "1");
        client.insertRecord(tableUser, "USR001", "detail", "created_by", "Doyatama");
    }

}