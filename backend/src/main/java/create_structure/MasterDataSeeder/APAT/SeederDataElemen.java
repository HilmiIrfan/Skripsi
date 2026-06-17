package create_structure.MasterDataSeeder.APAT;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import create_structure.HBaseCustomClient;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SeederDataElemen {

        public static void main(String[] args) throws IOException {

                Configuration conf = HBaseConfiguration.create();
                HBaseCustomClient client = new HBaseCustomClient(conf);

                // Waktu sekarang
                ZoneId zoneId = ZoneId.of("Asia/Jakarta");
                ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
                Instant instant = zonedDateTime.toInstant();

                // Tabel Elemen
                TableName tableElemen = TableName.valueOf("elemen");

                // Insert Data Table Elemen untuk Bahasa Indonesia Kelas XI Semester Ganjil
                // Mapel Bahasa Indonesia

                // Untuk Kelas XI Semester Ganjil maka ada kode khusus untuk elemen
                // ELE = Elemen, DKV = Agribisnis Perikanan Air Tawar, BI = Bahasa Indonesia,
                // KLS =
                // Kelas, F = Fase / XI, GL = Ganjil
                // Tiga angka terakhir adalah urutan elemen dalam fase tersebut

                // Elemen 1: Menyimak
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL001", "main", "idElemen", "ELEAPATBIKLSFGL001");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL001", "main", "namaElemen", "Menyimak");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL001", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL001", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL001", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL001", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL001", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL001", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL001", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL001", "kelas", "namaKelas", "XI");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL001", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL001", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL001", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS002");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL001", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Agribisnis Perikanan Air Tawar");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL001", "detail", "createdAt", instant.toString());

                // Elemen 2: Membaca dan Memirsa
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL002", "main", "idElemen", "ELEAPATBIKLSFGL002");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL002", "main", "namaElemen", "Membaca dan Memirsa");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL002", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL002", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL002", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL002", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL002", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL002", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL002", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL002", "kelas", "namaKelas", "XI");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL002", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL002", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL002", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS002");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL002", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Agribisnis Perikanan Air Tawar");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL002", "detail", "createdAt", instant.toString());

                // Elemen 3: Berbicara dan Mempresentasikan
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL003", "main", "idElemen", "ELEAPATBIKLSFGL003");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL003", "main", "namaElemen",
                                "Berbicara dan Mempresentasikan");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL003", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL003", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL003", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL003", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL003", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL003", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL003", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL003", "kelas", "namaKelas", "XI");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL003", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL003", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL003", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS002");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL003", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Agribisnis Perikanan Air Tawar");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL003", "detail", "createdAt", instant.toString());

                // Elemen 4: Menulis
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL004", "main", "idElemen", "ELEAPATBIKLSFGL004");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL004", "main", "namaElemen", "Menulis");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL004", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL004", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL004", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL004", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL004", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL004", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL004", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL004", "kelas", "namaKelas", "XI");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL004", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL004", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL004", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS002");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL004", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Agribisnis Perikanan Air Tawar");
                client.insertRecord(tableElemen, "ELEAPATBIKLSFGL004", "detail", "createdAt", instant.toString());

                // ======================================================================================================================

                // Insert Data Table Elemen untuk Informatika Kelas X Semester Ganjil Mapel

                // Untuk Kelas X Semester Ganjil maka ada kode khusus untuk elemen
                // ELE = Elemen, BI = Bahasa Indonesia, KLS = Kelas, E = Fase / X, GL = Ganjil
                // Tiga angka terakhir adalah urutan elemen dalam fase tersebut

                // Informatika ELEAPATINKLSEGL001

                client.insertRecord(tableElemen, "ELEAPATINKLSEGL001", "main", "idElemen", "ELEAPATINKLSEGL001");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL001", "main", "namaElemen",
                                "Berpikir Komputasional (BK)");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL001", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL001", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL001", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL001", "mapel", "name", "Informatika");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL001", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL001", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL001", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL001", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL001", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL001", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL001", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS002");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL001", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Agribisnis Perikanan Air Tawar");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL001", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEAPATINKLSEGL002", "main", "idElemen", "ELEAPATINKLSEGL002");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL002", "main", "namaElemen",
                                "Teknologi Informasi dan Komunikasi (TIK)");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL002", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL002", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL002", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL002", "mapel", "name", "Informatika");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL002", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL002", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL002", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL002", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL002", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL002", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL002", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS002");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL002", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Agribisnis Perikanan Air Tawar");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL002", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEAPATINKLSEGL003", "main", "idElemen", "ELEAPATINKLSEGL003");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL003", "main", "namaElemen", "Sistem Komputasi (SK)");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL003", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL003", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL003", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL003", "mapel", "name", "Informatika");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL003", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL003", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL003", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL003", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL003", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL003", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL003", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS002");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL003", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Agribisnis Perikanan Air Tawar");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL003", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEAPATINKLSEGL004", "main", "idElemen", "ELEAPATINKLSEGL004");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL004", "main", "namaElemen",
                                "Jaringan Komputer dan Internet (JKI)");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL004", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL004", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL004", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL004", "mapel", "name", "Informatika");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL004", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL004", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL004", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL004", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL004", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL004", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL004", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS002");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL004", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Agribisnis Perikanan Air Tawar");
                client.insertRecord(tableElemen, "ELEAPATINKLSEGL004", "detail", "createdAt", instant.toString());

                // =======================================================================================================================

                // Insert Data Table Elemen untuk Agribisnis Perikanan Air Tawar Kelas X
                // Semester

                // Ganjil Mapel Matematika

                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL005", "main", "idElemen", "ELEAPATMMKLSEGL005");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL005", "main", "namaElemen", "Bilangan");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL005", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL005", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL005", "mapel", "idMapel", "MAP005");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL005", "mapel", "name", "Matematika");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL005", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL005", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL005", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL005", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL005", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL005", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL005", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS002");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL005", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Agribisnis Perikanan Air Tawar");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL005", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL006", "main", "idElemen", "ELEAPATMMKLSEGL006");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL006", "main", "namaElemen", "Aljabar dan Fungsi");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL006", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL006", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL006", "mapel", "idMapel", "MAP005");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL006", "mapel", "name", "Matematika");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL006", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL006", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL006", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL006", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL006", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL006", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL006", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS002");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL006", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Agribisnis Perikanan Air Tawar");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL006", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL007", "main", "idElemen", "ELEAPATMMKLSEGL007");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL007", "main", "namaElemen", "Geometri");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL007", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL007", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL007", "mapel", "idMapel", "MAP005");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL007", "mapel", "name", "Matematika");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL007", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL007", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL007", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL007", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL007", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL007", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL007", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS002");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL007", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Agribisnis Perikanan Air Tawar");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL007", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL008", "main", "idElemen", "ELEAPATMMKLSEGL008");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL008", "main", "namaElemen",
                                "Analisis Data dan Peluang");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL008", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL008", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL008", "mapel", "idMapel", "MAP005");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL008", "mapel", "name", "Matematika");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL008", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL008", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL008", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL008", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL008", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL008", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL008", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS002");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL008", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Agribisnis Perikanan Air Tawar");
                client.insertRecord(tableElemen, "ELEAPATMMKLSEGL008", "detail", "createdAt", instant.toString());

                // =======================================================================================================================

                // Insert Data Table Elemen untuk Agribisnis Perikanan Air Tawar Kelas X
                // Semester
                // Ganjil Mapel Ilmu Pengetahuan Alam dan Sosial

                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL009", "main", "idElemen", "ELEAPATIPKLSEGL009");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL009", "main", "namaElemen",
                                "Menjelaskan Fenomena Secara Ilmiah");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL009", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL009", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL009", "mapel", "idMapel", "MAP011");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL009", "mapel", "name",
                                "Projek Ilmu Pengetahuan Alam dan Sosial");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL009", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL009", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL009", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL009", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL009", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL009", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL009", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS002");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL009", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Agribisnis Perikanan Air Tawar");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL009", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL010", "main", "idElemen", "ELEAPATIPKLSEGL010");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL010", "main", "namaElemen",
                                "Mendesain dan mengevaluasi penyelidikan ilmiah");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL010", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL010", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL010", "mapel", "idMapel", "MAP011");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL010", "mapel", "name",
                                "Projek Ilmu Pengetahuan Alam dan Sosial");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL010", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL010", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL010", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL010", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL010", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL010", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL010", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS002");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL010", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Agribisnis Perikanan Air Tawar");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL010", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL011", "main", "idElemen", "ELEAPATIPKLSEGL011");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL011", "main", "namaElemen",
                                "Menerjemahkan data dan bukti-bukti secara ilmiah");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL011", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL011", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL011", "mapel", "idMapel", "MAP011");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL011", "mapel", "name",
                                "Projek Ilmu Pengetahuan Alam dan Sosial");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL011", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL011", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL011", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL011", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL011", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL011", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL011", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS002");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL011", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Agribisnis Perikanan Air Tawar");
                client.insertRecord(tableElemen, "ELEAPATIPKLSEGL011", "detail", "createdAt", instant.toString());

                // =======================================================================================================================

                // Insert Data Table Elemen untuk Pendidikan Pancasila Kelas X Semester Ganjil
                // untuk Agribisnis Perikanan Air Tawar

                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL012", "main", "idElemen", "ELEAPATPPKLSEGL012");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL012", "main", "namaElemen",
                                "Pancasila");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL012", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL012", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL012", "mapel", "idMapel", "MAP009");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL012", "mapel", "name",
                                "Pendidikan Pancasila");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL012", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL012", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL012", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL012", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL012", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL012", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL012", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS002");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL012", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Agribisnis Perikanan Air Tawar");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL012", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL013", "main", "idElemen", "ELEAPATPPKLSEGL013");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL013", "main", "namaElemen",
                                "Undang Undang Dasar Negara Republik Indonesia Tahun 1945");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL013", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL013", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL013", "mapel", "idMapel", "MAP009");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL013", "mapel", "name",
                                "Pendidikan Pancasila");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL013", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL013", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL013", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL013", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL013", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL013", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL013", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS002");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL013", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Agribisnis Perikanan Air Tawar");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL013", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL014", "main", "idElemen", "ELEAPATPPKLSEGL014");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL014", "main", "namaElemen",
                                "Bhinneka Tunggal Ika");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL014", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL014", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL014", "mapel", "idMapel", "MAP009");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL014", "mapel", "name",
                                "Pendidikan Pancasila");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL014", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL014", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL014", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL014", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL014", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL014", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL014", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS002");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL014", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Agribisnis Perikanan Air Tawar");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL014", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL015", "main", "idElemen", "ELEAPATPPKLSEGL015");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL015", "main", "namaElemen",
                                "Negara Kesatuan Republik Indonesia");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL015", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL015", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL015", "mapel", "idMapel", "MAP009");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL015", "mapel", "name",
                                "Pendidikan Pancasila");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL015", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL015", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL015", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL015", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL015", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL015", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL015", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS002");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL015", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Agribisnis Perikanan Air Tawar");
                client.insertRecord(tableElemen, "ELEAPATPPKLSEGL015", "detail", "createdAt", instant.toString());

        }

}
