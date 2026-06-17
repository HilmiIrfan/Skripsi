package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.UjianAnalysis;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository

public class UjianAnalysisRepository {

    Configuration conf = HBaseConfiguration.create();
    String tableName = "ujian_analysis";

    @Autowired
    private ObjectMapper objectMapper; // Use the configured ObjectMapper with JSR310 support

    public List<UjianAnalysis> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableAnalysis = TableName.valueOf(tableName);
        Map<String, String> columnMapping = getStandardColumnMapping();

        // Define indexed fields
        Map<String, String> indexedFields = getIndexedFields();

        return client.showListTableIndex(tableAnalysis.toString(), columnMapping, UjianAnalysis.class, indexedFields,
                size);
    }

    public UjianAnalysis save(UjianAnalysis analysis) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        String rowKey = analysis.getIdAnalysis();
        TableName tableAnalysis = TableName.valueOf(tableName);

        // Save main info
        saveMainInfo(client, tableAnalysis, rowKey, analysis);

        // Save descriptive statistics
        saveDescriptiveStatistics(client, tableAnalysis, rowKey, analysis);

        // Save analysis data
        saveAnalysisData(client, tableAnalysis, rowKey, analysis);

        // Save relationships
        saveRelationships(client, tableAnalysis, rowKey, analysis);

        // Save metadata
        saveMetadata(client, tableAnalysis, rowKey, analysis);

        client.insertRecord(tableAnalysis, rowKey, "detail", "created_by", "System");
        return analysis;
    }

    private void saveMainInfo(HBaseCustomClient client, TableName table, String rowKey, UjianAnalysis analysis) {
        client.insertRecord(table, rowKey, "main", "idAnalysis", analysis.getIdAnalysis());
        client.insertRecord(table, rowKey, "main", "idUjian", analysis.getIdUjian());
        client.insertRecord(table, rowKey, "main", "idSchool", analysis.getIdSchool());
        client.insertRecord(table, rowKey, "main", "analysisType", analysis.getAnalysisType());

        if (analysis.getGeneratedBy() != null) {
            client.insertRecord(table, rowKey, "main", "generatedBy", analysis.getGeneratedBy());
        }

        if (analysis.getGeneratedAt() != null) {
            client.insertRecord(table, rowKey, "main", "generatedAt", analysis.getGeneratedAt().toString());
        }

        if (analysis.getUpdatedAt() != null) {
            client.insertRecord(table, rowKey, "main", "updatedAt", analysis.getUpdatedAt().toString());
        }

        if (analysis.getAnalysisVersion() != null) {
            client.insertRecord(table, rowKey, "main", "analysisVersion", analysis.getAnalysisVersion());
        }
    }

    private void saveDescriptiveStatistics(HBaseCustomClient client, TableName table, String rowKey,
            UjianAnalysis analysis) {
        String familyName = "descriptive";

        // Basic counts
        if (analysis.getTotalParticipants() != null) {
            client.insertRecord(table, rowKey, familyName, "totalParticipants",
                    analysis.getTotalParticipants().toString());
        }

        if (analysis.getCompletedParticipants() != null) {
            client.insertRecord(table, rowKey, familyName, "completedParticipants",
                    analysis.getCompletedParticipants().toString());
        }

        if (analysis.getIncompleteParticipants() != null) {
            client.insertRecord(table, rowKey, familyName, "incompleteParticipants",
                    analysis.getIncompleteParticipants().toString());
        }

        // Score statistics
        if (analysis.getAverageScore() != null) {
            client.insertRecord(table, rowKey, familyName, "averageScore", analysis.getAverageScore().toString());
        }

        if (analysis.getMedianScore() != null) {
            client.insertRecord(table, rowKey, familyName, "medianScore", analysis.getMedianScore().toString());
        }

        if (analysis.getHighestScore() != null) {
            client.insertRecord(table, rowKey, familyName, "highestScore", analysis.getHighestScore().toString());
        }

        if (analysis.getLowestScore() != null) {
            client.insertRecord(table, rowKey, familyName, "lowestScore", analysis.getLowestScore().toString());
        }

        if (analysis.getStandardDeviation() != null) {
            client.insertRecord(table, rowKey, familyName, "standardDeviation",
                    analysis.getStandardDeviation().toString());
        }

        if (analysis.getVariance() != null) {
            client.insertRecord(table, rowKey, familyName, "variance", analysis.getVariance().toString());
        }

        // Pass/Fail analysis
        if (analysis.getPassedCount() != null) {
            client.insertRecord(table, rowKey, familyName, "passedCount", analysis.getPassedCount().toString());
        }

        if (analysis.getFailedCount() != null) {
            client.insertRecord(table, rowKey, familyName, "failedCount", analysis.getFailedCount().toString());
        }

        if (analysis.getPassRate() != null) {
            client.insertRecord(table, rowKey, familyName, "passRate", analysis.getPassRate().toString());
        }

        if (analysis.getFailRate() != null) {
            client.insertRecord(table, rowKey, familyName, "failRate", analysis.getFailRate().toString());
        }

        // Grade distribution
        try {
            if (analysis.getGradeDistribution() != null && !analysis.getGradeDistribution().isEmpty()) {
                String gradeDistributionJson = objectMapper.writeValueAsString(analysis.getGradeDistribution());
                client.insertRecord(table, rowKey, familyName, "gradeDistribution", gradeDistributionJson);
            }

            if (analysis.getGradePercentages() != null && !analysis.getGradePercentages().isEmpty()) {
                String gradePercentagesJson = objectMapper.writeValueAsString(analysis.getGradePercentages());
                client.insertRecord(table, rowKey, familyName, "gradePercentages", gradePercentagesJson);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize grade distribution", e);
        }
    }

    private void saveAnalysisData(HBaseCustomClient client, TableName table, String rowKey, UjianAnalysis analysis) {
        try {
            // Time analysis - use 'analysis' family instead of 'time_analysis'
            String timeFamily = "analysis";
            if (analysis.getAverageCompletionTime() != null) {
                client.insertRecord(table, rowKey, timeFamily, "averageCompletionTime",
                        analysis.getAverageCompletionTime().toString());
            }

            if (analysis.getShortestCompletionTime() != null) {
                client.insertRecord(table, rowKey, timeFamily, "shortestCompletionTime",
                        analysis.getShortestCompletionTime().toString());
            }

            if (analysis.getLongestCompletionTime() != null) {
                client.insertRecord(table, rowKey, timeFamily, "longestCompletionTime",
                        analysis.getLongestCompletionTime().toString());
            }

            if (analysis.getTimePerQuestion() != null && !analysis.getTimePerQuestion().isEmpty()) {
                String timePerQuestionJson = objectMapper.writeValueAsString(analysis.getTimePerQuestion());
                client.insertRecord(table, rowKey, timeFamily, "timePerQuestion", timePerQuestionJson);
            }

            // Item analysis - use 'analysis' family
            String itemFamily = "analysis";
            if (analysis.getItemAnalysis() != null && !analysis.getItemAnalysis().isEmpty()) {
                String itemAnalysisJson = objectMapper.writeValueAsString(analysis.getItemAnalysis());
                client.insertRecord(table, rowKey, itemFamily, "itemAnalysis", itemAnalysisJson);
            }

            // Difficulty analysis - use 'analysis' family
            String difficultyFamily = "analysis";
            if (analysis.getQuestionDifficulty() != null && !analysis.getQuestionDifficulty().isEmpty()) {
                String questionDifficultyJson = objectMapper.writeValueAsString(analysis.getQuestionDifficulty());
                client.insertRecord(table, rowKey, difficultyFamily, "questionDifficulty", questionDifficultyJson);
            }

            if (analysis.getQuestionDiscrimination() != null && !analysis.getQuestionDiscrimination().isEmpty()) {
                String questionDiscriminationJson = objectMapper
                        .writeValueAsString(analysis.getQuestionDiscrimination());
                client.insertRecord(table, rowKey, difficultyFamily, "questionDiscrimination",
                        questionDiscriminationJson);
            }

            if (analysis.getEasiestQuestions() != null && !analysis.getEasiestQuestions().isEmpty()) {
                String easiestQuestionsJson = objectMapper.writeValueAsString(analysis.getEasiestQuestions());
                client.insertRecord(table, rowKey, difficultyFamily, "easiestQuestions", easiestQuestionsJson);
            }

            if (analysis.getHardestQuestions() != null && !analysis.getHardestQuestions().isEmpty()) {
                String hardestQuestionsJson = objectMapper.writeValueAsString(analysis.getHardestQuestions());
                client.insertRecord(table, rowKey, difficultyFamily, "hardestQuestions", hardestQuestionsJson);
            }

            if (analysis.getPoorDiscriminatingQuestions() != null
                    && !analysis.getPoorDiscriminatingQuestions().isEmpty()) {
                String poorDiscriminatingJson = objectMapper
                        .writeValueAsString(analysis.getPoorDiscriminatingQuestions());
                client.insertRecord(table, rowKey, difficultyFamily, "poorDiscriminatingQuestions",
                        poorDiscriminatingJson);
            } // Performance by categories - use 'analysis' family
            String performanceFamily = "analysis";
            if (analysis.getPerformanceByKelas() != null && !analysis.getPerformanceByKelas().isEmpty()) {
                String performanceByKelasJson = objectMapper.writeValueAsString(analysis.getPerformanceByKelas());
                client.insertRecord(table, rowKey, performanceFamily, "performanceByKelas", performanceByKelasJson);
            }

            if (analysis.getPerformanceByMapel() != null && !analysis.getPerformanceByMapel().isEmpty()) {
                String performanceByMapelJson = objectMapper.writeValueAsString(analysis.getPerformanceByMapel());
                client.insertRecord(table, rowKey, performanceFamily, "performanceByMapel", performanceByMapelJson);
            }

            if (analysis.getPerformanceByJurusan() != null && !analysis.getPerformanceByJurusan().isEmpty()) {
                String performanceByJurusanJson = objectMapper.writeValueAsString(analysis.getPerformanceByJurusan());
                client.insertRecord(table, rowKey, performanceFamily, "performanceByJurusan", performanceByJurusanJson);
            } // Cheating analysis - use 'cheating' family
            String cheatingFamily = "cheating";
            if (analysis.getSuspiciousSubmissions() != null) {
                client.insertRecord(table, rowKey, cheatingFamily, "suspiciousSubmissions",
                        analysis.getSuspiciousSubmissions().toString());
            }

            if (analysis.getFlaggedParticipants() != null) {
                client.insertRecord(table, rowKey, cheatingFamily, "flaggedParticipants",
                        analysis.getFlaggedParticipants().toString());
            }

            if (analysis.getIntegrityScore() != null) {
                client.insertRecord(table, rowKey, cheatingFamily, "integrityScore",
                        analysis.getIntegrityScore().toString());
            } // Recommendations - use 'recommendation' family
            String recommendationFamily = "recommendation";
            if (analysis.getRecommendations() != null && !analysis.getRecommendations().isEmpty()) {
                String recommendationsJson = objectMapper.writeValueAsString(analysis.getRecommendations());
                client.insertRecord(table, rowKey, recommendationFamily, "recommendations", recommendationsJson);
            }

            if (analysis.getImprovementSuggestions() != null && !analysis.getImprovementSuggestions().isEmpty()) {
                String improvementSuggestionsJson = objectMapper
                        .writeValueAsString(analysis.getImprovementSuggestions());
                client.insertRecord(table, rowKey, recommendationFamily, "improvementSuggestions",
                        improvementSuggestionsJson);
            }

            if (analysis.getQuestionRecommendations() != null && !analysis.getQuestionRecommendations().isEmpty()) {
                String questionRecommendationsJson = objectMapper
                        .writeValueAsString(analysis.getQuestionRecommendations());
                client.insertRecord(table, rowKey, recommendationFamily, "questionRecommendations",
                        questionRecommendationsJson);
            }

            if (analysis.getCurriculumSuggestions() != null && !analysis.getCurriculumSuggestions().isEmpty()) {
                String curriculumSuggestionsJson = objectMapper.writeValueAsString(analysis.getCurriculumSuggestions());
                client.insertRecord(table, rowKey, recommendationFamily, "curriculumSuggestions",
                        curriculumSuggestionsJson);
            } // Advanced analysis - use 'analysis' family
            String advancedFamily = "analysis";
            if (analysis.getTopicPerformanceAnalysis() != null && !analysis.getTopicPerformanceAnalysis().isEmpty()) {
                String topicPerformanceJson = objectMapper.writeValueAsString(analysis.getTopicPerformanceAnalysis());
                client.insertRecord(table, rowKey, advancedFamily, "topicPerformanceAnalysis", topicPerformanceJson);
            }

            if (analysis.getAnswerPatternDistribution() != null && !analysis.getAnswerPatternDistribution().isEmpty()) {
                String answerPatternJson = objectMapper.writeValueAsString(analysis.getAnswerPatternDistribution());
                client.insertRecord(table, rowKey, advancedFamily, "answerPatternDistribution", answerPatternJson);
            }

            if (analysis.getReliabilityCoefficient() != null) {
                client.insertRecord(table, rowKey, advancedFamily, "reliabilityCoefficient",
                        analysis.getReliabilityCoefficient().toString());
            }

            if (analysis.getValidityIndex() != null) {
                client.insertRecord(table, rowKey, advancedFamily, "validityIndex",
                        analysis.getValidityIndex().toString());
            }

            // Comparative analysis - use 'analysis' family
            String comparativeFamily = "analysis";
            if (analysis.getSchoolComparison() != null && !analysis.getSchoolComparison().isEmpty()) {
                String schoolComparisonJson = objectMapper.writeValueAsString(analysis.getSchoolComparison());
                client.insertRecord(table, rowKey, comparativeFamily, "schoolComparison", schoolComparisonJson);
            }

            if (analysis.getNationalAverage() != null && !analysis.getNationalAverage().isEmpty()) {
                String nationalAverageJson = objectMapper.writeValueAsString(analysis.getNationalAverage());
                client.insertRecord(table, rowKey, comparativeFamily, "nationalAverage", nationalAverageJson);
            }

            if (analysis.getPercentileRank() != null) {
                client.insertRecord(table, rowKey, comparativeFamily, "percentileRank",
                        analysis.getPercentileRank().toString());
            }

            // Learning analytics - use 'analysis' family
            String learningFamily = "analysis";
            if (analysis.getLearningGaps() != null && !analysis.getLearningGaps().isEmpty()) {
                String learningGapsJson = objectMapper.writeValueAsString(analysis.getLearningGaps());
                client.insertRecord(table, rowKey, learningFamily, "learningGaps", learningGapsJson);
            }

            if (analysis.getStudyRecommendations() != null && !analysis.getStudyRecommendations().isEmpty()) {
                String studyRecommendationsJson = objectMapper.writeValueAsString(analysis.getStudyRecommendations());
                client.insertRecord(table, rowKey, learningFamily, "studyRecommendations", studyRecommendationsJson);
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize analysis data", e);
        }
    }

    private void saveRelationships(HBaseCustomClient client, TableName table, String rowKey, UjianAnalysis analysis) {
        // Save Ujian relationship (if exists)
        if (analysis.getUjian() != null) {
            if (analysis.getUjian().getIdUjian() != null) {
                client.insertRecord(table, rowKey, "ujian", "idUjian", analysis.getUjian().getIdUjian());
            }
            if (analysis.getUjian().getNamaUjian() != null) {
                client.insertRecord(table, rowKey, "ujian", "namaUjian", analysis.getUjian().getNamaUjian());
            }
            if (analysis.getUjian().getStatusUjian() != null) {
                client.insertRecord(table, rowKey, "ujian", "statusUjian", analysis.getUjian().getStatusUjian());
            }
        }

        // Save School relationship - use 'school' family
        if (analysis.getSchool() != null) {
            if (analysis.getSchool().getIdSchool() != null) {
                client.insertRecord(table, rowKey, "school", "idSchool", analysis.getSchool().getIdSchool());
            }
            if (analysis.getSchool().getNameSchool() != null) {
                client.insertRecord(table, rowKey, "school", "nameSchool", analysis.getSchool().getNameSchool());
            }
        } // Save Generated By User relationship - use 'detail' family
        if (analysis.getGeneratedByUser() != null) {
            if (analysis.getGeneratedByUser().getId() != null) {
                client.insertRecord(table, rowKey, "detail", "generatedByUser_id",
                        analysis.getGeneratedByUser().getId());
            }
            if (analysis.getGeneratedByUser().getName() != null) {
                client.insertRecord(table, rowKey, "detail", "generatedByUser_name",
                        analysis.getGeneratedByUser().getName());
            }
            if (analysis.getGeneratedByUser().getUsername() != null) {
                client.insertRecord(table, rowKey, "detail", "generatedByUser_username",
                        analysis.getGeneratedByUser().getUsername());
            }
        } // Save violationIds - use 'cheating' family
        if (analysis.getViolationIds() != null && !analysis.getViolationIds().isEmpty()) {
            try {
                String violationIdsJson = objectMapper.writeValueAsString(analysis.getViolationIds());
                client.insertRecord(table, rowKey, "cheating", "violationIds", violationIdsJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize violationIds", e);
            }
        }
        // Save cheatDetections - use 'cheating' family
        if (analysis.getCheatDetections() != null && !analysis.getCheatDetections().isEmpty()) {
            try {
                String cheatDetectionsJson = objectMapper.writeValueAsString(analysis.getCheatDetections());
                client.insertRecord(table, rowKey, "cheating", "cheatDetections", cheatDetectionsJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize cheatDetections", e);
            }
        }
    }

    private void saveMetadata(HBaseCustomClient client, TableName table, String rowKey, UjianAnalysis analysis) {
        try {
            // Analysis metadata - use 'metadata' family
            if (analysis.getAnalysisMetadata() != null && !analysis.getAnalysisMetadata().isEmpty()) {
                String analysisMetadataJson = objectMapper.writeValueAsString(analysis.getAnalysisMetadata());
                client.insertRecord(table, rowKey, "metadata", "analysisMetadata", analysisMetadataJson);
            }

            // Configuration used - use 'metadata' family
            if (analysis.getConfigurationUsed() != null && !analysis.getConfigurationUsed().isEmpty()) {
                String configurationUsedJson = objectMapper.writeValueAsString(analysis.getConfigurationUsed());
                client.insertRecord(table, rowKey, "metadata", "configurationUsed", configurationUsedJson);
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize metadata", e);
        }
    }

    public UjianAnalysis findById(String analysisId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableAnalysis = TableName.valueOf(tableName);
        Map<String, String> columnMapping = getStandardColumnMapping();

        return client.showDataTable(tableAnalysis.toString(), columnMapping, analysisId, UjianAnalysis.class);
    }

    public List<UjianAnalysis> findBySchoolId(String schoolId, int size) throws IOException {
        TableName tableAnalysis = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        List<UjianAnalysis> analysisList = client.getDataListByColumnIndeks(
                tableAnalysis.toString(),
                columnMapping,
                "main",
                "idSchool",
                schoolId,
                UjianAnalysis.class,
                size,
                indexedFields);

        return analysisList;
    }

    public List<UjianAnalysis> findByUjianId(String ujianId, int size) throws IOException {
        TableName tableAnalysis = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        System.out.println("DEBUG: Finding analysis for ujianId: " + ujianId + " with size: " + size);

        List<UjianAnalysis> analysisList = client.getDataListByColumnIndeks(
                tableAnalysis.toString(),
                columnMapping,
                "main",
                "idUjian",
                ujianId,
                UjianAnalysis.class,
                size,
                indexedFields);

        System.out.println("DEBUG: Found " + analysisList.size() + " analyses for ujianId: " + ujianId);
        return analysisList;
    }

    public List<UjianAnalysis> findByUjianIdAndSchoolId(String ujianId, String schoolId, int size) throws IOException {
        // Get all analysis by ujian first, then filter by school
        List<UjianAnalysis> analysisList = findByUjianId(ujianId, size * 2);

        return analysisList.stream()
                .filter(analysis -> schoolId.equals(analysis.getIdSchool()))
                .limit(size)
                .collect(Collectors.toList());
    }

    public List<UjianAnalysis> findByAnalysisType(String analysisType, int size) throws IOException {
        TableName tableAnalysis = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        List<UjianAnalysis> analysisList = client.getDataListByColumnIndeks(
                tableAnalysis.toString(),
                columnMapping,
                "main",
                "analysisType",
                analysisType,
                UjianAnalysis.class,
                size,
                indexedFields);

        return analysisList;
    }

    public List<UjianAnalysis> findByAnalysisTypeAndSchoolId(String analysisType, String schoolId, int size)
            throws IOException {
        // Get all analysis by type first, then filter by school
        List<UjianAnalysis> analysisList = findByAnalysisType(analysisType, size * 2);

        return analysisList.stream()
                .filter(analysis -> schoolId.equals(analysis.getIdSchool()))
                .limit(size)
                .collect(Collectors.toList());
    }

    public List<UjianAnalysis> findByUjianIdAndAnalysisType(String ujianId, String analysisType) throws IOException {
        // Get all analysis by ujian first, then filter by type
        List<UjianAnalysis> analysisList = findByUjianId(ujianId, 100);
        return analysisList.stream()
                .filter(analysis -> analysisType != null && Objects.equals(analysisType, analysis.getAnalysisType()))
                .collect(Collectors.toList());
    }

    /**
     * Find analysis by multiple criteria - useful for complex queries
     */
    public List<UjianAnalysis> findAnalysisByCriteria(String schoolId, String analysisType, String generatedBy,
            int size) throws IOException {
        // First get by school if specified
        List<UjianAnalysis> analysisList;
        if (schoolId != null && !schoolId.equals("*")) {
            analysisList = findBySchoolId(schoolId, size * 3); // Get more for filtering
        } else {
            analysisList = findAll(size * 3);
        } // Apply filters
        return analysisList.stream()
                .filter(analysis -> analysisType == null || Objects.equals(analysisType, analysis.getAnalysisType()))
                .filter(analysis -> generatedBy == null || Objects.equals(generatedBy, analysis.getGeneratedBy()))
                .limit(size)
                .collect(Collectors.toList());
    }

    /**
     * Count analysis by criteria
     */
    public long countBySchoolAndType(String schoolId, String analysisType) throws IOException {
        List<UjianAnalysis> analysisList;
        if (schoolId != null && !schoolId.equals("*")) {
            analysisList = findBySchoolId(schoolId, 1000); // Get large number for counting
        } else {
            analysisList = findAll(1000);
        }
        return analysisList.stream()
                .filter(analysis -> analysisType == null || Objects.equals(analysisType, analysis.getAnalysisType()))
                .count();
    }

    /**
     * Get standard column mapping used across all queries
     */
    private Map<String, String> getStandardColumnMapping() {
        Map<String, String> columnMapping = new HashMap<>();

        // Main fields
        columnMapping.put("idAnalysis", "idAnalysis");
        columnMapping.put("idUjian", "idUjian");
        columnMapping.put("idSchool", "idSchool");
        columnMapping.put("analysisType", "analysisType");
        columnMapping.put("generatedBy", "generatedBy");
        columnMapping.put("generatedAt", "generatedAt");
        columnMapping.put("updatedAt", "updatedAt");
        columnMapping.put("analysisVersion", "analysisVersion");

        // Descriptive statistics
        columnMapping.put("totalParticipants", "totalParticipants");
        columnMapping.put("completedParticipants", "completedParticipants");
        columnMapping.put("incompleteParticipants", "incompleteParticipants");
        columnMapping.put("averageScore", "averageScore");
        columnMapping.put("medianScore", "medianScore");
        columnMapping.put("highestScore", "highestScore");
        columnMapping.put("lowestScore", "lowestScore");
        columnMapping.put("standardDeviation", "standardDeviation");
        columnMapping.put("variance", "variance");
        columnMapping.put("passedCount", "passedCount");
        columnMapping.put("failedCount", "failedCount");
        columnMapping.put("passRate", "passRate");
        columnMapping.put("failRate", "failRate");
        columnMapping.put("gradeDistribution", "gradeDistribution");
        columnMapping.put("gradePercentages", "gradePercentages");

        // Time analysis
        columnMapping.put("averageCompletionTime", "averageCompletionTime");
        columnMapping.put("shortestCompletionTime", "shortestCompletionTime");
        columnMapping.put("longestCompletionTime", "longestCompletionTime");
        columnMapping.put("timePerQuestion", "timePerQuestion");

        // Item analysis
        columnMapping.put("itemAnalysis", "itemAnalysis");

        // Difficulty analysis
        columnMapping.put("questionDifficulty", "questionDifficulty");
        columnMapping.put("questionDiscrimination", "questionDiscrimination");
        columnMapping.put("easiestQuestions", "easiestQuestions");
        columnMapping.put("hardestQuestions", "hardestQuestions");
        columnMapping.put("poorDiscriminatingQuestions", "poorDiscriminatingQuestions");

        // Performance analysis
        columnMapping.put("performanceByKelas", "performanceByKelas");
        columnMapping.put("performanceByMapel", "performanceByMapel");
        columnMapping.put("performanceByJurusan", "performanceByJurusan");

        // Cheating analysis
        columnMapping.put("suspiciousSubmissions", "suspiciousSubmissions");
        columnMapping.put("flaggedParticipants", "flaggedParticipants");
        columnMapping.put("integrityScore", "integrityScore");

        // Recommendations
        columnMapping.put("recommendations", "recommendations");
        columnMapping.put("improvementSuggestions", "improvementSuggestions");
        columnMapping.put("questionRecommendations", "questionRecommendations");
        columnMapping.put("curriculumSuggestions", "curriculumSuggestions");

        // Advanced analysis
        columnMapping.put("topicPerformanceAnalysis", "topicPerformanceAnalysis");
        columnMapping.put("answerPatternDistribution", "answerPatternDistribution");
        columnMapping.put("reliabilityCoefficient", "reliabilityCoefficient");
        columnMapping.put("validityIndex", "validityIndex");

        // Comparative analysis
        columnMapping.put("schoolComparison", "schoolComparison");
        columnMapping.put("nationalAverage", "nationalAverage");
        columnMapping.put("percentileRank", "percentileRank");

        // Learning analytics
        columnMapping.put("learningGaps", "learningGaps");
        columnMapping.put("studyRecommendations", "studyRecommendations");

        // Metadata
        columnMapping.put("analysisMetadata", "analysisMetadata");
        columnMapping.put("configurationUsed", "configurationUsed");

        // Relationships
        columnMapping.put("ujian", "ujian");
        columnMapping.put("school", "school");
        columnMapping.put("generatedByUser", "generatedByUser");
        // Cheating/Violation Integration
        columnMapping.put("violationIds", "violationIds");
        columnMapping.put("cheatDetections", "cheatDetections");

        return columnMapping;
    }

    /**
     * Get indexed fields configuration
     */
    private Map<String, String> getIndexedFields() {
        Map<String, String> indexedFields = new HashMap<>();

        // Complex objects that need special handling
        indexedFields.put("gradeDistribution", "MAP");
        indexedFields.put("gradePercentages", "MAP");
        indexedFields.put("timePerQuestion", "MAP");
        indexedFields.put("itemAnalysis", "MAP");
        indexedFields.put("questionDifficulty", "MAP");
        indexedFields.put("questionDiscrimination", "MAP");
        indexedFields.put("easiestQuestions", "LIST");
        indexedFields.put("hardestQuestions", "LIST");
        indexedFields.put("poorDiscriminatingQuestions", "LIST");
        indexedFields.put("performanceByKelas", "MAP");
        indexedFields.put("performanceByMapel", "MAP");
        indexedFields.put("performanceByJurusan", "MAP");
        indexedFields.put("recommendations", "LIST");
        indexedFields.put("improvementSuggestions", "LIST");
        indexedFields.put("questionRecommendations", "LIST");
        indexedFields.put("curriculumSuggestions", "LIST");
        indexedFields.put("topicPerformanceAnalysis", "MAP");
        indexedFields.put("answerPatternDistribution", "MAP");
        indexedFields.put("schoolComparison", "MAP");
        indexedFields.put("nationalAverage", "MAP");
        indexedFields.put("learningGaps", "MAP");
        indexedFields.put("studyRecommendations", "MAP");
        indexedFields.put("analysisMetadata", "MAP");
        indexedFields.put("configurationUsed", "MAP");
        indexedFields.put("violationIds", "LIST");
        indexedFields.put("cheatDetections", "LIST");

        return indexedFields;
    }

    public boolean deleteById(String analysisId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, analysisId);
        return true;
    }

    public boolean existsById(String analysisId) throws IOException {
        try {
            HBaseCustomClient client = new HBaseCustomClient(conf);
            TableName tableAnalysis = TableName.valueOf(tableName);
            Map<String, String> columnMapping = new HashMap<>();
            columnMapping.put("idAnalysis", "idAnalysis");

            UjianAnalysis analysis = client.getDataByColumn(tableAnalysis.toString(), columnMapping,
                    "main", "idAnalysis",
                    analysisId, UjianAnalysis.class);

            return analysis != null && analysis.getIdAnalysis() != null;
        } catch (Exception e) {
            return false; // If any error occurs, assume record doesn't exist
        }
    }

    /**
     * Update analysis metadata - optimized for metadata updates
     */
    public boolean updateMetadata(String analysisId, Map<String, Object> newMetadata) throws IOException {
        try {
            HBaseCustomClient client = new HBaseCustomClient(conf);
            TableName tableAnalysis = TableName.valueOf(tableName);

            String metadataJson = objectMapper.writeValueAsString(newMetadata);
            client.insertRecord(tableAnalysis, analysisId, "metadata", "analysisMetadata", metadataJson);
            client.insertRecord(tableAnalysis, analysisId, "main", "updatedAt",
                    java.time.Instant.now().toString());

            return true;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize metadata", e);
        }
    }

    /**
     * Update analysis recommendations - optimized for recommendation updates
     */
    public boolean updateRecommendations(String analysisId, List<String> recommendations,
            List<String> improvements) throws IOException {
        try {
            HBaseCustomClient client = new HBaseCustomClient(conf);
            TableName tableAnalysis = TableName.valueOf(tableName);

            if (recommendations != null) {
                String recommendationsJson = objectMapper.writeValueAsString(recommendations);
                client.insertRecord(tableAnalysis, analysisId, "recommendations", "recommendations",
                        recommendationsJson);
            }

            if (improvements != null) {
                String improvementsJson = objectMapper.writeValueAsString(improvements);
                client.insertRecord(tableAnalysis, analysisId, "recommendations", "improvementSuggestions",
                        improvementsJson);
            }

            client.insertRecord(tableAnalysis, analysisId, "main", "updatedAt",
                    java.time.Instant.now().toString());

            return true;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize recommendations", e);
        }
    }

    /**
     * Get analysis summary statistics
     */
    public Map<String, Object> getAnalysisStatistics(String schoolId) throws IOException {
        Map<String, Object> stats = new HashMap<>();

        List<UjianAnalysis> allAnalyses = schoolId.equals("*") ? findAll(1000) : findBySchoolId(schoolId, 1000);

        stats.put("totalAnalyses", allAnalyses.size());

        // Count by type
        long descriptiveCount = allAnalyses.stream()
                .filter(a -> "DESCRIPTIVE".equals(a.getAnalysisType()))
                .count();

        long comprehensiveCount = allAnalyses.stream()
                .filter(a -> "COMPREHENSIVE".equals(a.getAnalysisType()))
                .count();

        long itemAnalysisCount = allAnalyses.stream()
                .filter(a -> "ITEM_ANALYSIS".equals(a.getAnalysisType()))
                .count();

        stats.put("descriptiveAnalyses", descriptiveCount);
        stats.put("comprehensiveAnalyses", comprehensiveCount);
        stats.put("itemAnalyses", itemAnalysisCount);

        // Calculate average metrics
        double avgScore = allAnalyses.stream()
                .filter(a -> a.getAverageScore() != null)
                .mapToDouble(UjianAnalysis::getAverageScore)
                .average()
                .orElse(0.0);

        double avgPassRate = allAnalyses.stream()
                .filter(a -> a.getPassRate() != null)
                .mapToDouble(UjianAnalysis::getPassRate)
                .average()
                .orElse(0.0);

        double avgIntegrityScore = allAnalyses.stream()
                .filter(a -> a.getIntegrityScore() != null)
                .mapToDouble(UjianAnalysis::getIntegrityScore)
                .average()
                .orElse(0.0);

        stats.put("overallAverageScore", avgScore);
        stats.put("overallPassRate", avgPassRate);
        stats.put("overallIntegrityScore", avgIntegrityScore);

        return stats;
    }

    /**
     * Find recent analyses
     */
    public List<UjianAnalysis> findRecentAnalyses(String schoolId, int days, int size) throws IOException {
        java.time.Instant cutoffDate = java.time.Instant.now().minus(days, java.time.temporal.ChronoUnit.DAYS);

        List<UjianAnalysis> analysisList;
        if (schoolId != null && !schoolId.equals("*")) {
            analysisList = findBySchoolId(schoolId, size * 2);
        } else {
            analysisList = findAll(size * 2);
        }

        return analysisList.stream()
                .filter(analysis -> analysis.getGeneratedAt() != null &&
                        analysis.getGeneratedAt().isAfter(cutoffDate))
                .sorted((a1, a2) -> a2.getGeneratedAt().compareTo(a1.getGeneratedAt()))
                .limit(size)
                .collect(Collectors.toList());
    }
}