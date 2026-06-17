package com.doyatama.university.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class BankSoal {

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

    private SoalUjian soalUjian;
    private TahunAjaran tahunAjaran;
    private Kelas kelas;
    private Semester semester;
    private Mapel mapel;
    private Elemen elemen;
    private Acp acp;
    private Atp atp;
    private Taksonomi taksonomi; // taksonomi kesulitan
    private KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolah;
    private School school;

    public BankSoal() {
    }

    public BankSoal(String idBankSoal, String idSoalUjian, String namaUjian, String pertanyaan, String bobot,
            String jenisSoal,
            Map<String, String> opsi,
            Map<String, String> pasangan, List<String> jawabanBenar, String toleransiTypo, Instant createdAt,
            SoalUjian soalUjian, Taksonomi taksonomi, KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolah,
            Atp atp, TahunAjaran tahunAjaran, Kelas kelas, Semester semester, Mapel mapel, Elemen elemen, Acp acp,
            School school) {
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
        this.createdAt = createdAt;
        this.soalUjian = soalUjian;
        this.taksonomi = taksonomi;
        this.tahunAjaran = tahunAjaran;
        this.kelas = kelas;
        this.semester = semester;
        this.mapel = mapel;
        this.elemen = elemen;
        this.acp = acp;
        this.atp = atp;
        this.school = school;
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

    public Double getBobotNumerik() {
        try {
            return Double.parseDouble(bobot);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Format bobot tidak valid: " + bobot);
        }
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

    public Taksonomi getTaksonomi() {
        return taksonomi;
    }

    public void setTaksonomi(Taksonomi taksonomi) {
        this.taksonomi = taksonomi;
    }

    public SoalUjian getSoalUjian() {
        return soalUjian;
    }

    public void setSoalUjian(SoalUjian soalUjian) {
        this.soalUjian = soalUjian;
    }

    public KonsentrasiKeahlianSekolah getKonsentrasiKeahlianSekolah() {
        return konsentrasiKeahlianSekolah;
    }

    public void setKonsentrasiKeahlianSekolah(KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolah) {
        this.konsentrasiKeahlianSekolah = konsentrasiKeahlianSekolah;
    }

    public Atp getAtp() {
        return atp;
    }

    public void setAtp(Atp atp) {
        this.atp = atp;
    }

    public TahunAjaran getTahunAjaran() {
        return tahunAjaran;
    }

    public void setTahunAjaran(TahunAjaran tahunAjaran) {
        this.tahunAjaran = tahunAjaran;
    }

    public Kelas getKelas() {
        return kelas;
    }

    public void setKelas(Kelas kelas) {
        this.kelas = kelas;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public Mapel getMapel() {
        return mapel;
    }

    public void setMapel(Mapel mapel) {
        this.mapel = mapel;
    }

    public Elemen getElemen() {
        return elemen;
    }

    public void setElemen(Elemen elemen) {
        this.elemen = elemen;
    }

    public Acp getAcp() {
        return acp;
    }

    public void setAcp(Acp acp) {
        this.acp = acp;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    // Validation Methods
    public boolean isValid() {
        if (idSoalUjian == null || pertanyaan == null || jenisSoal == null) {
            return false;
        }

        try {
            Double.parseDouble(bobot);
        } catch (NumberFormatException e) {
            return false;
        }

        switch (jenisSoal) {
            case "PG":
                return validatePG();
            case "MULTI":
                return validateMulti();
            case "COCOK":
                return validateCocok();
            case "ISIAN":
                return validateIsian();
            default:
                return false;
        }
    }

    private boolean validatePG() {
        return opsi != null && opsi.size() >= 2 &&
                jawabanBenar != null && jawabanBenar.size() == 1;
    }

    private boolean validateMulti() {
        return opsi != null && opsi.size() >= 2 &&
                jawabanBenar != null && !jawabanBenar.isEmpty();
    }

    private boolean validateCocok() {
        if (pasangan == null || pasangan.isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile("^\\d+_(kiri|kanan)$");
        for (String key : pasangan.keySet()) {
            if (!pattern.matcher(key).matches()) {
                return false;
            }
        }
        return true;
    }

    private boolean validateIsian() {
        return jawabanBenar != null && !jawabanBenar.isEmpty();
    }

    // Utility Methods
    public boolean isPilihanGanda() {
        return "PG".equals(jenisSoal);
    }

    public boolean isMencocokkan() {
        return "COCOK".equals(jenisSoal);
    }

    public boolean isMultiPilihan() {
        return "MULTI".equals(jenisSoal);
    }

    public boolean isIsian() {
        return "ISIAN".equals(jenisSoal);
    }

    @Override
    public String toString() {
        return "BankSoal{" +
                "idBankSoal='" + idBankSoal + '\'' +
                ", soalUjian=" + soalUjian +
                '}';
    }

}
