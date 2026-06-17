package com.doyatama.university.payload;

import java.time.Instant;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UjianAnalysisRequest {
    private String idUjian;
    private String idSchool;
    private String analysisType;
    private String timestamp;

    // Default constructor
    public UjianAnalysisRequest() {
        this.timestamp = Instant.now().toString();
    }

    // Constructor with common fields
    public UjianAnalysisRequest(String idUjian, String idSchool, String analysisType) {
        this();
        this.idUjian = idUjian;
        this.idSchool = idSchool;
        this.analysisType = analysisType;
    }

    // Getters and setters
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

    public String getAnalysisType() {
        return analysisType;
    }

    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    // ==================== ENHANCED SUBCLASSES ====================

    /**
     * Request untuk generate comprehensive analysis
     */
    public static class GenerateAnalysisRequest extends UjianAnalysisRequest {
        private Boolean includeDescriptiveStats;
        private Boolean includeItemAnalysis;
        private Boolean includeDifficultyAnalysis;
        private Boolean includeTimeAnalysis;
        private Boolean includeCheatingAnalysis;
        private Boolean includeComparativeAnalysis;
        private Boolean includeLearningAnalytics;
        private Boolean includeRecommendations;
        private String detailLevel; // BASIC, DETAILED, COMPREHENSIVE
        private Map<String, Object> analysisConfiguration;
        private List<String> targetCategories; // KELAS, MAPEL, JURUSAN
        private Boolean generateReport;
        private String reportFormat; // PDF, EXCEL, JSON

        public GenerateAnalysisRequest() {
            super();
            this.includeDescriptiveStats = true;
            this.includeItemAnalysis = true;
            this.includeDifficultyAnalysis = true;
            this.includeTimeAnalysis = true;
            this.includeCheatingAnalysis = true;
            this.includeComparativeAnalysis = false;
            this.includeLearningAnalytics = true;
            this.includeRecommendations = true;
            this.detailLevel = "DETAILED";
            this.analysisConfiguration = new HashMap<>();
            this.targetCategories = new ArrayList<>();
            this.generateReport = false;
            this.reportFormat = "PDF";
        }

        // Getters and setters
        public Boolean getIncludeDescriptiveStats() {
            return includeDescriptiveStats;
        }

        public void setIncludeDescriptiveStats(Boolean includeDescriptiveStats) {
            this.includeDescriptiveStats = includeDescriptiveStats;
        }

        public Boolean getIncludeItemAnalysis() {
            return includeItemAnalysis;
        }

        public void setIncludeItemAnalysis(Boolean includeItemAnalysis) {
            this.includeItemAnalysis = includeItemAnalysis;
        }

        public Boolean getIncludeDifficultyAnalysis() {
            return includeDifficultyAnalysis;
        }

        public void setIncludeDifficultyAnalysis(Boolean includeDifficultyAnalysis) {
            this.includeDifficultyAnalysis = includeDifficultyAnalysis;
        }

        public Boolean getIncludeTimeAnalysis() {
            return includeTimeAnalysis;
        }

        public void setIncludeTimeAnalysis(Boolean includeTimeAnalysis) {
            this.includeTimeAnalysis = includeTimeAnalysis;
        }

        public Boolean getIncludeCheatingAnalysis() {
            return includeCheatingAnalysis;
        }

        public void setIncludeCheatingAnalysis(Boolean includeCheatingAnalysis) {
            this.includeCheatingAnalysis = includeCheatingAnalysis;
        }

        public Boolean getIncludeComparativeAnalysis() {
            return includeComparativeAnalysis;
        }

        public void setIncludeComparativeAnalysis(Boolean includeComparativeAnalysis) {
            this.includeComparativeAnalysis = includeComparativeAnalysis;
        }

        public Boolean getIncludeLearningAnalytics() {
            return includeLearningAnalytics;
        }

        public void setIncludeLearningAnalytics(Boolean includeLearningAnalytics) {
            this.includeLearningAnalytics = includeLearningAnalytics;
        }

        public Boolean getIncludeRecommendations() {
            return includeRecommendations;
        }

        public void setIncludeRecommendations(Boolean includeRecommendations) {
            this.includeRecommendations = includeRecommendations;
        }

        public String getDetailLevel() {
            return detailLevel;
        }

        public void setDetailLevel(String detailLevel) {
            this.detailLevel = detailLevel;
        }

        public Map<String, Object> getAnalysisConfiguration() {
            return analysisConfiguration;
        }

        public void setAnalysisConfiguration(Map<String, Object> analysisConfiguration) {
            this.analysisConfiguration = analysisConfiguration;
        }

        public List<String> getTargetCategories() {
            return targetCategories;
        }

        public void setTargetCategories(List<String> targetCategories) {
            this.targetCategories = targetCategories;
        }

        public Boolean getGenerateReport() {
            return generateReport;
        }

        public void setGenerateReport(Boolean generateReport) {
            this.generateReport = generateReport;
        }

        public String getReportFormat() {
            return reportFormat;
        }

        public void setReportFormat(String reportFormat) {
            this.reportFormat = reportFormat;
        }
    }

    /**
     * Request untuk get analysis dengan filter
     */
    public static class GetAnalysisRequest extends UjianAnalysisRequest {
        private String idAnalysis;
        private Boolean includeMetadata;
        private Boolean includeRelationalData;
        private Boolean includeItemDetails;
        private Boolean includeCategoryPerformance;
        private String[] specificCategories;
        private String dateRange; // LAST_WEEK, LAST_MONTH, LAST_YEAR, ALL
        private String sortBy; // DATE, SCORE, PARTICIPANTS
        private String sortOrder; // ASC, DESC
        private Integer limit;
        private Integer offset;

        public GetAnalysisRequest() {
            super();
            this.includeMetadata = true;
            this.includeRelationalData = true;
            this.includeItemDetails = false;
            this.includeCategoryPerformance = true;
            this.dateRange = "ALL";
            this.sortBy = "DATE";
            this.sortOrder = "DESC";
            this.limit = 50;
            this.offset = 0;
        }

        // Getters and setters
        public String getIdAnalysis() {
            return idAnalysis;
        }

        public void setIdAnalysis(String idAnalysis) {
            this.idAnalysis = idAnalysis;
        }

        public Boolean getIncludeMetadata() {
            return includeMetadata;
        }

        public void setIncludeMetadata(Boolean includeMetadata) {
            this.includeMetadata = includeMetadata;
        }

        public Boolean getIncludeRelationalData() {
            return includeRelationalData;
        }

        public void setIncludeRelationalData(Boolean includeRelationalData) {
            this.includeRelationalData = includeRelationalData;
        }

        public Boolean getIncludeItemDetails() {
            return includeItemDetails;
        }

        public void setIncludeItemDetails(Boolean includeItemDetails) {
            this.includeItemDetails = includeItemDetails;
        }

        public Boolean getIncludeCategoryPerformance() {
            return includeCategoryPerformance;
        }

        public void setIncludeCategoryPerformance(Boolean includeCategoryPerformance) {
            this.includeCategoryPerformance = includeCategoryPerformance;
        }

        public String[] getSpecificCategories() {
            return specificCategories;
        }

        public void setSpecificCategories(String[] specificCategories) {
            this.specificCategories = specificCategories;
        }

        public String getDateRange() {
            return dateRange;
        }

        public void setDateRange(String dateRange) {
            this.dateRange = dateRange;
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

        public Integer getLimit() {
            return limit;
        }

        public void setLimit(Integer limit) {
            this.limit = limit;
        }

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }
    }

    /**
     * Request untuk update analysis
     */
    public static class UpdateAnalysisRequest extends UjianAnalysisRequest {
        private String idAnalysis;
        private Map<String, Object> updatedMetadata;
        private List<String> additionalRecommendations;
        private List<String> additionalImprovements;
        private Map<String, Double> adjustedScores;
        private String updateReason;
        private String updatedBy;
        private Boolean recalculateStats;

        public UpdateAnalysisRequest() {
            super();
            this.updatedMetadata = new HashMap<>();
            this.additionalRecommendations = new ArrayList<>();
            this.additionalImprovements = new ArrayList<>();
            this.adjustedScores = new HashMap<>();
            this.recalculateStats = false;
        }

        // Getters and setters
        public String getIdAnalysis() {
            return idAnalysis;
        }

        public void setIdAnalysis(String idAnalysis) {
            this.idAnalysis = idAnalysis;
        }

        public Map<String, Object> getUpdatedMetadata() {
            return updatedMetadata;
        }

        public void setUpdatedMetadata(Map<String, Object> updatedMetadata) {
            this.updatedMetadata = updatedMetadata;
        }

        public List<String> getAdditionalRecommendations() {
            return additionalRecommendations;
        }

        public void setAdditionalRecommendations(List<String> additionalRecommendations) {
            this.additionalRecommendations = additionalRecommendations;
        }

        public List<String> getAdditionalImprovements() {
            return additionalImprovements;
        }

        public void setAdditionalImprovements(List<String> additionalImprovements) {
            this.additionalImprovements = additionalImprovements;
        }

        public Map<String, Double> getAdjustedScores() {
            return adjustedScores;
        }

        public void setAdjustedScores(Map<String, Double> adjustedScores) {
            this.adjustedScores = adjustedScores;
        }

        public String getUpdateReason() {
            return updateReason;
        }

        public void setUpdateReason(String updateReason) {
            this.updateReason = updateReason;
        }

        public String getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
        }

        public Boolean getRecalculateStats() {
            return recalculateStats;
        }

        public void setRecalculateStats(Boolean recalculateStats) {
            this.recalculateStats = recalculateStats;
        }
    }

    /**
     * Request untuk compare multiple analyses
     */
    public static class CompareAnalysisRequest extends UjianAnalysisRequest {
        private List<String> analysisIds;
        private List<String> ujianIds;
        private String comparisonType; // SAME_SCHOOL, CROSS_SCHOOL, TEMPORAL, CATEGORICAL
        private String comparisonScope; // OVERALL, ITEM_LEVEL, CATEGORY_LEVEL
        private Map<String, Object> comparisonCriteria;
        private Boolean includeStatisticalTests;
        private String[] metrics; // AVERAGE_SCORE, PASS_RATE, DIFFICULTY, DISCRIMINATION
        private Boolean generateReport;

        public CompareAnalysisRequest() {
            super();
            this.analysisIds = new ArrayList<>();
            this.ujianIds = new ArrayList<>();
            this.comparisonType = "SAME_SCHOOL";
            this.comparisonScope = "OVERALL";
            this.comparisonCriteria = new HashMap<>();
            this.includeStatisticalTests = false;
            this.generateReport = false;
        }

        // Getters and setters
        public List<String> getAnalysisIds() {
            return analysisIds;
        }

        public void setAnalysisIds(List<String> analysisIds) {
            this.analysisIds = analysisIds;
        }

        public List<String> getUjianIds() {
            return ujianIds;
        }

        public void setUjianIds(List<String> ujianIds) {
            this.ujianIds = ujianIds;
        }

        public String getComparisonType() {
            return comparisonType;
        }

        public void setComparisonType(String comparisonType) {
            this.comparisonType = comparisonType;
        }

        public String getComparisonScope() {
            return comparisonScope;
        }

        public void setComparisonScope(String comparisonScope) {
            this.comparisonScope = comparisonScope;
        }

        public Map<String, Object> getComparisonCriteria() {
            return comparisonCriteria;
        }

        public void setComparisonCriteria(Map<String, Object> comparisonCriteria) {
            this.comparisonCriteria = comparisonCriteria;
        }

        public Boolean getIncludeStatisticalTests() {
            return includeStatisticalTests;
        }

        public void setIncludeStatisticalTests(Boolean includeStatisticalTests) {
            this.includeStatisticalTests = includeStatisticalTests;
        }

        public String[] getMetrics() {
            return metrics;
        }

        public void setMetrics(String[] metrics) {
            this.metrics = metrics;
        }

        public Boolean getGenerateReport() {
            return generateReport;
        }

        public void setGenerateReport(Boolean generateReport) {
            this.generateReport = generateReport;
        }
    }

    /**
     * Request untuk export analysis
     */
    public static class ExportAnalysisRequest extends UjianAnalysisRequest {
        private String idAnalysis;
        private String format; // PDF, EXCEL, CSV, JSON
        private Boolean includeCharts;
        private Boolean includeRawData;
        private Boolean includeRecommendations;
        private Boolean includeStatistics;
        private Boolean includeItemDetails;
        private String[] sections; // OVERVIEW, DESCRIPTIVE, ITEM_ANALYSIS, RECOMMENDATIONS
        private String templateType; // STANDARD, DETAILED, EXECUTIVE_SUMMARY
        private Map<String, Object> customSettings;

        public ExportAnalysisRequest() {
            super();
            this.format = "PDF";
            this.includeCharts = true;
            this.includeRawData = false;
            this.includeRecommendations = true;
            this.includeStatistics = true;
            this.includeItemDetails = false;
            this.templateType = "STANDARD";
            this.customSettings = new HashMap<>();
        }

        // Getters and setters
        public String getIdAnalysis() {
            return idAnalysis;
        }

        public void setIdAnalysis(String idAnalysis) {
            this.idAnalysis = idAnalysis;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public Boolean getIncludeCharts() {
            return includeCharts;
        }

        public void setIncludeCharts(Boolean includeCharts) {
            this.includeCharts = includeCharts;
        }

        public Boolean getIncludeRawData() {
            return includeRawData;
        }

        public void setIncludeRawData(Boolean includeRawData) {
            this.includeRawData = includeRawData;
        }

        public Boolean getIncludeRecommendations() {
            return includeRecommendations;
        }

        public void setIncludeRecommendations(Boolean includeRecommendations) {
            this.includeRecommendations = includeRecommendations;
        }

        public Boolean getIncludeStatistics() {
            return includeStatistics;
        }

        public void setIncludeStatistics(Boolean includeStatistics) {
            this.includeStatistics = includeStatistics;
        }

        public Boolean getIncludeItemDetails() {
            return includeItemDetails;
        }

        public void setIncludeItemDetails(Boolean includeItemDetails) {
            this.includeItemDetails = includeItemDetails;
        }

        public String[] getSections() {
            return sections;
        }

        public void setSections(String[] sections) {
            this.sections = sections;
        }

        public String getTemplateType() {
            return templateType;
        }

        public void setTemplateType(String templateType) {
            this.templateType = templateType;
        }

        public Map<String, Object> getCustomSettings() {
            return customSettings;
        }

        public void setCustomSettings(Map<String, Object> customSettings) {
            this.customSettings = customSettings;
        }
    }

    /**
     * Request untuk generate item recommendation
     */
    public static class GenerateItemRecommendationRequest extends UjianAnalysisRequest {
        private String idAnalysis;
        private Double minDifficultyThreshold;
        private Double maxDifficultyThreshold;
        private Double minDiscriminationThreshold;
        private Double minReliabilityThreshold;
        private String recommendationType; // IMPROVE, REVIEW, DISCARD, REPLACE
        private Boolean includeAlternatives;
        private Map<String, Object> criteria;

        public GenerateItemRecommendationRequest() {
            super();
            this.minDifficultyThreshold = 0.3;
            this.maxDifficultyThreshold = 0.8;
            this.minDiscriminationThreshold = 0.3;
            this.minReliabilityThreshold = 0.7;
            this.recommendationType = "IMPROVE";
            this.includeAlternatives = false;
            this.criteria = new HashMap<>();
        }

        // Getters and setters
        public String getIdAnalysis() {
            return idAnalysis;
        }

        public void setIdAnalysis(String idAnalysis) {
            this.idAnalysis = idAnalysis;
        }

        public Double getMinDifficultyThreshold() {
            return minDifficultyThreshold;
        }

        public void setMinDifficultyThreshold(Double minDifficultyThreshold) {
            this.minDifficultyThreshold = minDifficultyThreshold;
        }

        public Double getMaxDifficultyThreshold() {
            return maxDifficultyThreshold;
        }

        public void setMaxDifficultyThreshold(Double maxDifficultyThreshold) {
            this.maxDifficultyThreshold = maxDifficultyThreshold;
        }

        public Double getMinDiscriminationThreshold() {
            return minDiscriminationThreshold;
        }

        public void setMinDiscriminationThreshold(Double minDiscriminationThreshold) {
            this.minDiscriminationThreshold = minDiscriminationThreshold;
        }

        public Double getMinReliabilityThreshold() {
            return minReliabilityThreshold;
        }

        public void setMinReliabilityThreshold(Double minReliabilityThreshold) {
            this.minReliabilityThreshold = minReliabilityThreshold;
        }

        public String getRecommendationType() {
            return recommendationType;
        }

        public void setRecommendationType(String recommendationType) {
            this.recommendationType = recommendationType;
        }

        public Boolean getIncludeAlternatives() {
            return includeAlternatives;
        }

        public void setIncludeAlternatives(Boolean includeAlternatives) {
            this.includeAlternatives = includeAlternatives;
        }

        public Map<String, Object> getCriteria() {
            return criteria;
        }

        public void setCriteria(Map<String, Object> criteria) {
            this.criteria = criteria;
        }
    }

    /**
     * Request untuk generate trend analysis
     */
    public static class GenerateTrendAnalysisRequest extends UjianAnalysisRequest {
        private List<String> ujianIds;
        private String trendScope; // TEMPORAL, CROSS_CLASS, CROSS_SUBJECT
        private String timeFrame; // WEEKLY, MONTHLY, QUARTERLY, YEARLY
        private String[] metrics; // AVERAGE_SCORE, PASS_RATE, PARTICIPATION, CHEATING
        private Boolean includePredictions;
        private Boolean includeSeasonality;
        private Map<String, Object> trendConfiguration;

        public GenerateTrendAnalysisRequest() {
            super();
            this.ujianIds = new ArrayList<>();
            this.trendScope = "TEMPORAL";
            this.timeFrame = "MONTHLY";
            this.includePredictions = false;
            this.includeSeasonality = false;
            this.trendConfiguration = new HashMap<>();
        }

        // Getters and setters
        public List<String> getUjianIds() {
            return ujianIds;
        }

        public void setUjianIds(List<String> ujianIds) {
            this.ujianIds = ujianIds;
        }

        public String getTrendScope() {
            return trendScope;
        }

        public void setTrendScope(String trendScope) {
            this.trendScope = trendScope;
        }

        public String getTimeFrame() {
            return timeFrame;
        }

        public void setTimeFrame(String timeFrame) {
            this.timeFrame = timeFrame;
        }

        public String[] getMetrics() {
            return metrics;
        }

        public void setMetrics(String[] metrics) {
            this.metrics = metrics;
        }

        public Boolean getIncludePredictions() {
            return includePredictions;
        }

        public void setIncludePredictions(Boolean includePredictions) {
            this.includePredictions = includePredictions;
        }

        public Boolean getIncludeSeasonality() {
            return includeSeasonality;
        }

        public void setIncludeSeasonality(Boolean includeSeasonality) {
            this.includeSeasonality = includeSeasonality;
        }

        public Map<String, Object> getTrendConfiguration() {
            return trendConfiguration;
        }

        public void setTrendConfiguration(Map<String, Object> trendConfiguration) {
            this.trendConfiguration = trendConfiguration;
        }
    }

    /**
     * Request untuk bulk analysis operation
     */
    public static class BulkAnalysisRequest extends UjianAnalysisRequest {
        private List<String> ujianIds;
        private String operation; // GENERATE, UPDATE, DELETE, EXPORT
        private Map<String, Object> operationParameters;
        private Boolean processInBackground;
        private String notificationEmail;
        private String batchId;

        public BulkAnalysisRequest() {
            super();
            this.ujianIds = new ArrayList<>();
            this.operation = "GENERATE";
            this.operationParameters = new HashMap<>();
            this.processInBackground = true;
        }

        // Getters and setters
        public List<String> getUjianIds() {
            return ujianIds;
        }

        public void setUjianIds(List<String> ujianIds) {
            this.ujianIds = ujianIds;
        }

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public Map<String, Object> getOperationParameters() {
            return operationParameters;
        }

        public void setOperationParameters(Map<String, Object> operationParameters) {
            this.operationParameters = operationParameters;
        }

        public Boolean getProcessInBackground() {
            return processInBackground;
        }

        public void setProcessInBackground(Boolean processInBackground) {
            this.processInBackground = processInBackground;
        }

        public String getNotificationEmail() {
            return notificationEmail;
        }

        public void setNotificationEmail(String notificationEmail) {
            this.notificationEmail = notificationEmail;
        }

        public String getBatchId() {
            return batchId;
        }

        public void setBatchId(String batchId) {
            this.batchId = batchId;
        }
    }

    /**
     * Request untuk dashboard summary
     */
    public static class GetDashboardSummaryRequest extends UjianAnalysisRequest {
        private String timeRange; // LAST_WEEK, LAST_MONTH, LAST_QUARTER, LAST_YEAR
        private String[] includeMetrics; // OVERALL_STATS, TRENDING, ALERTS, RECOMMENDATIONS
        private Boolean includeCharts;
        private String aggregationLevel; // SCHOOL, CLASS, SUBJECT
        private Map<String, Object> filters;

        public GetDashboardSummaryRequest() {
            super();
            this.timeRange = "LAST_MONTH";
            this.includeCharts = true;
            this.aggregationLevel = "SCHOOL";
            this.filters = new HashMap<>();
        }

        // Getters and setters
        public String getTimeRange() {
            return timeRange;
        }

        public void setTimeRange(String timeRange) {
            this.timeRange = timeRange;
        }

        public String[] getIncludeMetrics() {
            return includeMetrics;
        }

        public void setIncludeMetrics(String[] includeMetrics) {
            this.includeMetrics = includeMetrics;
        }

        public Boolean getIncludeCharts() {
            return includeCharts;
        }

        public void setIncludeCharts(Boolean includeCharts) {
            this.includeCharts = includeCharts;
        }

        public String getAggregationLevel() {
            return aggregationLevel;
        }

        public void setAggregationLevel(String aggregationLevel) {
            this.aggregationLevel = aggregationLevel;
        }

        public Map<String, Object> getFilters() {
            return filters;
        }

        public void setFilters(Map<String, Object> filters) {
            this.filters = filters;
        }
    }
}