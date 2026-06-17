package com.doyatama.university.model;

import java.time.Instant;
import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HasilUjian {
    // Basic Information
    private String idHasilUjian;
    private String idUjian;
    private String idPeserta;
    private String sessionId;
    private String idSchool;
    private Integer attemptNumber;
    private String statusPengerjaan; // SELESAI, TIMEOUT, DIBATALKAN
    private Boolean isAutoSubmit;

    // Timing Information
    private Instant waktuMulai;
    private Instant waktuSelesai;
    private Integer durasiPengerjaan; // in seconds
    private Integer sisaWaktu; // in seconds

    // Answer Data
    private Map<String, Object> jawabanPeserta; // idBankSoal -> jawaban
    private Map<String, Boolean> jawabanBenar; // idBankSoal -> true/false
    private Map<String, Double> skorPerSoal; // idBankSoal -> skor

    // Score Information
    private Double totalSkor;
    private Double skorMaksimal;
    private Double persentase;
    private String nilaiHuruf; // A, B, C, D, E
    private Boolean lulus;

    // Analysis Data
    private Integer jumlahBenar;
    private Integer jumlahSalah;
    private Integer jumlahKosong;
    private Integer totalSoal;

    // Time Analytics (NEW FIELDS)
    private Map<String, Integer> timeSpentPerQuestion; // idBankSoal -> seconds
    private Map<String, Instant> answerTimestamps; // idBankSoal -> timestamp
    private Map<String, Object> answerHistory; // idBankSoal -> List<String> (jawaban changes)
    private Map<String, Integer> attemptCountPerQuestion; // idBankSoal -> count

    // Behavioral Analytics (NEW FIELDS)
    private String workingPattern; // RUSHED, THOROUGH, NORMAL, INCONSISTENT
    private Double consistencyScore; // 0.0 - 1.0
    private Boolean hasSignsOfGuessing;
    private Boolean hasSignsOfAnxiety;
    private String confidenceLevel; // HIGH, MEDIUM, LOW

    // Performance Insights (NEW FIELDS)
    private Map<String, Object> topicPerformance; // topic -> score percentage
    private Map<String, Object> conceptMastery; // concept -> mastery level
    private List<String> strengths; // list of strong areas
    private List<String> weaknesses; // list of weak areas
    private List<String> recommendedStudyAreas;

    // Answer Pattern Analysis (NEW FIELDS)
    private Integer totalAnswerChanges;
    private Double answerChangeSuccessRate; // percentage of beneficial changes
    private List<String> frequentlyChangedQuestions;
    private Map<String, Object> changePatterns;

    // Learning Analytics (NEW FIELDS)
    private String learningStyle; // VISUAL, AUDITORY, KINESTHETIC, MIXED
    private Map<String, Object> learningInsights;
    private List<String> studyStrategies;
    private Double adaptiveDifficultyScore;

    // Comparative Analysis (NEW FIELDS)
    private Integer rankInClass;
    private Integer rankInSchool;
    private Double percentileRank;
    private String relativePerformance; // ABOVE_AVERAGE, AVERAGE, BELOW_AVERAGE

    // Security & Integrity
    private String resultHash;
    private Boolean isVerified;
    private String verifiedBy;
    private Instant verificationTime;
    private String securityStatus; // CLEAN, FLAGGED, SUSPICIOUS
    private Map<String, Object> securityFlags;

    // Quality Assurance
    private Boolean hasAppeal;
    private String appealReason;
    private String appealStatus; // PENDING, APPROVED, REJECTED
    private Instant appealSubmittedAt;
    private String appealReviewedBy;
    private Map<String, Object> appealData;

    // Metadata & Relations
    private Map<String, Object> metadata;
    private Instant createdAt;
    private Instant updatedAt;

    // Relationship Objects
    private Ujian ujian;
    private User peserta;
    private School school;

    // TAMBAHAN: Fields untuk violation tracking
    private String violationType;
    private String violationSeverity;
    private Integer violationCount;
    private Instant flaggedAt;

    // Tambahan: relasi ke CheatDetection
    private List<String> violationIds; // Menyimpan ID pelanggaran dari CheatDetection
    private List<CheatDetection> cheatDetections; // (opsional) Menyimpan objek CheatDetection

    // Constructors
    public HasilUjian() {
        this.jawabanPeserta = new HashMap<>();
        this.jawabanBenar = new HashMap<>();
        this.skorPerSoal = new HashMap<>();
        this.timeSpentPerQuestion = new HashMap<>();
        this.answerTimestamps = new HashMap<>();
        this.answerHistory = new HashMap<>();
        this.attemptCountPerQuestion = new HashMap<>();
        this.topicPerformance = new HashMap<>();
        this.conceptMastery = new HashMap<>();
        this.strengths = new ArrayList<>();
        this.weaknesses = new ArrayList<>();
        this.recommendedStudyAreas = new ArrayList<>();
        this.frequentlyChangedQuestions = new ArrayList<>();
        this.changePatterns = new HashMap<>();
        this.learningInsights = new HashMap<>();
        this.studyStrategies = new ArrayList<>();
        this.securityFlags = new HashMap<>();
        this.appealData = new HashMap<>();
        this.metadata = new HashMap<>();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.isVerified = false;
        this.hasAppeal = false;
        this.securityStatus = "CLEAN";
    }

    // TAMBAHAN: Constructor untuk kompatibilitas dengan UjianSession
    public HasilUjian(String idUjian, String idPeserta, String sessionId, String idSchool) {
        this(); // Call default constructor
        this.idUjian = idUjian;
        this.idPeserta = idPeserta;
        this.sessionId = sessionId;
        this.idSchool = idSchool;
        this.idHasilUjian = UUID.randomUUID().toString();
    }

    // Basic Getters and Setters
    public String getIdHasilUjian() {
        return idHasilUjian;
    }

    public void setIdHasilUjian(String idHasilUjian) {
        this.idHasilUjian = idHasilUjian;
    }

    public String getIdUjian() {
        return idUjian;
    }

    public void setIdUjian(String idUjian) {
        this.idUjian = idUjian;
    }

    public String getIdPeserta() {
        return idPeserta;
    }

    public void setIdPeserta(String idPeserta) {
        this.idPeserta = idPeserta;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getIdSchool() {
        return idSchool;
    }

    public void setIdSchool(String idSchool) {
        this.idSchool = idSchool;
    }

    public Integer getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(Integer attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public String getStatusPengerjaan() {
        return statusPengerjaan;
    }

    public void setStatusPengerjaan(String statusPengerjaan) {
        this.statusPengerjaan = statusPengerjaan;
    }

    public Boolean getIsAutoSubmit() {
        return isAutoSubmit;
    }

    public void setIsAutoSubmit(Boolean isAutoSubmit) {
        this.isAutoSubmit = isAutoSubmit;
    }

    // Timing Getters and Setters
    public Instant getWaktuMulai() {
        return waktuMulai;
    }

    public void setWaktuMulai(Instant waktuMulai) {
        this.waktuMulai = waktuMulai;
    }

    public Instant getWaktuSelesai() {
        return waktuSelesai;
    }

    public void setWaktuSelesai(Instant waktuSelesai) {
        this.waktuSelesai = waktuSelesai;
    }

    public Integer getDurasiPengerjaan() {
        return durasiPengerjaan;
    }

    public void setDurasiPengerjaan(Integer durasiPengerjaan) {
        this.durasiPengerjaan = durasiPengerjaan;
    }

    public Integer getSisaWaktu() {
        return sisaWaktu;
    }

    public void setSisaWaktu(Integer sisaWaktu) {
        this.sisaWaktu = sisaWaktu;
    }

    // Answer Data Getters and Setters
    public Map<String, Object> getJawabanPeserta() {
        return jawabanPeserta;
    }

    public void setJawabanPeserta(Map<String, Object> jawabanPeserta) {
        this.jawabanPeserta = jawabanPeserta;
    }

    public Map<String, Boolean> getJawabanBenar() {
        return jawabanBenar;
    }

    public void setJawabanBenar(Map<String, Boolean> jawabanBenar) {
        this.jawabanBenar = jawabanBenar;
    }

    public Map<String, Double> getSkorPerSoal() {
        return skorPerSoal;
    }

    public void setSkorPerSoal(Map<String, Double> skorPerSoal) {
        this.skorPerSoal = skorPerSoal;
    }

    // Score Information Getters and Setters
    public Double getTotalSkor() {
        return totalSkor;
    }

    public void setTotalSkor(Double totalSkor) {
        this.totalSkor = totalSkor;
    }

    public Double getSkorMaksimal() {
        return skorMaksimal;
    }

    public void setSkorMaksimal(Double skorMaksimal) {
        this.skorMaksimal = skorMaksimal;
    }

    public Double getPersentase() {
        return persentase;
    }

    public void setPersentase(Double persentase) {
        this.persentase = persentase;
    }

    public String getNilaiHuruf() {
        return nilaiHuruf;
    }

    public void setNilaiHuruf(String nilaiHuruf) {
        this.nilaiHuruf = nilaiHuruf;
    }

    public Boolean getLulus() {
        return lulus;
    }

    public void setLulus(Boolean lulus) {
        this.lulus = lulus;
    }

    // Analysis Data Getters and Setters
    public Integer getJumlahBenar() {
        return jumlahBenar;
    }

    public void setJumlahBenar(Integer jumlahBenar) {
        this.jumlahBenar = jumlahBenar;
    }

    public Integer getJumlahSalah() {
        return jumlahSalah;
    }

    public void setJumlahSalah(Integer jumlahSalah) {
        this.jumlahSalah = jumlahSalah;
    }

    public Integer getJumlahKosong() {
        return jumlahKosong;
    }

    public void setJumlahKosong(Integer jumlahKosong) {
        this.jumlahKosong = jumlahKosong;
    }

    public Integer getTotalSoal() {
        return totalSoal;
    }

    public void setTotalSoal(Integer totalSoal) {
        this.totalSoal = totalSoal;
    }

    // NEW ANALYTICS GETTERS AND SETTERS

    // Time Analytics
    public Map<String, Integer> getTimeSpentPerQuestion() {
        return timeSpentPerQuestion;
    }

    public void setTimeSpentPerQuestion(Map<String, Integer> timeSpentPerQuestion) {
        this.timeSpentPerQuestion = timeSpentPerQuestion;
    }

    public Map<String, Instant> getAnswerTimestamps() {
        return answerTimestamps;
    }

    public void setAnswerTimestamps(Map<String, Instant> answerTimestamps) {
        this.answerTimestamps = answerTimestamps;
    }

    public Map<String, Object> getAnswerHistory() {
        return answerHistory;
    }

    public void setAnswerHistory(Map<String, Object> answerHistory) {
        this.answerHistory = answerHistory;
    }

    public Map<String, Integer> getAttemptCountPerQuestion() {
        return attemptCountPerQuestion;
    }

    public void setAttemptCountPerQuestion(Map<String, Integer> attemptCountPerQuestion) {
        this.attemptCountPerQuestion = attemptCountPerQuestion;
    }

    // Behavioral Analytics
    public String getWorkingPattern() {
        return workingPattern;
    }

    public void setWorkingPattern(String workingPattern) {
        this.workingPattern = workingPattern;
    }

    public Double getConsistencyScore() {
        return consistencyScore;
    }

    public void setConsistencyScore(Double consistencyScore) {
        this.consistencyScore = consistencyScore;
    }

    public Boolean getHasSignsOfGuessing() {
        return hasSignsOfGuessing;
    }

    public void setHasSignsOfGuessing(Boolean hasSignsOfGuessing) {
        this.hasSignsOfGuessing = hasSignsOfGuessing;
    }

    public Boolean getHasSignsOfAnxiety() {
        return hasSignsOfAnxiety;
    }

    public void setHasSignsOfAnxiety(Boolean hasSignsOfAnxiety) {
        this.hasSignsOfAnxiety = hasSignsOfAnxiety;
    }

    public String getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(String confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    // Performance Insights
    public Map<String, Object> getTopicPerformance() {
        return topicPerformance;
    }

    public void setTopicPerformance(Map<String, Object> topicPerformance) {
        this.topicPerformance = topicPerformance;
    }

    public Map<String, Object> getConceptMastery() {
        return conceptMastery;
    }

    public void setConceptMastery(Map<String, Object> conceptMastery) {
        this.conceptMastery = conceptMastery;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    public List<String> getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(List<String> weaknesses) {
        this.weaknesses = weaknesses;
    }

    public List<String> getRecommendedStudyAreas() {
        return recommendedStudyAreas;
    }

    public void setRecommendedStudyAreas(List<String> recommendedStudyAreas) {
        this.recommendedStudyAreas = recommendedStudyAreas;
    }

    // Answer Pattern Analysis
    public Integer getTotalAnswerChanges() {
        return totalAnswerChanges;
    }

    public void setTotalAnswerChanges(Integer totalAnswerChanges) {
        this.totalAnswerChanges = totalAnswerChanges;
    }

    public Double getAnswerChangeSuccessRate() {
        return answerChangeSuccessRate;
    }

    public void setAnswerChangeSuccessRate(Double answerChangeSuccessRate) {
        this.answerChangeSuccessRate = answerChangeSuccessRate;
    }

    public List<String> getFrequentlyChangedQuestions() {
        return frequentlyChangedQuestions;
    }

    public void setFrequentlyChangedQuestions(List<String> frequentlyChangedQuestions) {
        this.frequentlyChangedQuestions = frequentlyChangedQuestions;
    }

    public Map<String, Object> getChangePatterns() {
        return changePatterns;
    }

    public void setChangePatterns(Map<String, Object> changePatterns) {
        this.changePatterns = changePatterns;
    }

    // Learning Analytics
    public String getLearningStyle() {
        return learningStyle;
    }

    public void setLearningStyle(String learningStyle) {
        this.learningStyle = learningStyle;
    }

    public Map<String, Object> getLearningInsights() {
        return learningInsights;
    }

    public void setLearningInsights(Map<String, Object> learningInsights) {
        this.learningInsights = learningInsights;
    }

    public List<String> getStudyStrategies() {
        return studyStrategies;
    }

    public void setStudyStrategies(List<String> studyStrategies) {
        this.studyStrategies = studyStrategies;
    }

    public Double getAdaptiveDifficultyScore() {
        return adaptiveDifficultyScore;
    }

    public void setAdaptiveDifficultyScore(Double adaptiveDifficultyScore) {
        this.adaptiveDifficultyScore = adaptiveDifficultyScore;
    }

    // Comparative Analysis
    public Integer getRankInClass() {
        return rankInClass;
    }

    public void setRankInClass(Integer rankInClass) {
        this.rankInClass = rankInClass;
    }

    public Integer getRankInSchool() {
        return rankInSchool;
    }

    public void setRankInSchool(Integer rankInSchool) {
        this.rankInSchool = rankInSchool;
    }

    public Double getPercentileRank() {
        return percentileRank;
    }

    public void setPercentileRank(Double percentileRank) {
        this.percentileRank = percentileRank;
    }

    public String getRelativePerformance() {
        return relativePerformance;
    }

    public void setRelativePerformance(String relativePerformance) {
        this.relativePerformance = relativePerformance;
    }

    // Security & Integrity Getters and Setters
    public String getResultHash() {
        return resultHash;
    }

    public void setResultHash(String resultHash) {
        this.resultHash = resultHash;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public Instant getVerificationTime() {
        return verificationTime;
    }

    public void setVerificationTime(Instant verificationTime) {
        this.verificationTime = verificationTime;
    }

    public String getSecurityStatus() {
        return securityStatus;
    }

    public void setSecurityStatus(String securityStatus) {
        this.securityStatus = securityStatus;
    }

    public Map<String, Object> getSecurityFlags() {
        return securityFlags;
    }

    public void setSecurityFlags(Map<String, Object> securityFlags) {
        this.securityFlags = securityFlags;
    }

    // Quality Assurance Getters and Setters
    public Boolean getHasAppeal() {
        return hasAppeal;
    }

    public void setHasAppeal(Boolean hasAppeal) {
        this.hasAppeal = hasAppeal;
    }

    public String getAppealReason() {
        return appealReason;
    }

    public void setAppealReason(String appealReason) {
        this.appealReason = appealReason;
    }

    public String getAppealStatus() {
        return appealStatus;
    }

    public void setAppealStatus(String appealStatus) {
        this.appealStatus = appealStatus;
    }

    public Instant getAppealSubmittedAt() {
        return appealSubmittedAt;
    }

    public void setAppealSubmittedAt(Instant appealSubmittedAt) {
        this.appealSubmittedAt = appealSubmittedAt;
    }

    public String getAppealReviewedBy() {
        return appealReviewedBy;
    }

    public void setAppealReviewedBy(String appealReviewedBy) {
        this.appealReviewedBy = appealReviewedBy;
    }

    public Map<String, Object> getAppealData() {
        return appealData;
    }

    public void setAppealData(Map<String, Object> appealData) {
        this.appealData = appealData;
    }

    // Metadata & Relations Getters and Setters
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
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

    // Relationship Objects Getters and Setters
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

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    // TAMBAHAN: Method untuk menambahkan cheat detection ke metadata
    public void addCheatDetection(CheatDetection detection) {
        Map<String, Object> metadata = this.getMetadata();
        if (metadata == null) {
            metadata = new HashMap<>();
            this.setMetadata(metadata);
        }

        // Add cheat detection info
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> violations = (List<Map<String, Object>>) metadata.get("violations");
        if (violations == null) {
            violations = new ArrayList<>();
            metadata.put("violations", violations);
        }

        Map<String, Object> violationData = new HashMap<>();
        violationData.put("idDetection", detection.getIdDetection());
        violationData.put("typeViolation", detection.getTypeViolation());
        violationData.put("severity", detection.getSeverity());
        violationData.put("detectedAt", detection.getDetectedAt().toString());
        violationData.put("violationCount", detection.getViolationCount());

        violations.add(violationData);

        // Update security status based on violations
        updateSecurityStatusFromViolations();
    }

    /**
     * TAMBAHAN: Method untuk update security status berdasarkan violations
     */
    private void updateSecurityStatusFromViolations() {
        Map<String, Object> metadata = this.getMetadata();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> violations = (List<Map<String, Object>>) metadata.get("violations");

        if (violations == null || violations.isEmpty()) {
            this.setSecurityStatus("CLEAN");
            return;
        }

        // Count critical and high severity violations
        long criticalCount = violations.stream()
                .filter(v -> "CRITICAL".equals(v.get("severity")))
                .count();

        long highCount = violations.stream()
                .filter(v -> "HIGH".equals(v.get("severity")))
                .count();

        if (criticalCount > 0) {
            this.setSecurityStatus("FLAGGED");
        } else if (highCount > 2 || violations.size() > 5) {
            this.setSecurityStatus("SUSPICIOUS");
        } else {
            this.setSecurityStatus("MONITORED");
        }
    }

    /**
     * TAMBAHAN: Method untuk sync dengan UjianSession
     */
    public void syncWithSession(UjianSession session) {
        if (session != null) {
            this.setWaktuMulai(session.getStartTime());
            this.setWaktuSelesai(session.getEndTime());
            this.setIsAutoSubmit(session.getIsAutoSubmit());
            this.setAttemptNumber(session.getAttemptNumber());

            // Sync navigation dan timing data
            if (session.getNavigationHistory() != null) {
                Map<String, Object> metadata = this.getMetadata();
                metadata.put("navigationHistory", session.getNavigationHistory());
                this.setMetadata(metadata);
            }

            // Calculate duration
            if (session.getStartTime() != null && session.getEndTime() != null) {
                long duration = session.getEndTime().getEpochSecond() - session.getStartTime().getEpochSecond();
                this.setDurasiPengerjaan((int) duration);
            }

            this.setSisaWaktu(session.getTimeRemaining());
        }
    }

    // TAMBAHAN: Getters/Setters untuk violation fields
    public String getViolationType() {
        return violationType;
    }

    public void setViolationType(String violationType) {
        this.violationType = violationType;
    }

    public String getViolationSeverity() {
        return violationSeverity;
    }

    public void setViolationSeverity(String violationSeverity) {
        this.violationSeverity = violationSeverity;
    }

    public Integer getViolationCount() {
        return violationCount;
    }

    public void setViolationCount(Integer violationCount) {
        this.violationCount = violationCount;
    }

    public Instant getFlaggedAt() {
        return flaggedAt;
    }

    public void setFlaggedAt(Instant flaggedAt) {
        this.flaggedAt = flaggedAt;
    }

    // Getters and Setters for CheatDetection relations
    public List<String> getViolationIds() {
        return violationIds;
    }

    public void setViolationIds(List<String> violationIds) {
        this.violationIds = violationIds;
    }

    public List<CheatDetection> getCheatDetections() {
        return cheatDetections;
    }

    public void setCheatDetections(List<CheatDetection> cheatDetections) {
        this.cheatDetections = cheatDetections;
    }

    // Business Logic Methods
    public void calculateGrade() {
        if (persentase == null) {
            nilaiHuruf = "E";
            return;
        }

        if (persentase >= 90) {
            nilaiHuruf = "A";
        } else if (persentase >= 80) {
            nilaiHuruf = "B";
        } else if (persentase >= 70) {
            nilaiHuruf = "C";
        } else if (persentase >= 60) {
            nilaiHuruf = "D";
        } else {
            nilaiHuruf = "E";
        }
    }

    public void determinePassStatus(double minPassingScore) {
        this.lulus = persentase != null && persentase >= minPassingScore;
    }

    public void generateSecurityHash() {
        try {
            String dataToHash = String.format("%s:%s:%s:%s:%s",
                    idHasilUjian, totalSkor, persentase, jawabanPeserta.toString(), createdAt.toString());

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(dataToHash.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            this.resultHash = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            this.resultHash = "HASH_ERROR";
        }
    }

    public boolean validateIntegrity() {
        String currentHash = this.resultHash;
        generateSecurityHash();
        return currentHash != null && currentHash.equals(this.resultHash);
    }

    public void performCompleteAnalysis() {
        // Initialize default values for all analytics fields to prevent NPE
        if (this.totalAnswerChanges == null)
            this.totalAnswerChanges = 0;
        if (this.answerChangeSuccessRate == null)
            this.answerChangeSuccessRate = 0.0;
        if (this.workingPattern == null)
            this.workingPattern = "UNKNOWN";
        if (this.hasSignsOfGuessing == null)
            this.hasSignsOfGuessing = false;
        if (this.hasSignsOfAnxiety == null)
            this.hasSignsOfAnxiety = false;
        if (this.consistencyScore == null)
            this.consistencyScore = 0.0;

        analyzeAnswerPatterns();
        analyzeWorkingPatterns();
        analyzeGuessingSigns();
        analyzeAnxietySigns();
        analyzeConsistency();
    }// Analytics Helper Methods

    private void analyzeAnswerPatterns() {
        if (answerHistory == null || answerHistory.isEmpty()) {
            // Initialize with default values if answerHistory is empty
            this.totalAnswerChanges = 0;
            this.answerChangeSuccessRate = 0.0;
            return;
        }

        int totalChanges = 0;
        int beneficialChanges = 0;

        for (Map.Entry<String, Object> entry : answerHistory.entrySet()) {
            String questionId = entry.getKey();

            @SuppressWarnings("unchecked")
            List<String> history = (List<String>) entry.getValue();

            if (history != null && history.size() > 1) {
                totalChanges += (history.size() - 1);

                String finalAnswer = history.get(history.size() - 1);
                String firstAnswer = history.get(0);

                Boolean finalCorrect = jawabanBenar.get(questionId);
                if (finalCorrect != null && finalCorrect) {
                    if (!finalAnswer.equals(firstAnswer)) {
                        beneficialChanges++;
                    }
                }

                if (history.size() > 3) {
                    frequentlyChangedQuestions.add(questionId);
                }
            }
        }

        this.totalAnswerChanges = totalChanges;
        this.answerChangeSuccessRate = totalChanges > 0 ? (double) beneficialChanges / totalChanges * 100.0 : 0.0;
    }

    private void analyzeWorkingPatterns() {
        if (timeSpentPerQuestion == null || timeSpentPerQuestion.isEmpty()) {
            this.workingPattern = "UNKNOWN";
            return;
        }

        List<Integer> times = new ArrayList<>(timeSpentPerQuestion.values());
        double avgTime = times.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        double variance = times.stream().mapToDouble(time -> Math.pow(time - avgTime, 2)).average().orElse(0.0);
        double stdDev = Math.sqrt(variance);

        if (avgTime < 30) {
            this.workingPattern = "RUSHED";
        } else if (avgTime > 120) {
            this.workingPattern = "THOROUGH";
        } else if (stdDev > avgTime * 0.5) {
            this.workingPattern = "INCONSISTENT";
        } else {
            this.workingPattern = "NORMAL";
        }
    }

    private void analyzeGuessingSigns() {
        if (timeSpentPerQuestion == null || answerHistory == null) {
            this.hasSignsOfGuessing = false;
            return;
        }

        int quickAnswers = 0;
        int frequentChanges = 0;

        for (Map.Entry<String, Integer> entry : timeSpentPerQuestion.entrySet()) {
            if (entry.getValue() < 15) { // Less than 15 seconds
                quickAnswers++;
            }
        }

        for (Map.Entry<String, Object> entry : answerHistory.entrySet()) {
            @SuppressWarnings("unchecked")
            List<String> history = (List<String>) entry.getValue();
            if (history != null && history.size() > 2) {
                frequentChanges++;
            }
        }

        this.hasSignsOfGuessing = (quickAnswers > totalSoal * 0.3) || (frequentChanges > totalSoal * 0.2);
    }

    private void analyzeAnxietySigns() {
        if (answerHistory == null || timeSpentPerQuestion == null) {
            this.hasSignsOfAnxiety = false;
            return;
        }

        int anxietyIndicators = 0;

        // Check if totalAnswerChanges is null before using it
        if (this.totalAnswerChanges != null && this.totalAnswerChanges > this.totalSoal * 0.4) {
            anxietyIndicators++;
        }

        if (this.consistencyScore != null && this.consistencyScore < 0.4) {
            anxietyIndicators++;
        }

        long quickChanges = this.answerHistory.entrySet().stream()
                .filter(entry -> {
                    @SuppressWarnings("unchecked")
                    List<String> history = (List<String>) entry.getValue();
                    return history != null && history.size() > 2;
                })
                .count();

        if (quickChanges > this.totalSoal * 0.3) {
            anxietyIndicators++;
        }

        this.hasSignsOfAnxiety = anxietyIndicators >= 2;
    }

    private void analyzeConsistency() {
        if (timeSpentPerQuestion == null || timeSpentPerQuestion.isEmpty()) {
            this.consistencyScore = 0.0;
            return;
        }

        List<Integer> times = new ArrayList<>(timeSpentPerQuestion.values());
        double mean = times.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        double variance = times.stream().mapToDouble(time -> Math.pow(time - mean, 2)).average().orElse(0.0);
        double stdDev = Math.sqrt(variance);

        // Consistency score: lower standard deviation relative to mean = higher
        // consistency
        this.consistencyScore = mean > 0 ? Math.max(0.0, 1.0 - (stdDev / mean)) : 0.0;
    }

    @Override
    public String toString() {
        return "HasilUjian{" +
                "idHasilUjian='" + idHasilUjian + '\'' +
                ", idUjian='" + idUjian + '\'' +
                ", idPeserta='" + idPeserta + '\'' +
                ", totalSkor=" + totalSkor +
                ", persentase=" + persentase +
                ", nilaiHuruf='" + nilaiHuruf + '\'' +
                ", lulus=" + lulus +
                '}';
    }
}