package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.Acp;
import com.doyatama.university.model.Atp;
import com.doyatama.university.model.BankSoal;
import com.doyatama.university.model.Elemen;
import com.doyatama.university.model.Kelas;
import com.doyatama.university.model.Taksonomi;
import com.doyatama.university.model.KonsentrasiKeahlianSekolah;
import com.doyatama.university.model.Mapel;
import com.doyatama.university.model.School;
import com.doyatama.university.model.Semester;
import com.doyatama.university.model.SoalUjian;
import com.doyatama.university.model.TahunAjaran;
import com.doyatama.university.payload.BankSoalRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.AcpRepository;
import com.doyatama.university.repository.AtpRepository;
import com.doyatama.university.repository.BankSoalRepository;
import com.doyatama.university.repository.ElemenRepository;
import com.doyatama.university.repository.KelasRepository;
import com.doyatama.university.repository.TaksonomiRepository;
import com.doyatama.university.repository.KonsentrasiKeahlianSekolahRepository;
import com.doyatama.university.repository.MapelRepository;
import com.doyatama.university.repository.SchoolRepository;
import com.doyatama.university.repository.SemesterRepository;
import com.doyatama.university.repository.SoalUjianRepository;
import com.doyatama.university.repository.TahunAjaranRepository;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BankSoalService {

    private BankSoalRepository bankSoalRepository = new BankSoalRepository();
    private SoalUjianRepository soalUjianRepository = new SoalUjianRepository();
    private AtpRepository atpRepository = new AtpRepository();
    private TahunAjaranRepository tahunAjaranRepository = new TahunAjaranRepository();
    private KelasRepository kelasRepository = new KelasRepository();
    private SemesterRepository semesterRepository = new SemesterRepository();
    private MapelRepository mapelRepository = new MapelRepository();
    private ElemenRepository elemenRepository = new ElemenRepository();
    private AcpRepository acpRepository = new AcpRepository();
    private TaksonomiRepository taksonomiRepository = new TaksonomiRepository();
    private SchoolRepository schoolRepository = new SchoolRepository();
    private KonsentrasiKeahlianSekolahRepository konsentrasiKeahlianSekolahRepository = new KonsentrasiKeahlianSekolahRepository();

    public PagedResponse<BankSoal> getAllBankSoal(int page, int size, String userID, String schoolID)
            throws IOException {
        validatePageNumberAndSize(page, size);

        List<BankSoal> bankSoalResponse;

        if (schoolID.equalsIgnoreCase("*")) {
            bankSoalResponse = bankSoalRepository.findAll(size);
        } else {
            bankSoalResponse = bankSoalRepository.findBankSoalBySekolah(schoolID, size);
        }

        return new PagedResponse<>(bankSoalResponse, bankSoalResponse.size(), "Successfully get data", 200);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    public BankSoal createBankSoal(BankSoalRequest bankSoalRequest) throws IOException {
        // Validate and generate ID
        if (bankSoalRequest.getIdBankSoal() == null) {
            bankSoalRequest.setIdBankSoal(UUID.randomUUID().toString());
        }

        if (bankSoalRepository.existsById(bankSoalRequest.getIdBankSoal())) {
            throw new IllegalArgumentException("Bank Soal sudah ada");
        }

        // Get related entities
        TahunAjaran tahunAjaranResponse = tahunAjaranRepository.findById(bankSoalRequest.getIdTahun());
        Kelas kelasResponse = kelasRepository.findById(bankSoalRequest.getIdKelas());
        Semester semesterResponse = semesterRepository.findById(bankSoalRequest.getIdSemester());
        Mapel mapelResponse = mapelRepository.findById(bankSoalRequest.getIdMapel());
        Elemen elemenResponse = elemenRepository.findById(bankSoalRequest.getIdElemen());
        Acp acpResponse = acpRepository.findById(bankSoalRequest.getIdAcp());
        Atp atpResponse = atpRepository.findById(bankSoalRequest.getIdAtp());
        SoalUjian soalUjianResponse = soalUjianRepository.findById(bankSoalRequest.getIdSoalUjian());
        Taksonomi taksonomiResponse = taksonomiRepository.findById(bankSoalRequest.getIdTaksonomi());
        KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolahResponse = konsentrasiKeahlianSekolahRepository
                .findById(bankSoalRequest.getIdKonsentrasiSekolah());
        School schoolResponse = schoolRepository.findById(bankSoalRequest.getIdSchool());

        // Validate question type
        if (bankSoalRequest.getJenisSoal() == null) {
            throw new IllegalArgumentException("Jenis soal wajib diisi");
        }

        // Create question entity
        BankSoal bankSoal = new BankSoal();
        bankSoal.setIdBankSoal(bankSoalRequest.getIdBankSoal());
        bankSoal.setIdSoalUjian(bankSoalRequest.getIdSoalUjian());
        bankSoal.setNamaUjian(bankSoalRequest.getNamaUjian());
        bankSoal.setPertanyaan(bankSoalRequest.getPertanyaan());
        bankSoal.setBobot(bankSoalRequest.getBobot());
        bankSoal.setJenisSoal(bankSoalRequest.getJenisSoal());

        bankSoal.setCreatedAt(bankSoalRequest.getCreatedAt() != null ? bankSoalRequest.getCreatedAt() : Instant.now());
        bankSoal.setSoalUjian(soalUjianResponse);
        bankSoal.setTaksonomi(taksonomiResponse);
        bankSoal.setKonsentrasiKeahlianSekolah(konsentrasiKeahlianSekolahResponse);
        bankSoal.setTahunAjaran(tahunAjaranResponse);
        bankSoal.setSemester(semesterResponse);
        bankSoal.setKelas(kelasResponse);
        bankSoal.setMapel(mapelResponse);
        bankSoal.setElemen(elemenResponse);
        bankSoal.setAcp(acpResponse);
        bankSoal.setAtp(atpResponse);
        bankSoal.setSchool(schoolResponse);

        // Handle different question types
        switch (bankSoalRequest.getJenisSoal().toUpperCase()) {
            case "PG":
                validatePertanyaanPG(bankSoalRequest);
                bankSoal.setOpsi(bankSoalRequest.getOpsi());
                bankSoal.setJawabanBenar(bankSoalRequest.getJawabanBenar());
                break;

            case "MULTI":
                validatePertanyaanMulti(bankSoalRequest);
                bankSoal.setOpsi(bankSoalRequest.getOpsi());
                bankSoal.setJawabanBenar(bankSoalRequest.getJawabanBenar());
                break;

            case "COCOK":
                validatePertanyaanCocok(bankSoalRequest);
                bankSoal.setPasangan(bankSoalRequest.getPasangan());
                bankSoal.setJawabanBenar(bankSoalRequest.getJawabanBenar());
                break;

            case "ISIAN":
                validatePertanyaanIsian(bankSoalRequest);
                bankSoal.setJawabanBenar(bankSoalRequest.getJawabanBenar());
                bankSoal.setToleransiTypo(bankSoalRequest.getToleransiTypo());
                break;

            default:
                throw new IllegalArgumentException("Jenis bankSoal tidak dikenali: " + bankSoalRequest.getJenisSoal());
        }

        return bankSoalRepository.save(bankSoal);
    }

    // Validation methods
    private void validatePertanyaanPG(BankSoalRequest request) {
        if (request.getOpsi() == null || request.getOpsi().isEmpty()) {
            throw new IllegalArgumentException("Opsi wajib diisi untuk PG");
        }
        if (request.getJawabanBenar() == null || request.getJawabanBenar().size() != 1) {
            throw new IllegalArgumentException("Harus ada tepat satu jawaban benar untuk PG");
        }
        if (!request.getOpsi().containsKey(request.getJawabanBenar().get(0))) {
            throw new IllegalArgumentException("Jawaban benar harus ada dalam opsi");
        }
    }

    private void validatePertanyaanMulti(BankSoalRequest request) {
        if (request.getOpsi() == null || request.getOpsi().isEmpty()) {
            throw new IllegalArgumentException("Opsi wajib diisi untuk MULTI");
        }
        if (request.getJawabanBenar() == null || request.getJawabanBenar().isEmpty()) {
            throw new IllegalArgumentException("Jawaban benar wajib diisi untuk MULTI");
        }
        for (String jawaban : request.getJawabanBenar()) {
            if (!request.getOpsi().containsKey(jawaban)) {
                throw new IllegalArgumentException("Jawaban benar '" + jawaban + "' tidak ada dalam opsi");
            }
        }
    }

    private void validatePertanyaanCocok(BankSoalRequest request) {
        if (request.getPasangan() == null || request.getPasangan().isEmpty()) {
            throw new IllegalArgumentException("Pasangan wajib diisi untuk COCOK");
        }
        if (request.getJawabanBenar() == null || request.getJawabanBenar().isEmpty()) {
            throw new IllegalArgumentException("Jawaban benar wajib diisi untuk COCOK");
        }

        // Kumpulkan semua nilai dari sisi kiri dan kanan
        Set<String> nilaiKiri = new HashSet<>();
        Set<String> nilaiKanan = new HashSet<>();

        for (Map.Entry<String, String> entry : request.getPasangan().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (key.contains("_kiri")) {
                nilaiKiri.add(value);
            } else if (key.contains("_kanan")) {
                nilaiKanan.add(value);
            }
        }

        if (nilaiKiri.isEmpty() || nilaiKanan.isEmpty()) {
            throw new IllegalArgumentException("Pasangan harus memiliki nilai untuk sisi kiri dan kanan");
        }

        // Validasi format jawaban - format baru "a=f", "b=d", dll
        for (String jawaban : request.getJawabanBenar()) {
            if (!jawaban.contains("=")) {
                throw new IllegalArgumentException("Format jawaban tidak valid: " + jawaban);
            }

            String[] parts = jawaban.split("=");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Format jawaban tidak valid: " + jawaban);
            }

            // Validasi ketat: nilai harus persis ada di kumpulan nilai kiri dan kanan
            if (!nilaiKiri.contains(parts[0])) {
                throw new IllegalArgumentException("Nilai kiri '" + parts[0] + "' tidak ada dalam pasangan");
            }

            if (!nilaiKanan.contains(parts[1])) {
                throw new IllegalArgumentException("Nilai kanan '" + parts[1] + "' tidak ada dalam pasangan");
            }
        }
    }

    private void validatePertanyaanIsian(BankSoalRequest request) {
        if (request.getJawabanBenar() == null || request.getJawabanBenar().isEmpty()) {
            throw new IllegalArgumentException("Jawaban benar wajib diisi untuk ISIAN");
        }
        if (request.getToleransiTypo() == null) {
            request.setToleransiTypo(String.valueOf(0)); // Default no typo tolerance
        }
    }

    public DefaultResponse<BankSoal> getBankSoalById(String bankSoalId) throws IOException {
        BankSoal bankSoalResponse = bankSoalRepository.findById(bankSoalId);
        return new DefaultResponse<>(bankSoalResponse.isValid() ? bankSoalResponse : null,
                bankSoalResponse.isValid() ? 1 : 0,
                "Successfully get data");
    }

    public BankSoal updateBankSoal(String bankSoalId, BankSoalRequest bankSoalRequest) throws IOException {
        // Check if bank soal exists
        BankSoal existingBankSoal = bankSoalRepository.findById(bankSoalId);
        if (existingBankSoal == null || !existingBankSoal.isValid()) {
            throw new ResourceNotFoundException("Bank Soal", "id", bankSoalId);
        }

        // Get related entities
        TahunAjaran tahunAjaranResponse = tahunAjaranRepository.findById(bankSoalRequest.getIdTahun());
        Kelas kelasResponse = kelasRepository.findById(bankSoalRequest.getIdKelas());
        Semester semesterResponse = semesterRepository.findById(bankSoalRequest.getIdSemester());
        Mapel mapelResponse = mapelRepository.findById(bankSoalRequest.getIdMapel());
        Elemen elemenResponse = elemenRepository.findById(bankSoalRequest.getIdElemen());
        Acp acpResponse = acpRepository.findById(bankSoalRequest.getIdAcp());
        Atp atpResponse = atpRepository.findById(bankSoalRequest.getIdAtp());
        SoalUjian soalUjianResponse = soalUjianRepository.findById(bankSoalRequest.getIdSoalUjian());
        Taksonomi taksonomiResponse = taksonomiRepository.findById(bankSoalRequest.getIdTaksonomi());
        KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolahResponse = konsentrasiKeahlianSekolahRepository
                .findById(bankSoalRequest.getIdKonsentrasiSekolah());
        School schoolResponse = schoolRepository.findById(bankSoalRequest.getIdSchool());

        // Build updated BankSoal object
        BankSoal updatedBankSoal = new BankSoal();
        updatedBankSoal.setIdBankSoal(bankSoalId); // Keep the same ID
        updatedBankSoal.setIdSoalUjian(bankSoalRequest.getIdSoalUjian());
        updatedBankSoal.setNamaUjian(bankSoalRequest.getNamaUjian());
        updatedBankSoal.setPertanyaan(bankSoalRequest.getPertanyaan());
        updatedBankSoal.setBobot(bankSoalRequest.getBobot());
        updatedBankSoal.setJenisSoal(bankSoalRequest.getJenisSoal());

        updatedBankSoal.setCreatedAt(existingBankSoal.getCreatedAt()); // Keep original creation date
        updatedBankSoal.setSoalUjian(soalUjianResponse);
        updatedBankSoal.setTaksonomi(taksonomiResponse);
        updatedBankSoal.setKonsentrasiKeahlianSekolah(konsentrasiKeahlianSekolahResponse);
        updatedBankSoal.setTahunAjaran(tahunAjaranResponse);
        updatedBankSoal.setSemester(semesterResponse);
        updatedBankSoal.setKelas(kelasResponse);
        updatedBankSoal.setMapel(mapelResponse);
        updatedBankSoal.setElemen(elemenResponse);
        updatedBankSoal.setAcp(acpResponse);
        updatedBankSoal.setAtp(atpResponse);
        updatedBankSoal.setSchool(schoolResponse);

        // Handle different question types
        switch (bankSoalRequest.getJenisSoal().toUpperCase()) {
            case "PG":
                validatePertanyaanPG(bankSoalRequest);
                updatedBankSoal.setOpsi(bankSoalRequest.getOpsi());
                updatedBankSoal.setJawabanBenar(bankSoalRequest.getJawabanBenar());
                break;

            case "MULTI":
                validatePertanyaanMulti(bankSoalRequest);
                updatedBankSoal.setOpsi(bankSoalRequest.getOpsi());
                updatedBankSoal.setJawabanBenar(bankSoalRequest.getJawabanBenar());
                break;

            case "COCOK":
                validatePertanyaanCocok(bankSoalRequest);
                updatedBankSoal.setPasangan(bankSoalRequest.getPasangan());
                updatedBankSoal.setJawabanBenar(bankSoalRequest.getJawabanBenar());
                break;

            case "ISIAN":
                validatePertanyaanIsian(bankSoalRequest);
                updatedBankSoal.setJawabanBenar(bankSoalRequest.getJawabanBenar());
                updatedBankSoal.setToleransiTypo(bankSoalRequest.getToleransiTypo());
                break;

            default:
                throw new IllegalArgumentException("Jenis bankSoal tidak dikenali: " + bankSoalRequest.getJenisSoal());
        }

        // Update bank soal using proper update method
        BankSoal savedBankSoal = bankSoalRepository.update(bankSoalId, updatedBankSoal);

        // Note: BankSoal updates do NOT cascade to SoalUjian as per business
        // requirements
        // This maintains data integrity by keeping the original SoalUjian as the source
        // of truth

        return savedBankSoal;
    }

    public void deleteBankSoalById(String bankSoalId) throws IOException {
        BankSoal bankSoalResponse = bankSoalRepository.findById(bankSoalId);
        if (bankSoalResponse.isValid()) {
            bankSoalRepository.deleteById(bankSoalId);
        } else {
            throw new ResourceNotFoundException("Bank Soal", "id", bankSoalId);
        }
    }

}
