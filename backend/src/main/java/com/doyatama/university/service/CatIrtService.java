package com.doyatama.university.service;

import com.doyatama.university.model.BankSoalUjian;
import com.doyatama.university.model.Ujian;
import com.doyatama.university.model.UjianSession;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CatIrtService {
    private static final double D = 1.7;
    private static final double MIN_THETA = -4.0;
    private static final double MAX_THETA = 4.0;
    private static final double THETA_STEP = 0.1;
    private static final double MIN_PROBABILITY = 1.0e-9;

    public static class IrtEstimation {
        private final double theta;
        private final double standardError;
        private final double testInformation;
        private final int answeredItems;

        public IrtEstimation(double theta, double standardError, double testInformation, int answeredItems) {
            this.theta = theta;
            this.standardError = standardError;
            this.testInformation = testInformation;
            this.answeredItems = answeredItems;
        }

        public double getTheta() {
            return theta;
        }

        public double getStandardError() {
            return standardError;
        }

        public double getTestInformation() {
            return testInformation;
        }

        public int getAnsweredItems() {
            return answeredItems;
        }
    }

    public void initializeSession(UjianSession session, Ujian ujian) {
        if (session == null || ujian == null || !Boolean.TRUE.equals(ujian.getIsCatEnabled())) {
            return;
        }

        Map<String, Object> settings = ujian.getCatSettings() != null ? ujian.getCatSettings() : new HashMap<>();
        double initialTheta = getDouble(settings, "initialTheta", 0.0);
        session.setThetaEstimate(initialTheta);
        session.setStandardError(1.0);
        session.setAdministeredQuestionIds(new ArrayList<>());

        BankSoalUjian firstQuestion = selectNextQuestion(ujian, session);
        if (firstQuestion != null) {
            session.setCurrentAdaptiveQuestionId(firstQuestion.getIdBankSoal());
        }

        Map<String, Object> metadata = session.getAdaptiveMetadata();
        if (metadata == null) {
            metadata = new HashMap<>();
        }
        metadata.put("irtModel", "3PL");
        metadata.put("initialTheta", initialTheta);
        metadata.put("targetStandardError", getDouble(settings, "targetStandardError", 0.3));
        metadata.put("minQuestions", getInt(settings, "minQuestions", 5));
        metadata.put("maxQuestions",
                getInt(settings, "maxQuestions", ujian.getJumlahSoal() != null ? ujian.getJumlahSoal() : 0));
        session.setAdaptiveMetadata(metadata);
    }

    public IrtEstimation updateSessionEstimate(UjianSession session, Ujian ujian) {
        if (session == null || ujian == null || !Boolean.TRUE.equals(ujian.getIsCatEnabled())) {
            return null;
        }

        IrtEstimation estimation = estimateTheta(ujian.getBankSoalList(), session.getAnswers());
        session.setThetaEstimate(estimation.getTheta());
        session.setStandardError(estimation.getStandardError());

        Map<String, Object> metadata = session.getAdaptiveMetadata();
        if (metadata == null) {
            metadata = new HashMap<>();
        }
        metadata.put("irtModel", "3PL");
        metadata.put("theta", estimation.getTheta());
        metadata.put("standardError", estimation.getStandardError());
        metadata.put("testInformation", estimation.getTestInformation());
        metadata.put("answeredItems", estimation.getAnsweredItems());
        metadata.put("scaledScore", toScaledScore(estimation.getTheta()));
        metadata.put("proficiencyLevel", toProficiencyLevel(estimation.getTheta()));
        session.setAdaptiveMetadata(metadata);

        BankSoalUjian nextQuestion = selectNextQuestion(ujian, session);
        session.setCurrentAdaptiveQuestionId(nextQuestion != null ? nextQuestion.getIdBankSoal() : null);

        return estimation;
    }

    public IrtEstimation estimateTheta(List<BankSoalUjian> items, Map<String, Object> answers) {
        if (items == null || items.isEmpty() || answers == null || answers.isEmpty()) {
            return new IrtEstimation(0.0, 1.0, 1.0, 0);
        }

        double weightedThetaSum = 0.0;
        double weightSum = 0.0;
        int answeredItems = 0;

        for (double theta = MIN_THETA; theta <= MAX_THETA + 1.0e-9; theta += THETA_STEP) {
            double logPosterior = -0.5 * theta * theta;

            for (BankSoalUjian item : items) {
                if (item == null || item.getIdBankSoal() == null || !answers.containsKey(item.getIdBankSoal())) {
                    continue;
                }

                Object answer = answers.get(item.getIdBankSoal());
                boolean correct = evaluateAnswer(item, answer);
                double p = clampProbability(probability(theta, item));
                logPosterior += correct ? Math.log(p) : Math.log(1.0 - p);
            }

            double weight = Math.exp(logPosterior);
            weightedThetaSum += theta * weight;
            weightSum += weight;
        }

        if (weightSum <= 0.0 || Double.isNaN(weightSum) || Double.isInfinite(weightSum)) {
            return new IrtEstimation(0.0, 1.0, 1.0, 0);
        }

        double thetaEstimate = weightedThetaSum / weightSum;
        double information = 0.0;

        for (BankSoalUjian item : items) {
            if (item == null || item.getIdBankSoal() == null || !answers.containsKey(item.getIdBankSoal())) {
                continue;
            }
            information += itemInformation(thetaEstimate, item);
            answeredItems++;
        }

        double standardError = information > 0.0 ? 1.0 / Math.sqrt(information) : 1.0;
        return new IrtEstimation(round(thetaEstimate), round(standardError), round(information), answeredItems);
    }

    public BankSoalUjian selectNextQuestion(Ujian ujian, UjianSession session) {
        if (ujian == null || ujian.getBankSoalList() == null || ujian.getBankSoalList().isEmpty() || session == null) {
            return null;
        }

        if (shouldStop(ujian, session)) {
            return null;
        }

        Set<String> administered = new HashSet<>();
        if (session.getAdministeredQuestionIds() != null) {
            administered.addAll(session.getAdministeredQuestionIds());
        }
        if (session.getAnswers() != null) {
            administered.addAll(session.getAnswers().keySet());
        }

        double theta = session.getThetaEstimate() != null ? session.getThetaEstimate() : 0.0;
        BankSoalUjian selected = null;
        double bestInformation = -1.0;

        for (BankSoalUjian item : ujian.getBankSoalList()) {
            if (item == null || item.getIdBankSoal() == null || administered.contains(item.getIdBankSoal())) {
                continue;
            }

            double information = itemInformation(theta, item);
            if (information > bestInformation) {
                bestInformation = information;
                selected = item;
            }
        }

        return selected;
    }

    public boolean shouldStop(Ujian ujian, UjianSession session) {
        if (ujian == null || session == null || !Boolean.TRUE.equals(ujian.getIsCatEnabled())) {
            return false;
        }

        Map<String, Object> settings = ujian.getCatSettings() != null ? ujian.getCatSettings() : new HashMap<>();
        int answered = session.getAnsweredQuestions() != null ? session.getAnsweredQuestions() : 0;
        int minQuestions = getInt(settings, "minQuestions", 5);
        int maxQuestions = getInt(settings, "maxQuestions",
                ujian.getJumlahSoal() != null ? ujian.getJumlahSoal() : answered);
        double targetStandardError = getDouble(settings, "targetStandardError", 0.3);

        if (maxQuestions > 0 && answered >= maxQuestions) {
            return true;
        }

        return answered >= minQuestions
                && session.getStandardError() != null
                && session.getStandardError() <= targetStandardError;
    }

    public double probability(double theta, BankSoalUjian item) {
        double a = effectiveDiscrimination(item);
        double b = effectiveDifficulty(item);
        double c = effectiveGuessing(item);
        double exponent = -D * a * (theta - b);
        double logistic = 1.0 / (1.0 + Math.exp(exponent));
        return c + (1.0 - c) * logistic;
    }

    public double itemInformation(double theta, BankSoalUjian item) {
        double a = effectiveDiscrimination(item);
        double c = effectiveGuessing(item);
        double p = clampProbability(probability(theta, item));
        double q = 1.0 - p;
        double adjusted = (p - c) / Math.max(1.0e-6, 1.0 - c);
        return D * D * a * a * (q / p) * adjusted * adjusted;
    }

    public double toScaledScore(double theta) {
        return round(Math.max(0.0, Math.min(100.0, 50.0 + theta * 12.5)));
    }

    public String toProficiencyLevel(double theta) {
        if (theta >= 1.5) {
            return "ADVANCED";
        }
        if (theta >= 0.5) {
            return "PROFICIENT";
        }
        if (theta >= -0.5) {
            return "BASIC";
        }
        return "NEEDS_SUPPORT";
    }

    public boolean evaluateAnswer(BankSoalUjian item, Object answer) {
        if (item == null || answer == null || item.getJawabanBenar() == null || item.getJawabanBenar().isEmpty()) {
            return false;
        }

        String jenisSoal = item.getJenisSoal() != null ? item.getJenisSoal().toUpperCase() : "";
        switch (jenisSoal) {
            case "PG":
            case "ISIAN":
                return item.getJawabanBenar().stream()
                        .anyMatch(correct -> normalize(correct).equals(normalize(answer.toString())));
            case "MULTI":
            case "COCOK":
                return normalizeSet(answer).equals(new HashSet<>(normalizeList(item.getJawabanBenar())));
            default:
                return false;
        }
    }

    private List<String> normalizeList(List<String> values) {
        List<String> normalized = new ArrayList<>();
        for (String value : values) {
            normalized.add(normalize(value));
        }
        return normalized;
    }

    private Set<String> normalizeSet(Object value) {
        Set<String> normalized = new HashSet<>();
        if (value instanceof List) {
            for (Object item : (List<?>) value) {
                normalized.add(normalize(String.valueOf(item)));
            }
        } else if (value instanceof Map) {
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                normalized.add(normalize(String.valueOf(entry.getKey()) + "=" + String.valueOf(entry.getValue())));
            }
        } else {
            String[] parts = value.toString().split(",");
            for (String part : parts) {
                normalized.add(normalize(part));
            }
        }
        return normalized;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }

    private double effectiveDiscrimination(BankSoalUjian item) {
        Double value = item.getIrtDiscrimination();
        return value != null && value > 0.0 ? value : 1.0;
    }

    private double effectiveDifficulty(BankSoalUjian item) {
        Double value = item.getIrtDifficulty();
        if (value != null) {
            return Math.max(MIN_THETA, Math.min(MAX_THETA, value));
        }
        try {
            double bobot = Double.parseDouble(item.getBobot());
            return Math.max(MIN_THETA, Math.min(MAX_THETA, (bobot - 1.0)));
        } catch (Exception e) {
            return 0.0;
        }
    }

    private double effectiveGuessing(BankSoalUjian item) {
        Double value = item.getIrtGuessing();
        if (value != null && value >= 0.0 && value < 1.0) {
            return value;
        }
        if (item.getOpsi() != null && !item.getOpsi().isEmpty()) {
            return Math.min(0.35, 1.0 / item.getOpsi().size());
        }
        return 0.0;
    }

    private double clampProbability(double value) {
        return Math.max(MIN_PROBABILITY, Math.min(1.0 - MIN_PROBABILITY, value));
    }

    private double getDouble(Map<String, Object> map, String key, double defaultValue) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        if (value != null) {
            try {
                return Double.parseDouble(value.toString());
            } catch (NumberFormatException ignored) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    private int getInt(Map<String, Object> map, String key, int defaultValue) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value != null) {
            try {
                return Integer.parseInt(value.toString());
            } catch (NumberFormatException ignored) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    private double round(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }
}
