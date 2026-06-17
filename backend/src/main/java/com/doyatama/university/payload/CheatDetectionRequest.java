package com.doyatama.university.payload;

import java.time.Instant;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheatDetectionRequest {
    private String sessionId;
    private String idPeserta;
    private String idUjian;
    private String idSchool;
    private String timestamp;

    // Default constructor
    public CheatDetectionRequest() {
        this.timestamp = Instant.now().toString();
    }

    // Constructor with common fields
    public CheatDetectionRequest(String sessionId, String idPeserta, String idUjian, String idSchool) {
        this();
        this.sessionId = sessionId;
        this.idPeserta = idPeserta;
        this.idUjian = idUjian;
        this.idSchool = idSchool;
    }

    // Getters and setters
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    // ==================== SUBCLASSES FOR DIFFERENT OPERATIONS ====================

    /**
     * Request untuk record violation dari frontend
     */
    public static class RecordViolationRequest extends CheatDetectionRequest {
        private String typeViolation; // TAB_SWITCH, WINDOW_BLUR, COPY_PASTE, etc.
        private String severity; // LOW, MEDIUM, HIGH, CRITICAL
        private Map<String, Object> evidence;
        private Map<String, Object> frontendEvents;

        // Browser & System Info
        private String browserInfo;
        private String userAgent;
        private String windowTitle;
        private Integer screenWidth;
        private Integer screenHeight;
        private Boolean fullscreenStatus;

        // Timing data
        private Long detectionTimestamp;
        private String questionId; // Soal yang sedang dikerjakan saat violation
        private Map<String, Long> answerTimestamps;

        public RecordViolationRequest() {
            super();
            this.evidence = new HashMap<>();
            this.frontendEvents = new HashMap<>();
            this.answerTimestamps = new HashMap<>();
            this.severity = "MEDIUM";
            this.fullscreenStatus = true;
            this.detectionTimestamp = System.currentTimeMillis();
        }

        // Getters and setters
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

        public Map<String, Object> getEvidence() {
            return evidence;
        }

        public void setEvidence(Map<String, Object> evidence) {
            this.evidence = evidence;
        }

        public Map<String, Object> getFrontendEvents() {
            return frontendEvents;
        }

        public void setFrontendEvents(Map<String, Object> frontendEvents) {
            this.frontendEvents = frontendEvents;
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

        public Long getDetectionTimestamp() {
            return detectionTimestamp;
        }

        public void setDetectionTimestamp(Long detectionTimestamp) {
            this.detectionTimestamp = detectionTimestamp;
        }

        public String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        public Map<String, Long> getAnswerTimestamps() {
            return answerTimestamps;
        }

        public void setAnswerTimestamps(Map<String, Long> answerTimestamps) {
            this.answerTimestamps = answerTimestamps;
        }
    }

    /**
     * Request untuk batch record multiple violations
     */
    public static class BatchRecordViolationsRequest extends CheatDetectionRequest {
        private List<ViolationEvent> violations;
        private Map<String, Object> sessionContext;
        private String batchId;

        public BatchRecordViolationsRequest() {
            super();
            this.violations = new ArrayList<>();
            this.sessionContext = new HashMap<>();
            this.batchId = "BATCH-" + System.currentTimeMillis();
        }

        // Inner class untuk violation event
        public static class ViolationEvent {
            private String typeViolation;
            private String severity;
            private Long timestamp;
            private String questionId;
            private Map<String, Object> eventData;

            public ViolationEvent() {
                this.eventData = new HashMap<>();
                this.timestamp = System.currentTimeMillis();
                this.severity = "MEDIUM";
            }

            // Getters and setters
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

            public Long getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(Long timestamp) {
                this.timestamp = timestamp;
            }

            public String getQuestionId() {
                return questionId;
            }

            public void setQuestionId(String questionId) {
                this.questionId = questionId;
            }

            public Map<String, Object> getEventData() {
                return eventData;
            }

            public void setEventData(Map<String, Object> eventData) {
                this.eventData = eventData;
            }
        }

        // Getters and setters
        public List<ViolationEvent> getViolations() {
            return violations;
        }

        public void setViolations(List<ViolationEvent> violations) {
            this.violations = violations;
        }

        public Map<String, Object> getSessionContext() {
            return sessionContext;
        }

        public void setSessionContext(Map<String, Object> sessionContext) {
            this.sessionContext = sessionContext;
        }

        public String getBatchId() {
            return batchId;
        }

        public void setBatchId(String batchId) {
            this.batchId = batchId;
        }
    }

    /**
     * Request untuk analyze answer patterns (backend analysis)
     */
    public static class AnalyzeAnswerPatternsRequest extends CheatDetectionRequest {
        private Map<String, Integer> timeSpentPerQuestion; // Main focus: timing
        private Map<String, List<String>> answerHistory; // Answer change history
        private Integer totalQuestions;
        private String analysisType; // TIMING, BEHAVIORAL, COMPREHENSIVE

        public AnalyzeAnswerPatternsRequest() {
            super();
            this.timeSpentPerQuestion = new HashMap<>();
            this.answerHistory = new HashMap<>();
            this.analysisType = "BEHAVIORAL";
        }

        // Getters and setters - SIMPLIFIED
        public Map<String, Integer> getTimeSpentPerQuestion() {
            return timeSpentPerQuestion;
        }

        public void setTimeSpentPerQuestion(Map<String, Integer> timeSpentPerQuestion) {
            this.timeSpentPerQuestion = timeSpentPerQuestion;
        }

        public Map<String, List<String>> getAnswerHistory() {
            return answerHistory;
        }

        public void setAnswerHistory(Map<String, List<String>> answerHistory) {
            this.answerHistory = answerHistory;
        }

        public Integer getTotalQuestions() {
            return totalQuestions;
        }

        public void setTotalQuestions(Integer totalQuestions) {
            this.totalQuestions = totalQuestions;
        }

        public String getAnalysisType() {
            return analysisType;
        }

        public void setAnalysisType(String analysisType) {
            this.analysisType = analysisType;
        }
    }

    /**
     * Request untuk take action on violation
     */
    public static class TakeActionRequest extends CheatDetectionRequest {
        private String idDetection;
        private String actionTaken; // WARNING, LOCK_EXAM, TERMINATE_SESSION, FLAGGED_FOR_REVIEW, AUTO_SUBMIT
        private String actionBy; // SYSTEM, PROCTOR, ADMIN
        private String actionReason;
        private Boolean notifyPeserta;
        private Map<String, Object> actionData;

        public TakeActionRequest() {
            super();
            this.actionBy = "SYSTEM";
            this.notifyPeserta = false;
            this.actionData = new HashMap<>();
        }

        // Getters and setters
        public String getIdDetection() {
            return idDetection;
        }

        public void setIdDetection(String idDetection) {
            this.idDetection = idDetection;
        }

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

        public String getActionReason() {
            return actionReason;
        }

        public void setActionReason(String actionReason) {
            this.actionReason = actionReason;
        }

        public Boolean getNotifyPeserta() {
            return notifyPeserta;
        }

        public void setNotifyPeserta(Boolean notifyPeserta) {
            this.notifyPeserta = notifyPeserta;
        }

        public Map<String, Object> getActionData() {
            return actionData;
        }

        public void setActionData(Map<String, Object> actionData) {
            this.actionData = actionData;
        }
    }

    /**
     * Request untuk resolve violation (admin/teacher)
     */
    public static class ResolveViolationRequest extends CheatDetectionRequest {
        private String idDetection;
        private String resolvedBy;
        private String resolutionNotes;
        private Boolean isValidViolation; // True if confirmed, false if false positive
        private String reviewerComment;
        private Map<String, Object> resolutionData;

        public ResolveViolationRequest() {
            super();
            this.isValidViolation = true;
            this.resolutionData = new HashMap<>();
        }

        // Getters and setters
        public String getIdDetection() {
            return idDetection;
        }

        public void setIdDetection(String idDetection) {
            this.idDetection = idDetection;
        }

        public String getResolvedBy() {
            return resolvedBy;
        }

        public void setResolvedBy(String resolvedBy) {
            this.resolvedBy = resolvedBy;
        }

        public String getResolutionNotes() {
            return resolutionNotes;
        }

        public void setResolutionNotes(String resolutionNotes) {
            this.resolutionNotes = resolutionNotes;
        }

        public Boolean getIsValidViolation() {
            return isValidViolation;
        }

        public void setIsValidViolation(Boolean isValidViolation) {
            this.isValidViolation = isValidViolation;
        }

        public String getReviewerComment() {
            return reviewerComment;
        }

        public void setReviewerComment(String reviewerComment) {
            this.reviewerComment = reviewerComment;
        }

        public Map<String, Object> getResolutionData() {
            return resolutionData;
        }

        public void setResolutionData(Map<String, Object> resolutionData) {
            this.resolutionData = resolutionData;
        }
    }

    /**
     * Request untuk get violations by criteria
     */
    public static class GetViolationsRequest extends CheatDetectionRequest {
        private String typeViolation;
        private String severity;
        private Boolean resolved;
        private String timeRange; // LAST_HOUR, LAST_DAY, LAST_WEEK, ALL
        private Integer limit;
        private String sortBy; // TIMESTAMP, SEVERITY, TYPE
        private String sortOrder; // ASC, DESC
        private Boolean includeEvidence;
        private Boolean includeActions;

        public GetViolationsRequest() {
            super();
            this.timeRange = "LAST_DAY";
            this.limit = 50;
            this.sortBy = "TIMESTAMP";
            this.sortOrder = "DESC";
            this.includeEvidence = false;
            this.includeActions = true;
        }

        // Getters and setters
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

        public Boolean getResolved() {
            return resolved;
        }

        public void setResolved(Boolean resolved) {
            this.resolved = resolved;
        }

        public String getTimeRange() {
            return timeRange;
        }

        public void setTimeRange(String timeRange) {
            this.timeRange = timeRange;
        }

        public Integer getLimit() {
            return limit;
        }

        public void setLimit(Integer limit) {
            this.limit = limit;
        }

        public String getSortBy() {
            return sortBy;
        }

        public void setSortBy(String sortBy) {
            this.sortBy = sortBy;
        }

        public String getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
        }

        public Boolean getIncludeEvidence() {
            return includeEvidence;
        }

        public void setIncludeEvidence(Boolean includeEvidence) {
            this.includeEvidence = includeEvidence;
        }

        public Boolean getIncludeActions() {
            return includeActions;
        }

        public void setIncludeActions(Boolean includeActions) {
            this.includeActions = includeActions;
        }
    }

    /**
     * Request untuk generate violation statistics
     */
    public static class GenerateStatisticsRequest extends CheatDetectionRequest {
        private String timeRange; // TODAY, WEEK, MONTH, YEAR, ALL
        private String groupBy; // TYPE, SEVERITY, PESERTA, UJIAN, HOUR, DAY
        private Boolean includeResolvedViolations;
        private Boolean includeTrends;
        private Boolean includeTopViolators;
        private String[] analysisTypes; // OVERVIEW, DETAILED, COMPARATIVE

        public GenerateStatisticsRequest() {
            super();
            this.timeRange = "WEEK";
            this.groupBy = "TYPE";
            this.includeResolvedViolations = true;
            this.includeTrends = true;
            this.includeTopViolators = false;
            this.analysisTypes = new String[] { "OVERVIEW" };
        }

        // Getters and setters
        public String getTimeRange() {
            return timeRange;
        }

        public void setTimeRange(String timeRange) {
            this.timeRange = timeRange;
        }

        public String getGroupBy() {
            return groupBy;
        }

        public void setGroupBy(String groupBy) {
            this.groupBy = groupBy;
        }

        public Boolean getIncludeResolvedViolations() {
            return includeResolvedViolations;
        }

        public void setIncludeResolvedViolations(Boolean includeResolvedViolations) {
            this.includeResolvedViolations = includeResolvedViolations;
        }

        public Boolean getIncludeTrends() {
            return includeTrends;
        }

        public void setIncludeTrends(Boolean includeTrends) {
            this.includeTrends = includeTrends;
        }

        public Boolean getIncludeTopViolators() {
            return includeTopViolators;
        }

        public void setIncludeTopViolators(Boolean includeTopViolators) {
            this.includeTopViolators = includeTopViolators;
        }

        public String[] getAnalysisTypes() {
            return analysisTypes;
        }

        public void setAnalysisTypes(String[] analysisTypes) {
            this.analysisTypes = analysisTypes;
        }
    }

    /**
     * Request untuk export violation reports
     */
    public static class ExportViolationsRequest extends CheatDetectionRequest {
        private String format; // PDF, EXCEL, CSV, JSON
        private String timeRange;
        private String[] violationTypes;
        private String[] severityLevels;
        private Boolean includeEvidence;
        private Boolean includeStatistics;
        private Boolean includeRecommendations;
        private String reportTemplate; // SUMMARY, DETAILED, ADMINISTRATIVE

        public ExportViolationsRequest() {
            super();
            this.format = "PDF";
            this.timeRange = "WEEK";
            this.includeEvidence = false;
            this.includeStatistics = true;
            this.includeRecommendations = true;
            this.reportTemplate = "SUMMARY";
        }

        // Getters and setters
        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getTimeRange() {
            return timeRange;
        }

        public void setTimeRange(String timeRange) {
            this.timeRange = timeRange;
        }

        public String[] getViolationTypes() {
            return violationTypes;
        }

        public void setViolationTypes(String[] violationTypes) {
            this.violationTypes = violationTypes;
        }

        public String[] getSeverityLevels() {
            return severityLevels;
        }

        public void setSeverityLevels(String[] severityLevels) {
            this.severityLevels = severityLevels;
        }

        public Boolean getIncludeEvidence() {
            return includeEvidence;
        }

        public void setIncludeEvidence(Boolean includeEvidence) {
            this.includeEvidence = includeEvidence;
        }

        public Boolean getIncludeStatistics() {
            return includeStatistics;
        }

        public void setIncludeStatistics(Boolean includeStatistics) {
            this.includeStatistics = includeStatistics;
        }

        public Boolean getIncludeRecommendations() {
            return includeRecommendations;
        }

        public void setIncludeRecommendations(Boolean includeRecommendations) {
            this.includeRecommendations = includeRecommendations;
        }

        public String getReportTemplate() {
            return reportTemplate;
        }

        public void setReportTemplate(String reportTemplate) {
            this.reportTemplate = reportTemplate;
        }
    }
}