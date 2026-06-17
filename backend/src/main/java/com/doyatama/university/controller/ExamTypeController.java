package com.doyatama.university.controller;

import com.doyatama.university.model.ExamType;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.ExamTypeRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.ExamTypeService;
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
@RequestMapping("/api/exam-type")
public class ExamTypeController {
    private ExamTypeService examTypeService = new ExamTypeService();

    @GetMapping
    public PagedResponse<ExamType> getExamTypes(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return examTypeService.getAllExamTypes(page, size);
    }

    @PostMapping
    public ResponseEntity<?> createExamType(@Valid @RequestBody ExamTypeRequest examTypeRequest) throws IOException {
        ExamType examType = examTypeService.createExamType(examTypeRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{examTypeId}")
                .buildAndExpand(examType.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "ExamType Created Successfully"));
    }

    @GetMapping("/{examTypeId}")
    public DefaultResponse<ExamType> getExamTypeById(@PathVariable String examTypeId) throws IOException {
        return examTypeService.getExamTypeById(examTypeId);
    }

    @PutMapping("/{examTypeId}")
    public ResponseEntity<?> updateExamType(@PathVariable String examTypeId,
                                            @Valid @RequestBody ExamTypeRequest examTypeRequest) throws IOException {
        ExamType examType = examTypeService.updateExamType(examTypeId, examTypeRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{examTypeId}")
                .buildAndExpand(examType.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "ExamType Updated Successfully"));
    }

    @DeleteMapping("/{examTypeId}")
    public HttpStatus deleteExamType(@PathVariable (value = "examTypeId") String examTypeId) throws IOException {
        examTypeService.deleteExamTypeById(examTypeId);
        return HttpStatus.FORBIDDEN;
    }
}