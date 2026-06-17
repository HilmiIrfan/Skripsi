package com.doyatama.university.service;

import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.*;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.QuizAnnouncementRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.*;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * @author alfa
 */
@Service
public class QuizAnnouncementService {
    private QuizAnnouncementRepository quizAnnouncementRepository = new QuizAnnouncementRepository();

    private QuestionRepository questionRepository = new QuestionRepository();
    private RPSRepository rpsRepository = new RPSRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(QuizAnnouncementService.class);

    public PagedResponse<QuizAnnouncement> getAllQuizAnnouncements(int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve QuizAnnouncements
        List<QuizAnnouncement> quizAnnouncements = quizAnnouncementRepository.findAll(size);

        return new PagedResponse<>(quizAnnouncements, quizAnnouncements.size(), "Successfully retrieved data", 200);
    }

    public QuizAnnouncement createQuizAnnouncement(QuizAnnouncementRequest quizAnnouncementRequest) throws IOException {
        QuizAnnouncement quizAnnouncement = new QuizAnnouncement();

        List<Question> questionList = null;
        if (quizAnnouncementRequest.getType_quiz().equals("quiz1")) {
            // Define the filteredQuestions list
            List<Question> filteredQuestions = new ArrayList<>();

            // Retrieve the questions associated with the given RPS ID
            List<Question> questions = questionRepository.findAllByRPS(quizAnnouncementRequest.getRps_id(),
                    quizAnnouncementRequest.getSizeQuestion());

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

                        // Check if the last two characters are "-1", "-2", "-3", or "-4" and if
                        // ExamType is QUIZ
                        if ((lastTwoChars.equals("-1") || lastTwoChars.equals("-2") || lastTwoChars.equals("-3")
                                || lastTwoChars.equals("-4")) && question.getExamType2() == Question.ExamType2.QUIZ) {
                            // Add the current Question to filteredQuestions
                            filteredQuestions.add(question);
                        }
                    }
                }
            }

            questionList = filteredQuestions;
        } else {
            // Define the filteredQuestions list
            List<Question> filteredQuestions = new ArrayList<>();

            // Retrieve the questions associated with the given RPS ID
            List<Question> questions = questionRepository.findAllByRPS(quizAnnouncementRequest.getRps_id(),
                    quizAnnouncementRequest.getSizeQuestion());

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

                        // Check if the last two characters are "-1", "-2", "-3", or "-4" and if
                        // ExamType is EXERCISE
                        if ((lastTwoChars.equals("-9") || lastTwoChars.equals("10") || lastTwoChars.equals("11")
                                || lastTwoChars.equals("12")) && question.getExamType2() == Question.ExamType2.QUIZ) {
                            // Add the current Question to filteredQuestions
                            filteredQuestions.add(question);
                        }
                    }
                }
            }

            questionList = filteredQuestions;
        }

        RPS rpsResponse = rpsRepository.findById(quizAnnouncementRequest.getRps_id());

        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        if (rpsResponse.getName() != null) {
            quizAnnouncement.setName(quizAnnouncementRequest.getName());
            quizAnnouncement.setDescription(quizAnnouncementRequest.getDescription());
            quizAnnouncement.setMin_grade(quizAnnouncementRequest.getMin_grade());
            quizAnnouncement.setDuration(quizAnnouncementRequest.getDuration());
            quizAnnouncement.setDate_start(quizAnnouncementRequest.getDate_start());
            quizAnnouncement.setDate_end(quizAnnouncementRequest.getDate_end());
            quizAnnouncement.setType_quiz(quizAnnouncementRequest.getType_quiz());
            quizAnnouncement.setQuestions(questionList);
            quizAnnouncement.setRps(rpsResponse);
            quizAnnouncement.setCreated_at(instant);

            return quizAnnouncementRepository.save(quizAnnouncement);
        } else {
            return null;
        }
    }

    public DefaultResponse<QuizAnnouncement> getQuizAnnouncementById(String quizAnnouncementId) throws IOException {
        // Retrieve QuizAnnouncement
        QuizAnnouncement quizAnnouncementResponse = quizAnnouncementRepository.findById(quizAnnouncementId);
        boolean isValid = quizAnnouncementResponse != null && quizAnnouncementResponse.isValid();
        return new DefaultResponse<>(isValid ? quizAnnouncementResponse : null, isValid ? 1 : 0,
                "Successfully retrieved data");
    }

    public QuizAnnouncement updateQuizAnnouncement(String quizAnnouncementId,
            QuizAnnouncementRequest quizAnnouncementRequest) throws IOException {
        QuizAnnouncement quizAnnouncement = quizAnnouncementRepository.findById(quizAnnouncementId);
        if (quizAnnouncement == null) {
            throw new IOException("QuizAnnouncement not found");
        }

        List<Question> questionList = questionRepository.findAllById(quizAnnouncementRequest.getQuestions());
        RPS rpsResponse = rpsRepository.findById(quizAnnouncementRequest.getRps_id());

        if (questionList.size() != 0 && rpsResponse.getName() != null) {
            quizAnnouncement.setName(quizAnnouncementRequest.getName());
            quizAnnouncement.setDescription(quizAnnouncementRequest.getDescription());
            quizAnnouncement.setMin_grade(quizAnnouncementRequest.getMin_grade());
            quizAnnouncement.setDuration(quizAnnouncementRequest.getDuration());
            quizAnnouncement.setDate_start(quizAnnouncementRequest.getDate_start());
            quizAnnouncement.setDate_end(quizAnnouncementRequest.getDate_end());
            quizAnnouncement.setType_quiz(quizAnnouncementRequest.getType_quiz());
            quizAnnouncement.setQuestions(questionList);
            quizAnnouncement.setRps(rpsResponse);
            return quizAnnouncementRepository.update(quizAnnouncementId, quizAnnouncement);
        } else {
            return null;
        }
    }

    public void deleteQuizAnnouncementById(String quizAnnouncementId) throws IOException {
        QuizAnnouncement quizAnnouncementResponse = quizAnnouncementRepository.findById(quizAnnouncementId);
        if (quizAnnouncementResponse != null && quizAnnouncementResponse.isValid()) {
            quizAnnouncementRepository.deleteById(quizAnnouncementId);
        } else {
            throw new ResourceNotFoundException("QuizAnnouncement", "id", quizAnnouncementId);
        }
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be less than zero.");
        }

        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than zero.");
        }
    }
}
