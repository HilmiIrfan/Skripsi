package create_structure.MasterDataSeeder;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import create_structure.HBaseCustomClient;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SeederDataMapel {

    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        HBaseCustomClient client = new HBaseCustomClient(conf);

        // Waktu sekarang
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        // Tabel Mapel
        TableName tableMapel = TableName.valueOf("mapels");

        // Semester Ganjil, Kelas X, Tahun Ajaran 2025/2026
        // Insert Data Table Mapel

        // MAP001 - Bahasa Indonesia
        client.insertRecord(tableMapel, "MAP001", "main", "idMapel", "MAP001");
        client.insertRecord(tableMapel, "MAP001", "main", "name", "Bahasa Indonesia");
        client.insertRecord(tableMapel, "MAP001", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP001", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP001", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP001", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP001", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP001", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP001", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP001", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP001", "detail", "createdAt", instant.toString());

        // MAP002 - Bahasa Inggris
        client.insertRecord(tableMapel, "MAP002", "main", "idMapel", "MAP002");
        client.insertRecord(tableMapel, "MAP002", "main", "name", "Bahasa Inggris");
        client.insertRecord(tableMapel, "MAP002", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP002", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP002", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP002", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP002", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP002", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP002", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP002", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP002", "detail", "createdAt", instant.toString());

        // MAP003 - Dasar-dasar Program Keahlian
        client.insertRecord(tableMapel, "MAP003", "main", "idMapel", "MAP003");
        client.insertRecord(tableMapel, "MAP003", "main", "name", "Dasar-dasar Program Keahlian");
        client.insertRecord(tableMapel, "MAP003", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP003", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP003", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP003", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP003", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP003", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP003", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP003", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP003", "detail", "createdAt", instant.toString());

        // MAP004 - Informatika
        client.insertRecord(tableMapel, "MAP004", "main", "idMapel", "MAP004");
        client.insertRecord(tableMapel, "MAP004", "main", "name", "Informatika");
        client.insertRecord(tableMapel, "MAP004", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP004", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP004", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP004", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP004", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP004", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP004", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP004", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP004", "detail", "createdAt", instant.toString());

        // MAP005 - Matematika
        client.insertRecord(tableMapel, "MAP005", "main", "idMapel", "MAP005");
        client.insertRecord(tableMapel, "MAP005", "main", "name", "Matematika");
        client.insertRecord(tableMapel, "MAP005", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP005", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP005", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP005", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP005", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP005", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP005", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP005", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP005", "detail", "createdAt", instant.toString());

        // MAP006 - Muatan Lokal
        client.insertRecord(tableMapel, "MAP006", "main", "idMapel", "MAP006");
        client.insertRecord(tableMapel, "MAP006", "main", "name", "Muatan Lokal");
        client.insertRecord(tableMapel, "MAP006", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP006", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP006", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP006", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP006", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP006", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP006", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP006", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP006", "detail", "createdAt", instant.toString());

        // MAP007 - Pendidikan Agama Islam dan Budi Pekerti
        client.insertRecord(tableMapel, "MAP007", "main", "idMapel", "MAP007");
        client.insertRecord(tableMapel, "MAP007", "main", "name", "Pendidikan Agama Islam dan Budi Pekerti");
        client.insertRecord(tableMapel, "MAP007", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP007", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP007", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP007", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP007", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP007", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP007", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP007", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP007", "detail", "createdAt", instant.toString());

        // MAP008 - Pendidikan Jasmani, Olahraga, dan Kesehatan
        client.insertRecord(tableMapel, "MAP008", "main", "idMapel", "MAP008");
        client.insertRecord(tableMapel, "MAP008", "main", "name", "Pendidikan Jasmani, Olahraga, dan Kesehatan");
        client.insertRecord(tableMapel, "MAP008", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP008", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP008", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP008", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP008", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP008", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP008", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP008", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP008", "detail", "createdAt", instant.toString());

        // MAP009 - Pendidikan Pancasila
        client.insertRecord(tableMapel, "MAP009", "main", "idMapel", "MAP009");
        client.insertRecord(tableMapel, "MAP009", "main", "name", "Pendidikan Pancasila");
        client.insertRecord(tableMapel, "MAP009", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP009", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP009", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP009", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP009", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP009", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP009", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP009", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP009", "detail", "createdAt", instant.toString());

        // MAP010 - Praktik Kerja Lapangan
        client.insertRecord(tableMapel, "MAP010", "main", "idMapel", "MAP010");
        client.insertRecord(tableMapel, "MAP010", "main", "name", "Praktik Kerja Lapangan");
        client.insertRecord(tableMapel, "MAP010", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP010", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP010", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP010", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP010", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP010", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP010", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP010", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP010", "detail", "createdAt", instant.toString());

        // MAP011 - Projek Ilmu Pengetahuan Alam dan Sosial
        client.insertRecord(tableMapel, "MAP011", "main", "idMapel", "MAP011");
        client.insertRecord(tableMapel, "MAP011", "main", "name", "Projek Ilmu Pengetahuan Alam dan Sosial");
        client.insertRecord(tableMapel, "MAP011", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP011", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP011", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP011", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP011", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP011", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP011", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP011", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP011", "detail", "createdAt", instant.toString());

        // MAP012 - Projek Kreatif dan Kewirausahaan
        client.insertRecord(tableMapel, "MAP012", "main", "idMapel", "MAP012");
        client.insertRecord(tableMapel, "MAP012", "main", "name", "Projek Kreatif dan Kewirausahaan");
        client.insertRecord(tableMapel, "MAP012", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP012", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP012", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP012", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP012", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP012", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP012", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP012", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP012", "detail", "createdAt", instant.toString());

        // MAP013 - Sejarah
        client.insertRecord(tableMapel, "MAP013", "main", "idMapel", "MAP013");
        client.insertRecord(tableMapel, "MAP013", "main", "name", "Sejarah");
        client.insertRecord(tableMapel, "MAP013", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP013", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP013", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP013", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP013", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP013", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP013", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP013", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP013", "detail", "createdAt", instant.toString());

        // MAP014 - Seni Budaya
        client.insertRecord(tableMapel, "MAP014", "main", "idMapel", "MAP014");
        client.insertRecord(tableMapel, "MAP014", "main", "name", "Seni Budaya");
        client.insertRecord(tableMapel, "MAP014", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP014", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP014", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP014", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP014", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP014", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP014", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP014", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP014", "detail", "createdAt", instant.toString());

        // ======================================================================================================================

        // Semester Genap, Kelas X, Tahun Ajaran 2025/2026

        // MAP015 - Bahasa Indonesia
        client.insertRecord(tableMapel, "MAP015", "main", "idMapel", "MAP015");
        client.insertRecord(tableMapel, "MAP015", "main", "name", "Bahasa Indonesia");
        client.insertRecord(tableMapel, "MAP015", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP015", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP015", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP015", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP015", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP015", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP015", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP015", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP015", "detail", "createdAt", instant.toString());

        // MAP016 - Bahasa Inggris
        client.insertRecord(tableMapel, "MAP016", "main", "idMapel", "MAP016");
        client.insertRecord(tableMapel, "MAP016", "main", "name", "Bahasa Inggris");
        client.insertRecord(tableMapel, "MAP016", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP016", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP016", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP016", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP016", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP016", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP016", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP016", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP016", "detail", "createdAt", instant.toString());

        // MAP017 - Dasar-dasar Program Keahlian
        client.insertRecord(tableMapel, "MAP017", "main", "idMapel", "MAP017");
        client.insertRecord(tableMapel, "MAP017", "main", "name", "Dasar-dasar Program Keahlian");
        client.insertRecord(tableMapel, "MAP017", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP017", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP017", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP017", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP017", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP017", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP017", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP017", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP017", "detail", "createdAt", instant.toString());

        // MAP018 - Informatika
        client.insertRecord(tableMapel, "MAP018", "main", "idMapel", "MAP018");
        client.insertRecord(tableMapel, "MAP018", "main", "name", "Informatika");
        client.insertRecord(tableMapel, "MAP018", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP018", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP018", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP018", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP018", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP018", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP018", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP018", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP018", "detail", "createdAt", instant.toString());

        // MAP019 - Matematika
        client.insertRecord(tableMapel, "MAP019", "main", "idMapel", "MAP019");
        client.insertRecord(tableMapel, "MAP019", "main", "name", "Matematika");
        client.insertRecord(tableMapel, "MAP019", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP019", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP019", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP019", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP019", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP019", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP019", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP019", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP019", "detail", "createdAt", instant.toString());

        // MAP020 - Muatan Lokal
        client.insertRecord(tableMapel, "MAP020", "main", "idMapel", "MAP020");
        client.insertRecord(tableMapel, "MAP020", "main", "name", "Muatan Lokal");
        client.insertRecord(tableMapel, "MAP020", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP020", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP020", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP020", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP020", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP020", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP020", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP020", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP020", "detail", "createdAt", instant.toString());

        // MAP021 - Pendidikan Agama Islam dan Budi Pekerti
        client.insertRecord(tableMapel, "MAP021", "main", "idMapel", "MAP021");
        client.insertRecord(tableMapel, "MAP021", "main", "name", "Pendidikan Agama Islam dan Budi Pekerti");
        client.insertRecord(tableMapel, "MAP021", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP021", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP021", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP021", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP021", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP021", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP021", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP021", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP021", "detail", "createdAt", instant.toString());

        // MAP022 - Pendidikan Jasmani, Olahraga, dan Kesehatan
        client.insertRecord(tableMapel, "MAP022", "main", "idMapel", "MAP022");
        client.insertRecord(tableMapel, "MAP022", "main", "name", "Pendidikan Jasmani, Olahraga, dan Kesehatan");
        client.insertRecord(tableMapel, "MAP022", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP022", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP022", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP022", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP022", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP022", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP022", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP022", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP022", "detail", "createdAt", instant.toString());

        // MAP023 - Pendidikan Pancasila
        client.insertRecord(tableMapel, "MAP023", "main", "idMapel", "MAP023");
        client.insertRecord(tableMapel, "MAP023", "main", "name", "Pendidikan Pancasila");
        client.insertRecord(tableMapel, "MAP023", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP023", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP023", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP023", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP023", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP023", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP023", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP023", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP023", "detail", "createdAt", instant.toString());

        // MAP024 - Praktik Kerja Lapangan
        client.insertRecord(tableMapel, "MAP024", "main", "idMapel", "MAP024");
        client.insertRecord(tableMapel, "MAP024", "main", "name", "Praktik Kerja Lapangan");
        client.insertRecord(tableMapel, "MAP024", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP024", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP024", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP024", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP024", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP024", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP024", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP024", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP024", "detail", "createdAt", instant.toString());

        // MAP025 - Projek Ilmu Pengetahuan Alam dan Sosial
        client.insertRecord(tableMapel, "MAP025", "main", "idMapel", "MAP025");
        client.insertRecord(tableMapel, "MAP025", "main", "name", "Projek Ilmu Pengetahuan Alam dan Sosial");
        client.insertRecord(tableMapel, "MAP025", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP025", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP025", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP025", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP025", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP025", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP025", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP025", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP025", "detail", "createdAt", instant.toString());

        // MAP026 - Projek Kreatif dan Kewirausahaan
        client.insertRecord(tableMapel, "MAP026", "main", "idMapel", "MAP026");
        client.insertRecord(tableMapel, "MAP026", "main", "name", "Projek Kreatif dan Kewirausahaan");
        client.insertRecord(tableMapel, "MAP026", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP026", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP026", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP026", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP026", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP026", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP026", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP026", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP026", "detail", "createdAt", instant.toString());

        // MAP027 - Sejarah
        client.insertRecord(tableMapel, "MAP027", "main", "idMapel", "MAP027");
        client.insertRecord(tableMapel, "MAP027", "main", "name", "Sejarah");
        client.insertRecord(tableMapel, "MAP027", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP027", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP027", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP027", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP027", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP027", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP027", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP027", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP027", "detail", "createdAt", instant.toString());

        // MAP028 - Seni Budaya
        client.insertRecord(tableMapel, "MAP028", "main", "idMapel", "MAP028");
        client.insertRecord(tableMapel, "MAP028", "main", "name", "Seni Budaya");
        client.insertRecord(tableMapel, "MAP028", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP028", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP028", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP028", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP028", "kelas", "idKelas", "KLS001");
        client.insertRecord(tableMapel, "MAP028", "kelas", "namaKelas", "X");
        client.insertRecord(tableMapel, "MAP028", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP028", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP028", "detail", "createdAt", instant.toString());

        // ======================================================================================================================

        // Semester Ganjil, Kelas XI, Tahun Ajaran 2025/2026

        // MAP029 - Bahasa Indonesia
        client.insertRecord(tableMapel, "MAP029", "main", "idMapel", "MAP029");
        client.insertRecord(tableMapel, "MAP029", "main", "name", "Bahasa Indonesia");
        client.insertRecord(tableMapel, "MAP029", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP029", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP029", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP029", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP029", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP029", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP029", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP029", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP029", "detail", "createdAt", instant.toString());

        // MAP030 - Bahasa Inggris
        client.insertRecord(tableMapel, "MAP030", "main", "idMapel", "MAP030");
        client.insertRecord(tableMapel, "MAP030", "main", "name", "Bahasa Inggris");
        client.insertRecord(tableMapel, "MAP030", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP030", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP030", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP030", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP030", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP030", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP030", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP030", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP030", "detail", "createdAt", instant.toString());

        // MAP031 - Dasar-dasar Program Keahlian
        client.insertRecord(tableMapel, "MAP031", "main", "idMapel", "MAP031");
        client.insertRecord(tableMapel, "MAP031", "main", "name", "Dasar-dasar Program Keahlian");
        client.insertRecord(tableMapel, "MAP031", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP031", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP031", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP031", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP031", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP031", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP031", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP031", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP031", "detail", "createdAt", instant.toString());

        // MAP032 - Informatika
        client.insertRecord(tableMapel, "MAP032", "main", "idMapel", "MAP032");
        client.insertRecord(tableMapel, "MAP032", "main", "name", "Informatika");
        client.insertRecord(tableMapel, "MAP032", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP032", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP032", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP032", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP032", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP032", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP032", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP032", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP032", "detail", "createdAt", instant.toString());

        // MAP033 - Matematika
        client.insertRecord(tableMapel, "MAP033", "main", "idMapel", "MAP033");
        client.insertRecord(tableMapel, "MAP033", "main", "name", "Matematika");
        client.insertRecord(tableMapel, "MAP033", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP033", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP033", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP033", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP033", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP033", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP033", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP033", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP033", "detail", "createdAt", instant.toString());

        // MAP034 - Muatan Lokal
        client.insertRecord(tableMapel, "MAP034", "main", "idMapel", "MAP034");
        client.insertRecord(tableMapel, "MAP034", "main", "name", "Muatan Lokal");
        client.insertRecord(tableMapel, "MAP034", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP034", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP034", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP034", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP034", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP034", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP034", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP034", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP034", "detail", "createdAt", instant.toString());

        // MAP035 - Pendidikan Agama Islam dan Budi Pekerti
        client.insertRecord(tableMapel, "MAP035", "main", "idMapel", "MAP035");
        client.insertRecord(tableMapel, "MAP035", "main", "name", "Pendidikan Agama Islam dan Budi Pekerti");
        client.insertRecord(tableMapel, "MAP035", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP035", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP035", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP035", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP035", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP035", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP035", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP035", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP035", "detail", "createdAt", instant.toString());

        // MAP036 - Pendidikan Jasmani, Olahraga, dan Kesehatan
        client.insertRecord(tableMapel, "MAP036", "main", "idMapel", "MAP036");
        client.insertRecord(tableMapel, "MAP036", "main", "name", "Pendidikan Jasmani, Olahraga, dan Kesehatan");
        client.insertRecord(tableMapel, "MAP036", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP036", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP036", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP036", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP036", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP036", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP036", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP036", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP036", "detail", "createdAt", instant.toString());

        // MAP037 - Pendidikan Pancasila
        client.insertRecord(tableMapel, "MAP037", "main", "idMapel", "MAP037");
        client.insertRecord(tableMapel, "MAP037", "main", "name", "Pendidikan Pancasila");
        client.insertRecord(tableMapel, "MAP037", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP037", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP037", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP037", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP037", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP037", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP037", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP037", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP037", "detail", "createdAt", instant.toString());

        // MAP038 - Praktik Kerja Lapangan
        client.insertRecord(tableMapel, "MAP038", "main", "idMapel", "MAP038");
        client.insertRecord(tableMapel, "MAP038", "main", "name", "Praktik Kerja Lapangan");
        client.insertRecord(tableMapel, "MAP038", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP038", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP038", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP038", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP038", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP038", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP038", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP038", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP038", "detail", "createdAt", instant.toString());

        // MAP039 - Projek Ilmu Pengetahuan Alam dan Sosial
        client.insertRecord(tableMapel, "MAP039", "main", "idMapel", "MAP039");
        client.insertRecord(tableMapel, "MAP039", "main", "name", "Projek Ilmu Pengetahuan Alam dan Sosial");
        client.insertRecord(tableMapel, "MAP039", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP039", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP039", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP039", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP039", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP039", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP039", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP039", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP039", "detail", "createdAt", instant.toString());

        // MAP040 - Projek Kreatif dan Kewirausahaan
        client.insertRecord(tableMapel, "MAP040", "main", "idMapel", "MAP040");
        client.insertRecord(tableMapel, "MAP040", "main", "name", "Projek Kreatif dan Kewirausahaan");
        client.insertRecord(tableMapel, "MAP040", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP040", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP040", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP040", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP040", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP040", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP040", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP040", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP040", "detail", "createdAt", instant.toString());

        // MAP041 - Sejarah
        client.insertRecord(tableMapel, "MAP041", "main", "idMapel", "MAP041");
        client.insertRecord(tableMapel, "MAP041", "main", "name", "Sejarah");
        client.insertRecord(tableMapel, "MAP041", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP041", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP041", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP041", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP041", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP041", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP041", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP041", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP041", "detail", "createdAt", instant.toString());

        // MAP042 - Seni Budaya
        client.insertRecord(tableMapel, "MAP042", "main", "idMapel", "MAP042");
        client.insertRecord(tableMapel, "MAP042", "main", "name", "Seni Budaya");
        client.insertRecord(tableMapel, "MAP042", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP042", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP042", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP042", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP042", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP042", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP042", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP042", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP042", "detail", "createdAt", instant.toString());

        // ======================================================================================================================

        // Semester Genap, Kelas XI, Tahun Ajaran 2025/2026

        // MAP043 - Bahasa Indonesia
        client.insertRecord(tableMapel, "MAP043", "main", "idMapel", "MAP043");
        client.insertRecord(tableMapel, "MAP043", "main", "name", "Bahasa Indonesia");
        client.insertRecord(tableMapel, "MAP043", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP043", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP043", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP043", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP043", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP043", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP043", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP043", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP043", "detail", "createdAt", instant.toString());

        // MAP044 - Bahasa Inggris
        client.insertRecord(tableMapel, "MAP044", "main", "idMapel", "MAP044");
        client.insertRecord(tableMapel, "MAP044", "main", "name", "Bahasa Inggris");
        client.insertRecord(tableMapel, "MAP044", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP044", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP044", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP044", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP044", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP044", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP044", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP044", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP044", "detail", "createdAt", instant.toString());

        // MAP045 - Dasar-dasar Program Keahlian
        client.insertRecord(tableMapel, "MAP045", "main", "idMapel", "MAP045");
        client.insertRecord(tableMapel, "MAP045", "main", "name", "Dasar-dasar Program Keahlian");
        client.insertRecord(tableMapel, "MAP045", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP045", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP045", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP045", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP045", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP045", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP045", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP045", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP045", "detail", "createdAt", instant.toString());

        // MAP046 - Informatika
        client.insertRecord(tableMapel, "MAP046", "main", "idMapel", "MAP046");
        client.insertRecord(tableMapel, "MAP046", "main", "name", "Informatika");
        client.insertRecord(tableMapel, "MAP046", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP046", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP046", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP046", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP046", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP046", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP046", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP046", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP046", "detail", "createdAt", instant.toString());

        // MAP047 - Matematika
        client.insertRecord(tableMapel, "MAP047", "main", "idMapel", "MAP047");
        client.insertRecord(tableMapel, "MAP047", "main", "name", "Matematika");
        client.insertRecord(tableMapel, "MAP047", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP047", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP047", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP047", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP047", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP047", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP047", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP047", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP047", "detail", "createdAt", instant.toString());

        // MAP048 - Muatan Lokal
        client.insertRecord(tableMapel, "MAP048", "main", "idMapel", "MAP048");
        client.insertRecord(tableMapel, "MAP048", "main", "name", "Muatan Lokal");
        client.insertRecord(tableMapel, "MAP048", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP048", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP048", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP048", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP048", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP048", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP048", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP048", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP048", "detail", "createdAt", instant.toString());

        // MAP049 - Pendidikan Agama Islam dan Budi Pekerti
        client.insertRecord(tableMapel, "MAP049", "main", "idMapel", "MAP049");
        client.insertRecord(tableMapel, "MAP049", "main", "name", "Pendidikan Agama Islam dan Budi Pekerti");
        client.insertRecord(tableMapel, "MAP049", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP049", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP049", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP049", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP049", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP049", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP049", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP049", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP049", "detail", "createdAt", instant.toString());

        // MAP050 - Pendidikan Jasmani, Olahraga, dan Kesehatan
        client.insertRecord(tableMapel, "MAP050", "main", "idMapel", "MAP050");
        client.insertRecord(tableMapel, "MAP050", "main", "name", "Pendidikan Jasmani, Olahraga, dan Kesehatan");
        client.insertRecord(tableMapel, "MAP050", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP050", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP050", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP050", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP050", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP050", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP050", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP050", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP050", "detail", "createdAt", instant.toString());

        // MAP051 - Pendidikan Pancasila
        client.insertRecord(tableMapel, "MAP051", "main", "idMapel", "MAP051");
        client.insertRecord(tableMapel, "MAP051", "main", "name", "Pendidikan Pancasila");
        client.insertRecord(tableMapel, "MAP051", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP051", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP051", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP051", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP051", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP051", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP051", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP051", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP051", "detail", "createdAt", instant.toString());

        // MAP052 - Praktik Kerja Lapangan
        client.insertRecord(tableMapel, "MAP052", "main", "idMapel", "MAP052");
        client.insertRecord(tableMapel, "MAP052", "main", "name", "Praktik Kerja Lapangan");
        client.insertRecord(tableMapel, "MAP052", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP052", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP052", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP052", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP052", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP052", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP052", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP052", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP052", "detail", "createdAt", instant.toString());

        // MAP053 - Projek Ilmu Pengetahuan Alam dan Sosial
        client.insertRecord(tableMapel, "MAP053", "main", "idMapel", "MAP053");
        client.insertRecord(tableMapel, "MAP053", "main", "name", "Projek Ilmu Pengetahuan Alam dan Sosial");
        client.insertRecord(tableMapel, "MAP053", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP053", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP053", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP053", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP053", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP053", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP053", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP053", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP053", "detail", "createdAt", instant.toString());

        // MAP054 - Projek Kreatif dan Kewirausahaan
        client.insertRecord(tableMapel, "MAP054", "main", "idMapel", "MAP054");
        client.insertRecord(tableMapel, "MAP054", "main", "name", "Projek Kreatif dan Kewirausahaan");
        client.insertRecord(tableMapel, "MAP054", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP054", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP054", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP054", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP054", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP054", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP054", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP054", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP054", "detail", "createdAt", instant.toString());

        // MAP055 - Sejarah
        client.insertRecord(tableMapel, "MAP055", "main", "idMapel", "MAP055");
        client.insertRecord(tableMapel, "MAP055", "main", "name", "Sejarah");
        client.insertRecord(tableMapel, "MAP055", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP055", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP055", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP055", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP055", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP055", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP055", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP055", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP055", "detail", "createdAt", instant.toString());

        // MAP056 - Seni Budaya
        client.insertRecord(tableMapel, "MAP056", "main", "idMapel", "MAP056");
        client.insertRecord(tableMapel, "MAP056", "main", "name", "Seni Budaya");
        client.insertRecord(tableMapel, "MAP056", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP056", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP056", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP056", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP056", "kelas", "idKelas", "KLS002");
        client.insertRecord(tableMapel, "MAP056", "kelas", "namaKelas", "XI");
        client.insertRecord(tableMapel, "MAP056", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP056", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP056", "detail", "createdAt", instant.toString());

        // ======================================================================================================================

        // Kelas XII, Tahun Ajaran 2025/2026

        // Semester Ganjil, Kelas XII, Tahun Ajaran 2025/2026

        // MAP057 - Bahasa Indonesia
        client.insertRecord(tableMapel, "MAP057", "main", "idMapel", "MAP057");
        client.insertRecord(tableMapel, "MAP057", "main", "name", "Bahasa Indonesia");
        client.insertRecord(tableMapel, "MAP057", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP057", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP057", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP057", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP057", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP057", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP057", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP057", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP057", "detail", "createdAt", instant.toString());

        // MAP058 - Bahasa Inggris
        client.insertRecord(tableMapel, "MAP058", "main", "idMapel", "MAP058");
        client.insertRecord(tableMapel, "MAP058", "main", "name", "Bahasa Inggris");
        client.insertRecord(tableMapel, "MAP058", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP058", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP058", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP058", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP058", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP058", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP058", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP058", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP058", "detail", "createdAt", instant.toString());

        // MAP059 - Dasar-dasar Program Keahlian
        client.insertRecord(tableMapel, "MAP059", "main", "idMapel", "MAP059");
        client.insertRecord(tableMapel, "MAP059", "main", "name", "Dasar-dasar Program Keahlian");
        client.insertRecord(tableMapel, "MAP059", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP059", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP059", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP059", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP059", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP059", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP059", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP059", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP059", "detail", "createdAt", instant.toString());

        // MAP060 - Informatika
        client.insertRecord(tableMapel, "MAP060", "main", "idMapel", "MAP060");
        client.insertRecord(tableMapel, "MAP060", "main", "name", "Informatika");
        client.insertRecord(tableMapel, "MAP060", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP060", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP060", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP060", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP060", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP060", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP060", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP060", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP060", "detail", "createdAt", instant.toString());

        // MAP061 - Matematika
        client.insertRecord(tableMapel, "MAP061", "main", "idMapel", "MAP061");
        client.insertRecord(tableMapel, "MAP061", "main", "name", "Matematika");
        client.insertRecord(tableMapel, "MAP061", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP061", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP061", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP061", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP061", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP061", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP061", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP061", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP061", "detail", "createdAt", instant.toString());

        // MAP062 - Muatan Lokal
        client.insertRecord(tableMapel, "MAP062", "main", "idMapel", "MAP062");
        client.insertRecord(tableMapel, "MAP062", "main", "name", "Muatan Lokal");
        client.insertRecord(tableMapel, "MAP062", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP062", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP062", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP062", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP062", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP062", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP062", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP062", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP062", "detail", "createdAt", instant.toString());

        // MAP063 - Pendidikan Agama Islam dan Budi Pekerti
        client.insertRecord(tableMapel, "MAP063", "main", "idMapel", "MAP063");
        client.insertRecord(tableMapel, "MAP063", "main", "name", "Pendidikan Agama Islam dan Budi Pekerti");
        client.insertRecord(tableMapel, "MAP063", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP063", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP063", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP063", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP063", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP063", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP063", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP063", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP063", "detail", "createdAt", instant.toString());

        // MAP064 - Pendidikan Jasmani, Olahraga, dan Kesehatan
        client.insertRecord(tableMapel, "MAP064", "main", "idMapel", "MAP064");
        client.insertRecord(tableMapel, "MAP064", "main", "name", "Pendidikan Jasmani, Olahraga, dan Kesehatan");
        client.insertRecord(tableMapel, "MAP064", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP064", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP064", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP064", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP064", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP064", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP064", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP064", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP064", "detail", "createdAt", instant.toString());

        // MAP065 - Pendidikan Pancasila
        client.insertRecord(tableMapel, "MAP065", "main", "idMapel", "MAP065");
        client.insertRecord(tableMapel, "MAP065", "main", "name", "Pendidikan Pancasila");
        client.insertRecord(tableMapel, "MAP065", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP065", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP065", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP065", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP065", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP065", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP065", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP065", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP065", "detail", "createdAt", instant.toString());

        // MAP066 - Praktik Kerja Lapangan
        client.insertRecord(tableMapel, "MAP066", "main", "idMapel", "MAP066");
        client.insertRecord(tableMapel, "MAP066", "main", "name", "Praktik Kerja Lapangan");
        client.insertRecord(tableMapel, "MAP066", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP066", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP066", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP066", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP066", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP066", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP066", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP066", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP066", "detail", "createdAt", instant.toString());

        // MAP067 - Projek Ilmu Pengetahuan Alam dan Sosial
        client.insertRecord(tableMapel, "MAP067", "main", "idMapel", "MAP067");
        client.insertRecord(tableMapel, "MAP067", "main", "name", "Projek Ilmu Pengetahuan Alam dan Sosial");
        client.insertRecord(tableMapel, "MAP067", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP067", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP067", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP067", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP067", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP067", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP067", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP067", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP067", "detail", "createdAt", instant.toString());

        // MAP068 - Projek Kreatif dan Kewirausahaan
        client.insertRecord(tableMapel, "MAP068", "main", "idMapel", "MAP068");
        client.insertRecord(tableMapel, "MAP068", "main", "name", "Projek Kreatif dan Kewirausahaan");
        client.insertRecord(tableMapel, "MAP068", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP068", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP068", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP068", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP068", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP068", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP068", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP068", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP068", "detail", "createdAt", instant.toString());

        // MAP069 - Sejarah
        client.insertRecord(tableMapel, "MAP069", "main", "idMapel", "MAP069");
        client.insertRecord(tableMapel, "MAP069", "main", "name", "Sejarah");
        client.insertRecord(tableMapel, "MAP069", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP069", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP069", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP069", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP069", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP069", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP069", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP069", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP069", "detail", "createdAt", instant.toString());

        // MAP070 - Seni Budaya
        client.insertRecord(tableMapel, "MAP070", "main", "idMapel", "MAP070");
        client.insertRecord(tableMapel, "MAP070", "main", "name", "Seni Budaya");
        client.insertRecord(tableMapel, "MAP070", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP070", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP070", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP070", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP070", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP070", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP070", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP070", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP070", "detail", "createdAt", instant.toString());

        // ======================================================================================================================

        // Semester Genap, Kelas XII, Tahun Ajaran 2025/2026

        // MAP071 - Bahasa Indonesia
        client.insertRecord(tableMapel, "MAP071", "main", "idMapel", "MAP071");
        client.insertRecord(tableMapel, "MAP071", "main", "name", "Bahasa Indonesia");
        client.insertRecord(tableMapel, "MAP071", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP071", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP071", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP071", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP071", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP071", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP071", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP071", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP071", "detail", "createdAt", instant.toString());

        // MAP072 - Bahasa Inggris
        client.insertRecord(tableMapel, "MAP072", "main", "idMapel", "MAP072");
        client.insertRecord(tableMapel, "MAP072", "main", "name", "Bahasa Inggris");
        client.insertRecord(tableMapel, "MAP072", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP072", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP072", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP072", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP072", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP072", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP072", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP072", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP072", "detail", "createdAt", instant.toString());

        // MAP073 - Dasar-dasar Program Keahlian
        client.insertRecord(tableMapel, "MAP073", "main", "idMapel", "MAP073");
        client.insertRecord(tableMapel, "MAP073", "main", "name", "Dasar-dasar Program Keahlian");
        client.insertRecord(tableMapel, "MAP073", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP073", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP073", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP073", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP073", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP073", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP073", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP073", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP073", "detail", "createdAt", instant.toString());

        // MAP074 - Informatika
        client.insertRecord(tableMapel, "MAP074", "main", "idMapel", "MAP074");
        client.insertRecord(tableMapel, "MAP074", "main", "name", "Informatika");
        client.insertRecord(tableMapel, "MAP074", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP074", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP074", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP074", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP074", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP074", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP074", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP074", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP074", "detail", "createdAt", instant.toString());

        // MAP075 - Matematika
        client.insertRecord(tableMapel, "MAP075", "main", "idMapel", "MAP075");
        client.insertRecord(tableMapel, "MAP075", "main", "name", "Matematika");
        client.insertRecord(tableMapel, "MAP075", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP075", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP075", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP075", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP075", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP075", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP075", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP075", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP075", "detail", "createdAt", instant.toString());

        // MAP076 - Muatan Lokal
        client.insertRecord(tableMapel, "MAP076", "main", "idMapel", "MAP076");
        client.insertRecord(tableMapel, "MAP076", "main", "name", "Muatan Lokal");
        client.insertRecord(tableMapel, "MAP076", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP076", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP076", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP076", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP076", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP076", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP076", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP076", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP076", "detail", "createdAt", instant.toString());

        // MAP077 - Pendidikan Agama Islam dan Budi Pekerti
        client.insertRecord(tableMapel, "MAP077", "main", "idMapel", "MAP077");
        client.insertRecord(tableMapel, "MAP077", "main", "name", "Pendidikan Agama Islam dan Budi Pekerti");
        client.insertRecord(tableMapel, "MAP077", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP077", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP077", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP077", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP077", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP077", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP077", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP077", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP077", "detail", "createdAt", instant.toString());

        // MAP078 - Pendidikan Jasmani, Olahraga, dan Kesehatan
        client.insertRecord(tableMapel, "MAP078", "main", "idMapel", "MAP078");
        client.insertRecord(tableMapel, "MAP078", "main", "name", "Pendidikan Jasmani, Olahraga, dan Kesehatan");
        client.insertRecord(tableMapel, "MAP078", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP078", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP078", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP078", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP078", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP078", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP078", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP078", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP078", "detail", "createdAt", instant.toString());

        // MAP079 - Pendidikan Pancasila
        client.insertRecord(tableMapel, "MAP079", "main", "idMapel", "MAP079");
        client.insertRecord(tableMapel, "MAP079", "main", "name", "Pendidikan Pancasila");
        client.insertRecord(tableMapel, "MAP079", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP079", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP079", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP079", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP079", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP079", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP079", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP079", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP079", "detail", "createdAt", instant.toString());

        // MAP080 - Praktik Kerja Lapangan
        client.insertRecord(tableMapel, "MAP080", "main", "idMapel", "MAP080");
        client.insertRecord(tableMapel, "MAP080", "main", "name", "Praktik Kerja Lapangan");
        client.insertRecord(tableMapel, "MAP080", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP080", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP080", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP080", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP080", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP080", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP080", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP080", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP080", "detail", "createdAt", instant.toString());

        // MAP081 - Projek Ilmu Pengetahuan Alam dan Sosial
        client.insertRecord(tableMapel, "MAP081", "main", "idMapel", "MAP081");
        client.insertRecord(tableMapel, "MAP081", "main", "name", "Projek Ilmu Pengetahuan Alam dan Sosial");
        client.insertRecord(tableMapel, "MAP081", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP081", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP081", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP081", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP081", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP081", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP081", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP081", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP081", "detail", "createdAt", instant.toString());

        // MAP082 - Projek Kreatif dan Kewirausahaan
        client.insertRecord(tableMapel, "MAP082", "main", "idMapel", "MAP082");
        client.insertRecord(tableMapel, "MAP082", "main", "name", "Projek Kreatif dan Kewirausahaan");
        client.insertRecord(tableMapel, "MAP082", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP082", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP082", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP082", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP082", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP082", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP082", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP082", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP082", "detail", "createdAt", instant.toString());

        // MAP083 - Sejarah
        client.insertRecord(tableMapel, "MAP083", "main", "idMapel", "MAP083");
        client.insertRecord(tableMapel, "MAP083", "main", "name", "Sejarah");
        client.insertRecord(tableMapel, "MAP083", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP083", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP083", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP083", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP083", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP083", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP083", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP083", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP083", "detail", "createdAt", instant.toString());

        // MAP084 - Seni Budaya
        client.insertRecord(tableMapel, "MAP084", "main", "idMapel", "MAP084");
        client.insertRecord(tableMapel, "MAP084", "main", "name", "Seni Budaya");
        client.insertRecord(tableMapel, "MAP084", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP084", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP084", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP084", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP084", "kelas", "idKelas", "KLS003"); // Diubah ke KLS003
        client.insertRecord(tableMapel, "MAP084", "kelas", "namaKelas", "XII"); // Diubah ke XII
        client.insertRecord(tableMapel, "MAP084", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP084", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP084", "detail", "createdAt", instant.toString());

        // =======================================================================================================================
        // Bagian Untuk Mapel Konsentrasi dan Pilihan DKV XII

        // Mapel Konsentrasi DKV Kelas XII
        client.insertRecord(tableMapel, "MAP085", "main", "idMapel", "MAP085");
        client.insertRecord(tableMapel, "MAP085", "main", "name", "Konsentrasi Keahlian Desain Komunikasi Visual");
        client.insertRecord(tableMapel, "MAP085", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP085", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP085", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP085", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP085", "kelas", "idKelas", "KLS003");
        client.insertRecord(tableMapel, "MAP085", "kelas", "namaKelas", "XII");
        client.insertRecord(tableMapel, "MAP085", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP085", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP085", "detail", "createdAt", instant.toString());

        client.insertRecord(tableMapel, "MAP086", "main", "idMapel", "MAP086");
        client.insertRecord(tableMapel, "MAP086", "main", "name", "Konsentrasi Keahlian Desain Komunikasi Visual");
        client.insertRecord(tableMapel, "MAP086", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP086", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP086", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP086", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP086", "kelas", "idKelas", "KLS003");
        client.insertRecord(tableMapel, "MAP086", "kelas", "namaKelas", "XII");
        client.insertRecord(tableMapel, "MAP086", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP086", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP086", "detail", "createdAt", instant.toString());

        // Mapel Pilihan DKV Kelas XII
        client.insertRecord(tableMapel, "MAP087", "main", "idMapel", "MAP087");
        client.insertRecord(tableMapel, "MAP087", "main", "name", "Teknik Animasi 2D dan 3D");
        client.insertRecord(tableMapel, "MAP087", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP087", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP087", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP087", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP087", "kelas", "idKelas", "KLS003");
        client.insertRecord(tableMapel, "MAP087", "kelas", "namaKelas", "XII");
        client.insertRecord(tableMapel, "MAP087", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP087", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP087", "detail", "createdAt", instant.toString());

        client.insertRecord(tableMapel, "MAP088", "main", "idMapel", "MAP088");
        client.insertRecord(tableMapel, "MAP088", "main", "name", "Teknik Animasi 2D dan 3D");
        client.insertRecord(tableMapel, "MAP088", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP088", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP088", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP088", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP088", "kelas", "idKelas", "KLS003");
        client.insertRecord(tableMapel, "MAP088", "kelas", "namaKelas", "XII");
        client.insertRecord(tableMapel, "MAP088", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP088", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP088", "detail", "createdAt", instant.toString());

        // =======================================================================================================================

        // Bagian Untuk Mapel Konsentrasi dan Pilihan DPB Kelas X, XI, dan XII

        // Mapel Konsentrasi DPB Kelas XII
        client.insertRecord(tableMapel, "MAP089", "main", "idMapel", "MAP089");
        client.insertRecord(tableMapel, "MAP089", "main", "name", "Desain dan Produksi Busana");
        client.insertRecord(tableMapel, "MAP089", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP089", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP089", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP089", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP089", "kelas", "idKelas", "KLS003");
        client.insertRecord(tableMapel, "MAP089", "kelas", "namaKelas", "XII");
        client.insertRecord(tableMapel, "MAP089", "semester", "idSemester", "SM001");
        client.insertRecord(tableMapel, "MAP089", "semester", "namaSemester", "Ganjil");
        client.insertRecord(tableMapel, "MAP089", "detail", "createdAt", instant.toString());

        client.insertRecord(tableMapel, "MAP090", "main", "idMapel", "MAP090");
        client.insertRecord(tableMapel, "MAP090", "main", "name", "Desain dan Produksi Busana");
        client.insertRecord(tableMapel, "MAP090", "school", "idSchool", "RWK001");
        client.insertRecord(tableMapel, "MAP090", "school", "nameSchool", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableMapel, "MAP090", "tahunAjaran", "idTahun", "TA001");
        client.insertRecord(tableMapel, "MAP090", "tahunAjaran", "tahunAjaran", "2025/2026");
        client.insertRecord(tableMapel, "MAP090", "kelas", "idKelas", "KLS003");
        client.insertRecord(tableMapel, "MAP090", "kelas", "namaKelas", "XII");
        client.insertRecord(tableMapel, "MAP090", "semester", "idSemester", "SM002");
        client.insertRecord(tableMapel, "MAP090", "semester", "namaSemester", "Genap");
        client.insertRecord(tableMapel, "MAP090", "detail", "createdAt", instant.toString());

        // ========================================================================================================================

    }

}
