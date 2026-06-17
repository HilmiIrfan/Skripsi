package com.doyatama.university.controller;

import com.doyatama.university.model.JenisSoal;
import com.doyatama.university.model.Semester;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.JenisSoalRequest;
import com.doyatama.university.security.CurrentUser;
import com.doyatama.university.security.UserPrincipal;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.payload.SemesterRequest;
import com.doyatama.university.service.JenisSoalService;
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
@RequestMapping("/api/jenis-soal")
public class JenisSoalController {

    private JenisSoalService jenisSoalService = new JenisSoalService();

    @GetMapping
    public PagedResponse<JenisSoal> getJenisSoal(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @CurrentUser UserPrincipal currentUser) throws IOException {

        String schoolID = currentUser.getSchoolId();

        return jenisSoalService.getAllJenisSoal(page, size, schoolID);
    }

    @PostMapping
    public ResponseEntity<?> createJenisSoal(@Valid @RequestBody JenisSoalRequest jenisSoalRequest)
            throws IOException {
        try {
            JenisSoal jenisSoal = jenisSoalService.createJenisSoal(jenisSoalRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{jenisSoalId}")
                    .buildAndExpand(jenisSoal.getIdJenisSoal()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Jenis Soal Created Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }
    }

    @GetMapping("/{jenisSoalId}")
    public DefaultResponse<JenisSoal> getJenisSoalById(@PathVariable String jenisSoalId) throws IOException {
        return jenisSoalService.getJenisSoalById(jenisSoalId);
    };

    @PutMapping("/{jenisSoalId}")
    public ResponseEntity<?> updateJenisSoal(@PathVariable String jenisSoalId,
            @Valid @RequestBody JenisSoalRequest jenisSoalRequest) throws IOException {
        try {
            JenisSoal jenisSoal = jenisSoalService.updateJenisSoal(jenisSoalId, jenisSoalRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{jenisSoalId}")
                    .buildAndExpand(jenisSoal.getIdJenisSoal()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Jenis Soal Updated Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }

    }

    @DeleteMapping("/{jenisSoalId}")
    public ResponseEntity<?> deleteJenisSoal(@PathVariable String jenisSoalId) throws IOException {
        jenisSoalService.deleteJenisSoalById(jenisSoalId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(true, "Jenis Soal Deleted Successfully"));
    }

}
