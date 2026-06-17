package create_structure.MasterDataSeeder.DKV;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import create_structure.HBaseCustomClient;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SeederDataACP {

        public static void main(String[] args) throws IOException {
                Configuration conf = HBaseConfiguration.create();
                HBaseCustomClient client = new HBaseCustomClient(conf);

                // Waktu sekarang
                ZoneId zoneId = ZoneId.of("Asia/Jakarta");
                ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
                Instant instant = zonedDateTime.toInstant();

                // Tabel Elemen
                TableName tableAcp = TableName.valueOf("acp");

                // Insert Data Table ACP untuk Bahasa Indonesia Kelas XI Semester Ganjil
                // Mapel Bahasa Indonesia

                // Untuk Kelas XI Semester Ganjil maka ada kode khusus untuk ACP
                // ACP = ACP, ELE = Elemen, DKV = Desain Komunikasi Visual, BI = Bahasa
                // Indonesia, KLS =
                // Kelas, F = Fase / XI, GL = Ganjil
                // Tiga angka terakhir adalah urutan elemen dalam fase tersebut

                // Elemen 1: Menyimak
                // ACP 1
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL001", "main", "idAcp", "ACPELEDKVBIKLSFGL001");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL001", "main", "namaAcp",
                                "Peserta didik mampu mengevaluasi berbagai gagasan dan pandangan berdasarkan kaidah logika berpikir dari menyimak berbagai jenis teks (nonfiksi dan fiksi) dalam bentuk monolog, dialog, dan gelar wicara; mengkreasi dan mengapresiasi gagasan dan pendapat untuk menanggapi teks yang disimak. ");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL001", "school", "idSchool", "RWK001");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL001", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL001", "elemen", "idElemen", "ELEDKVBIKLSFGL001");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL001", "elemen", "namaElemen", "Menyimak");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL001", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL001", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL001", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL001", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL001", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL001", "kelas", "namaKelas", "XI");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL001", "semester", "idSemester", "SM001");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL001", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL001", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL001", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL001", "detail", "createdAt", instant.toString());

                // Elemen 2: Membaca dan Memirsa
                // ACP 2
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL002", "main", "idAcp", "ACPELEDKVBIKLSFGL002");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL002", "main", "namaAcp",
                                "Peserta didik mampu mengevaluasi gagasan dan pandangan berdasarkan kaidah logika berpikir dari membaca berbagai tipe teks (nonfiksi dan fiksi) di media cetak dan elektronik. Peserta didik mampu mengapresiasi teks fiksi dan nonfiksi ");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL002", "school", "idSchool", "RWK001");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL002", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL002", "elemen", "idElemen", "ELEDKVBIKLSFGL002");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL002", "elemen", "namaElemen", "Membaca dan Memirsa");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL002", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL002", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL002", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL002", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL002", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL002", "kelas", "namaKelas", "XI");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL002", "semester", "idSemester", "SM001");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL002", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL002", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL002", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL002", "detail", "createdAt", instant.toString());

                // Elemen 3: Berbicara dan Mepresentasikan
                // ACP 3
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL003", "main", "idAcp", "ACPELEDKVBIKLSFGL003");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL003", "main", "namaAcp",
                                "Peserta didik mampu menyajikan gagasan, pikiran, dan kreativitas dalam berbahasa dalam bentuk monolog, dialog, dan gelar wicara secara logis, sistematis, kritis, dan kreatif; mampu menyajikan karya sastra secara kreatif dan menarik. Peserta didik mampu mengkreasi teks sesuai dengan norma kesopanan dan budaya Indonesia. Peserta didik mampu menyajikan dan mempertahankan hasil penelitian, serta menyimpulkan masukan dari mitra diskusi.");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL003", "school", "idSchool", "RWK001");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL003", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL003", "elemen", "idElemen", "ELEDKVBIKLSFGL003");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL003", "elemen", "namaElemen", "Membaca dan Memirsa");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL003", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL003", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL003", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL003", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL003", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL003", "kelas", "namaKelas", "XI");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL003", "semester", "idSemester", "SM001");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL003", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL003", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL003", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL003", "detail", "createdAt", instant.toString());

                // Elemen 4: Menulis
                // ACP 4
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL004", "main", "idAcp", "ACPELEDKVBIKLSFGL004");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL004", "main", "namaAcp",
                                "Peserta didik mampu menulis gagasan,pikiran, pandangan, pengetahuan metakognisi untuk berbagai tujuan secara logis, kritis, dan kreatif. Peserta didik mampu menulis berbagai jenis karya sastra. Peserta didik mampu menulis teks refleksi diri. Peserta didik mampu menulis hasil penelitian, teks fungsional dunia kerja, dan pengembangan studi lanjut. Peserta didik mampu memodifikasi/mendekonstruksikan karya sastra untuk tujuan ekonomi kreatif. Peserta didik mampu menerbitkan tulisan hasil karyanya di media cetak maupun digital.");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL004", "school", "idSchool", "RWK001");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL004", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL004", "elemen", "idElemen", "ELEDKVBIKLSFGL004");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL004", "elemen", "namaElemen", "Menulis");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL004", "mapel", "idMapel", "MAP029");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL004", "mapel", "name", "Bahasa Indonesia");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL004", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL004", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL004", "kelas", "idKelas", "KLS002");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL004", "kelas", "namaKelas", "XI");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL004", "semester", "idSemester", "SM001");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL004", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL004", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL004", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAcp, "ACPELEDKVBIKLSFGL004", "detail", "createdAt", instant.toString());

                // ======================================================================================================================

                // Insert Data Table ACP untuk Informatika Kelas X Semester Ganjil Mapel

                // Untuk Kelas XI Semester Ganjil maka ada kode khusus untuk ACP
                // ACP = ACP, ELE = Elemen, DKV = Desain Komunikasi Visual, BI = Bahasa
                // Indonesia, KLS =
                // Kelas, F = Fase / XI, GL = Ganjil
                // Tiga angka terakhir adalah urutan elemen dalam fase tersebut

                // ACP Informatika 1
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL001", "main", "idAcp", "ACPELEDKVINKLSEGL001");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL001", "main", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu menerapkan strategi algoritmik standar untuk menghasilkan beberapa solusi persoalan dengan data diskrit bervolume tidak kecil pada kehidupan sehari-hari maupun implementasinya dalam program komputer. ");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL001", "school", "idSchool", "RWK001");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL001", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL001", "elemen", "idElemen", "ELEDKVINKLSEGL001");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL001", "elemen", "namaElemen",
                                "Berpikir Komputasional (BK)");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL001", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL001", "mapel", "name", "Informatika");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL001", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL001", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL001", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL001", "kelas", "namaKelas", "X");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL001", "semester", "idSemester", "SM001");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL001", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL001", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL001", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL001", "detail", "createdAt", instant.toString());

                // ACP Informatika 2
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL002", "main", "idAcp", "ACPELEDKVINKLSEGL002");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL002", "main", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu memanfaatkan berbagai aplikasi secara bersamaan dan optimal untuk berkomunikasi, mencari sumber data yang akan diolah menjadi informasi, baik di dunia nyata maupun di internet, serta mahir menggunakan fitur lanjut aplikasi perkantoran (pengolah kata, angka, dan presentasi) beserta otomasinya untuk mengintegrasikan dan menyajikan konten aplikasi dalam berbagai representasi yang memudahkan analisis dan interpretasi konten tersebut");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL002", "school", "idSchool", "RWK001");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL002", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL002", "elemen", "idElemen", "ELEDKVINKLSEGL002");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL002", "elemen", "namaElemen",
                                "Teknologi Informasi dan Komunikasi (TIK)");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL002", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL002", "mapel", "name", "Informatika");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL002", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL002", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL002", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL002", "kelas", "namaKelas", "X");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL002", "semester", "idSemester", "SM001");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL002", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL002", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL002", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL002", "detail", "createdAt", instant.toString());

                // ACP Informatika 3
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL003", "main", "idAcp", "ACPELEDKVINKLSEGL003");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL003", "main", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu memahami peran sistem operasi dan mekanisme internal yang terjadi pada interaksi antara perangkat keras, perangkat lunak, dan pengguna.");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL003", "school", "idSchool", "RWK001");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL003", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL003", "elemen", "idElemen", "ELEDKVINKLSEGL003");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL003", "elemen", "namaElemen", "Sistem Komputer (SK)");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL003", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL003", "mapel", "name", "Informatika");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL003", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL003", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL003", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL003", "kelas", "namaKelas", "X");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL003", "semester", "idSemester", "SM001");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL003", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL003", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL003", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL003", "detail", "createdAt", instant.toString());

                // ACP Informatika 4
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL004", "main", "idAcp", "ACPELEDKVINKLSEGL004");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL004", "main", "namaAcp",
                                "Pada akhir fase E, peserta didik mampu menerapkan konektivitas jaringan lokal, komunikasi data via ponsel, konektivitas internet melalui jaringan kabel dan nirkabel (bluetooth, wifi, internet), enkripsi untuk memproteksi data pada saat melakukan penyambungan perangkat ke jaringan lokal maupun internet yang tersedia. ");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL004", "school", "idSchool", "RWK001");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL004", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL004", "elemen", "idElemen", "ELEDKVINKLSEGL004");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL004", "elemen", "namaElemen",
                                "Jaringan Komputer dan Internet (JKI)");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL004", "mapel", "idMapel", "MAP004");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL004", "mapel", "name", "Informatika");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL004", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL004", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL004", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL004", "kelas", "namaKelas", "X");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL004", "semester", "idSemester", "SM001");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL004", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL004", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL004", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAcp, "ACPELEDKVINKLSEGL004", "detail", "createdAt", instant.toString());

                // ======================================================================================================================

                // Insert Data Table ACP untuk Informatika Kelas X Semester Ganjil Mapel

                // Untuk Kelas XI Semester Ganjil maka ada kode khusus untuk ACP
                // ACP = ACP, ELE = Elemen, DKV = Desain Komunikasi Visual, BI = Bahasa
                // Indonesia, KLS =
                // Kelas, F = Fase / XI, GL = Ganjil
                // Tiga angka terakhir adalah urutan elemen dalam fase tersebut

                // ACP Matematika 1
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL005", "main", "idAcp", "ACPELEDKVMMKLSEGL005");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL005", "main", "namaAcp",
                                "Peserta didik dapat menggeneralisasi sifat-sifat bilangan berpangkat (termasuk bilangan pangkat pecahan). Mereka dapat menerapkan barisan dan deret aritmetika dan geometri, termasuk masalah yang terkait bunga tunggal dan bunga majemuk.");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL005", "school", "idSchool", "RWK001");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL005", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL005", "elemen", "idElemen", "ELEDKVMMKLSEGL005");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL005", "elemen", "namaElemen", "Bilangan");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL005", "mapel", "idMapel", "MAP005");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL005", "mapel", "name", "Matematika");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL005", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL005", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL005", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL005", "kelas", "namaKelas", "X");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL005", "semester", "idSemester", "SM001");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL005", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL005", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL005", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL005", "detail", "createdAt", instant.toString());

                // ACP Matematika 2
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL006", "main", "idAcp", "ACPELEDKVMMKLSEGL006");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL006", "main", "namaAcp",
                                "Peserta didik dapat menyelesaikan masalah yang berkaitan dengan sistem persamaan linear tiga variabel dan system pertidaksamaan linear dua variabel. Mereka dapat menyelesaikan masalah yang berkaitan dengan persamaan dan fungsi kuadrat (termasuk akar imajiner), serta persamaan eksponensial (berbasis/ bilangan pokok sama) dan fungsi eksponensial. ");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL006", "school", "idSchool", "RWK001");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL006", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL006", "elemen", "idElemen", "ELEDKVMMKLSEGL006");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL006", "elemen", "namaElemen", "Aljabar and Fungsi");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL006", "mapel", "idMapel", "MAP005");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL006", "mapel", "name", "Matematika");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL006", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL006", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL006", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL006", "kelas", "namaKelas", "X");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL006", "semester", "idSemester", "SM001");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL006", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL006", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL006", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL006", "detail", "createdAt", instant.toString());

                // ACP Matematika 3
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL007", "main", "idAcp", "ACPELEDKVMMKLSEGL007");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL007", "main", "namaAcp",
                                "Peserta didik dapat menyelesaikan permasalahan segitiga siku-siku yang melibatkan perbandingan trigonometri dan aplikasinya.");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL007", "school", "idSchool", "RWK001");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL007", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL007", "elemen", "idElemen", "ELEDKVMMKLSEGL007");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL007", "elemen", "namaElemen", "Geometri");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL007", "mapel", "idMapel", "MAP005");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL007", "mapel", "name", "Matematika");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL007", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL007", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL007", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL007", "kelas", "namaKelas", "X");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL007", "semester", "idSemester", "SM001");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL007", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL007", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL007", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL007", "detail", "createdAt", instant.toString());

                // ACP Matematika 4
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL008", "main", "idAcp", "ACPELEDKVMMKLSEGL008");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL008", "main", "namaAcp",
                                "Peserta didik dapat merepresentasikan dan menginterpretasi data dengan cara menentukan jangkauan kuartil dan interkuartil. Mereka dapat membuat dan menginterpretasi diagram box plot (box-and whisker plot) dan menggunakannya untuk membandingkan himpunan data. Mereka dapat menentukan dan menggunakan dari box plot, histogram dan dot plot sesuai dengan natur (karakteristik) data dan kebutuhan. Mereka dapat menggunakan diagram pencar untuk menyelidiki dan menjelaskan hubungan antara dua variabel numerik / kuantitatif (termasuk salah satunya variabel bebas berupa waktu). Mereka dapat mengevaluasi laporan statistika di media berdasarkan tampilan, statistika dan representasi data. Peserta didik dapat menjelaskan peluang dan menentukan frekuensi harapan dari kejadian majemuk. Mereka menyelidiki konsep dari kejadian saling bebas dan saling lepas, dan menentukan peluangnya. ");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL008", "school", "idSchool", "RWK001");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL008", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL008", "elemen", "idElemen", "ELEDKVMMKLSEGL008");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL008", "elemen", "namaElemen",
                                "Analisis Data dan Peluang");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL008", "mapel", "idMapel", "MAP005");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL008", "mapel", "name", "Matematika");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL008", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL008", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL008", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL008", "kelas", "namaKelas", "X");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL008", "semester", "idSemester", "SM001");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL008", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL008", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL008", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAcp, "ACPELEDKVMMKLSEGL008", "detail", "createdAt", instant.toString());

                // ======================================================================================================================

                // Insert Data Table ACP untuk Informatika Kelas X Semester Ganjil Mapel

                // Untuk Kelas XI Semester Ganjil maka ada kode khusus untuk ACP
                // ACP = ACP, ELE = Elemen, DKV = Desain Komunikasi Visual, BI = Bahasa
                // Indonesia, KLS =
                // Kelas, F = Fase / XI, GL = Ganjil
                // Tiga angka terakhir adalah urutan elemen dalam fase tersebut

                // ACP Ilmu Pengetahuan Alam Sosial
                // ACP 1
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL009", "main", "idAcp", "ACPELEDKVIPKLSEGL009");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL009", "main", "namaAcp",
                                "Peserta didik mampu memahami pengetahuan ilmiah dan menerapkannya atau membuat prediksi sederhana disertai dengan pembuktian fenomena-fenomena yang terjadi di lingkungan sekitarnya dilihat dari berbagai aspek seperti makhluk hidup dan lingkungannya, zat dan perubahannya, energi dan perubahannya, bumi dan antariksa, keruangan dan konektivitas antarruang dan antarwaktu, interaksi, komunikasi, sosialisasi, institusi sosial dan dinamika sosial, serta perilaku ekonomi dan kesejahteraan dan mengaitkan fenomena-fenomena tersebut dengan keterampilan teknis pada bidang keahliannya.");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL009", "school", "idSchool", "RWK001");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL009", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL009", "elemen", "idElemen", "ELEDKVIPKLSEGL009");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL009", "elemen", "namaElemen",
                                "Menjelaskan Fenomena Secara Ilmiah");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL009", "mapel", "idMapel", "MAP011");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL009", "mapel", "name",
                                "Projek Ilmu Pengetahuan Alam dan Sosial");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL009", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL009", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL009", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL009", "kelas", "namaKelas", "X");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL009", "semester", "idSemester", "SM001");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL009", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL009", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL009", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL009", "detail", "createdAt", instant.toString());

                // ACP 2
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL010", "main", "idAcp", "ACPELEDKVIPKLSEGL010");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL010", "main", "namaAcp",
                                "Peserta didik mampu menerapkan prosedur penyelidikan ilmiah dan mengevaluasi kekurangan atau kesalahan pada desain percobaan ilmiah tersebut.");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL010", "school", "idSchool", "RWK001");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL010", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL010", "elemen", "idElemen", "ELEDKVIPKLSEGL010");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL010", "elemen", "namaElemen",
                                "Mendesain dan mengevaluasi penyelidikan ilmiah");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL010", "mapel", "idMapel", "MAP011");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL010", "mapel", "name",
                                "Projek Ilmu Pengetahuan Alam dan Sosial");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL010", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL010", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL010", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL010", "kelas", "namaKelas", "X");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL010", "semester", "idSemester", "SM001");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL010", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL010", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL010", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL010", "detail", "createdAt", instant.toString());

                // ACP 3
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL011", "main", "idAcp", "ACPELEDKVIPKLSEGL011");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL011", "main", "namaAcp",
                                "Peserta didik mampu menerjemahkan data dan bukti dari berbagai sumber seperti tabel hasil, grafik, atau sumber data lain untuk membangun sebuah argumen dan dapat mempertahankan argumen tersebut dengan penjelasan ilmiah, mengomunikasikan proses dan hasil, dan melakukan refleksi diri terhadap tahapan kegiatan yang dilakukan. ");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL011", "school", "idSchool", "RWK001");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL011", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL011", "elemen", "idElemen", "ELEDKVIPKLSEGL011");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL011", "elemen", "namaElemen",
                                "Menerjemahkan data dan bukti-bukti secara ilmiah");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL011", "mapel", "idMapel", "MAP011");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL011", "mapel", "name",
                                "Projek Ilmu Pengetahuan Alam dan Sosial");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL011", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL011", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL011", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL011", "kelas", "namaKelas", "X");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL011", "semester", "idSemester", "SM001");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL011", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL011", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL011", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAcp, "ACPELEDKVIPKLSEGL011", "detail", "createdAt", instant.toString());

                // ======================================================================================================================

                // Insert Data Table ACP untuk Informatika Kelas X Semester Ganjil Mapel

                // Untuk Kelas XI Semester Ganjil maka ada kode khusus untuk ACP
                // ACP = ACP, ELE = Elemen, DKV = Desain Komunikasi Visual, BI = Bahasa
                // Indonesia, KLS =
                // Kelas, F = Fase / XI, GL = Ganjil
                // Tiga angka terakhir adalah urutan elemen dalam fase tersebut

                // ACP Pendidikan Pancasila
                // ACP 1

                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL012", "main", "idAcp", "ACPELEDKVPPKLSEGL012");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL012", "main", "namaAcp",
                                "Peserta didik menganalisis cara pandang para pendiri negara tentang dasar negara; menganalisis kedudukan Pancasila sebagai dasar negara, pandangan hidup, dan ideologi negara; merumuskan gagasan solutif untuk mengatasi perilaku yang bertentangan dengan nilai Pancasila dalam kehidupan sehari-hari.");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL012", "school", "idSchool", "RWK001");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL012", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL012", "elemen", "idElemen", "ELEDKVPPKLSEGL012");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL012", "elemen", "namaElemen",
                                "Pancasila");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL012", "mapel", "idMapel", "MAP009");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL012", "mapel", "name",
                                "Pendidikan Pancasila");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL012", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL012", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL012", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL012", "kelas", "namaKelas", "X");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL012", "semester", "idSemester", "SM001");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL012", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL012", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL012", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL012", "detail", "createdAt", instant.toString());

                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL013", "main", "idAcp", "ACPELEDKVPPKLSEGL013");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL013", "main", "namaAcp",
                                "Peserta didik menerapkan perilaku taat hukum berdasarkan peraturan yang berlaku di masyarakat; menganalisis tata urutan peraturan perundang-undangan di Indonesia. ");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL013", "school", "idSchool", "RWK001");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL013", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL013", "elemen", "idElemen", "ELEDKVPPKLSEGL013");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL013", "elemen", "namaElemen",
                                "Undang-Undang Dasar Negara Republik Indonesia Tahun 1945");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL013", "mapel", "idMapel", "MAP009");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL013", "mapel", "name",
                                "Pendidikan Pancasila");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL013", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL013", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL013", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL013", "kelas", "namaKelas", "X");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL013", "semester", "idSemester", "SM001");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL013", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL013", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL013", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL013", "detail", "createdAt", instant.toString());

                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL014", "main", "idAcp", "ACPELEDKVPPKLSEGL014");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL014", "main", "namaAcp",
                                "Peserta didik menyajikan asal usul dan makna semboyan Bhinneka Tunggal Ika sebagai modal sosial; membangun harmoni dalam keberagaman; dan mengenal gotong royong sebagai perwujudan sistem ekonomi Pancasila yang inklusif dan berkeadilan. ");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL014", "school", "idSchool", "RWK001");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL014", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL014", "elemen", "idElemen", "ELEDKVPPKLSEGL014");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL014", "elemen", "namaElemen",
                                "Bhinneka Tunggal Ika");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL014", "mapel", "idMapel", "MAP009");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL014", "mapel", "name",
                                "Pendidikan Pancasila");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL014", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL014", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL014", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL014", "kelas", "namaKelas", "X");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL014", "semester", "idSemester", "SM001");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL014", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL014", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL014", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL014", "detail", "createdAt", instant.toString());

                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL015", "main", "idAcp", "ACPELEDKVPPKLSEGL015");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL015", "main", "namaAcp",
                                "Peserta didik menerapkan perilaku sesuai dengan hak dan kewajiban sebagai warga sekolah, warga masyarakat dan warga negara; serta memahami peran dan kedudukannya sebagai Warga Negara Indonesia; memahami sistem pertahanan dan keamanan negara; menganalisis peran Indonesia dalam hubungan antarbangsa dan negara; serta menguraikan nilai-nilai Pancasila yang harus diwujudkan dalam pembangunan nasional");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL015", "school", "idSchool", "RWK001");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL015", "school", "nameSchool",
                                "SMK Negeri 01 ROWOKANGKUNG");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL015", "elemen", "idElemen", "ELEDKVPPKLSEGL015");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL015", "elemen", "namaElemen",
                                "Negara Kesatuan Republik Indonesia");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL015", "mapel", "idMapel", "MAP009");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL015", "mapel", "name",
                                "Pendidikan Pancasila");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL015", "tahunAjaran", "idTahun", "TA001");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL015", "tahunAjaran", "tahunAjaran", "2025/2026");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL015", "kelas", "idKelas", "KLS001");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL015", "kelas", "namaKelas", "X");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL015", "semester", "idSemester", "SM001");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL015", "semester", "namaSemester", "Ganjil");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL015", "konsentrasiKeahlianSekolah",
                                "idKonsentrasiSekolah",
                                "KKS001");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL015", "konsentrasiKeahlianSekolah",
                                "namaKonsentrasiSekolah",
                                "Desain Komunikasi Visual");
                client.insertRecord(tableAcp, "ACPELEDKVPPKLSEGL015", "detail", "createdAt", instant.toString());

        }

}
