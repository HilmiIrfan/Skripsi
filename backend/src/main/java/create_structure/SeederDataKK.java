package create_structure;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SeederDataKK {

    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        HBaseCustomClient client = new HBaseCustomClient(conf);

        // Waktu sekarang
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        // Table Name
        TableName tableKonsentrasiKeahlian = TableName.valueOf("konsentrasiKeahlians");

        // BK001
        client.insertRecord(tableKonsentrasiKeahlian, "KK001", "main", "id", "KK001");
        client.insertRecord(tableKonsentrasiKeahlian, "KK001", "main", "konsentrasi", "Teknik Perawatan Gedung");
        client.insertRecord(tableKonsentrasiKeahlian, "KK001", "programKeahlian", "id", "PK001");
        client.insertRecord(tableKonsentrasiKeahlian, "KK001", "programKeahlian", "program",
                "Teknik Perawatan Gedung");
        client.insertRecord(tableKonsentrasiKeahlian, "KK001", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK002", "main", "id", "KK002");
        client.insertRecord(tableKonsentrasiKeahlian, "KK002", "main", "konsentrasi",
                "Konstruksi Jalan, Irigasi, dan Jembatan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK002", "programKeahlian", "id", "PK001");
        client.insertRecord(tableKonsentrasiKeahlian, "KK002", "programKeahlian", "program",
                "Konstruksi dan Perawatan Bangunan Sipil");
        client.insertRecord(tableKonsentrasiKeahlian, "KK002", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK003", "main", "id", "KK003");
        client.insertRecord(tableKonsentrasiKeahlian, "KK003", "main", "konsentrasi", "Konstruksi Jalan dan Jembatan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK003", "programKeahlian", "id", "PK001");
        client.insertRecord(tableKonsentrasiKeahlian, "KK003", "programKeahlian", "program",
                "Konstruksi dan Perawatan Bangunan Sipil");
        client.insertRecord(tableKonsentrasiKeahlian, "KK003", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK004", "main", "id", "KK004");
        client.insertRecord(tableKonsentrasiKeahlian, "KK004", "main", "konsentrasi",
                "Teknik Konstruksi dan Perumahan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK004", "programKeahlian", "id", "PK003");
        client.insertRecord(tableKonsentrasiKeahlian, "KK004", "programKeahlian", "program",
                "Teknik Konstruksi dan Perumahan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK004", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK005", "main", "id", "KK005");
        client.insertRecord(tableKonsentrasiKeahlian, "KK005", "main", "konsentrasi", "Konstruksi Gedung dan Sanitasi");
        client.insertRecord(tableKonsentrasiKeahlian, "KK005", "programKeahlian", "id", "PK003");
        client.insertRecord(tableKonsentrasiKeahlian, "KK005", "programKeahlian", "program",
                "Teknik Konstruksi dan Perumahan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK005", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK006", "main", "id", "KK006");
        client.insertRecord(tableKonsentrasiKeahlian, "KK006", "main", "konsentrasi",
                "Desain Pemodelan dan Informasi Bangunan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK006", "programKeahlian", "id", "PK004");
        client.insertRecord(tableKonsentrasiKeahlian, "KK006", "programKeahlian", "program",
                "Desain Pemodelan dan Informasi Bangunan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK006", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK007", "main", "id", "KK007");
        client.insertRecord(tableKonsentrasiKeahlian, "KK007", "main", "konsentrasi",
                "Desain Interior dan Teknik Furnitur");
        client.insertRecord(tableKonsentrasiKeahlian, "KK007", "programKeahlian", "id", "PK005");
        client.insertRecord(tableKonsentrasiKeahlian, "KK007", "programKeahlian", "program", "Teknik Furnitur");
        client.insertRecord(tableKonsentrasiKeahlian, "KK007", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK008", "main", "id", "KK008");
        client.insertRecord(tableKonsentrasiKeahlian, "KK008", "main", "konsentrasi", "Teknik Furnitur");
        client.insertRecord(tableKonsentrasiKeahlian, "KK008", "programKeahlian", "id", "PK005");
        client.insertRecord(tableKonsentrasiKeahlian, "KK008", "programKeahlian", "program", "Teknik Furnitur");
        client.insertRecord(tableKonsentrasiKeahlian, "KK008", "detail", "created_by", "Doyatama");

        // BK002
        client.insertRecord(tableKonsentrasiKeahlian, "KK009", "main", "id", "KK009");
        client.insertRecord(tableKonsentrasiKeahlian, "KK009", "main", "konsentrasi", "Teknik Pemesinan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK009", "programKeahlian", "id", "PK006");
        client.insertRecord(tableKonsentrasiKeahlian, "KK009", "programKeahlian", "program", "Teknik Mesin");
        client.insertRecord(tableKonsentrasiKeahlian, "KK009", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK010", "main", "id", "KK010");
        client.insertRecord(tableKonsentrasiKeahlian, "KK010", "main", "konsentrasi", "Teknik Mekanik Industri");
        client.insertRecord(tableKonsentrasiKeahlian, "KK010", "programKeahlian", "id", "PK006");
        client.insertRecord(tableKonsentrasiKeahlian, "KK010", "programKeahlian", "program", "Teknik Mesin");
        client.insertRecord(tableKonsentrasiKeahlian, "KK010", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK011", "main", "id", "KK011");
        client.insertRecord(tableKonsentrasiKeahlian, "KK011", "main", "konsentrasi", "Teknik Pengecoran Logam");
        client.insertRecord(tableKonsentrasiKeahlian, "KK011", "programKeahlian", "id", "PK006");
        client.insertRecord(tableKonsentrasiKeahlian, "KK011", "programKeahlian", "program", "Teknik Mesin");
        client.insertRecord(tableKonsentrasiKeahlian, "KK011", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK012", "main", "id", "KK012");
        client.insertRecord(tableKonsentrasiKeahlian, "KK012", "main", "konsentrasi", "Desain Gambar Mesin");
        client.insertRecord(tableKonsentrasiKeahlian, "KK012", "programKeahlian", "id", "PK006");
        client.insertRecord(tableKonsentrasiKeahlian, "KK012", "programKeahlian", "program", "Teknik Mesin");
        client.insertRecord(tableKonsentrasiKeahlian, "KK012", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK013", "main", "id", "KK013");
        client.insertRecord(tableKonsentrasiKeahlian, "KK013", "main", "konsentrasi", "Teknik Pemesinan Pesawat Udara");
        client.insertRecord(tableKonsentrasiKeahlian, "KK013", "programKeahlian", "id", "PK006");
        client.insertRecord(tableKonsentrasiKeahlian, "KK013", "programKeahlian", "program", "Teknik Mesin");
        client.insertRecord(tableKonsentrasiKeahlian, "KK013", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK014", "main", "id", "KK014");
        client.insertRecord(tableKonsentrasiKeahlian, "KK014", "main", "konsentrasi",
                "Teknik Konstruksi Rangka Pesawat Udara");
        client.insertRecord(tableKonsentrasiKeahlian, "KK014", "programKeahlian", "id", "PK006");
        client.insertRecord(tableKonsentrasiKeahlian, "KK014", "programKeahlian", "program", "Teknik Mesin");
        client.insertRecord(tableKonsentrasiKeahlian, "KK014", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK015", "main", "id", "KK015");
        client.insertRecord(tableKonsentrasiKeahlian, "KK015", "main", "konsentrasi", "Teknik Pemesinan Kapal");
        client.insertRecord(tableKonsentrasiKeahlian, "KK015", "programKeahlian", "id", "PK006");
        client.insertRecord(tableKonsentrasiKeahlian, "KK015", "programKeahlian", "program", "Teknik Mesin");
        client.insertRecord(tableKonsentrasiKeahlian, "KK015", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK016", "main", "id", "KK016");
        client.insertRecord(tableKonsentrasiKeahlian, "KK016", "main", "konsentrasi", "Teknik Kendaraan Ringan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK016", "programKeahlian", "id", "PK007");
        client.insertRecord(tableKonsentrasiKeahlian, "KK016", "programKeahlian", "program", "Teknik Otomotif");
        client.insertRecord(tableKonsentrasiKeahlian, "KK016", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK017", "main", "id", "KK017");
        client.insertRecord(tableKonsentrasiKeahlian, "KK017", "main", "konsentrasi", "Teknik Sepeda Motor");
        client.insertRecord(tableKonsentrasiKeahlian, "KK017", "programKeahlian", "id", "PK007");
        client.insertRecord(tableKonsentrasiKeahlian, "KK017", "programKeahlian", "program", "Teknik Otomotif");
        client.insertRecord(tableKonsentrasiKeahlian, "KK017", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK018", "main", "id", "KK018");
        client.insertRecord(tableKonsentrasiKeahlian, "KK018", "main", "konsentrasi", "Teknik Alat Berat");
        client.insertRecord(tableKonsentrasiKeahlian, "KK018", "programKeahlian", "id", "PK007");
        client.insertRecord(tableKonsentrasiKeahlian, "KK018", "programKeahlian", "program", "Teknik Otomotif");
        client.insertRecord(tableKonsentrasiKeahlian, "KK018", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK019", "main", "id", "KK019");
        client.insertRecord(tableKonsentrasiKeahlian, "KK019", "main", "konsentrasi", "Teknik Ototronik");
        client.insertRecord(tableKonsentrasiKeahlian, "KK019", "programKeahlian", "id", "PK007");
        client.insertRecord(tableKonsentrasiKeahlian, "KK019", "programKeahlian", "program", "Teknik Otomotif");
        client.insertRecord(tableKonsentrasiKeahlian, "KK019", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK020", "main", "id", "KK020");
        client.insertRecord(tableKonsentrasiKeahlian, "KK020", "main", "konsentrasi", "Teknik Bodi Kendaraan Ringan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK020", "programKeahlian", "id", "PK007");
        client.insertRecord(tableKonsentrasiKeahlian, "KK020", "programKeahlian", "program", "Teknik Otomotif");
        client.insertRecord(tableKonsentrasiKeahlian, "KK020", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK021", "main", "id", "KK021");
        client.insertRecord(tableKonsentrasiKeahlian, "KK021", "main", "konsentrasi", "Teknik Pengelasan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK021", "programKeahlian", "id", "PK008");
        client.insertRecord(tableKonsentrasiKeahlian, "KK021", "programKeahlian", "program",
                "Teknik Pengelasan dan Fabrikasi Logam");
        client.insertRecord(tableKonsentrasiKeahlian, "KK021", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK022", "main", "id", "KK022");
        client.insertRecord(tableKonsentrasiKeahlian, "KK022", "main", "konsentrasi", "Teknik Pengelasan Kapal");
        client.insertRecord(tableKonsentrasiKeahlian, "KK022", "programKeahlian", "id", "PK008");
        client.insertRecord(tableKonsentrasiKeahlian, "KK022", "programKeahlian", "program",
                "Teknik Pengelasan dan Fabrikasi Logam");
        client.insertRecord(tableKonsentrasiKeahlian, "KK022", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK023", "main", "id", "KK023");
        client.insertRecord(tableKonsentrasiKeahlian, "KK023", "main", "konsentrasi",
                "Teknik Konstruksi Badan Pesawat Udara");
        client.insertRecord(tableKonsentrasiKeahlian, "KK023", "programKeahlian", "id", "PK008");
        client.insertRecord(tableKonsentrasiKeahlian, "KK023", "programKeahlian", "program",
                "Teknik Pengelasan dan Fabrikasi Logam");
        client.insertRecord(tableKonsentrasiKeahlian, "KK023", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK024", "main", "id", "KK024");
        client.insertRecord(tableKonsentrasiKeahlian, "KK024", "main", "konsentrasi",
                "Teknik Fabrikasi Logam dan Manufaktur");
        client.insertRecord(tableKonsentrasiKeahlian, "KK024", "programKeahlian", "id", "PK008");
        client.insertRecord(tableKonsentrasiKeahlian, "KK024", "programKeahlian", "program",
                "Teknik Pengelasan dan Fabrikasi Logam");
        client.insertRecord(tableKonsentrasiKeahlian, "KK024", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK025", "main", "id", "KK025");
        client.insertRecord(tableKonsentrasiKeahlian, "KK025", "main", "konsentrasi", "Teknik Pengendalian Produksi");
        client.insertRecord(tableKonsentrasiKeahlian, "KK025", "programKeahlian", "id", "PK009");
        client.insertRecord(tableKonsentrasiKeahlian, "KK025", "programKeahlian", "program", "Teknik Logistik");
        client.insertRecord(tableKonsentrasiKeahlian, "KK025", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK026", "main", "id", "KK026");
        client.insertRecord(tableKonsentrasiKeahlian, "KK026", "main", "konsentrasi", "Teknik Logistik");
        client.insertRecord(tableKonsentrasiKeahlian, "KK026", "programKeahlian", "id", "PK009");
        client.insertRecord(tableKonsentrasiKeahlian, "KK026", "programKeahlian", "program", "Teknik Logistik");
        client.insertRecord(tableKonsentrasiKeahlian, "KK026", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK027", "main", "id", "KK027");
        client.insertRecord(tableKonsentrasiKeahlian, "KK027", "main", "konsentrasi", "Teknik Audio Video");
        client.insertRecord(tableKonsentrasiKeahlian, "KK027", "programKeahlian", "id", "PK010");
        client.insertRecord(tableKonsentrasiKeahlian, "KK027", "programKeahlian", "program", "Teknik Elektronika");
        client.insertRecord(tableKonsentrasiKeahlian, "KK027", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK028", "main", "id", "KK028");
        client.insertRecord(tableKonsentrasiKeahlian, "KK028", "main", "konsentrasi", "Teknik Mekatronika");
        client.insertRecord(tableKonsentrasiKeahlian, "KK028", "programKeahlian", "id", "PK010");
        client.insertRecord(tableKonsentrasiKeahlian, "KK028", "programKeahlian", "program", "Teknik Elektronika");
        client.insertRecord(tableKonsentrasiKeahlian, "KK028", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK029", "main", "id", "KK029");
        client.insertRecord(tableKonsentrasiKeahlian, "KK029", "main", "konsentrasi", "Teknik Elektronika Industri");
        client.insertRecord(tableKonsentrasiKeahlian, "KK029", "programKeahlian", "id", "PK010");
        client.insertRecord(tableKonsentrasiKeahlian, "KK029", "programKeahlian", "program", "Teknik Elektronika");
        client.insertRecord(tableKonsentrasiKeahlian, "KK029", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK030", "main", "id", "KK030");
        client.insertRecord(tableKonsentrasiKeahlian, "KK030", "main", "konsentrasi", "Teknik Otomasi Industri");
        client.insertRecord(tableKonsentrasiKeahlian, "KK030", "programKeahlian", "id", "PK010");
        client.insertRecord(tableKonsentrasiKeahlian, "KK030", "programKeahlian", "program", "Teknik Elektronika");
        client.insertRecord(tableKonsentrasiKeahlian, "KK030", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK031", "main", "id", "KK031");
        client.insertRecord(tableKonsentrasiKeahlian, "KK031", "main", "konsentrasi", "Teknik Elektronika Komunikasi");
        client.insertRecord(tableKonsentrasiKeahlian, "KK031", "programKeahlian", "id", "PK010");
        client.insertRecord(tableKonsentrasiKeahlian, "KK031", "programKeahlian", "program", "Teknik Elektronika");
        client.insertRecord(tableKonsentrasiKeahlian, "KK031", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK032", "main", "id", "KK032");
        client.insertRecord(tableKonsentrasiKeahlian, "KK032", "main", "konsentrasi", "Instrumenasi Medik");
        client.insertRecord(tableKonsentrasiKeahlian, "KK032", "programKeahlian", "id", "PK010");
        client.insertRecord(tableKonsentrasiKeahlian, "KK032", "programKeahlian", "program", "Teknik Elektronika");
        client.insertRecord(tableKonsentrasiKeahlian, "KK032", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK033", "main", "id", "KK033");
        client.insertRecord(tableKonsentrasiKeahlian, "KK033", "main", "konsentrasi",
                "Teknik Elektronika Pesawat Udara");
        client.insertRecord(tableKonsentrasiKeahlian, "KK033", "programKeahlian", "id", "PK010");
        client.insertRecord(tableKonsentrasiKeahlian, "KK033", "programKeahlian", "program", "Teknik Elektronika");
        client.insertRecord(tableKonsentrasiKeahlian, "KK033", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK034", "main", "id", "KK034");
        client.insertRecord(tableKonsentrasiKeahlian, "KK034", "main", "konsentrasi",
                "Instrumentasi dan Otomatisasi Proses");
        client.insertRecord(tableKonsentrasiKeahlian, "KK034", "programKeahlian", "id", "PK010");
        client.insertRecord(tableKonsentrasiKeahlian, "KK034", "programKeahlian", "program", "Teknik Elektronika");
        client.insertRecord(tableKonsentrasiKeahlian, "KK034", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK035", "main", "id", "KK035");
        client.insertRecord(tableKonsentrasiKeahlian, "KK035", "main", "konsentrasi", "Airframe Powerplant");
        client.insertRecord(tableKonsentrasiKeahlian, "KK035", "programKeahlian", "id", "PK011");
        client.insertRecord(tableKonsentrasiKeahlian, "KK035", "programKeahlian", "program", "Teknik Pesawat Udara");
        client.insertRecord(tableKonsentrasiKeahlian, "KK035", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK036", "main", "id", "KK036");
        client.insertRecord(tableKonsentrasiKeahlian, "KK036", "main", "konsentrasi", "Electrical Avionic");
        client.insertRecord(tableKonsentrasiKeahlian, "KK036", "programKeahlian", "id", "PK011");
        client.insertRecord(tableKonsentrasiKeahlian, "KK036", "programKeahlian", "program", "Teknik Pesawat Udara");
        client.insertRecord(tableKonsentrasiKeahlian, "KK036", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK037", "main", "id", "KK037");
        client.insertRecord(tableKonsentrasiKeahlian, "KK037", "main", "konsentrasi", "Desain Rancang Bangun Kapal");
        client.insertRecord(tableKonsentrasiKeahlian, "KK037", "programKeahlian", "id", "PK012");
        client.insertRecord(tableKonsentrasiKeahlian, "KK037", "programKeahlian", "program", "Teknik Konstruksi Kapal");
        client.insertRecord(tableKonsentrasiKeahlian, "KK037", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK038", "main", "id", "KK038");
        client.insertRecord(tableKonsentrasiKeahlian, "KK038", "main", "konsentrasi", "Konstruksi Kapal Baja");
        client.insertRecord(tableKonsentrasiKeahlian, "KK038", "programKeahlian", "id", "PK012");
        client.insertRecord(tableKonsentrasiKeahlian, "KK038", "programKeahlian", "program", "Teknik Konstruksi Kapal");
        client.insertRecord(tableKonsentrasiKeahlian, "KK038", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK039", "main", "id", "KK039");
        client.insertRecord(tableKonsentrasiKeahlian, "KK039", "main", "konsentrasi", "Konstruksi Kapal Non Baja");
        client.insertRecord(tableKonsentrasiKeahlian, "KK039", "programKeahlian", "id", "PK012");
        client.insertRecord(tableKonsentrasiKeahlian, "KK039", "programKeahlian", "program", "Teknik Konstruksi Kapal");
        client.insertRecord(tableKonsentrasiKeahlian, "KK039", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK040", "main", "id", "KK040");
        client.insertRecord(tableKonsentrasiKeahlian, "KK040", "main", "konsentrasi", "Interior Kapal");
        client.insertRecord(tableKonsentrasiKeahlian, "KK040", "programKeahlian", "id", "PK012");
        client.insertRecord(tableKonsentrasiKeahlian, "KK040", "programKeahlian", "program", "Teknik Konstruksi Kapal");
        client.insertRecord(tableKonsentrasiKeahlian, "KK040", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK041", "main", "id", "KK041");
        client.insertRecord(tableKonsentrasiKeahlian, "KK041", "main", "konsentrasi", "Kimia Analisis");
        client.insertRecord(tableKonsentrasiKeahlian, "KK041", "programKeahlian", "id", "PK013");
        client.insertRecord(tableKonsentrasiKeahlian, "KK041", "programKeahlian", "program", "Kimia Analisis");
        client.insertRecord(tableKonsentrasiKeahlian, "KK041", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK042", "main", "id", "KK042");
        client.insertRecord(tableKonsentrasiKeahlian, "KK042", "main", "konsentrasi",
                "Analisis Pengujian Laboratorium");
        client.insertRecord(tableKonsentrasiKeahlian, "KK042", "programKeahlian", "id", "PK013");
        client.insertRecord(tableKonsentrasiKeahlian, "KK042", "programKeahlian", "program", "Kimia Analisis");
        client.insertRecord(tableKonsentrasiKeahlian, "KK042", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK043", "main", "id", "KK043");
        client.insertRecord(tableKonsentrasiKeahlian, "KK043", "main", "konsentrasi", "Teknik Kimia Industri");
        client.insertRecord(tableKonsentrasiKeahlian, "KK043", "programKeahlian", "id", "PK014");
        client.insertRecord(tableKonsentrasiKeahlian, "KK043", "programKeahlian", "program", "Teknik Kimia Industri");
        client.insertRecord(tableKonsentrasiKeahlian, "KK043", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK044", "main", "id", "KK044");
        client.insertRecord(tableKonsentrasiKeahlian, "KK044", "main", "konsentrasi", "Kimia Tekstil");
        client.insertRecord(tableKonsentrasiKeahlian, "KK044", "programKeahlian", "id", "PK014");
        client.insertRecord(tableKonsentrasiKeahlian, "KK044", "programKeahlian", "program", "Teknik Kimia Industri");
        client.insertRecord(tableKonsentrasiKeahlian, "KK044", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK045", "main", "id", "KK045");
        client.insertRecord(tableKonsentrasiKeahlian, "KK045", "main", "konsentrasi", "Teknik Pembuatan Serat Filamen");
        client.insertRecord(tableKonsentrasiKeahlian, "KK045", "programKeahlian", "id", "PK015");
        client.insertRecord(tableKonsentrasiKeahlian, "KK045", "programKeahlian", "program", "Teknik Tekstil");
        client.insertRecord(tableKonsentrasiKeahlian, "KK045", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK046", "main", "id", "KK046");
        client.insertRecord(tableKonsentrasiKeahlian, "KK046", "main", "konsentrasi", "Teknik Pembuatan Benang Stapel");
        client.insertRecord(tableKonsentrasiKeahlian, "KK046", "programKeahlian", "id", "PK015");
        client.insertRecord(tableKonsentrasiKeahlian, "KK046", "programKeahlian", "program", "Teknik Tekstil");
        client.insertRecord(tableKonsentrasiKeahlian, "KK046", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK047", "main", "id", "KK047");
        client.insertRecord(tableKonsentrasiKeahlian, "KK047", "main", "konsentrasi", "Teknik Pembuatan Kain");
        client.insertRecord(tableKonsentrasiKeahlian, "KK047", "programKeahlian", "id", "PK015");
        client.insertRecord(tableKonsentrasiKeahlian, "KK047", "programKeahlian", "program", "Teknik Tekstil");
        client.insertRecord(tableKonsentrasiKeahlian, "KK047", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK048", "main", "id", "KK048");
        client.insertRecord(tableKonsentrasiKeahlian, "KK048", "main", "konsentrasi", "Teknik Penyempurnaan Tekstil");
        client.insertRecord(tableKonsentrasiKeahlian, "KK048", "programKeahlian", "id", "PK015");
        client.insertRecord(tableKonsentrasiKeahlian, "KK048", "programKeahlian", "program", "Teknik Tekstil");
        client.insertRecord(tableKonsentrasiKeahlian, "KK048", "detail", "created_by", "Doyatama");

        // BK003
        client.insertRecord(tableKonsentrasiKeahlian, "KK049", "main", "id", "KK049");
        client.insertRecord(tableKonsentrasiKeahlian, "KK049", "main", "konsentrasi",
                "Teknik Instalasi Tenaga Listrik");
        client.insertRecord(tableKonsentrasiKeahlian, "KK049", "programKeahlian", "id", "PK016");
        client.insertRecord(tableKonsentrasiKeahlian, "KK049", "programKeahlian", "program",
                "Teknik Ketenagalistrikan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK049", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK050", "main", "id", "KK050");
        client.insertRecord(tableKonsentrasiKeahlian, "KK050", "main", "konsentrasi",
                "Teknik Pembangkit Tenaga Listrik");
        client.insertRecord(tableKonsentrasiKeahlian, "KK050", "programKeahlian", "id", "PK016");
        client.insertRecord(tableKonsentrasiKeahlian, "KK050", "programKeahlian", "program",
                "Teknik Ketenagalistrikan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK050", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK051", "main", "id", "KK051");
        client.insertRecord(tableKonsentrasiKeahlian, "KK051", "main", "konsentrasi", "Teknik Jaringan Tenaga Listrik");
        client.insertRecord(tableKonsentrasiKeahlian, "KK051", "programKeahlian", "id", "PK016");
        client.insertRecord(tableKonsentrasiKeahlian, "KK051", "programKeahlian", "program",
                "Teknik Ketenagalistrikan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK051", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK052", "main", "id", "KK052");
        client.insertRecord(tableKonsentrasiKeahlian, "KK052", "main", "konsentrasi",
                "Teknik Pemanasan, Tata Udara, dan Pendinginan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK052", "programKeahlian", "id", "PK016");
        client.insertRecord(tableKonsentrasiKeahlian, "KK052", "programKeahlian", "program",
                "Teknik Ketenagalistrikan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK052", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK053", "main", "id", "KK053");
        client.insertRecord(tableKonsentrasiKeahlian, "KK053", "main", "konsentrasi",
                "Teknik Kelistrikan Pesawat Udara");
        client.insertRecord(tableKonsentrasiKeahlian, "KK053", "programKeahlian", "id", "PK016");
        client.insertRecord(tableKonsentrasiKeahlian, "KK053", "programKeahlian", "program",
                "Teknik Ketenagalistrikan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK053", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK054", "main", "id", "KK054");
        client.insertRecord(tableKonsentrasiKeahlian, "KK054", "main", "konsentrasi", "Teknik Kelistrikan Kapal");
        client.insertRecord(tableKonsentrasiKeahlian, "KK054", "programKeahlian", "id", "PK016");
        client.insertRecord(tableKonsentrasiKeahlian, "KK054", "programKeahlian", "program",
                "Teknik Ketenagalistrikan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK054", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK055", "main", "id", "KK055");
        client.insertRecord(tableKonsentrasiKeahlian, "KK055", "main", "konsentrasi",
                "Teknik Energi Surya, Hidro, dan Angin");
        client.insertRecord(tableKonsentrasiKeahlian, "KK055", "programKeahlian", "id", "PK017");
        client.insertRecord(tableKonsentrasiKeahlian, "KK055", "programKeahlian", "program",
                "Teknik Energi Terbarukan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK055", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK056", "main", "id", "KK056");
        client.insertRecord(tableKonsentrasiKeahlian, "KK056", "main", "konsentrasi", "Teknik Energi Biomassa");
        client.insertRecord(tableKonsentrasiKeahlian, "KK056", "programKeahlian", "id", "PK017");
        client.insertRecord(tableKonsentrasiKeahlian, "KK056", "programKeahlian", "program",
                "Teknik Energi Terbarukan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK056", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK057", "main", "id", "KK057");
        client.insertRecord(tableKonsentrasiKeahlian, "KK057", "main", "konsentrasi", "Teknik Geomatika");
        client.insertRecord(tableKonsentrasiKeahlian, "KK057", "programKeahlian", "id", "PK018");
        client.insertRecord(tableKonsentrasiKeahlian, "KK057", "programKeahlian", "program", "Teknik Geospasial");
        client.insertRecord(tableKonsentrasiKeahlian, "KK057", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK058", "main", "id", "KK058");
        client.insertRecord(tableKonsentrasiKeahlian, "KK058", "main", "konsentrasi", "Informasi Geospasial");
        client.insertRecord(tableKonsentrasiKeahlian, "KK058", "programKeahlian", "id", "PK018");
        client.insertRecord(tableKonsentrasiKeahlian, "KK058", "programKeahlian", "program", "Teknik Geospasial");
        client.insertRecord(tableKonsentrasiKeahlian, "KK058", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK059", "main", "id", "KK059");
        client.insertRecord(tableKonsentrasiKeahlian, "KK059", "main", "konsentrasi", "Geologi Pertambangan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK059", "programKeahlian", "id", "PK019");
        client.insertRecord(tableKonsentrasiKeahlian, "KK059", "programKeahlian", "program",
                "Teknik Geologi Pertambangan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK059", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK060", "main", "id", "KK060");
        client.insertRecord(tableKonsentrasiKeahlian, "KK060", "main", "konsentrasi", "Teknik Produksi Minyak dan Gas");
        client.insertRecord(tableKonsentrasiKeahlian, "KK060", "programKeahlian", "id", "PK020");
        client.insertRecord(tableKonsentrasiKeahlian, "KK060", "programKeahlian", "program", "Teknik Perminyakan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK060", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK061", "main", "id", "KK061");
        client.insertRecord(tableKonsentrasiKeahlian, "KK061", "main", "konsentrasi", "Teknik Pemboran Minyak dan Gas");
        client.insertRecord(tableKonsentrasiKeahlian, "KK061", "programKeahlian", "id", "PK020");
        client.insertRecord(tableKonsentrasiKeahlian, "KK061", "programKeahlian", "program", "Teknik Perminyakan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK061", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK062", "main", "id", "KK062");
        client.insertRecord(tableKonsentrasiKeahlian, "KK062", "main", "konsentrasi",
                "Teknik Pengolahan Minyak, Gas dan Petrokimia");
        client.insertRecord(tableKonsentrasiKeahlian, "KK062", "programKeahlian", "id", "PK020");
        client.insertRecord(tableKonsentrasiKeahlian, "KK062", "programKeahlian", "program", "Teknik Perminyakan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK062", "detail", "created_by", "Doyatama");

        // BK004
        client.insertRecord(tableKonsentrasiKeahlian, "KK063", "main", "id", "KK063");
        client.insertRecord(tableKonsentrasiKeahlian, "KK063", "main", "konsentrasi", "Rekayasa Perangkat Lunak");
        client.insertRecord(tableKonsentrasiKeahlian, "KK063", "programKeahlian", "id", "PK021");
        client.insertRecord(tableKonsentrasiKeahlian, "KK063", "programKeahlian", "program",
                "Pengembangan Perangkat Lunak dan Gim");
        client.insertRecord(tableKonsentrasiKeahlian, "KK063", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK064", "main", "id", "KK064");
        client.insertRecord(tableKonsentrasiKeahlian, "KK064", "main", "konsentrasi", "Pengembangan Gim");
        client.insertRecord(tableKonsentrasiKeahlian, "KK064", "programKeahlian", "id", "PK021");
        client.insertRecord(tableKonsentrasiKeahlian, "KK064", "programKeahlian", "program",
                "Pengembangan Perangkat Lunak dan Gim");
        client.insertRecord(tableKonsentrasiKeahlian, "KK064", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK065", "main", "id", "KK065");
        client.insertRecord(tableKonsentrasiKeahlian, "KK065", "main", "konsentrasi",
                "Sistem Informasi, Jaringan, dan Aplikasi");
        client.insertRecord(tableKonsentrasiKeahlian, "KK065", "programKeahlian", "id", "PK021");
        client.insertRecord(tableKonsentrasiKeahlian, "KK065", "programKeahlian", "program",
                "Pengembangan Perangkat Lunak dan Gim");
        client.insertRecord(tableKonsentrasiKeahlian, "KK065", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK066", "main", "id", "KK066");
        client.insertRecord(tableKonsentrasiKeahlian, "KK066", "main", "konsentrasi", "Teknik Komputer dan Jaringan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK066", "programKeahlian", "id", "PK022");
        client.insertRecord(tableKonsentrasiKeahlian, "KK066", "programKeahlian", "program",
                "Teknik Jaringan Komputer dan Telekomunikasi");
        client.insertRecord(tableKonsentrasiKeahlian, "KK066", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK067", "main", "id", "KK067");
        client.insertRecord(tableKonsentrasiKeahlian, "KK067", "main", "konsentrasi",
                "Teknik Jaringan Akses Telekomunikasi");
        client.insertRecord(tableKonsentrasiKeahlian, "KK067", "programKeahlian", "id", "PK022");
        client.insertRecord(tableKonsentrasiKeahlian, "KK067", "programKeahlian", "program",
                "Teknik Jaringan Komputer dan Telekomunikasi");
        client.insertRecord(tableKonsentrasiKeahlian, "KK067", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK068", "main", "id", "KK068");
        client.insertRecord(tableKonsentrasiKeahlian, "KK068", "main", "konsentrasi",
                "Teknik Transmisi Telekomunikasi");
        client.insertRecord(tableKonsentrasiKeahlian, "KK068", "programKeahlian", "id", "PK022");
        client.insertRecord(tableKonsentrasiKeahlian, "KK068", "programKeahlian", "program",
                "Teknik Jaringan Komputer dan Telekomunikasi");
        client.insertRecord(tableKonsentrasiKeahlian, "KK068", "detail", "created_by", "Doyatama");

        // BK005
        client.insertRecord(tableKonsentrasiKeahlian, "KK069", "main", "id", "KK069");
        client.insertRecord(tableKonsentrasiKeahlian, "KK069", "main", "konsentrasi",
                "Layanan Penunjang Keperawatan dan Caregiving");
        client.insertRecord(tableKonsentrasiKeahlian, "KK069", "programKeahlian", "id", "PK023");
        client.insertRecord(tableKonsentrasiKeahlian, "KK069", "programKeahlian", "program", "Layanan Kesehatan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK069", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK070", "main", "id", "KK070");
        client.insertRecord(tableKonsentrasiKeahlian, "KK070", "main", "konsentrasi", "Layanan Penunjang Dental Care");
        client.insertRecord(tableKonsentrasiKeahlian, "KK070", "programKeahlian", "id", "PK023");
        client.insertRecord(tableKonsentrasiKeahlian, "KK070", "programKeahlian", "program", "Layanan Kesehatan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK070", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK071", "main", "id", "KK071");
        client.insertRecord(tableKonsentrasiKeahlian, "KK071", "main", "konsentrasi",
                "Layanan Penunjang Laboratorium Medik");
        client.insertRecord(tableKonsentrasiKeahlian, "KK071", "programKeahlian", "id", "PK024");
        client.insertRecord(tableKonsentrasiKeahlian, "KK071", "programKeahlian", "program",
                "Teknik Laboratorium Medik");
        client.insertRecord(tableKonsentrasiKeahlian, "KK071", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK072", "main", "id", "KK072");
        client.insertRecord(tableKonsentrasiKeahlian, "KK072", "main", "konsentrasi",
                "Layanan Penunjang Kefarmasian Klinis dan Komunitas");
        client.insertRecord(tableKonsentrasiKeahlian, "KK072", "programKeahlian", "id", "PK025");
        client.insertRecord(tableKonsentrasiKeahlian, "KK072", "programKeahlian", "program", "Teknologi Farmasi");
        client.insertRecord(tableKonsentrasiKeahlian, "KK072", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK073", "main", "id", "KK073");
        client.insertRecord(tableKonsentrasiKeahlian, "KK073", "main", "konsentrasi", "Farmasi Industri");
        client.insertRecord(tableKonsentrasiKeahlian, "KK073", "programKeahlian", "id", "PK025");
        client.insertRecord(tableKonsentrasiKeahlian, "KK073", "programKeahlian", "program", "Teknologi Farmasi");
        client.insertRecord(tableKonsentrasiKeahlian, "KK073", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK074", "main", "id", "KK074");
        client.insertRecord(tableKonsentrasiKeahlian, "KK074", "main", "konsentrasi", "Pekerjaan Sosial");
        client.insertRecord(tableKonsentrasiKeahlian, "KK074", "programKeahlian", "id", "PK026");
        client.insertRecord(tableKonsentrasiKeahlian, "KK074", "programKeahlian", "program", "Pekerjaan Sosial");
        client.insertRecord(tableKonsentrasiKeahlian, "KK074", "detail", "created_by", "Doyatama");

        // BK006
        client.insertRecord(tableKonsentrasiKeahlian, "KK075", "main", "id", "KK075");
        client.insertRecord(tableKonsentrasiKeahlian, "KK075", "main", "konsentrasi", "Agribisnis Tanaman Perkebunan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK075", "programKeahlian", "id", "PK027");
        client.insertRecord(tableKonsentrasiKeahlian, "KK075", "programKeahlian", "program", "Agribisnis Tanaman");
        client.insertRecord(tableKonsentrasiKeahlian, "KK075", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK076", "main", "id", "KK076");
        client.insertRecord(tableKonsentrasiKeahlian, "KK076", "main", "konsentrasi",
                "Agribisnis Tanaman Pangan dan Horticultura");
        client.insertRecord(tableKonsentrasiKeahlian, "KK076", "programKeahlian", "id", "PK027");
        client.insertRecord(tableKonsentrasiKeahlian, "KK076", "programKeahlian", "program", "Agribisnis Tanaman");
        client.insertRecord(tableKonsentrasiKeahlian, "KK076", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK077", "main", "id", "KK077");
        client.insertRecord(tableKonsentrasiKeahlian, "KK077", "main", "konsentrasi", "Agribisnis Perbenihan Tanaman");
        client.insertRecord(tableKonsentrasiKeahlian, "KK077", "programKeahlian", "id", "PK027");
        client.insertRecord(tableKonsentrasiKeahlian, "KK077", "programKeahlian", "program", "Agribisnis Tanaman");
        client.insertRecord(tableKonsentrasiKeahlian, "KK077", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK078", "main", "id", "KK078");
        client.insertRecord(tableKonsentrasiKeahlian, "KK078", "main", "konsentrasi",
                "Agribisnis Lanskap dan Pertamanan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK078", "programKeahlian", "id", "PK027");
        client.insertRecord(tableKonsentrasiKeahlian, "KK078", "programKeahlian", "program", "Agribisnis Tanaman");
        client.insertRecord(tableKonsentrasiKeahlian, "KK078", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK079", "main", "id", "KK079");
        client.insertRecord(tableKonsentrasiKeahlian, "KK079", "main", "konsentrasi", "Agribisnis Ternak Ruminansia");
        client.insertRecord(tableKonsentrasiKeahlian, "KK079", "programKeahlian", "id", "PK028");
        client.insertRecord(tableKonsentrasiKeahlian, "KK079", "programKeahlian", "program", "Agribisnis Ternak");
        client.insertRecord(tableKonsentrasiKeahlian, "KK079", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK080", "main", "id", "KK080");
        client.insertRecord(tableKonsentrasiKeahlian, "KK080", "main", "konsentrasi", "Agribisnis Ternak Unggas");
        client.insertRecord(tableKonsentrasiKeahlian, "KK080", "programKeahlian", "id", "PK028");
        client.insertRecord(tableKonsentrasiKeahlian, "KK080", "programKeahlian", "program", "Agribisnis Ternak");
        client.insertRecord(tableKonsentrasiKeahlian, "KK080", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK081", "main", "id", "KK081");
        client.insertRecord(tableKonsentrasiKeahlian, "KK081", "main", "konsentrasi", "Kesehatan Hewan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK081", "programKeahlian", "id", "PK028");
        client.insertRecord(tableKonsentrasiKeahlian, "KK081", "programKeahlian", "program", "Agribisnis Ternak");
        client.insertRecord(tableKonsentrasiKeahlian, "KK081", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK082", "main", "id", "KK082");
        client.insertRecord(tableKonsentrasiKeahlian, "KK082", "main", "konsentrasi", "Agribisnis Ikan Hias");
        client.insertRecord(tableKonsentrasiKeahlian, "KK082", "programKeahlian", "id", "PK029");
        client.insertRecord(tableKonsentrasiKeahlian, "KK082", "programKeahlian", "program", "Agribisnis Perikanan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK082", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK083", "main", "id", "KK083");
        client.insertRecord(tableKonsentrasiKeahlian, "KK083", "main", "konsentrasi",
                "Agribisnis Perikanan Payau dan Laut");
        client.insertRecord(tableKonsentrasiKeahlian, "KK083", "programKeahlian", "id", "PK029");
        client.insertRecord(tableKonsentrasiKeahlian, "KK083", "programKeahlian", "program", "Agribisnis Perikanan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK083", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK084", "main", "id", "KK084");
        client.insertRecord(tableKonsentrasiKeahlian, "KK084", "main", "konsentrasi", "Agribisnis Perikanan Air Tawar");
        client.insertRecord(tableKonsentrasiKeahlian, "KK084", "programKeahlian", "id", "PK029");
        client.insertRecord(tableKonsentrasiKeahlian, "KK084", "programKeahlian", "program", "Agribisnis Perikanan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK084", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK085", "main", "id", "KK085");
        client.insertRecord(tableKonsentrasiKeahlian, "KK085", "main", "konsentrasi", "Agribisnis Rumput Laut");
        client.insertRecord(tableKonsentrasiKeahlian, "KK085", "programKeahlian", "id", "PK029");
        client.insertRecord(tableKonsentrasiKeahlian, "KK085", "programKeahlian", "program", "Agribisnis Perikanan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK085", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK086", "main", "id", "KK086");
        client.insertRecord(tableKonsentrasiKeahlian, "KK086", "main", "konsentrasi", "Usaha Pertanian Terpadu");
        client.insertRecord(tableKonsentrasiKeahlian, "KK086", "programKeahlian", "id", "PK030");
        client.insertRecord(tableKonsentrasiKeahlian, "KK086", "programKeahlian", "program", "Usaha Pertanian Terpadu");
        client.insertRecord(tableKonsentrasiKeahlian, "KK086", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK087", "main", "id", "KK087");
        client.insertRecord(tableKonsentrasiKeahlian, "KK087", "main", "konsentrasi", "Mekanisasi Pertanian");
        client.insertRecord(tableKonsentrasiKeahlian, "KK087", "programKeahlian", "id", "PK031");
        client.insertRecord(tableKonsentrasiKeahlian, "KK087", "programKeahlian", "program",
                "Agritekno Pengolahan Hasil Pertanian");
        client.insertRecord(tableKonsentrasiKeahlian, "KK087", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK088", "main", "id", "KK088");
        client.insertRecord(tableKonsentrasiKeahlian, "KK088", "main", "konsentrasi",
                "Agribisnis Pengolahan Hasil Pertanian");
        client.insertRecord(tableKonsentrasiKeahlian, "KK088", "programKeahlian", "id", "PK031");
        client.insertRecord(tableKonsentrasiKeahlian, "KK088", "programKeahlian", "program",
                "Agritekno Pengolahan Hasil Pertanian");
        client.insertRecord(tableKonsentrasiKeahlian, "KK088", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK089", "main", "id", "KK089");
        client.insertRecord(tableKonsentrasiKeahlian, "KK089", "main", "konsentrasi",
                "Agribisnis Pengolahan Hasil Perikanan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK089", "programKeahlian", "id", "PK031");
        client.insertRecord(tableKonsentrasiKeahlian, "KK089", "programKeahlian", "program",
                "Agritekno Pengolahan Hasil Pertanian");
        client.insertRecord(tableKonsentrasiKeahlian, "KK089", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK090", "main", "id", "KK090");
        client.insertRecord(tableKonsentrasiKeahlian, "KK090", "main", "konsentrasi",
                "Pengawasan Mutu Hasil Pertanian");
        client.insertRecord(tableKonsentrasiKeahlian, "KK090", "programKeahlian", "id", "PK031");
        client.insertRecord(tableKonsentrasiKeahlian, "KK090", "programKeahlian", "program",
                "Agritekno Pengolahan Hasil Pertanian");
        client.insertRecord(tableKonsentrasiKeahlian, "KK090", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK091", "main", "id", "KK091");
        client.insertRecord(tableKonsentrasiKeahlian, "KK091", "main", "konsentrasi", "Kehutanan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK091", "programKeahlian", "id", "PK032");
        client.insertRecord(tableKonsentrasiKeahlian, "KK091", "programKeahlian", "program", "Kehutanan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK091", "detail", "created_by", "Doyatama");

        // BK007
        client.insertRecord(tableKonsentrasiKeahlian, "KK092", "main", "id", "KK092");
        client.insertRecord(tableKonsentrasiKeahlian, "KK092", "main", "konsentrasi", "Teknika Kapal Penangkap Ikan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK092", "programKeahlian", "id", "PK033");
        client.insertRecord(tableKonsentrasiKeahlian, "KK092", "programKeahlian", "program",
                "Teknika Kapal Penangkap Ikan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK092", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK093", "main", "id", "KK093");
        client.insertRecord(tableKonsentrasiKeahlian, "KK093", "main", "konsentrasi", "Nautika Kapal Penangkap Ikan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK093", "programKeahlian", "id", "PK034");
        client.insertRecord(tableKonsentrasiKeahlian, "KK093", "programKeahlian", "program",
                "Nautika Kapal Penangkap Ikan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK093", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK094", "main", "id", "KK094");
        client.insertRecord(tableKonsentrasiKeahlian, "KK094", "main", "konsentrasi", "Teknika Kapal Niaga");
        client.insertRecord(tableKonsentrasiKeahlian, "KK094", "programKeahlian", "id", "PK035");
        client.insertRecord(tableKonsentrasiKeahlian, "KK094", "programKeahlian", "program", "Teknika Kapal Niaga");
        client.insertRecord(tableKonsentrasiKeahlian, "KK094", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK095", "main", "id", "KK095");
        client.insertRecord(tableKonsentrasiKeahlian, "KK095", "main", "konsentrasi", "Nautika Kapal Niaga");
        client.insertRecord(tableKonsentrasiKeahlian, "KK095", "programKeahlian", "id", "PK036");
        client.insertRecord(tableKonsentrasiKeahlian, "KK095", "programKeahlian", "program", "Nautika Kapal Niaga");
        client.insertRecord(tableKonsentrasiKeahlian, "KK095", "detail", "created_by", "Doyatama");

        // BK008
        client.insertRecord(tableKonsentrasiKeahlian, "KK096", "main", "id", "KK096");
        client.insertRecord(tableKonsentrasiKeahlian, "KK096", "main", "konsentrasi", "Bisnis Digital");
        client.insertRecord(tableKonsentrasiKeahlian, "KK096", "programKeahlian", "id", "PK037");
        client.insertRecord(tableKonsentrasiKeahlian, "KK096", "programKeahlian", "program", "Pemasaran");
        client.insertRecord(tableKonsentrasiKeahlian, "KK096", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK097", "main", "id", "KK097");
        client.insertRecord(tableKonsentrasiKeahlian, "KK097", "main", "konsentrasi", "Bisnis Retail");
        client.insertRecord(tableKonsentrasiKeahlian, "KK097", "programKeahlian", "id", "PK037");
        client.insertRecord(tableKonsentrasiKeahlian, "KK097", "programKeahlian", "program", "Pemasaran");
        client.insertRecord(tableKonsentrasiKeahlian, "KK097", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK098", "main", "id", "KK098");
        client.insertRecord(tableKonsentrasiKeahlian, "KK098", "main", "konsentrasi", "Manajemen Perkantoran");
        client.insertRecord(tableKonsentrasiKeahlian, "KK098", "programKeahlian", "id", "PK038");
        client.insertRecord(tableKonsentrasiKeahlian, "KK098", "programKeahlian", "program",
                "Manajemen Perkantoran dan Layanan Bisnis");
        client.insertRecord(tableKonsentrasiKeahlian, "KK098", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK099", "main", "id", "KK099");
        client.insertRecord(tableKonsentrasiKeahlian, "KK099", "main", "konsentrasi", "Manajemen Logistik");
        client.insertRecord(tableKonsentrasiKeahlian, "KK099", "programKeahlian", "id", "PK038");
        client.insertRecord(tableKonsentrasiKeahlian, "KK099", "programKeahlian", "program",
                "Manajemen Perkantoran dan Layanan Bisnis");
        client.insertRecord(tableKonsentrasiKeahlian, "KK099", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK100", "main", "id", "KK100");
        client.insertRecord(tableKonsentrasiKeahlian, "KK100", "main", "konsentrasi", "Layanan Perbankan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK100", "programKeahlian", "id", "PK039");
        client.insertRecord(tableKonsentrasiKeahlian, "KK100", "programKeahlian", "program",
                "Akuntansi dan Keuangan Lembaga");
        client.insertRecord(tableKonsentrasiKeahlian, "KK100", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK101", "main", "id", "KK101");
        client.insertRecord(tableKonsentrasiKeahlian, "KK101", "main", "konsentrasi", "Layanan Perbankan Syariah");
        client.insertRecord(tableKonsentrasiKeahlian, "KK101", "programKeahlian", "id", "PK039");
        client.insertRecord(tableKonsentrasiKeahlian, "KK101", "programKeahlian", "program",
                "Akuntansi dan Keuangan Lembaga");
        client.insertRecord(tableKonsentrasiKeahlian, "KK101", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK102", "main", "id", "KK102");
        client.insertRecord(tableKonsentrasiKeahlian, "KK102", "main", "konsentrasi", "Akuntansi");
        client.insertRecord(tableKonsentrasiKeahlian, "KK102", "programKeahlian", "id", "PK039");
        client.insertRecord(tableKonsentrasiKeahlian, "KK102", "programKeahlian", "program",
                "Akuntansi dan Keuangan Lembaga");
        client.insertRecord(tableKonsentrasiKeahlian, "KK102", "detail", "created_by", "Doyatama");

        // BK009
        client.insertRecord(tableKonsentrasiKeahlian, "KK103", "main", "id", "KK103");
        client.insertRecord(tableKonsentrasiKeahlian, "KK103", "main", "konsentrasi", "Usaha Layanan Wisata");
        client.insertRecord(tableKonsentrasiKeahlian, "KK103", "programKeahlian", "id", "PK040");
        client.insertRecord(tableKonsentrasiKeahlian, "KK103", "programKeahlian", "program",
                "Usaha Layanan Pariwisata");
        client.insertRecord(tableKonsentrasiKeahlian, "KK103", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK104", "main", "id", "KK104");
        client.insertRecord(tableKonsentrasiKeahlian, "KK104", "main", "konsentrasi", "Ekowisata");
        client.insertRecord(tableKonsentrasiKeahlian, "KK104", "programKeahlian", "id", "PK040");
        client.insertRecord(tableKonsentrasiKeahlian, "KK104", "programKeahlian", "program",
                "Usaha Layanan Pariwisata");
        client.insertRecord(tableKonsentrasiKeahlian, "KK104", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK105", "main", "id", "KK105");
        client.insertRecord(tableKonsentrasiKeahlian, "KK105", "main", "konsentrasi", "Perhotelan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK105", "programKeahlian", "id", "PK041");
        client.insertRecord(tableKonsentrasiKeahlian, "KK105", "programKeahlian", "program", "Perhotelan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK105", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK106", "main", "id", "KK106");
        client.insertRecord(tableKonsentrasiKeahlian, "KK106", "main", "konsentrasi", "Kuliner");
        client.insertRecord(tableKonsentrasiKeahlian, "KK106", "programKeahlian", "id", "PK042");
        client.insertRecord(tableKonsentrasiKeahlian, "KK106", "programKeahlian", "program", "Kuliner");
        client.insertRecord(tableKonsentrasiKeahlian, "KK106", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK107", "main", "id", "KK107");
        client.insertRecord(tableKonsentrasiKeahlian, "KK107", "main", "konsentrasi",
                "Tata Kecantikan Kulit dan Rambut");
        client.insertRecord(tableKonsentrasiKeahlian, "KK107", "programKeahlian", "id", "PK043");
        client.insertRecord(tableKonsentrasiKeahlian, "KK107", "programKeahlian", "program", "Kecantikan dan Spa");
        client.insertRecord(tableKonsentrasiKeahlian, "KK107", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK108", "main", "id", "KK108");
        client.insertRecord(tableKonsentrasiKeahlian, "KK108", "main", "konsentrasi", "Spa dan Beauty Therapy");
        client.insertRecord(tableKonsentrasiKeahlian, "KK108", "programKeahlian", "id", "PK043");
        client.insertRecord(tableKonsentrasiKeahlian, "KK108", "programKeahlian", "program", "Kecantikan dan Spa");
        client.insertRecord(tableKonsentrasiKeahlian, "KK108", "detail", "created_by", "Doyatama");

        // BK010
        client.insertRecord(tableKonsentrasiKeahlian, "KK109", "main", "id", "KK109");
        client.insertRecord(tableKonsentrasiKeahlian, "KK109", "main", "konsentrasi", "Seni Lukis");
        client.insertRecord(tableKonsentrasiKeahlian, "KK109", "programKeahlian", "id", "PK044");
        client.insertRecord(tableKonsentrasiKeahlian, "KK109", "programKeahlian", "program", "Seni Rupa");
        client.insertRecord(tableKonsentrasiKeahlian, "KK109", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK110", "main", "id", "KK110");
        client.insertRecord(tableKonsentrasiKeahlian, "KK110", "main", "konsentrasi", "Seni Patung");
        client.insertRecord(tableKonsentrasiKeahlian, "KK110", "programKeahlian", "id", "PK044");
        client.insertRecord(tableKonsentrasiKeahlian, "KK110", "programKeahlian", "program", "Seni Rupa");
        client.insertRecord(tableKonsentrasiKeahlian, "KK110", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK111", "main", "id", "KK111");
        client.insertRecord(tableKonsentrasiKeahlian, "KK111", "main", "konsentrasi", "Desain Komunikasi Visual");
        client.insertRecord(tableKonsentrasiKeahlian, "KK111", "programKeahlian", "id", "PK045");
        client.insertRecord(tableKonsentrasiKeahlian, "KK111", "programKeahlian", "program",
                "Desain Komunikasi Visual");
        client.insertRecord(tableKonsentrasiKeahlian, "KK111", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK112", "main", "id", "KK112");
        client.insertRecord(tableKonsentrasiKeahlian, "KK112", "main", "konsentrasi", "Teknik Grafika");
        client.insertRecord(tableKonsentrasiKeahlian, "KK112", "programKeahlian", "id", "PK045");
        client.insertRecord(tableKonsentrasiKeahlian, "KK112", "programKeahlian", "program",
                "Desain Komunikasi Visual");
        client.insertRecord(tableKonsentrasiKeahlian, "KK112", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK113", "main", "id", "KK113");
        client.insertRecord(tableKonsentrasiKeahlian, "KK113", "main", "konsentrasi",
                "Kriya Kreatif Batik dan Tekstil");
        client.insertRecord(tableKonsentrasiKeahlian, "KK113", "programKeahlian", "id", "PK046");
        client.insertRecord(tableKonsentrasiKeahlian, "KK113", "programKeahlian", "program",
                "Desain dan Produksi Kriya");
        client.insertRecord(tableKonsentrasiKeahlian, "KK113", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK114", "main", "id", "KK114");
        client.insertRecord(tableKonsentrasiKeahlian, "KK114", "main", "konsentrasi",
                "Kriya Kreatif Kulit dan Imitasi");
        client.insertRecord(tableKonsentrasiKeahlian, "KK114", "programKeahlian", "id", "PK046");
        client.insertRecord(tableKonsentrasiKeahlian, "KK114", "programKeahlian", "program",
                "Desain dan Produksi Kriya");
        client.insertRecord(tableKonsentrasiKeahlian, "KK114", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK115", "main", "id", "KK115");
        client.insertRecord(tableKonsentrasiKeahlian, "KK115", "main", "konsentrasi", "Kriya Kreatif Keramik");
        client.insertRecord(tableKonsentrasiKeahlian, "KK115", "programKeahlian", "id", "PK046");
        client.insertRecord(tableKonsentrasiKeahlian, "KK115", "programKeahlian", "program",
                "Desain dan Produksi Kriya");
        client.insertRecord(tableKonsentrasiKeahlian, "KK115", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK116", "main", "id", "KK116");
        client.insertRecord(tableKonsentrasiKeahlian, "KK116", "main", "konsentrasi",
                "Kriya Kreatif Logam dan Perhiasan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK116", "programKeahlian", "id", "PK046");
        client.insertRecord(tableKonsentrasiKeahlian, "KK116", "programKeahlian", "program",
                "Desain dan Produksi Kriya");
        client.insertRecord(tableKonsentrasiKeahlian, "KK116", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK117", "main", "id", "KK117");
        client.insertRecord(tableKonsentrasiKeahlian, "KK117", "main", "konsentrasi", "Kriya Kreatif Kayu dan Rotan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK117", "programKeahlian", "id", "PK046");
        client.insertRecord(tableKonsentrasiKeahlian, "KK117", "programKeahlian", "program",
                "Desain dan Produksi Kriya");
        client.insertRecord(tableKonsentrasiKeahlian, "KK117", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK118", "main", "id", "KK118");
        client.insertRecord(tableKonsentrasiKeahlian, "KK118", "main", "konsentrasi", "Seni Musik");
        client.insertRecord(tableKonsentrasiKeahlian, "KK118", "programKeahlian", "id", "PK047");
        client.insertRecord(tableKonsentrasiKeahlian, "KK118", "programKeahlian", "program", "Seni Pertunjukan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK118", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK119", "main", "id", "KK119");
        client.insertRecord(tableKonsentrasiKeahlian, "KK119", "main", "konsentrasi", "Seni Tari");
        client.insertRecord(tableKonsentrasiKeahlian, "KK119", "programKeahlian", "id", "PK047");
        client.insertRecord(tableKonsentrasiKeahlian, "KK119", "programKeahlian", "program", "Seni Pertunjukan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK119", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK120", "main", "id", "KK120");
        client.insertRecord(tableKonsentrasiKeahlian, "KK120", "main", "konsentrasi", "Seni Karawitan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK120", "programKeahlian", "id", "PK047");
        client.insertRecord(tableKonsentrasiKeahlian, "KK120", "programKeahlian", "program", "Seni Pertunjukan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK120", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK121", "main", "id", "KK121");
        client.insertRecord(tableKonsentrasiKeahlian, "KK121", "main", "konsentrasi", "Seni Pedalangan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK121", "programKeahlian", "id", "PK047");
        client.insertRecord(tableKonsentrasiKeahlian, "KK121", "programKeahlian", "program", "Seni Pertunjukan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK121", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK122", "main", "id", "KK122");
        client.insertRecord(tableKonsentrasiKeahlian, "KK122", "main", "konsentrasi", "Seni Teater");
        client.insertRecord(tableKonsentrasiKeahlian, "KK122", "programKeahlian", "id", "PK047");
        client.insertRecord(tableKonsentrasiKeahlian, "KK122", "programKeahlian", "program", "Seni Pertunjukan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK122", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK123", "main", "id", "KK123");
        client.insertRecord(tableKonsentrasiKeahlian, "KK123", "main", "konsentrasi", "Tata Artistik Teater");
        client.insertRecord(tableKonsentrasiKeahlian, "KK123", "programKeahlian", "id", "PK047");
        client.insertRecord(tableKonsentrasiKeahlian, "KK123", "programKeahlian", "program", "Seni Pertunjukan");
        client.insertRecord(tableKonsentrasiKeahlian, "KK123", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK124", "main", "id", "KK124");
        client.insertRecord(tableKonsentrasiKeahlian, "KK124", "main", "konsentrasi",
                "Produksi dan Siaran Program Radio");
        client.insertRecord(tableKonsentrasiKeahlian, "KK124", "programKeahlian", "id", "PK048");
        client.insertRecord(tableKonsentrasiKeahlian, "KK124", "programKeahlian", "program",
                "Broadcasting dan Perfilman");
        client.insertRecord(tableKonsentrasiKeahlian, "KK124", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK125", "main", "id", "KK125");
        client.insertRecord(tableKonsentrasiKeahlian, "KK125", "main", "konsentrasi",
                "Produksi dan Siaran Program Televisi");
        client.insertRecord(tableKonsentrasiKeahlian, "KK125", "programKeahlian", "id", "PK048");
        client.insertRecord(tableKonsentrasiKeahlian, "KK125", "programKeahlian", "program",
                "Broadcasting dan Perfilman");
        client.insertRecord(tableKonsentrasiKeahlian, "KK125", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK126", "main", "id", "KK126");
        client.insertRecord(tableKonsentrasiKeahlian, "KK126", "main", "konsentrasi", "Produksi Film");
        client.insertRecord(tableKonsentrasiKeahlian, "KK126", "programKeahlian", "id", "PK048");
        client.insertRecord(tableKonsentrasiKeahlian, "KK126", "programKeahlian", "program",
                "Broadcasting dan Perfilman");
        client.insertRecord(tableKonsentrasiKeahlian, "KK126", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK127", "main", "id", "KK127");
        client.insertRecord(tableKonsentrasiKeahlian, "KK127", "main", "konsentrasi", "Animasi");
        client.insertRecord(tableKonsentrasiKeahlian, "KK127", "programKeahlian", "id", "PK049");
        client.insertRecord(tableKonsentrasiKeahlian, "KK127", "programKeahlian", "program", "Animasi");
        client.insertRecord(tableKonsentrasiKeahlian, "KK127", "detail", "created_by", "Doyatama");

        client.insertRecord(tableKonsentrasiKeahlian, "KK128", "main", "id", "KK128");
        client.insertRecord(tableKonsentrasiKeahlian, "KK128", "main", "konsentrasi", "Desain dan Produksi Busana");
        client.insertRecord(tableKonsentrasiKeahlian, "KK128", "programKeahlian", "id", "PK050");
        client.insertRecord(tableKonsentrasiKeahlian, "KK128", "programKeahlian", "program", "Busana");
        client.insertRecord(tableKonsentrasiKeahlian, "KK128", "detail", "created_by", "Doyatama");

    }

}
