package com.doyatama.university.controller;

import com.doyatama.university.model.Semester;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.SemesterRequest;
import com.doyatama.university.security.CurrentUser;
import com.doyatama.university.security.UserPrincipal;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.SemesterService;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/semester")
public class SemesterController {
    private SemesterService semesterService = new SemesterService();

    @GetMapping
    public PagedResponse<Semester> getSemester(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @CurrentUser UserPrincipal currentUser) throws IOException {

        String schoolID = currentUser.getSchoolId();

        return semesterService.getAllSemester(page, size, schoolID);
    }

    @PostMapping
    public ResponseEntity<?> createSemester(@Valid @RequestBody SemesterRequest semesterRequest) throws IOException {
        try {
            Semester semester = semesterService.createSemester(semesterRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{semesterId}")
                    .buildAndExpand(semester.getIdSemester()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Semester Created Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }
    }

    @GetMapping("/{semesterId}")
    public DefaultResponse<Semester> getSemesterById(@PathVariable String semesterId) throws IOException {
        return semesterService.getSemesterById(semesterId);
    }

    @PutMapping("/{semesterId}")
    public ResponseEntity<?> updateSemester(@PathVariable String semesterId,
            @Valid @RequestBody SemesterRequest semesterRequest) throws IOException {
        Semester semester = semesterService.updateSemester(semesterId, semesterRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{semesterId}")
                .buildAndExpand(semester.getIdSemester()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Semester Updated Successfully"));
    }

    @DeleteMapping("/{semesterId}")
    public ResponseEntity<?> deleteSemester(@PathVariable String semesterId) throws IOException {
        semesterService.deleteSemesterById(semesterId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(true, "Semester Deleted Successfully"));
    }
}
