package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.School;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.SchoolRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.SchoolRepository;
import com.doyatama.university.util.AppConstants;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
public class SchoolService {
    private SchoolRepository schoolRepository = new SchoolRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(SchoolService.class);

    public PagedResponse<School> getAllSchool(int page, int size, String schoolID) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<School> schoolResponse;

        if (schoolID == null || schoolID.isEmpty() || schoolID.equals("*") || schoolID.equals("-")) {
            // Menampilkan semua user tanpa filter sekolah
            schoolResponse = schoolRepository.findAll(size);
        } else {
            // Menampilkan user berdasarkan sekolah
            schoolResponse = schoolRepository.findSchoolBySekolah(schoolID, size);
        }

        return new PagedResponse<>(schoolResponse, schoolResponse.size(), "Successfully get data", 200);
    }

    public School createSchool(SchoolRequest schoolRequest) throws IOException {
        School school = new School();
        school.setIdSchool(schoolRequest.getIdSchool());
        school.setNameSchool(schoolRequest.getNameSchool());
        school.setAddress(schoolRequest.getAddress());
        return schoolRepository.save(school);
    }

    public DefaultResponse<School> getSchoolById(String schoolId) throws IOException {
        // Retrieve School
        School schoolResponse = schoolRepository.findById(schoolId);
        return new DefaultResponse<>(schoolResponse.isValid() ? schoolResponse : null, schoolResponse.isValid() ? 1 : 0,
                "Successfully get data");
    }

    public School updateSchool(String schoolId, SchoolRequest schoolRequest) throws IOException {
        School school = new School();
        school.setNameSchool(schoolRequest.getNameSchool());
        school.setAddress(schoolRequest.getAddress());
        return schoolRepository.update(schoolId, school);
    }

    public void deleteSchoolById(String schoolId) throws IOException {
        School schoolResponse = schoolRepository.findById(schoolId);
        if (schoolResponse.isValid()) {
            schoolRepository.deleteById(schoolId);
        } else {
            throw new ResourceNotFoundException("School", "idSchool", schoolId);
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
