package com.doyatama.university.controller;

import com.doyatama.university.model.KonsentrasiKeahlianSekolah;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.KonsentrasiKeahlianSekolahRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.security.CurrentUser;
import com.doyatama.university.security.UserPrincipal;
import com.doyatama.university.service.KonsentrasiKeahlianSekolahService;
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
@RequestMapping("/api/konsentrasi-keahlian-sekolah")
public class KonsentrasiKeahlianSekolahController {

    private KonsentrasiKeahlianSekolahService konsentrasiKeahlianSekolahService = new KonsentrasiKeahlianSekolahService();

    @GetMapping
    public PagedResponse<KonsentrasiKeahlianSekolah> getKonsentrasiKeahlianSekolah(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "konsentrasiKeahlianID", defaultValue = "*") String konsentrasiKeahlianID,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        String schoolID = currentUser.getSchoolId();
        return konsentrasiKeahlianSekolahService.getAllKonsentrasiKeahlianSekolah(page, size, schoolID,
                konsentrasiKeahlianID);
    }

    @PostMapping
    public ResponseEntity<?> createKonsentrasiKeahlianSekolah(
            @Valid @RequestBody KonsentrasiKeahlianSekolahRequest konsentrasiKeahlianSekolahRequest)
            throws IOException {
        try {
            KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolah = konsentrasiKeahlianSekolahService
                    .createKonsentrasiKeahlianSekolah(konsentrasiKeahlianSekolahRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{konsentrasiKeahlianSekolahId}")
                    .buildAndExpand(konsentrasiKeahlianSekolah.getIdKonsentrasiSekolah()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Konsentrasi Keahlian Sekolah Created Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{konsentrasiKeahlianSekolahId}")
    public DefaultResponse<KonsentrasiKeahlianSekolah> getKonsentrasiKeahlianSekolahById(
            @PathVariable String konsentrasiKeahlianSekolahId) throws IOException {
        return konsentrasiKeahlianSekolahService.getKonsentrasiKeahlianSekolahById(konsentrasiKeahlianSekolahId);
    }

    @PutMapping("/{konsentrasiKeahlianSekolahId}")
    public ResponseEntity<?> updateKonsentrasiKeahlianSekolah(@PathVariable String konsentrasiKeahlianSekolahId,
            @Valid @RequestBody KonsentrasiKeahlianSekolahRequest konsentrasiKeahlianSekolahRequest)
            throws IOException {
        try {
            KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolah = konsentrasiKeahlianSekolahService
                    .updateKonsentrasiKeahlianSekolah(konsentrasiKeahlianSekolahId, konsentrasiKeahlianSekolahRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{konsentrasiKeahlianSekolahId}")
                    .buildAndExpand(konsentrasiKeahlianSekolah.getIdKonsentrasiSekolah()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Konsentrasi Keahlian Sekolah Updated Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{konsentrasiKeahlianSekolahId}")
    public ResponseEntity<?> deleteKonsentrasiKeahlianSekolah(@PathVariable String konsentrasiKeahlianSekolahId)
            throws IOException {
        try {
            konsentrasiKeahlianSekolahService.deleteKonsentrasiKeahlianSekolahById(konsentrasiKeahlianSekolahId);
            return ResponseEntity.ok().body(new ApiResponse(true, "Konsentrasi Keahlian Sekolah Deleted Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, e.getMessage()));
        }
    }
}
