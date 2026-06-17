package com.doyatama.university.controller;

import com.doyatama.university.model.Subject;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.SubjectRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.SubjectService;
import com.doyatama.university.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/subject")
public class SubjectController {
    private SubjectService subjectService = new SubjectService();

    @GetMapping
    public PagedResponse<Subject> getSubjects(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return subjectService.getAllSubject(page, size);
    }

    @PostMapping
    public ResponseEntity<?> createSubject(@Valid @RequestBody SubjectRequest subjectRequest) throws IOException {
        Subject subject = subjectService.createSubject(subjectRequest);

        if (subject == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Please check relational ID"));
        } else {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{subjectId}")
                    .buildAndExpand(subject.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Subject Created Successfully"));
        }
    }

    @GetMapping("/{subjectId}")
    public DefaultResponse<Subject> getSubjectById(@PathVariable String subjectId) throws IOException {
        return subjectService.getSubjectById(subjectId);
    }

    @PutMapping("/{subjectId}")
    public ResponseEntity<?> updateSubject(@PathVariable String subjectId,
            @Valid @RequestBody SubjectRequest subjectRequest) throws IOException {
        Subject subject = subjectService.updateSubject(subjectId, subjectRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{subjectId}")
                .buildAndExpand(subject.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Subject Updated Successfully"));
    }

    @DeleteMapping("/{subjectId}")
    public HttpStatus deleteSubject(@PathVariable(value = "subjectId") String subjectId) throws IOException {
        subjectService.deleteSubjectById(subjectId);
        return HttpStatus.FORBIDDEN;
    }
}
