package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.QuestionCriteria;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.QuestionCriteriaRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.QuestionCriteriaRepository;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.util.List;

/**
 * @author alfa
 */
public class QuestionCriteriaService {
    private QuestionCriteriaRepository questionCriteriaRepository = new QuestionCriteriaRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(QuestionCriteriaService.class);

    public PagedResponse<QuestionCriteria> getAllQuestionCriteria(int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve polls
        List<QuestionCriteria> questionCriteriaResponse = questionCriteriaRepository.findAll(size);
        return new PagedResponse<>(questionCriteriaResponse, questionCriteriaResponse.size(), "Successfully get data",
                200);
    }

    public QuestionCriteria createQuestionCriteria(QuestionCriteriaRequest questionCriteriaRequest) throws IOException {
        QuestionCriteria questionCriteria = new QuestionCriteria();
        questionCriteria.setName(questionCriteriaRequest.getName());
        questionCriteria.setDescription(questionCriteriaRequest.getDescription());
        questionCriteria.setCategory(questionCriteriaRequest.getCategory());
        return questionCriteriaRepository.save(questionCriteria);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    public DefaultResponse<QuestionCriteria> getQuestionCriteriaById(String questionCriteriaId) throws IOException {
        // Retrieve QuestionCriteria
        QuestionCriteria questionCriteriaResponse = questionCriteriaRepository.findById(questionCriteriaId);
        return new DefaultResponse<>(questionCriteriaResponse.isValid() ? questionCriteriaResponse : null,
                questionCriteriaResponse.isValid() ? 1 : 0, "Successfully get data");
    }

    public QuestionCriteria updateQuestionCriteria(String questionCriteriaId,
            QuestionCriteriaRequest questionCriteriaRequest) throws IOException {
        // Create a new QuestionCriteria object
        QuestionCriteria questionCriteria = new QuestionCriteria();

        // Set the properties on the QuestionCriteria object
        questionCriteria.setId(questionCriteriaId);
        questionCriteria.setName(questionCriteriaRequest.getName());
        questionCriteria.setDescription(questionCriteriaRequest.getDescription());
        questionCriteria.setCategory(questionCriteriaRequest.getCategory());
        // Now you can use the questionCriteria object in your update method
        return questionCriteriaRepository.update(questionCriteria);
    }

    public void deleteQuestionCriteriaById(String questionCriteriaId) throws IOException {
        QuestionCriteria questionCriteriaResponse = questionCriteriaRepository.findById(questionCriteriaId);
        if (questionCriteriaResponse.isValid()) {
            questionCriteriaRepository.deleteById(questionCriteriaId);
        } else {
            throw new ResourceNotFoundException("QuestionCriteria", "id", questionCriteriaId);
        }
    }
}
