package com.doyatama.university.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class Ujian {

    private String idUjian;
    private String namaUjian;
    private String deskripsi;
    private Integer durasiMenit;
    private Instant waktuMulaiDijadwalkan;
    private Instant waktuSelesaiOtomatis;
    private String statusUjian; // DRAFT, AKTIF, SELESAI, DIBATALKAN
    private Boolean isLive;
    private Boolean isAutoStart;
    private Boolean isAutoEnd;
    private Integer jumlahSoal;
    private Double totalBobot;
    private String tipeSoal; // ACAK, BERURUTAN
    private Boolean tampilkanNilai;
    private Integer jumlahPeserta; // Jumlah peserta yang telah mengikuti ujian

    // Menyimpan daftar ID soal untuk referensi cepat
    private List<String> idBankSoalList;

    // Menyimpan salinan data soal yang disederhanakan (embedded/denormalized)
    private List<BankSoalUjian> bankSoalList;

    private Map<String, Object> pengaturan;
    private Instant createdAt;
    private Instant updatedAt;

    // Pengaturan CAT
    private Boolean isCatEnabled;
    private Map<String, Object> catSettings;
    private Boolean allowLateStart;
    private Integer maxLateStartMinutes;
    private Boolean showTimerToParticipants;
    private Boolean preventCheating;

    // Pengaturan waktu fleksibel
    private Boolean isFlexibleTiming;
    private Instant batasAkhirMulai;
    private Boolean autoEndAfterDuration;
    private Integer toleransiKeterlambatanMenit;

    // Pengaturan soal dan skor
    private String strategiPemilihanSoal; // RANDOM, SEQUENTIAL
    private Double minPassingScore;
    private Boolean allowReview;
    private Boolean allowBacktrack;
    private Integer maxAttempts;

    // Relasi Utama: Konteks dari Ujian ini
    private TahunAjaran tahunAjaran;
    private Kelas kelas;
    private Semester semester;
    private Mapel mapel;
    private KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolah;
    private User createdBy;
    private School school;

    // Default constructor
    public Ujian() {
        this.pengaturan = new HashMap<>();
        this.catSettings = new HashMap<>();
        this.idBankSoalList = new ArrayList<>();
        this.bankSoalList = new ArrayList<>();
    }

    // Constructor with parameters
    public Ujian(String idUjian, String namaUjian, String deskripsi, Integer durasiMenit,
            Instant waktuMulaiDijadwalkan, String statusUjian, Boolean isLive, Integer jumlahSoal,
            Double totalBobot, String tipeSoal, Boolean tampilkanNilai, BankSoalUjian bankSoalList,
            List<String> idBankSoalList, Map<String, Object> pengaturan, Instant createdAt,
            TahunAjaran tahunAjaran, Kelas kelas, Semester semester, Mapel mapel, School school,
            KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolah, User createdBy) {

        this.idUjian = idUjian;
        this.namaUjian = namaUjian;
        this.deskripsi = deskripsi;
        this.durasiMenit = durasiMenit;
        this.waktuMulaiDijadwalkan = waktuMulaiDijadwalkan;
        this.statusUjian = statusUjian;
        this.isLive = isLive;
        this.jumlahSoal = jumlahSoal;
        this.totalBobot = totalBobot;
        this.tipeSoal = tipeSoal;
        this.tampilkanNilai = tampilkanNilai;
        this.idBankSoalList = idBankSoalList != null ? idBankSoalList : new ArrayList<>();
        this.pengaturan = pengaturan != null ? pengaturan : new HashMap<>();
        this.catSettings = new HashMap<>();
        this.bankSoalList = new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = Instant.now();

        // Set relations
        this.tahunAjaran = tahunAjaran;
        this.kelas = kelas;
        this.semester = semester;
        this.mapel = mapel;
        this.school = school;
        this.konsentrasiKeahlianSekolah = konsentrasiKeahlianSekolah;
        this.createdBy = createdBy;

        this.initializeDefaults();
    }

    private void initializeDefaults() {
        if (this.isAutoStart == null)
            this.isAutoStart = false;
        if (this.isAutoEnd == null)
            this.isAutoEnd = true;
        if (this.isCatEnabled == null)
            this.isCatEnabled = true;
        if (this.allowLateStart == null)
            this.allowLateStart = false;
        if (this.maxLateStartMinutes == null)
            this.maxLateStartMinutes = 0;
        if (this.showTimerToParticipants == null)
            this.showTimerToParticipants = true;
        if (this.preventCheating == null)
            this.preventCheating = false;
        if (this.isFlexibleTiming == null)
            this.isFlexibleTiming = false;
        if (this.autoEndAfterDuration == null)
            this.autoEndAfterDuration = true;
        if (this.toleransiKeterlambatanMenit == null)
            this.toleransiKeterlambatanMenit = 5;
        if (this.strategiPemilihanSoal == null)
            this.strategiPemilihanSoal = "RANDOM";
        if (this.allowReview == null)
            this.allowReview = true;
        if (this.allowBacktrack == null)
            this.allowBacktrack = false;
        if (this.maxAttempts == null)
            this.maxAttempts = 1;
        if (this.minPassingScore == null)
            this.minPassingScore = 60.0;
    }

    public String getIdUjian() {
        return idUjian;
    }

    public void setIdUjian(String idUjian) {
        this.idUjian = idUjian;
    }

    public List<String> getIdBankSoalList() {
        return idBankSoalList;
    }

    public void setIdBankSoalList(List<String> idBankSoalList) {
        this.idBankSoalList = idBankSoalList;
    }

    public List<BankSoalUjian> getBankSoalList() {
        return bankSoalList;
    }

    public void setBankSoalList(List<BankSoalUjian> bankSoalList) {
        this.bankSoalList = bankSoalList;
    }

    public String getNamaUjian() {
        return namaUjian;
    }

    public void setNamaUjian(String namaUjian) {
        this.namaUjian = namaUjian;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public Integer getDurasiMenit() {
        return durasiMenit;
    }

    public void setDurasiMenit(Integer durasiMenit) {
        this.durasiMenit = durasiMenit;
    }

    public Instant getWaktuMulaiDijadwalkan() {
        return waktuMulaiDijadwalkan;
    }

    public void setWaktuMulaiDijadwalkan(Instant waktuMulaiDijadwalkan) {
        this.waktuMulaiDijadwalkan = waktuMulaiDijadwalkan;
    }

    public Instant getWaktuSelesaiOtomatis() {
        return waktuSelesaiOtomatis;
    }

    public void setWaktuSelesaiOtomatis(Instant waktuSelesaiOtomatis) {
        this.waktuSelesaiOtomatis = waktuSelesaiOtomatis;
    }

    public String getStatusUjian() {
        return statusUjian;
    }

    public void setStatusUjian(String statusUjian) {
        this.statusUjian = statusUjian;
    }

    public Boolean getIsLive() {
        return isLive;
    }

    public void setIsLive(Boolean isLive) {
        this.isLive = isLive;
    }

    public Boolean getIsAutoStart() {
        return isAutoStart;
    }

    public void setIsAutoStart(Boolean isAutoStart) {
        this.isAutoStart = isAutoStart;
    }

    public Boolean getIsAutoEnd() {
        return isAutoEnd;
    }

    public void setIsAutoEnd(Boolean isAutoEnd) {
        this.isAutoEnd = isAutoEnd;
    }

    public Integer getJumlahSoal() {
        return jumlahSoal;
    }

    public void setJumlahSoal(Integer jumlahSoal) {
        this.jumlahSoal = jumlahSoal;
    }

    public Double getTotalBobot() {
        return totalBobot;
    }

    public void setTotalBobot(Double totalBobot) {
        this.totalBobot = totalBobot;
    }

    public String getTipeSoal() {
        return tipeSoal;
    }

    public void setTipeSoal(String tipeSoal) {
        this.tipeSoal = tipeSoal;
    }

    public Boolean getTampilkanNilai() {
        return tampilkanNilai;
    }

    public void setTampilkanNilai(Boolean tampilkanNilai) {
        this.tampilkanNilai = tampilkanNilai;
    }

    public Map<String, Object> getPengaturan() {
        return pengaturan;
    }

    public void setPengaturan(Map<String, Object> pengaturan) {
        this.pengaturan = pengaturan;
    }

    // CAT settings
    public Boolean getIsCatEnabled() {
        return isCatEnabled;
    }

    public void setIsCatEnabled(Boolean isCatEnabled) {
        this.isCatEnabled = isCatEnabled;
    }

    public Map<String, Object> getCatSettings() {
        return catSettings;
    }

    public void setCatSettings(Map<String, Object> catSettings) {
        this.catSettings = catSettings;
    }

    public Boolean getAllowLateStart() {
        return allowLateStart;
    }

    public void setAllowLateStart(Boolean allowLateStart) {
        this.allowLateStart = allowLateStart;
    }

    public Integer getMaxLateStartMinutes() {
        return maxLateStartMinutes;
    }

    public void setMaxLateStartMinutes(Integer maxLateStartMinutes) {
        this.maxLateStartMinutes = maxLateStartMinutes;
    }

    public Boolean getShowTimerToParticipants() {
        return showTimerToParticipants;
    }

    public void setShowTimerToParticipants(Boolean showTimerToParticipants) {
        this.showTimerToParticipants = showTimerToParticipants;
    }

    public Boolean getPreventCheating() {
        return preventCheating;
    }

    public void setPreventCheating(Boolean preventCheating) {
        this.preventCheating = preventCheating;
    }

    // Flexible timing settings
    public Boolean getIsFlexibleTiming() {
        return isFlexibleTiming;
    }

    public void setIsFlexibleTiming(Boolean isFlexibleTiming) {
        this.isFlexibleTiming = isFlexibleTiming;
    }

    public Instant getBatasAkhirMulai() {
        return batasAkhirMulai;
    }

    public void setBatasAkhirMulai(Instant batasAkhirMulai) {
        this.batasAkhirMulai = batasAkhirMulai;
    }

    public Boolean getAutoEndAfterDuration() {
        return autoEndAfterDuration;
    }

    public void setAutoEndAfterDuration(Boolean autoEndAfterDuration) {
        this.autoEndAfterDuration = autoEndAfterDuration;
    }

    public Integer getToleransiKeterlambatanMenit() {
        return toleransiKeterlambatanMenit;
    }

    public void setToleransiKeterlambatanMenit(Integer toleransiKeterlambatanMenit) {
        this.toleransiKeterlambatanMenit = toleransiKeterlambatanMenit;
    }

    // Question settings
    public String getStrategiPemilihanSoal() {
        return strategiPemilihanSoal;
    }

    public void setStrategiPemilihanSoal(String strategiPemilihanSoal) {
        this.strategiPemilihanSoal = strategiPemilihanSoal;
    }

    public Double getMinPassingScore() {
        return minPassingScore;
    }

    public void setMinPassingScore(Double minPassingScore) {
        this.minPassingScore = minPassingScore;
    }

    public Boolean getAllowReview() {
        return allowReview;
    }

    public void setAllowReview(Boolean allowReview) {
        this.allowReview = allowReview;
    }

    public Boolean getAllowBacktrack() {
        return allowBacktrack;
    }

    public void setAllowBacktrack(Boolean allowBacktrack) {
        this.allowBacktrack = allowBacktrack;
    }

    public Integer getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(Integer maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    // Timestamp fields
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

    // Relation objects
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

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public KonsentrasiKeahlianSekolah getKonsentrasiKeahlianSekolah() {
        return konsentrasiKeahlianSekolah;
    }

    public void setKonsentrasiKeahlianSekolah(KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolah) {
        this.konsentrasiKeahlianSekolah = konsentrasiKeahlianSekolah;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    // Simple utility methods (data access only)
    public boolean isAktif() {
        return "AKTIF".equals(statusUjian);
    }

    public boolean isSelesai() {
        return "SELESAI".equals(statusUjian);
    }

    public boolean isDraft() {
        return "DRAFT".equals(statusUjian);
    }

    // Basic data management methods
    public void addBankSoal(BankSoalUjian bankSoal) {
        if (bankSoal != null && bankSoal.getIdBankSoal() != null) {
            if (this.bankSoalList == null) {
                this.bankSoalList = new ArrayList<>();
            }
            if (this.idBankSoalList == null) {
                this.idBankSoalList = new ArrayList<>();
            }
            this.bankSoalList.add(bankSoal);
            this.idBankSoalList.add(bankSoal.getIdBankSoal());
            this.updatedAt = Instant.now();
        }
    }

    public void removeBankSoal(String idBankSoal) {
        if (idBankSoal != null) {
            this.idBankSoalList.remove(idBankSoal);
            if (this.bankSoalList != null) {
                this.bankSoalList.removeIf(bs -> idBankSoal.equals(bs.getIdBankSoal()));
            }
            this.updatedAt = Instant.now();
        }
    }

    public boolean hasBankSoal(String idBankSoal) {
        return this.idBankSoalList != null && this.idBankSoalList.contains(idBankSoal);
    }

    // Basic state management methods
    public void activateUjian() {
        this.statusUjian = "AKTIF";
        this.updatedAt = Instant.now();
    }

    public void startUjian() {
        if (isAktif()) {
            this.isLive = true;
            this.updatedAt = Instant.now();
        }
    }

    public void endUjian() {
        this.isLive = false;
        this.statusUjian = "SELESAI";
        this.updatedAt = Instant.now();
    }

    public void cancelUjian() {
        this.isLive = false;
        this.statusUjian = "DIBATALKAN";
        this.updatedAt = Instant.now();
    }

    public Integer getJumlahPeserta() {
        return jumlahPeserta;
    }

    public void setJumlahPeserta(Integer jumlahPeserta) {
        this.jumlahPeserta = jumlahPeserta;
    }

    @Override
    public String toString() {
        return "Ujian{" +
                "idUjian='" + idUjian + '\'' +
                ", namaUjian='" + namaUjian + '\'' +
                ", statusUjian='" + statusUjian + '\'' +
                ", isLive=" + isLive +
                ", isCatEnabled=" + isCatEnabled +
                ", isFlexibleTiming=" + isFlexibleTiming +
                ", waktuMulaiDijadwalkan=" + waktuMulaiDijadwalkan +
                ", waktuSelesaiOtomatis=" + waktuSelesaiOtomatis +
                ", batasAkhirMulai=" + batasAkhirMulai +
                ", bankSoalCount=" + (idBankSoalList != null ? idBankSoalList.size() : 0) +
                '}';
    }
}