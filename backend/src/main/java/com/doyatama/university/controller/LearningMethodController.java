package com.doyatama.university.controller;

import com.doyatama.university.model.LearningMethod;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.LearningMethodRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.LearningMethodService;
import com.doyatama.university.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/learning-method")
public class LearningMethodController {
    private LearningMethodService learningMethodService = new LearningMethodService();

    @GetMapping
    public PagedResponse<LearningMethod> getLearningMethods(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return learningMethodService.getAllLearningMethod(page, size);
    }

    @PostMapping
    public ResponseEntity<?> createLearningMethod(@Valid @RequestBody LearningMethodRequest learningMethodRequest) throws IOException {
        LearningMethod learningMethod = learningMethodService.createLearningMethod(learningMethodRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{learningMethodId}")
                .buildAndExpand(learningMethod.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "LearningMethod Created Successfully"));
    }

    @GetMapping("/{learningMethodId}")
    public DefaultResponse<LearningMethod> getLearningMethodById(@PathVariable String learningMethodId) throws IOException {
        return learningMethodService.getLearningMethodById(learningMethodId);
    }


    @PutMapping("/{learningMethodId}")
    public ResponseEntity<?> updateLearningMethod(@PathVariable String learningMethodId,
                                              @Valid @RequestBody LearningMethodRequest learningMethodRequest) throws IOException {
        LearningMethod learningMethod = learningMethodService.updateLearningMethod(learningMethodId, learningMethodRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{learningMethodId}")
                .buildAndExpand(learningMethod.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "LearningMethod Updated Successfully"));
    }

    @DeleteMapping("/{learningMethodId}")
    public HttpStatus deleteLearningMethod(@PathVariable (value = "learningMethodId") String learningMethodId) throws IOException {
        learningMethodService.deleteLearningMethodById(learningMethodId);
        return HttpStatus.FORBIDDEN;
    }
}
