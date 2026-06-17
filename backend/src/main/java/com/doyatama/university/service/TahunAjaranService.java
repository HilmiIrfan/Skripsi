package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.School;
import com.doyatama.university.model.TahunAjaran;
import com.doyatama.university.payload.TahunAjaranRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.SchoolRepository;
import com.doyatama.university.repository.TahunAjaranRepository;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TahunAjaranService {
    private TahunAjaranRepository tahunRepository = new TahunAjaranRepository();
    private SchoolRepository schoolRepository = new SchoolRepository();

    public PagedResponse<TahunAjaran> getAllTahunAjaran(int page, String schoolID, int size) throws IOException {
        System.out.println("⚙️ [Service] schoolID diterima: " + schoolID);
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<TahunAjaran> tahunResponse;

        if (schoolID.equalsIgnoreCase("*")) {
            System.out.println("🔄 [Service] Menjalankan findAll()");
            tahunResponse = tahunRepository.findAll(size);
        } else {
            System.out.println("🔍 [Service] Menjalankan findTahunAjaranBySekolah untuk schoolID: " + schoolID);
            tahunResponse = tahunRepository.findTahunAjaranBySekolah(schoolID, size);
        }

        return new PagedResponse<>(tahunResponse, tahunResponse.size(), "Successfully get data", 200);
    }

    public TahunAjaran createTahunAjaran(TahunAjaranRequest tahunRequest) throws IOException {

        if (tahunRequest.getIdTahun() == null) {
            tahunRequest.setIdTahun(UUID.randomUUID().toString());
        }

        if (tahunRepository.existsById(tahunRequest.getIdTahun())) {
            throw new IllegalArgumentException("Tahun Ajaran already exist");
        }

        School schoolResponse = schoolRepository.findById(tahunRequest.getIdSekolah());

        TahunAjaran tahun = new TahunAjaran();
        tahun.setIdTahun(tahunRequest.getIdTahun());
        tahun.setTahunAjaran(tahunRequest.getTahunAjaran());

        tahun.setSchool(schoolResponse);

        return tahunRepository.save(tahun);
    }

    public DefaultResponse<TahunAjaran> getTahunAjaranById(String tahunId) throws IOException {
        TahunAjaran tahunResponse = tahunRepository.findTahunById(tahunId);
        return new DefaultResponse<>(tahunResponse.isValid() ? tahunResponse : null, tahunResponse.isValid() ? 1 : 0,
                "Successfully get data");
    }

    public TahunAjaran updateTahunAjaran(String tahunId, TahunAjaranRequest tahunRequest) throws IOException {

        TahunAjaran tahun = new TahunAjaran();

        School schoolResponse = schoolRepository.findById(tahunRequest.getIdSekolah());

        if (schoolResponse.getIdSchool() != null) {
            tahun.setTahunAjaran(tahunRequest.getTahunAjaran());
            tahun.setSchool(schoolResponse);

            return tahunRepository.update(tahunId, tahun);
        } else {
            return null;
        }
    }

    public void deleteTahunAjaranById(String tahunId) throws IOException {
        TahunAjaran tahunResponse = tahunRepository.findTahunById(tahunId);
        if (tahunResponse.isValid()) {
            tahunRepository.deleteById(tahunId);
        } else {
            throw new ResourceNotFoundException("TahunAjaran", "id", tahunId);
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
