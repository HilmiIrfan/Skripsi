package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.ProgramKeahlian;
import com.doyatama.university.model.ProgramKeahlianSekolah;
import com.doyatama.university.model.School;
import com.doyatama.university.payload.ProgramKeahlianSekolahRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.ProgramKeahlianSekolahRepository;
import com.doyatama.university.repository.ProgramKeahlianRepository;
import com.doyatama.university.repository.SchoolRepository;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class ProgramKeahlianSekolahService {

    private ProgramKeahlianSekolahRepository programKeahlianSekolahRepository = new ProgramKeahlianSekolahRepository();
    private ProgramKeahlianRepository programKeahlianRepository = new ProgramKeahlianRepository();
    private SchoolRepository schoolRepository = new SchoolRepository();

    public PagedResponse<ProgramKeahlianSekolah> getAllProgramKeahlianSekolah(int page, int size, String schoolID,
            String programKeahlianID) throws IOException {
        validatePageNumberAndSize(page, size);

        List<ProgramKeahlianSekolah> programKeahlianSekolahResponse;

        if (schoolID.equalsIgnoreCase("*")) {
            programKeahlianSekolahResponse = programKeahlianSekolahRepository.findAll(size);
        } else {
            programKeahlianSekolahResponse = programKeahlianSekolahRepository.findProgramKeahlianSekolahBySekolah(
                    schoolID,
                    size);
        }

        return new PagedResponse<>(programKeahlianSekolahResponse, programKeahlianSekolahResponse.size(),
                "Successfully get data", 200);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    public ProgramKeahlianSekolah createProgramKeahlianSekolah(
            ProgramKeahlianSekolahRequest programKeahlianSekolahRequest)
            throws IOException {

        if (programKeahlianSekolahRequest.getIdProgramSekolah() == null) {
            programKeahlianSekolahRequest.setIdProgramSekolah(UUID.randomUUID().toString());
        }

        if (programKeahlianSekolahRepository
                .existsById(programKeahlianSekolahRequest.getIdProgramSekolah())) {
            throw new IllegalArgumentException("Program Sekolah already exist");
        }

        ProgramKeahlian programKeahlianResponse = programKeahlianRepository
                .findById(programKeahlianSekolahRequest.getIdProgramKeahlian());
        if (programKeahlianResponse == null) {
            throw new IllegalArgumentException("Program Keahlian not found");
        }

        School schoolResponse = schoolRepository.findById(programKeahlianSekolahRequest.getIdSekolah());
        if (schoolResponse == null) {
            throw new IllegalArgumentException("School not found");
        }

        ProgramKeahlianSekolah programKeahlianSekolah = new ProgramKeahlianSekolah();

        programKeahlianSekolah.setIdProgramSekolah(programKeahlianSekolahRequest.getIdProgramSekolah());
        programKeahlianSekolah.setNamaProgramSekolah(programKeahlianSekolahRequest.getNamaProgramSekolah());

        programKeahlianSekolah.setSchool(schoolResponse);
        programKeahlianSekolah.setProgramKeahlian(programKeahlianResponse);

        return programKeahlianSekolahRepository.save(programKeahlianSekolah);
    }

    public DefaultResponse<ProgramKeahlianSekolah> getProgramKeahlianSekolahById(String programKeahlianSekolahId)
            throws IOException {
        ProgramKeahlianSekolah programKeahlianSekolahResponse = programKeahlianSekolahRepository
                .findProgramKeahlianSekolahById(programKeahlianSekolahId);
        return new DefaultResponse<>(programKeahlianSekolahResponse.isValid() ? programKeahlianSekolahResponse : null,
                programKeahlianSekolahResponse.isValid() ? 1 : 0, "Successfully get data");
    }

    public ProgramKeahlianSekolah updateProgramKeahlianSekolah(String programKeahlianSekolahId,
            ProgramKeahlianSekolahRequest programKeahlianSekolahRequest) throws IOException {

        ProgramKeahlianSekolah programKeahlianSekolah = new ProgramKeahlianSekolah();

        ProgramKeahlian programKeahlianResponse = programKeahlianRepository
                .findById(programKeahlianSekolahRequest.getIdProgramKeahlian());
        if (programKeahlianResponse == null) {
            throw new IllegalArgumentException("Program Keahlian not found");
        }

        School schoolResponse = schoolRepository.findById(programKeahlianSekolahRequest.getIdSekolah());
        if (schoolResponse == null) {
            throw new IllegalArgumentException("School not found");
        }

        if (schoolResponse.getIdSchool() != null) {
            programKeahlianSekolah.setNamaProgramSekolah(programKeahlianSekolahRequest.getNamaProgramSekolah());
            programKeahlianSekolah.setSchool(schoolResponse);
            programKeahlianSekolah.setProgramKeahlian(programKeahlianResponse);

            return programKeahlianSekolahRepository.update(programKeahlianSekolahId, programKeahlianSekolah);
        } else {
            return null;
        }

    }

    public void deleteProgramKeahlianSekolahById(String programKeahlianSekolahId) throws IOException {
        ProgramKeahlianSekolah programKeahlianSekolahResponse = programKeahlianSekolahRepository
                .findProgramKeahlianSekolahById(programKeahlianSekolahId);
        if (programKeahlianSekolahResponse.isValid()) {
            programKeahlianSekolahRepository.deleteById(programKeahlianSekolahId);
        } else {
            throw new ResourceNotFoundException("Program Keahlian Sekolah", "idProgramSekolah",
                    programKeahlianSekolahId);
        }
    }

}
