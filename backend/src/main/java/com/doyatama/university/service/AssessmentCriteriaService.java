package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.AssessmentCriteria;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.AssessmentCriteriaRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.AssessmentCriteriaRepository;
import com.doyatama.university.util.AppConstants;
import org.springframework.stereotype.Service;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@Service
public class AssessmentCriteriaService {
    private AssessmentCriteriaRepository assessmentCriteriaRepository = new AssessmentCriteriaRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(AssessmentCriteriaService.class);

    public PagedResponse<AssessmentCriteria> getAllAssessmentCriteria(int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<AssessmentCriteria> assessmentCriteriaResponse = assessmentCriteriaRepository.findAll(size);

        return new PagedResponse<>(assessmentCriteriaResponse, assessmentCriteriaResponse.size(),
                "Successfully get data", 200);
    }

    public AssessmentCriteria createAssessmentCriteria(AssessmentCriteriaRequest assessmentCriteriaRequest)
            throws IOException {
        AssessmentCriteria assessmentCriteria = new AssessmentCriteria();

        assessmentCriteria.setName(assessmentCriteriaRequest.getName());
        assessmentCriteria.setDescription(assessmentCriteriaRequest.getDescription());

        return assessmentCriteriaRepository.save(assessmentCriteria);
    }

    public DefaultResponse<AssessmentCriteria> getAssessmentCriteriaById(String assessmentCriteriaId)
            throws IOException {
        // Retrieve AssessmentCriteria
        AssessmentCriteria assessmentCriteriaResponse = assessmentCriteriaRepository.findById(assessmentCriteriaId);
        return new DefaultResponse<>(assessmentCriteriaResponse.isValid() ? assessmentCriteriaResponse : null,
                assessmentCriteriaResponse.isValid() ? 1 : 0, "Successfully get data");
    }

    public AssessmentCriteria updateAssessmentCriteria(String assessmentCriteriaId,
            AssessmentCriteriaRequest assessmentCriteriaRequest) throws IOException {
        AssessmentCriteria assessmentCriteria = new AssessmentCriteria();
        assessmentCriteria.setName(assessmentCriteriaRequest.getName());
        assessmentCriteria.setDescription(assessmentCriteriaRequest.getDescription());

        return assessmentCriteriaRepository.update(assessmentCriteriaId, assessmentCriteria);
    }

    public void deleteAssessmentCriteriaById(String assessmentCriteriaId) throws IOException {
        AssessmentCriteria assessmentCriteriaResponse = assessmentCriteriaRepository.findById(assessmentCriteriaId);
        if (assessmentCriteriaResponse.isValid()) {
            assessmentCriteriaRepository.deleteById(assessmentCriteriaId);
        } else {
            throw new ResourceNotFoundException("AssessmentCriteria", "id", assessmentCriteriaId);
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
