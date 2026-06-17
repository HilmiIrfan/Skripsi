package com.doyatama.university.model;

import java.time.Instant;
import java.util.Map;
import java.util.HashMap;
import com.doyatama.university.constants.ViolationType;
import com.doyatama.university.constants.SeverityLevel;
import com.doyatama.university.constants.ActionType;

public class CheatDetection {
    private String idDetection;
    private String sessionId;
    private String idPeserta;
    private String idUjian;
    private String idSchool;

    // Detection details - FOKUS UNTUK LAB KOMPUTER
    private String typeViolation;
    // TAB_SWITCH, WINDOW_BLUR, COPY_PASTE, RIGHT_CLICK, KEYBOARD_SHORTCUT,
    // FULLSCREEN_EXIT, BROWSER_DEV_TOOLS, PRINT_SCREEN, ALT_TAB, CTRL_C_V

    private String severity; // LOW, MEDIUM, HIGH, CRITICAL
    private Integer violationCount;
    private Instant detectedAt;
    private Instant firstDetectedAt;

    // Evidence data - YANG RELEVAN UNTUK LAB
    private Map<String, Object> evidence; // Screenshots, keystroke logs, window events, etc
    private String browserInfo; // Browser type dan version
    private String userAgent; // Untuk deteksi browser manipulation

    // HAPUS: deviceFingerprint (tidak relevan untuk lab)
    // HAPUS: ipAddress (sama semua di lab)

    // Frontend Detection Data - TAMBAHAN UNTUK FRONTEND MONITORING
    private Map<String, Object> frontendEvents; // Window focus/blur, key events, mouse events
    private String windowTitle; // Title window yang aktif saat violation
    private Integer screenWidth; // Untuk deteksi screen sharing atau remote access
    private Integer screenHeight;
    private Boolean fullscreenStatus; // Status fullscreen saat detection

    // Timing Analysis - PENTING UNTUK DETEKSI PATTERN
    private Long timeBetweenAnswers; // Waktu antar jawaban (ms)
    private Map<String, Long> answerTimestamps; // Timestamp setiap jawaban
    private String answerPattern; // Pattern jawaban (sequential, random, etc)

    // Action taken
    private String actionTaken; // WARNING, LOCK_EXAM, TERMINATE_SESSION, FLAGGED_FOR_REVIEW, AUTO_SUBMIT
    private String actionBy; // SYSTEM, PROCTOR, ADMIN
    private Instant actionAt;
    private String actionReason;

    // Status
    private Boolean resolved;
    private String resolvedBy;
    private Instant resolvedAt;
    private String resolutionNotes;

    // Timestamps
    private Instant createdAt;
    private Instant updatedAt;

    // Relationships
    private UjianSession ujianSession;
    private User peserta;
    private Ujian ujian;
    private School school; // Constructors

    public CheatDetection() {
        this.evidence = new HashMap<>();
        this.frontendEvents = new HashMap<>();
        this.answerTimestamps = new HashMap<>();
        this.resolved = false;
        this.violationCount = 1;
        this.fullscreenStatus = true; // Default assume fullscreen
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.detectedAt = Instant.now(); // Ensure detectedAt is never null
        this.firstDetectedAt = Instant.now(); // Ensure firstDetectedAt is never null
    }

    public CheatDetection(String sessionId, String idPeserta, String idUjian, String idSchool,
            String typeViolation, String severity) {
        this();
        this.sessionId = sessionId;
        this.idPeserta = idPeserta;
        this.idUjian = idUjian;
        this.idSchool = idSchool;
        this.typeViolation = typeViolation;
        this.severity = severity;
        this.detectedAt = Instant.now();
        this.firstDetectedAt = Instant.now();
    }

    // Business Methods - UPDATED UNTUK LAB CONTEXT
    public void incrementViolationCount() {
        this.violationCount++;
        this.updatedAt = Instant.now();

        // Auto-escalate severity jika violation count tinggi
        if (this.violationCount >= 5 && "LOW".equals(this.severity)) {
            this.severity = "MEDIUM";
        } else if (this.violationCount >= 10 && "MEDIUM".equals(this.severity)) {
            this.severity = "HIGH";
        }
    }

    public void addEvidence(String key, Object value) {
        if (this.evidence == null) {
            this.evidence = new HashMap<>();
        }
        this.evidence.put(key, value);
        this.evidence.put("evidenceTimestamp_" + key, Instant.now().toString());
        this.updatedAt = Instant.now();
    }

    public void addFrontendEvent(String eventType, Object eventData) {
        if (this.frontendEvents == null) {
            this.frontendEvents = new HashMap<>();
        }

        String timestamp = Instant.now().toString();
        this.frontendEvents.put(eventType + "_" + timestamp, eventData);
        this.updatedAt = Instant.now();
    }

    public void recordAnswerTime(String questionId, Long timestamp) {
        if (this.answerTimestamps == null) {
            this.answerTimestamps = new HashMap<>();
        }
        this.answerTimestamps.put(questionId, timestamp);

        // Calculate time between answers if we have previous answer
        if (this.answerTimestamps.size() > 1) {
            Long[] timestamps = this.answerTimestamps.values().toArray(new Long[0]);
            if (timestamps.length >= 2) {
                this.timeBetweenAnswers = Math
                        .abs(timestamps[timestamps.length - 1] - timestamps[timestamps.length - 2]);
            }
        }

        this.updatedAt = Instant.now();
    }

    public void takeAction(String action, String actionBy, String reason) {
        this.actionTaken = action;
        this.actionBy = actionBy;
        this.actionReason = reason;
        this.actionAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void resolve(String resolvedBy, String notes) {
        this.resolved = true;
        this.resolvedBy = resolvedBy;
        this.resolvedAt = Instant.now();
        this.resolutionNotes = notes;
        this.updatedAt = Instant.now();
    }

    public boolean isCritical() {
        return "CRITICAL".equals(this.severity);
    }

    public boolean isResolved() {
        return this.resolved != null && this.resolved;
    }

    public boolean requiresImmediateAction() {
        return isCritical() || this.violationCount >= 15;
    }

    // COMPLETE GETTERS AND SETTERS - UPDATED
    public String getIdDetection() {
        return idDetection;
    }

    public void setIdDetection(String idDetection) {
        this.idDetection = idDetection;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getIdPeserta() {
        return idPeserta;
    }

    public void setIdPeserta(String idPeserta) {
        this.idPeserta = idPeserta;
    }

    public String getIdUjian() {
        return idUjian;
    }

    public void setIdUjian(String idUjian) {
        this.idUjian = idUjian;
    }

    public String getIdSchool() {
        return idSchool;
    }

    public void setIdSchool(String idSchool) {
        this.idSchool = idSchool;
    }

    public String getTypeViolation() {
        return typeViolation;
    }

    public void setTypeViolation(String typeViolation) {
        this.typeViolation = typeViolation;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Integer getViolationCount() {
        return violationCount;
    }

    public void setViolationCount(Integer violationCount) {
        this.violationCount = violationCount;
    }

    public Instant getDetectedAt() {
        return detectedAt;
    }

    public void setDetectedAt(Instant detectedAt) {
        this.detectedAt = detectedAt;
    }

    public Instant getFirstDetectedAt() {
        return firstDetectedAt;
    }

    public void setFirstDetectedAt(Instant firstDetectedAt) {
        this.firstDetectedAt = firstDetectedAt;
    }

    public Map<String, Object> getEvidence() {
        return evidence;
    }

    public void setEvidence(Map<String, Object> evidence) {
        this.evidence = evidence;
    }

    public String getBrowserInfo() {
        return browserInfo;
    }

    public void setBrowserInfo(String browserInfo) {
        this.browserInfo = browserInfo;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    // NEW GETTERS/SETTERS FOR FRONTEND DETECTION
    public Map<String, Object> getFrontendEvents() {
        return frontendEvents;
    }

    public void setFrontendEvents(Map<String, Object> frontendEvents) {
        this.frontendEvents = frontendEvents;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }

    public Integer getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(Integer screenWidth) {
        this.screenWidth = screenWidth;
    }

    public Integer getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(Integer screenHeight) {
        this.screenHeight = screenHeight;
    }

    public Boolean getFullscreenStatus() {
        return fullscreenStatus;
    }

    public void setFullscreenStatus(Boolean fullscreenStatus) {
        this.fullscreenStatus = fullscreenStatus;
    }

    public Long getTimeBetweenAnswers() {
        return timeBetweenAnswers;
    }

    public void setTimeBetweenAnswers(Long timeBetweenAnswers) {
        this.timeBetweenAnswers = timeBetweenAnswers;
    }

    public Map<String, Long> getAnswerTimestamps() {
        return answerTimestamps;
    }

    public void setAnswerTimestamps(Map<String, Long> answerTimestamps) {
        this.answerTimestamps = answerTimestamps;
    }

    public String getAnswerPattern() {
        return answerPattern;
    }

    public void setAnswerPattern(String answerPattern) {
        this.answerPattern = answerPattern;
    }

    // REMAINING GETTERS/SETTERS
    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public Instant getActionAt() {
        return actionAt;
    }

    public void setActionAt(Instant actionAt) {
        this.actionAt = actionAt;
    }

    public String getActionReason() {
        return actionReason;
    }

    public void setActionReason(String actionReason) {
        this.actionReason = actionReason;
    }

    public Boolean getResolved() {
        return resolved;
    }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }

    public String getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(Instant resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public String getResolutionNotes() {
        return resolutionNotes;
    }

    public void setResolutionNotes(String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
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

    public UjianSession getUjianSession() {
        return ujianSession;
    }

    public void setUjianSession(UjianSession ujianSession) {
        this.ujianSession = ujianSession;
    }

    public User getPeserta() {
        return peserta;
    }

    public void setPeserta(User peserta) {
        this.peserta = peserta;
    }

    public Ujian getUjian() {
        return ujian;
    }

    public void setUjian(Ujian ujian) {
        this.ujian = ujian;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    // Method untuk set violation dengan default severity
    public void setViolationWithDefaults(String violationType) {
        this.typeViolation = violationType;
        this.severity = ViolationType.getDefaultSeverity(violationType);

        // Auto-set action berdasarkan severity
        if (SeverityLevel.CRITICAL.equals(this.severity)) {
            this.actionTaken = ActionType.AUTO_SUBMIT;
        } else if (SeverityLevel.HIGH.equals(this.severity)) {
            this.actionTaken = ActionType.FLAGGED_FOR_REVIEW;
        }
    }

    // TAMBAHAN: Method untuk create detection dari UjianSession
    public static CheatDetection createFromSession(UjianSession session, String typeViolation,
            String severity, Map<String, Object> evidence) {
        CheatDetection detection = new CheatDetection(
                session.getSessionId(),
                session.getIdPeserta(),
                session.getIdUjian(),
                session.getIdSchool(),
                typeViolation,
                severity);

        detection.setIdDetection(java.util.UUID.randomUUID().toString());

        // Set relational data
        detection.setUjianSession(session);
        if (session.getPeserta() != null) {
            detection.setPeserta(session.getPeserta());
        }
        if (session.getUjian() != null) {
            detection.setUjian(session.getUjian());
        }
        if (session.getSchool() != null) {
            detection.setSchool(session.getSchool());
        }

        // Set evidence
        if (evidence != null) {
            detection.setEvidence(evidence);
        }

        return detection;
    }

    /**
     * TAMBAHAN: Method untuk update HasilUjian dengan violation ini
     */
    public void updateHasilUjian(HasilUjian hasilUjian) {
        if (hasilUjian != null && this.sessionId.equals(hasilUjian.getSessionId())) {
            hasilUjian.addCheatDetection(this);
        }
    }

    /**
     * TAMBAHAN: Method untuk check apakah perlu auto-submit
     */
    public boolean shouldAutoSubmit() {
        return "CRITICAL".equals(this.severity) ||
                (this.violationCount != null && this.violationCount >= 15);
    }

    /**
     * TAMBAHAN: Method untuk sync dengan session metadata
     */
    public void syncWithSession(UjianSession session) {
        if (session != null && session.getSessionId().equals(this.sessionId)) {
            // Update browser info from session metadata
            Map<String, Object> sessionMeta = session.getSessionMetadata();
            if (sessionMeta != null) {
                if (sessionMeta.containsKey("userAgent")) {
                    this.setUserAgent((String) sessionMeta.get("userAgent"));
                }
                if (sessionMeta.containsKey("browserInfo")) {
                    this.setBrowserInfo((String) sessionMeta.get("browserInfo"));
                }
            }

            // Add session context to evidence
            Map<String, Object> evidence = this.getEvidence();
            if (evidence == null) {
                evidence = new HashMap<>();
                this.setEvidence(evidence);
            }

            evidence.put("sessionStatus", session.getStatus());
            evidence.put("sessionStartTime", session.getStartTime());
            evidence.put("currentSoalIndex", session.getCurrentSoalIndex());
            evidence.put("answeredQuestions", session.getAnsweredQuestions());
            evidence.put("timeRemaining", session.getTimeRemaining());
        }
    }
}