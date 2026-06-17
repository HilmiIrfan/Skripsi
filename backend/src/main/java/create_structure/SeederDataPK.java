package create_structure;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SeederDataPK {

    public static void main(String[] args) throws IOException {

        Configuration conf = HBaseConfiguration.create();
        HBaseCustomClient client = new HBaseCustomClient(conf);

        // Waktu sekarang
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        // Table Program Keahlian
        TableName tableProgramKeahlian = TableName.valueOf("programKeahlians");

        // BK001
        client.insertRecord(tableProgramKeahlian, "PK001", "main", "id", "PK001");
        client.insertRecord(tableProgramKeahlian, "PK001", "main", "program", "Teknik Perawatan Gedung");
        client.insertRecord(tableProgramKeahlian, "PK001", "bidangKeahlian", "id", "BK001");
        client.insertRecord(tableProgramKeahlian, "PK001", "bidangKeahlian", "bidang",
                "Teknologi Konstruksi dan Bangunan");
        client.insertRecord(tableProgramKeahlian, "PK001", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK002", "main", "id", "PK002");
        client.insertRecord(tableProgramKeahlian, "PK002", "main", "program",
                "Konstruksi dan Perawatan Bangunan Sipil");
        client.insertRecord(tableProgramKeahlian, "PK002", "bidangKeahlian", "id", "BK001");
        client.insertRecord(tableProgramKeahlian, "PK002", "bidangKeahlian", "bidang",
                "Teknologi Konstruksi dan Bangunan");
        client.insertRecord(tableProgramKeahlian, "PK002", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK003", "main", "id", "PK003");
        client.insertRecord(tableProgramKeahlian, "PK003", "main", "program", "Teknik Konstruksi dan Perumahan");
        client.insertRecord(tableProgramKeahlian, "PK003", "bidangKeahlian", "id", "BK001");
        client.insertRecord(tableProgramKeahlian, "PK003", "bidangKeahlian", "bidang",
                "Teknologi Konstruksi dan Bangunan");
        client.insertRecord(tableProgramKeahlian, "PK003", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK004", "main", "id", "PK004");
        client.insertRecord(tableProgramKeahlian, "PK004", "main", "program",
                "Desain Pemodelan dan Informasi Bangunan");
        client.insertRecord(tableProgramKeahlian, "PK004", "bidangKeahlian", "id", "BK001");
        client.insertRecord(tableProgramKeahlian, "PK004", "bidangKeahlian", "bidang",
                "Teknologi Konstruksi dan Bangunan");
        client.insertRecord(tableProgramKeahlian, "PK004", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK005", "main", "id", "PK005");
        client.insertRecord(tableProgramKeahlian, "PK005", "main", "program", "Teknik Furnitur");
        client.insertRecord(tableProgramKeahlian, "PK005", "bidangKeahlian", "id", "BK001");
        client.insertRecord(tableProgramKeahlian, "PK005", "bidangKeahlian", "bidang",
                "Teknologi Konstruksi dan Bangunan");
        client.insertRecord(tableProgramKeahlian, "PK005", "detail", "created_by", "Doyatama");

        // BK002
        client.insertRecord(tableProgramKeahlian, "PK006", "main", "id", "PK006");
        client.insertRecord(tableProgramKeahlian, "PK006", "main", "program", "Teknik Mesin");
        client.insertRecord(tableProgramKeahlian, "PK006", "bidangKeahlian", "id", "BK002");
        client.insertRecord(tableProgramKeahlian, "PK006", "bidangKeahlian", "bidang",
                "Teknologi Manufaktur dan Rekayasa");
        client.insertRecord(tableProgramKeahlian, "PK006", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK007", "main", "id", "PK007");
        client.insertRecord(tableProgramKeahlian, "PK007", "main", "program", "Teknik Otomotif");
        client.insertRecord(tableProgramKeahlian, "PK007", "bidangKeahlian", "id", "BK002");
        client.insertRecord(tableProgramKeahlian, "PK007", "bidangKeahlian", "bidang",
                "Teknologi Manufaktur dan Rekayasa");
        client.insertRecord(tableProgramKeahlian, "PK007", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK008", "main", "id", "PK008");
        client.insertRecord(tableProgramKeahlian, "PK008", "main", "program", "Teknik Pengelasan dan Fabrikasi Logam");
        client.insertRecord(tableProgramKeahlian, "PK008", "bidangKeahlian", "id", "BK002");
        client.insertRecord(tableProgramKeahlian, "PK008", "bidangKeahlian", "bidang",
                "Teknologi Manufaktur dan Rekayasa");
        client.insertRecord(tableProgramKeahlian, "PK008", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK009", "main", "id", "PK009");
        client.insertRecord(tableProgramKeahlian, "PK009", "main", "program", "Teknik Logistik");
        client.insertRecord(tableProgramKeahlian, "PK009", "bidangKeahlian", "id", "BK002");
        client.insertRecord(tableProgramKeahlian, "PK009", "bidangKeahlian", "bidang",
                "Teknologi Manufaktur dan Rekayasa");
        client.insertRecord(tableProgramKeahlian, "PK009", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK010", "main", "id", "PK010");
        client.insertRecord(tableProgramKeahlian, "PK010", "main", "program", "Teknik Elektronika");
        client.insertRecord(tableProgramKeahlian, "PK010", "bidangKeahlian", "id", "BK002");
        client.insertRecord(tableProgramKeahlian, "PK010", "bidangKeahlian", "bidang",
                "Teknologi Manufaktur dan Rekayasa");
        client.insertRecord(tableProgramKeahlian, "PK010", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK011", "main", "id", "PK011");
        client.insertRecord(tableProgramKeahlian, "PK011", "main", "program", "Teknik Pesawat Udara");
        client.insertRecord(tableProgramKeahlian, "PK011", "bidangKeahlian", "id", "BK002");
        client.insertRecord(tableProgramKeahlian, "PK011", "bidangKeahlian", "bidang",
                "Teknologi Manufaktur dan Rekayasa");
        client.insertRecord(tableProgramKeahlian, "PK011", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK012", "main", "id", "PK012");
        client.insertRecord(tableProgramKeahlian, "PK012", "main", "program", "Teknik Konstruksi Kapal");
        client.insertRecord(tableProgramKeahlian, "PK012", "bidangKeahlian", "id", "BK002");
        client.insertRecord(tableProgramKeahlian, "PK012", "bidangKeahlian", "bidang",
                "Teknologi Manufaktur dan Rekayasa");
        client.insertRecord(tableProgramKeahlian, "PK012", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK013", "main", "id", "PK013");
        client.insertRecord(tableProgramKeahlian, "PK013", "main", "program", "Kimia Analisis");
        client.insertRecord(tableProgramKeahlian, "PK013", "bidangKeahlian", "id", "BK002");
        client.insertRecord(tableProgramKeahlian, "PK013", "bidangKeahlian", "bidang",
                "Teknologi Manufaktur dan Rekayasa");
        client.insertRecord(tableProgramKeahlian, "PK013", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK014", "main", "id", "PK014");
        client.insertRecord(tableProgramKeahlian, "PK014", "main", "program", "Teknik Kimia Industri");
        client.insertRecord(tableProgramKeahlian, "PK014", "bidangKeahlian", "id", "BK002");
        client.insertRecord(tableProgramKeahlian, "PK014", "bidangKeahlian", "bidang",
                "Teknologi Manufaktur dan Rekayasa");
        client.insertRecord(tableProgramKeahlian, "PK014", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK015", "main", "id", "PK015");
        client.insertRecord(tableProgramKeahlian, "PK015", "main", "program", "Teknik Tekstil");
        client.insertRecord(tableProgramKeahlian, "PK015", "bidangKeahlian", "id", "BK002");
        client.insertRecord(tableProgramKeahlian, "PK015", "bidangKeahlian", "bidang",
                "Teknologi Manufaktur dan Rekayasa");
        client.insertRecord(tableProgramKeahlian, "PK015", "detail", "created_by", "Doyatama");

        // BK003
        client.insertRecord(tableProgramKeahlian, "PK016", "main", "id", "PK016");
        client.insertRecord(tableProgramKeahlian, "PK016", "main", "program", "Teknik Ketenagalistrikan");
        client.insertRecord(tableProgramKeahlian, "PK016", "bidangKeahlian", "id", "BK003");
        client.insertRecord(tableProgramKeahlian, "PK016", "bidangKeahlian", "bidang", "Energi dan Pertambangan");
        client.insertRecord(tableProgramKeahlian, "PK016", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK017", "main", "id", "PK017");
        client.insertRecord(tableProgramKeahlian, "PK017", "main", "program", "Teknik Energi Terbarukan");
        client.insertRecord(tableProgramKeahlian, "PK017", "bidangKeahlian", "id", "BK003");
        client.insertRecord(tableProgramKeahlian, "PK017", "bidangKeahlian", "bidang", "Energi dan Pertambangan");
        client.insertRecord(tableProgramKeahlian, "PK017", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK018", "main", "id", "PK018");
        client.insertRecord(tableProgramKeahlian, "PK018", "main", "program", "Teknik Geospasial");
        client.insertRecord(tableProgramKeahlian, "PK018", "bidangKeahlian", "id", "BK003");
        client.insertRecord(tableProgramKeahlian, "PK018", "bidangKeahlian", "bidang", "Energi dan Pertambangan");
        client.insertRecord(tableProgramKeahlian, "PK018", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK019", "main", "id", "PK019");
        client.insertRecord(tableProgramKeahlian, "PK019", "main", "program", "Teknik Geologi Pertambangan");
        client.insertRecord(tableProgramKeahlian, "PK019", "bidangKeahlian", "id", "BK003");
        client.insertRecord(tableProgramKeahlian, "PK019", "bidangKeahlian", "bidang", "Energi dan Pertambangan");
        client.insertRecord(tableProgramKeahlian, "PK019", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK020", "main", "id", "PK020");
        client.insertRecord(tableProgramKeahlian, "PK020", "main", "program", "Teknik Perminyakan");
        client.insertRecord(tableProgramKeahlian, "PK020", "bidangKeahlian", "id", "BK003");
        client.insertRecord(tableProgramKeahlian, "PK020", "bidangKeahlian", "bidang", "Energi dan Pertambangan");
        client.insertRecord(tableProgramKeahlian, "PK020", "detail", "created_by", "Doyatama");

        // BK004
        client.insertRecord(tableProgramKeahlian, "PK021", "main", "id", "PK021");
        client.insertRecord(tableProgramKeahlian, "PK021", "main", "program", "Pengembangan Perangkat Lunak dan Gim");
        client.insertRecord(tableProgramKeahlian, "PK021", "bidangKeahlian", "id", "BK004");
        client.insertRecord(tableProgramKeahlian, "PK021", "bidangKeahlian", "bidang", "Teknologi Informasi");
        client.insertRecord(tableProgramKeahlian, "PK021", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK022", "main", "id", "PK022");
        client.insertRecord(tableProgramKeahlian, "PK022", "main", "program",
                "Teknik Jaringan Komputer dan Telekomunikasi");
        client.insertRecord(tableProgramKeahlian, "PK022", "bidangKeahlian", "id", "BK004");
        client.insertRecord(tableProgramKeahlian, "PK022", "bidangKeahlian", "bidang", "Teknologi Informasi");
        client.insertRecord(tableProgramKeahlian, "PK022", "detail", "created_by", "Doyatama");

        // BK005
        client.insertRecord(tableProgramKeahlian, "PK023", "main", "id", "PK023");
        client.insertRecord(tableProgramKeahlian, "PK023", "main", "program", "Layanan Kesehatan");
        client.insertRecord(tableProgramKeahlian, "PK023", "bidangKeahlian", "id", "BK005");
        client.insertRecord(tableProgramKeahlian, "PK023", "bidangKeahlian", "bidang",
                "Kesehatan dan Pekerjaan Sosial");
        client.insertRecord(tableProgramKeahlian, "PK023", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK024", "main", "id", "PK024");
        client.insertRecord(tableProgramKeahlian, "PK024", "main", "program", "Teknik Laboratorium Medik");
        client.insertRecord(tableProgramKeahlian, "PK024", "bidangKeahlian", "id", "BK005");
        client.insertRecord(tableProgramKeahlian, "PK024", "bidangKeahlian", "bidang",
                "Kesehatan dan Pekerjaan Sosial");
        client.insertRecord(tableProgramKeahlian, "PK024", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK025", "main", "id", "PK025");
        client.insertRecord(tableProgramKeahlian, "PK025", "main", "program", "Teknologi Farmasi");
        client.insertRecord(tableProgramKeahlian, "PK025", "bidangKeahlian", "id", "BK005");
        client.insertRecord(tableProgramKeahlian, "PK025", "bidangKeahlian", "bidang",
                "Kesehatan dan Pekerjaan Sosial");
        client.insertRecord(tableProgramKeahlian, "PK025", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK026", "main", "id", "PK026");
        client.insertRecord(tableProgramKeahlian, "PK026", "main", "program", "Pekerjaan Sosial");
        client.insertRecord(tableProgramKeahlian, "PK026", "bidangKeahlian", "id", "BK005");
        client.insertRecord(tableProgramKeahlian, "PK026", "bidangKeahlian", "bidang",
                "Kesehatan dan Pekerjaan Sosial");
        client.insertRecord(tableProgramKeahlian, "PK026", "detail", "created_by", "Doyatama");

        // BK006
        client.insertRecord(tableProgramKeahlian, "PK027", "main", "id", "PK027");
        client.insertRecord(tableProgramKeahlian, "PK027", "main", "program", "Agribisnis Tanaman");
        client.insertRecord(tableProgramKeahlian, "PK027", "bidangKeahlian", "id", "BK006");
        client.insertRecord(tableProgramKeahlian, "PK027", "bidangKeahlian", "bidang", "Agribisnis dan Agritekno");
        client.insertRecord(tableProgramKeahlian, "PK027", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK028", "main", "id", "PK028");
        client.insertRecord(tableProgramKeahlian, "PK028", "main", "program", "Agribisnis Ternak");
        client.insertRecord(tableProgramKeahlian, "PK028", "bidangKeahlian", "id", "BK006");
        client.insertRecord(tableProgramKeahlian, "PK028", "bidangKeahlian", "bidang", "Agribisnis dan Agritekno");
        client.insertRecord(tableProgramKeahlian, "PK028", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK029", "main", "id", "PK029");
        client.insertRecord(tableProgramKeahlian, "PK029", "main", "program", "Agribisnis Perikanan");
        client.insertRecord(tableProgramKeahlian, "PK029", "bidangKeahlian", "id", "BK006");
        client.insertRecord(tableProgramKeahlian, "PK029", "bidangKeahlian", "bidang", "Agribisnis dan Agritekno");
        client.insertRecord(tableProgramKeahlian, "PK029", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK030", "main", "id", "PK030");
        client.insertRecord(tableProgramKeahlian, "PK030", "main", "program", "Usaha Pertanian Terpadu");
        client.insertRecord(tableProgramKeahlian, "PK030", "bidangKeahlian", "id", "BK006");
        client.insertRecord(tableProgramKeahlian, "PK030", "bidangKeahlian", "bidang", "Agribisnis dan Agritekno");
        client.insertRecord(tableProgramKeahlian, "PK030", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK031", "main", "id", "PK031");
        client.insertRecord(tableProgramKeahlian, "PK031", "main", "program", "Agritekno Pengolahan Hasil Pertanian");
        client.insertRecord(tableProgramKeahlian, "PK031", "bidangKeahlian", "id", "BK006");
        client.insertRecord(tableProgramKeahlian, "PK031", "bidangKeahlian", "bidang", "Agribisnis dan Agritekno");
        client.insertRecord(tableProgramKeahlian, "PK031", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK032", "main", "id", "PK032");
        client.insertRecord(tableProgramKeahlian, "PK032", "main", "program", "Kehutanan");
        client.insertRecord(tableProgramKeahlian, "PK032", "bidangKeahlian", "id", "BK006");
        client.insertRecord(tableProgramKeahlian, "PK032", "bidangKeahlian", "bidang", "Agribisnis dan Agritekno");
        client.insertRecord(tableProgramKeahlian, "PK032", "detail", "created_by", "Doyatama");

        // BK007
        client.insertRecord(tableProgramKeahlian, "PK033", "main", "id", "PK033");
        client.insertRecord(tableProgramKeahlian, "PK033", "main", "program", "Teknika Kapal Penangkap Ikan");
        client.insertRecord(tableProgramKeahlian, "PK033", "bidangKeahlian", "id", "BK007");
        client.insertRecord(tableProgramKeahlian, "PK033", "bidangKeahlian", "bidang", "Kemaritiman");
        client.insertRecord(tableProgramKeahlian, "PK033", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK034", "main", "id", "PK034");
        client.insertRecord(tableProgramKeahlian, "PK034", "main", "program", "Nautika Kapal Penangkap Ikan");
        client.insertRecord(tableProgramKeahlian, "PK034", "bidangKeahlian", "id", "BK007");
        client.insertRecord(tableProgramKeahlian, "PK034", "bidangKeahlian", "bidang", "Kemaritiman");
        client.insertRecord(tableProgramKeahlian, "PK034", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK035", "main", "id", "PK035");
        client.insertRecord(tableProgramKeahlian, "PK035", "main", "program", "Teknika Kapal Niaga");
        client.insertRecord(tableProgramKeahlian, "PK035", "bidangKeahlian", "id", "BK007");
        client.insertRecord(tableProgramKeahlian, "PK035", "bidangKeahlian", "bidang", "Kemaritiman");
        client.insertRecord(tableProgramKeahlian, "PK035", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK036", "main", "id", "PK036");
        client.insertRecord(tableProgramKeahlian, "PK036", "main", "program", "Nautika Kapal Niaga");
        client.insertRecord(tableProgramKeahlian, "PK036", "bidangKeahlian", "id", "BK007");
        client.insertRecord(tableProgramKeahlian, "PK036", "bidangKeahlian", "bidang", "Kemaritiman");
        client.insertRecord(tableProgramKeahlian, "PK036", "detail", "created_by", "Doyatama");

        // BK008
        client.insertRecord(tableProgramKeahlian, "PK037", "main", "id", "PK037");
        client.insertRecord(tableProgramKeahlian, "PK037", "main", "program", "Pemasaran");
        client.insertRecord(tableProgramKeahlian, "PK037", "bidangKeahlian", "id", "BK008");
        client.insertRecord(tableProgramKeahlian, "PK037", "bidangKeahlian", "bidang", "Bisnis dan Manajemen");
        client.insertRecord(tableProgramKeahlian, "PK037", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK038", "main", "id", "PK038");
        client.insertRecord(tableProgramKeahlian, "PK038", "main", "program",
                "Manajemen Perkantoran dan Layanan Bisnis");
        client.insertRecord(tableProgramKeahlian, "PK038", "bidangKeahlian", "id", "BK008");
        client.insertRecord(tableProgramKeahlian, "PK038", "bidangKeahlian", "bidang", "Bisnis dan Manajemen");
        client.insertRecord(tableProgramKeahlian, "PK038", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK039", "main", "id", "PK039");
        client.insertRecord(tableProgramKeahlian, "PK039", "main", "program", "Akuntansi dan Keuangan Lembaga");
        client.insertRecord(tableProgramKeahlian, "PK039", "bidangKeahlian", "id", "BK008");
        client.insertRecord(tableProgramKeahlian, "PK039", "bidangKeahlian", "bidang", "Bisnis dan Manajemen");
        client.insertRecord(tableProgramKeahlian, "PK039", "detail", "created_by", "Doyatama");

        // BK009
        client.insertRecord(tableProgramKeahlian, "PK040", "main", "id", "PK040");
        client.insertRecord(tableProgramKeahlian, "PK040", "main", "program", "Usaha Layanan Pariwisata");
        client.insertRecord(tableProgramKeahlian, "PK040", "bidangKeahlian", "id", "BK009");
        client.insertRecord(tableProgramKeahlian, "PK040", "bidangKeahlian", "bidang", "Pariwisata");
        client.insertRecord(tableProgramKeahlian, "PK040", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK041", "main", "id", "PK041");
        client.insertRecord(tableProgramKeahlian, "PK041", "main", "program", "Perhotelan");
        client.insertRecord(tableProgramKeahlian, "PK041", "bidangKeahlian", "id", "BK009");
        client.insertRecord(tableProgramKeahlian, "PK041", "bidangKeahlian", "bidang", "Pariwisata");
        client.insertRecord(tableProgramKeahlian, "PK041", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK042", "main", "id", "PK042");
        client.insertRecord(tableProgramKeahlian, "PK042", "main", "program", "Kuliner");
        client.insertRecord(tableProgramKeahlian, "PK042", "bidangKeahlian", "id", "BK009");
        client.insertRecord(tableProgramKeahlian, "PK042", "bidangKeahlian", "bidang", "Pariwisata");
        client.insertRecord(tableProgramKeahlian, "PK042", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK043", "main", "id", "PK043");
        client.insertRecord(tableProgramKeahlian, "PK043", "main", "program", "Kecantikan dan Spa");
        client.insertRecord(tableProgramKeahlian, "PK043", "bidangKeahlian", "id", "BK009");
        client.insertRecord(tableProgramKeahlian, "PK043", "bidangKeahlian", "bidang", "Pariwisata");
        client.insertRecord(tableProgramKeahlian, "PK043", "detail", "created_by", "Doyatama");

        // BK010
        client.insertRecord(tableProgramKeahlian, "PK044", "main", "id", "PK044");
        client.insertRecord(tableProgramKeahlian, "PK044", "main", "program", "Seni Rupa");
        client.insertRecord(tableProgramKeahlian, "PK044", "bidangKeahlian", "id", "BK010");
        client.insertRecord(tableProgramKeahlian, "PK044", "bidangKeahlian", "bidang", "Seni dan Ekonomi Kreatif");
        client.insertRecord(tableProgramKeahlian, "PK044", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK045", "main", "id", "PK045");
        client.insertRecord(tableProgramKeahlian, "PK045", "main", "program", "Desain Komunikasi Visual");
        client.insertRecord(tableProgramKeahlian, "PK045", "bidangKeahlian", "id", "BK010");
        client.insertRecord(tableProgramKeahlian, "PK045", "bidangKeahlian", "bidang", "Seni dan Ekonomi Kreatif");
        client.insertRecord(tableProgramKeahlian, "PK045", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK046", "main", "id", "PK046");
        client.insertRecord(tableProgramKeahlian, "PK046", "main", "program", "Desain dan Produksi Kriya");
        client.insertRecord(tableProgramKeahlian, "PK046", "bidangKeahlian", "id", "BK010");
        client.insertRecord(tableProgramKeahlian, "PK046", "bidangKeahlian", "bidang", "Seni dan Ekonomi Kreatif");
        client.insertRecord(tableProgramKeahlian, "PK046", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK047", "main", "id", "PK047");
        client.insertRecord(tableProgramKeahlian, "PK047", "main", "program", "Seni Pertunjukan");
        client.insertRecord(tableProgramKeahlian, "PK047", "bidangKeahlian", "id", "BK010");
        client.insertRecord(tableProgramKeahlian, "PK047", "bidangKeahlian", "bidang", "Seni dan Ekonomi Kreatif");
        client.insertRecord(tableProgramKeahlian, "PK047", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK048", "main", "id", "PK048");
        client.insertRecord(tableProgramKeahlian, "PK048", "main", "program", "Broadcasting dan Perfilman");
        client.insertRecord(tableProgramKeahlian, "PK048", "bidangKeahlian", "id", "BK010");
        client.insertRecord(tableProgramKeahlian, "PK048", "bidangKeahlian", "bidang", "Seni dan Ekonomi Kreatif");
        client.insertRecord(tableProgramKeahlian, "PK048", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK049", "main", "id", "PK049");
        client.insertRecord(tableProgramKeahlian, "PK049", "main", "program", "Animasi");
        client.insertRecord(tableProgramKeahlian, "PK049", "bidangKeahlian", "id", "BK010");
        client.insertRecord(tableProgramKeahlian, "PK049", "bidangKeahlian", "bidang", "Seni dan Ekonomi Kreatif");
        client.insertRecord(tableProgramKeahlian, "PK049", "detail", "created_by", "Doyatama");

        client.insertRecord(tableProgramKeahlian, "PK050", "main", "id", "PK050");
        client.insertRecord(tableProgramKeahlian, "PK050", "main", "program", "Busana");
        client.insertRecord(tableProgramKeahlian, "PK050", "bidangKeahlian", "id", "BK010");
        client.insertRecord(tableProgramKeahlian, "PK050", "bidangKeahlian", "bidang", "Seni dan Ekonomi Kreatif");
        client.insertRecord(tableProgramKeahlian, "PK050", "detail", "created_by", "Doyatama");

    }

}
