package create_structure.MasterDataSeeder.TSM;

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
                // ELE = Elemen, DKV = Teknik Sepeda Motor, BI = Bahasa Indonesia,
                // KLS =
                // Kelas, F = Fase / XI, GL = Ganjil
                // Tiga angka terakhir adalah urutan elemen dalam fase tersebut

                // Elemen 1: Menyimak
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL001", "main", "idElemen", "ELETSMBIKLSFGL001");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL001", "main", "namaElemen", "Menyimak");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL001", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL001", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL001", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL001", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL001", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL001", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL001", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL001", "kelas", "namaKelas", "XI");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL001", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL001", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL001", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS004");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL001", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Teknik Sepeda Motor");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL001", "detail", "createdAt", instant.toString());

                // Elemen 2: Membaca dan Memirsa
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL002", "main", "idElemen", "ELETSMBIKLSFGL002");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL002", "main", "namaElemen", "Membaca dan Memirsa");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL002", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL002", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL002", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL002", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL002", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL002", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL002", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL002", "kelas", "namaKelas", "XI");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL002", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL002", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL002", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS004");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL002", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Teknik Sepeda Motor");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL002", "detail", "createdAt", instant.toString());

                // Elemen 3: Berbicara dan Mempresentasikan
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL003", "main", "idElemen", "ELETSMBIKLSFGL003");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL003", "main", "namaElemen",
                                "Berbicara dan Mempresentasikan");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL003", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL003", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL003", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL003", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL003", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL003", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL003", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL003", "kelas", "namaKelas", "XI");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL003", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL003", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL003", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS004");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL003", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Teknik Sepeda Motor");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL003", "detail", "createdAt", instant.toString());

                // Elemen 4: Menulis
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL004", "main", "idElemen", "ELETSMBIKLSFGL004");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL004", "main", "namaElemen", "Menulis");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL004", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL004", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL004", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL004", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL004", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL004", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL004", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL004", "kelas", "namaKelas", "XI");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL004", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL004", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL004", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS004");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL004", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Teknik Sepeda Motor");
                client.insertRecord(tableElemen, "ELETSMBIKLSFGL004", "detail", "createdAt", instant.toString());

                // ======================================================================================================================

                // Insert Data Table Elemen untuk Informatika Kelas X Semester Ganjil Mapel

                // Untuk Kelas X Semester Ganjil maka ada kode khusus untuk elemen
                // ELE = Elemen, BI = Bahasa Indonesia, KLS = Kelas, E = Fase / X, GL = Ganjil
                // Tiga angka terakhir adalah urutan elemen dalam fase tersebut

                // Informatika ELETSMINKLSEGL001

                client.insertRecord(tableElemen, "ELETSMINKLSEGL001", "main", "idElemen", "ELETSMINKLSEGL001");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL001", "main", "namaElemen",
                                "Berpikir Komputasional (BK)");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL001", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL001", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL001", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL001", "mapel", "name", "Informatika");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL001", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL001", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL001", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL001", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL001", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL001", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL001", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS004");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL001", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Teknik Sepeda Motor");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL001", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELETSMINKLSEGL002", "main", "idElemen", "ELETSMINKLSEGL002");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL002", "main", "namaElemen",
                                "Teknologi Informasi dan Komunikasi (TIK)");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL002", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL002", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL002", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL002", "mapel", "name", "Informatika");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL002", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL002", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL002", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL002", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL002", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL002", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL002", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS004");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL002", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Teknik Sepeda Motor");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL002", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELETSMINKLSEGL003", "main", "idElemen", "ELETSMINKLSEGL003");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL003", "main", "namaElemen", "Sistem Komputasi (SK)");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL003", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL003", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL003", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL003", "mapel", "name", "Informatika");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL003", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL003", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL003", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL003", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL003", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL003", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL003", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS004");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL003", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Teknik Sepeda Motor");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL003", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELETSMINKLSEGL004", "main", "idElemen", "ELETSMINKLSEGL004");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL004", "main", "namaElemen",
                                "Jaringan Komputer dan Internet (JKI)");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL004", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL004", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL004", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL004", "mapel", "name", "Informatika");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL004", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL004", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL004", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL004", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL004", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL004", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL004", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS004");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL004", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Teknik Sepeda Motor");
                client.insertRecord(tableElemen, "ELETSMINKLSEGL004", "detail", "createdAt", instant.toString());

                // =======================================================================================================================

                // Insert Data Table Elemen untuk Teknik Sepeda Motor Kelas X
                // Semester

                // Ganjil Mapel Matematika

                client.insertRecord(tableElemen, "ELETSMMMKLSEGL005", "main", "idElemen", "ELETSMMMKLSEGL005");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL005", "main", "namaElemen", "Bilangan");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL005", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL005", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL005", "mapel", "idMapel", "MAP005");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL005", "mapel", "name", "Matematika");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL005", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL005", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL005", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL005", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL005", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL005", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL005", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS004");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL005", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Teknik Sepeda Motor");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL005", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELETSMMMKLSEGL006", "main", "idElemen", "ELETSMMMKLSEGL006");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL006", "main", "namaElemen", "Aljabar dan Fungsi");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL006", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL006", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL006", "mapel", "idMapel", "MAP005");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL006", "mapel", "name", "Matematika");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL006", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL006", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL006", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL006", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL006", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL006", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL006", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS004");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL006", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Teknik Sepeda Motor");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL006", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELETSMMMKLSEGL007", "main", "idElemen", "ELETSMMMKLSEGL007");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL007", "main", "namaElemen", "Geometri");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL007", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL007", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL007", "mapel", "idMapel", "MAP005");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL007", "mapel", "name", "Matematika");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL007", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL007", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL007", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL007", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL007", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL007", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL007", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS004");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL007", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Teknik Sepeda Motor");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL007", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELETSMMMKLSEGL008", "main", "idElemen", "ELETSMMMKLSEGL008");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL008", "main", "namaElemen",
                                "Analisis Data dan Peluang");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL008", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL008", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL008", "mapel", "idMapel", "MAP005");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL008", "mapel", "name", "Matematika");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL008", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL008", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL008", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL008", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL008", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL008", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL008", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS004");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL008", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Teknik Sepeda Motor");
                client.insertRecord(tableElemen, "ELETSMMMKLSEGL008", "detail", "createdAt", instant.toString());

                // =======================================================================================================================

                // Insert Data Table Elemen untuk Teknik Sepeda Motor Kelas X
                // Semester
                // Ganjil Mapel Ilmu Pengetahuan Alam dan Sosial

                client.insertRecord(tableElemen, "ELETSMIPKLSEGL009", "main", "idElemen", "ELETSMIPKLSEGL009");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL009", "main", "namaElemen",
                                "Menjelaskan Fenomena Secara Ilmiah");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL009", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL009", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL009", "mapel", "idMapel", "MAP011");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL009", "mapel", "name",
                                "Projek Ilmu Pengetahuan Alam dan Sosial");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL009", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL009", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL009", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL009", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL009", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL009", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL009", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS004");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL009", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Teknik Sepeda Motor");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL009", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELETSMIPKLSEGL010", "main", "idElemen", "ELETSMIPKLSEGL010");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL010", "main", "namaElemen",
                                "Mendesain dan mengevaluasi penyelidikan ilmiah");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL010", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL010", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL010", "mapel", "idMapel", "MAP011");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL010", "mapel", "name",
                                "Projek Ilmu Pengetahuan Alam dan Sosial");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL010", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL010", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL010", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL010", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL010", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL010", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL010", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS004");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL010", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Teknik Sepeda Motor");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL010", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELETSMIPKLSEGL011", "main", "idElemen", "ELETSMIPKLSEGL011");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL011", "main", "namaElemen",
                                "Menerjemahkan data dan bukti-bukti secara ilmiah");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL011", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL011", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL011", "mapel", "idMapel", "MAP011");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL011", "mapel", "name",
                                "Projek Ilmu Pengetahuan Alam dan Sosial");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL011", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL011", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL011", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL011", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL011", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL011", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL011", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS004");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL011", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Teknik Sepeda Motor");
                client.insertRecord(tableElemen, "ELETSMIPKLSEGL011", "detail", "createdAt", instant.toString());

                // =======================================================================================================================

                // Insert Data Table Elemen untuk Pendidikan Pancasila Kelas X Semester Ganjil
                // untuk Teknik Sepeda Motor

                client.insertRecord(tableElemen, "ELETSMPPKLSEGL012", "main", "idElemen", "ELETSMPPKLSEGL012");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL012", "main", "namaElemen",
                                "Pancasila");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL012", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL012", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL012", "mapel", "idMapel", "MAP009");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL012", "mapel", "name",
                                "Pendidikan Pancasila");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL012", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL012", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL012", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL012", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL012", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL012", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL012", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS004");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL012", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Teknik Sepeda Motor");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL012", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELETSMPPKLSEGL013", "main", "idElemen", "ELETSMPPKLSEGL013");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL013", "main", "namaElemen",
                                "Undang Undang Dasar Negara Republik Indonesia Tahun 1945");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL013", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL013", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL013", "mapel", "idMapel", "MAP009");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL013", "mapel", "name",
                                "Pendidikan Pancasila");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL013", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL013", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL013", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL013", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL013", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL013", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL013", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS004");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL013", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Teknik Sepeda Motor");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL013", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELETSMPPKLSEGL014", "main", "idElemen", "ELETSMPPKLSEGL014");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL014", "main", "namaElemen",
                                "Bhinneka Tunggal Ika");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL014", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL014", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL014", "mapel", "idMapel", "MAP009");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL014", "mapel", "name",
                                "Pendidikan Pancasila");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL014", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL014", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL014", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL014", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL014", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL014", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL014", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS004");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL014", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Teknik Sepeda Motor");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL014", "detail", "createdAt", instant.toString());

                client.insertRecord(tableElemen, "ELETSMPPKLSEGL015", "main", "idElemen", "ELETSMPPKLSEGL015");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL015", "main", "namaElemen",
                                "Negara Kesatuan Republik Indonesia");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL015", "school", "idSchool", "RWK001");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL015", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL015", "mapel", "idMapel", "MAP009");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL015", "mapel", "name",
                                "Pendidikan Pancasila");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL015", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL015", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL015", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL015", "kelas", "namaKelas", "X");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL015", "semester", "idSemester", "SM001");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL015", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL015", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS004");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL015", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Teknik Sepeda Motor");
                client.insertRecord(tableElemen, "ELETSMPPKLSEGL015", "detail", "createdAt", instant.toString());

        }

}
