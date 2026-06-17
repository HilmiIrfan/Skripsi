package com.doyatama.university.controller;

import com.doyatama.university.model.AssessmentCriteria;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.AssessmentCriteriaRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.AssessmentCriteriaService;
import com.doyatama.university.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/assessment-criteria")
public class AssessmentCriteriaController {
    private AssessmentCriteriaService assessmentCriteriaService = new AssessmentCriteriaService();

    @GetMapping
    public PagedResponse<AssessmentCriteria> getAssessmentCriterias(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return assessmentCriteriaService.getAllAssessmentCriteria(page, size);
    }

    @PostMapping
    public ResponseEntity<?> createAssessmentCriteria(@Valid @RequestBody AssessmentCriteriaRequest assessmentCriteriaRequest) throws IOException {
        AssessmentCriteria assessmentCriteria = assessmentCriteriaService.createAssessmentCriteria(assessmentCriteriaRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{assessmentCriteriaId}")
                .buildAndExpand(assessmentCriteria.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "AssessmentCriteria Created Successfully"));
    }

    @GetMapping("/{assessmentCriteriaId}")
    public DefaultResponse<AssessmentCriteria> getAssessmentCriteriaById(@PathVariable String assessmentCriteriaId) throws IOException {
        return assessmentCriteriaService.getAssessmentCriteriaById(assessmentCriteriaId);
    }


    @PutMapping("/{assessmentCriteriaId}")
    public ResponseEntity<?> updateAssessmentCriteria(@PathVariable String assessmentCriteriaId,
                                              @Valid @RequestBody AssessmentCriteriaRequest assessmentCriteriaRequest) throws IOException {
        AssessmentCriteria assessmentCriteria = assessmentCriteriaService.updateAssessmentCriteria(assessmentCriteriaId, assessmentCriteriaRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{assessmentCriteriaId}")
                .buildAndExpand(assessmentCriteria.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "AssessmentCriteria Updated Successfully"));
    }

    @DeleteMapping("/{assessmentCriteriaId}")
    public HttpStatus deleteAssessmentCriteria(@PathVariable (value = "assessmentCriteriaId") String assessmentCriteriaId) throws IOException {
        assessmentCriteriaService.deleteAssessmentCriteriaById(assessmentCriteriaId);
        return HttpStatus.FORBIDDEN;
    }
}
