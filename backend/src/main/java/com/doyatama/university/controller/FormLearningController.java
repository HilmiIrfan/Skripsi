package com.doyatama.university.controller;

import com.doyatama.university.model.FormLearning;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.FormLearningRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.FormLearningService;
import com.doyatama.university.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/form-learning")
public class FormLearningController {
    private FormLearningService formLearningService = new FormLearningService();

    @GetMapping
    public PagedResponse<FormLearning> getFormLearnings(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return formLearningService.getAllFormLearning(page, size);
    }

    @PostMapping
    public ResponseEntity<?> createFormLearning(@Valid @RequestBody FormLearningRequest formLearningRequest) throws IOException {
        FormLearning formLearning = formLearningService.createFormLearning(formLearningRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{formLearningId}")
                .buildAndExpand(formLearning.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "FormLearning Created Successfully"));
    }

    @GetMapping("/{formLearningId}")
    public DefaultResponse<FormLearning> getFormLearningById(@PathVariable String formLearningId) throws IOException {
        return formLearningService.getFormLearningById(formLearningId);
    }


    @PutMapping("/{formLearningId}")
    public ResponseEntity<?> updateFormLearning(@PathVariable String formLearningId,
                                              @Valid @RequestBody FormLearningRequest formLearningRequest) throws IOException {
        FormLearning formLearning = formLearningService.updateFormLearning(formLearningId, formLearningRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{formLearningId}")
                .buildAndExpand(formLearning.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "FormLearning Updated Successfully"));
    }

    @DeleteMapping("/{formLearningId}")
    public HttpStatus deleteFormLearning(@PathVariable (value = "formLearningId") String formLearningId) throws IOException {
        formLearningService.deleteFormLearningById(formLearningId);
        return HttpStatus.FORBIDDEN;
    }
}
