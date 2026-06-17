package create_structure;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SeederDataBK {
    public static void main(String[] args) throws IOException {

        Configuration conf = HBaseConfiguration.create();
        HBaseCustomClient client = new HBaseCustomClient(conf);

        // Waktu sekarang
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        // Tabel bidang keahlian
        TableName tableBidangKeahlian = TableName.valueOf("bidangKeahlians");

        // ==============================================================================================
        // INSERT DATA
        // ==============================================================================================

        // Insert Data Table Bidang Keahlian
        client.insertRecord(tableBidangKeahlian, "BK001", "main", "id", "BK001");
        client.insertRecord(tableBidangKeahlian, "BK001", "main", "bidang", "Teknologi Konstruksi dan Bangunan");
        client.insertRecord(tableBidangKeahlian, "BK001", "detail", "created_by", "Doyatama");

        client.insertRecord(tableBidangKeahlian, "BK002", "main", "id", "BK002");
        client.insertRecord(tableBidangKeahlian, "BK002", "main", "bidang", "Teknologi Manufaktur dan Rekayasa");
        client.insertRecord(tableBidangKeahlian, "BK002", "detail", "created_by", "Doyatama");

        client.insertRecord(tableBidangKeahlian, "BK003", "main", "id", "BK003");
        client.insertRecord(tableBidangKeahlian, "BK003", "main", "bidang", "Energi dan Pertambangan");
        client.insertRecord(tableBidangKeahlian, "BK003", "detail", "created_by", "Doyatama");

        client.insertRecord(tableBidangKeahlian, "BK004", "main", "id", "BK004");
        client.insertRecord(tableBidangKeahlian, "BK004", "main", "bidang", "Teknologi Informasi");
        client.insertRecord(tableBidangKeahlian, "BK004", "detail", "created_by", "Doyatama");

        client.insertRecord(tableBidangKeahlian, "BK005", "main", "id", "BK005");
        client.insertRecord(tableBidangKeahlian, "BK005", "main", "bidang", "Kesehatan dan Pekerjaan Sosial");
        client.insertRecord(tableBidangKeahlian, "BK005", "detail", "created_by", "Doyatama");

        client.insertRecord(tableBidangKeahlian, "BK006", "main", "id", "BK006");
        client.insertRecord(tableBidangKeahlian, "BK006", "main", "bidang", "Agribisnis dan Agriteknologi ");
        client.insertRecord(tableBidangKeahlian, "BK006", "detail", "created_by", "Doyatama");

        client.insertRecord(tableBidangKeahlian, "BK007", "main", "id", "BK007");
        client.insertRecord(tableBidangKeahlian, "BK007", "main", "bidang", "Kemaritiman");
        client.insertRecord(tableBidangKeahlian, "BK007", "detail", "created_by", "Doyatama");

        client.insertRecord(tableBidangKeahlian, "BK008", "main", "id", "BK008");
        client.insertRecord(tableBidangKeahlian, "BK008", "main", "bidang", "Bisnis dan Manajemen");
        client.insertRecord(tableBidangKeahlian, "BK008", "detail", "created_by", "Doyatama");

        client.insertRecord(tableBidangKeahlian, "BK009", "main", "id", "BK009");
        client.insertRecord(tableBidangKeahlian, "BK009", "main", "bidang", "Pariwisata");
        client.insertRecord(tableBidangKeahlian, "BK009", "detail", "created_by", "Doyatama");

        client.insertRecord(tableBidangKeahlian, "BK010", "main", "id", "BK010");
        client.insertRecord(tableBidangKeahlian, "BK010", "main", "bidang", "Seni dan Ekonomi Kreatif");
        client.insertRecord(tableBidangKeahlian, "BK010", "detail", "created_by", "Doyatama");

        // ==============================================================================================

        // Insert Data Table Program Keahlian

    }
}
