package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.*;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.QuestionRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.RPSDetailRepository;
import com.doyatama.university.repository.QuestionRepository;
import com.doyatama.university.util.AppConstants;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

@Service
public class QuestionService {
    private QuestionRepository questionRepository = new QuestionRepository();
    private RPSDetailRepository rpsDetailRepository = new RPSDetailRepository();

    // private ExamTypeRepository examTypeRepository = new ExamTypeRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(QuestionService.class);

    public PagedResponse<Question> getAllQuestion(int page, int size, String rpsDetailID, String rpsID)
            throws IOException {
        validatePageNumberAndSize(page, size);

        List<Question> questionResponse = new ArrayList<>();

        // jika semua *
        if (rpsDetailID.equalsIgnoreCase("*") && rpsID.equalsIgnoreCase("*"))
            questionResponse = questionRepository.findAll(size);

        // jika rpsDetail ada isinya
        if (!rpsDetailID.equalsIgnoreCase("*") && rpsID.equalsIgnoreCase("*"))
            questionResponse = questionRepository.findAllByRPSDetail(rpsDetailID, size);

        // jika rps ada isinya
        if (rpsDetailID.equalsIgnoreCase("*") && !rpsID.equalsIgnoreCase("*"))
            questionResponse = questionRepository.findAllByRPS(rpsID, size);

        // Retrieve Polls

        return new PagedResponse<>(questionResponse, questionResponse.size(), "Successfully get data", 200);
    }

    public PagedResponse<Question> getAllQuestionsByRPS(int page, int size, String rpsID) throws IOException {
        validatePageNumberAndSize(page, size);

        List<Question> questionResponse = new ArrayList<>();

        // if rpsID is "*"
        if (rpsID.equalsIgnoreCase("*")) {
            questionResponse = questionRepository.findAll(size);
        }

        // if rpsID is not "*"
        if (!rpsID.equalsIgnoreCase("*")) {
            questionResponse = questionRepository.findAllByRPS(rpsID, size);
        }

        return new PagedResponse<>(questionResponse, questionResponse.size(), "Successfully get data", 200);
    }

    // public List<Question> getQuestionsByExerciseAttempt(int size, String
    // exerciseAttemptId) throws IOException {
    // // Retrieve ExerciseAttempt by its ID
    // ExerciseAttempt exerciseAttempt =
    // exerciseAttemptRepository.findById(exerciseAttemptId);

    // // Get the list of StudentAnswer objects from the ExerciseAttempt
    // List<StudentAnswer> studentAnswers = exerciseAttempt.getStudent_answers();

    // List<Question> questionResponse = new ArrayList<>();

    // // For each StudentAnswer, retrieve the corresponding Question
    // for (StudentAnswer studentAnswer : studentAnswers) {
    // // Retrieve question ID from a student answer
    // String questionId = studentAnswer.getQuestion().getId();

    // // Use the question ID to find the question
    // Question question = questionRepository.findById(questionId);

    // // Add the question to the response list
    // questionResponse.add(question);
    // }

    // return questionResponse;
    // }

    public Question createQuestion(QuestionRequest questionRequest, String savePath) throws IOException {
        Question question = new Question();

        RPSDetail rpsDetailResponse = rpsDetailRepository.findById(questionRequest.getRps_detail_id().toString());

        if (rpsDetailResponse.getSub_cp_mk() != null) {
            question.setTitle(questionRequest.getTitle());
            question.setDescription(questionRequest.getDescription());
            question.setExplanation(questionRequest.getExplanation());
            question.setQuestionType(Question.QuestionType.valueOf(questionRequest.getQuestion_type()));
            // question.setAnswerType(Question.AnswerType.valueOf(questionRequest.getAnswer_type()));
            if (questionRequest.getAnswer_type() != null && !questionRequest.getAnswer_type().isEmpty()) {
                question.setAnswerType(Question.AnswerType.valueOf(questionRequest.getAnswer_type()));
            } else {
                question.setAnswerType(Question.AnswerType.MULTIPLE_CHOICE);
            }
            question.setFile_path(savePath.isEmpty() ? null : savePath);
            if (questionRequest.getExamType() != null && !questionRequest.getExamType().isEmpty()) {
                question.setExamType(Question.ExamType.valueOf(questionRequest.getExamType()));
            } else {
                question.setExamType(null);
            }

            if (questionRequest.getExamType2() != null && !questionRequest.getExamType2().isEmpty()) {
                question.setExamType2(Question.ExamType2.valueOf(questionRequest.getExamType2()));
            } else {
                question.setExamType2(null);
            }

            if (questionRequest.getExamType3() != null && !questionRequest.getExamType3().isEmpty()) {
                question.setExamType3(Question.ExamType3.valueOf(questionRequest.getExamType3()));
            } else {
                question.setExamType3(null);
            }
            question.setRps_detail(rpsDetailResponse);
            return questionRepository.save(question);
        } else {
            return null;
        }
    }

    public DefaultResponse<Question> getQuestionById(String questionId) throws IOException {
        // Retrieve Question
        Question questionResponse = questionRepository.findById(questionId);
        return new DefaultResponse<>(questionResponse.isValid() ? questionResponse : null,
                questionResponse.isValid() ? 1 : 0, "Successfully get data");
    }

    public PagedResponse<Question> getQuestionByIdPaged(String questionId) throws IOException {
        // Retrieve Question
        Question questionResponse = questionRepository.findById(questionId);
        List<Question> questions = Collections.singletonList(questionResponse);

        long totalElements = questions.size();
        String message = "Successfully get data";
        long statusCode = questionResponse != null && questionResponse.isValid() ? 200 : 404;

        return new PagedResponse<>(questions, totalElements, message, statusCode);
    }

    public Question updateQuestion(String questionId, QuestionRequest questionRequest) throws IOException {
        Question question = questionRepository.findById(questionId);

        if (question != null) {
            RPSDetail rpsDetailResponse = rpsDetailRepository.findById(questionRequest.getRps_detail_id().toString());
            if (rpsDetailResponse.getSub_cp_mk() != null) {
                question.setExplanation(questionRequest.getExplanation());
                question.setTitle(questionRequest.getTitle());
                question.setDescription(questionRequest.getDescription());
                question.setQuestionType(Question.QuestionType.valueOf(questionRequest.getQuestion_type()));
                question.setAnswerType(Question.AnswerType.valueOf(questionRequest.getAnswer_type()));

                // Handle examType as an array and take the first value
                // String[] examTypes = questionRequest.getExamType();
                // if (examTypes != null && examTypes.length > 0) {
                // question.setExamType(Question.ExamType.valueOf(examTypes[0]));
                // }

                question.setExamType2(Question.ExamType2.valueOf(questionRequest.getExamType2()));
                question.setExamType3(Question.ExamType3.valueOf(questionRequest.getExamType3()));
                question.setRps_detail(rpsDetailResponse);
                return questionRepository.update(questionId, question);
            } else {
                return null;
            }
        } else {
            throw new ResourceNotFoundException("Question", "id", questionId);
        }
    }

    public void deleteQuestionById(String rpsDetailId) throws IOException {
        Question questionResponse = questionRepository.findById(rpsDetailId);
        if (questionResponse.isValid()) {
            questionRepository.deleteById(rpsDetailId);
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