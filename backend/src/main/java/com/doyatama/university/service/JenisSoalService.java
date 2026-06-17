package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.School;
import com.doyatama.university.model.JenisSoal;
import com.doyatama.university.payload.JenisSoalRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.SchoolRepository;
import com.doyatama.university.repository.JenisSoalRepository;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class JenisSoalService {
    private JenisSoalRepository jenisSoalRepository = new JenisSoalRepository();
    private SchoolRepository schoolRepository = new SchoolRepository();

    public PagedResponse<JenisSoal> getAllJenisSoal(int page, int size, String schoolID) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<JenisSoal> jenisSoalResponse;

        if (schoolID.equalsIgnoreCase("*")) {
            jenisSoalResponse = jenisSoalRepository.findAll(size);
        } else {
            jenisSoalResponse = jenisSoalRepository.findJenisSoalBySekolah(schoolID, size);
        }

        return new PagedResponse<>(jenisSoalResponse, jenisSoalResponse.size(), "Successfully get data", 200);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    public JenisSoal createJenisSoal(JenisSoalRequest jenisSoalRequest) throws IOException {

        if (jenisSoalRequest.getIdJenisSoal() == null) {
            jenisSoalRequest.setIdJenisSoal(UUID.randomUUID().toString());
        }

        if (jenisSoalRepository.existById(jenisSoalRequest.getIdJenisSoal())) {
            throw new IllegalArgumentException("Jenis Soal already exist");
        }

        School schoolResponse = schoolRepository.findById(jenisSoalRequest.getIdSekolah());

        JenisSoal jenisSoal = new JenisSoal();
        jenisSoal.setIdJenisSoal(jenisSoalRequest.getIdJenisSoal());
        jenisSoal.setNamaJenisSoal(jenisSoalRequest.getNamaJenisSoal());
        jenisSoal.setSchool(schoolResponse);
        return jenisSoalRepository.save(jenisSoal);
    }

    public DefaultResponse<JenisSoal> getJenisSoalById(String jenisSoalId) throws IOException {
        // Retrieve Jenis Soal
        JenisSoal jenisSoal = jenisSoalRepository.findById(jenisSoalId);
        return new DefaultResponse<>(jenisSoal.isValid() ? jenisSoal : null, jenisSoal.isValid() ? 1 : 0,
                "Successfully get data");
    }

    public JenisSoal updateJenisSoal(String jenisSoalId, JenisSoalRequest jenisSoalRequest) throws IOException {

        JenisSoal jenisSoal = new JenisSoal();
        School schoolResponse = schoolRepository.findById(jenisSoalRequest.getIdSekolah());

        if (schoolResponse.getIdSchool() != null) {
            jenisSoal.setNamaJenisSoal(jenisSoalRequest.getNamaJenisSoal());
            jenisSoal.setSchool(schoolResponse);
            return jenisSoalRepository.update(jenisSoalId, jenisSoal);
        } else {
            return null;
        }
    }

    public void deleteJenisSoalById(String jenisSoalId) throws IOException {
        JenisSoal jenisSoalResponse = jenisSoalRepository.findById(jenisSoalId);
        if (jenisSoalResponse.isValid()) {
            jenisSoalRepository.deleteById(jenisSoalId);
        } else {
            throw new ResourceNotFoundException("Jenis Soal", "id", jenisSoalId);
        }
    }

}
