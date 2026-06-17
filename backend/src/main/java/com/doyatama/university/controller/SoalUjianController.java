package com.doyatama.university.controller;

import com.doyatama.university.model.SoalUjian;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.SoalUjianRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.security.CurrentUser;
import com.doyatama.university.security.UserPrincipal;
import com.doyatama.university.service.SoalUjianService;
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
@RequestMapping("/api/soalUjian")
public class SoalUjianController {

    private SoalUjianService soalUjianService = new SoalUjianService();

    @GetMapping
    public PagedResponse<SoalUjian> getSoalUjian(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "userID", defaultValue = "*") String userID,
            @CurrentUser UserPrincipal currentUser) throws IOException {

        String schoolID = currentUser.getSchoolId();
        return soalUjianService.getAllSoalUjian(page, size, userID, schoolID);
    }

    @PostMapping
    public ResponseEntity<?> createSoalUjian(@Valid @RequestBody SoalUjianRequest soalUjianRequest) throws IOException {
        try {
            SoalUjian soalUjian = soalUjianService.createSoalUjian(soalUjianRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{soalUjianId}")
                    .buildAndExpand(soalUjian.getIdSoalUjian()).toUri();
            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Soal Ujian Created Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }
    }

    @GetMapping("/{soalUjianId}")
    public DefaultResponse<SoalUjian> getSoalUjianById(@PathVariable String soalUjianId) throws IOException {
        return soalUjianService.getSoalUjianById(soalUjianId);
    }

    @PutMapping("/{soalUjianId}")
    public ResponseEntity<?> updateSoalUjian(@PathVariable String soalUjianId,
            @Valid @RequestBody SoalUjianRequest soalUjianRequest) throws IOException {
        try {
            SoalUjian soalUjian = soalUjianService.updateSoalUjian(soalUjianId, soalUjianRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{soalUjianId}")
                    .buildAndExpand(soalUjian.getIdSoalUjian()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Soal Ujian Updated Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }
    }

    @DeleteMapping("/{soalUjianId}")
    public ResponseEntity<?> deleteSoalUjian(@PathVariable String soalUjianId) throws IOException {
        try {
            soalUjianService.deleteSoalUjianById(soalUjianId);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(true, "Soal Ujian Deleted Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }
    }

}
