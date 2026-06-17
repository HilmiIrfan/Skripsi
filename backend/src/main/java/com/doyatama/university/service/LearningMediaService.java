package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.LearningMedia;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.LearningMediaRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.LearningMediaRepository;
import com.doyatama.university.util.AppConstants;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
public class LearningMediaService {
    private LearningMediaRepository learningMediaRepository = new LearningMediaRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(LearningMediaService.class);

    public PagedResponse<LearningMedia> getAllLearningMedia(int page, int size, String type) throws IOException {
        validatePageNumberAndSize(page, size);
        List<LearningMedia> learningMediaResponse;
        if (type.equalsIgnoreCase("*")) {
            learningMediaResponse = learningMediaRepository.findAll(size);
        } else {
            learningMediaResponse = learningMediaRepository.findByType(type, size);
        }

        // Retrieve Polls

        return new PagedResponse<>(learningMediaResponse, learningMediaResponse.size(), "Successfully get data", 200);
    }

    public LearningMedia createLearningMedia(LearningMediaRequest learningMediaRequest) throws IOException {
        LearningMedia learningMedia = new LearningMedia();

        learningMedia.setName(learningMediaRequest.getName());
        learningMedia.setDescription(learningMediaRequest.getDescription());
        learningMedia.setType(learningMediaRequest.getType());

        return learningMediaRepository.save(learningMedia);
    }

    public DefaultResponse<LearningMedia> getLearningMediaById(String learningMediaId) throws IOException {
        // Retrieve LearningMedia
        LearningMedia learningMediaResponse = learningMediaRepository.findById(learningMediaId);
        return new DefaultResponse<>(learningMediaResponse.isValid() ? learningMediaResponse : null,
                learningMediaResponse.isValid() ? 1 : 0, "Successfully get data");
    }

    public LearningMedia updateLearningMedia(String learningMediaId, LearningMediaRequest learningMediaRequest)
            throws IOException {
        LearningMedia learningMedia = new LearningMedia();
        learningMedia.setName(learningMediaRequest.getName());
        learningMedia.setDescription(learningMediaRequest.getDescription());
        learningMedia.setType(learningMediaRequest.getType());
        return learningMediaRepository.update(learningMediaId, learningMedia);
    }

    public void deleteLearningMediaById(String learningMediaId) throws IOException {
        LearningMedia learningMediaResponse = learningMediaRepository.findById(learningMediaId);
        if (learningMediaResponse.isValid()) {
            learningMediaRepository.deleteById(learningMediaId);
        } else {
            throw new ResourceNotFoundException("LearningMedia", "id", learningMediaId);
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
