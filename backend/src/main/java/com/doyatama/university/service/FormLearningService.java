package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.FormLearning;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.FormLearningRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.FormLearningRepository;
import com.doyatama.university.util.AppConstants;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
public class FormLearningService {
    private FormLearningRepository formLearningRepository = new FormLearningRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(FormLearningService.class);

    public PagedResponse<FormLearning> getAllFormLearning(int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<FormLearning> formLearningResponse = formLearningRepository.findAll(size);

        return new PagedResponse<>(formLearningResponse, formLearningResponse.size(), "Successfully get data", 200);
    }

    public FormLearning createFormLearning(FormLearningRequest formLearningRequest) throws IOException {
        FormLearning formLearning = new FormLearning();

        formLearning.setName(formLearningRequest.getName());
        formLearning.setDescription(formLearningRequest.getDescription());

        return formLearningRepository.save(formLearning);
    }

    public DefaultResponse<FormLearning> getFormLearningById(String formLearningId) throws IOException {
        // Retrieve FormLearning
        FormLearning formLearningResponse = formLearningRepository.findById(formLearningId);
        return new DefaultResponse<>(formLearningResponse.isValid() ? formLearningResponse : null,
                formLearningResponse.isValid() ? 1 : 0, "Successfully get data");
    }

    public FormLearning updateFormLearning(String formLearningId, FormLearningRequest formLearningRequest)
            throws IOException {
        FormLearning formLearning = new FormLearning();
        formLearning.setName(formLearningRequest.getName());
        formLearning.setDescription(formLearningRequest.getDescription());

        return formLearningRepository.update(formLearningId, formLearning);
    }

    public void deleteFormLearningById(String formLearningId) throws IOException {
        FormLearning formLearningResponse = formLearningRepository.findById(formLearningId);
        if (formLearningResponse.isValid()) {
            formLearningRepository.deleteById(formLearningId);
        } else {
            throw new ResourceNotFoundException("FormLearning", "id", formLearningId);
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
