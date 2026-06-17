package create_structure.MasterDataSeeder.DPB;

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
                // ELE = Elemen, DKV = Desain dan Produksi Busana, BI = Bahasa Indonesia,
                // KLS =
                // Kelas, F = Fase / XI, GL = Ganjil
                // Tiga angka terakhir adalah urutan elemen dalam fase tersebut

                // Elemen 1: Menyimak
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL001", "main", "idElemen", "ELEDPBBIKLSFGL001");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL001", "main", "namaElemen", "Menyimak");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL001", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL001", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL001", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL001", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL001", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL001", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL001", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL001", "kelas", "namaKelas", "XI");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL001", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL001", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL001", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS003");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL001", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain dan Produksi Busana");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL001", "detail", "createdAt", instant.toString());

                // Elemen 2: Membaca dan Memirsa
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL002", "main", "idElemen", "ELEDPBBIKLSFGL002");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL002", "main", "namaElemen", "Membaca dan Memirsa");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL002", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL002", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL002", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL002", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL002", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL002", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL002", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL002", "kelas", "namaKelas", "XI");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL002", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL002", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL002", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS003");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL002", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain dan Produksi Busana");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL002", "detail", "createdAt", instant.toString());

                // Elemen 3: Berbicara dan Mempresentasikan
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL003", "main", "idElemen", "ELEDPBBIKLSFGL003");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL003", "main", "namaElemen",
                                "Berbicara dan Mempresentasikan");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL003", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL003", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL003", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL003", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL003", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL003", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL003", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL003", "kelas", "namaKelas", "XI");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL003", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL003", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL003", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS003");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL003", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain dan Produksi Busana");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL003", "detail", "createdAt", instant.toString());

                // Elemen 4: Menulis
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL004", "main", "idElemen", "ELEDPBBIKLSFGL004");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL004", "main", "namaElemen", "Menulis");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL004", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL004", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL004", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL004", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL004", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL004", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL004", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL004", "kelas", "namaKelas", "XI");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL004", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL004", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL004", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS003");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL004", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain dan Produksi Busana");
                client.insertRecord(tableElemen, "ELEDPBBIKLSFGL004", "detail", "createdAt", instant.toString());

                // ======================================================================================================================

                // Insert Data Table Elemen untuk Informatika Kelas X Semester Ganjil Mapel

                // Untuk Kelas X Semester Ganjil maka ada kode khusus untuk elemen
                // ELE = Elemen, BI = Bahasa Indonesia, KLS = Kelas, E = Fase / X, GL = Ganjil
                // Tiga angka terakhir adalah urutan elemen dalam fase tersebut

                // Informatika ELEDPBINKLSEGL001

                client.insertRecord(tableElemen, "ELEDPBINKLSEGL001", "main", "idElemen", "ELEDPBINKLSEGL001");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL001", "main", "namaElemen",
                                "Berpikir Komputasional (BK)");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL001", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL001", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL001", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL001", "mapel", "name", "Informatika");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL001", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL001", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL001", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL001", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL001", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL001", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL001", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS003");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL001", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain dan Produksi Busana");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL001", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDPBINKLSEGL002", "main", "idElemen", "ELEDPBINKLSEGL002");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL002", "main", "namaElemen",
                                "Teknologi Informasi dan Komunikasi (TIK)");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL002", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL002", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL002", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL002", "mapel", "name", "Informatika");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL002", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL002", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL002", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL002", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL002", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL002", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL002", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS003");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL002", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain dan Produksi Busana");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL002", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDPBINKLSEGL003", "main", "idElemen", "ELEDPBINKLSEGL003");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL003", "main", "namaElemen", "Sistem Komputasi (SK)");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL003", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL003", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL003", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL003", "mapel", "name", "Informatika");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL003", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL003", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL003", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL003", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL003", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL003", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL003", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS003");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL003", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain dan Produksi Busana");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL003", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDPBINKLSEGL004", "main", "idElemen", "ELEDPBINKLSEGL004");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL004", "main", "namaElemen",
                                "Jaringan Komputer dan Internet (JKI)");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL004", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL004", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL004", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL004", "mapel", "name", "Informatika");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL004", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL004", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL004", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL004", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL004", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL004", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL004", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS003");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL004", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain dan Produksi Busana");
                client.insertRecord(tableElemen, "ELEDPBINKLSEGL004", "detail", "createdAt", instant.toString());

                // =======================================================================================================================

                // Insert Data Table Elemen untuk Desain dan Produksi Busana Kelas X
                // Semester

                // Ganjil Mapel Matematika

                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL005", "main", "idElemen", "ELEDPBMMKLSEGL005");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL005", "main", "namaElemen", "Bilangan");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL005", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL005", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL005", "mapel", "idMapel", "MAP005");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL005", "mapel", "name", "Matematika");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL005", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL005", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL005", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL005", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL005", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL005", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL005", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS003");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL005", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain dan Produksi Busana");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL005", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL006", "main", "idElemen", "ELEDPBMMKLSEGL006");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL006", "main", "namaElemen", "Aljabar dan Fungsi");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL006", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL006", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL006", "mapel", "idMapel", "MAP005");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL006", "mapel", "name", "Matematika");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL006", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL006", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL006", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL006", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL006", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL006", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL006", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS003");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL006", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain dan Produksi Busana");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL006", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL007", "main", "idElemen", "ELEDPBMMKLSEGL007");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL007", "main", "namaElemen", "Geometri");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL007", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL007", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL007", "mapel", "idMapel", "MAP005");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL007", "mapel", "name", "Matematika");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL007", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL007", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL007", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL007", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL007", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL007", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL007", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS003");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL007", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain dan Produksi Busana");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL007", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL008", "main", "idElemen", "ELEDPBMMKLSEGL008");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL008", "main", "namaElemen",
                                "Analisis Data dan Peluang");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL008", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL008", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL008", "mapel", "idMapel", "MAP005");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL008", "mapel", "name", "Matematika");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL008", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL008", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL008", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL008", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL008", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL008", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL008", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS003");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL008", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain dan Produksi Busana");
                client.insertRecord(tableElemen, "ELEDPBMMKLSEGL008", "detail", "createdAt", instant.toString());

                // =======================================================================================================================

                // Insert Data Table Elemen untuk Desain dan Produksi Busana Kelas X
                // Semester
                // Ganjil Mapel Ilmu Pengetahuan Alam dan Sosial

                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL009", "main", "idElemen", "ELEDPBIPKLSEGL009");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL009", "main", "namaElemen",
                                "Menjelaskan Fenomena Secara Ilmiah");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL009", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL009", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL009", "mapel", "idMapel", "MAP011");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL009", "mapel", "name",
                                "Projek Ilmu Pengetahuan Alam dan Sosial");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL009", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL009", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL009", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL009", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL009", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL009", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL009", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS003");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL009", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain dan Produksi Busana");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL009", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL010", "main", "idElemen", "ELEDPBIPKLSEGL010");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL010", "main", "namaElemen",
                                "Mendesain dan mengevaluasi penyelidikan ilmiah");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL010", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL010", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL010", "mapel", "idMapel", "MAP011");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL010", "mapel", "name",
                                "Projek Ilmu Pengetahuan Alam dan Sosial");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL010", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL010", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL010", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL010", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL010", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL010", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL010", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS003");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL010", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain dan Produksi Busana");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL010", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL011", "main", "idElemen", "ELEDPBIPKLSEGL011");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL011", "main", "namaElemen",
                                "Menerjemahkan data dan bukti-bukti secara ilmiah");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL011", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL011", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL011", "mapel", "idMapel", "MAP011");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL011", "mapel", "name",
                                "Projek Ilmu Pengetahuan Alam dan Sosial");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL011", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL011", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL011", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL011", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL011", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL011", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL011", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS003");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL011", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain dan Produksi Busana");
                client.insertRecord(tableElemen, "ELEDPBIPKLSEGL011", "detail", "createdAt", instant.toString());

                // =======================================================================================================================

                // Insert Data Table Elemen untuk Pendidikan Pancasila Kelas X Semester Ganjil
                // untuk Desain dan Produksi Busana

                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL012", "main", "idElemen", "ELEDPBPPKLSEGL012");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL012", "main", "namaElemen",
                                "Pancasila");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL012", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL012", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL012", "mapel", "idMapel", "MAP009");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL012", "mapel", "name",
                                "Pendidikan Pancasila");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL012", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL012", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL012", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL012", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL012", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL012", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL012", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS003");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL012", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain dan Produksi Busana");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL012", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL013", "main", "idElemen", "ELEDPBPPKLSEGL013");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL013", "main", "namaElemen",
                                "Undang Undang Dasar Negara Republik Indonesia Tahun 1945");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL013", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL013", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL013", "mapel", "idMapel", "MAP009");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL013", "mapel", "name",
                                "Pendidikan Pancasila");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL013", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL013", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL013", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL013", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL013", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL013", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL013", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS003");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL013", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain dan Produksi Busana");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL013", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL014", "main", "idElemen", "ELEDPBPPKLSEGL014");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL014", "main", "namaElemen",
                                "Bhinneka Tunggal Ika");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL014", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL014", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL014", "mapel", "idMapel", "MAP009");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL014", "mapel", "name",
                                "Pendidikan Pancasila");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL014", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL014", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL014", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL014", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL014", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL014", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL014", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS003");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL014", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain dan Produksi Busana");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL014", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL015", "main", "idElemen", "ELEDPBPPKLSEGL015");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL015", "main", "namaElemen",
                                "Negara Kesatuan Republik Indonesia");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL015", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL015", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL015", "mapel", "idMapel", "MAP009");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL015", "mapel", "name",
                                "Pendidikan Pancasila");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL015", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL015", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL015", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL015", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL015", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL015", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL015", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS003");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL015", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain dan Produksi Busana");
                client.insertRecord(tableElemen, "ELEDPBPPKLSEGL015", "detail", "createdAt", instant.toString());

        }

}
