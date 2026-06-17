package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.SoalUjian;
import com.doyatama.university.model.Taksonomi;
import com.doyatama.university.model.User;
import com.doyatama.university.model.KonsentrasiKeahlianSekolah;
import com.doyatama.university.model.School;
import com.doyatama.university.payload.SoalUjianRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.SoalUjianRepository;
import com.doyatama.university.repository.TaksonomiRepository;
import com.doyatama.university.repository.UserRepository;
import com.doyatama.university.repository.KonsentrasiKeahlianSekolahRepository;
import com.doyatama.university.repository.SchoolRepository;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.Set;

public class SoalUjianService {
    private SoalUjianRepository soalUjianRepository = new SoalUjianRepository();
    private UserRepository userRepository = new UserRepository();
    private TaksonomiRepository taksonomiRepository = new TaksonomiRepository();
    private SchoolRepository schoolRepository = new SchoolRepository();
    private KonsentrasiKeahlianSekolahRepository konsentrasiKeahlianSekolahRepository = new KonsentrasiKeahlianSekolahRepository();

    public PagedResponse<SoalUjian> getAllSoalUjian(int page, int size, String userID, String schoolID)
            throws IOException {
        validatePageNumberAndSize(page, size);

        List<SoalUjian> soalUjianResponse;

        if (schoolID.equalsIgnoreCase("*")) {
            soalUjianResponse = soalUjianRepository.findAll(size);
        } else {
            soalUjianResponse = soalUjianRepository.findSoalUjianBySekolah(schoolID, size);
        }

        return new PagedResponse<>(soalUjianResponse, soalUjianResponse.size(), "Successfully get data", 200);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    public SoalUjian createSoalUjian(SoalUjianRequest soalUjianRequest) throws IOException {
        // Validate and generate ID
        if (soalUjianRequest.getIdSoalUjian() == null) {
            soalUjianRequest.setIdSoalUjian(UUID.randomUUID().toString());
        }

        if (soalUjianRepository.existsById(soalUjianRequest.getIdSoalUjian())) {
            throw new IllegalArgumentException("Soal ujian sudah ada");
        }

        // Get related entities
        User userResponse = userRepository.findById(soalUjianRequest.getIdUser());
        Taksonomi taksonomiResponse = taksonomiRepository.findById(soalUjianRequest.getIdTaksonomi());
        KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolahResponse = konsentrasiKeahlianSekolahRepository
                .findById(soalUjianRequest.getIdKonsentrasiSekolah());
        School schoolResponse = schoolRepository.findById(soalUjianRequest.getIdSchool());

        // Validate question type
        if (soalUjianRequest.getJenisSoal() == null) {
            throw new IllegalArgumentException("Jenis soal wajib diisi");
        }

        // Create question entity
        SoalUjian soal = new SoalUjian();
        soal.setIdSoalUjian(soalUjianRequest.getIdSoalUjian());
        soal.setNamaUjian(soalUjianRequest.getNamaUjian());
        soal.setPertanyaan(soalUjianRequest.getPertanyaan());
        soal.setBobot(soalUjianRequest.getBobot());
        soal.setJenisSoal(soalUjianRequest.getJenisSoal());
        soal.setCreatedAt(soalUjianRequest.getCreatedAt() != null ? soalUjianRequest.getCreatedAt() : Instant.now());
        soal.setUser(userResponse);
        soal.setTaksonomi(taksonomiResponse);
        soal.setKonsentrasiKeahlianSekolah(konsentrasiKeahlianSekolahResponse);
        soal.setSchool(schoolResponse);

        // Handle different question types
        switch (soalUjianRequest.getJenisSoal().toUpperCase()) {
            case "PG":
                validatePertanyaanPG(soalUjianRequest);
                soal.setOpsi(soalUjianRequest.getOpsi());
                soal.setJawabanBenar(soalUjianRequest.getJawabanBenar());
                break;

            case "MULTI":
                validatePertanyaanMulti(soalUjianRequest);
                soal.setOpsi(soalUjianRequest.getOpsi());
                soal.setJawabanBenar(soalUjianRequest.getJawabanBenar());
                break;

            case "COCOK":
                validatePertanyaanCocok(soalUjianRequest);
                soal.setPasangan(soalUjianRequest.getPasangan());
                soal.setJawabanBenar(soalUjianRequest.getJawabanBenar());
                break;

            case "ISIAN":
                validatePertanyaanIsian(soalUjianRequest);
                soal.setJawabanBenar(soalUjianRequest.getJawabanBenar());
                soal.setToleransiTypo(soalUjianRequest.getToleransiTypo());
                break;

            default:
                throw new IllegalArgumentException("Jenis soal tidak dikenali: " + soalUjianRequest.getJenisSoal());
        }

        // Create SoalUjian without cascade (no BankSoal creation)
        return soalUjianRepository.save(soal);
    }

    // Validation methods
    private void validatePertanyaanPG(SoalUjianRequest request) {
        System.out.println("Validating PG question...");
        System.out.println("Opsi received: " + request.getOpsi());
        System.out.println("JawabanBenar received: " + request.getJawabanBenar());
        
        if (request.getOpsi() == null || request.getOpsi().isEmpty()) {
            throw new IllegalArgumentException("Opsi wajib diisi untuk soal Pilihan Ganda (PG)");
        }
        
        if (request.getOpsi().size() < 2) {
            throw new IllegalArgumentException("Soal PG harus memiliki minimal 2 opsi jawaban");
        }
        
        if (request.getJawabanBenar() == null || request.getJawabanBenar().isEmpty()) {
            throw new IllegalArgumentException("Jawaban benar wajib diisi untuk PG");
        }
        
        if (request.getJawabanBenar().size() != 1) {
            throw new IllegalArgumentException("Soal PG harus memiliki tepat satu jawaban benar. Ditemukan: " + request.getJawabanBenar().size() + " jawaban");
        }
        
        String jawabanBenar = request.getJawabanBenar().get(0);
        if (jawabanBenar == null || jawabanBenar.trim().isEmpty()) {
            throw new IllegalArgumentException("Jawaban benar tidak boleh kosong");
        }
        
        if (!request.getOpsi().containsKey(jawabanBenar)) {
            throw new IllegalArgumentException("Jawaban benar '" + jawabanBenar + "' harus ada dalam opsi. Opsi yang tersedia: " + request.getOpsi().keySet());
        }
        
        // Validate that all option keys are not empty
        for (Map.Entry<String, String> entry : request.getOpsi().entrySet()) {
            if (entry.getKey() == null || entry.getKey().trim().isEmpty()) {
                throw new IllegalArgumentException("Kunci opsi tidak boleh kosong");
            }
            if (entry.getValue() == null || entry.getValue().trim().isEmpty()) {
                throw new IllegalArgumentException("Nilai opsi '" + entry.getKey() + "' tidak boleh kosong");
            }
        }
        
        System.out.println("PG validation passed successfully");
    }

    private void validatePertanyaanMulti(SoalUjianRequest request) {
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

    private void validatePertanyaanCocok(SoalUjianRequest request) {
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

    private void validatePertanyaanIsian(SoalUjianRequest request) {
        if (request.getJawabanBenar() == null || request.getJawabanBenar().isEmpty()) {
            throw new IllegalArgumentException("Jawaban benar wajib diisi untuk ISIAN");
        }
        if (request.getToleransiTypo() == null) {
            request.setToleransiTypo(String.valueOf(0)); // Default no typo tolerance
        }
    }

    public DefaultResponse<SoalUjian> getSoalUjianById(String soalUjianId) throws IOException {
        SoalUjian soalUjianResponse = soalUjianRepository.findById(soalUjianId);
        return new DefaultResponse<>(soalUjianResponse.isValid() ? soalUjianResponse : null,
                soalUjianResponse.isValid() ? 1 : 0,
                "Successfully get data");
    }

    public SoalUjian updateSoalUjian(String soalUjianId, SoalUjianRequest soalUjianRequest) throws IOException {
        // Validate input parameters
        if (soalUjianId == null || soalUjianId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID Soal Ujian wajib diisi");
        }
        if (soalUjianRequest == null) {
            throw new IllegalArgumentException("Request soal ujian tidak boleh null");
        }

        // Check if question exists
        SoalUjian existingSoal = soalUjianRepository.findById(soalUjianId);
        if (existingSoal == null || !existingSoal.isValid()) {
            throw new IllegalArgumentException("Soal ujian dengan ID " + soalUjianId + " tidak ditemukan");
        }

        // Validate basic required fields
        if (soalUjianRequest.getNamaUjian() == null || soalUjianRequest.getNamaUjian().trim().isEmpty()) {
            throw new IllegalArgumentException("Nama ujian wajib diisi");
        }
        if (soalUjianRequest.getPertanyaan() == null || soalUjianRequest.getPertanyaan().trim().isEmpty()) {
            throw new IllegalArgumentException("Pertanyaan wajib diisi");
        }
        if (soalUjianRequest.getJenisSoal() == null || soalUjianRequest.getJenisSoal().trim().isEmpty()) {
            throw new IllegalArgumentException("Jenis soal wajib diisi");
        }
        if (soalUjianRequest.getBobot() == null || soalUjianRequest.getBobot().trim().isEmpty()) {
            throw new IllegalArgumentException("Bobot soal wajib diisi");
        }

        // Validate bobot is numeric
        try {
            Double.parseDouble(soalUjianRequest.getBobot());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Bobot soal harus berupa angka yang valid");
        }

        // Get related entities
        User userResponse = null;
        if (soalUjianRequest.getIdUser() != null && !soalUjianRequest.getIdUser().trim().isEmpty()) {
            userResponse = userRepository.findById(soalUjianRequest.getIdUser());
            if (userResponse == null || !userResponse.isValid()) {
                throw new IllegalArgumentException("User dengan ID " + soalUjianRequest.getIdUser() + " tidak ditemukan");
            }
        }

        Taksonomi taksonomiResponse = null;
        if (soalUjianRequest.getIdTaksonomi() != null && !soalUjianRequest.getIdTaksonomi().trim().isEmpty()) {
            taksonomiResponse = taksonomiRepository.findById(soalUjianRequest.getIdTaksonomi());
            if (taksonomiResponse == null || !taksonomiResponse.isValid()) {
                throw new IllegalArgumentException("Taksonomi dengan ID " + soalUjianRequest.getIdTaksonomi() + " tidak ditemukan");
            }
        }

        KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolahResponse = null;
        if (soalUjianRequest.getIdKonsentrasiSekolah() != null && !soalUjianRequest.getIdKonsentrasiSekolah().trim().isEmpty()) {
            konsentrasiKeahlianSekolahResponse = konsentrasiKeahlianSekolahRepository
                    .findById(soalUjianRequest.getIdKonsentrasiSekolah());
            if (konsentrasiKeahlianSekolahResponse == null || !konsentrasiKeahlianSekolahResponse.isValid()) {
                throw new IllegalArgumentException("Konsentrasi Keahlian Sekolah dengan ID " + soalUjianRequest.getIdKonsentrasiSekolah() + " tidak ditemukan");
            }
        }

        School schoolResponse = null;
        if (soalUjianRequest.getIdSchool() != null && !soalUjianRequest.getIdSchool().trim().isEmpty()) {
            schoolResponse = schoolRepository.findById(soalUjianRequest.getIdSchool());
            if (schoolResponse == null || !schoolResponse.isValid()) {
                throw new IllegalArgumentException("School dengan ID " + soalUjianRequest.getIdSchool() + " tidak ditemukan");
            }
        }

        // Update question entity
        SoalUjian updatedSoal = new SoalUjian();
        updatedSoal.setIdSoalUjian(soalUjianId); // Maintain the same ID
        updatedSoal.setNamaUjian(soalUjianRequest.getNamaUjian());
        updatedSoal.setPertanyaan(soalUjianRequest.getPertanyaan());
        updatedSoal.setBobot(soalUjianRequest.getBobot());
        updatedSoal.setJenisSoal(soalUjianRequest.getJenisSoal());
        updatedSoal.setCreatedAt(existingSoal.getCreatedAt()); // Keep original creation date
        updatedSoal.setUser(userResponse);
        updatedSoal.setTaksonomi(taksonomiResponse);
        updatedSoal.setKonsentrasiKeahlianSekolah(konsentrasiKeahlianSekolahResponse);
        updatedSoal.setSchool(schoolResponse);

        // Handle different question types
        String jenisSoalUpper = soalUjianRequest.getJenisSoal().toUpperCase().trim();
        switch (jenisSoalUpper) {
            case "PG":
                System.out.println("Processing PG question type");
                validatePertanyaanPG(soalUjianRequest);
                updatedSoal.setOpsi(soalUjianRequest.getOpsi());
                updatedSoal.setJawabanBenar(soalUjianRequest.getJawabanBenar());
                // Clear fields not used by PG
                updatedSoal.setPasangan(null);
                updatedSoal.setToleransiTypo(null);
                System.out.println("PG question processed successfully. Opsi: " + soalUjianRequest.getOpsi() + ", Jawaban: " + soalUjianRequest.getJawabanBenar());
                break;

            case "MULTI":
                System.out.println("Processing MULTI question type");
                validatePertanyaanMulti(soalUjianRequest);
                updatedSoal.setOpsi(soalUjianRequest.getOpsi());
                updatedSoal.setJawabanBenar(soalUjianRequest.getJawabanBenar());
                // Clear fields not used by MULTI
                updatedSoal.setPasangan(null);
                updatedSoal.setToleransiTypo(null);
                break;

            case "COCOK":
                System.out.println("Processing COCOK question type");
                validatePertanyaanCocok(soalUjianRequest);
                updatedSoal.setPasangan(soalUjianRequest.getPasangan());
                updatedSoal.setJawabanBenar(soalUjianRequest.getJawabanBenar());
                // Clear fields not used by COCOK
                updatedSoal.setOpsi(null);
                updatedSoal.setToleransiTypo(null);
                break;

            case "ISIAN":
                System.out.println("Processing ISIAN question type");
                validatePertanyaanIsian(soalUjianRequest);
                updatedSoal.setJawabanBenar(soalUjianRequest.getJawabanBenar());
                updatedSoal.setToleransiTypo(soalUjianRequest.getToleransiTypo());
                // Clear fields not used by ISIAN
                updatedSoal.setOpsi(null);
                updatedSoal.setPasangan(null);
                break;

            default:
                throw new IllegalArgumentException("Jenis soal tidak dikenali: '" + soalUjianRequest.getJenisSoal() + "'. Jenis soal yang valid: PG, MULTI, COCOK, ISIAN");
        }

        System.out.println("About to update soal with cascade. ID: " + soalUjianId);
        
        try {
            // Use updateWithCascade to automatically update corresponding BankSoal entries
            SoalUjian result = soalUjianRepository.updateWithCascade(soalUjianId, updatedSoal);
            System.out.println("Successfully updated SoalUjian with ID: " + soalUjianId);
            return result;
        } catch (Exception e) {
            System.err.println("Error updating SoalUjian with ID: " + soalUjianId + ". Error: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Gagal mengupdate soal ujian: " + e.getMessage(), e);
        }
    }

    public void deleteSoalUjianById(String soalUjianId) throws IOException {
        SoalUjian soalUjianResponse = soalUjianRepository.findById(soalUjianId);
        if (soalUjianResponse.isValid()) {
            // Delete SoalUjian without cascade (no BankSoal deletion)
            soalUjianRepository.deleteById(soalUjianId);
        } else {
            throw new ResourceNotFoundException("Soal Ujian", "id", soalUjianId);
        }
    }

}
