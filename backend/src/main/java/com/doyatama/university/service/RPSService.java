package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.*;
import com.doyatama.university.model.RPS;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.RPSRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.*;
import com.doyatama.university.util.AppConstants;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class RPSService {
    private RPSRepository rpsRepository = new RPSRepository();
    private StudyProgramRepository studyProgramRepository = new StudyProgramRepository();
    private LearningMediaRepository learningMediaRepository = new LearningMediaRepository();
    private SubjectRepository subjectRepository = new SubjectRepository();
    private LectureRepository lectureRepository = new LectureRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(RPSService.class);

    public PagedResponse<RPS> getAllRPS(int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<RPS> rpsResponse = rpsRepository.findAll(size);

        return new PagedResponse<>(rpsResponse, rpsResponse.size(), "Successfully get data", 200);
    }

    public List<RPS> importRPSFromExcel(MultipartFile file) throws IOException {
        if (!ExcelUploadService.isValidExcelFile(file)) {
            throw new BadRequestException("Invalid Excel file");
        }

        List<RPS> rpsList = ExcelUploadService.getRPSDataFromExcel(file.getInputStream());
        for (RPS rps : rpsList) {
            rpsRepository.save(rps);
        }
        return rpsList;
    }

    public RPS createRPS(RPSRequest rpsRequest) throws IOException {
        RPS rps = new RPS();

        List<LearningMedia> learningMediaSoftwareList = learningMediaRepository
                .findAllById(rpsRequest.getLearning_media_softwares());
        List<LearningMedia> learningMediaHardwareList = learningMediaRepository
                .findAllById(rpsRequest.getLearning_media_hardwares());
        StudyProgram studyProgramResponse = studyProgramRepository.findById(rpsRequest.getStudy_program_id());
        Subject subjectResponse = subjectRepository.findById(rpsRequest.getSubject_id());
        List<Subject> requirementSubjectList = subjectRepository.findRelationById(rpsRequest.getRequirement_subjects());
        List<Lecture> dev_lecturers = lectureRepository.findRelationById(rpsRequest.getDev_lecturers());
        List<Lecture> teaching_lecturers = lectureRepository.findRelationById(rpsRequest.getTeaching_lecturers());
        List<Lecture> coordinator_lecturers = lectureRepository.findRelationById(rpsRequest.getCoordinator_lecturers());
        Lecture ka_study_program = lectureRepository.findById(rpsRequest.getKa_study_program());

        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        if (learningMediaSoftwareList.size() != 0 &&
                learningMediaHardwareList.size() != 0 &&
                studyProgramResponse.getName() != null &&
                subjectResponse.getName() != null &&
                requirementSubjectList.size() != 0 &&
                dev_lecturers.size() != 0 &&
                teaching_lecturers.size() != 0 &&
                coordinator_lecturers.size() != 0 &&
                ka_study_program.getName() != null) {

            rps.setName(rpsRequest.getName());
            rps.setSks(rpsRequest.getSks());
            rps.setSemester(rpsRequest.getSemester());
            rps.setCpl_prodi(rpsRequest.getCpl_prodi());
            rps.setCpl_mk(rpsRequest.getCpl_mk());
            rps.setLearning_media_softwares(learningMediaSoftwareList);
            rps.setLearning_media_hardwares(learningMediaHardwareList);
            rps.setRequirement_subjects(requirementSubjectList);
            rps.setStudy_program(studyProgramResponse);
            rps.setSubject(subjectResponse);
            rps.setDev_lecturers(dev_lecturers);
            rps.setTeaching_lecturers(teaching_lecturers);
            rps.setCoordinator_lecturers(coordinator_lecturers);
            rps.setKa_study_program(ka_study_program);
            rps.setCreatedAt(instant);

            return rpsRepository.save(rps);
        } else {
            return null;
        }
    }

    public DefaultResponse<RPS> getRPSById(String rpsId) throws IOException {
        // Retrieve RPS
        RPS rpsResponse = rpsRepository.findById(rpsId);
        return new DefaultResponse<>(rpsResponse.isValid() ? rpsResponse : null, rpsResponse.isValid() ? 1 : 0,
                "Successfully get data");
    }

    public RPS updateRPS(String rpsId, RPSRequest rpsRequest) throws IOException {
        RPS rps = new RPS();
        List<LearningMedia> learningMediaSoftwareList = learningMediaRepository
                .findAllById(rpsRequest.getLearning_media_softwares());
        List<LearningMedia> learningMediaHardwareList = learningMediaRepository
                .findAllById(rpsRequest.getLearning_media_hardwares());
        StudyProgram studyProgramResponse = studyProgramRepository.findById(rpsRequest.getStudy_program_id());
        Subject subjectResponse = subjectRepository.findById(rpsRequest.getSubject_id());
        List<Subject> requirementSubjectList = subjectRepository.findRelationById(rpsRequest.getRequirement_subjects());
        List<Lecture> dev_lecturers = lectureRepository.findRelationById(rpsRequest.getDev_lecturers());
        List<Lecture> teaching_lecturers = lectureRepository.findRelationById(rpsRequest.getTeaching_lecturers());
        List<Lecture> coordinator_lecturers = lectureRepository.findRelationById(rpsRequest.getCoordinator_lecturers());
        Lecture ka_study_program = lectureRepository.findById(rpsRequest.getKa_study_program());
        if (learningMediaSoftwareList.size() != 0 &&
                learningMediaHardwareList.size() != 0 &&
                studyProgramResponse.getName() != null &&
                subjectResponse.getName() != null &&
                requirementSubjectList.size() != 0 &&
                dev_lecturers.size() != 0 &&
                teaching_lecturers.size() != 0 &&
                coordinator_lecturers.size() != 0 &&
                ka_study_program.getName() != null) {

            rps.setName(rpsRequest.getName());
            rps.setSks(rpsRequest.getSks());
            rps.setSemester(rpsRequest.getSemester());
            rps.setCpl_prodi(rpsRequest.getCpl_prodi());
            rps.setCpl_mk(rpsRequest.getCpl_mk());
            rps.setLearning_media_softwares(learningMediaSoftwareList);
            rps.setLearning_media_hardwares(learningMediaHardwareList);
            rps.setRequirement_subjects(requirementSubjectList);
            rps.setStudy_program(studyProgramResponse);
            rps.setSubject(subjectResponse);
            rps.setDev_lecturers(dev_lecturers);
            rps.setTeaching_lecturers(teaching_lecturers);
            rps.setCoordinator_lecturers(coordinator_lecturers);
            rps.setKa_study_program(ka_study_program);
            return rpsRepository.update(rpsId, rps);
        } else {
            return null;
        }
    }

    public void deleteRPSById(String rpsId) throws IOException {
        RPS rpsResponse = rpsRepository.findById(rpsId);
        if (rpsResponse.isValid()) {
            rpsRepository.deleteById(rpsId);
        } else {
            throw new ResourceNotFoundException("RPS", "id", rpsId);
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
