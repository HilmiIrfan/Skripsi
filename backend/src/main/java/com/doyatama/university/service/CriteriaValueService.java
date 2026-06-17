package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.model.*;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.CriteriaValueRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.*;
import com.doyatama.university.util.AppConstants;
import org.springframework.stereotype.Service;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author alfa
 */
@Service

public class CriteriaValueService {
    private CriteriaValueRepository criteriaValueRepository = new CriteriaValueRepository();
    private LinguisticValueRepository linguisticValueRepository = new LinguisticValueRepository();

    private QuizAnnouncementRepository quizAnnouncementRepository = new QuizAnnouncementRepository();
    // private UserRepository userRepository = new UserRepository();

    QuestionService questionService = new QuestionService();

    // private static final Logger logger =
    // LoggerFactory.getLogger(CriteriaValueService.class);

    // public PagedResponse<CriteriaValue> getAllCriteriaValue(int page, int size)
    // throws IOException {
    // validatePageNumberAndSize(page, size);
    //
    // // Retrieve CriteriaValue
    // List<CriteriaValue> criteriaValueResponse =
    // criteriaValueRepository.findAll(size);
    //
    // // Retrieve all Questions
    // PagedResponse<Question> questionResponse =
    // questionService.getAllQuestion(page, size, "*", "*");
    //
    // // TODO: Use the questionResponse as needed
    //
    // return new PagedResponse<>(criteriaValueResponse,
    // criteriaValueResponse.size(), "Successfully get data", 200);
    // }

    public PagedResponse<CriteriaValue> getAllCriteriaValueByQuestion(String questionId, int page, int size)
            throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve CriteriaValue by questionId
        List<CriteriaValue> criteriaValueResponse = criteriaValueRepository.findAllByQuestion(questionId, size);
        return new PagedResponse<>(criteriaValueResponse, criteriaValueResponse.size(), "Successfully get data", 200);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    public CriteriaValue createCriteriaValue(CriteriaValueRequest criteriaValueRequest, String questionId)
            throws IOException {
        CriteriaValue criteriaValue = new CriteriaValue();
        QuestionRepository questionRepository = new QuestionRepository();
        // TeamTeachingRepository teamTeachingRepository = new TeamTeachingRepository();

        Question questionResponse = questionRepository.findById(criteriaValueRequest.getQuestionId().toString());
        // TeamTeaching teamTeaching =
        // teamTeachingRepository.findById(criteriaValueRequest.getTeamTeachingId());

        LinguisticValue value1 = linguisticValueRepository.findById(criteriaValueRequest.getValue1());
        LinguisticValue value2 = linguisticValueRepository.findById(criteriaValueRequest.getValue2());
        LinguisticValue value3 = linguisticValueRepository.findById(criteriaValueRequest.getValue3());
        LinguisticValue value4 = linguisticValueRepository.findById(criteriaValueRequest.getValue4());
        LinguisticValue value5 = linguisticValueRepository.findById(criteriaValueRequest.getValue5());
        LinguisticValue value6 = linguisticValueRepository.findById(criteriaValueRequest.getValue6());
        LinguisticValue value7 = linguisticValueRepository.findById(criteriaValueRequest.getValue7());
        LinguisticValue value8 = linguisticValueRepository.findById(criteriaValueRequest.getValue8());
        LinguisticValue value9 = linguisticValueRepository.findById(criteriaValueRequest.getValue9());

        if (value1.getName() != null &&
                value2.getName() != null &&
                value3.getName() != null &&
                value4.getName() != null &&
                value5.getName() != null &&
                value6.getName() != null &&
                value7.getName() != null &&
                value8.getName() != null &&
                value9.getName() != null) {

            criteriaValue.setQuestion(questionResponse);
            criteriaValue.setUser(criteriaValueRequest.getUser_id());
            // criteriaValue.setTeamTeaching(teamTeaching);
            criteriaValue.setValue1(value1);
            criteriaValue.setValue2(value2);
            criteriaValue.setValue3(value3);
            criteriaValue.setValue4(value4);
            criteriaValue.setValue5(value5);
            criteriaValue.setValue6(value6);
            criteriaValue.setValue7(value7);
            criteriaValue.setValue8(value8);
            criteriaValue.setValue9(value9);
            return criteriaValueRepository.save(criteriaValue, questionId);
        }
        return null;
    }

    public PagedResponse<CriteriaValue> getQuestionsWithCriteriaValuesFromQuizAnnouncement(String quizAnnouncementId,
            int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        QuizAnnouncement quizAnnouncement = quizAnnouncementRepository.findById(quizAnnouncementId);
        if (quizAnnouncement == null) {
            throw new IOException("QuizAnnouncement not found with id: " + quizAnnouncementId);
        }

        // Assuming you have a JSON string or a Map that needs to be converted to a List
        // of Questions
        ObjectMapper objectMapper = new ObjectMapper();
        List<Question> questions = objectMapper.convertValue(quizAnnouncement.getQuestions(),
                new TypeReference<List<Question>>() {
                });

        // Debugging: Print the size and contents of questions
        System.out.println("Questions size: " + questions.size());
        for (Question question : questions) {
            System.out.println("Question ID: " + question.getId());
        }

        // Now you can proceed with your existing logic
        List<CriteriaValue> criteriaValueResponse = new ArrayList<>();

        for (Question question : questions) {
            List<CriteriaValue> criteriaValues = criteriaValueRepository.findAllByQuestion(question.getId(),
                    Integer.MAX_VALUE);

            // Debugging: Print the size and contents of criteriaValues
            System.out.println("CriteriaValues size for question " + question.getId() + ": " + criteriaValues.size());
            for (CriteriaValue criteriaValue : criteriaValues) {
                System.out.println("CriteriaValue ID: " + criteriaValue.getId());
            }

            question.setCriteriaValues(criteriaValues);
            criteriaValueResponse.addAll(criteriaValues);

            // Calculate the average of value1.getAverage()
            double sum = 0;
            for (CriteriaValue criteriaValue : criteriaValues) {
                sum += Float.parseFloat(criteriaValue.getValue1().getAverage());
            }
            double average = criteriaValues.isEmpty() ? 0 : sum / criteriaValues.size();
            question.setAverageValue1(average);

            // Calculate the average of value1.getAverage()
            double sum2 = 0;
            for (CriteriaValue criteriaValue : criteriaValues) {
                sum2 += Float.parseFloat(criteriaValue.getValue1().getAverage());
            }
            double average2 = criteriaValues.isEmpty() ? 0 : sum2 / criteriaValues.size();
            question.setAverageValue1(average2);

            // Calculate the average of value1.getAverage()
            double sum3 = 0;
            for (CriteriaValue criteriaValue : criteriaValues) {
                sum3 += Float.parseFloat(criteriaValue.getValue1().getAverage());
            }
            double average3 = criteriaValues.isEmpty() ? 0 : sum3 / criteriaValues.size();
            question.setAverageValue1(average3);
        }

        return new PagedResponse<>(criteriaValueResponse, criteriaValueResponse.size(), "Successfully get data", 200);
    }

    public DefaultResponse<CriteriaValue> getCriteriaValueById(String criteriaValueId) throws IOException {
        // Retrieve CriteriaValue
        CriteriaValue criteriaValueResponse = criteriaValueRepository.findById(criteriaValueId);
        return new DefaultResponse<>(criteriaValueResponse.isValid() ? criteriaValueResponse : null,
                criteriaValueResponse.isValid() ? 1 : 0, "Successfully get data");
    }

    // public CriteriaValue updateCriteriaValue(String criteriaValueId,
    // CriteriaValueRequest criteriaValueRequest) throws IOException {
    // // Create a new CriteriaValue object
    // CriteriaValue criteriaValue = new CriteriaValue();
    //
    // // Create instances of the repositories
    // LinguisticValueRepository linguisticValueRepository = new
    // LinguisticValueRepository();
    // QuestionRepository questionRepository = new QuestionRepository();
    // RPSDetailRepository rpsDetailRepository = new RPSDetailRepository();
    // TeamTeachingRepository teamTeachingRepository = new TeamTeachingRepository();
    //
    // // Retrieve the objects from the repositories
    // LinguisticValue linguisticValue =
    // linguisticValueRepository.findById(criteriaValueRequest.getLinguisticValueId());
    // Question question =
    // questionRepository.findById(criteriaValueRequest.getQuestion());
    // RPSDetail rpsDetail =
    // rpsDetailRepository.findById(criteriaValueRequest.getRpsDetailId());
    // TeamTeaching teamTeaching =
    // teamTeachingRepository.findById(criteriaValueRequest.getTeamTeachingId());
    //
    // // Set the objects in CriteriaValue
    // criteriaValue.setLinguisticValue(linguisticValue);
    // criteriaValue.setQuestion(question);
    // criteriaValue.setRPSDetail(rpsDetail);
    // criteriaValue.setTeamTeaching(teamTeaching);
    //
    // return criteriaValueRepository.save(criteriaValue);
    // }

    public void deleteCriteriaValueById(String criteriaValueId) throws IOException {
        // CriteriaValue criteriaValueResponse =
        // criteriaValueRepository.findById(criteriaValueId);

        criteriaValueRepository.deleteById(criteriaValueId);

    }
}
