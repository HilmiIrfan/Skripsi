package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.*;
import com.doyatama.university.model.RPSDetail;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.RPSDetailRequest;
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
public class RPSDetailService {
    private RPSDetailRepository rpsDetailRepository = new RPSDetailRepository();
    private RPSRepository rpsRepository = new RPSRepository();
    private FormLearningRepository formLearningRepository = new FormLearningRepository();
    private LearningMethodRepository learningMethodRepository = new LearningMethodRepository();
    private AssessmentCriteriaRepository assessmentCriteriaRepository = new AssessmentCriteriaRepository();
    private AppraisalFormRepository appraisalFormRepository = new AppraisalFormRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(RPSDetailService.class);

    public PagedResponse<RPSDetail> getAllRPSDetail(int page, int size, String rpsID) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve rpsDetail
        List<RPSDetail> rpsDetailResponse;

        if (rpsID.equalsIgnoreCase("*")) {
            rpsDetailResponse = rpsDetailRepository.findAll(size);
        } else {
            rpsDetailResponse = rpsDetailRepository.findByRpsID(rpsID, size);
        }

        return new PagedResponse<>(rpsDetailResponse, rpsDetailResponse.size(), "Successfully get data", 200);
    }

    public List<RPSDetail> importRPSDetailFromExcel(MultipartFile file) throws IOException {
        if (!ExcelUploadService.isValidExcelFile(file)) {
            throw new BadRequestException("Invalid Excel file");
        }

        List<RPSDetail> rpsDetailList = ExcelUploadService.getRPSDetailDataFromExcel(file.getInputStream());
        for (RPSDetail rpsDetail : rpsDetailList) {
            rpsDetailRepository.save(rpsDetail);
        }
        return rpsDetailList;
    }

    public RPSDetail createRPSDetail(RPSDetailRequest rpsDetailRequest) throws IOException {
        RPSDetail rpsDetail = new RPSDetail();

        RPS rpsResponse = rpsRepository.findById(rpsDetailRequest.getRps_id());
        FormLearning formLearningResponse = formLearningRepository.findById(rpsDetailRequest.getForm_learning_id());
        List<LearningMethod> learningMethodList = learningMethodRepository
                .findAllById(rpsDetailRequest.getLearning_methods());
        List<AssessmentCriteria> assessmentCriteriaList = assessmentCriteriaRepository
                .findAllById(rpsDetailRequest.getAssessment_criterias());
        List<AppraisalForm> appraisalFormList = appraisalFormRepository
                .findAllById(rpsDetailRequest.getAppraisal_forms());

        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        if (rpsResponse.getName() != null &&
                formLearningResponse.getName() != null &&
                learningMethodList.size() != 0 &&
                assessmentCriteriaList.size() != 0 &&
                appraisalFormList.size() != 0) {

            rpsDetail.setWeek(rpsDetailRequest.getWeek());
            rpsDetail.setRps(rpsResponse);
            rpsDetail.setSub_cp_mk(rpsDetailRequest.getSub_cp_mk());
            rpsDetail.setLearning_materials(rpsDetailRequest.getLearning_materials());
            rpsDetail.setForm_learning(formLearningResponse);
            rpsDetail.setLearning_methods(learningMethodList);
            rpsDetail.setAssignments(rpsDetailRequest.getAssignments());
            rpsDetail.setEstimated_times(rpsDetailRequest.getEstimated_times());
            rpsDetail.setStudent_learning_experiences(rpsDetailRequest.getStudent_learning_experiences());
            rpsDetail.setAssessment_criterias(assessmentCriteriaList);
            rpsDetail.setAppraisal_forms(appraisalFormList);
            rpsDetail.setAssessment_indicators(rpsDetailRequest.getAssessment_indicators());
            rpsDetail.setWeight(rpsDetailRequest.getWeight());
            rpsDetail.setCreatedAt(instant);

            return rpsDetailRepository.save(rpsDetail);
        } else {
            return null;
        }
    }

    public DefaultResponse<RPSDetail> getRPSDetailById(String rpsDetailId) throws IOException {
        // Retrieve RPSDetail
        RPSDetail rpsDetailResponse = rpsDetailRepository.findById(rpsDetailId);
        return new DefaultResponse<>(rpsDetailResponse.isValid() ? rpsDetailResponse : null,
                rpsDetailResponse.isValid() ? 1 : 0, "Successfully get data");
    }

    public RPSDetail updateRPSDetail(String rpsDetailId, RPSDetailRequest rpsDetailRequest) throws IOException {
        RPSDetail rpsDetail = new RPSDetail();
        RPS rpsResponse = rpsRepository.findById(rpsDetailRequest.getRps_id());
        FormLearning formLearningResponse = formLearningRepository.findById(rpsDetailRequest.getForm_learning_id());
        List<LearningMethod> learningMethodList = learningMethodRepository
                .findAllById(rpsDetailRequest.getLearning_methods());
        List<AssessmentCriteria> assessmentCriteriaList = assessmentCriteriaRepository
                .findAllById(rpsDetailRequest.getAssessment_criterias());
        List<AppraisalForm> appraisalFormList = appraisalFormRepository
                .findAllById(rpsDetailRequest.getAppraisal_forms());

        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        if (rpsResponse.getName() != null &&
                formLearningResponse.getName() != null &&
                learningMethodList.size() != 0 &&
                assessmentCriteriaList.size() != 0 &&
                appraisalFormList.size() != 0) {
            rpsDetail.setWeek(rpsDetailRequest.getWeek());
            rpsDetail.setRps(rpsResponse);
            rpsDetail.setSub_cp_mk(rpsDetailRequest.getSub_cp_mk());
            rpsDetail.setLearning_materials(rpsDetailRequest.getLearning_materials());
            rpsDetail.setForm_learning(formLearningResponse);
            rpsDetail.setLearning_methods(learningMethodList);
            rpsDetail.setAssignments(rpsDetailRequest.getAssignments());
            rpsDetail.setEstimated_times(rpsDetailRequest.getEstimated_times());
            rpsDetail.setStudent_learning_experiences(rpsDetailRequest.getStudent_learning_experiences());
            rpsDetail.setAssessment_criterias(assessmentCriteriaList);
            rpsDetail.setAppraisal_forms(appraisalFormList);
            rpsDetail.setAssessment_indicators(rpsDetailRequest.getAssessment_indicators());
            rpsDetail.setWeight(rpsDetailRequest.getWeight());
            rpsDetail.setCreatedAt(instant);

            return rpsDetailRepository.save(rpsDetail);
        } else {
            return null;
        }
    }

    public void deleteRPSDetailById(String rpsDetailId) throws IOException {
        RPSDetail rpsDetailResponse = rpsDetailRepository.findById(rpsDetailId);
        if (rpsDetailResponse.isValid()) {
            rpsDetailRepository.deleteById(rpsDetailId);
        } else {
            throw new ResourceNotFoundException("RPSDetail", "id", rpsDetailId);
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
