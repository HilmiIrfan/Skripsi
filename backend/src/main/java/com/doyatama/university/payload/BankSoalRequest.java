package com.doyatama.university.payload;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class BankSoalRequest {
    private String idBankSoal;
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

    private String idTaksonomi;
    private String idAtp;
    private String idMapel;
    private String idTahun;
    private String idSemester;
    private String idKelas;
    private String idKonsentrasiSekolah;
    private String idElemen;
    private String idAcp;
    private String idSchool;

    public BankSoalRequest() {
    }

    public BankSoalRequest(String idBankSoal, String idSoalUjian, String namaUjian, String pertanyaan, String bobot,
            String jenisSoal,
            Map<String, String> opsi,
            Map<String, String> pasangan, List<String> jawabanBenar, String toleransiTypo, String idTaksonomi,
            String idAtp,
            String idMapel, String idTahun, String idSemester, String idKelas, String idKonsentrasiSekolah,
            String idElemen,
            String idAcp, String idSchool) {
        this.idBankSoal = idBankSoal;
        this.idSoalUjian = idSoalUjian;
        this.namaUjian = namaUjian;
        this.pertanyaan = pertanyaan;
        this.bobot = bobot;
        this.jenisSoal = jenisSoal;
        this.opsi = opsi;
        this.pasangan = pasangan;
        this.jawabanBenar = jawabanBenar;
        this.toleransiTypo = toleransiTypo;
        this.idTaksonomi = idTaksonomi;
        this.idAtp = idAtp;
        this.idMapel = idMapel;
        this.idTahun = idTahun;
        this.idSemester = idSemester;
        this.idKelas = idKelas;
        this.idKonsentrasiSekolah = idKonsentrasiSekolah;
        this.idElemen = idElemen;
        this.idAcp = idAcp;
        this.idSchool = idSchool;
    }

    public String getIdBankSoal() {
        return idBankSoal;
    }

    public void setIdBankSoal(String idBankSoal) {
        this.idBankSoal = idBankSoal;
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

    public String getIdTaksonomi() {
        return idTaksonomi;
    }

    public void setIdTaksonomi(String idTaksonomi) {
        this.idTaksonomi = idTaksonomi;
    }

    public String getIdAtp() {
        return idAtp;
    }

    public void setIdAtp(String idAtp) {
        this.idAtp = idAtp;
    }

    public String getIdMapel() {
        return idMapel;
    }

    public void setIdMapel(String idMapel) {
        this.idMapel = idMapel;
    }

    public String getIdTahun() {
        return idTahun;
    }

    public void setIdTahun(String idTahun) {
        this.idTahun = idTahun;
    }

    public String getIdSemester() {
        return idSemester;
    }

    public void setIdSemester(String idSemester) {
        this.idSemester = idSemester;
    }

    public String getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(String idKelas) {
        this.idKelas = idKelas;
    }

    public String getIdKonsentrasiSekolah() {
        return idKonsentrasiSekolah;
    }

    public void setIdKonsentrasiSekolah(String idKonsentrasiSekolah) {
        this.idKonsentrasiSekolah = idKonsentrasiSekolah;
    }

    public String getIdElemen() {
        return idElemen;
    }

    public void setIdElemen(String idElemen) {
        this.idElemen = idElemen;
    }

    public String getIdAcp() {
        return idAcp;
    }

    public void setIdAcp(String idAcp) {
        this.idAcp = idAcp;
    }

    public String getIdSchool() {
        return idSchool;
    }

    public void setIdSchool(String idSchool) {
        this.idSchool = idSchool;
    }

}
