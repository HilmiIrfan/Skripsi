package create_structure.MasterDataSeeder.DKV;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import create_structure.HBaseCustomClient;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SeederDataATP {
        public static void main(String[] args) throws IOException {
                Configuration conf = HBaseConfiguration.create();
                HBaseCustomClient client = new HBaseCustomClient(conf);

                // Waktu sekarang
                ZoneId zoneId = ZoneId.of("Asia/Jakarta");
                ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
                Instant instant = zonedDateTime.toInstant();

                // Tabel Elemen
                TableName tableAtp = TableName.valueOf("atp");

                // Insert Data Table ACP untuk Bahasa Indonesia Kelas XI Semester Ganjil
                // Mapel Bahasa Indonesia

                // Untuk Kelas XI Semester Ganjil maka ada kode khusus untuk ACP
                // ATP = ATP, ACP = ACP, ELE = Elemen, DKV = Desain Komunikasi Visual,
                // BI = Bahasa Indonesia, KLS = Kelas, F = Fase / XI, GL = Ganjil
                // Tiga angka terakhir adalah urutan elemen dalam fase tersebut

                // Elemen 1: Menyimak dan Memirsa 1
                // ATP 1
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL001", "main", "idAtp", "ATPACPELEDKVBIKLSFGL001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL001", "main", "namaAtp",
                                "B1. Menemukan Tema dan pesan dalam cerpen yang menginspirasi penggubahan puisi Menganalisis unsur-unsur pembangun puisi");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL001", "main", "jumlahJpl", "4");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL001", "acp", "idAcp", "ACPELEDKVBIKLSFGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL001", "acp", "namaAcp",
                                "Peserta didik mampu mengevaluasi gagasan dan pandangan berdasarkan kaidah logika berpikir dari membaca berbagai tipe teks (nonfiksi dan fiksi) di media cetak dan elektronik. Peserta didik mampu mengapresiasi teks fiksi dan nonfiksi ");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL001", "elemen", "idElemen", "ELEDKVBIKLSFGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL001", "elemen", "namaElemen", "Membaca dan Memirsa");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL001", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL001", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL001", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL001", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL001", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL001", "kelas", "namaKelas", "XI");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL001", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL001", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL001", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL001", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL001", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL001", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL001", "detail", "createdAt", instant.toString());

                // ATP 2
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL002", "main", "idAtp", "ATPACPELEDKVBIKLSFGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL002", "main", "namaAtp",
                                "Mengidentifikasi perbedaan antara drama, puisi dan prosa");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL002", "main", "jumlahJpl", "4");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL002", "acp", "idAcp", "ACPELEDKVBIKLSFGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL002", "acp", "namaAcp",
                                "Peserta didik mampu mengevaluasi gagasan dan pandangan berdasarkan kaidah logika berpikir dari membaca berbagai tipe teks (nonfiksi dan fiksi) di media cetak dan elektronik. Peserta didik mampu mengapresiasi teks fiksi dan nonfiksi ");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL002", "elemen", "idElemen", "ELEDKVBIKLSFGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL002", "elemen", "namaElemen", "Membaca dan Memirsa");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL002", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL002", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL002", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL002", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL002", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL002", "kelas", "namaKelas", "XI");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL002", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL002", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL002", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL002", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL002", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL002", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL002", "detail", "createdAt", instant.toString());

                // ATP 3
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL003", "main", "idAtp", "ATPACPELEDKVBIKLSFGL003");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL003", "main", "namaAtp",
                                "Mengidentifikasi unsur unsur pembangun pertunjukan drama");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL003", "main", "jumlahJpl", "4");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL003", "acp", "idAcp", "ACPELEDKVBIKLSFGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL003", "acp", "namaAcp",
                                "Peserta didik mampu mengevaluasi gagasan dan pandangan berdasarkan kaidah logika berpikir dari membaca berbagai tipe teks (nonfiksi dan fiksi) di media cetak dan elektronik. Peserta didik mampu mengapresiasi teks fiksi dan nonfiksi ");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL003", "elemen", "idElemen", "ELEDKVBIKLSFGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL003", "elemen", "namaElemen", "Membaca dan Memirsa");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL003", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL003", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL003", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL003", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL003", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL003", "kelas", "namaKelas", "XI");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL003", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL003", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL003", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL003", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL003", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL003", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL003", "detail", "createdAt", instant.toString());

                // ATP 4
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL004", "main", "idAtp", "ATPACPELEDKVBIKLSFGL004");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL004", "main", "namaAtp",
                                "Menulis puisi berdasarkan cerpen");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL004", "main", "jumlahJpl", "4");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL004", "acp", "idAcp", "ACPELEDKVBIKLSFGL004");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL004", "acp", "namaAcp",
                                "Peserta didik mampu menulis gagasan,pikiran, pandangan, pengetahuan metakognisi untuk berbagai tujuan secara logis, kritis, dan kreatif. Peserta didik mampu menulis berbagai jenis karya sastra. Peserta didik mampu menulis teks refleksi diri. Peserta didik mampu menulis hasil penelitian, teks fungsional dunia kerja, dan pengembangan studi lanjut. Peserta didik mampu memodifikasi/mendekonstruksikan karya sastra untuk tujuan ekonomi kreatif. Peserta didik mampu menerbitkan tulisan hasil karyanya di media cetak maupun digital.");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL004", "elemen", "idElemen", "ELEDKVBIKLSFGL004");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL004", "elemen", "namaElemen", "Menulis");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL004", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL004", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL004", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL004", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL004", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL004", "kelas", "namaKelas", "XI");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL004", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL004", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL004", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL004", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL004", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL004", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL004", "detail", "createdAt", instant.toString());

                // ATP 5
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL005", "main", "idAtp", "ATPACPELEDKVBIKLSFGL005");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL005", "main", "namaAtp",
                                "D2 Menulis naskah drama berdasarkan cerpen ");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL005", "main", "jumlahJpl", "4");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL005", "acp", "idAcp", "ACPELEDKVBIKLSFGL004");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL005", "acp", "namaAcp",
                                "Peserta didik mampu menulis gagasan,pikiran, pandangan, pengetahuan metakognisi untuk berbagai tujuan secara logis, kritis, dan kreatif. Peserta didik mampu menulis berbagai jenis karya sastra. Peserta didik mampu menulis teks refleksi diri. Peserta didik mampu menulis hasil penelitian, teks fungsional dunia kerja, dan pengembangan studi lanjut. Peserta didik mampu memodifikasi/mendekonstruksikan karya sastra untuk tujuan ekonomi kreatif. Peserta didik mampu menerbitkan tulisan hasil karyanya di media cetak maupun digital.");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL005", "elemen", "idElemen", "ELEDKVBIKLSFGL004");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL005", "elemen", "namaElemen", "Menulis");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL005", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL005", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL005", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL005", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL005", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL005", "kelas", "namaKelas", "XI");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL005", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL005", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL005", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL005", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL005", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL005", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL005", "detail", "createdAt", instant.toString());

                // ATP 6
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL006", "main", "idAtp", "ATPACPELEDKVBIKLSFGL006");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL006", "main",
                                "namaAtp",
                                "Menulis karya ilmiah");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL006", "main", "jumlahJpl", "4");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL006", "acp", "idAcp", "ACPELEDKVBIKLSFGL004");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL006", "acp", "namaAcp",
                                "Peserta didik mampu menulis gagasan,pikiran, pandangan, pengetahuan metakognisi untuk berbagai tujuan secara logis, kritis, dan kreatif. Peserta didik mampu menulis berbagai jenis karya sastra. Peserta didik mampu menulis teks refleksi diri. Peserta didik mampu menulis hasil penelitian, teks fungsional dunia kerja, dan pengembangan studi lanjut. Peserta didik mampu memodifikasi/mendekonstruksikan karya sastra untuk tujuan ekonomi kreatif. Peserta didik mampu menerbitkan tulisan hasil karyanya di media cetak maupun digital.");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL006", "elemen", "idElemen", "ELEDKVBIKLSFGL004");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL006", "elemen", "namaElemen", "Menulis");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL006", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL006", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL006", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL006", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL006", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL006", "kelas", "namaKelas", "XI");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL006", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL006", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL006", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL006", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL006", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL006", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL006", "detail", "createdAt", instant.toString());

                // ATP 7
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL007", "main", "idAtp", "ATPACPELEDKVBIKLSFGL007");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL007", "main", "namaAtp",
                                "Menampilkan musikalisasi puisi");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL007", "main", "jumlahJpl", "4");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL007", "acp", "idAcp", "ACPELEDKVBIKLSFGL003");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL007", "acp", "namaAcp",
                                "Peserta didik mampu menyajikan gagasan, pikiran, dan kreativitas dalam berbahasa dalam bentuk monolog, dialog, dan gelar wicara secara logis, sistematis, kritis, dan kreatif; mampu menyajikan karya sastra secara kreatif dan menarik. Peserta didik mampu mengkreasi teks sesuai dengan norma kesopanan dan budaya Indonesia. Peserta didik mampu menyajikan dan mempertahankan hasil penelitian, serta menyimpulkan masukan dari mitra diskusi.");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL007", "elemen", "idElemen", "ELEDKVBIKLSFGL003");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL007", "elemen", "namaElemen",
                                "Berbicara dan Mempresentasikan");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL007", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL007", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL007", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL007", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL007", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL007", "kelas", "namaKelas", "XI");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL007", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL007", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL007", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL007", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL007", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL007", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL007", "detail", "createdAt", instant.toString());

                // ATP 8
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL008", "main", "idAtp", "ATPACPELEDKVBIKLSFGL008");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL008", "main", "namaAtp",
                                "C1 Menyajikan pertujukan drama");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL008", "main", "jumlahJpl", "4");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL008", "acp", "idAcp", "ACPELEDKVBIKLSFGL003");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL008", "acp", "namaAcp",
                                "Peserta didik mampu menyajikan gagasan, pikiran, dan kreativitas dalam berbahasa dalam bentuk monolog, dialog, dan gelar wicara secara logis, sistematis, kritis, dan kreatif; mampu menyajikan karya sastra secara kreatif dan menarik. Peserta didik mampu mengkreasi teks sesuai dengan norma kesopanan dan budaya Indonesia. Peserta didik mampu menyajikan dan mempertahankan hasil penelitian, serta menyimpulkan masukan dari mitra diskusi.");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL008", "elemen", "idElemen", "ELEDKVBIKLSFGL003");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL008", "elemen", "namaElemen",
                                "Berbicara dan Mempresentasikan");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL008", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL008", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL008", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL008", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL008", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL008", "kelas", "namaKelas", "XI");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL008", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL008", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL008", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL008", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL008", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL008", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL008", "detail", "createdAt", instant.toString());

                // ATP 9
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL009", "main", "idAtp", "ATPACPELEDKVBIKLSFGL009");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL009", "main", "namaAtp",
                                "Menyajikan karya Ilmiah");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL009", "main", "jumlahJpl", "4");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL009", "acp", "idAcp", "ACPELEDKVBIKLSFGL003");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL009", "acp", "namaAcp",
                                "Peserta didik mampu menyajikan gagasan, pikiran, dan kreativitas dalam berbahasa dalam bentuk monolog, dialog, dan gelar wicara secara logis, sistematis, kritis, dan kreatif; mampu menyajikan karya sastra secara kreatif dan menarik. Peserta didik mampu mengkreasi teks sesuai dengan norma kesopanan dan budaya Indonesia. Peserta didik mampu menyajikan dan mempertahankan hasil penelitian, serta menyimpulkan masukan dari mitra diskusi.");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL009", "elemen", "idElemen", "ELEDKVBIKLSFGL003");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL009", "elemen", "namaElemen",
                                "Berbicara dan Mempresentasikan");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL009", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL009", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL009", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL009", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL009", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL009", "kelas", "namaKelas", "XI");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL009", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL009", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL009", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL009", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL009", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL009", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVBIKLSFGL009", "detail", "createdAt", instant.toString());

                // =========================================================================================================================================

                // Insert Data Table ACP untuk Informatikaa Kelas XI Semester Ganjil
                // Mapel Informatikaa

                // Untuk Kelas XI Semester Ganjil maka ada kode khusus untuk ACP
                // ATP = ATP, ACP = ACP, ELE = Elemen, DKV = Desain Komunikasi Visual,
                // IN = Informatikaa, KLS = Kelas, F = Fase / XI, GL = Ganjil
                // Tiga angka terakhir adalah urutan elemen dalam fase tersebut

                // Elemen 1: Berfikir Komputasional 1
                // ATP 1
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL001", "main", "idAtp", "ATPACPELEDKVINKLSEGL001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL001", "main", "namaAtp",
                                "BK1. Peserta didik memahami algoritma pengambilan keputusan untuk pemecahan sebuah masalah. ");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL001", "acp", "idAcp", "ACPELEDKVINKLSEGL001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL001", "acp", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu menerapkan strategi algoritmik standar untuk menghasilkan beberapa solusi persoalan dengan data diskrit bervolume tidak kecil pada kehidupan sehari-hari maupun implementasinya dalam program komputer. ");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL001", "elemen", "idElemen",
                                "ELEDKVINKLSEGL001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL001", "elemen", "namaElemen",
                                "Berpikir Komputasional (BK)");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL001", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL001", "mapel", "name", "Informatika");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL001", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL001", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL001", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL001", "kelas", "namaKelas", "X");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL001", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL001", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL001", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL001", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL001", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL001", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL001", "detail", "createdAt", instant.toString());

                // ATP 2
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL002", "main", "idAtp", "ATPACPELEDKVINKLSEGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL002", "main", "namaAtp",
                                "BK2. Peserta didik mampu menerapkan strategi algoritmik untuk menemukan cara yang paling efisien dalam pemecahan sebuah masalah.");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL002", "acp", "idAcp", "ACPELEDKVINKLSEGL001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL002", "acp", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu menerapkan strategi algoritmik standar untuk menghasilkan beberapa solusi persoalan dengan data diskrit bervolume tidak kecil pada kehidupan sehari-hari maupun implementasinya dalam program komputer. ");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL002", "elemen", "idElemen",
                                "ELEDKVINKLSEGL001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL002", "elemen", "namaElemen",
                                "Berpikir Komputasional (BK)");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL002", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL002", "mapel", "name", "Informatika");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL002", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL002", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL002", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL002", "kelas", "namaKelas", "X");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL002", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL002", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL002", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL002", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL002", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL002", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL002", "detail", "createdAt", instant.toString());

                // ATP 3
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL003", "main", "idAtp", "ATPACPELEDKVINKLSEGL003");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL003", "main", "namaAtp",
                                "BK3. Siswa memahami beberapa algoritma proses sorting.");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL003", "acp", "idAcp", "ACPELEDKVINKLSEGL001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL003", "acp", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu menerapkan strategi algoritmik standar untuk menghasilkan beberapa solusi persoalan dengan data diskrit bervolume tidak kecil pada kehidupan sehari-hari maupun implementasinya dalam program komputer. ");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL003", "elemen", "idElemen",
                                "ELEDKVINKLSEGL001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL003", "elemen", "namaElemen",
                                "Berpikir Komputasional (BK)");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL003", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL003", "mapel", "name", "Informatika");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL003", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL003", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL003", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL003", "kelas", "namaKelas", "X");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL003", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL003", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL003", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL003", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL003", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL003", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL003", "detail", "createdAt", instant.toString());

                // ATP 4
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL004", "main", "idAtp", "ATPACPELEDKVINKLSEGL004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL004", "main", "namaAtp",
                                "BK4. Siswa mampu menerapkan strategi algoritmik untuk menemukan cara yang paling efisien dalam proses sorting.");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL004", "acp", "idAcp", "ACPELEDKVINKLSEGL001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL004", "acp", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu menerapkan strategi algoritmik standar untuk menghasilkan beberapa solusi persoalan dengan data diskrit bervolume tidak kecil pada kehidupan sehari-hari maupun implementasinya dalam program komputer. ");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL004", "elemen", "idElemen",
                                "ELEDKVINKLSEGL001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL004", "elemen", "namaElemen",
                                "Berpikir Komputasional (BK)");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL004", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL004", "mapel", "name", "Informatika");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL004", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL004", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL004", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL004", "kelas", "namaKelas", "X");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL004", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL004", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL004", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL004", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL004", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL004", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL004", "detail", "createdAt", instant.toString());

                // ATP 5
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL005", "main", "idAtp", "ATPACPELEDKVINKLSEGL005");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL005", "main", "namaAtp",
                                "BK5. Siswa memahami konsep struktur data stack dan queue serta operasi-operasi yang dapat dikenakan pada struktur data tersebut.");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL005", "acp", "idAcp", "ACPELEDKVINKLSEGL001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL005", "acp", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu menerapkan strategi algoritmik standar untuk menghasilkan beberapa solusi persoalan dengan data diskrit bervolume tidak kecil pada kehidupan sehari-hari maupun implementasinya dalam program komputer. ");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL005", "elemen", "idElemen",
                                "ELEDKVINKLSEGL001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL005", "elemen", "namaElemen",
                                "Berpikir Komputasional (BK)");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL005", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL005", "mapel", "name", "Informatika");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL005", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL005", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL005", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL005", "kelas", "namaKelas", "X");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL005", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL005", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL005", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL005", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL005", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL005", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL005", "detail", "createdAt", instant.toString());

                // ATP 6
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL006", "main", "idAtp", "ATPACPELEDKVINKLSEGL006");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL006", "main", "namaAtp",
                                "BK6. Siswa mampu mengenali pemanfaatan stack dan queue dalam persoalan sehari-hari.");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL006", "acp", "idAcp", "ACPELEDKVINKLSEGL001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL006", "acp", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu menerapkan strategi algoritmik standar untuk menghasilkan beberapa solusi persoalan dengan data diskrit bervolume tidak kecil pada kehidupan sehari-hari maupun implementasinya dalam program komputer. ");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL006", "elemen", "idElemen",
                                "ELEDKVINKLSEGL001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL006", "elemen", "namaElemen",
                                "Berpikir Komputasional (BK)");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL006", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL006", "mapel", "name", "Informatika");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL006", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL006", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL006", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL006", "kelas", "namaKelas", "X");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL006", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL006", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL006", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL006", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL006", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL006", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL006", "detail", "createdAt", instant.toString());

                // ATP 7
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL007", "main", "idAtp", "ATPACPELEDKVINKLSEGL007");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL007", "main", "namaAtp",
                                "TIK1. Peserta didik mampu memahami serta menjelaskan tentang Teknologi Informasi dan Komunikasi serta pemanfaatannya.");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL007", "acp", "idAcp", "ACPELEDKVINKLSEGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL007", "acp", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu memanfaatkan berbagai aplikasi secara bersamaan dan optimal untuk berkomunikasi, mencari sumber data yang akan diolah menjadi informasi, baik di dunia nyata maupun di internet, serta mahir menggunakan fitur lanjut aplikasi perkantoran (pengolah kata, angka, dan presentasi) beserta otomasinya untuk mengintegrasikan dan menyajikan konten aplikasi dalam berbagai representasi yang memudahkan analisis dan interpretasi konten tersebut");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL007", "elemen", "idElemen", "ELEDKVINKLSEGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL007", "elemen", "namaElemen",
                                "Teknologi Informasi dan Komunikasi (TIK)");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL007", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL007", "mapel", "name", "Informatika");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL007", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL007", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL007", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL007", "kelas", "namaKelas", "X");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL007", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL007", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL007", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL007", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL007", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL007", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL007", "detail", "createdAt", instant.toString());

                // ATP 8
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL008", "main", "idAtp", "ATPACPELEDKVINKLSEGL008");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL008", "main", "namaAtp",
                                "TIK2. Peserta didik mampu memahami Aplikasi Video Conference (Google Meet).");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL008", "acp", "idAcp", "ACPELEDKVINKLSEGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL008", "acp", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu memanfaatkan berbagai aplikasi secara bersamaan dan optimal untuk berkomunikasi, mencari sumber data yang akan diolah menjadi informasi, baik di dunia nyata maupun di internet, serta mahir menggunakan fitur lanjut aplikasi perkantoran (pengolah kata, angka, dan presentasi) beserta otomasinya untuk mengintegrasikan dan menyajikan konten aplikasi dalam berbagai representasi yang memudahkan analisis dan interpretasi konten tersebut");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL008", "elemen", "idElemen", "ELEDKVINKLSEGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL008", "elemen", "namaElemen",
                                "Teknologi Informasi dan Komunikasi (TIK)");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL008", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL008", "mapel", "name", "Informatika");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL008", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL008", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL008", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL008", "kelas", "namaKelas", "X");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL008", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL008", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL008", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL008", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL008", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL008", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL008", "detail", "createdAt", instant.toString());

                // ATP 9
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL009", "main", "idAtp", "ATPACPELEDKVINKLSEGL009");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL009", "main", "namaAtp",
                                "TIK3. Peserta didik mampu memahami konsep aplikasi peyimpanan Awan/Cloud (Google Drive) ");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL009", "acp", "idAcp", "ACPELEDKVINKLSEGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL009", "acp", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu memanfaatkan berbagai aplikasi secara bersamaan dan optimal untuk berkomunikasi, mencari sumber data yang akan diolah menjadi informasi, baik di dunia nyata maupun di internet, serta mahir menggunakan fitur lanjut aplikasi perkantoran (pengolah kata, angka, dan presentasi) beserta otomasinya untuk mengintegrasikan dan menyajikan konten aplikasi dalam berbagai representasi yang memudahkan analisis dan interpretasi konten tersebut");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL009", "elemen", "idElemen", "ELEDKVINKLSEGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL009", "elemen", "namaElemen",
                                "Teknologi Informasi dan Komunikasi (TIK)");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL009", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL009", "mapel", "name", "Informatika");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL009", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL009", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL009", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL009", "kelas", "namaKelas", "X");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL009", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL009", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL009", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL009", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL009", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL009", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL009", "detail", "createdAt", instant.toString());

                // ATP 10
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL010", "main", "idAtp", "ATPACPELEDKVINKLSEGL010");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL010", "main", "namaAtp",
                                "TIK4. Peserta didik mampu membuat akun serta menggunakan Produk Google");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL010", "acp", "idAcp", "ACPELEDKVINKLSEGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL010", "acp", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu memanfaatkan berbagai aplikasi secara bersamaan dan optimal untuk berkomunikasi, mencari sumber data yang akan diolah menjadi informasi, baik di dunia nyata maupun di internet, serta mahir menggunakan fitur lanjut aplikasi perkantoran (pengolah kata, angka, dan presentasi) beserta otomasinya untuk mengintegrasikan dan menyajikan konten aplikasi dalam berbagai representasi yang memudahkan analisis dan interpretasi konten tersebut");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL010", "elemen", "idElemen", "ELEDKVINKLSEGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL010", "elemen", "namaElemen",
                                "Teknologi Informasi dan Komunikasi (TIK)");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL010", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL010", "mapel", "name", "Informatika");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL010", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL010", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL010", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL010", "kelas", "namaKelas", "X");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL010", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL010", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL010", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL010", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL010", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL010", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL010", "detail", "createdAt", instant.toString());

                // ATP 11
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL011", "main", "idAtp", "ATPACPELEDKVINKLSEGL011");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL011", "main", "namaAtp",
                                "TIK5. Peserta didik mampu memahami dan menggunakan Aplikasi Microsoft Word (Memahami pengertian Microsoft Word, Memahami prosedur pengoperasian Microsoft Word, Memahami menu-menu pada Microsoft Word, Memahami fungsi Hotkey/ shortcut pada Microsoft Word dan membuat Daftar isi Otomatis).");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL011", "acp", "idAcp", "ACPELEDKVINKLSEGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL011", "acp", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu memanfaatkan berbagai aplikasi secara bersamaan dan optimal untuk berkomunikasi, mencari sumber data yang akan diolah menjadi informasi, baik di dunia nyata maupun di internet, serta mahir menggunakan fitur lanjut aplikasi perkantoran (pengolah kata, angka, dan presentasi) beserta otomasinya untuk mengintegrasikan dan menyajikan konten aplikasi dalam berbagai representasi yang memudahkan analisis dan interpretasi konten tersebut");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL011", "elemen", "idElemen", "ELEDKVINKLSEGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL011", "elemen", "namaElemen",
                                "Teknologi Informasi dan Komunikasi (TIK)");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL011", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL011", "mapel", "name", "Informatika");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL011", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL011", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL011", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL011", "kelas", "namaKelas", "X");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL011", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL011", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL011", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL011", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL011", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL011", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL011", "detail", "createdAt", instant.toString());

                // ATP 12
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL012", "main", "idAtp", "ATPACPELEDKVINKLSEGL012");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL012", "main", "namaAtp",
                                "TIK6. Peserta didik mampu Memahami pengertian dari aplikasi Microsoft Excel, Mengetahui fitur pada Microsoft Excel, Membuat table pada Microsoft Excel, membuat format data table pada Microsoft Excel serta Shortcut Microsoft Excel");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL012", "acp", "idAcp", "ACPELEDKVINKLSEGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL012", "acp", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu memanfaatkan berbagai aplikasi secara bersamaan dan optimal untuk berkomunikasi, mencari sumber data yang akan diolah menjadi informasi, baik di dunia nyata maupun di internet, serta mahir menggunakan fitur lanjut aplikasi perkantoran (pengolah kata, angka, dan presentasi) beserta otomasinya untuk mengintegrasikan dan menyajikan konten aplikasi dalam berbagai representasi yang memudahkan analisis dan interpretasi konten tersebut");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL012", "elemen", "idElemen", "ELEDKVINKLSEGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL012", "elemen", "namaElemen",
                                "Teknologi Informasi dan Komunikasi (TIK)");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL012", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL012", "mapel", "name", "Informatika");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL012", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL012", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL012", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL012", "kelas", "namaKelas", "X");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL012", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL012", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL012", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL012", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL012", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL012", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL012", "detail", "createdAt", instant.toString());

                // ATP 13
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL013", "main", "idAtp", "ATPACPELEDKVINKLSEGL013");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL013", "main", "namaAtp",
                                "TIK7. Peserta didik mampu memahami dan mengenali Microsoft Powerpoint, menyebutkan dan mendiskripsikan menu atau icon yang terdapat pada Microsoft Powerpoint, mengetahui fungsi icon atau menu pada Microsoft Powerpoint, menjalankan perintah- perintah pengelolaan file, mengoperasikan aplikasi Microsoft Powerpoint (Membuat slide presentasi sederhana) ");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL013", "acp", "idAcp", "ACPELEDKVINKLSEGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL013", "acp", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu memanfaatkan berbagai aplikasi secara bersamaan dan optimal untuk berkomunikasi, mencari sumber data yang akan diolah menjadi informasi, baik di dunia nyata maupun di internet, serta mahir menggunakan fitur lanjut aplikasi perkantoran (pengolah kata, angka, dan presentasi) beserta otomasinya untuk mengintegrasikan dan menyajikan konten aplikasi dalam berbagai representasi yang memudahkan analisis dan interpretasi konten tersebut");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL013", "elemen", "idElemen", "ELEDKVINKLSEGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL013", "elemen", "namaElemen",
                                "Teknologi Informasi dan Komunikasi (TIK)");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL013", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL013", "mapel", "name", "Informatika");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL013", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL013", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL013", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL013", "kelas", "namaKelas", "X");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL013", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL013", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL013", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL013", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL013", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL013", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL013", "detail", "createdAt", instant.toString());

                // ATP 14
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL014", "main", "idAtp", "ATPACPELEDKVINKLSEGL014");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL014", "main", "namaAtp",
                                "TIK8. Mengidentifikasi dan menggunakan perangkat fotografi dan videografi.");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL014", "acp", "idAcp", "ACPELEDKVINKLSEGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL014", "acp", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu memanfaatkan berbagai aplikasi secara bersamaan dan optimal untuk berkomunikasi, mencari sumber data yang akan diolah menjadi informasi, baik di dunia nyata maupun di internet, serta mahir menggunakan fitur lanjut aplikasi perkantoran (pengolah kata, angka, dan presentasi) beserta otomasinya untuk mengintegrasikan dan menyajikan konten aplikasi dalam berbagai representasi yang memudahkan analisis dan interpretasi konten tersebut");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL014", "elemen", "idElemen", "ELEDKVINKLSEGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL014", "elemen", "namaElemen",
                                "Teknologi Informasi dan Komunikasi (TIK)");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL014", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL014", "mapel", "name", "Informatika");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL014", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL014", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL014", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL014", "kelas", "namaKelas", "X");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL014", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL014", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL014", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL014", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL014", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL014", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL014", "detail", "createdAt", instant.toString());

                // ATP 15
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL015", "main", "idAtp", "ATPACPELEDKVINKLSEGL015");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL015", "main", "namaAtp",
                                "TIK11. Menggunakan aplikasi editing video ");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL015", "acp", "idAcp", "ACPELEDKVINKLSEGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL015", "acp", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu memanfaatkan berbagai aplikasi secara bersamaan dan optimal untuk berkomunikasi, mencari sumber data yang akan diolah menjadi informasi, baik di dunia nyata maupun di internet, serta mahir menggunakan fitur lanjut aplikasi perkantoran (pengolah kata, angka, dan presentasi) beserta otomasinya untuk mengintegrasikan dan menyajikan konten aplikasi dalam berbagai representasi yang memudahkan analisis dan interpretasi konten tersebut");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL015", "elemen", "idElemen", "ELEDKVINKLSEGL002");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL015", "elemen", "namaElemen",
                                "Teknologi Informasi dan Komunikasi (TIK)");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL015", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL015", "mapel", "name", "Informatika");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL015", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL015", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL015", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL015", "kelas", "namaKelas", "X");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL015", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL015", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL015", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL015", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL015", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL015", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL015", "detail", "createdAt", instant.toString());

                // ATP 16
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL016", "main", "idAtp", "ATPACPELEDKVINKLSEGL016");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL016", "main", "namaAtp",
                                "SK1. Peserta didik mampu mengetahui dan memahami sejarah Operating system Komputer (Windows, Linux & Mac Os) & Mobile (Android & IOS).");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL016", "acp", "idAcp", "ACPELEDKVINKLSEGL003");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL016", "acp", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu memahami peran sistem operasi dan mekanisme internal yang terjadi pada interaksi antara perangkat keras, perangkat lunak, dan pengguna.");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL016", "elemen", "idElemen", "ELEDKVINKLSEGL003");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL016", "elemen", "namaElemen",
                                "Sistem Komputer (SK)");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL016", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL016", "mapel", "name", "Informatika");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL016", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL016", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL016", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL016", "kelas", "namaKelas", "X");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL016", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL016", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL016", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL016", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL016", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL016", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL016", "detail", "createdAt", instant.toString());

                // ATP 17
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL017", "main", "idAtp", "ATPACPELEDKVINKLSEGL017");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL017", "main", "namaAtp",
                                "SK3. Peserta didik mampu menggunakan Printer (menyalakan, mencetak, mematikan serta Troubleshooting).");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL017", "acp", "idAcp", "ACPELEDKVINKLSEGL003");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL017", "acp", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu memahami peran sistem operasi dan mekanisme internal yang terjadi pada interaksi antara perangkat keras, perangkat lunak, dan pengguna.");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL017", "elemen", "idElemen", "ELEDKVINKLSEGL003");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL017", "elemen", "namaElemen",
                                "Sistem Komputer (SK)");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL017", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL017", "mapel", "name", "Informatika");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL017", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL017", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL017", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL017", "kelas", "namaKelas", "X");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL017", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL017", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL017", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL017", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL017", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL017", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL017", "detail", "createdAt", instant.toString());

                // ATP 18
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL018", "main", "idAtp", "ATPACPELEDKVINKLSEGL018");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL018", "main", "namaAtp",
                                "SK4. Peserta didik mampu menggunakan Scanner (menyalakan, scan, mematikan serta Troubleshooting).");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL018", "acp", "idAcp", "ACPELEDKVINKLSEGL003");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL018", "acp", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu memahami peran sistem operasi dan mekanisme internal yang terjadi pada interaksi antara perangkat keras, perangkat lunak, dan pengguna.");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL018", "elemen", "idElemen", "ELEDKVINKLSEGL003");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL018", "elemen", "namaElemen",
                                "Sistem Komputer (SK)");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL018", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL018", "mapel", "name", "Informatika");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL018", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL018", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL018", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL018", "kelas", "namaKelas", "X");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL018", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL018", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL018", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL018", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL018", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL018", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL018", "detail", "createdAt", instant.toString());

                // ATP 19
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL019", "main", "idAtp", "ATPACPELEDKVINKLSEGL019");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL019", "main", "namaAtp",
                                "SK5. Peserta didik mampu menggunakan Joystick");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL019", "acp", "idAcp", "ACPELEDKVINKLSEGL003");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL019", "acp", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu memahami peran sistem operasi dan mekanisme internal yang terjadi pada interaksi antara perangkat keras, perangkat lunak, dan pengguna.");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL019", "elemen", "idElemen", "ELEDKVINKLSEGL003");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL019", "elemen", "namaElemen",
                                "Sistem Komputer (SK)");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL019", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL019", "mapel", "name", "Informatika");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL019", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL019", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL019", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL019", "kelas", "namaKelas", "X");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL019", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL019", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL019", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL019", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL019", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL019", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL019", "detail", "createdAt", instant.toString());

                // ATP 20
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL020", "main", "idAtp", "ATPACPELEDKVINKLSEGL020");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL020", "main", "namaAtp",
                                "SK6. Peserta didik mampu menggunakan Webcam (menyalakan, menggunakan serta mematikan), dan menggunakan Wacom.");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL020", "acp", "idAcp", "ACPELEDKVINKLSEGL003");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL020", "acp", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu memahami peran sistem operasi dan mekanisme internal yang terjadi pada interaksi antara perangkat keras, perangkat lunak, dan pengguna.");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL020", "elemen", "idElemen", "ELEDKVINKLSEGL003");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL020", "elemen", "namaElemen",
                                "Sistem Komputer (SK)");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL020", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL020", "mapel", "name", "Informatika");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL020", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL020", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL020", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL020", "kelas", "namaKelas", "X");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL020", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL020", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL020", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL020", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL020", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL020", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL020", "detail", "createdAt", instant.toString());

                // ATP 21
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL021", "main", "idAtp", "ATPACPELEDKVINKLSEGL021");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL021", "main", "namaAtp",
                                "JKI1. Peserta didik mampu memahami perbedaan jaringan lokal dan internet dan jenis-jenis konektivitas internet melalui jaringan kabel dan nirkabel.");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL021", "acp", "idAcp", "ACPELEDKVINKLSEGL004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL021", "acp", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu menerapkan konektivitas jaringan lokal, komunikasi data via ponsel, konektivitas internet melalui jaringan kabel dan nirkabel (bluetooth, wifi, internet), enkripsi untuk memproteksi data pada saat melakukan penyambungan perangkat ke jaringan lokal maupun internet yang tersedia. ");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL021", "elemen", "idElemen", "ELEDKVINKLSEGL004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL021", "elemen", "namaElemen",
                                "Jaringan Komputer dan Internet (JKI)");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL021", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL021", "mapel", "name", "Informatika");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL021", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL021", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL021", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL021", "kelas", "namaKelas", "X");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL021", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL021", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL021", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL021", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL021", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL021", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL021", "detail", "createdAt", instant.toString());

                // ATP 22
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL022", "main", "idAtp", "ATPACPELEDKVINKLSEGL022");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL022", "main", "namaAtp",
                                "JKI2. Peserta didik mampu memahami teknologi komunikasi untuk keperluan komunikasi data via HP. ");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL022", "acp", "idAcp", "ACPELEDKVINKLSEGL004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL022", "acp", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu menerapkan konektivitas jaringan lokal, komunikasi data via ponsel, konektivitas internet melalui jaringan kabel dan nirkabel (bluetooth, wifi, internet), enkripsi untuk memproteksi data pada saat melakukan penyambungan perangkat ke jaringan lokal maupun internet yang tersedia. ");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL022", "elemen", "idElemen", "ELEDKVINKLSEGL004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL022", "elemen", "namaElemen",
                                "Jaringan Komputer dan Internet (JKI)");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL022", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL022", "mapel", "name", "Informatika");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL022", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL022", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL022", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL022", "kelas", "namaKelas", "X");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL022", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL022", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL022", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL022", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL022", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL022", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL022", "detail", "createdAt", instant.toString());

                // ATP 23
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL023", "main", "idAtp", "ATPACPELEDKVINKLSEGL023");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL023", "main", "namaAtp",
                                "JJKI3. Peserta didik mampu memahami pentingnya proteksi data pribadi saat terhubung ke jaringan internet serta menerapkan enkripsi untuk memproteksi dokumen. ");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL023", "acp", "idAcp", "ACPELEDKVINKLSEGL004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL023", "acp", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu menerapkan konektivitas jaringan lokal, komunikasi data via ponsel, konektivitas internet melalui jaringan kabel dan nirkabel (bluetooth, wifi, internet), enkripsi untuk memproteksi data pada saat melakukan penyambungan perangkat ke jaringan lokal maupun internet yang tersedia. ");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL023", "elemen", "idElemen", "ELEDKVINKLSEGL004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL023", "elemen", "namaElemen",
                                "Jaringan Komputer dan Internet (JKI)");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL023", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL023", "mapel", "name", "Informatika");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL023", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL023", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL023", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL023", "kelas", "namaKelas", "X");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL023", "semester", "idSemester", "SM001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL023", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL023", "school", "idSchool", "RWK001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL023", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL023", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL023", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAtp, "ATPACPELEDKVINKLSEGL023", "detail", "createdAt", instant.toString());

        }

}
