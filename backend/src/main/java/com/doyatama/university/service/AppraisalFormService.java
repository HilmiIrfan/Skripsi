package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.AppraisalForm;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.AppraisalFormRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.AppraisalFormRepository;
import com.doyatama.university.util.AppConstants;
import org.springframework.stereotype.Service;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@Service
public class AppraisalFormService {
    private AppraisalFormRepository appraisalFormRepository = new AppraisalFormRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(AppraisalFormService.class);

    public PagedResponse<AppraisalForm> getAllAppraisalForm(int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<AppraisalForm> appraisalFormResponse = appraisalFormRepository.findAll(size);

        return new PagedResponse<>(appraisalFormResponse, appraisalFormResponse.size(), "Successfully get data", 200);
    }

    public AppraisalForm createAppraisalForm(AppraisalFormRequest appraisalFormRequest) throws IOException {
        AppraisalForm appraisalForm = new AppraisalForm();

        appraisalForm.setName(appraisalFormRequest.getName());
        appraisalForm.setDescription(appraisalFormRequest.getDescription());

        return appraisalFormRepository.save(appraisalForm);
    }

    public DefaultResponse<AppraisalForm> getAppraisalFormById(String appraisalFormId) throws IOException {
        // Retrieve AppraisalForm
        AppraisalForm appraisalFormResponse = appraisalFormRepository.findById(appraisalFormId);
        return new DefaultResponse<>(appraisalFormResponse.isValid() ? appraisalFormResponse : null,
                appraisalFormResponse.isValid() ? 1 : 0, "Successfully get data");
    }

    public AppraisalForm updateAppraisalForm(String appraisalFormId, AppraisalFormRequest appraisalFormRequest)
            throws IOException {
        AppraisalForm appraisalForm = new AppraisalForm();
        appraisalForm.setName(appraisalFormRequest.getName());
        appraisalForm.setDescription(appraisalFormRequest.getDescription());

        return appraisalFormRepository.update(appraisalFormId, appraisalForm);
    }

    public void deleteAppraisalFormById(String appraisalFormId) throws IOException {
        AppraisalForm appraisalFormResponse = appraisalFormRepository.findById(appraisalFormId);
        if (appraisalFormResponse.isValid()) {
            appraisalFormRepository.deleteById(appraisalFormId);
        } else {
            throw new ResourceNotFoundException("AppraisalForm", "id", appraisalFormId);
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
