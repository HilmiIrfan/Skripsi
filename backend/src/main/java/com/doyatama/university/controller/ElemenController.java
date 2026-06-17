package com.doyatama.university.controller;

import com.doyatama.university.model.Elemen;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.ElemenRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.security.CurrentUser;
import com.doyatama.university.security.UserPrincipal;
import com.doyatama.university.service.ElemenService;
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
@RequestMapping("/api/elemen")
public class ElemenController {
    private ElemenService elemenService = new ElemenService();

    @GetMapping
    public PagedResponse<Elemen> getElemen(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "mapelID", defaultValue = "*") String mapelID,
            @RequestParam(value = "tahunAjaranID", defaultValue = "*") String tahunAjaranID,
            @RequestParam(value = "semesterID", defaultValue = "*") String semesterID,
            @RequestParam(value = "kelasID", defaultValue = "*") String kelasID,
            @RequestParam(value = "konsentrasiKeahlianSekolahID", defaultValue = "*") String konsentrasiKeahlianSekolahID,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        String schoolID = currentUser.getSchoolId();
        return elemenService.getAllElemen(page, size, mapelID, tahunAjaranID, semesterID, kelasID,
                konsentrasiKeahlianSekolahID, schoolID);
    }

    @PostMapping
    public ResponseEntity<?> createElemen(@Valid @RequestBody ElemenRequest elemenRequest) throws IOException {
        try {
            Elemen elemen = elemenService.createElemen(elemenRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{elemenId}")
                    .buildAndExpand(elemen.getIdElemen()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Elemen Created Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }
    }

    @GetMapping("/{elemenId}")
    public DefaultResponse<Elemen> getElemenById(@PathVariable String elemenId) throws IOException {
        return elemenService.getElemenById(elemenId);
    }

    @PutMapping("/{elemenId}")
    public ResponseEntity<?> updateElemen(@PathVariable String elemenId,
            @Valid @RequestBody ElemenRequest elemenRequest) throws IOException {
        try {
            Elemen elemen = elemenService.updateElemen(elemenId, elemenRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{elemenId}")
                    .buildAndExpand(elemen.getIdElemen()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Elemen Updated Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }
    }

    @DeleteMapping("/{elemenId}")
    public ResponseEntity<?> deleteElemen(@PathVariable String elemenId) throws IOException {
        try {
            elemenService.deleteElemenById(elemenId);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(true, "Elemen Deleted Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }
    }
}
