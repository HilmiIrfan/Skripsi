package com.doyatama.university.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class UjianPeserta {

    private String idUjianPeserta;
    private String idUjian;
    private String idUser; // Peserta ujian (siswa smk)
    private Instant waktuMulaiMengerjakan; // Kapan peserta mulai mengerjakan
    private Instant waktuSelesaiMengerjakan; // Kapan peserta selesai/submit
    private String statusPengerjaan; // BELUM_MULAI, SEDANG_MENGERJAKAN, SELESAI, TIMEOUT, DISKUALIFIKASI
    private Double nilaiAkhir; // Nilai yang didapat peserta
    private Integer soalDijawab; // Jumlah soal yang sudah dijawab
    private Integer soalBenar; // Jumlah jawaban benar

    // Bank soal relationship
    private List<String> idBankSoalList; // List ID bank soal yang digunakan untuk peserta ini
    private List<String> urutanSoal; // Urutan soal untuk peserta ini (jika diacak)

    // Jawaban structure untuk berbagai tipe soal
    private Map<String, String> jawabanPeserta; // Key: idBankSoal, Value: jawaban (single answer)
    private Map<String, List<String>> jawabanPesertaMulti; // Key: idBankSoal, Value: multiple answers for MULTI type
    private Map<String, Map<String, String>> jawabanPesertaCocok; // Key: idBankSoal, Value: matching pairs for COCOK
                                                                  // type

    private Map<String, Boolean> statusJawaban; // Key: idBankSoal, Value: benar/salah
    private Map<String, Double> skorPerSoal; // Key: idBankSoal, Value: skor yang didapat per soal
    private Map<String, Instant> waktuJawaban; // Key: idBankSoal, Value: waktu menjawab
    private Long totalWaktuPengerjaan; // Total waktu dalam detik

    // Timing management
    private Instant batasWaktuPeserta; // Batas waktu khusus untuk peserta ini
    private Integer durasiEfektifMenit; // Durasi efektif yang diberikan kepada peserta
    private Boolean isLateStart; // Apakah peserta mulai terlambat
    private Integer keterlambatanMenit; // Berapa menit terlambat

    // Activity tracking
    private Map<String, Object> logAktivitas; // Log aktivitas peserta
    private Integer jumlahPelanggaran; // Jumlah pelanggaran kecurangan
    private List<String> detailPelanggaran; // Detail pelanggaran
    private Boolean isSubmittedBySystem; // Apakah disubmit otomatis oleh sistem
    private Map<String, Long> waktuPerSoal; // Waktu yang digunakan per soal (dalam detik)

    // Session data
    private Integer attemptNumber; // Percobaan ke berapa (untuk multiple attempts)
    private Map<String, Integer> revisitCount; // Key: idBankSoal, Value: berapa kali soal dikunjungi kembali

    // Timestamps
    private Instant createdAt;
    private Instant updatedAt;

    // Relasi objects (transient, tidak disimpan di HBase)
    private transient Ujian ujian;
    private transient User peserta;
    private transient List<BankSoal> bankSoalList; // List of BankSoal objects

    // Default constructor
    public UjianPeserta() {
        this.idBankSoalList = new ArrayList<>();
        this.jawabanPeserta = new HashMap<>();
        this.jawabanPesertaMulti = new HashMap<>();
        this.jawabanPesertaCocok = new HashMap<>();
        this.statusJawaban = new HashMap<>();
        this.skorPerSoal = new HashMap<>();
        this.waktuJawaban = new HashMap<>();
        this.logAktivitas = new HashMap<>();
        this.waktuPerSoal = new HashMap<>();
        this.revisitCount = new HashMap<>();
        this.detailPelanggaran = new ArrayList<>();
        this.initializeDefaults();
    }

    // Constructor with basic parameters
    public UjianPeserta(String idUjianPeserta, String idUjian, String idUser,
            Instant waktuMulaiMengerjakan, String statusPengerjaan,
            List<String> idBankSoalList, List<String> urutanSoal,
            Map<String, String> jawabanPeserta, Instant createdAt) {
        this();
        this.idUjianPeserta = idUjianPeserta;
        this.idUjian = idUjian;
        this.idUser = idUser;
        this.waktuMulaiMengerjakan = waktuMulaiMengerjakan;
        this.statusPengerjaan = statusPengerjaan;
        this.idBankSoalList = idBankSoalList != null ? new ArrayList<>(idBankSoalList) : new ArrayList<>();
        this.urutanSoal = urutanSoal != null ? new ArrayList<>(urutanSoal) : new ArrayList<>();
        this.jawabanPeserta = jawabanPeserta != null ? new HashMap<>(jawabanPeserta) : new HashMap<>();
        this.createdAt = createdAt;
        this.updatedAt = Instant.now();
    }

    // Constructor dengan relasi Ujian
    public UjianPeserta(String idUjianPeserta, String idUjian, String idUser, Ujian ujian) {
        this();
        this.idUjianPeserta = idUjianPeserta;
        this.idUjian = idUjian;
        this.idUser = idUser;
        this.ujian = ujian;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    private void initializeDefaults() {
        if (this.soalDijawab == null)
            this.soalDijawab = 0;
        if (this.soalBenar == null)
            this.soalBenar = 0;
        if (this.nilaiAkhir == null)
            this.nilaiAkhir = 0.0;
        if (this.isLateStart == null)
            this.isLateStart = false;
        if (this.keterlambatanMenit == null)
            this.keterlambatanMenit = 0;
        if (this.jumlahPelanggaran == null)
            this.jumlahPelanggaran = 0;
        if (this.isSubmittedBySystem == null)
            this.isSubmittedBySystem = false;
        if (this.attemptNumber == null)
            this.attemptNumber = 1;
    }

    // Getters and Setters
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
        if (this.waktuMulaiMengerjakan != null && waktuSelesaiMengerjakan != null) {
            this.totalWaktuPengerjaan = waktuSelesaiMengerjakan.getEpochSecond() -
                    this.waktuMulaiMengerjakan.getEpochSecond();
        }
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

    public Map<String, Boolean> getStatusJawaban() {
        return statusJawaban;
    }

    public void setStatusJawaban(Map<String, Boolean> statusJawaban) {
        this.statusJawaban = statusJawaban;
    }

    public Map<String, Double> getSkorPerSoal() {
        return skorPerSoal;
    }

    public void setSkorPerSoal(Map<String, Double> skorPerSoal) {
        this.skorPerSoal = skorPerSoal;
    }

    public Map<String, Instant> getWaktuJawaban() {
        return waktuJawaban;
    }

    public void setWaktuJawaban(Map<String, Instant> waktuJawaban) {
        this.waktuJawaban = waktuJawaban;
    }

    public Long getTotalWaktuPengerjaan() {
        return totalWaktuPengerjaan;
    }

    public void setTotalWaktuPengerjaan(Long totalWaktuPengerjaan) {
        this.totalWaktuPengerjaan = totalWaktuPengerjaan;
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

    public Map<String, Object> getLogAktivitas() {
        return logAktivitas;
    }

    public void setLogAktivitas(Map<String, Object> logAktivitas) {
        this.logAktivitas = logAktivitas;
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

    public Map<String, Long> getWaktuPerSoal() {
        return waktuPerSoal;
    }

    public void setWaktuPerSoal(Map<String, Long> waktuPerSoal) {
        this.waktuPerSoal = waktuPerSoal;
    }

    public Integer getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(Integer attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public Map<String, Integer> getRevisitCount() {
        return revisitCount;
    }

    public void setRevisitCount(Map<String, Integer> revisitCount) {
        this.revisitCount = revisitCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Ujian getUjian() {
        return ujian;
    }

    public void setUjian(Ujian ujian) {
        this.ujian = ujian;
    }

    public User getPeserta() {
        return peserta;
    }

    public void setPeserta(User peserta) {
        this.peserta = peserta;
    }

    public List<BankSoal> getBankSoalList() {
        return bankSoalList;
    }

    public void setBankSoalList(List<BankSoal> bankSoalList) {
        this.bankSoalList = bankSoalList;
        if (bankSoalList != null) {
            this.idBankSoalList = new ArrayList<>();
            for (BankSoal bankSoal : bankSoalList) {
                this.idBankSoalList.add(bankSoal.getIdBankSoal());
            }
        }
    }

    // Simple utility methods (status checking only)
    public boolean isBelumMulai() {
        return "BELUM_MULAI".equals(statusPengerjaan);
    }

    public boolean isSedangMengerjakan() {
        return "SEDANG_MENGERJAKAN".equals(statusPengerjaan);
    }

    public boolean isSelesai() {
        return "SELESAI".equals(statusPengerjaan);
    }

    public boolean isTimeout() {
        return "TIMEOUT".equals(statusPengerjaan);
    }

    public boolean isDiskualifikasi() {
        return "DISKUALIFIKASI".equals(statusPengerjaan);
    }

    // Basic data management methods
    public void addJawaban(String idBankSoal, String jawaban) {
        this.jawabanPeserta.put(idBankSoal, jawaban);
        this.waktuJawaban.put(idBankSoal, Instant.now());
        this.updatedAt = Instant.now();
    }

    public void addJawabanMulti(String idBankSoal, List<String> jawaban) {
        this.jawabanPesertaMulti.put(idBankSoal, jawaban);
        this.waktuJawaban.put(idBankSoal, Instant.now());
        this.updatedAt = Instant.now();
    }

    public void addJawabanCocok(String idBankSoal, Map<String, String> jawaban) {
        this.jawabanPesertaCocok.put(idBankSoal, jawaban);
        this.waktuJawaban.put(idBankSoal, Instant.now());
        this.updatedAt = Instant.now();
    }

    public void updateStatusJawaban(String idBankSoal, Boolean benar, Double skor) {
        this.statusJawaban.put(idBankSoal, benar);
        this.skorPerSoal.put(idBankSoal, skor != null ? skor : 0.0);
        this.updatedAt = Instant.now();
    }

    public void addPelanggaran(String detailPelanggaran) {
        if (this.detailPelanggaran == null) {
            this.detailPelanggaran = new ArrayList<>();
        }
        this.detailPelanggaran.add(detailPelanggaran);
        this.jumlahPelanggaran = this.detailPelanggaran.size();
        this.updatedAt = Instant.now();
    }

    public void logActivity(String activity, Object data) {
        String timestamp = Instant.now().toString();
        Map<String, Object> activityData = new HashMap<>();
        activityData.put("timestamp", timestamp);
        activityData.put("activity", activity);
        activityData.put("data", data);

        this.logAktivitas.put(timestamp, activityData);
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "UjianPeserta{" +
                "idUjianPeserta='" + idUjianPeserta + '\'' +
                ", idUjian='" + idUjian + '\'' +
                ", idUser='" + idUser + '\'' +
                ", statusPengerjaan='" + statusPengerjaan + '\'' +
                ", nilaiAkhir=" + nilaiAkhir +
                ", soalDijawab=" + soalDijawab +
                ", soalBenar=" + soalBenar +
                ", totalWaktuPengerjaan=" + totalWaktuPengerjaan +
                ", isLateStart=" + isLateStart +
                ", keterlambatanMenit=" + keterlambatanMenit +
                ", jumlahPelanggaran=" + jumlahPelanggaran +
                ", isSubmittedBySystem=" + isSubmittedBySystem +
                ", attemptNumber=" + attemptNumber +
                ", bankSoalCount=" + (idBankSoalList != null ? idBankSoalList.size() : 0) +
                '}';
    }
}