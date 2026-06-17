package com.doyatama.university.payload;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HasilUjianRequest {

    // Base request class
    public static class BaseHasilUjianRequest {
        private String idUjian;
        private String idPeserta;
        private String sessionId;
        private String timestamp;

        public BaseHasilUjianRequest() {
            this.timestamp = Instant.now().toString();
        }

        // Getters and setters
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

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }

    // Request untuk get hasil by peserta dengan filtering options
    public static class GetHasilByPesertaRequest extends BaseHasilUjianRequest {
        private Boolean includeAnswers;
        private Boolean includeTimeAnalytics;
        private Boolean includeBehavioralAnalysis;
        private Boolean includeSecurityData;
        private Boolean includeAnalysis;
        private Integer attemptNumber;
        private Integer limit;

        public GetHasilByPesertaRequest() {
            super();
            this.includeAnswers = false;
            this.includeTimeAnalytics = false;
            this.includeBehavioralAnalysis = false;
            this.includeSecurityData = false;
            this.includeAnalysis = true;
            this.limit = 10;
        }

        // Getters and setters
        public Boolean getIncludeAnswers() {
            return includeAnswers;
        }

        public void setIncludeAnswers(Boolean includeAnswers) {
            this.includeAnswers = includeAnswers;
        }

        public Boolean getIncludeTimeAnalytics() {
            return includeTimeAnalytics;
        }

        public void setIncludeTimeAnalytics(Boolean includeTimeAnalytics) {
            this.includeTimeAnalytics = includeTimeAnalytics;
        }

        public Boolean getIncludeBehavioralAnalysis() {
            return includeBehavioralAnalysis;
        }

        public void setIncludeBehavioralAnalysis(Boolean includeBehavioralAnalysis) {
            this.includeBehavioralAnalysis = includeBehavioralAnalysis;
        }

        public Boolean getIncludeSecurityData() {
            return includeSecurityData;
        }

        public void setIncludeSecurityData(Boolean includeSecurityData) {
            this.includeSecurityData = includeSecurityData;
        }

        public Boolean getIncludeAnalysis() {
            return includeAnalysis;
        }

        public void setIncludeAnalysis(Boolean includeAnalysis) {
            this.includeAnalysis = includeAnalysis;
        }

        public Integer getAttemptNumber() {
            return attemptNumber;
        }

        public void setAttemptNumber(Integer attemptNumber) {
            this.attemptNumber = attemptNumber;
        }

        public Integer getLimit() {
            return limit;
        }

        public void setLimit(Integer limit) {
            this.limit = limit;
        }
    }

    // Request untuk update analytics
    public static class UpdateAnalyticsRequest extends BaseHasilUjianRequest {
        private Map<String, Integer> timeSpentPerQuestion;
        private Map<String, Object> answerHistory;
        private Map<String, Integer> attemptCountPerQuestion;
        private String workingPattern;
        private Double consistencyScore;
        private Boolean hasSignsOfGuessing;
        private Boolean hasSignsOfAnxiety;
        private String confidenceLevel;
        private Map<String, Object> topicPerformance;
        private List<String> strengths;
        private List<String> weaknesses;
        private List<String> recommendedStudyAreas;

        public UpdateAnalyticsRequest() {
            super();
        }

        // Getters and setters untuk semua fields
        public Map<String, Integer> getTimeSpentPerQuestion() {
            return timeSpentPerQuestion;
        }

        public void setTimeSpentPerQuestion(Map<String, Integer> timeSpentPerQuestion) {
            this.timeSpentPerQuestion = timeSpentPerQuestion;
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

        public Map<String, Object> getTopicPerformance() {
            return topicPerformance;
        }

        public void setTopicPerformance(Map<String, Object> topicPerformance) {
            this.topicPerformance = topicPerformance;
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
    }

    // Request untuk update security status
    public static class UpdateSecurityRequest extends BaseHasilUjianRequest {
        private String securityStatus;
        private Map<String, Object> securityFlags;
        private String reason;
        private String updatedBy;

        public UpdateSecurityRequest() {
            super();
        }

        // Getters and setters
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

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
        }
    }

    // Request untuk submit appeal
    public static class SubmitAppealRequest extends BaseHasilUjianRequest {
        private String appealReason;
        private String appealDescription;
        private List<String> attachmentIds;
        private String submittedBy;

        public SubmitAppealRequest() {
            super();
        }

        // Getters and setters
        public String getAppealReason() {
            return appealReason;
        }

        public void setAppealReason(String appealReason) {
            this.appealReason = appealReason;
        }

        public String getAppealDescription() {
            return appealDescription;
        }

        public void setAppealDescription(String appealDescription) {
            this.appealDescription = appealDescription;
        }

        public List<String> getAttachmentIds() {
            return attachmentIds;
        }

        public void setAttachmentIds(List<String> attachmentIds) {
            this.attachmentIds = attachmentIds;
        }

        public String getSubmittedBy() {
            return submittedBy;
        }

        public void setSubmittedBy(String submittedBy) {
            this.submittedBy = submittedBy;
        }
    }

    // Request untuk export hasil ujian
    public static class ExportHasilRequest extends BaseHasilUjianRequest {
        private String format; // EXCEL, PDF, CSV
        private Boolean includeAnswers;
        private Boolean includeAnalysis;
        private Boolean includeStatistics;
        private String sortBy;
        private String filterStatus;
        private List<String> ujianIds;

        public ExportHasilRequest() {
            super();
            this.format = "EXCEL";
            this.includeAnswers = false;
            this.includeAnalysis = true;
            this.includeStatistics = true;
            this.sortBy = "SCORE_DESC";
            this.filterStatus = "ALL";
        }

        // Getters and setters
        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public Boolean getIncludeAnswers() {
            return includeAnswers;
        }

        public void setIncludeAnswers(Boolean includeAnswers) {
            this.includeAnswers = includeAnswers;
        }

        public Boolean getIncludeAnalysis() {
            return includeAnalysis;
        }

        public void setIncludeAnalysis(Boolean includeAnalysis) {
            this.includeAnalysis = includeAnalysis;
        }

        public Boolean getIncludeStatistics() {
            return includeStatistics;
        }

        public void setIncludeStatistics(Boolean includeStatistics) {
            this.includeStatistics = includeStatistics;
        }

        public String getSortBy() {
            return sortBy;
        }

        public void setSortBy(String sortBy) {
            this.sortBy = sortBy;
        }

        public String getFilterStatus() {
            return filterStatus;
        }

        public void setFilterStatus(String filterStatus) {
            this.filterStatus = filterStatus;
        }

        public List<String> getUjianIds() {
            return ujianIds;
        }

        public void setUjianIds(List<String> ujianIds) {
            this.ujianIds = ujianIds;
        }
    }

    // Request untuk generate advanced analytics
    public static class GenerateAnalyticsRequest extends BaseHasilUjianRequest {
        private String analysisScope; // INDIVIDUAL, CLASS, SCHOOL, UJIAN
        private Boolean includeComparative;
        private Boolean includePredictive;
        private Boolean includeRecommendations;
        private String analyticsLevel; // BASIC, DETAILED, COMPREHENSIVE

        public GenerateAnalyticsRequest() {
            super();
            this.analysisScope = "INDIVIDUAL";
            this.includeComparative = false;
            this.includePredictive = false;
            this.includeRecommendations = true;
            this.analyticsLevel = "DETAILED";
        }

        // Getters and setters
        public String getAnalysisScope() {
            return analysisScope;
        }

        public void setAnalysisScope(String analysisScope) {
            this.analysisScope = analysisScope;
        }

        public Boolean getIncludeComparative() {
            return includeComparative;
        }

        public void setIncludeComparative(Boolean includeComparative) {
            this.includeComparative = includeComparative;
        }

        public Boolean getIncludePredictive() {
            return includePredictive;
        }

        public void setIncludePredictive(Boolean includePredictive) {
            this.includePredictive = includePredictive;
        }

        public Boolean getIncludeRecommendations() {
            return includeRecommendations;
        }

        public void setIncludeRecommendations(Boolean includeRecommendations) {
            this.includeRecommendations = includeRecommendations;
        }

        public String getAnalyticsLevel() {
            return analyticsLevel;
        }

        public void setAnalyticsLevel(String analyticsLevel) {
            this.analyticsLevel = analyticsLevel;
        }
    }
}