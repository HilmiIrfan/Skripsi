package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.Question;
import com.doyatama.university.model.Answer;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.AnswerRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.QuestionRepository;
import com.doyatama.university.repository.AnswerRepository;
import com.doyatama.university.util.AppConstants;
import org.springframework.stereotype.Service;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnswerService {
    private AnswerRepository answerRepository = new AnswerRepository();
    private QuestionRepository questionRepository = new QuestionRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(AnswerService.class);

    public PagedResponse<Answer> getAllAnswer(int page, int size, String questionId) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<Answer> answerResponse = new ArrayList<>();

        if (questionId.equalsIgnoreCase("*")) {
            answerResponse = answerRepository.findAll(size);
        } else {
            answerResponse = answerRepository.findAnswerByQuestion(questionId, size);
        }

        return new PagedResponse<>(answerResponse, answerResponse.size(), "Successfully get data", 200);
    }

    public Answer createAnswer(AnswerRequest answerRequest, String savePath) throws IOException {
        Answer answer = new Answer();
        Question questionResponse = questionRepository.findById(answerRequest.getQuestion_id().toString());
        if (questionResponse.getTitle() != null) {
            answer.setTitle(answerRequest.getTitle());
            answer.setDescription(answerRequest.getDescription());
            answer.setType(Answer.AnswerType.valueOf(answerRequest.getType()));
            answer.setIs_right(answerRequest.getIs_right());
            answer.setFile_path(savePath);
            answer.setQuestion(questionResponse);
            return answerRepository.save(answer);
        } else {
            return null;
        }
    }

    public DefaultResponse<Answer> getAnswerById(String answerId) throws IOException {
        // Retrieve Answer
        Answer answerResponse = answerRepository.findById(answerId);
        return new DefaultResponse<>(answerResponse.isValid() ? answerResponse : null, answerResponse.isValid() ? 1 : 0,
                "Successfully get data");
    }

    public Answer updateAnswer(String answerId, AnswerRequest answerRequest, String savePath) throws IOException {
        Answer answer = new Answer();
        Question questionResponse = questionRepository.findById(answerRequest.getQuestion_id().toString());
        if (questionResponse.getTitle() != null) {
            answer.setTitle(answerRequest.getTitle());
            answer.setDescription(answerRequest.getDescription());
            answer.setType(Answer.AnswerType.valueOf(answerRequest.getType()));
            answer.setIs_right(answerRequest.getIs_right());
            answer.setFile_path(savePath);
            answer.setQuestion(questionResponse);
            return answerRepository.update(answerId, answer);
        } else {
            return null;
        }
    }

    public void deleteAnswerById(String answerId) throws IOException {
        Answer answerResponse = answerRepository.findById(answerId);
        if (answerResponse.isValid()) {
            answerRepository.deleteById(answerId);
        } else {
            throw new ResourceNotFoundException("Answer", "id", answerId);
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