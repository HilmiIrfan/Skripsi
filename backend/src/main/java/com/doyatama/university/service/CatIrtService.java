package com.doyatama.university.service;

import com.doyatama.university.model.BankSoalUjian;
import com.doyatama.university.model.Ujian;
import com.doyatama.university.model.UjianSession;
import com.doyatama.university.repository.CatExposureRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Service
public class CatIrtService {

    private static final Logger logger = LoggerFactory.getLogger(CatIrtService.class);

    // -------------------------------------------------------------------------
    // KONSTANTA UMUM
    // -------------------------------------------------------------------------
    private static final double D               = 1.7;
    private static final double MIN_THETA       = -4.0;
    private static final double MAX_THETA       = 4.0;
    private static final double MIN_PROBABILITY = 1.0e-9;

    // -------------------------------------------------------------------------
    // KONSTANTA MLE NEWTON-RAPHSON
    // -------------------------------------------------------------------------
    /** Batas maksimum iterasi Newton-Raphson. */
    private static final int    NR_MAX_ITER  = 50;
    /** Toleransi konvergensi: iterasi berhenti jika |Δθ| < NR_TOLERANCE. */
    private static final double NR_TOLERANCE = 1.0e-6;
    /** Guard pembagian dengan nol pada Hessian dan probabilitas. */
    private static final double NR_DELTA     = 1.0e-8;

    // -------------------------------------------------------------------------
    // KONSTANTA SYMPSON-HETTER
    // -------------------------------------------------------------------------
    /**
     * Target exposure rate default (K_i).
     * Nilai 0.5 berarti setiap soal boleh muncul di maksimal 50% sesi ujian.
     * Dapat di-override per-soal melalui catSettings.exposureRates.
     */
    private static final double DEFAULT_MAX_EXPOSURE_RATE = 0.5;

    /**
     * Jumlah sesi minimum sebelum gating Sympson-Hetter aktif (cold-start guard).
     * Selama data historis < SH_COLD_START, setiap soal selalu lolos gating.
     */
    private static final int    SH_COLD_START = 5;

    // -------------------------------------------------------------------------
    // DEPENDENCY: Repository HBase untuk Sympson-Hetter
    // exposureCount dan selectionCount kini disimpan persisten di HBase
    // melalui tabel `cat_exposure`, bukan lagi ConcurrentHashMap in-memory.
    // -------------------------------------------------------------------------
    @Autowired
    private CatExposureRepository catExposureRepository;

    // =========================================================================
    // INNER CLASS — Hasil estimasi IRT
    // =========================================================================
    public static class IrtEstimation {
        private final double theta;
        private final double standardError;
        private final double testInformation;
        private final int    answeredItems;
        /** Jumlah iterasi Newton-Raphson yang dibutuhkan hingga konvergen. */
        private final int    nrIterations;

        public IrtEstimation(double theta, double standardError,
                             double testInformation, int answeredItems, int nrIterations) {
            this.theta           = theta;
            this.standardError   = standardError;
            this.testInformation = testInformation;
            this.answeredItems   = answeredItems;
            this.nrIterations    = nrIterations;
        }

        public double getTheta()           { return theta; }
        public double getStandardError()   { return standardError; }
        public double getTestInformation() { return testInformation; }
        public int    getAnsweredItems()   { return answeredItems; }
        public int    getNrIterations()    { return nrIterations; }
    }

    // =========================================================================
    // INISIALISASI SESI CAT
    // =========================================================================
    public void initializeSession(UjianSession session, Ujian ujian) {
        if (session == null || ujian == null || !Boolean.TRUE.equals(ujian.getIsCatEnabled())) {
            return;
        }

        Map<String, Object> settings     = ujian.getCatSettings() != null
                                           ? ujian.getCatSettings() : new HashMap<>();
        double              initialTheta = getDouble(settings, "initialTheta", 0.0);

        session.setThetaEstimate(initialTheta);
        session.setStandardError(1.0);
        session.setAdministeredQuestionIds(new ArrayList<>());

        BankSoalUjian firstQuestion = selectNextQuestion(ujian, session);
        if (firstQuestion != null) {
            session.setCurrentAdaptiveQuestionId(firstQuestion.getIdBankSoal());
        }

        Map<String, Object> metadata = session.getAdaptiveMetadata();
        if (metadata == null) metadata = new HashMap<>();
        metadata.put("irtModel",            "3PL");
        metadata.put("estimationMethod",    "MLE-Newton-Raphson");
        metadata.put("exposureControl",     "Sympson-Hetter");
        metadata.put("initialTheta",        initialTheta);
        metadata.put("targetStandardError", getDouble(settings, "targetStandardError", 0.3));
        metadata.put("minQuestions",        getInt(settings, "minQuestions", 5));
        metadata.put("maxQuestions",
                getInt(settings, "maxQuestions", ujian.getJumlahSoal() != null ? ujian.getJumlahSoal() : 0));
        session.setAdaptiveMetadata(metadata);
    }

    // =========================================================================
    // UPDATE ESTIMASI SESI (dipanggil setiap jawaban masuk)
    // =========================================================================
    public IrtEstimation updateSessionEstimate(UjianSession session, Ujian ujian) {
        if (session == null || ujian == null || !Boolean.TRUE.equals(ujian.getIsCatEnabled())) {
            return null;
        }

        IrtEstimation estimation = estimateTheta(ujian.getBankSoalList(), session.getAnswers());
        session.setThetaEstimate(estimation.getTheta());
        session.setStandardError(estimation.getStandardError());

        Map<String, Object> metadata = session.getAdaptiveMetadata();
        if (metadata == null) metadata = new HashMap<>();
        metadata.put("irtModel",            "3PL");
        metadata.put("estimationMethod",    "MLE-Newton-Raphson");
        metadata.put("exposureControl",     "Sympson-Hetter");
        metadata.put("theta",               estimation.getTheta());
        metadata.put("standardError",       estimation.getStandardError());
        metadata.put("testInformation",     estimation.getTestInformation());
        metadata.put("answeredItems",       estimation.getAnsweredItems());
        metadata.put("nrIterations",        estimation.getNrIterations());
        metadata.put("scaledScore",         toScaledScore(estimation.getTheta()));
        metadata.put("proficiencyLevel",    toProficiencyLevel(estimation.getTheta()));
        session.setAdaptiveMetadata(metadata);

        BankSoalUjian nextQuestion = selectNextQuestion(ujian, session);
        session.setCurrentAdaptiveQuestionId(nextQuestion != null ? nextQuestion.getIdBankSoal() : null);

        return estimation;
    }

    // =========================================================================
    // ALGORITMA 1: MLE NEWTON-RAPHSON
    // Menggantikan estimasi EAP (grid integrasi) yang sebelumnya digunakan.
    //
    // Rumus update:
    //   θ_{t+1} = θ_t − L'(θ_t) / L''(θ_t)
    //
    // Turunan pertama log-likelihood (Gradient) untuk Model 3PL:
    //   L'(θ) = Σ_i  D·a_i·(u_i − P_i)·(P_i − c_i)
    //            ──────────────────────────────────────
    //                      P_i · (1 − c_i)
    //
    // Turunan kedua log-likelihood (Hessian):
    //   L''(θ) = Σ_i  −D²·a_i²·(P_i − c_i)²·(1 − P_i)
    //             ─────────────────────────────────────────
    //                      P_i² · (1 − c_i)²
    //
    // Konvergensi: |θ_{t+1} − θ_t| < NR_TOLERANCE  atau  t = NR_MAX_ITER
    // =========================================================================
    public IrtEstimation estimateTheta(List<BankSoalUjian> items, Map<String, Object> answers) {
        if (items == null || items.isEmpty() || answers == null || answers.isEmpty()) {
            return new IrtEstimation(0.0, 1.0, 1.0, 0, 0);
        }

        // Kumpulkan item yang sudah dijawab beserta status benar/salah
        List<BankSoalUjian> answeredItems = new ArrayList<>();
        List<Boolean>       correctness   = new ArrayList<>();

        for (BankSoalUjian item : items) {
            if (item == null || item.getIdBankSoal() == null) continue;
            if (!answers.containsKey(item.getIdBankSoal()))   continue;
            answeredItems.add(item);
            correctness.add(evaluateAnswer(item, answers.get(item.getIdBankSoal())));
        }

        if (answeredItems.isEmpty()) {
            return new IrtEstimation(0.0, 1.0, 1.0, 0, 0);
        }

        // Nilai awal θ₀ berdasarkan tingkat kesulitan soal yang dijawab benar
        double theta = clampTheta(computeInitialTheta(answeredItems, correctness));

        int iteration = 0;

        // ── Loop Newton-Raphson ──────────────────────────────────────────────
        for (iteration = 0; iteration < NR_MAX_ITER; iteration++) {

            double gradient = 0.0;  // L'(θ)  — turunan pertama
            double hessian  = 0.0;  // L''(θ) — turunan kedua

            for (int i = 0; i < answeredItems.size(); i++) {
                BankSoalUjian item    = answeredItems.get(i);
                boolean       correct = correctness.get(i);

                double a          = effectiveDiscrimination(item);
                double c          = effectiveGuessing(item);
                double p          = clampProbability(probability(theta, item));
                double u          = correct ? 1.0 : 0.0;
                double oneMinusC  = Math.max(NR_DELTA, 1.0 - c);
                double pMinusC    = p - c;

                // Gradient: L'(θ) += D·a·(u−P)·(P−c) / (P·(1−c))
                gradient += D * a * (u - p) * pMinusC
                            / (clampProbability(p) * oneMinusC);

                // Hessian: L''(θ) += −D²·a²·(P−c)²·(1−P) / (P²·(1−c)²)
                hessian  += -D * D * a * a
                            * (pMinusC * pMinusC)
                            * (1.0 - p)
                            / (clampProbability(p) * clampProbability(p)
                               * oneMinusC * oneMinusC);
            }

            // Hindari pembagian dengan nol
            if (Math.abs(hessian) < NR_DELTA) break;

            // Update: θ_new = θ − L'(θ)/L''(θ)
            double delta    = gradient / hessian;
            double thetaNew = clampTheta(theta - delta);

            if (Math.abs(thetaNew - theta) < NR_TOLERANCE) {
                theta = thetaNew;
                iteration++;
                break;
            }
            theta = thetaNew;
        }
        // ── Selesai Newton-Raphson ───────────────────────────────────────────

        // Hitung total test information dan standard error di θ akhir
        double totalInfo = 0.0;
        for (BankSoalUjian item : answeredItems) {
            totalInfo += itemInformation(theta, item);
        }
        double standardError = totalInfo > 0.0 ? 1.0 / Math.sqrt(totalInfo) : 1.0;

        return new IrtEstimation(
                round(theta),
                round(standardError),
                round(totalInfo),
                answeredItems.size(),
                iteration
        );
    }

    /**
     * Hitung nilai awal θ₀ untuk Newton-Raphson.
     * Heuristik: rata-rata kesulitan (b) soal yang dijawab BENAR.
     * Kasus ujung: semua salah → MIN_THETA+1, semua benar → MAX_THETA−1.
     */
    private double computeInitialTheta(List<BankSoalUjian> items, List<Boolean> correctness) {
        long correctCount = correctness.stream().filter(v -> v).count();
        if (correctCount == 0)             return MIN_THETA + 1.0;
        if (correctCount == items.size())  return MAX_THETA - 1.0;

        double sumB = 0.0;
        int    cnt  = 0;
        for (int i = 0; i < items.size(); i++) {
            if (correctness.get(i)) {
                sumB += effectiveDifficulty(items.get(i));
                cnt++;
            }
        }
        return cnt > 0 ? sumB / cnt : 0.0;
    }

    // =========================================================================
    // ALGORITMA 2: MFI + SYMPSON-HETTER EXPOSURE CONTROL
    // Menggantikan selectNextQuestion() yang hanya memfilter administered set.
    //
    // Prinsip Sympson-Hetter:
    //   Setiap item i memiliki parameter gating:
    //     a_i = K_i / r_i
    //       K_i : target exposure rate (dikonfigurasi admin, misal 0.5)
    //       r_i : estimasi P(item i terpilih MFI) dari data historis
    //
    //   Prosedur per langkah CAT:
    //     1. Urutkan kandidat berdasarkan I(θ) — descending (MFI)
    //     2. Catat semua kandidat ke selectionCount (data historis)
    //     3. Untuk setiap kandidat (mulai terbaik):
    //          u ~ Uniform(0,1)
    //          jika u < a_i → administrasikan item (lolos gating)
    //          jika u ≥ a_i → lewati, coba kandidat berikutnya
    //     4. Fallback: jika semua lewat → ambil informasi tertinggi tanpa gating
    // =========================================================================
    public BankSoalUjian selectNextQuestion(Ujian ujian, UjianSession session) {
        if (ujian == null || ujian.getBankSoalList() == null
                || ujian.getBankSoalList().isEmpty() || session == null) {
            return null;
        }
        if (shouldStop(ujian, session)) {
            return null;
        }

        Set<String> administered = buildAdministeredSet(session);
        double      theta        = session.getThetaEstimate() != null
                                   ? session.getThetaEstimate() : 0.0;

        // ── Langkah 1: Kumpulkan kandidat yang belum diadministrasikan ────────
        List<BankSoalUjian> candidates = new ArrayList<>();
        for (BankSoalUjian item : ujian.getBankSoalList()) {
            if (item == null || item.getIdBankSoal() == null) continue;
            if (administered.contains(item.getIdBankSoal()))  continue;
            candidates.add(item);
        }

        if (candidates.isEmpty()) return null;

        // ── Langkah 2: Urutkan berdasarkan Fisher Information — descending (MFI)
        candidates.sort((a, b) -> Double.compare(
                itemInformation(theta, b),
                itemInformation(theta, a)
        ));

        // ── Langkah 3: Catat bahwa kandidat-kandidat ini dipilih MFI (ke HBase) ──
        for (BankSoalUjian item : candidates) {
            catExposureRepository.incrementSelectionCount(item.getIdBankSoal());
        }

        // ── Langkah 4: Sympson-Hetter Gating ─────────────────────────────────
        Random random = new Random();
        for (BankSoalUjian item : candidates) {
            String id  = item.getIdBankSoal();

            // Hitung parameter kontrol a_i = K_i / r_i
            double K_i = getTargetExposureRate(id, ujian);   // target dari konfigurasi
            double r_i = estimateSelectionRate(id);          // historis dari HBase

            double a_i;
            if (r_i <= 0.0 || r_i <= K_i) {
                // Item jarang terpilih atau belum cukup data → selalu lolos
                a_i = 1.0;
            } else {
                a_i = K_i / r_i;   // nilai antara 0 dan 1
            }

            logger.debug("[SH] soal={} K_i={} r_i={} a_i={}", id, K_i, r_i, a_i);

            // Lempar dadu: u ~ Uniform(0,1)
            if (random.nextDouble() < a_i) {
                // LOLOS gating → simpan ke HBase dan administrasikan item ini
                catExposureRepository.incrementExposureCount(id);
                return item;
            }
            // GAGAL gating → coba kandidat berikutnya
        }

        // ── Langkah 5: Fallback (sangat jarang) ──────────────────────────────
        // Jika semua kandidat gagal gating, ambil yang informasinya tertinggi
        // tanpa gating agar ujian tidak stuck.
        BankSoalUjian fallback = candidates.get(0);
        catExposureRepository.incrementExposureCount(fallback.getIdBankSoal());
        return fallback;
    }

    /**
     * Ambil target exposure rate K_i untuk item tertentu.
     * Prioritas: catSettings.exposureRates[id] → catSettings.defaultExposureRate
     *            → DEFAULT_MAX_EXPOSURE_RATE (0.5)
     *
     * Contoh catSettings:
     * {
     *   "defaultExposureRate": 0.5,
     *   "exposureRates": { "soal-001": 0.3, "soal-002": 0.7 }
     * }
     */
    @SuppressWarnings("unchecked")
    private double getTargetExposureRate(String idBankSoal, Ujian ujian) {
        if (ujian.getCatSettings() == null) return DEFAULT_MAX_EXPOSURE_RATE;

        Object ratesObj = ujian.getCatSettings().get("exposureRates");
        if (ratesObj instanceof Map) {
            Map<String, Object> rates = (Map<String, Object>) ratesObj;
            Object val = rates.get(idBankSoal);
            if (val instanceof Number) return ((Number) val).doubleValue();
        }
        return getDouble(ujian.getCatSettings(), "defaultExposureRate", DEFAULT_MAX_EXPOSURE_RATE);
    }

    /**
     * Estimasi selection rate r_i dari data historis yang tersimpan di HBase.
     * r_i = exposureCount_i / selectionCount_i
     *
     * Cold-start: jika item belum pernah muncul SH_COLD_START kali,
     * kembalikan nilai kecil agar item bebas tampil selama pengumpulan data awal.
     */
    private double estimateSelectionRate(String idBankSoal) {
        // Baca dari HBase (persisten lintas-sesi dan lintas-restart)
        int selected = catExposureRepository.getSelectionCount(idBankSoal);
        int exposed  = catExposureRepository.getExposureCount(idBankSoal);

        if (selected < SH_COLD_START) {
            // Cold-start: belum cukup data → kembalikan nilai di bawah K default
            return DEFAULT_MAX_EXPOSURE_RATE * 0.5;
        }
        return (double) exposed / selected;
    }

    // =========================================================================
    // STOPPING RULE
    // =========================================================================
    public boolean shouldStop(Ujian ujian, UjianSession session) {
        if (ujian == null || session == null || !Boolean.TRUE.equals(ujian.getIsCatEnabled())) {
            return false;
        }

        Map<String, Object> settings     = ujian.getCatSettings() != null
                                           ? ujian.getCatSettings() : new HashMap<>();
        int                 answered     = session.getAnsweredQuestions() != null
                                           ? session.getAnsweredQuestions() : 0;
        int                 maxQuestions = getInt(settings, "maxQuestions",
                                           ujian.getJumlahSoal() != null
                                           ? ujian.getJumlahSoal() : answered);

        // Kriteria 1: Jumlah soal maksimum tercapai
        if (maxQuestions > 0 && answered >= maxQuestions) return true;

        // Kriteria 2: Standard Error sudah di bawah target (setelah min soal terpenuhi)
        double targetSE = getDouble(settings, "targetStandardError", 0.3);
        int    minQ     = getInt(settings, "minQuestions", 5);
        if (answered >= minQ
                && session.getStandardError() != null
                && session.getStandardError() <= targetSE) {
            return true;
        }

        return false;
    }

    /**
     * Cek apakah jawaban untuk soal tertentu BENAR.
     * Digunakan sebagai stopping rule tambahan di CAT mode:
     * jika jawaban SALAH → ujian langsung berhenti.
     */
    public boolean isAnswerCorrect(Ujian ujian, String idBankSoal, Object answer) {
        if (ujian == null || ujian.getBankSoalList() == null || idBankSoal == null) {
            return false;
        }
        for (BankSoalUjian item : ujian.getBankSoalList()) {
            if (item != null && idBankSoal.equals(item.getIdBankSoal())) {
                return evaluateAnswer(item, answer);
            }
        }
        return false;
    }

    // =========================================================================
    // MODEL 3PL — PROBABILITAS
    // P(θ) = c + (1 − c) · 1/(1 + e^{−D·a·(θ−b)})
    // =========================================================================
    public double probability(double theta, BankSoalUjian item) {
        double a        = effectiveDiscrimination(item);
        double b        = effectiveDifficulty(item);
        double c        = effectiveGuessing(item);
        double exponent = -D * a * (theta - b);
        double logistic = 1.0 / (1.0 + Math.exp(exponent));
        return c + (1.0 - c) * logistic;
    }

    // =========================================================================
    // ITEM INFORMATION FUNCTION
    // I(θ) = D²·a²·(Q/P)·((P−c)/(1−c))²
    // =========================================================================
    public double itemInformation(double theta, BankSoalUjian item) {
        double a        = effectiveDiscrimination(item);
        double c        = effectiveGuessing(item);
        double p        = clampProbability(probability(theta, item));
        double q        = 1.0 - p;
        double adjusted = (p - c) / Math.max(1.0e-6, 1.0 - c);
        return D * D * a * a * (q / p) * adjusted * adjusted;
    }

    // =========================================================================
    // KONVERSI SKOR
    // =========================================================================
    public double toScaledScore(double theta) {
        return round(Math.max(0.0, Math.min(100.0, 50.0 + theta * 12.5)));
    }

    public String toProficiencyLevel(double theta) {
        if (theta >= 1.5)  return "ADVANCED";
        if (theta >= 0.5)  return "PROFICIENT";
        if (theta >= -0.5) return "BASIC";
        return "NEEDS_SUPPORT";
    }

    // =========================================================================
    // EVALUASI JAWABAN
    // =========================================================================
    public boolean evaluateAnswer(BankSoalUjian item, Object answer) {
        if (item == null || answer == null
                || item.getJawabanBenar() == null || item.getJawabanBenar().isEmpty()) {
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

    // =========================================================================
    // HELPER METHODS
    // =========================================================================

    /** Bangun Set soal yang sudah diadministrasikan dari session. */
    private Set<String> buildAdministeredSet(UjianSession session) {
        Set<String> administered = new HashSet<>();
        if (session.getAdministeredQuestionIds() != null) {
            administered.addAll(session.getAdministeredQuestionIds());
        }
        if (session.getAnswers() != null) {
            administered.addAll(session.getAnswers().keySet());
        }
        return administered;
    }

    private double clampTheta(double theta) {
        return Math.max(MIN_THETA, Math.min(MAX_THETA, theta));
    }

    private double clampProbability(double value) {
        return Math.max(MIN_PROBABILITY, Math.min(1.0 - MIN_PROBABILITY, value));
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
                normalized.add(normalize(String.valueOf(entry.getKey())
                        + "=" + String.valueOf(entry.getValue())));
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
