package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.Kelas;
import com.doyatama.university.model.School;
import com.doyatama.university.payload.KelasRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.KelasRepository;
import com.doyatama.university.repository.SchoolRepository;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class KelasService {
    private KelasRepository kelasRepository = new KelasRepository();
    private SchoolRepository schoolRepository = new SchoolRepository();

    public PagedResponse<Kelas> getAllKelas(int page, int size, String schoolID) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<Kelas> kelasResponse;

        if (schoolID.equalsIgnoreCase("*")) {
            kelasResponse = kelasRepository.findAll(size);
        } else {
            kelasResponse = kelasRepository.findKelasBySekolah(schoolID, size);
        }

        return new PagedResponse<>(kelasResponse, kelasResponse.size(), "Successfully get data", 200);
    }

    public Kelas createKelas(KelasRequest kelasRequest) throws IOException {

        if (kelasRequest.getIdKelas() == null) {
            kelasRequest.setIdKelas(UUID.randomUUID().toString());
        }

        if (kelasRepository.existsById(kelasRequest.getIdKelas())) {
            throw new IllegalArgumentException("Kelas already exist");
        }

        School schoolResponse = schoolRepository.findById(kelasRequest.getIdSekolah());

        Kelas kelas = new Kelas();
        kelas.setIdKelas(kelasRequest.getIdKelas());
        kelas.setNamaKelas(kelasRequest.getNamaKelas());
        kelas.setSchool(schoolResponse);
        return kelasRepository.save(kelas);
    }

    public DefaultResponse<Kelas> getKelasById(String kelasId) throws IOException {
        // Retrieve Kelas
        Kelas kelas = kelasRepository.findById(kelasId);
        return new DefaultResponse<>(kelas.isValid() ? kelas : null, kelas.isValid() ? 1 : 0, "Successfully get data");
    }

    public Kelas updateKelas(String kelasId, KelasRequest kelasRequest) throws IOException {

        Kelas kelas = new Kelas();

        School schoolResponse = schoolRepository.findById(kelasRequest.getIdSekolah());

        if (schoolResponse.getIdSchool() != null) {
            kelas.setNamaKelas(kelasRequest.getNamaKelas());
            kelas.setSchool(schoolResponse);

            return kelasRepository.update(kelasId, kelas);
        } else {
            return null;
        }
    }

    public void deleteKelasById(String kelasId) throws IOException {
        Kelas kelasResponse = kelasRepository.findById(kelasId);
        if (kelasResponse.isValid()) {
            kelasRepository.deleteById(kelasId);
        } else {
            throw new ResourceNotFoundException("Kelas", "id", kelasId);
        }
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

}
