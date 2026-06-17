package com.doyatama.university.controller;

import com.doyatama.university.model.Exam;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.ExamRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.ExamService;
import com.doyatama.university.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/exam")
public class ExamController {
    private ExamService examService = new ExamService();
    ExamRequest examRequest = new ExamRequest();

    @GetMapping
    public PagedResponse<Exam> getExams(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return examService.getAllExam(page, size, examRequest);
    }

    @PostMapping
    public ResponseEntity<?> createExam(@Valid @RequestBody ExamRequest examRequest) throws IOException {
        Exam exam = examService.createExam(examRequest);

        if (exam == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Please check relational ID"));
        } else {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{examId}")
                    .buildAndExpand(exam.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Exam Created Successfully"));
        }
    }

    @GetMapping("/{examId}")
    public DefaultResponse<Exam> getExamById(@PathVariable String examId) throws IOException {
        return examService.getExamById(examId);
    }

    @PutMapping("/{examId}")
    public ResponseEntity<?> updateExam(@PathVariable String examId,
            @Valid @RequestBody ExamRequest examRequest) throws IOException {
        Exam exam = examService.updateExam(examId, examRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{examId}")
                .buildAndExpand(exam.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Exam Updated Successfully"));
    }

    @DeleteMapping("/{examId}")
    public HttpStatus deleteExam(@PathVariable(value = "examId") String examId) throws IOException {
        examService.deleteExamById(examId);
        return HttpStatus.FORBIDDEN;
    }
}
