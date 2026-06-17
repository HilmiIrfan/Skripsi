package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.BankSoal;
import com.doyatama.university.model.BankSoalUjian;
import com.doyatama.university.model.Kelas;
import com.doyatama.university.model.KonsentrasiKeahlianSekolah;
import com.doyatama.university.model.Mapel;
import com.doyatama.university.model.School;
import com.doyatama.university.model.Semester;
import com.doyatama.university.model.TahunAjaran;
import com.doyatama.university.model.Ujian;
import com.doyatama.university.model.User;
import com.doyatama.university.model.UjianAnalysis;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.payload.UjianRequest;
import com.doyatama.university.repository.BankSoalRepository;
import com.doyatama.university.repository.KelasRepository;
import com.doyatama.university.repository.KonsentrasiKeahlianSekolahRepository;
import com.doyatama.university.repository.MapelRepository;
import com.doyatama.university.repository.SchoolRepository;
import com.doyatama.university.repository.SemesterRepository;
import com.doyatama.university.repository.TahunAjaranRepository;
import com.doyatama.university.repository.UjianRepository;
import com.doyatama.university.repository.UserRepository;
import com.doyatama.university.util.AppConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UjianService {

    private static final Logger logger = LoggerFactory.getLogger(UjianService.class);

    private UjianRepository ujianRepository = new UjianRepository();
    private BankSoalRepository bankSoalRepository = new BankSoalRepository();
    private TahunAjaranRepository tahunAjaranRepository = new TahunAjaranRepository();
    private KelasRepository kelasRepository = new KelasRepository();
    private SemesterRepository semesterRepository = new SemesterRepository();
    private MapelRepository mapelRepository = new MapelRepository();
    private KonsentrasiKeahlianSekolahRepository konsentrasiKeahlianSekolahRepository = new KonsentrasiKeahlianSekolahRepository();
    private SchoolRepository schoolRepository = new SchoolRepository();
    private UserRepository userRepository = new UserRepository();
    @Autowired
    private UjianAnalysisService ujianAnalysisService;

    @Autowired
    private HasilUjianService hasilUjianService;

    public PagedResponse<Ujian> getAllUjian(int page, int size, String userID, String schoolID) throws IOException {
        validatePageNumberAndSize(page, size);

        List<Ujian> ujianResponse;

        if (schoolID.equalsIgnoreCase("*")) {
            ujianResponse = ujianRepository.findAll(size);
        } else {
            ujianResponse = ujianRepository.findUjianBySekolah(schoolID, size);
        }

        // Enrich ujian data with participant counts
        for (Ujian ujian : ujianResponse) {
            try {
                enrichUjianWithParticipantData(ujian);
            } catch (Exception e) {
                logger.warn("Failed to enrich ujian {} with participant data: {}", ujian.getIdUjian(), e.getMessage());
            }
        }

        return new PagedResponse<>(ujianResponse, ujianResponse.size(), "Successfully get data", 200);
    }

    public PagedResponse<Ujian> getUjianByStatus(String status, int page, int size, String schoolID)
            throws IOException {
        validatePageNumberAndSize(page, size);
        validateStatus(status);

        List<Ujian> ujianResponse;

        if (schoolID.equalsIgnoreCase("*")) {
            ujianResponse = ujianRepository.findByStatus(status, size);
        } else {
            ujianResponse = ujianRepository.findByStatusAndSekolah(status, schoolID, size);
        }

        return new PagedResponse<>(ujianResponse, ujianResponse.size(), "Successfully get data", 200);
    }

    public PagedResponse<Ujian> getUjianAktif(int page, int size, String schoolID) throws IOException {
        validatePageNumberAndSize(page, size);

        List<Ujian> ujianResponse;

        if (schoolID.equalsIgnoreCase("*")) {
            ujianResponse = ujianRepository.findActiveUjian(size);
        } else {
            ujianResponse = ujianRepository.findActiveUjianBySekolah(schoolID, size);
        }

        return new PagedResponse<>(ujianResponse, ujianResponse.size(), "Successfully get data", 200);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    private void validateStatus(String status) {
        if (status == null || (!status.equals("DRAFT") && !status.equals("AKTIF") &&
                !status.equals("SELESAI") && !status.equals("DIBATALKAN"))) {
            throw new BadRequestException("Status must be one of: DRAFT, AKTIF, SELESAI, DIBATALKAN");
        }
    }

    public Ujian createUjian(UjianRequest ujianRequest) throws IOException {
        // Validate input
        validateUjianRequest(ujianRequest);

        // 1. Create and initialize Ujian object
        Ujian ujian = new Ujian();
        ujian.setIdUjian(UUID.randomUUID().toString());
        ujian.setCreatedAt(Instant.now());
        ujian.setUpdatedAt(Instant.now());

        // 2. Set basic fields
        ujian.setNamaUjian(ujianRequest.getNamaUjian());
        ujian.setDeskripsi(ujianRequest.getDeskripsi());
        ujian.setDurasiMenit(ujianRequest.getDurasiMenit());
        ujian.setWaktuMulaiDijadwalkan(ujianRequest.getWaktuMulaiDijadwalkan());
        ujian.setWaktuSelesaiOtomatis(ujianRequest.getWaktuSelesaiOtomatis());
        ujian.setStatusUjian(ujianRequest.getStatusUjian() != null ? ujianRequest.getStatusUjian() : "DRAFT");
        ujian.setIsLive(ujianRequest.getIsLive() != null ? ujianRequest.getIsLive() : false);
        ujian.setTipeSoal(ujianRequest.getTipeSoal() != null ? ujianRequest.getTipeSoal() : "ACAK");
        ujian.setTampilkanNilai(ujianRequest.getTampilkanNilai());

        // 3. HYDRATE RELATION OBJECTS
        hydrateRelationObjects(ujian, ujianRequest);

        // 4. PROCESS BANK SOAL (DENORMALIZATION)
        processBankSoalForUjian(ujian, ujianRequest);

        // 5. SET ALL OTHER CONFIGURATION FIELDS
        setUjianConfigurations(ujian, ujianRequest);

        // 6. Calculate automatic end time if needed
        calculateAutomaticEndTime(ujian);

        // 7. SAVE TO REPOSITORY
        return ujianRepository.save(ujian);
    }

    public Ujian updateUjian(String ujianId, UjianRequest ujianRequest) throws IOException {
        Ujian existingUjian = ujianRepository.findById(ujianId);
        if (existingUjian == null) {
            throw new ResourceNotFoundException("Ujian", "id", ujianId);
        }

        // Don't allow update if ujian is already active or finished
        if (existingUjian.isAktif() || existingUjian.isSelesai()) {
            throw new BadRequestException("Tidak dapat mengubah ujian yang sudah aktif atau selesai");
        }

        validateUjianRequest(ujianRequest);

        // Update basic fields
        existingUjian.setNamaUjian(ujianRequest.getNamaUjian());
        existingUjian.setDeskripsi(ujianRequest.getDeskripsi());
        existingUjian.setDurasiMenit(ujianRequest.getDurasiMenit());
        existingUjian.setWaktuMulaiDijadwalkan(ujianRequest.getWaktuMulaiDijadwalkan());
        existingUjian.setWaktuSelesaiOtomatis(ujianRequest.getWaktuSelesaiOtomatis());
        existingUjian.setTipeSoal(ujianRequest.getTipeSoal());
        existingUjian.setTampilkanNilai(ujianRequest.getTampilkanNilai());
        existingUjian.setUpdatedAt(Instant.now());

        // Update relation objects
        hydrateRelationObjects(existingUjian, ujianRequest);

        // Update bank soal if provided
        if (ujianRequest.getIdBankSoalList() != null) {
            processBankSoalForUjian(existingUjian, ujianRequest);
        }

        // Update configurations
        setUjianConfigurations(existingUjian, ujianRequest);

        // Recalculate automatic end time if needed
        calculateAutomaticEndTime(existingUjian);

        return ujianRepository.save(existingUjian);
    }

    private void hydrateRelationObjects(Ujian ujian, UjianRequest ujianRequest) throws IOException {
        if (ujianRequest.getIdTahun() != null) {
            TahunAjaran tahunAjaran = tahunAjaranRepository.findById(ujianRequest.getIdTahun());
            if (tahunAjaran == null) {
                throw new ResourceNotFoundException("Tahun Ajaran not found with id: " + ujianRequest.getIdTahun(),
                        "idTahun", ujianRequest.getIdTahun());
            }
            ujian.setTahunAjaran(tahunAjaran);
        }

        if (ujianRequest.getIdKelas() != null) {
            Kelas kelas = kelasRepository.findById(ujianRequest.getIdKelas());
            if (kelas == null) {
                throw new ResourceNotFoundException("Kelas not found with id: " + ujianRequest.getIdKelas(),
                        "idKelas", ujianRequest.getIdKelas());
            }
            ujian.setKelas(kelas);
        }

        if (ujianRequest.getIdSemester() != null) {
            Semester semester = semesterRepository.findById(ujianRequest.getIdSemester());
            if (semester == null) {
                throw new ResourceNotFoundException("Semester not found with id: " + ujianRequest.getIdSemester(),
                        "idSemester", ujianRequest.getIdSemester());
            }
            ujian.setSemester(semester);
        }

        if (ujianRequest.getIdMapel() != null) {
            Mapel mapel = mapelRepository.findById(ujianRequest.getIdMapel());
            if (mapel == null) {
                throw new ResourceNotFoundException("Mapel not found with id: " + ujianRequest.getIdMapel(),
                        "idMapel", ujianRequest.getIdMapel());
            }
            ujian.setMapel(mapel);
        }

        if (ujianRequest.getIdKonsentrasiKeahlianSekolah() != null) {
            KonsentrasiKeahlianSekolah kks = konsentrasiKeahlianSekolahRepository
                    .findById(ujianRequest.getIdKonsentrasiKeahlianSekolah());
            if (kks == null) {
                throw new ResourceNotFoundException("Konsentrasi Keahlian Sekolah not found with id: "
                        + ujianRequest.getIdKonsentrasiKeahlianSekolah(), "idKonsentrasiKeahlianSekolah",
                        ujianRequest.getIdKonsentrasiKeahlianSekolah());
            }
            ujian.setKonsentrasiKeahlianSekolah(kks);
        }

        if (ujianRequest.getIdSchool() != null) {
            School school = schoolRepository.findById(ujianRequest.getIdSchool());
            if (school == null) {
                throw new ResourceNotFoundException("School not found with id: " + ujianRequest.getIdSchool(),
                        "idSchool", ujianRequest.getIdSchool());
            }
            ujian.setSchool(school);
        }

        if (ujianRequest.getIdCreatedBy() != null) {
            User createdBy = userRepository.findById(ujianRequest.getIdCreatedBy());
            if (createdBy == null) {
                throw new ResourceNotFoundException("User not found with id: " + ujianRequest.getIdCreatedBy(),
                        "idCreatedBy", ujianRequest.getIdCreatedBy());
            }
            ujian.setCreatedBy(createdBy);
        }
    }

    private void processBankSoalForUjian(Ujian ujian, UjianRequest ujianRequest) throws IOException {
        if (ujianRequest.getIdBankSoalList() == null || ujianRequest.getIdBankSoalList().isEmpty()) {
            throw new IllegalArgumentException("Daftar ID Bank Soal tidak boleh kosong");
        }

        // Get full BankSoal objects from repository
        List<BankSoal> fullBankSoalList = bankSoalRepository.findAllById(ujianRequest.getIdBankSoalList());
        if (fullBankSoalList.size() != ujianRequest.getIdBankSoalList().size()) {
            throw new IllegalArgumentException("Salah satu atau lebih ID Bank Soal tidak ditemukan");
        }

        // Transform to BankSoalUjian (denormalized version)
        List<BankSoalUjian> simpleBankSoalList = fullBankSoalList.stream()
                .map(BankSoalUjian::new) // Use the constructor that takes BankSoal
                .collect(Collectors.toList());

        // Set both ID list and denormalized objects
        ujian.setIdBankSoalList(ujianRequest.getIdBankSoalList());
        ujian.setBankSoalList(simpleBankSoalList);

        // Calculate totals from the denormalized data
        ujian.setJumlahSoal(simpleBankSoalList.size());
        ujian.setTotalBobot(simpleBankSoalList.stream()
                .mapToDouble(s -> Double.parseDouble(s.getBobot()))
                .sum());
    }

    private void setUjianConfigurations(Ujian ujian, UjianRequest ujianRequest) {
        // Set pengaturan maps
        ujian.setPengaturan(ujianRequest.getPengaturan() != null ? ujianRequest.getPengaturan() : new HashMap<>());
        ujian.setCatSettings(ujianRequest.getCatSettings() != null ? ujianRequest.getCatSettings() : new HashMap<>());

        // Set CAT and timing configurations
        if (ujianRequest.getIsCatEnabled() != null)
            ujian.setIsCatEnabled(ujianRequest.getIsCatEnabled());
        if (ujianRequest.getAllowLateStart() != null)
            ujian.setAllowLateStart(ujianRequest.getAllowLateStart());
        if (ujianRequest.getMaxLateStartMinutes() != null)
            ujian.setMaxLateStartMinutes(ujianRequest.getMaxLateStartMinutes());
        if (ujianRequest.getShowTimerToParticipants() != null)
            ujian.setShowTimerToParticipants(ujianRequest.getShowTimerToParticipants());
        if (ujianRequest.getPreventCheating() != null)
            ujian.setPreventCheating(ujianRequest.getPreventCheating());
        if (ujianRequest.getIsFlexibleTiming() != null)
            ujian.setIsFlexibleTiming(ujianRequest.getIsFlexibleTiming());
        if (ujianRequest.getBatasAkhirMulai() != null)
            ujian.setBatasAkhirMulai(ujianRequest.getBatasAkhirMulai());
        if (ujianRequest.getAutoEndAfterDuration() != null)
            ujian.setAutoEndAfterDuration(ujianRequest.getAutoEndAfterDuration());
        if (ujianRequest.getToleransiKeterlambatanMenit() != null)
            ujian.setToleransiKeterlambatanMenit(ujianRequest.getToleransiKeterlambatanMenit());
        if (ujianRequest.getStrategiPemilihanSoal() != null)
            ujian.setStrategiPemilihanSoal(ujianRequest.getStrategiPemilihanSoal());
        if (ujianRequest.getMinPassingScore() != null)
            ujian.setMinPassingScore(ujianRequest.getMinPassingScore());
        if (ujianRequest.getAllowReview() != null)
            ujian.setAllowReview(ujianRequest.getAllowReview());
        if (ujianRequest.getAllowBacktrack() != null)
            ujian.setAllowBacktrack(ujianRequest.getAllowBacktrack());
        if (ujianRequest.getMaxAttempts() != null)
            ujian.setMaxAttempts(ujianRequest.getMaxAttempts());
    }

    private void calculateAutomaticEndTime(Ujian ujian) {
        if (ujian.getWaktuSelesaiOtomatis() == null &&
                ujian.getWaktuMulaiDijadwalkan() != null &&
                ujian.getDurasiMenit() != null) {
            ujian.setWaktuSelesaiOtomatis(
                    ujian.getWaktuMulaiDijadwalkan().plusSeconds(ujian.getDurasiMenit() * 60L));
        }
    }

    private void validateUjianRequest(UjianRequest request) {
        if (request.getNamaUjian() == null || request.getNamaUjian().trim().isEmpty()) {
            throw new IllegalArgumentException("Nama ujian wajib diisi");
        }

        if (request.getIdSchool() == null || request.getIdSchool().trim().isEmpty()) {
            throw new IllegalArgumentException("ID sekolah wajib diisi");
        }

        if (request.getIdCreatedBy() == null || request.getIdCreatedBy().trim().isEmpty()) {
            throw new IllegalArgumentException("ID pembuat ujian wajib diisi");
        }

        if (request.getDurasiMenit() != null && request.getDurasiMenit() <= 0) {
            throw new IllegalArgumentException("Durasi ujian harus lebih dari 0 menit");
        }

        if (request.getMinPassingScore() != null
                && (request.getMinPassingScore() < 0 || request.getMinPassingScore() > 100)) {
            throw new IllegalArgumentException("Passing score harus antara 0-100");
        }

        if (request.getMaxAttempts() != null && request.getMaxAttempts() <= 0) {
            throw new IllegalArgumentException("Maksimal percobaan harus lebih dari 0");
        }

        if (request.getMaxLateStartMinutes() != null && request.getMaxLateStartMinutes() < 0) {
            throw new IllegalArgumentException("Maksimal keterlambatan tidak boleh negatif");
        }

        if (request.getToleransiKeterlambatanMenit() != null && request.getToleransiKeterlambatanMenit() < 0) {
            throw new IllegalArgumentException("Toleransi keterlambatan tidak boleh negatif");
        }
    }

    public DefaultResponse<Ujian> getUjianById(String ujianId) throws IOException {
        Ujian ujianResponse = ujianRepository.findById(ujianId);
        if (ujianResponse == null) {
            throw new ResourceNotFoundException("Ujian", "id", ujianId);
        }

        // Enrich ujian with relational data
        enrichUjianWithRelationalData(ujianResponse);

        return new DefaultResponse<>(ujianResponse, 1, "Successfully get data");
    }

    public void deleteUjianById(String ujianId) throws IOException {
        Ujian ujianResponse = ujianRepository.findById(ujianId);
        if (ujianResponse == null) {
            throw new ResourceNotFoundException("Ujian", "id", ujianId);
        }

        // Don't allow deletion if ujian is active
        if (ujianResponse.isAktif() || ujianResponse.getIsLive()) {
            throw new BadRequestException("Tidak dapat menghapus ujian yang sedang aktif");
        }

        ujianRepository.deleteById(ujianId);
    }

    // Ujian state management methods
    public Ujian activateUjian(String ujianId) throws IOException {
        Ujian ujian = ujianRepository.findById(ujianId);
        if (ujian == null) {
            throw new ResourceNotFoundException("Ujian", "id", ujianId);
        }

        if (!ujian.isDraft()) {
            throw new BadRequestException("Hanya ujian dengan status DRAFT yang dapat diaktifkan");
        }

        // Validasi wajib
        if (ujian.getIdBankSoalList() == null || ujian.getIdBankSoalList().isEmpty()) {
            throw new BadRequestException("Ujian harus memiliki bank soal sebelum dapat diaktifkan");
        }

        // **Tambahkan validasi dan pengambilan ulang bankSoalList jika null/empty**
        if (ujian.getBankSoalList() == null || ujian.getBankSoalList().isEmpty()) {
            // Ambil ulang dari repository jika perlu
            List<BankSoal> fullBankSoalList = bankSoalRepository.findAllById(ujian.getIdBankSoalList());
            List<BankSoalUjian> simpleBankSoalList = fullBankSoalList.stream()
                    .map(BankSoalUjian::new)
                    .collect(Collectors.toList());
            ujian.setBankSoalList(simpleBankSoalList);
        }

        if (ujian.getWaktuMulaiDijadwalkan() == null) {
            throw new BadRequestException("Ujian harus memiliki waktu mulai sebelum dapat diaktifkan");
        }

        ujian.activateUjian();
        return ujianRepository.save(ujian);
    }

    public Ujian startUjian(String ujianId) throws IOException {
        Ujian ujian = ujianRepository.findById(ujianId);
        if (ujian == null) {
            throw new ResourceNotFoundException("Ujian", "id", ujianId);
        }

        if (!ujian.isAktif()) {
            throw new BadRequestException("Hanya ujian dengan status AKTIF yang dapat dimulai");
        }

        if (ujian.getIsLive()) {
            throw new BadRequestException("Ujian sudah dalam keadaan live");
        }

        ujian.startUjian();
        return ujianRepository.save(ujian);
    }

    public Ujian endUjian(String ujianId) throws IOException {
        Ujian ujian = ujianRepository.findById(ujianId);
        if (ujian == null) {
            throw new ResourceNotFoundException("Ujian", "id", ujianId);
        }

        if (!ujian.isAktif() && !ujian.getIsLive()) {
            throw new BadRequestException("Ujian tidak dalam keadaan aktif atau live");
        }

        ujian.endUjian();
        Ujian savedUjian = ujianRepository.save(ujian);

        // Generate final analysis when exam ends
        try {
            logger.info("Generating final analysis for ended ujian: {}", ujianId);
            generateFinalAnalysisForUjian(savedUjian);
        } catch (Exception e) {
            logger.warn("Failed to generate final analysis for ujian {}: {}", ujianId, e.getMessage());
        }

        return savedUjian;
    }

    public Ujian cancelUjian(String ujianId) throws IOException {
        Ujian ujian = ujianRepository.findById(ujianId);
        if (ujian == null) {
            throw new ResourceNotFoundException("Ujian", "id", ujianId);
        }

        if (ujian.isSelesai()) {
            throw new BadRequestException("Ujian yang sudah selesai tidak dapat dibatalkan");
        }

        ujian.cancelUjian();
        return ujianRepository.save(ujian);
    }

    // Bank Soal management methods
    public Ujian addBankSoalToUjian(String ujianId, String bankSoalId) throws IOException {
        Ujian ujian = ujianRepository.findById(ujianId);
        if (ujian == null) {
            throw new ResourceNotFoundException("Ujian", "id", ujianId);
        }

        if (ujian.isAktif() || ujian.isSelesai()) {
            throw new BadRequestException("Tidak dapat menambah bank soal ke ujian yang sudah aktif atau selesai");
        }

        BankSoal fullBankSoal = bankSoalRepository.findById(bankSoalId);
        if (fullBankSoal == null || !fullBankSoal.isValid()) {
            throw new ResourceNotFoundException("Bank Soal", "id", bankSoalId);
        }

        if (ujian.hasBankSoal(bankSoalId)) {
            throw new BadRequestException("Bank soal sudah ada dalam ujian");
        }

        // Convert to BankSoalUjian and add to ujian
        BankSoalUjian bankSoalUjian = new BankSoalUjian(fullBankSoal);
        ujian.addBankSoal(bankSoalUjian);

        // Recalculate totals
        ujian.setJumlahSoal(ujian.getBankSoalList().size());
        ujian.setTotalBobot(ujian.getBankSoalList().stream()
                .mapToDouble(s -> Double.parseDouble(s.getBobot()))
                .sum());

        return ujianRepository.save(ujian);
    }

    public Ujian removeBankSoalFromUjian(String ujianId, String bankSoalId) throws IOException {
        Ujian ujian = ujianRepository.findById(ujianId);
        if (ujian == null) {
            throw new ResourceNotFoundException("Ujian", "id", ujianId);
        }

        if (ujian.isAktif() || ujian.isSelesai()) {
            throw new BadRequestException("Tidak dapat menghapus bank soal dari ujian yang sudah aktif atau selesai");
        }

        if (!ujian.hasBankSoal(bankSoalId)) {
            throw new BadRequestException("Bank soal tidak ada dalam ujian");
        }

        ujian.removeBankSoal(bankSoalId);

        // Recalculate totals
        ujian.setJumlahSoal(ujian.getBankSoalList().size());
        ujian.setTotalBobot(ujian.getBankSoalList().stream()
                .mapToDouble(s -> Double.parseDouble(s.getBobot()))
                .sum());

        return ujianRepository.save(ujian);
    }

    // Statistics and reporting methods
    public Map<String, Object> getUjianStatistics(String schoolId) throws IOException {
        Map<String, Object> stats = new HashMap<>();

        List<Ujian> allUjian = schoolId.equals("*") ? ujianRepository.findAll(1000)
                : ujianRepository.findUjianBySekolah(schoolId, 1000);

        long draftCount = allUjian.stream().filter(Ujian::isDraft).count();
        long aktifCount = allUjian.stream().filter(Ujian::isAktif).count();
        long selesaiCount = allUjian.stream().filter(Ujian::isSelesai).count();
        long liveCount = allUjian.stream().filter(u -> u.getIsLive() != null && u.getIsLive()).count();

        stats.put("totalUjian", allUjian.size());
        stats.put("draftCount", draftCount);
        stats.put("aktifCount", aktifCount);
        stats.put("selesaiCount", selesaiCount);
        stats.put("liveCount", liveCount);

        return stats;
    }

    // ==================== ANALYSIS GENERATION HELPERS ====================

    /**
     * Generate final comprehensive analysis when exam ends
     */
    private void generateFinalAnalysisForUjian(Ujian ujian) {
        try {
            logger.info("Starting final analysis generation for ended ujian: {}", ujian.getIdUjian());

            // Create request for comprehensive analysis generation
            com.doyatama.university.payload.UjianAnalysisRequest.GenerateAnalysisRequest request = new com.doyatama.university.payload.UjianAnalysisRequest.GenerateAnalysisRequest();
            request.setIdUjian(ujian.getIdUjian());
            request.setIdSchool(ujian.getSchool() != null ? ujian.getSchool().getIdSchool() : "");
            request.setAnalysisType("COMPREHENSIVE");
            request.setIncludeDescriptiveStats(true);
            request.setIncludeItemAnalysis(true);
            request.setIncludeDifficultyAnalysis(true);
            request.setIncludeTimeAnalysis(true);
            request.setIncludeCheatingAnalysis(true);
            request.setIncludeComparativeAnalysis(true);
            request.setIncludeLearningAnalytics(true);
            request.setIncludeRecommendations(true);
            request.setDetailLevel("DETAILED"); // Add configuration - allow one final analysis when exam ends
            Map<String, Object> configuration = new HashMap<>();
            configuration.put("allowDuplicate", false);
            configuration.put("finalAnalysis", true);
            configuration.put("autoGenerated", true);
            request.setAnalysisConfiguration(configuration);

            // Generate analysis
            UjianAnalysis generatedAnalysis = ujianAnalysisService.generateAnalysis(request);
            logger.info("Final analysis generated successfully for ujian: {} with analysis ID: {}",
                    ujian.getIdUjian(), generatedAnalysis.getIdAnalysis());

        } catch (Exception e) {
            logger.error("Failed to generate final analysis for ujian {}: {}", ujian.getIdUjian(), e.getMessage(), e);
        }
    }

    /**
     * Enrich ujian with participant count and other metadata
     */
    private void enrichUjianWithParticipantData(Ujian ujian) {
        try {
            // Add null check for hasilUjianService
            if (hasilUjianService != null) {
                // Count participants from hasil_ujian table
                long participantCount = hasilUjianService.countParticipantsByUjian(ujian.getIdUjian());
                ujian.setJumlahPeserta((int) participantCount);
            } else {
                logger.warn("HasilUjianService is null, cannot get participant count for ujian {}", ujian.getIdUjian());
                ujian.setJumlahPeserta(0);
            }

            // You can add more enrichment here like active sessions, etc.

        } catch (Exception e) {
            logger.warn("Failed to get participant count for ujian {}: {}", ujian.getIdUjian(), e.getMessage());
            ujian.setJumlahPeserta(0);
        }
    }

    /**
     * Enrich ujian with complete relational data (kelas, mapel, semester, etc.)
     */
    public void enrichUjianWithRelationalData(Ujian ujian) {
        try {
            // Enrich tahun ajaran
            if (ujian.getTahunAjaran() != null && ujian.getTahunAjaran().getIdTahun() != null) {
                TahunAjaran tahunAjaran = tahunAjaranRepository.findById(ujian.getTahunAjaran().getIdTahun());
                if (tahunAjaran != null) {
                    ujian.setTahunAjaran(tahunAjaran);
                }
            }

            // Enrich kelas
            if (ujian.getKelas() != null && ujian.getKelas().getIdKelas() != null) {
                Kelas kelas = kelasRepository.findById(ujian.getKelas().getIdKelas());
                if (kelas != null) {
                    ujian.setKelas(kelas);
                }
            }

            // Enrich semester
            if (ujian.getSemester() != null && ujian.getSemester().getIdSemester() != null) {
                Semester semester = semesterRepository.findById(ujian.getSemester().getIdSemester());
                if (semester != null) {
                    ujian.setSemester(semester);
                }
            }

            // Enrich mapel
            if (ujian.getMapel() != null && ujian.getMapel().getIdMapel() != null) {
                Mapel mapel = mapelRepository.findById(ujian.getMapel().getIdMapel());
                if (mapel != null) {
                    ujian.setMapel(mapel);
                }
            }

            // Enrich konsentrasi keahlian sekolah
            if (ujian.getKonsentrasiKeahlianSekolah() != null
                    && ujian.getKonsentrasiKeahlianSekolah().getIdKonsentrasiSekolah() != null) {
                KonsentrasiKeahlianSekolah konsentrasi = konsentrasiKeahlianSekolahRepository
                        .findById(ujian.getKonsentrasiKeahlianSekolah().getIdKonsentrasiSekolah());
                if (konsentrasi != null) {
                    ujian.setKonsentrasiKeahlianSekolah(konsentrasi);
                }
            }

            // Enrich school
            if (ujian.getSchool() != null && ujian.getSchool().getIdSchool() != null) {
                School school = schoolRepository.findById(ujian.getSchool().getIdSchool());
                if (school != null) {
                    ujian.setSchool(school);
                }
            }

            // Enrich created by
            if (ujian.getCreatedBy() != null && ujian.getCreatedBy().getId() != null) {
                User createdBy = userRepository.findById(ujian.getCreatedBy().getId());
                if (createdBy != null) {
                    ujian.setCreatedBy(createdBy);
                }
            }

        } catch (Exception e) {
            logger.warn("Failed to enrich ujian {} with relational data: {}", ujian.getIdUjian(), e.getMessage());
        }
    }
}