package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.Department;
import com.doyatama.university.model.StudyProgram;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.StudyProgramRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.DepartmentRepository;
import com.doyatama.university.repository.StudyProgramRepository;
import com.doyatama.university.util.AppConstants;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
public class StudyProgramService {
    private StudyProgramRepository studyProgramRepository = new StudyProgramRepository();
    private DepartmentRepository departmentRepository = new DepartmentRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(StudyProgramService.class);

    public PagedResponse<StudyProgram> getAllStudyProgram(int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<StudyProgram> studyProgramResponse = studyProgramRepository.findAll(size);

        return new PagedResponse<>(studyProgramResponse, studyProgramResponse.size(), "Successfully get data", 200);
    }

    public StudyProgram createStudyProgram(StudyProgramRequest studyProgramRequest) throws IOException {
        StudyProgram studyProgram = new StudyProgram();
        Department departmentResponse = departmentRepository
                .findById(studyProgramRequest.getDepartment_id().toString());
        if (departmentResponse.getName() != null) {
            studyProgram.setName(studyProgramRequest.getName());
            studyProgram.setDescription(studyProgramRequest.getDescription());
            studyProgram.setDepartment(departmentResponse);
            return studyProgramRepository.save(studyProgram);
        } else {
            return null;
        }
    }

    public DefaultResponse<StudyProgram> getStudyProgramById(String studyProgramId) throws IOException {
        // Retrieve StudyProgram
        StudyProgram studyProgramResponse = studyProgramRepository.findById(studyProgramId);
        return new DefaultResponse<>(studyProgramResponse.isValid() ? studyProgramResponse : null,
                studyProgramResponse.isValid() ? 1 : 0, "Successfully get data");
    }

    public StudyProgram updateStudyProgram(String studyProgramId, StudyProgramRequest studyProgramRequest)
            throws IOException {
        StudyProgram studyProgram = new StudyProgram();
        Department departmentResponse = departmentRepository
                .findById(studyProgramRequest.getDepartment_id().toString());
        if (departmentResponse.getName() != null) {
            studyProgram.setName(studyProgramRequest.getName());
            studyProgram.setDescription(studyProgramRequest.getDescription());
            studyProgram.setDepartment(departmentResponse);
            return studyProgramRepository.update(studyProgramId, studyProgram);
        } else {
            return null;
        }
    }

    public void deleteStudyProgramById(String departmentId) throws IOException {
        StudyProgram studyProgramResponse = studyProgramRepository.findById(departmentId);
        if (studyProgramResponse.isValid()) {
            studyProgramRepository.deleteById(departmentId);
        } else {
            throw new ResourceNotFoundException("Department", "id", departmentId);
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