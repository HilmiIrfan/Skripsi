package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.*;
import com.doyatama.university.model.ExamAttempt;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.ExamAttemptRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.*;
import com.doyatama.university.util.AppConstants;
import org.springframework.stereotype.Service;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ExamAttemptService {
    private ExamAttemptRepository examAttemptRepository = new ExamAttemptRepository();
    private AnswerRepository answerRepository = new AnswerRepository();
    private ExamRepository examRepository = new ExamRepository();
    private UserRepository userRepository = new UserRepository();
    private StudentRepository studentRepository = new StudentRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(ExamAttemptService.class);

    public PagedResponse<ExamAttempt> getAllExamAttempt(int page, int size, String userID, String examID)
            throws IOException {
        validatePageNumberAndSize(page, size);

        List<ExamAttempt> examAttemptResponse = new ArrayList<>();

        if (userID.equalsIgnoreCase("*") && examID.equalsIgnoreCase("*"))
            examAttemptResponse = examAttemptRepository.findAll(size);
        if (!userID.equalsIgnoreCase("*") && examID.equalsIgnoreCase("*"))
            examAttemptResponse = examAttemptRepository.findByUser(userID, size);
        if (userID.equalsIgnoreCase("*") && !examID.equalsIgnoreCase("*"))
            examAttemptResponse = examAttemptRepository.findByExam(examID, size);

        return new PagedResponse<>(examAttemptResponse, examAttemptResponse.size(), "Successfully get data", 200);
    }

    public ExamAttempt createExamAttempt(ExamAttemptRequest examAttemptRequest) throws IOException {
        ExamAttempt examAttempt = new ExamAttempt();

        List<Answer> studentAnswerList = answerRepository.findAllById(examAttemptRequest.getStudentAnswers());
        Exam examResponse = examRepository.findById(examAttemptRequest.getExam_id());
        User userResponse = userRepository.findById(examAttemptRequest.getUser_id());
        Student studentResponse = studentRepository.findById(examAttemptRequest.getStudent_id());

        if (studentAnswerList.size() != 0 && examResponse.getName() != null && userResponse.getName() != null
                && studentResponse.getName() != null) {
            List<StudentAnswer> results = new ArrayList<>();

            for (Answer answer : studentAnswerList) {
                Boolean isRight = answer.getIs_right();
                StudentAnswer result = new StudentAnswer();
                result.setId(UUID.randomUUID().toString());
                result.setQuestion(answer.getQuestion());
                result.setAnswer(answer);
                result.setScore(isRight != null && isRight ? 1 : 0);
                result.setCreated_at(Instant.now());
                results.add(result);
            }

            int totalQuestions = results.size();
            int correctAnswers = 0;

            for (StudentAnswer result : results) {
                if (result.getScore() == 1) {
                    correctAnswers++;
                }
            }

            double percentage = (double) correctAnswers / totalQuestions * 100;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String formattedPercentage = decimalFormat.format(percentage);

            int minGrade = examResponse.getMin_grade();

            if (percentage >= minGrade) {
                examAttempt.setState("Lulus");
            } else {
                examAttempt.setState("Tidak Lulus");
            }

            examAttempt.setGrade(Double.parseDouble(formattedPercentage));
            examAttempt.setTotal_right(correctAnswers);
            examAttempt.setDuration(examAttemptRequest.getDuration());
            examAttempt.setExam(examResponse);
            examAttempt.setUser(userResponse);
            examAttempt.setStudent(studentResponse);
            examAttempt.setStudent_answers(results);

            return examAttemptRepository.save(examAttempt);
        } else {
            return null;
        }
    }

    public DefaultResponse<ExamAttempt> getExamAttemptById(String examAttemptId) throws IOException {
        // Retrieve ExamAttempt
        ExamAttempt examAttemptResponse = examAttemptRepository.findById(examAttemptId);
        return new DefaultResponse<>(examAttemptResponse.isValid() ? examAttemptResponse : null,
                examAttemptResponse.isValid() ? 1 : 0, "Successfully get data");
    }

    public ExamAttempt updateExamAttempt(String examAttemptId, ExamAttemptRequest examAttemptRequest)
            throws IOException {
        // ExamAttempt examAttempt = new ExamAttempt();
        // List<Question> questionList =
        // questionRepository.findAllById(examAttemptRequest.getQuestions());
        // RPS rpsResponse = rpsRepository.findById(examAttemptRequest.getRps_id());
        //
        // if (questionList.size() != 0 && rpsResponse.getName() != null) {
        // examAttempt.setName(examAttemptRequest.getName());
        // examAttempt.setDescription(examAttemptRequest.getDescription());
        // examAttempt.setDuration(examAttemptRequest.getDuration());
        // examAttempt.setDate_start(examAttemptRequest.getDate_start());
        // examAttempt.setDate_end(examAttemptRequest.getDate_end());
        // examAttempt.setQuestions(questionList);
        // examAttempt.setRps(rpsResponse);
        // return examAttemptRepository.update(examAttemptId, examAttempt);
        // } else {
        return null;
        // }
    }

    public void deleteExamAttemptById(String examAttemptId) throws IOException {
        ExamAttempt examAttemptResponse = examAttemptRepository.findById(examAttemptId);
        if (examAttemptResponse.isValid()) {
            examAttemptRepository.deleteById(examAttemptId);
        } else {
            throw new ResourceNotFoundException("ExamAttempt", "id", examAttemptId);
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
