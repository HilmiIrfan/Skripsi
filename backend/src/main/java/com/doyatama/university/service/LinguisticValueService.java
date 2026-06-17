package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.LinguisticValue;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.LinguisticValueRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.LinguisticValueRepository;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.util.List;

/**
 * @author alfa
 */

public class LinguisticValueService {
    private LinguisticValueRepository linguisticValueRepository = new LinguisticValueRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(LinguisticValueService.class);

    public PagedResponse<LinguisticValue> getAllLinguisticValue(int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve polls
        List<LinguisticValue> linguisticValueResponse = linguisticValueRepository.findAll(size);
        return new PagedResponse<>(linguisticValueResponse, linguisticValueResponse.size(), "Successfully get data",
                200);
    }

    public LinguisticValue createLinguisticValue(LinguisticValueRequest linguisticValueRequest, String savePath)
            throws IOException {
        LinguisticValue linguisticValue = new LinguisticValue();

        // Check if the name in the request is not null

        linguisticValue.setName(linguisticValueRequest.getName());
        linguisticValue.setValue1(linguisticValueRequest.getValue1());
        linguisticValue.setValue2(linguisticValueRequest.getValue2());
        linguisticValue.setValue3(linguisticValueRequest.getValue3());
        linguisticValue.setValue4(linguisticValueRequest.getValue4());
        linguisticValue.setFile_path(savePath.isEmpty() ? null : savePath);

        return linguisticValueRepository.save(linguisticValue);

    }

    public DefaultResponse<LinguisticValue> getLinguisticValueById(String linguisticValueId) throws IOException {
        // Retrieve LinguisticValue
        LinguisticValue linguisticValueResponse = linguisticValueRepository.findById(linguisticValueId);
        return new DefaultResponse<>(linguisticValueResponse.isValid() ? linguisticValueResponse : null,
                linguisticValueResponse.isValid() ? 1 : 0, "Successfully get data");
    }

    public LinguisticValue updateLinguisticValue(String linguisticValueId,
            LinguisticValueRequest linguisticValueRequest) throws IOException {
        // Fetch the existing LinguisticValue object
        LinguisticValue linguisticValue = linguisticValueRepository.findById(linguisticValueId);

        // Update the fields on the LinguisticValue object
        linguisticValue.setName(linguisticValueRequest.getName());
        linguisticValue.setValue1(linguisticValueRequest.getValue1());
        linguisticValue.setValue2(linguisticValueRequest.getValue2());
        linguisticValue.setValue3(linguisticValueRequest.getValue3());
        linguisticValue.setValue4(linguisticValueRequest.getValue4());

        // Save the updated LinguisticValue object back to the repository
        return linguisticValueRepository.update(linguisticValue);
    }

    public void deleteLinguisticValueById(String linguisticValueId) throws IOException {
        LinguisticValue linguisticValueResponse = linguisticValueRepository.findById(linguisticValueId);
        if (linguisticValueResponse == null) {
            throw new ResourceNotFoundException("LinguisticValue", "id", linguisticValueId);
        }
        linguisticValueRepository.deleteById(linguisticValueId);
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
