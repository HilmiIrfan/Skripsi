package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.StudyProgram;
import com.doyatama.university.model.Subject;
import com.doyatama.university.model.SubjectGroup;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.SubjectRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.StudyProgramRepository;
import com.doyatama.university.repository.SubjectGroupRepository;
import com.doyatama.university.repository.SubjectRepository;
import com.doyatama.university.util.AppConstants;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
public class SubjectService {
    private SubjectRepository subjectRepository = new SubjectRepository();
    private StudyProgramRepository studyProgramRepository = new StudyProgramRepository();
    private SubjectGroupRepository subjectGroupRepository = new SubjectGroupRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(SubjectService.class);

    public PagedResponse<Subject> getAllSubject(int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<Subject> subjectResponse = subjectRepository.findAll(size);

        return new PagedResponse<>(subjectResponse, subjectResponse.size(), "Successfully get data", 200);
    }

    public Subject createSubject(SubjectRequest subjectRequest) throws IOException {
        Subject subject = new Subject();
        System.out.println(subjectRequest.getStudy_program_id());
        StudyProgram studyProgramResponse = studyProgramRepository.findById(subjectRequest.getStudy_program_id());
        SubjectGroup subjectGroupResponse = subjectGroupRepository.findById(subjectRequest.getSubject_group_id());
        if (studyProgramResponse.getName() != null && subjectGroupResponse.getName() != null) {
            subject.setName(subjectRequest.getName());
            subject.setDescription(subjectRequest.getDescription());
            subject.setCredit_point(subjectRequest.getCredit_point());
            subject.setYear_commenced(subjectRequest.getYear_commenced());
            subject.setStudyProgram(studyProgramResponse);
            subject.setSubject_group(subjectGroupResponse);
            return subjectRepository.save(subject);
        } else {
            return null;
        }
    }

    public DefaultResponse<Subject> getSubjectById(String subjectId) throws IOException {
        // Retrieve Subject
        Subject subjectResponse = subjectRepository.findById(subjectId);
        return new DefaultResponse<>(subjectResponse.isValid() ? subjectResponse : null,
                subjectResponse.isValid() ? 1 : 0, "Successfully get data");
    }

    public Subject updateSubject(String subjectId, SubjectRequest subjectRequest) throws IOException {
        Subject subject = new Subject();
        StudyProgram studyProgramResponse = studyProgramRepository
                .findById(subjectRequest.getStudy_program_id().toString());
        SubjectGroup subjectGroupResponse = subjectGroupRepository
                .findById(subjectRequest.getSubject_group_id().toString());
        if (studyProgramResponse.getName() != null && subjectGroupResponse.getName() != null) {
            subject.setName(subjectRequest.getName());
            subject.setDescription(subjectRequest.getDescription());
            subject.setCredit_point(subjectRequest.getCredit_point());
            subject.setYear_commenced(subjectRequest.getYear_commenced());
            subject.setStudyProgram(studyProgramResponse);
            subject.setSubject_group(subjectGroupResponse);
            return subjectRepository.update(subjectId, subject);
        } else {
            return null;
        }
    }

    public void deleteSubjectById(String subjectId) throws IOException {
        Subject subjectResponse = subjectRepository.findById(subjectId);
        if (subjectResponse.isValid()) {
            subjectRepository.deleteById(subjectId);
        } else {
            throw new ResourceNotFoundException("Subject", "id", subjectId);
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
