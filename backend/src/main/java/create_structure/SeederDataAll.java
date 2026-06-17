package create_structure;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SeederDataAll {

        public static void main(String[] args) throws IOException {
                Configuration conf = HBaseConfiguration.create();
                HBaseCustomClient client = new HBaseCustomClient(conf);

                // Waktu sekarang
                ZoneId zoneId = ZoneId.of("Asia/Jakarta");
                ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
                Instant instant = zonedDateTime.toInstant();

                // Tabel Konsentrasi Keahlian Sekolah
                TableName tableKonsentrasiKeahlianSekolah = TableName.valueOf("konsentrasiKeahlianSekolah");
                // Insert Data Table Konsentrasi Keahlian Sekolah
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS001", "main", "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS001", "main", "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS001", "school", "idSchool", "RWK001");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS001", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS001", "konsentrasiKeahlian", "id",
                                "KK111");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS001", "konsentrasiKeahlian", "konsentrasi",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS001", "detail", "createdAt",
                                instant.toString());

                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS002", "main", "idKonsentrasiSekolah",
                                "KKS002");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS002", "main", "namaKonsentrasiSekolah",
                                "Agribisnis Perikanan Air Tawar");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS002", "school", "idSchool", "RWK001");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS002", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS002", "konsentrasiKeahlian", "id",
                                "KK084");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS002", "konsentrasiKeahlian", "konsentrasi",
                                "Agribisnis Perikanan Air Tawar");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS002", "detail", "createdAt",
                                instant.toString());

                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS003", "main", "idKonsentrasiSekolah",
                                "KKS003");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS003", "main", "namaKonsentrasiSekolah",
                                "Desain dan Produksi Busana");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS003", "school", "idSchool", "RWK001");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS003", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS003", "konsentrasiKeahlian", "id",
                                "KK128");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS003", "konsentrasiKeahlian", "konsentrasi",
                                "Desain dan Produksi Busana");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS003", "detail", "createdAt",
                                instant.toString());

                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS004", "main", "idKonsentrasiSekolah",
                                "KKS004");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS004", "main", "namaKonsentrasiSekolah",
                                "Teknik Sepeda Motor");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS004", "school", "idSchool", "RWK001");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS004", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS004", "konsentrasiKeahlian", "id",
                                "KK017");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS004", "konsentrasiKeahlian", "konsentrasi",
                                "Teknik Sepeda Motor");
                client.insertRecord(tableKonsentrasiKeahlianSekolah, "KKS004", "detail", "createdAt",
                                instant.toString());

                // Tabel Semester
                TableName tableSemester = TableName.valueOf("semester");
                // Insert Data Table Semester
                client.insertRecord(tableSemester, "SM001", "main", "idSemester", "SM001");
                client.insertRecord(tableSemester, "SM001", "main", "namaSemester", "Ganjil");
                client.insertRecord(tableSemester, "SM001", "school", "idSchool", "RWK001");
                client.insertRecord(tableSemester, "SM001", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableSemester, "SM001", "detail", "createdAt", instant.toString());

                client.insertRecord(tableSemester, "SM002", "main", "idSemester", "SM002");
                client.insertRecord(tableSemester, "SM002", "main", "namaSemester", "Genap");
                client.insertRecord(tableSemester, "SM002", "school", "idSchool", "RWK001");
                client.insertRecord(tableSemester, "SM002", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableSemester, "SM002", "detail", "createdAt", instant.toString());

                // Tabel Tahun Ajaran
                TableName tableTahunAjaran = TableName.valueOf("tahunAjaran");
                // Insert Data Table Tahun Ajaran
                client.insertRecord(tableTahunAjaran, "TA001", "main", "idTahun", "TA001");
                client.insertRecord(tableTahunAjaran, "TA001", "main", "tahunAjaran", "2025/2026");
                client.insertRecord(tableTahunAjaran, "TA001", "school", "idSchool", "RWK001");
                client.insertRecord(tableTahunAjaran, "TA001", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableTahunAjaran, "TA001", "detail", "createdAt", instant.toString());

                client.insertRecord(tableTahunAjaran, "TA002", "main", "idTahun", "TA002");
                client.insertRecord(tableTahunAjaran, "TA002", "main", "tahunAjaran", "2024/2025");
                client.insertRecord(tableTahunAjaran, "TA002", "school", "idSchool", "RWK001");
                client.insertRecord(tableTahunAjaran, "TA002", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableTahunAjaran, "TA002", "detail", "createdAt", instant.toString());

                // Tabel Kelas
                TableName tableKelas = TableName.valueOf("kelas");
                // Insert Data Table Kelas
                client.insertRecord(tableKelas, "KLS001", "main", "idKelas", "KLS001");
                client.insertRecord(tableKelas, "KLS001", "main", "namaKelas", "X");
                client.insertRecord(tableKelas, "KLS001", "school", "idSchool", "RWK001");
                client.insertRecord(tableKelas, "KLS001", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableKelas, "KLS001", "detail", "createdAt", instant.toString());

                client.insertRecord(tableKelas, "KLS002", "main", "idKelas", "KLS002");
                client.insertRecord(tableKelas, "KLS002", "main", "namaKelas", "XI");
                client.insertRecord(tableKelas, "KLS002", "school", "idSchool", "RWK001");
                client.insertRecord(tableKelas, "KLS002", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableKelas, "KLS002", "detail", "createdAt", instant.toString());

                client.insertRecord(tableKelas, "KLS003", "main", "idKelas", "KLS003");
                client.insertRecord(tableKelas, "KLS003", "main", "namaKelas", "XII");
                client.insertRecord(tableKelas, "KLS003", "school", "idSchool", "RWK001");
                client.insertRecord(tableKelas, "KLS003", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableKelas, "KLS003", "detail", "createdAt", instant.toString());

        }

}
