package com.doyatama.university.payload;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class UjianPesertaRequest {
    private String idUjianPeserta;
    private String idUjian;
    private String idUser; // Peserta ujian (siswa smk)
    private Instant waktuMulaiMengerjakan;
    private Instant waktuSelesaiMengerjakan;
    private String statusPengerjaan; // BELUM_MULAI, SEDANG_MENGERJAKAN, SELESAI, TIMEOUT, DISKUALIFIKASI
    private Double nilaiAkhir;
    private Integer soalDijawab;
    private Integer soalBenar;

    // Bank soal relationship
    private List<String> idBankSoalList;
    private List<String> urutanSoal;

    // Jawaban structure untuk berbagai tipe soal
    private Map<String, String> jawabanPeserta; // Key: idBankSoal, Value: jawaban (single answer)
    private Map<String, List<String>> jawabanPesertaMulti; // Key: idBankSoal, Value: multiple answers
    private Map<String, Map<String, String>> jawabanPesertaCocok; // Key: idBankSoal, Value: matching pairs

    // Timing management
    private Instant batasWaktuPeserta;
    private Integer durasiEfektifMenit;
    private Boolean isLateStart;
    private Integer keterlambatanMenit;

    // Activity tracking
    private Integer jumlahPelanggaran;
    private List<String> detailPelanggaran;
    private Boolean isSubmittedBySystem;

    // Session data
    private Integer attemptNumber;

    private Instant createdAt;

    public UjianPesertaRequest() {
    }

    public String getIdUjianPeserta() {
        return idUjianPeserta;
    }

    public void setIdUjianPeserta(String idUjianPeserta) {
        this.idUjianPeserta = idUjianPeserta;
    }

    public String getIdUjian() {
        return idUjian;
    }

    public void setIdUjian(String idUjian) {
        this.idUjian = idUjian;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public Instant getWaktuMulaiMengerjakan() {
        return waktuMulaiMengerjakan;
    }

    public void setWaktuMulaiMengerjakan(Instant waktuMulaiMengerjakan) {
        this.waktuMulaiMengerjakan = waktuMulaiMengerjakan;
    }

    public Instant getWaktuSelesaiMengerjakan() {
        return waktuSelesaiMengerjakan;
    }

    public void setWaktuSelesaiMengerjakan(Instant waktuSelesaiMengerjakan) {
        this.waktuSelesaiMengerjakan = waktuSelesaiMengerjakan;
    }

    public String getStatusPengerjaan() {
        return statusPengerjaan;
    }

    public void setStatusPengerjaan(String statusPengerjaan) {
        this.statusPengerjaan = statusPengerjaan;
    }

    public Double getNilaiAkhir() {
        return nilaiAkhir;
    }

    public void setNilaiAkhir(Double nilaiAkhir) {
        this.nilaiAkhir = nilaiAkhir;
    }

    public Integer getSoalDijawab() {
        return soalDijawab;
    }

    public void setSoalDijawab(Integer soalDijawab) {
        this.soalDijawab = soalDijawab;
    }

    public Integer getSoalBenar() {
        return soalBenar;
    }

    public void setSoalBenar(Integer soalBenar) {
        this.soalBenar = soalBenar;
    }

    public List<String> getIdBankSoalList() {
        return idBankSoalList;
    }

    public void setIdBankSoalList(List<String> idBankSoalList) {
        this.idBankSoalList = idBankSoalList;
    }

    public List<String> getUrutanSoal() {
        return urutanSoal;
    }

    public void setUrutanSoal(List<String> urutanSoal) {
        this.urutanSoal = urutanSoal;
    }

    public Map<String, String> getJawabanPeserta() {
        return jawabanPeserta;
    }

    public void setJawabanPeserta(Map<String, String> jawabanPeserta) {
        this.jawabanPeserta = jawabanPeserta;
    }

    public Map<String, List<String>> getJawabanPesertaMulti() {
        return jawabanPesertaMulti;
    }

    public void setJawabanPesertaMulti(Map<String, List<String>> jawabanPesertaMulti) {
        this.jawabanPesertaMulti = jawabanPesertaMulti;
    }

    public Map<String, Map<String, String>> getJawabanPesertaCocok() {
        return jawabanPesertaCocok;
    }

    public void setJawabanPesertaCocok(Map<String, Map<String, String>> jawabanPesertaCocok) {
        this.jawabanPesertaCocok = jawabanPesertaCocok;
    }

    public Instant getBatasWaktuPeserta() {
        return batasWaktuPeserta;
    }

    public void setBatasWaktuPeserta(Instant batasWaktuPeserta) {
        this.batasWaktuPeserta = batasWaktuPeserta;
    }

    public Integer getDurasiEfektifMenit() {
        return durasiEfektifMenit;
    }

    public void setDurasiEfektifMenit(Integer durasiEfektifMenit) {
        this.durasiEfektifMenit = durasiEfektifMenit;
    }

    public Boolean getIsLateStart() {
        return isLateStart;
    }

    public void setIsLateStart(Boolean isLateStart) {
        this.isLateStart = isLateStart;
    }

    public Integer getKeterlambatanMenit() {
        return keterlambatanMenit;
    }

    public void setKeterlambatanMenit(Integer keterlambatanMenit) {
        this.keterlambatanMenit = keterlambatanMenit;
    }

    public Integer getJumlahPelanggaran() {
        return jumlahPelanggaran;
    }

    public void setJumlahPelanggaran(Integer jumlahPelanggaran) {
        this.jumlahPelanggaran = jumlahPelanggaran;
    }

    public List<String> getDetailPelanggaran() {
        return detailPelanggaran;
    }

    public void setDetailPelanggaran(List<String> detailPelanggaran) {
        this.detailPelanggaran = detailPelanggaran;
    }

    public Boolean getIsSubmittedBySystem() {
        return isSubmittedBySystem;
    }

    public void setIsSubmittedBySystem(Boolean isSubmittedBySystem) {
        this.isSubmittedBySystem = isSubmittedBySystem;
    }

    public Integer getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(Integer attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}