package com.doyatama.university.controller;

import com.doyatama.university.model.QuestionCriteria;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.QuestionCriteriaRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.QuestionCriteriaService;
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
@RequestMapping("/api/question-criteria")
public class QuestionCriteriaController {
    private QuestionCriteriaService questionCriteriaService = new QuestionCriteriaService();

    @GetMapping
    public PagedResponse<QuestionCriteria> getQuestionCriteria(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                              @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return questionCriteriaService.getAllQuestionCriteria(page,size);
    }

    @PostMapping
    public ResponseEntity<?> createQuestionCriteria(@Valid @RequestBody QuestionCriteriaRequest questionCriteriaRequest) throws IOException {
        QuestionCriteria questionCriteria = questionCriteriaService.createQuestionCriteria(questionCriteriaRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{questionCriteriaId}")
                .buildAndExpand(questionCriteria.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Question Criteria Created Successfully"));
    }

    @GetMapping("/{questionCriteriaId}")
    public DefaultResponse<QuestionCriteria> getQuestionCriteriaById(@PathVariable String questionCriteriaId) throws IOException {
        return questionCriteriaService.getQuestionCriteriaById(questionCriteriaId);
    }
    @PutMapping("/{questionCriteriaId}")
    public ResponseEntity<?> updateQuestionCriteria(@PathVariable String questionCriteriaId,
                                          @Valid @RequestBody QuestionCriteriaRequest questionCriteriaRequest) throws IOException {
        QuestionCriteria questionCriteria = questionCriteriaService.updateQuestionCriteria(questionCriteriaId, questionCriteriaRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{questionCriteriaId}")
                .buildAndExpand(questionCriteria.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Question Criteria Updated Successfully"));
    }
    @DeleteMapping("/{questionCriteriaId}")
    public HttpStatus deleteQuestionCriteria(@PathVariable (value = "questionCriteriaId") String questionCriteriaId) throws IOException {
        questionCriteriaService.deleteQuestionCriteriaById(questionCriteriaId);
        return HttpStatus.OK;
    }
}
