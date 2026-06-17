package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.*;
import com.doyatama.university.model.ExerciseAttempt;
import com.doyatama.university.payload.ExerciseAttemptRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.*;
import com.doyatama.university.util.AppConstants;
import org.springframework.stereotype.Service;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.*;
import java.util.List;

@Service
public class ExerciseAttemptService {
    private ExerciseAttemptRepository exerciseAttemptRepository;
    private AnswerRepository answerRepository = new AnswerRepository();
    private ExerciseRepository exerciseRepository = new ExerciseRepository();
    private UserRepository userRepository = new UserRepository();
    private StudentRepository studentRepository = new StudentRepository();

    private final QuestionRepository questionRepository;

    public ExerciseAttemptService(ExerciseAttemptRepository exerciseAttemptRepository,
            QuestionRepository questionRepository) {
        this.exerciseAttemptRepository = exerciseAttemptRepository;
        this.questionRepository = questionRepository;
    }

    // private static final Logger logger =
    // LoggerFactory.getLogger(ExerciseAttemptService.class);

    public PagedResponse<ExerciseAttempt> getAllExerciseAttempt(int page, int size, String userID, String ExerciseID)
            throws IOException {
        validatePageNumberAndSize(page, size);

        List<ExerciseAttempt> exerciseAttemptResponse = new ArrayList<>();

        if (userID.equalsIgnoreCase("*") && ExerciseID.equalsIgnoreCase("*"))
            exerciseAttemptResponse = exerciseAttemptRepository.findAll(size);
        if (!userID.equalsIgnoreCase("*") && ExerciseID.equalsIgnoreCase("*"))
            exerciseAttemptResponse = exerciseAttemptRepository.findByUser(userID, size);
        if (userID.equalsIgnoreCase("*") && !ExerciseID.equalsIgnoreCase("*"))
            exerciseAttemptResponse = exerciseAttemptRepository.findByExercise(ExerciseID, size);

        return new PagedResponse<>(exerciseAttemptResponse, exerciseAttemptResponse.size(), "Successfully get data",
                200);
    }

    public PagedResponse<ExerciseAttempt> getExerciseAttemptById(String exerciseAttemptId, int page, int size)
            throws IOException {
        validatePageNumberAndSize(page, size); // We only need one page since we're retrieving by ID

        List<ExerciseAttempt> exerciseAttemptResponse = new ArrayList<>();

        if (!exerciseAttemptId.equalsIgnoreCase("*")) {
            exerciseAttemptResponse = exerciseAttemptRepository.findByExerciseAttemptId(exerciseAttemptId, size);
        }

        return new PagedResponse<>(exerciseAttemptResponse, exerciseAttemptResponse.size(), "Successfully get data",
                200);
    }

    public ExerciseAttempt createExerciseAttempt(ExerciseAttemptRequest exerciseAttemptRequest) throws IOException {
        ExerciseAttempt exerciseAttempt = new ExerciseAttempt();

        List<Answer> studentAnswerList = answerRepository.findAllById(exerciseAttemptRequest.getStudentAnswers());
        Exercise exerciseResponse = exerciseRepository.findById(exerciseAttemptRequest.getExercise_id());
        User userResponse = userRepository.findById(exerciseAttemptRequest.getUser_id());
        Student studentResponse = studentRepository.findById(exerciseAttemptRequest.getStudent_id());

        if (studentAnswerList.size() != 0 && exerciseResponse.getName() != null && userResponse.getName() != null
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

            int minGrade = exerciseResponse.getMin_grade();

            if (percentage >= minGrade) {
                exerciseAttempt.setState("Lulus");
            } else {
                exerciseAttempt.setState("Tidak Lulus");
            }

            exerciseAttempt.setGrade(Double.parseDouble(formattedPercentage));
            exerciseAttempt.setTotal_right(correctAnswers);
            exerciseAttempt.setDuration(exerciseAttemptRequest.getDuration());
            exerciseAttempt.setExercise(exerciseResponse);
            exerciseAttempt.setUser(userResponse);
            exerciseAttempt.setStudent(studentResponse);
            exerciseAttempt.setStudent_answers(results);

            return exerciseAttemptRepository.save(exerciseAttempt);
        } else {
            return null;
        }
    }

    public List<String> getQuestionExplanations(ExerciseAttempt exerciseAttempt) throws IOException {
        List<String> explanations = new ArrayList<>();
        for (StudentAnswer studentAnswer : exerciseAttempt.getStudentAnswers()) {
            String questionId = studentAnswer.getQuestion().getId();
            Question question = questionRepository.findById(questionId);
            if (question != null) {
                String explanation = question.getExplanation();
                explanations.add(explanation);
            }
        }
        return explanations;
    }

    public PagedResponse<ExerciseAttempt> getExerciseAttemptById(String exerciseAttemptId) throws IOException {
        // Retrieve ExerciseAttempt
        ExerciseAttempt exerciseAttemptResponse = exerciseAttemptRepository.findById(exerciseAttemptId);

        // Get explanations
        List<String> explanations = getQuestionExplanations(exerciseAttemptResponse);
        exerciseAttemptResponse.setExplanations(explanations);

        List<ExerciseAttempt> exerciseAttempts = Collections.singletonList(exerciseAttemptResponse);

        long totalElements = exerciseAttempts.size();
        String message = "Successfully get data";
        long statusCode = exerciseAttemptResponse != null ? 200 : 404;

        return new PagedResponse<>(exerciseAttempts, totalElements, message, statusCode);
    }

    public ExerciseAttempt updateExerciseAttempt(String exerciseAttemptId,
            ExerciseAttemptRequest exerciseAttemptRequest) {
        // ExerciseAttempt exerciseAttempt = new ExerciseAttempt();
        // List<Question> questionList =
        // questionRepository.findAllById(exerciseAttemptRequest.getQuestions());
        // RPS rpsResponse = rpsRepository.findById(exerciseAttemptRequest.getRps_id());
        //
        // if (questionList.size() != 0 && rpsResponse.getName() != null) {
        // exerciseAttempt.setName(exerciseAttemptRequest.getName());
        // exerciseAttempt.setDescription(exerciseAttemptRequest.getDescription());
        // exerciseAttempt.setDuration(exerciseAttemptRequest.getDuration());
        // exerciseAttempt.setDate_start(exerciseAttemptRequest.getDate_start());
        // exerciseAttempt.setDate_end(exerciseAttemptRequest.getDate_end());
        // exerciseAttempt.setQuestions(questionList);
        // exerciseAttempt.setRps(rpsResponse);
        // return exerciseAttemptRepository.update(exerciseAttemptId, exerciseAttempt);
        // } else {
        return null;
        // }
    }

    public void deleteExerciseAttemptById(String exerciseAttemptId) throws IOException {
        ExerciseAttempt exerciseAttemptResponse = exerciseAttemptRepository.findById(exerciseAttemptId);
        if (exerciseAttemptResponse.isValid()) {
            exerciseAttemptRepository.deleteById(exerciseAttemptId);
        } else {
            throw new ResourceNotFoundException("ExerciseAttempt", "id", exerciseAttemptId);
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
