package com.doyatama.university.model;

import java.time.Instant;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class UjianAnalysis {
    private String idAnalysis;
    private String idUjian;
    private String idSchool;
    private String analysisType; // DESCRIPTIVE, ITEM_ANALYSIS, COMPARATIVE, DIFFICULTY_ANALYSIS, COMPREHENSIVE

    // Descriptive Statistics
    private Integer totalParticipants;
    private Integer completedParticipants;
    private Integer incompleteParticipants;
    private Double averageScore;
    private Double medianScore;
    private Double highestScore;
    private Double lowestScore;
    private Double standardDeviation;
    private Double variance;

    // Grade Distribution
    private Map<String, Integer> gradeDistribution; // A=10, B=20, C=15, etc
    private Map<String, Double> gradePercentages;

    // Pass/Fail Analysis
    private Integer passedCount;
    private Integer failedCount;
    private Double passRate;
    private Double failRate;

    // Time Analysis
    private Double averageCompletionTime; // in minutes
    private Double shortestCompletionTime;
    private Double longestCompletionTime;
    private Map<String, Double> timePerQuestion; // average time per question

    // Item Analysis (per soal)
    private Map<String, ItemAnalysisData> itemAnalysis;

    // Difficulty Analysis
    private Map<String, Double> questionDifficulty; // idBankSoal -> difficulty index (0-1)
    private Map<String, Double> questionDiscrimination; // discrimination index
    private List<String> easiestQuestions;
    private List<String> hardestQuestions;
    private List<String> poorDiscriminatingQuestions;

    // Performance by Categories
    private Map<String, CategoryPerformance> performanceByKelas;
    private Map<String, CategoryPerformance> performanceByMapel;
    private Map<String, CategoryPerformance> performanceByJurusan;

    // Cheating Analysis
    private Integer suspiciousSubmissions;
    private Integer flaggedParticipants;
    private Double integrityScore; // Overall exam integrity score (0.0 - 1.0)

    // Cheating/Violation Integration
    private List<String> violationIds;
    private List<Object> cheatDetections; // Use Object for generic, or replace with CheatDetection if available

    // Recommendations
    private List<String> recommendations;
    private List<String> improvementSuggestions;

    // Additional Analysis Fields
    private Map<String, Double> topicPerformanceAnalysis; // Topic -> average performance
    private Map<String, Integer> answerPatternDistribution; // Pattern -> count
    private List<String> questionRecommendations; // Questions to review/remove
    private Double reliabilityCoefficient; // Cronbach's alpha
    private Double validityIndex; // Content validity index

    // Comparative Analysis
    private Map<String, Double> schoolComparison; // Other schools comparison
    private Map<String, Double> nationalAverage; // National benchmarks
    private Double percentileRank; // School's percentile rank

    // Learning Analytics
    private Map<String, List<String>> learningGaps; // Topic -> list of weak areas
    private Map<String, String> studyRecommendations; // Participant -> study plan
    private List<String> curriculumSuggestions; // Curriculum improvement suggestions

    // Metadata
    private Map<String, Object> analysisMetadata;
    private String generatedBy;
    private Instant generatedAt;
    private Instant updatedAt;
    private String analysisVersion; // Version of analysis algorithm used
    private Map<String, Object> configurationUsed; // Analysis configuration parameters

    // Relationships
    private Ujian ujian;
    private School school;
    private User generatedByUser;

    // Inner classes for complex data structures
    public static class ItemAnalysisData {
        private String idBankSoal;
        private String pertanyaan;
        private String jenisSoal;
        private Integer totalResponses;
        private Integer correctResponses;
        private Double correctPercentage;
        private Double difficultyIndex; // P-value (0-1)
        private Double discriminationIndex; // Point-biserial correlation
        private Map<String, Integer> optionFrequency; // A=5, B=10, C=3, D=2
        private Map<String, Double> optionPercentage;
        private String recommendation; // GOOD, REVIEW, DISCARD, NEEDS_REVISION
        private Double reliability; // Item reliability
        private List<String> commonMistakes; // Common wrong answers
        private String difficultyLevel; // EASY, MEDIUM, HARD, VERY_HARD

        // Constructor
        public ItemAnalysisData() {
            this.optionFrequency = new HashMap<>();
            this.optionPercentage = new HashMap<>();
            this.commonMistakes = new ArrayList<>();
        }

        // Getters and setters
        public String getIdBankSoal() {
            return idBankSoal;
        }

        public void setIdBankSoal(String idBankSoal) {
            this.idBankSoal = idBankSoal;
        }

        public String getPertanyaan() {
            return pertanyaan;
        }

        public void setPertanyaan(String pertanyaan) {
            this.pertanyaan = pertanyaan;
        }

        public String getJenisSoal() {
            return jenisSoal;
        }

        public void setJenisSoal(String jenisSoal) {
            this.jenisSoal = jenisSoal;
        }

        public Integer getTotalResponses() {
            return totalResponses;
        }

        public void setTotalResponses(Integer totalResponses) {
            this.totalResponses = totalResponses;
        }

        public Integer getCorrectResponses() {
            return correctResponses;
        }

        public void setCorrectResponses(Integer correctResponses) {
            this.correctResponses = correctResponses;
        }

        public Double getCorrectPercentage() {
            return correctPercentage;
        }

        public void setCorrectPercentage(Double correctPercentage) {
            this.correctPercentage = correctPercentage;
        }

        public Double getDifficultyIndex() {
            return difficultyIndex;
        }

        public void setDifficultyIndex(Double difficultyIndex) {
            this.difficultyIndex = difficultyIndex;
        }

        public Double getDiscriminationIndex() {
            return discriminationIndex;
        }

        public void setDiscriminationIndex(Double discriminationIndex) {
            this.discriminationIndex = discriminationIndex;
        }

        public Map<String, Integer> getOptionFrequency() {
            return optionFrequency;
        }

        public void setOptionFrequency(Map<String, Integer> optionFrequency) {
            this.optionFrequency = optionFrequency;
        }

        public Map<String, Double> getOptionPercentage() {
            return optionPercentage;
        }

        public void setOptionPercentage(Map<String, Double> optionPercentage) {
            this.optionPercentage = optionPercentage;
        }

        public String getRecommendation() {
            return recommendation;
        }

        public void setRecommendation(String recommendation) {
            this.recommendation = recommendation;
        }

        public Double getReliability() {
            return reliability;
        }

        public void setReliability(Double reliability) {
            this.reliability = reliability;
        }

        public List<String> getCommonMistakes() {
            return commonMistakes;
        }

        public void setCommonMistakes(List<String> commonMistakes) {
            this.commonMistakes = commonMistakes;
        }

        public String getDifficultyLevel() {
            return difficultyLevel;
        }

        public void setDifficultyLevel(String difficultyLevel) {
            this.difficultyLevel = difficultyLevel;
        }
    }

    public static class CategoryPerformance {
        private String categoryName;
        private String categoryType; // KELAS, MAPEL, JURUSAN, GENDER, etc.
        private Integer participantCount;
        private Double averageScore;
        private Double highestScore;
        private Double lowestScore;
        private Double passRate;
        private Double standardDeviation;
        private Map<String, Integer> gradeDistribution;
        private Map<String, Double> topicPerformance; // For detailed topic analysis
        private List<String> strengthAreas;
        private List<String> weaknessAreas;

        // Constructor
        public CategoryPerformance() {
            this.gradeDistribution = new HashMap<>();
            this.topicPerformance = new HashMap<>();
            this.strengthAreas = new ArrayList<>();
            this.weaknessAreas = new ArrayList<>();
        }

        // Getters and setters
        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCategoryType() {
            return categoryType;
        }

        public void setCategoryType(String categoryType) {
            this.categoryType = categoryType;
        }

        public Integer getParticipantCount() {
            return participantCount;
        }

        public void setParticipantCount(Integer participantCount) {
            this.participantCount = participantCount;
        }

        public Double getAverageScore() {
            return averageScore;
        }

        public void setAverageScore(Double averageScore) {
            this.averageScore = averageScore;
        }

        public Double getHighestScore() {
            return highestScore;
        }

        public void setHighestScore(Double highestScore) {
            this.highestScore = highestScore;
        }

        public Double getLowestScore() {
            return lowestScore;
        }

        public void setLowestScore(Double lowestScore) {
            this.lowestScore = lowestScore;
        }

        public Double getPassRate() {
            return passRate;
        }

        public void setPassRate(Double passRate) {
            this.passRate = passRate;
        }

        public Double getStandardDeviation() {
            return standardDeviation;
        }

        public void setStandardDeviation(Double standardDeviation) {
            this.standardDeviation = standardDeviation;
        }

        public Map<String, Integer> getGradeDistribution() {
            return gradeDistribution;
        }

        public void setGradeDistribution(Map<String, Integer> gradeDistribution) {
            this.gradeDistribution = gradeDistribution;
        }

        public Map<String, Double> getTopicPerformance() {
            return topicPerformance;
        }

        public void setTopicPerformance(Map<String, Double> topicPerformance) {
            this.topicPerformance = topicPerformance;
        }

        public List<String> getStrengthAreas() {
            return strengthAreas;
        }

        public void setStrengthAreas(List<String> strengthAreas) {
            this.strengthAreas = strengthAreas;
        }

        public List<String> getWeaknessAreas() {
            return weaknessAreas;
        }

        public void setWeaknessAreas(List<String> weaknessAreas) {
            this.weaknessAreas = weaknessAreas;
        }
    }

    // Constructors
    public UjianAnalysis() {
        this.gradeDistribution = new HashMap<>();
        this.gradePercentages = new HashMap<>();
        this.timePerQuestion = new HashMap<>();
        this.itemAnalysis = new HashMap<>();
        this.questionDifficulty = new HashMap<>();
        this.questionDiscrimination = new HashMap<>();
        this.easiestQuestions = new ArrayList<>();
        this.hardestQuestions = new ArrayList<>();
        this.poorDiscriminatingQuestions = new ArrayList<>();
        this.performanceByKelas = new HashMap<>();
        this.performanceByMapel = new HashMap<>();
        this.performanceByJurusan = new HashMap<>();
        this.recommendations = new ArrayList<>();
        this.improvementSuggestions = new ArrayList<>();
        this.analysisMetadata = new HashMap<>();
        this.topicPerformanceAnalysis = new HashMap<>();
        this.answerPatternDistribution = new HashMap<>();
        this.questionRecommendations = new ArrayList<>();
        this.schoolComparison = new HashMap<>();
        this.nationalAverage = new HashMap<>();
        this.learningGaps = new HashMap<>();
        this.studyRecommendations = new HashMap<>();
        this.curriculumSuggestions = new ArrayList<>();
        this.configurationUsed = new HashMap<>();
        this.generatedAt = Instant.now();
        this.updatedAt = Instant.now();
        this.analysisVersion = "1.0";
        this.violationIds = new ArrayList<>();
        this.cheatDetections = new ArrayList<>();
    }

    public UjianAnalysis(String idUjian, String idSchool, String analysisType) {
        this();
        this.idUjian = idUjian;
        this.idSchool = idSchool;
        this.analysisType = analysisType;
    }

    // Main getters and setters
    public String getIdAnalysis() {
        return idAnalysis;
    }

    public void setIdAnalysis(String idAnalysis) {
        this.idAnalysis = idAnalysis;
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

    public String getAnalysisType() {
        return analysisType;
    }

    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
    }

    // Descriptive Statistics getters and setters
    public Integer getTotalParticipants() {
        return totalParticipants;
    }

    public void setTotalParticipants(Integer totalParticipants) {
        this.totalParticipants = totalParticipants;
    }

    public Integer getCompletedParticipants() {
        return completedParticipants;
    }

    public void setCompletedParticipants(Integer completedParticipants) {
        this.completedParticipants = completedParticipants;
    }

    public Integer getIncompleteParticipants() {
        return incompleteParticipants;
    }

    public void setIncompleteParticipants(Integer incompleteParticipants) {
        this.incompleteParticipants = incompleteParticipants;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public Double getMedianScore() {
        return medianScore;
    }

    public void setMedianScore(Double medianScore) {
        this.medianScore = medianScore;
    }

    public Double getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(Double highestScore) {
        this.highestScore = highestScore;
    }

    public Double getLowestScore() {
        return lowestScore;
    }

    public void setLowestScore(Double lowestScore) {
        this.lowestScore = lowestScore;
    }

    public Double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(Double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public Double getVariance() {
        return variance;
    }

    public void setVariance(Double variance) {
        this.variance = variance;
    }

    // Grade Distribution getters and setters
    public Map<String, Integer> getGradeDistribution() {
        return gradeDistribution;
    }

    public void setGradeDistribution(Map<String, Integer> gradeDistribution) {
        this.gradeDistribution = gradeDistribution;
    }

    public Map<String, Double> getGradePercentages() {
        return gradePercentages;
    }

    public void setGradePercentages(Map<String, Double> gradePercentages) {
        this.gradePercentages = gradePercentages;
    }

    // Pass/Fail Analysis getters and setters
    public Integer getPassedCount() {
        return passedCount;
    }

    public void setPassedCount(Integer passedCount) {
        this.passedCount = passedCount;
    }

    public Integer getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(Integer failedCount) {
        this.failedCount = failedCount;
    }

    public Double getPassRate() {
        return passRate;
    }

    public void setPassRate(Double passRate) {
        this.passRate = passRate;
    }

    public Double getFailRate() {
        return failRate;
    }

    public void setFailRate(Double failRate) {
        this.failRate = failRate;
    }

    // Time Analysis getters and setters
    public Double getAverageCompletionTime() {
        return averageCompletionTime;
    }

    public void setAverageCompletionTime(Double averageCompletionTime) {
        this.averageCompletionTime = averageCompletionTime;
    }

    public Double getShortestCompletionTime() {
        return shortestCompletionTime;
    }

    public void setShortestCompletionTime(Double shortestCompletionTime) {
        this.shortestCompletionTime = shortestCompletionTime;
    }

    public Double getLongestCompletionTime() {
        return longestCompletionTime;
    }

    public void setLongestCompletionTime(Double longestCompletionTime) {
        this.longestCompletionTime = longestCompletionTime;
    }

    public Map<String, Double> getTimePerQuestion() {
        return timePerQuestion;
    }

    public void setTimePerQuestion(Map<String, Double> timePerQuestion) {
        this.timePerQuestion = timePerQuestion;
    }

    // Item Analysis getters and setters
    public Map<String, ItemAnalysisData> getItemAnalysis() {
        return itemAnalysis;
    }

    public void setItemAnalysis(Map<String, ItemAnalysisData> itemAnalysis) {
        this.itemAnalysis = itemAnalysis;
    }

    // Difficulty Analysis getters and setters
    public Map<String, Double> getQuestionDifficulty() {
        return questionDifficulty;
    }

    public void setQuestionDifficulty(Map<String, Double> questionDifficulty) {
        this.questionDifficulty = questionDifficulty;
    }

    public Map<String, Double> getQuestionDiscrimination() {
        return questionDiscrimination;
    }

    public void setQuestionDiscrimination(Map<String, Double> questionDiscrimination) {
        this.questionDiscrimination = questionDiscrimination;
    }

    public List<String> getEasiestQuestions() {
        return easiestQuestions;
    }

    public void setEasiestQuestions(List<String> easiestQuestions) {
        this.easiestQuestions = easiestQuestions;
    }

    public List<String> getHardestQuestions() {
        return hardestQuestions;
    }

    public void setHardestQuestions(List<String> hardestQuestions) {
        this.hardestQuestions = hardestQuestions;
    }

    public List<String> getPoorDiscriminatingQuestions() {
        return poorDiscriminatingQuestions;
    }

    public void setPoorDiscriminatingQuestions(List<String> poorDiscriminatingQuestions) {
        this.poorDiscriminatingQuestions = poorDiscriminatingQuestions;
    }

    // Performance by Categories getters and setters
    public Map<String, CategoryPerformance> getPerformanceByKelas() {
        return performanceByKelas;
    }

    public void setPerformanceByKelas(Map<String, CategoryPerformance> performanceByKelas) {
        this.performanceByKelas = performanceByKelas;
    }

    public Map<String, CategoryPerformance> getPerformanceByMapel() {
        return performanceByMapel;
    }

    public void setPerformanceByMapel(Map<String, CategoryPerformance> performanceByMapel) {
        this.performanceByMapel = performanceByMapel;
    }

    public Map<String, CategoryPerformance> getPerformanceByJurusan() {
        return performanceByJurusan;
    }

    public void setPerformanceByJurusan(Map<String, CategoryPerformance> performanceByJurusan) {
        this.performanceByJurusan = performanceByJurusan;
    }

    // Cheating Analysis getters and setters
    public Integer getSuspiciousSubmissions() {
        return suspiciousSubmissions;
    }

    public void setSuspiciousSubmissions(Integer suspiciousSubmissions) {
        this.suspiciousSubmissions = suspiciousSubmissions;
    }

    public Integer getFlaggedParticipants() {
        return flaggedParticipants;
    }

    public void setFlaggedParticipants(Integer flaggedParticipants) {
        this.flaggedParticipants = flaggedParticipants;
    }

    public Double getIntegrityScore() {
        return integrityScore;
    }

    public void setIntegrityScore(Double integrityScore) {
        this.integrityScore = integrityScore;
    }

    // Cheating/Violation Integration getters and setters
    public List<String> getViolationIds() {
        return violationIds;
    }

    public void setViolationIds(List<String> violationIds) {
        this.violationIds = violationIds;
    }

    public List<Object> getCheatDetections() {
        return cheatDetections;
    }

    public void setCheatDetections(List<Object> cheatDetections) {
        this.cheatDetections = cheatDetections;
    }

    // Recommendations getters and setters
    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }

    public List<String> getImprovementSuggestions() {
        return improvementSuggestions;
    }

    public void setImprovementSuggestions(List<String> improvementSuggestions) {
        this.improvementSuggestions = improvementSuggestions;
    }

    // Additional Analysis Fields getters and setters
    public Map<String, Double> getTopicPerformanceAnalysis() {
        return topicPerformanceAnalysis;
    }

    public void setTopicPerformanceAnalysis(Map<String, Double> topicPerformanceAnalysis) {
        this.topicPerformanceAnalysis = topicPerformanceAnalysis;
    }

    public Map<String, Integer> getAnswerPatternDistribution() {
        return answerPatternDistribution;
    }

    public void setAnswerPatternDistribution(Map<String, Integer> answerPatternDistribution) {
        this.answerPatternDistribution = answerPatternDistribution;
    }

    public List<String> getQuestionRecommendations() {
        return questionRecommendations;
    }

    public void setQuestionRecommendations(List<String> questionRecommendations) {
        this.questionRecommendations = questionRecommendations;
    }

    public Double getReliabilityCoefficient() {
        return reliabilityCoefficient;
    }

    public void setReliabilityCoefficient(Double reliabilityCoefficient) {
        this.reliabilityCoefficient = reliabilityCoefficient;
    }

    public Double getValidityIndex() {
        return validityIndex;
    }

    public void setValidityIndex(Double validityIndex) {
        this.validityIndex = validityIndex;
    }

    // Comparative Analysis getters and setters
    public Map<String, Double> getSchoolComparison() {
        return schoolComparison;
    }

    public void setSchoolComparison(Map<String, Double> schoolComparison) {
        this.schoolComparison = schoolComparison;
    }

    public Map<String, Double> getNationalAverage() {
        return nationalAverage;
    }

    public void setNationalAverage(Map<String, Double> nationalAverage) {
        this.nationalAverage = nationalAverage;
    }

    public Double getPercentileRank() {
        return percentileRank;
    }

    public void setPercentileRank(Double percentileRank) {
        this.percentileRank = percentileRank;
    }

    // Learning Analytics getters and setters
    public Map<String, List<String>> getLearningGaps() {
        return learningGaps;
    }

    public void setLearningGaps(Map<String, List<String>> learningGaps) {
        this.learningGaps = learningGaps;
    }

    public Map<String, String> getStudyRecommendations() {
        return studyRecommendations;
    }

    public void setStudyRecommendations(Map<String, String> studyRecommendations) {
        this.studyRecommendations = studyRecommendations;
    }

    public List<String> getCurriculumSuggestions() {
        return curriculumSuggestions;
    }

    public void setCurriculumSuggestions(List<String> curriculumSuggestions) {
        this.curriculumSuggestions = curriculumSuggestions;
    }

    // Metadata getters and setters
    public Map<String, Object> getAnalysisMetadata() {
        return analysisMetadata;
    }

    public void setAnalysisMetadata(Map<String, Object> analysisMetadata) {
        this.analysisMetadata = analysisMetadata;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAnalysisVersion() {
        return analysisVersion;
    }

    public void setAnalysisVersion(String analysisVersion) {
        this.analysisVersion = analysisVersion;
    }

    public Map<String, Object> getConfigurationUsed() {
        return configurationUsed;
    }

    public void setConfigurationUsed(Map<String, Object> configurationUsed) {
        this.configurationUsed = configurationUsed;
    }

    // Relationships getters and setters
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

    public User getGeneratedByUser() {
        return generatedByUser;
    }

    public void setGeneratedByUser(User generatedByUser) {
        this.generatedByUser = generatedByUser;
    }

    // Utility methods
    public void addRecommendation(String recommendation) {
        if (this.recommendations == null) {
            this.recommendations = new ArrayList<>();
        }
        this.recommendations.add(recommendation);
        this.updatedAt = Instant.now();
    }

    public void addImprovementSuggestion(String suggestion) {
        if (this.improvementSuggestions == null) {
            this.improvementSuggestions = new ArrayList<>();
        }
        this.improvementSuggestions.add(suggestion);
        this.updatedAt = Instant.now();
    }

    public void addQuestionRecommendation(String questionRecommendation) {
        if (this.questionRecommendations == null) {
            this.questionRecommendations = new ArrayList<>();
        }
        this.questionRecommendations.add(questionRecommendation);
        this.updatedAt = Instant.now();
    }

    public void addCurriculumSuggestion(String suggestion) {
        if (this.curriculumSuggestions == null) {
            this.curriculumSuggestions = new ArrayList<>();
        }
        this.curriculumSuggestions.add(suggestion);
        this.updatedAt = Instant.now();
    }

    public void addMetadata(String key, Object value) {
        if (this.analysisMetadata == null) {
            this.analysisMetadata = new HashMap<>();
        }
        this.analysisMetadata.put(key, value);
        this.updatedAt = Instant.now();
    }

    public void updateTimestamp() {
        this.updatedAt = Instant.now();
    }

    // Validation method
    public boolean isValidAnalysis() {
        return this.idUjian != null &&
                this.idSchool != null &&
                this.analysisType != null &&
                this.totalParticipants != null &&
                this.totalParticipants > 0;
    }

    @Override
    public String toString() {
        return "UjianAnalysis{" +
                "idAnalysis='" + idAnalysis + '\'' +
                ", idUjian='" + idUjian + '\'' +
                ", analysisType='" + analysisType + '\'' +
                ", totalParticipants=" + totalParticipants +
                ", averageScore=" + averageScore +
                ", passRate=" + passRate +
                ", integrityScore=" + integrityScore +
                ", generatedAt=" + generatedAt +
                '}';
    }
}