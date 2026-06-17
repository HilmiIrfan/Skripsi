package com.doyatama.university.controller;

import com.doyatama.university.model.AppraisalForm;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.AppraisalFormRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.AppraisalFormService;
import com.doyatama.university.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/appraisal-form")
public class AppraisalFormController {
    private AppraisalFormService appraisalFormService = new AppraisalFormService();

    @GetMapping
    public PagedResponse<AppraisalForm> getAppraisalForms(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return appraisalFormService.getAllAppraisalForm(page, size);
    }

    @PostMapping
    public ResponseEntity<?> createAppraisalForm(@Valid @RequestBody AppraisalFormRequest appraisalFormRequest) throws IOException {
        AppraisalForm appraisalForm = appraisalFormService.createAppraisalForm(appraisalFormRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{appraisalFormId}")
                .buildAndExpand(appraisalForm.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "AppraisalForm Created Successfully"));
    }

    @GetMapping("/{appraisalFormId}")
    public DefaultResponse<AppraisalForm> getAppraisalFormById(@PathVariable String appraisalFormId) throws IOException {
        return appraisalFormService.getAppraisalFormById(appraisalFormId);
    }


    @PutMapping("/{appraisalFormId}")
    public ResponseEntity<?> updateAppraisalForm(@PathVariable String appraisalFormId,
                                              @Valid @RequestBody AppraisalFormRequest appraisalFormRequest) throws IOException {
        AppraisalForm appraisalForm = appraisalFormService.updateAppraisalForm(appraisalFormId, appraisalFormRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{appraisalFormId}")
                .buildAndExpand(appraisalForm.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "AppraisalForm Updated Successfully"));
    }

    @DeleteMapping("/{appraisalFormId}")
    public HttpStatus deleteAppraisalForm(@PathVariable (value = "appraisalFormId") String appraisalFormId) throws IOException {
        appraisalFormService.deleteAppraisalFormById(appraisalFormId);
        return HttpStatus.FORBIDDEN;
    }
}
