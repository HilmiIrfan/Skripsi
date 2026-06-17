package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.KonsentrasiKeahlian;
import com.doyatama.university.model.KonsentrasiKeahlianSekolah;
import com.doyatama.university.model.School;
import com.doyatama.university.payload.KonsentrasiKeahlianSekolahRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.KonsentrasiKeahlianSekolahRepository;
import com.doyatama.university.repository.KonsentrasiKeahlianRepository;
import com.doyatama.university.repository.SchoolRepository;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class KonsentrasiKeahlianSekolahService {

    private KonsentrasiKeahlianSekolahRepository konsentrasiKeahlianSekolahRepository = new KonsentrasiKeahlianSekolahRepository();
    private KonsentrasiKeahlianRepository konsentrasiKeahlianRepository = new KonsentrasiKeahlianRepository();
    private SchoolRepository schoolRepository = new SchoolRepository();

    public PagedResponse<KonsentrasiKeahlianSekolah> getAllKonsentrasiKeahlianSekolah(int page, int size,
            String schoolID,
            String konsentrasiKeahlianID) throws IOException {
        validatePageNumberAndSize(page, size);

        List<KonsentrasiKeahlianSekolah> konsentrasiKeahlianSekolahResponse;

        if (schoolID.equalsIgnoreCase("*")) {
            konsentrasiKeahlianSekolahResponse = konsentrasiKeahlianSekolahRepository.findAll(size);
        } else {
            konsentrasiKeahlianSekolahResponse = konsentrasiKeahlianSekolahRepository
                    .findKonsentrasiKeahlianSekolahBySekolah(schoolID,
                            size);
        }

        return new PagedResponse<>(konsentrasiKeahlianSekolahResponse, konsentrasiKeahlianSekolahResponse.size(),
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

    public KonsentrasiKeahlianSekolah createKonsentrasiKeahlianSekolah(
            KonsentrasiKeahlianSekolahRequest konsentrasiKeahlianSekolahRequest)
            throws IOException {

        if (konsentrasiKeahlianSekolahRequest.getIdKonsentrasiSekolah() == null) {
            konsentrasiKeahlianSekolahRequest.setIdKonsentrasiSekolah(UUID.randomUUID().toString());
        }

        if (konsentrasiKeahlianSekolahRepository
                .existsById(konsentrasiKeahlianSekolahRequest.getIdKonsentrasiSekolah())) {
            throw new IllegalArgumentException("Konsentrasi Sekolah already exist");
        }

        School schoolResponse = schoolRepository.findById(konsentrasiKeahlianSekolahRequest.getIdSekolah());
        KonsentrasiKeahlian konsentrasiKeahlianResponse = konsentrasiKeahlianRepository
                .findById(konsentrasiKeahlianSekolahRequest.getIdKonsentrasiKeahlian());

        KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolah = new KonsentrasiKeahlianSekolah();

        konsentrasiKeahlianSekolah.setIdKonsentrasiSekolah(konsentrasiKeahlianSekolahRequest.getIdKonsentrasiSekolah());
        konsentrasiKeahlianSekolah
                .setNamaKonsentrasiSekolah(konsentrasiKeahlianSekolahRequest.getNamaKonsentrasiSekolah());

        konsentrasiKeahlianSekolah.setSchool(schoolResponse);
        konsentrasiKeahlianSekolah.setKonsentrasiKeahlian(konsentrasiKeahlianResponse);

        return konsentrasiKeahlianSekolahRepository.save(konsentrasiKeahlianSekolah);

    }

    public DefaultResponse<KonsentrasiKeahlianSekolah> getKonsentrasiKeahlianSekolahById(
            String konsentrasiKeahlianSekolahId) throws IOException {
        KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolah = konsentrasiKeahlianSekolahRepository
                .findKonsentrasiKeahlianSekolahById(konsentrasiKeahlianSekolahId);
        return new DefaultResponse<>(konsentrasiKeahlianSekolah.isValid() ? konsentrasiKeahlianSekolah : null,
                konsentrasiKeahlianSekolah.isValid() ? 1 : 0, "Successfully get data");

    }

    public KonsentrasiKeahlianSekolah updateKonsentrasiKeahlianSekolah(String konsentrasiKeahlianSekolahId,
            KonsentrasiKeahlianSekolahRequest konsentrasiKeahlianSekolahRequest) throws IOException {

        KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolah = new KonsentrasiKeahlianSekolah();

        School schoolResponse = schoolRepository.findById(konsentrasiKeahlianSekolahRequest.getIdSekolah());
        KonsentrasiKeahlian konsentrasiKeahlianResponse = konsentrasiKeahlianRepository
                .findById(konsentrasiKeahlianSekolahRequest.getIdKonsentrasiKeahlian());

        if (schoolResponse.getIdSchool() != null) {
            konsentrasiKeahlianSekolah
                    .setNamaKonsentrasiSekolah(konsentrasiKeahlianSekolahRequest.getNamaKonsentrasiSekolah());
            konsentrasiKeahlianSekolah.setSchool(schoolResponse);
            konsentrasiKeahlianSekolah.setKonsentrasiKeahlian(konsentrasiKeahlianResponse);

            return konsentrasiKeahlianSekolahRepository.update(konsentrasiKeahlianSekolahId,
                    konsentrasiKeahlianSekolah);
        } else {
            return null;

        }
    }

    public void deleteKonsentrasiKeahlianSekolahById(String konsentrasiKeahlianSekolahId) throws IOException {
        KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolahResponse = konsentrasiKeahlianSekolahRepository
                .findKonsentrasiKeahlianSekolahById(konsentrasiKeahlianSekolahId);
        if (konsentrasiKeahlianSekolahResponse.isValid()) {
            konsentrasiKeahlianSekolahRepository.deleteById(konsentrasiKeahlianSekolahId);
        } else {
            throw new ResourceNotFoundException("Konsentrasi Sekolah", "id", konsentrasiKeahlianSekolahId);
        }
    }

}
