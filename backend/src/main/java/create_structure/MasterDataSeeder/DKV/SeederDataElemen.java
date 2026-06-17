package create_structure.MasterDataSeeder.DKV;

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
                // ELE = Elemen, DKV = Desain Komunikasi Visual, BI = Bahasa Indonesia, KLS =
                // Kelas, F = Fase / XI, GL = Ganjil
                // Tiga angka terakhir adalah urutan elemen dalam fase tersebut

                // Elemen 1: Menyimak
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL001", "main", "idElemen", "ELEDKVBIKLSFGL001");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL001", "main", "namaElemen", "Menyimak");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL001", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL001", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL001", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL001", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL001", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL001", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL001", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL001", "kelas", "namaKelas", "XI");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL001", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL001", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL001", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL001", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL001", "detail", "createdAt", instant.toString());

                // Elemen 2: Membaca dan Memirsa
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL002", "main", "idElemen", "ELEDKVBIKLSFGL002");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL002", "main", "namaElemen", "Membaca dan Memirsa");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL002", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL002", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL002", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL002", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL002", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL002", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL002", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL002", "kelas", "namaKelas", "XI");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL002", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL002", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL002", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL002", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL002", "detail", "createdAt", instant.toString());

                // Elemen 3: Berbicara dan Mempresentasikan
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL003", "main", "idElemen", "ELEDKVBIKLSFGL003");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL003", "main", "namaElemen",
                                "Berbicara dan Mempresentasikan");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL003", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL003", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL003", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL003", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL003", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL003", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL003", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL003", "kelas", "namaKelas", "XI");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL003", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL003", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL003", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL003", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL003", "detail", "createdAt", instant.toString());

                // Elemen 4: Menulis
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL004", "main", "idElemen", "ELEDKVBIKLSFGL004");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL004", "main", "namaElemen", "Menulis");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL004", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL004", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL004", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL004", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL004", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL004", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL004", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL004", "kelas", "namaKelas", "XI");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL004", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL004", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL004", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL004", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVBIKLSFGL004", "detail", "createdAt", instant.toString());

                // ======================================================================================================================

                // Insert Data Table Elemen untuk Informatika Kelas X Semester Ganjil Mapel

                // Untuk Kelas X Semester Ganjil maka ada kode khusus untuk elemen
                // ELE = Elemen, BI = Bahasa Indonesia, KLS = Kelas, E = Fase / X, GL = Ganjil
                // Tiga angka terakhir adalah urutan elemen dalam fase tersebut

                // Informatika ELEDKVINKLSEGL001

                client.insertRecord(tableElemen, "ELEDKVINKLSEGL001", "main", "idElemen", "ELEDKVINKLSEGL001");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL001", "main", "namaElemen",
                                "Berpikir Komputasional (BK)");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL001", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL001", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL001", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL001", "mapel", "name", "Informatika");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL001", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL001", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL001", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL001", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL001", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL001", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL001", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL001", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL001", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDKVINKLSEGL002", "main", "idElemen", "ELEDKVINKLSEGL002");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL002", "main", "namaElemen",
                                "Teknologi Informasi dan Komunikasi (TIK)");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL002", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL002", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL002", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL002", "mapel", "name", "Informatika");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL002", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL002", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL002", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL002", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL002", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL002", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL002", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL002", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL002", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDKVINKLSEGL003", "main", "idElemen", "ELEDKVINKLSEGL003");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL003", "main", "namaElemen", "Sistem Komputasi (SK)");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL003", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL003", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL003", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL003", "mapel", "name", "Informatika");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL003", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL003", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL003", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL003", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL003", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL003", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL003", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL003", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL003", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDKVINKLSEGL004", "main", "idElemen", "ELEDKVINKLSEGL004");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL004", "main", "namaElemen",
                                "Jaringan Komputer dan Internet (JKI)");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL004", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL004", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL004", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL004", "mapel", "name", "Informatika");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL004", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL004", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL004", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL004", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL004", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL004", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL004", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL004", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVINKLSEGL004", "detail", "createdAt", instant.toString());

                // =======================================================================================================================

                // Insert Data Table Elemen untuk Desain Komunikasi Visual Kelas X Semester

                // Ganjil Mapel Matematika

                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL005", "main", "idElemen", "ELEDKVMMKLSEGL005");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL005", "main", "namaElemen", "Bilangan");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL005", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL005", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL005", "mapel", "idMapel", "MAP005");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL005", "mapel", "name", "Matematika");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL005", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL005", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL005", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL005", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL005", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL005", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL005", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL005", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL005", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL006", "main", "idElemen", "ELEDKVMMKLSEGL006");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL006", "main", "namaElemen", "Aljabar dan Fungsi");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL006", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL006", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL006", "mapel", "idMapel", "MAP005");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL006", "mapel", "name", "Matematika");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL006", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL006", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL006", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL006", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL006", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL006", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL006", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL006", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL006", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL007", "main", "idElemen", "ELEDKVMMKLSEGL007");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL007", "main", "namaElemen", "Geometri");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL007", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL007", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL007", "mapel", "idMapel", "MAP005");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL007", "mapel", "name", "Matematika");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL007", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL007", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL007", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL007", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL007", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL007", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL007", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL007", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL007", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL008", "main", "idElemen", "ELEDKVMMKLSEGL008");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL008", "main", "namaElemen",
                                "Analisis Data dan Peluang");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL008", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL008", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL008", "mapel", "idMapel", "MAP005");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL008", "mapel", "name", "Matematika");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL008", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL008", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL008", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL008", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL008", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL008", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL008", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL008", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVMMKLSEGL008", "detail", "createdAt", instant.toString());

                // =======================================================================================================================

                // Insert Data Table Elemen untuk Desain Komunikasi Visual Kelas X Semester
                // Ganjil Mapel Ilmu Pengetahuan Alam dan Sosial

                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL009", "main", "idElemen", "ELEDKVIPKLSEGL009");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL009", "main", "namaElemen",
                                "Menjelaskan Fenomena Secara Ilmiah");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL009", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL009", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL009", "mapel", "idMapel", "MAP011");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL009", "mapel", "name",
                                "Projek Ilmu Pengetahuan Alam dan Sosial");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL009", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL009", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL009", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL009", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL009", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL009", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL009", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL009", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL009", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL010", "main", "idElemen", "ELEDKVIPKLSEGL010");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL010", "main", "namaElemen",
                                "Mendesain dan mengevaluasi penyelidikan ilmiah");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL010", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL010", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL010", "mapel", "idMapel", "MAP011");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL010", "mapel", "name",
                                "Projek Ilmu Pengetahuan Alam dan Sosial");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL010", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL010", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL010", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL010", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL010", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL010", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL010", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL010", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL010", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL011", "main", "idElemen", "ELEDKVIPKLSEGL011");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL011", "main", "namaElemen",
                                "Menerjemahkan data dan bukti-bukti secara ilmiah");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL011", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL011", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL011", "mapel", "idMapel", "MAP011");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL011", "mapel", "name",
                                "Projek Ilmu Pengetahuan Alam dan Sosial");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL011", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL011", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL011", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL011", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL011", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL011", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL011", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL011", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVIPKLSEGL011", "detail", "createdAt", instant.toString());

                // =======================================================================================================================

                // Insert Data Table Elemen untuk Pendidikan Pancasila Kelas X Semester Ganjil

                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL012", "main", "idElemen", "ELEDKVPPKLSEGL012");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL012", "main", "namaElemen",
                                "Pancasila");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL012", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL012", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL012", "mapel", "idMapel", "MAP009");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL012", "mapel", "name",
                                "Pendidikan Pancasila");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL012", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL012", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL012", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL012", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL012", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL012", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL012", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL012", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL012", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL013", "main", "idElemen", "ELEDKVPPKLSEGL013");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL013", "main", "namaElemen",
                                "Undang Undang Dasar Negara Republik Indonesia Tahun 1945");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL013", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL013", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL013", "mapel", "idMapel", "MAP009");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL013", "mapel", "name",
                                "Pendidikan Pancasila");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL013", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL013", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL013", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL013", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL013", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL013", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL013", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL013", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL013", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL014", "main", "idElemen", "ELEDKVPPKLSEGL014");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL014", "main", "namaElemen",
                                "Bhinneka Tunggal Ika");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL014", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL014", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL014", "mapel", "idMapel", "MAP009");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL014", "mapel", "name",
                                "Pendidikan Pancasila");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL014", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL014", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL014", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL014", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL014", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL014", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL014", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL014", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL014", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL015", "main", "idElemen", "ELEDKVPPKLSEGL015");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL015", "main", "namaElemen",
                                "Negara Kesatuan Republik Indonesia");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL015", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL015", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL015", "mapel", "idMapel", "MAP009");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL015", "mapel", "name",
                                "Pendidikan Pancasila");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL015", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL015", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL015", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL015", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL015", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL015", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL015", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL015", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVPPKLSEGL015", "detail", "createdAt", instant.toString());

                // =======================================================================================================================

                // Bagian untuk Mapel Konsentrasi Keahlian Sekolah DKV
                // Mapel Desain Komunikasi Visual
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL016", "main", "idElemen", "ELEDKVKKDKLSFGL016");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL016", "main", "namaElemen",
                                "Prinsip Dasar Desain dan Komunikasi");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL016", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL016", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL016", "mapel", "idMapel", "MAP085");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL016", "mapel", "name",
                                "Konsentrasi Keahlian Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL016", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL016", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL016", "kelas", "idKelas", "KLS003");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL016", "kelas", "namaKelas", "XII");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL016", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL016", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL016", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL016", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL016", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL017", "main", "idElemen", "ELEDKVKKDKLSFGL017");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL017", "main", "namaElemen",
                                "Perangkat Lunak Desain");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL017", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL017", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL017", "mapel", "idMapel", "MAP085");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL017", "mapel", "name",
                                "Konsentrasi Keahlian Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL017", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL017", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL017", "kelas", "idKelas", "KLS003");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL017", "kelas", "namaKelas", "XII");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL017", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL017", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL017", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL017", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL017", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL018", "main", "idElemen", "ELEDKVKKDKLSFGL018");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL018", "main", "namaElemen",
                                "PMenerapkan Design Brief ");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL018", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL018", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL018", "mapel", "idMapel", "MAP085");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL018", "mapel", "name",
                                "Konsentrasi Keahlian Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL018", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL018", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL018", "kelas", "idKelas", "KLS003");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL018", "kelas", "namaKelas", "XII");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL018", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL018", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL018", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL018", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL018", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL019", "main", "idElemen", "ELEDKVKKDKLSFGL019");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL019", "main", "namaElemen",
                                "Karya Desain ");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL019", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL019", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL019", "mapel", "idMapel", "MAP085");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL019", "mapel", "name",
                                "Konsentrasi Keahlian Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL019", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL019", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL019", "kelas", "idKelas", "KLS003");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL019", "kelas", "namaKelas", "XII");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL019", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL019", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL019", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL019", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVKKDKLSFGL019", "detail", "createdAt", instant.toString());

                // Mapel Pilihan Desain Komunikasi Visual
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "main", "idElemen", "ELEDKVPPDKLSFGL020");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "main", "namaElemen",
                                "Gerak (animation)");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "mapel", "idMapel", "MAP087");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "mapel", "name",
                                "Teknik Animasi 2D dan 3D");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "kelas", "idKelas", "KLS003");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "kelas", "namaKelas", "XII");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah", "KKS001");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah", "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "main", "idElemen", "ELEDKVPPDKLSFGL020");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "main", "namaElemen",
                                "Visual (asset creation)");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "mapel", "idMapel", "MAP087");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "mapel", "name",
                                "Teknik Animasi 2D dan 3D");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "kelas", "idKelas", "KLS003");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "kelas", "namaKelas", "XII");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah", "KKS001");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah", "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "main", "idElemen", "ELEDKVPPDKLSFGL020");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "main", "namaElemen",
                                "Editorial (visual storytelling)");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "mapel", "idMapel", "MAP087");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "mapel", "name",
                                "Teknik Animasi 2D dan 3D");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "kelas", "idKelas", "KLS003");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "kelas", "namaKelas", "XII");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah", "KKS001");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah", "Desain Komunikasi Visual");
                client.insertRecord(tableElemen, "ELEDKVPPDKLSFGL020", "detail", "createdAt", instant.toString());

        }

}
