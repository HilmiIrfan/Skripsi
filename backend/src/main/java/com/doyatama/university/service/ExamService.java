package com.doyatama.university.service;

// import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.*;
import com.doyatama.university.model.Exam;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.ExamRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.*;
// import com.doyatama.university.util.AppConstants;
import org.springframework.stereotype.Service;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ExamService {
    private ExamRepository examRepository = new ExamRepository();
    private QuestionRepository questionRepository = new QuestionRepository();
    private RPSRepository rpsRepository = new RPSRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(ExamService.class);

    public PagedResponse<Exam> getAllExam(int page, int size, ExamRequest examRequest) throws IOException {
        // Get all exams from the repository
        List<Exam> allExams = examRepository.findAll(size);

        // Return the filtered exams in a PagedResponse object
        return new PagedResponse<>(allExams, allExams.size(), "Successfully get data", 200);
    }

    public Exam createExam(ExamRequest examRequest) throws IOException {
        Exam exam = new Exam();

        List<Question> questionList = null;
        if (examRequest.getQuestions() != null && !examRequest.getQuestions().isEmpty()) {
            questionList = questionRepository.findAllById(examRequest.getQuestions());
        }
        RPS rpsResponse = rpsRepository.findById(examRequest.getRps_id());

        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        if (rpsResponse.getName() != null) {

            exam.setName(examRequest.getName());
            exam.setDescription(examRequest.getDescription());
            exam.setMin_grade(examRequest.getMin_grade());
            exam.setDuration(examRequest.getDuration());
            exam.setDate_start(examRequest.getDate_start());
            exam.setDate_end(examRequest.getDate_end());
            exam.setType_exercise(examRequest.getType_exercise());
            exam.setQuestions(questionList);
            exam.setRps(rpsResponse);
            exam.setCreated_at(instant);

            return examRepository.save(exam);
        } else {
            return null;
        }
    }

    public DefaultResponse<Exam> getExamById(String examId) throws IOException {
        // Retrieve Exam
        Exam examResponse = examRepository.findById(examId);
        return new DefaultResponse<>(examResponse.isValid() ? examResponse : null, examResponse.isValid() ? 1 : 0,
                "Successfully get data");
    }

    public Exam updateExam(String examId, ExamRequest examRequest) throws IOException {
        Exam exam = new Exam();
        List<Question> questionList = questionRepository.findAllById(examRequest.getQuestions());
        RPS rpsResponse = rpsRepository.findById(examRequest.getRps_id());

        if (questionList.size() != 0 && rpsResponse.getName() != null) {
            exam.setName(examRequest.getName());
            exam.setDescription(examRequest.getDescription());
            exam.setMin_grade(examRequest.getMin_grade());
            exam.setDuration(examRequest.getDuration());
            exam.setDate_start(examRequest.getDate_start());
            exam.setDate_end(examRequest.getDate_end());
            exam.setType_exercise(examRequest.getType_exercise());
            exam.setQuestions(questionList);
            exam.setRps(rpsResponse);
            return examRepository.update(examId, exam);
        } else {
            return null;
        }
    }

    public void deleteExamById(String examId) throws IOException {
        Exam examResponse = examRepository.findById(examId);
        if (examResponse.isValid()) {
            examRepository.deleteById(examId);
        } else {
            throw new ResourceNotFoundException("Exam", "id", examId);
        }
    }

    // private void validatePageNumberAndSize(int page, int size) {
    // if (page < 0) {
    // throw new BadRequestException("Page number cannot be less than zero.");
    // }

    // if (size > AppConstants.MAX_PAGE_SIZE) {
    // throw new BadRequestException("Page size must not be greater than " +
    // AppConstants.MAX_PAGE_SIZE);
    // }
    // }

}
