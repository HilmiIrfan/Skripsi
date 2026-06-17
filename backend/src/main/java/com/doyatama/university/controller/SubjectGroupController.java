package com.doyatama.university.controller;

import com.doyatama.university.model.SubjectGroup;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.SubjectGroupRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.SubjectGroupService;
import com.doyatama.university.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/subject-group")
public class SubjectGroupController {
    private SubjectGroupService subjectGroupService = new SubjectGroupService();

    @GetMapping
    public PagedResponse<SubjectGroup> getSubjectGroups(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return subjectGroupService.getAllSubjectGroup(page, size);
    }

    @PostMapping
    public ResponseEntity<?> createSubjectGroup(@Valid @RequestBody SubjectGroupRequest subjectGroupRequest) throws IOException {
        SubjectGroup subjectGroup = subjectGroupService.createSubjectGroup(subjectGroupRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{subjectGroupId}")
                .buildAndExpand(subjectGroup.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "SubjectGroup Created Successfully"));
    }

    @GetMapping("/{subjectGroupId}")
    public DefaultResponse<SubjectGroup> getSubjectGroupById(@PathVariable String subjectGroupId) throws IOException {
        return subjectGroupService.getSubjectGroupById(subjectGroupId);
    }


    @PutMapping("/{subjectGroupId}")
    public ResponseEntity<?> updateSubjectGroup(@PathVariable String subjectGroupId,
                                              @Valid @RequestBody SubjectGroupRequest subjectGroupRequest) throws IOException {
        SubjectGroup subjectGroup = subjectGroupService.updateSubjectGroup(subjectGroupId, subjectGroupRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{subjectGroupId}")
                .buildAndExpand(subjectGroup.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "SubjectGroup Updated Successfully"));
    }

    @DeleteMapping("/{subjectGroupId}")
    public HttpStatus deleteSubject(@PathVariable (value = "subjectGroupId") String subjectGroupId) throws IOException {
        subjectGroupService.deleteSubjectGroupById(subjectGroupId);
        return HttpStatus.FORBIDDEN;
    }
}
