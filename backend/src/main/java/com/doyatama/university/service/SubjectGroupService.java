package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.SubjectGroup;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.SubjectGroupRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.SubjectGroupRepository;
import com.doyatama.university.util.AppConstants;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
public class SubjectGroupService {
    private SubjectGroupRepository subjectGroupRepository = new SubjectGroupRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(SubjectGroupService.class);

    public PagedResponse<SubjectGroup> getAllSubjectGroup(int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<SubjectGroup> subjectGroupResponse = subjectGroupRepository.findAll(size);

        return new PagedResponse<>(subjectGroupResponse, subjectGroupResponse.size(), "Successfully get data", 200);
    }

    public SubjectGroup createSubjectGroup(SubjectGroupRequest subjectGroupRequest) throws IOException {
        SubjectGroup subjectGroup = new SubjectGroup();

        subjectGroup.setName(subjectGroupRequest.getName());
        subjectGroup.setDescription(subjectGroupRequest.getDescription());

        return subjectGroupRepository.save(subjectGroup);
    }

    public DefaultResponse<SubjectGroup> getSubjectGroupById(String subjectGroupId) throws IOException {
        // Retrieve SubjectGroup
        SubjectGroup subjectGroupResponse = subjectGroupRepository.findById(subjectGroupId);
        return new DefaultResponse<>(subjectGroupResponse.isValid() ? subjectGroupResponse : null,
                subjectGroupResponse.isValid() ? 1 : 0, "Successfully get data");
    }

    public SubjectGroup updateSubjectGroup(String subjectGroupId, SubjectGroupRequest subjectGroupRequest)
            throws IOException {
        SubjectGroup subjectGroup = new SubjectGroup();

        subjectGroup.setName(subjectGroupRequest.getName());
        subjectGroup.setDescription(subjectGroupRequest.getDescription());

        return subjectGroupRepository.update(subjectGroupId, subjectGroup);
    }

    public void deleteSubjectGroupById(String subjectGroupId) throws IOException {
        SubjectGroup subjectGroupResponse = subjectGroupRepository.findById(subjectGroupId);
        if (subjectGroupResponse.isValid()) {
            subjectGroupRepository.deleteById(subjectGroupId);
        } else {
            throw new ResourceNotFoundException("Subject Group", "id", subjectGroupId);
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
