package com.doyatama.university.payload;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class SoalUjianRequest {
    private String idSoalUjian;
    private String namaUjian;
    private String pertanyaan;
    private String bobot;
    private String jenisSoal;
    private Map<String, String> opsi; // Untuk PG/MULTI
    private Map<String, String> pasangan; // Untuk COCOK
    private String toleransiTypo; // Khusus ISIAN
    private List<String> jawabanBenar; // kunci jawaban benar
    private Instant createdAt; // kapan soal dibuat

    private String idUser;
    private String idTaksonomi; // taksonomi kesulitan
    private String idSchool; // sekolah
    private String idKonsentrasiSekolah;

    public SoalUjianRequest() {
    }

    public SoalUjianRequest(String idSoalUjian, String namaUjian, String pertanyaan, String bobot, String jenisSoal,
            Map<String, String> opsi,
            Map<String, String> pasangan, List<String> jawabanBenar, String toleransiTypo, String idUser,
            String idKonsentrasiSekolah,
            String idTaksonomi, String idSchool) {
        this.idSoalUjian = idSoalUjian;
        this.namaUjian = namaUjian;
        this.pertanyaan = pertanyaan;
        this.bobot = bobot;
        this.jenisSoal = jenisSoal;
        this.opsi = opsi;
        this.pasangan = pasangan;
        this.jawabanBenar = jawabanBenar;
        this.toleransiTypo = toleransiTypo;
        this.idUser = idUser;
        this.idTaksonomi = idTaksonomi;
        this.idSchool = idSchool;
        this.idKonsentrasiSekolah = idKonsentrasiSekolah;
    }

    public String getIdSoalUjian() {
        return idSoalUjian;
    }

    public void setIdSoalUjian(String idSoalUjian) {
        this.idSoalUjian = idSoalUjian;
    }

    public String getNamaUjian() {
        return namaUjian;
    }

    public void setNamaUjian(String namaUjian) {
        this.namaUjian = namaUjian;
    }

    public String getPertanyaan() {
        return pertanyaan;
    }

    public void setPertanyaan(String pertanyaan) {
        this.pertanyaan = pertanyaan;
    }

    public String getBobot() {
        return bobot;
    }

    public void setBobot(String bobot) {
        this.bobot = bobot;
    }

    public String getJenisSoal() {
        return jenisSoal;
    }

    public void setJenisSoal(String jenisSoal) {
        this.jenisSoal = jenisSoal;
    }

    public Map<String, String> getOpsi() {
        return opsi;
    }

    public void setOpsi(Map<String, String> opsi) {
        this.opsi = opsi;
    }

    public Map<String, String> getPasangan() {
        return pasangan;
    }

    public void setPasangan(Map<String, String> pasangan) {
        this.pasangan = pasangan;
    }

    public String getToleransiTypo() {
        return toleransiTypo;
    }

    public void setToleransiTypo(String toleransiTypo) {
        this.toleransiTypo = toleransiTypo;
    }

    public List<String> getJawabanBenar() {
        return jawabanBenar;
    }

    public void setJawabanBenar(List<String> jawabanBenar) {
        this.jawabanBenar = jawabanBenar;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdTaksonomi() {
        return idTaksonomi;
    }

    public void setIdTaksonomi(String idTaksonomi) {
        this.idTaksonomi = idTaksonomi;
    }

    public String getIdSchool() {
        return idSchool;
    }

    public void setIdSchool(String idSchool) {
        this.idSchool = idSchool;
    }

    public String getIdKonsentrasiSekolah() {
        return idKonsentrasiSekolah;
    }

    public void setIdKonsentrasiSekolah(String idKonsentrasiSekolah) {
        this.idKonsentrasiSekolah = idKonsentrasiSekolah;
    }

}
