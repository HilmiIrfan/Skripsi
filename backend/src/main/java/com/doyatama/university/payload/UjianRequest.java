package com.doyatama.university.payload;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class UjianRequest {
    private String idUjian;
    private String namaUjian;
    private String deskripsi;
    private Integer durasiMenit;
    private Instant waktuMulaiDijadwalkan;
    private Instant waktuSelesaiOtomatis;
    private String statusUjian;
    private Boolean isLive;
    private Boolean isAutoStart;
    private Boolean isAutoEnd;
    private Integer jumlahSoal;
    private Double totalBobot;
    private String tipeSoal;
    private Boolean tampilkanNilai;

    // A list of BankSoal IDs to be included in the exam.
    private List<String> idBankSoalList;

    private Map<String, Object> pengaturan;
    private Instant createdAt;

    // CAT Settings
    private Boolean isCatEnabled;
    private Map<String, Object> catSettings;
    private Boolean allowLateStart;
    private Integer maxLateStartMinutes;
    private Boolean showTimerToParticipants;
    private Boolean preventCheating;

    // Flexible Timing Settings
    private Boolean isFlexibleTiming;
    private Instant batasAkhirMulai;
    private Boolean autoEndAfterDuration;
    private Integer toleransiKeterlambatanMenit;

    // Scoring and Review Settings
    private String strategiPemilihanSoal;
    private Double minPassingScore;
    private Boolean allowReview;
    private Boolean allowBacktrack;
    private Integer maxAttempts;

    // Relational data IDs (The main context for the entire exam)
    private String idTahun;
    private String idKelas;
    private String idSemester;
    private String idMapel;
    private String idKonsentrasiKeahlianSekolah;
    private String idCreatedBy;
    private String idSchool;

    // Note: idElemen, idAcp, dan idAtp have been removed as an exam can cover
    // multiple of these.
    // This information resides within each individual BankSoal, not at the exam
    // level.

    public UjianRequest() {
        // Default constructor
    }

    // --- Getters and Setters for all fields ---

    public String getIdUjian() {
        return idUjian;
    }

    public void setIdUjian(String idUjian) {
        this.idUjian = idUjian;
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

    public List<String> getIdBankSoalList() {
        return idBankSoalList;
    }

    public void setIdBankSoalList(List<String> idBankSoalList) {
        this.idBankSoalList = idBankSoalList;
    }

    public Map<String, Object> getPengaturan() {
        return pengaturan;
    }

    public void setPengaturan(Map<String, Object> pengaturan) {
        this.pengaturan = pengaturan;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

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

    public String getIdTahun() {
        return idTahun;
    }

    public void setIdTahun(String idTahun) {
        this.idTahun = idTahun;
    }

    public String getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(String idKelas) {
        this.idKelas = idKelas;
    }

    public String getIdSemester() {
        return idSemester;
    }

    public void setIdSemester(String idSemester) {
        this.idSemester = idSemester;
    }

    public String getIdMapel() {
        return idMapel;
    }

    public void setIdMapel(String idMapel) {
        this.idMapel = idMapel;
    }

    public String getIdKonsentrasiKeahlianSekolah() {
        return idKonsentrasiKeahlianSekolah;
    }

    public void setIdKonsentrasiKeahlianSekolah(String idKonsentrasiKeahlianSekolah) {
        this.idKonsentrasiKeahlianSekolah = idKonsentrasiKeahlianSekolah;
    }

    public String getIdCreatedBy() {
        return idCreatedBy;
    }

    public void setIdCreatedBy(String idCreatedBy) {
        this.idCreatedBy = idCreatedBy;
    }

    public String getIdSchool() {
        return idSchool;
    }

    public void setIdSchool(String idSchool) {
        this.idSchool = idSchool;
    }
}