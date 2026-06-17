package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.*;
import com.doyatama.university.model.Exercise;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.ExerciseRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.*;
import com.doyatama.university.util.AppConstants;
import org.springframework.stereotype.Service;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExerciseService {
    private ExerciseRepository exerciseRepository = new ExerciseRepository();
    private QuestionRepository questionRepository = new QuestionRepository();
    private RPSRepository rpsRepository = new RPSRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(ExerciseService.class);

    public PagedResponse<Exercise> getAllExercise(int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<Exercise> exerciseResponse = exerciseRepository.findAll(size);

        return new PagedResponse<>(exerciseResponse, exerciseResponse.size(), "Successfully get data", 200);
    }

    public PagedResponse<Question> getAllQuestionsByRPS(int page, int size, String rpsID, String type_exercise)
            throws IOException {
        validatePageNumberAndSize(page, size);

        // Define the filteredQuestions list
        List<Question> filteredQuestions = new ArrayList<>();

        // Retrieve the questions associated with the given RPS ID
        List<Question> questions = questionRepository.findAllByRPSType(rpsID, type_exercise, size);

        // Inside your for loop where you're iterating over the questions
        for (Question question : questions) {
            // Get the RPSDetail of the current Question
            RPSDetail questionRpsDetail = question.getRps_detail();

            // Check if questionRpsDetail is not null
            if (questionRpsDetail != null) {
                // Get the ID of the RPSDetail
                String questionRpsDetailId = questionRpsDetail.getId();

                // Check if questionRpsDetailId is not null or empty
                if (questionRpsDetailId != null && !questionRpsDetailId.isEmpty()) {

                    // Get the last two characters of the ID
                    String lastTwoChars = questionRpsDetailId.substring(questionRpsDetailId.length() - 2);
                    // Check if the type_exercise of the current question is "Latihan quiz 1"
                    if (type_exercise.equals("Latihan quiz 1")) {

                        // Check if the last two characters are "-1", "-2", "-3", or "-4" and if
                        // ExamType is EXERCISE
                        if ((lastTwoChars.equals("-1") || lastTwoChars.equals("-2") || lastTwoChars.equals("-3")
                                || lastTwoChars.equals("-4")) && question.getExamType() == Question.ExamType.EXERCISE) {
                            // Add the current Question to filteredQuestions
                            filteredQuestions.add(question);
                        }
                    } else if (type_exercise.equals("Latihan quiz 2")) {
                        // add your code for "Latihan quiz 2" here
                        if ((lastTwoChars.equals("-9") || lastTwoChars.equals("10") || lastTwoChars.equals("11")
                                || lastTwoChars.equals("12")) && question.getExamType() == Question.ExamType.EXERCISE) {
                            // Add the current Question to filteredQuestions
                            filteredQuestions.add(question);
                        }
                    } else if (type_exercise.equals("Latihan UTS")) {
                        // add your code for "Latihan quiz 3" here
                        if ((lastTwoChars.equals("-1") || lastTwoChars.equals("-2") || lastTwoChars.equals("-3")
                                || lastTwoChars.equals("-4") || lastTwoChars.equals("-5") || lastTwoChars.equals("-6")
                                || lastTwoChars.equals("-7") || lastTwoChars.equals("-8"))
                                && question.getExamType() == Question.ExamType.EXERCISE) {
                            // Add the current Question to filteredQuestions
                            filteredQuestions.add(question);
                        }
                    } else if (type_exercise.equals("Latihan UAS")) {
                        // add your code for "Latihan quiz 4" here
                    }
                }
            }
        }

        return new PagedResponse<>(filteredQuestions, filteredQuestions.size(), "Successfully get data", 200);
    }

    public Exercise createExercise(ExerciseRequest exerciseRequest) throws IOException {
        Exercise exercise = new Exercise();

        List<Question> questionList = null;
        if (exerciseRequest.getQuestions() != null && !exerciseRequest.getQuestions().isEmpty()) {
            questionList = questionRepository.findAllById(exerciseRequest.getQuestions());
        }
        RPS rpsResponse = rpsRepository.findById(exerciseRequest.getRps_id());

        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        if (rpsResponse.getName() != null) {

            exercise.setName(exerciseRequest.getName());
            exercise.setDescription(exerciseRequest.getDescription());
            exercise.setMin_grade(exerciseRequest.getMin_grade());
            exercise.setDuration(exerciseRequest.getDuration());
            exercise.setDate_start(exerciseRequest.getDate_start());
            exercise.setDate_end(exerciseRequest.getDate_end());
            exercise.setType_exercise(exerciseRequest.getType_exercise());
            exercise.setQuestions(questionList);
            exercise.setRps(rpsResponse);
            exercise.setCreated_at(instant);

            return exerciseRepository.save(exercise);
        } else {
            return null;
        }
    }

    public DefaultResponse<Exercise> getExerciseById(String exerciseId) throws IOException {
        // Retrieve Exercise
        Exercise exerciseResponse = exerciseRepository.findById(exerciseId);
        return new DefaultResponse<>(exerciseResponse.isValid() ? exerciseResponse : null,
                exerciseResponse.isValid() ? 1 : 0, "Successfully get data");
    }

    public Exercise updateExercise(String exerciseId, ExerciseRequest exerciseRequest) throws IOException {
        Exercise exercise = new Exercise();
        List<Question> questionList = questionRepository.findAllById(exerciseRequest.getQuestions());
        RPS rpsResponse = rpsRepository.findById(exerciseRequest.getRps_id());

        if (questionList.size() != 0 && rpsResponse.getName() != null) {
            exercise.setName(exerciseRequest.getName());
            exercise.setDescription(exerciseRequest.getDescription());
            exercise.setMin_grade(exerciseRequest.getMin_grade());
            exercise.setDuration(exerciseRequest.getDuration());
            exercise.setDate_start(exerciseRequest.getDate_start());
            exercise.setDate_end(exerciseRequest.getDate_end());
            exercise.setType_exercise(exerciseRequest.getType_exercise());
            exercise.setQuestions(questionList);
            exercise.setRps(rpsResponse);
            return exerciseRepository.update(exerciseId, exercise);
        } else {
            return null;
        }
    }

    public void deleteExerciseById(String exerciseId) throws IOException {
        Exercise exerciseResponse = exerciseRepository.findById(exerciseId);
        if (exerciseResponse.isValid()) {
            exerciseRepository.deleteById(exerciseId);
        } else {
            throw new ResourceNotFoundException("Exercise", "id", exerciseId);
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
