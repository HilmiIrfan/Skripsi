package com.doyatama.university.controller;

import com.doyatama.university.model.ExamAttempt;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.ExamAttemptRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.ExamAttemptService;
import com.doyatama.university.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/exam-attempt")
public class ExamAttemptController {
    private ExamAttemptService examAttemptService = new ExamAttemptService();

    @GetMapping
    public PagedResponse<ExamAttempt> getExamAttempts(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "userID", defaultValue = "*") String userID,
            @RequestParam(value = "examID", defaultValue = "*") String examID) throws IOException {
        return examAttemptService.getAllExamAttempt(page, size, userID, examID);
    }

    @PostMapping
    public ResponseEntity<?> createExamAttempt(@Valid @RequestBody ExamAttemptRequest examAttemptRequest)
            throws IOException {
        ExamAttempt examAttempt = examAttemptService.createExamAttempt(examAttemptRequest);

        if (examAttempt == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Please check relational ID"));
        } else {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{examAttemptId}")
                    .buildAndExpand(examAttempt.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "ExamAttempt Created Successfully"));
        }
    }

    @GetMapping("/{examAttemptId}")
    public DefaultResponse<ExamAttempt> getExamAttemptById(@PathVariable String examAttemptId) throws IOException {
        return examAttemptService.getExamAttemptById(examAttemptId);
    }

    @PutMapping("/{examAttemptId}")
    public ResponseEntity<?> updateExamAttempt(@PathVariable String examAttemptId,
            @Valid @RequestBody ExamAttemptRequest examAttemptRequest) throws IOException {
        ExamAttempt examAttempt = examAttemptService.updateExamAttempt(examAttemptId, examAttemptRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{examAttemptId}")
                .buildAndExpand(examAttempt.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "ExamAttempt Updated Successfully"));
    }

    @DeleteMapping("/{examAttemptId}")
    public HttpStatus deleteExamAttempt(@PathVariable(value = "examAttemptId") String examAttemptId)
            throws IOException {
        examAttemptService.deleteExamAttemptById(examAttemptId);
        return HttpStatus.FORBIDDEN;
    }
}
