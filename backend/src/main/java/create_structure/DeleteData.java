package create_structure;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;

public class DeleteData {

    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        HBaseCustomClient client = new HBaseCustomClient(conf);

         TableName tableHasilUjian = TableName.valueOf("hasil_ujian");
         client.truncateTable(tableHasilUjian, conf);

         TableName tableUjianAnalysis = TableName.valueOf("ujian_analysis");
         client.truncateTable(tableUjianAnalysis, conf);

         TableName tableUjian = TableName.valueOf("ujian");
         client.truncateTable(tableUjian, conf);

         TableName tableCheat = TableName.valueOf("cheat_detection");
         client.truncateTable(tableCheat, conf);

         TableName tableSession = TableName.valueOf("ujian_session");
         client.truncateTable(tableSession, conf);

    }
}
