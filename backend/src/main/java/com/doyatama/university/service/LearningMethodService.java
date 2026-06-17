package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.LearningMethod;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.LearningMethodRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.LearningMethodRepository;
import com.doyatama.university.util.AppConstants;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
public class LearningMethodService {
    private LearningMethodRepository learningMethodRepository = new LearningMethodRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(LearningMethodService.class);

    public PagedResponse<LearningMethod> getAllLearningMethod(int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<LearningMethod> learningMethodResponse = learningMethodRepository.findAll(size);

        return new PagedResponse<>(learningMethodResponse, learningMethodResponse.size(), "Successfully get data", 200);
    }

    public LearningMethod createLearningMethod(LearningMethodRequest learningMethodRequest) throws IOException {
        LearningMethod learningMethod = new LearningMethod();

        learningMethod.setName(learningMethodRequest.getName());
        learningMethod.setDescription(learningMethodRequest.getDescription());

        return learningMethodRepository.save(learningMethod);
    }

    public DefaultResponse<LearningMethod> getLearningMethodById(String learningMethodId) throws IOException {
        // Retrieve LearningMethod
        LearningMethod learningMethodResponse = learningMethodRepository.findById(learningMethodId);
        return new DefaultResponse<>(learningMethodResponse.isValid() ? learningMethodResponse : null,
                learningMethodResponse.isValid() ? 1 : 0, "Successfully get data");
    }

    public LearningMethod updateLearningMethod(String learningMethodId, LearningMethodRequest learningMethodRequest)
            throws IOException {
        LearningMethod learningMethod = new LearningMethod();
        learningMethod.setName(learningMethodRequest.getName());
        learningMethod.setDescription(learningMethodRequest.getDescription());

        return learningMethodRepository.update(learningMethodId, learningMethod);
    }

    public void deleteLearningMethodById(String learningMethodId) throws IOException {
        LearningMethod learningMethodResponse = learningMethodRepository.findById(learningMethodId);
        if (learningMethodResponse.isValid()) {
            learningMethodRepository.deleteById(learningMethodId);
        } else {
            throw new ResourceNotFoundException("LearningMethod", "id", learningMethodId);
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
