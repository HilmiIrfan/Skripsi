package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.Religion;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.ReligionRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.ReligionRepository;
import com.doyatama.university.util.AppConstants;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ReligionService {
    private ReligionRepository religionRepository = new ReligionRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(ReligionService.class);

    public PagedResponse<Religion> getAllReligion(int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<Religion> religionResponse = religionRepository.findAll(size);

        return new PagedResponse<>(religionResponse, religionResponse.size(), "Successfully get data", 200);
    }

    public Religion createReligion(ReligionRequest religionRequest) throws IOException {
        Religion religion = new Religion();

        religion.setName(religionRequest.getName());
        religion.setDescription(religionRequest.getDescription());

        return religionRepository.save(religion);
    }

    public DefaultResponse<Religion> getReligionById(String religionId) throws IOException {
        // Retrieve Religion
        Religion religionResponse = religionRepository.findById(religionId);
        return new DefaultResponse<>(religionResponse.isValid() ? religionResponse : null,
                religionResponse.isValid() ? 1 : 0, "Successfully get data");
    }

    public Religion updateReligion(String religionId, ReligionRequest religionRequest) throws IOException {
        Religion religion = new Religion();
        religion.setName(religionRequest.getName());
        religion.setDescription(religionRequest.getDescription());

        return religionRepository.update(religionId, religion);
    }

    public void deleteReligionById(String religionId) throws IOException {
        Religion religionResponse = religionRepository.findById(religionId);
        if (religionResponse.isValid()) {
            religionRepository.deleteById(religionId);
        } else {
            throw new ResourceNotFoundException("Religion", "id", religionId);
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
