package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.Taksonomi;
import com.doyatama.university.model.School;
import com.doyatama.university.payload.TaksonomiRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.TaksonomiRepository;
import com.doyatama.university.repository.SchoolRepository;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.hadoop.hbase.exceptions.IllegalArgumentIOException;

public class TaksonomiService {

    private TaksonomiRepository taksonomiRepository = new TaksonomiRepository();
    private SchoolRepository schoolRepository = new SchoolRepository();

    public PagedResponse<Taksonomi> getAllTaksonomi(int page, int size, String schoolID) throws IOException {
        validatePageNumberAndSize(page, size);

        List<Taksonomi> taksonomiResponse;

        if (schoolID.equalsIgnoreCase("*")) {
            taksonomiResponse = taksonomiRepository.findAll(size);
        } else {
            taksonomiResponse = taksonomiRepository.findTaksonomiBySekolah(schoolID, size);
        }

        return new PagedResponse<>(taksonomiResponse, taksonomiResponse.size(), "Successfully get data", 200);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    public Taksonomi createTaksonomi(TaksonomiRequest taksonomiRequest) throws IOException {

        if (taksonomiRequest.getIdTaksonomi() == null) {
            taksonomiRequest.setIdTaksonomi(UUID.randomUUID().toString());
        }

        if (taksonomiRepository.existById(taksonomiRequest.getIdTaksonomi())) {
            throw new IllegalArgumentIOException("Taksonomi with id already exists");
        }

        School schoolResponse = schoolRepository.findById(taksonomiRequest.getIdSekolah());
        Taksonomi taksonomi = new Taksonomi();

        taksonomi.setIdTaksonomi(taksonomiRequest.getIdTaksonomi());
        taksonomi.setNamaTaksonomi(taksonomiRequest.getNamaTaksonomi());
        taksonomi.setDeskripsiTaksonomi(taksonomiRequest.getDeskripsiTaksonomi());
        taksonomi.setSchool(schoolResponse);

        return taksonomiRepository.save(taksonomi);
    }

    public DefaultResponse<Taksonomi> getTaksonomiById(String taksonomiId) throws IOException {
        Taksonomi taksonomiResponse = taksonomiRepository.findById(taksonomiId);
        return new DefaultResponse<>(taksonomiResponse.isValid() ? taksonomiResponse : null,
                taksonomiResponse.isValid() ? 1 : 0,
                "Successfully get data");
    }

    public Taksonomi updateTaksonomi(String taksonomiId, TaksonomiRequest taksonomiRequest) throws IOException {

        Taksonomi taksonomi = new Taksonomi();
        School schoolResponse = schoolRepository.findById(taksonomiRequest.getIdSekolah());

        if (schoolResponse.getIdSchool() != null) {
            taksonomi.setNamaTaksonomi(taksonomiRequest.getNamaTaksonomi());
            taksonomi.setDeskripsiTaksonomi(taksonomiRequest.getDeskripsiTaksonomi());
            taksonomi.setSchool(schoolResponse);

            return taksonomiRepository.update(taksonomiId, taksonomi);
        } else {
            return null;
        }
    }

    public void deleteTaksonomiById(String taksonomiId) throws IOException {
        Taksonomi taksonomiResponse = taksonomiRepository.findById(taksonomiId);
        if (taksonomiResponse.isValid()) {
            taksonomiRepository.deleteById(taksonomiId);
        } else {
            throw new ResourceNotFoundException("Taksonomi", "id", taksonomiId);
        }
    }
}
