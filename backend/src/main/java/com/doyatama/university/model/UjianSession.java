package com.doyatama.university.model;

import java.time.Instant;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class UjianSession {
    private String idSession;
    private String idUjian;
    private String idPeserta;
    private String sessionId;
    private Instant startTime;
    private Instant endTime;
    private String status; // ACTIVE, COMPLETED, TIMEOUT, CANCELLED
    private Integer currentSoalIndex;
    private Map<String, Object> answers;
    private Integer attemptNumber;
    private Instant lastKeepAliveTime;
    private Integer timeRemaining; // in seconds
    private Boolean isSubmitted;
    private Instant submittedAt;
    private Boolean isAutoSubmit;
    private String idSchool;
    private Instant createdAt;
    private Instant updatedAt;

    // Navigation tracking
    private Map<String, Object> navigationHistory;

    // Progress tracking
    private Integer answeredQuestions;
    private Integer totalQuestions;

    // Auto-save tracking
    private Instant lastAutoSave;

    // Additional metadata
    private Map<String, Object> sessionMetadata;

    // Relational data (seperti pattern di model Ujian)
    private Ujian ujian;
    private User peserta;
    private School school;

    // TAMBAHAN: Track violations dalam session
    private List<String> violationIds; // Track IDs of violations
    private Map<String, Object> securityMetadata; // Track security-related data

    // Default constructor
    public UjianSession() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.status = "ACTIVE";
        this.isSubmitted = false;
        this.isAutoSubmit = false;
        this.currentSoalIndex = 0;
        this.attemptNumber = 1;
        this.answeredQuestions = 0;
        this.answers = new HashMap<>();
        this.navigationHistory = new HashMap<>();
        this.sessionMetadata = new HashMap<>();
        this.violationIds = new ArrayList<>();
        this.securityMetadata = new HashMap<>();
    }

    // Constructor with essential fields
    public UjianSession(String idUjian, String idPeserta, String sessionId,
            Integer attemptNumber, String idSchool, Integer timeRemaining) {
        this();
        this.idUjian = idUjian;
        this.idPeserta = idPeserta;
        this.sessionId = sessionId;
        this.attemptNumber = attemptNumber;
        this.idSchool = idSchool;
        this.timeRemaining = timeRemaining;
        this.startTime = Instant.now();
        this.lastKeepAliveTime = Instant.now();
    }

    // Getters and setters (sama seperti model Ujian pattern)
    public String getIdSession() {
        return idSession;
    }

    public void setIdSession(String idSession) {
        this.idSession = idSession;
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

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = Instant.now();
    }

    public Integer getCurrentSoalIndex() {
        return currentSoalIndex;
    }

    public void setCurrentSoalIndex(Integer currentSoalIndex) {
        this.currentSoalIndex = currentSoalIndex;
        this.updatedAt = Instant.now();
    }

    public Map<String, Object> getAnswers() {
        return answers;
    }

    public void addAnswer(String idBankSoal, Object jawaban) {
        if (this.answers == null) {
            this.answers = new HashMap<>();
        }
        this.answers.put(idBankSoal, jawaban);
        this.answeredQuestions = this.answers.size();
        this.updatedAt = java.time.Instant.now();
    }

    public void setAnswers(Map<String, Object> answers) {
        this.answers = answers != null ? answers : new HashMap<>();
        this.answeredQuestions = this.answers.size();
        this.updatedAt = java.time.Instant.now();
    }

    public Integer getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(Integer attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public Instant getLastKeepAliveTime() {
        return lastKeepAliveTime;
    }

    public void setLastKeepAliveTime(Instant lastKeepAliveTime) {
        this.lastKeepAliveTime = lastKeepAliveTime;
        this.updatedAt = Instant.now();
    }

    public Integer getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(Integer timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public Boolean getIsSubmitted() {
        return isSubmitted;
    }

    public void setIsSubmitted(Boolean isSubmitted) {
        this.isSubmitted = isSubmitted;
        if (isSubmitted) {
            this.status = "COMPLETED";
            this.endTime = Instant.now();
            this.submittedAt = Instant.now();
        }
        this.updatedAt = Instant.now();
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Boolean getIsAutoSubmit() {
        return isAutoSubmit;
    }

    public void setIsAutoSubmit(Boolean isAutoSubmit) {
        this.isAutoSubmit = isAutoSubmit;
    }

    public String getIdSchool() {
        return idSchool;
    }

    public void setIdSchool(String idSchool) {
        this.idSchool = idSchool;
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

    public Map<String, Object> getNavigationHistory() {
        return navigationHistory;
    }

    public void setNavigationHistory(Map<String, Object> navigationHistory) {
        this.navigationHistory = navigationHistory;
    }

    public Integer getAnsweredQuestions() {
        return answeredQuestions;
    }

    public void setAnsweredQuestions(Integer answeredQuestions) {
        this.answeredQuestions = answeredQuestions;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Instant getLastAutoSave() {
        return lastAutoSave;
    }

    public void setLastAutoSave(Instant lastAutoSave) {
        this.lastAutoSave = lastAutoSave;
    }

    public Map<String, Object> getSessionMetadata() {
        return sessionMetadata;
    }

    public void setSessionMetadata(Map<String, Object> sessionMetadata) {
        this.sessionMetadata = sessionMetadata;
    }

    // Relational objects (seperti di model Ujian)
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

    // TAMBAHAN: Getters/Setters untuk violation tracking
    public List<String> getViolationIds() {
        return violationIds;
    }

    public void setViolationIds(List<String> violationIds) {
        this.violationIds = violationIds;
    }

    public Map<String, Object> getSecurityMetadata() {
        return securityMetadata;
    }

    public void setSecurityMetadata(Map<String, Object> securityMetadata) {
        this.securityMetadata = securityMetadata;
    }

    // TAMBAHAN: Track cheat detections
    private List<CheatDetection> cheatDetections;

    public List<CheatDetection> getCheatDetections() {
        return cheatDetections;
    }

    public void setCheatDetections(List<CheatDetection> cheatDetections) {
        this.cheatDetections = cheatDetections;
    }

    /**
     * TAMBAHAN: Method untuk add violation ke session
     */
    public void addViolation(CheatDetection detection) {
        if (detection != null && detection.getSessionId().equals(this.sessionId)) {
            if (this.violationIds == null) {
                this.violationIds = new ArrayList<>();
            }
            this.violationIds.add(detection.getIdDetection());

            // Update security metadata
            if (this.securityMetadata == null) {
                this.securityMetadata = new HashMap<>();
            }

            this.securityMetadata.put("totalViolations", this.violationIds.size());
            this.securityMetadata.put("lastViolationAt", detection.getDetectedAt().toString());
            this.securityMetadata.put("hasCriticalViolation",
                    detection.getSeverity().equals("CRITICAL") ||
                            Boolean.TRUE.equals(this.securityMetadata.get("hasCriticalViolation")));

            this.updatedAt = Instant.now();
        }
    }

    /**
     * TAMBAHAN: Method untuk check apakah session harus auto-submit karena
     * violations
     */
    public boolean shouldAutoSubmitDueToViolations() {
        if (this.securityMetadata == null)
            return false;

        Integer totalViolations = (Integer) this.securityMetadata.get("totalViolations");
        Boolean hasCritical = (Boolean) this.securityMetadata.get("hasCriticalViolation");

        return Boolean.TRUE.equals(hasCritical) || (totalViolations != null && totalViolations >= 15);
    }

    /**
     * TAMBAHAN: Method untuk force submit dengan reason
     */
    public void forceSubmit(String reason, String submittedBy) {
        this.isSubmitted = true;
        this.isAutoSubmit = true;
        this.status = "COMPLETED";
        this.submittedAt = Instant.now();
        this.endTime = Instant.now();
        this.updatedAt = Instant.now();

        // Add force submit info to metadata
        if (this.sessionMetadata == null) {
            this.sessionMetadata = new HashMap<>();
        }
        this.sessionMetadata.put("forceSubmitted", true);
        this.sessionMetadata.put("forceSubmitReason", reason);
        this.sessionMetadata.put("forceSubmittedBy", submittedBy);
        this.sessionMetadata.put("forceSubmittedAt", Instant.now().toString());
    }

    // Helper methods (seperti di model Ujian)
    public boolean hasTimedOut() {
        return timeRemaining != null && timeRemaining <= 0;
    }

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(this.status) && !Boolean.TRUE.equals(this.isSubmitted);
    }

    public boolean isCompleted() {
        return "COMPLETED".equalsIgnoreCase(this.status) || Boolean.TRUE.equals(this.isSubmitted);
    }

    public void finalizeSession(boolean isAuto) {
        this.isSubmitted = true;
        this.isAutoSubmit = isAuto;
        this.status = "COMPLETED";
        this.submittedAt = Instant.now();
        this.endTime = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void ping() {
        this.lastKeepAliveTime = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void autoSave() {
        this.lastAutoSave = Instant.now();
        this.updatedAt = Instant.now();
    }

    public double getProgressPercentage() {
        if (totalQuestions == null || totalQuestions == 0) {
            return 0.0;
        }
        return (double) (answeredQuestions != null ? answeredQuestions : 0) / totalQuestions * 100.0;
    }

    /**
     * TAMBAHAN: Method untuk create HasilUjian dari session ini
     */
    public HasilUjian createHasilUjian() {
        HasilUjian hasil = new HasilUjian(this.idUjian, this.idPeserta, this.sessionId, this.idSchool);

        // Sync basic data
        hasil.syncWithSession(this);

        // Set answers
        if (this.answers != null) {
            hasil.setJawabanPeserta(this.answers);
        }

        // Set metadata from session
        Map<String, Object> metadata = hasil.getMetadata();
        if (this.sessionMetadata != null) {
            metadata.put("sessionMetadata", this.sessionMetadata);
        }
        if (this.navigationHistory != null) {
            metadata.put("navigationHistory", this.navigationHistory);
        }
        if (this.securityMetadata != null) {
            metadata.put("securityMetadata", this.securityMetadata);
        }

        metadata.put("answeredQuestions", this.answeredQuestions);
        metadata.put("totalQuestions", this.totalQuestions);
        hasil.setMetadata(metadata);

        // Set relational data
        hasil.setUjian(this.ujian);
        hasil.setPeserta(this.peserta);
        hasil.setSchool(this.school);

        return hasil;
    }

    @Override
    public String toString() {
        return "UjianSession{" +
                "idSession='" + idSession + '\'' +
                ", idUjian='" + idUjian + '\'' +
                ", idPeserta='" + idPeserta + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", status='" + status + '\'' +
                ", currentSoalIndex=" + currentSoalIndex +
                ", attemptNumber=" + attemptNumber +
                ", answeredQuestions=" + answeredQuestions +
                ", totalQuestions=" + totalQuestions +
                ", timeRemaining=" + timeRemaining +
                ", isSubmitted=" + isSubmitted +
                ", isAutoSubmit=" + isAutoSubmit +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
