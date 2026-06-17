package com.doyatama.university.controller;

import com.doyatama.university.model.School;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.SchoolRequest;
import com.doyatama.university.security.CurrentUser;
import com.doyatama.university.security.UserPrincipal;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.SchoolService;
import com.doyatama.university.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/school")
public class SchoolController {
    private SchoolService schoolService = new SchoolService();

    @GetMapping
    public PagedResponse<School> getSchools(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @CurrentUser UserPrincipal currentUser) throws IOException {

        String schoolID = currentUser.getSchoolId();
        // Cek jika admin tidak memiliki schoolID (null, kosong, atau "-")
        if (schoolID == null || schoolID.isEmpty() || schoolID.equals("-")) {
            // Jika admin tidak memiliki sekolah, tampilkan semua user
            return schoolService.getAllSchool(page, size, "*");
        }

        return schoolService.getAllSchool(page, size, schoolID);
    }

    @PostMapping
    public ResponseEntity<?> createSchool(@Valid @RequestBody SchoolRequest schoolRequest) throws IOException {
        School school = schoolService.createSchool(schoolRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{schoolId}")
                .buildAndExpand(school.getIdSchool()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "School Created Successfully"));
    }

    @GetMapping("/{schoolId}")
    public DefaultResponse<School> getSchoolById(@PathVariable String schoolId) throws IOException {
        return schoolService.getSchoolById(schoolId);
    }

    @PutMapping("/{schoolId}")
    public ResponseEntity<?> updateSchool(@PathVariable String schoolId,
            @Valid @RequestBody SchoolRequest schoolRequest) throws IOException {
        School school = schoolService.updateSchool(schoolId, schoolRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{schoolId}")
                .buildAndExpand(school.getIdSchool()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "School Updated Successfully"));
    }

    @DeleteMapping("/{schoolId}")
    public HttpStatus deleteSchool(@PathVariable(value = "schoolId") String schoolId) throws IOException {
        schoolService.deleteSchoolById(schoolId);
        return HttpStatus.FORBIDDEN;
    }
}
