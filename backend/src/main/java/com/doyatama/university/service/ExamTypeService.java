package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.ExamType;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.ExamTypeRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.ExamTypeRepository;
import com.doyatama.university.util.AppConstants;
import org.springframework.stereotype.Service;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@Service
public class ExamTypeService {
    private ExamTypeRepository examTypeRepository = new ExamTypeRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(ExamTypeService.class);

    public PagedResponse<ExamType> getAllExamTypes(int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve ExamTypes
        List<ExamType> examTypeResponse = examTypeRepository.findAll(size);

        return new PagedResponse<>(examTypeResponse, examTypeResponse.size(), "Successfully get data", 200);
    }

    public ExamType createExamType(ExamTypeRequest examTypeRequest) throws IOException {
        ExamType examType = new ExamType();

        examType.setName(examTypeRequest.getName());
        examType.setDescription(examTypeRequest.getDescription());

        return examTypeRepository.save(examType);
    }

    public DefaultResponse<ExamType> getExamTypeById(String examTypeId) throws IOException {
        // Retrieve ExamType
        ExamType examTypeResponse = examTypeRepository.findById(examTypeId);
        return new DefaultResponse<>(examTypeResponse.isValid() ? examTypeResponse : null,
                examTypeResponse.isValid() ? 1 : 0, "Successfully get data");
    }

    public ExamType updateExamType(String examTypeId, ExamTypeRequest examTypeRequest) throws IOException {
        ExamType examType = new ExamType();
        examType.setName(examTypeRequest.getName());
        examType.setDescription(examTypeRequest.getDescription());

        return examTypeRepository.update(examTypeId, examType);
    }

    public void deleteExamTypeById(String examTypeId) throws IOException {
        ExamType examTypeResponse = examTypeRepository.findById(examTypeId);
        if (examTypeResponse.isValid()) {
            examTypeRepository.deleteById(examTypeId);
        } else {
            throw new ResourceNotFoundException("ExamType", "id", examTypeId);
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