package create_structure;

import java.time.Instant;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SeederUser {

    public static void main(String[] args) throws IOException {

        Configuration conf = HBaseConfiguration.create();
        HBaseCustomClient client = new HBaseCustomClient(conf);

        // Waktu sekarang
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        // Tabel Elemen
        TableName tableUser = TableName.valueOf("users");
        // Insert Users
        client.insertRecord(tableUser, "USR001", "main", "id", "USR001");
        client.insertRecord(tableUser, "USR001", "main", "email", "admin@gmail.com");
        client.insertRecord(tableUser, "USR001", "main", "name", "Administrator");
        client.insertRecord(tableUser, "USR001", "main", "username", "admin");
        client.insertRecord(tableUser, "USR001", "main", "password",
                "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2"); // password
        client.insertRecord(tableUser, "USR001", "main", "roles", "1");
        client.insertRecord(tableUser, "USR001", "main", "created_at", "2023-05-14T04:56:23.174Z");
        client.insertRecord(tableUser, "USR001", "detail", "created_by", "Doyatama");

        client.insertRecord(tableUser, "USR002", "main", "id", "USR002");
        client.insertRecord(tableUser, "USR002", "main", "email", "operator1@gmail.com");
        client.insertRecord(tableUser, "USR002", "main", "name", "Operator1");
        client.insertRecord(tableUser, "USR002", "main", "username", "operator1");
        client.insertRecord(tableUser, "USR002", "main", "password",
                "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2"); // password
        client.insertRecord(tableUser, "USR002", "school", "idSchool", "RWK001");
        client.insertRecord(tableUser, "USR002", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableUser, "USR002", "main", "roles", "2");
        client.insertRecord(tableUser, "USR002", "main", "created_at", "2023-05-14T04:56:23.174Z");
        client.insertRecord(tableUser, "USR002", "detail", "created_by", "Doyatama");

        client.insertRecord(tableUser, "USR003", "main", "id", "USR003");
        client.insertRecord(tableUser, "USR003", "main", "email", "operator2@gmail.com");
        client.insertRecord(tableUser, "USR003", "main", "name", "Operator2");
        client.insertRecord(tableUser, "USR003", "main", "username", "operator2");
        client.insertRecord(tableUser, "USR003", "main", "password",
                "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2"); // password
        client.insertRecord(tableUser, "USR003", "school", "idSchool", "TMP001");
        client.insertRecord(tableUser, "USR003", "school", "nameSchool", "SMK Negeri 01 TEMPEH");
        client.insertRecord(tableUser, "USR003", "main", "roles", "2");
        client.insertRecord(tableUser, "USR003", "main", "created_at", "2023-05-14T04:56:23.174Z");
        client.insertRecord(tableUser, "USR003", "detail", "created_by", "Doyatama");

        client.insertRecord(tableUser, "USR004", "main", "id", "USR004");
        client.insertRecord(tableUser, "USR004", "main", "email", "guru@gmail.com");
        client.insertRecord(tableUser, "USR004", "main", "name", "Guru SMK Rowokangkung");
        client.insertRecord(tableUser, "USR004", "main", "username", "gurusmk1");
        client.insertRecord(tableUser, "USR004", "main", "password",
                "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2"); // password
        client.insertRecord(tableUser, "USR004", "school", "idSchool", "RWK001");
        client.insertRecord(tableUser, "USR004", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableUser, "USR004", "main", "roles", "3");
        client.insertRecord(tableUser, "USR004", "main", "created_at", Instant.now().toString());
        client.insertRecord(tableUser, "USR004", "detail", "created_by", "Doyatama");

        client.insertRecord(tableUser, "USR005", "main", "id", "USR005");
        client.insertRecord(tableUser, "USR005", "main", "email", "murid@gmail.com");
        client.insertRecord(tableUser, "USR005", "main", "name", "Murid SMK Rowokangkung");
        client.insertRecord(tableUser, "USR005", "main", "username", "muridsmk1");
        client.insertRecord(tableUser, "USR005", "main", "password",
                "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2"); // password
        client.insertRecord(tableUser, "USR005", "school", "idSchool", "RWK001");
        client.insertRecord(tableUser, "USR005", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableUser, "USR005", "main", "roles", "5");
        client.insertRecord(tableUser, "USR005", "main", "created_at", Instant.now().toString());
        client.insertRecord(tableUser, "USR005", "detail", "created_by", "Doyatama");
    }
}
