package com.doyatama.university.controller;

import com.doyatama.university.model.CriteriaValue;
import com.doyatama.university.model.Question;
import com.doyatama.university.model.RPS;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.CriteriaValueRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.CriteriaValueService;
import com.doyatama.university.service.QuestionService;
import com.doyatama.university.service.RPSService;

import com.doyatama.university.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

/**
 * @author alfa
 */
@RestController
@RequestMapping("/api/criteria-value")
public class CriteriaValueController {
    private CriteriaValueService criteriaValueService = new CriteriaValueService();
    private RPSService rpsService = new RPSService();
    private QuestionService questionService = new QuestionService(); // Add this line

    @GetMapping
    public PagedResponse<RPS> getRPSs(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return rpsService.getAllRPS(page, size); // Call getAllRPS on the instance
    }

    @GetMapping("/questions")
    public PagedResponse<Question> getAllQuestionsByRPS(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam("rpsID") String rpsID) throws IOException {
        return questionService.getAllQuestionsByRPS(page, size, rpsID);
    }

    @GetMapping("/question/{questionId}")
    public PagedResponse<CriteriaValue> getAllCriteriaValueByQuestion(@PathVariable String questionId,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return criteriaValueService.getAllCriteriaValueByQuestion(questionId, page, size);
    }

    @GetMapping("/quizAnnouncement/{quizAnnouncementId}")
    public PagedResponse<CriteriaValue> getQuestionsWithCriteriaValuesFromQuizAnnouncement(
            @PathVariable String quizAnnouncementId,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return criteriaValueService.getQuestionsWithCriteriaValuesFromQuizAnnouncement(quizAnnouncementId, page, size);
    }

    @PostMapping("/{questionId}")
    public ResponseEntity<?> createCriteriaValue(@Valid @RequestBody CriteriaValueRequest criteriaValueRequest,
            @PathVariable String questionId) throws IOException {
        CriteriaValue criteriaValue = criteriaValueService.createCriteriaValue(criteriaValueRequest, questionId);

        if (criteriaValue == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Linguistic Value ID not found"));
        } else {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{criteriaValueId}")
                    .buildAndExpand(criteriaValue.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "CriteriaValue Created Successfully"));
        }
    }

    @GetMapping("/{criteriaValueId}")
    public DefaultResponse<CriteriaValue> getCriteriaValueById(@PathVariable String criteriaValueId)
            throws IOException {
        return criteriaValueService.getCriteriaValueById(criteriaValueId);
    }

    // @PutMapping("/{criteriaValueId}")
    // public ResponseEntity<?> updateCriteriaValue(@PathVariable String
    // criteriaValueId,
    // @Valid @RequestBody CriteriaValueRequest criteriaValueRequest) throws
    // IOException {
    // CriteriaValue criteriaValue =
    // criteriaValueService.updateCriteriaValue(criteriaValueId,
    // criteriaValueRequest);
    //
    // URI location = ServletUriComponentsBuilder
    // .fromCurrentRequest().path("/{criteriaValueId}")
    // .buildAndExpand(criteriaValue.getId()).toUri();
    //
    // return ResponseEntity.created(location)
    // .body(new ApiResponse(true, "CriteriaValue Updated Successfully"));
    // }

    @DeleteMapping("/{criteriaValueId}")
    public HttpStatus deleteCriteriaValue(@PathVariable(value = "criteriaValueId") String criteriaValueId)
            throws IOException {
        criteriaValueService.deleteCriteriaValueById(criteriaValueId);
        return HttpStatus.FORBIDDEN;
    }

}
