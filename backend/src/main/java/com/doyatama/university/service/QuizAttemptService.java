package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.*;
import com.doyatama.university.model.QuizAttempt;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.QuizAttemptRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.*;
import com.doyatama.university.util.AppConstants;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class QuizAttemptService {
    private QuizAttemptRepository quizAttemptRepository = new QuizAttemptRepository();
    private AnswerRepository answerRepository = new AnswerRepository();
    private QuizRepository quizRepository = new QuizRepository();
    private UserRepository userRepository = new UserRepository();
    private StudentRepository studentRepository = new StudentRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(QuizAttemptService.class);

    public PagedResponse<QuizAttempt> getAllQuizAttempt(int page, int size, String userID, String quizID)
            throws IOException {
        validatePageNumberAndSize(page, size);

        List<QuizAttempt> quizAttemptResponse = new ArrayList<>();

        if (userID.equalsIgnoreCase("*") && quizID.equalsIgnoreCase("*"))
            quizAttemptResponse = quizAttemptRepository.findAll(size);
        if (!userID.equalsIgnoreCase("*") && quizID.equalsIgnoreCase("*"))
            quizAttemptResponse = quizAttemptRepository.findByUser(userID, size);
        if (userID.equalsIgnoreCase("*") && !quizID.equalsIgnoreCase("*"))
            quizAttemptResponse = quizAttemptRepository.findByQuiz(quizID, size);

        return new PagedResponse<>(quizAttemptResponse, quizAttemptResponse.size(), "Successfully get data", 200);
    }

    public QuizAttempt createQuizAttempt(QuizAttemptRequest quizAttemptRequest) throws IOException {
        QuizAttempt quizAttempt = new QuizAttempt();

        List<Answer> studentAnswerList = answerRepository.findAllById(quizAttemptRequest.getStudentAnswers());
        Quiz quizResponse = quizRepository.findById(quizAttemptRequest.getQuiz_id());
        User userResponse = userRepository.findById(quizAttemptRequest.getUser_id());
        Student studentResponse = studentRepository.findById(quizAttemptRequest.getStudent_id());

        if (studentAnswerList.size() != 0 && quizResponse.getName() != null && userResponse.getName() != null
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

            int minGrade = quizResponse.getMin_grade();

            if (percentage >= minGrade) {
                quizAttempt.setState("Lulus");
            } else {
                quizAttempt.setState("Tidak Lulus");
            }

            quizAttempt.setGrade(Double.parseDouble(formattedPercentage));
            quizAttempt.setTotal_right(correctAnswers);
            quizAttempt.setDuration(quizAttemptRequest.getDuration());
            quizAttempt.setQuiz(quizResponse);
            quizAttempt.setUser(userResponse);
            quizAttempt.setStudent(studentResponse);
            quizAttempt.setStudent_answers(results);

            return quizAttemptRepository.save(quizAttempt);
        } else {
            return null;
        }
    }

    public DefaultResponse<QuizAttempt> getQuizAttemptById(String quizAttemptId) throws IOException {
        // Retrieve QuizAttempt
        QuizAttempt quizAttemptResponse = quizAttemptRepository.findById(quizAttemptId);
        return new DefaultResponse<>(quizAttemptResponse.isValid() ? quizAttemptResponse : null,
                quizAttemptResponse.isValid() ? 1 : 0, "Successfully get data");
    }

    public QuizAttempt updateQuizAttempt(String quizAttemptId, QuizAttemptRequest quizAttemptRequest)
            throws IOException {
        // QuizAttempt quizAttempt = new QuizAttempt();
        // List<Question> questionList =
        // questionRepository.findAllById(quizAttemptRequest.getQuestions());
        // RPS rpsResponse = rpsRepository.findById(quizAttemptRequest.getRps_id());
        //
        // if (questionList.size() != 0 && rpsResponse.getName() != null) {
        // quizAttempt.setName(quizAttemptRequest.getName());
        // quizAttempt.setDescription(quizAttemptRequest.getDescription());
        // quizAttempt.setDuration(quizAttemptRequest.getDuration());
        // quizAttempt.setDate_start(quizAttemptRequest.getDate_start());
        // quizAttempt.setDate_end(quizAttemptRequest.getDate_end());
        // quizAttempt.setQuestions(questionList);
        // quizAttempt.setRps(rpsResponse);
        // return quizAttemptRepository.update(quizAttemptId, quizAttempt);
        // } else {
        return null;
        // }
    }

    public void deleteQuizAttemptById(String quizAttemptId) throws IOException {
        QuizAttempt quizAttemptResponse = quizAttemptRepository.findById(quizAttemptId);
        if (quizAttemptResponse.isValid()) {
            quizAttemptRepository.deleteById(quizAttemptId);
        } else {
            throw new ResourceNotFoundException("QuizAttempt", "id", quizAttemptId);
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
