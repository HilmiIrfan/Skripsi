package com.doyatama.university.controller;

import com.doyatama.university.model.ProgramKeahlianSekolah;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.ProgramKeahlianSekolahRequest;
import com.doyatama.university.security.CurrentUser;
import com.doyatama.university.security.UserPrincipal;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.ProgramKeahlianSekolahService;
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
@RequestMapping("/api/program-keahlian-sekolah")
public class ProgramKeahlianSekolahController {

        private ProgramKeahlianSekolahService programKeahlianSekolahService = new ProgramKeahlianSekolahService();

        @GetMapping
        public PagedResponse<ProgramKeahlianSekolah> getProgramKeahlianSekolah(
                        @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                        @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                        @RequestParam(value = "programKeahlianID", defaultValue = "*") String programKeahlianID,
                        @CurrentUser UserPrincipal currentUser) throws IOException {
                String schoolID = currentUser.getSchoolId();
                return programKeahlianSekolahService.getAllProgramKeahlianSekolah(page, size, schoolID,
                                programKeahlianID);
        }

        @PostMapping
        public ResponseEntity<?> createProgramKeahlianSekolah(
                        @Valid @RequestBody ProgramKeahlianSekolahRequest programKeahlianSekolahRequest)
                        throws IOException {
                try {
                        ProgramKeahlianSekolah programKeahlianSekolah = programKeahlianSekolahService
                                        .createProgramKeahlianSekolah(programKeahlianSekolahRequest);

                        URI location = ServletUriComponentsBuilder
                                        .fromCurrentRequest().path("/{programKeahlianSekolahId}")
                                        .buildAndExpand(programKeahlianSekolah.getIdProgramSekolah()).toUri();

                        return ResponseEntity.created(location)
                                        .body(new ApiResponse(true,
                                                        "Program, Keahlian Sekolah Created Successfully"));
                } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest()
                                        .body(new ApiResponse(false, e.getMessage()));
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(new ApiResponse(false, e.getMessage()));
                }
        }

        @GetMapping("/{programKeahlianSekolahId}")
        public DefaultResponse<ProgramKeahlianSekolah> getProgramKeahlianSekolahById(
                        @PathVariable String programKeahlianSekolahId) throws IOException {
                return programKeahlianSekolahService.getProgramKeahlianSekolahById(programKeahlianSekolahId);
        }

        @PutMapping("/{programKeahlianSekolahId}")
        public ResponseEntity<?> updateProgramKeahlianSekolah(@PathVariable String programKeahlianSekolahId,
                        @Valid @RequestBody ProgramKeahlianSekolahRequest programKeahlianSekolahRequest)
                        throws IOException {
                try {
                        ProgramKeahlianSekolah programKeahlianSekolah = programKeahlianSekolahService
                                        .updateProgramKeahlianSekolah(programKeahlianSekolahId,
                                                        programKeahlianSekolahRequest);

                        URI location = ServletUriComponentsBuilder
                                        .fromCurrentRequest().path("/{programKeahlianSekolahId}")
                                        .buildAndExpand(programKeahlianSekolah.getIdProgramSekolah()).toUri();

                        return ResponseEntity.created(location)
                                        .body(new ApiResponse(true, "Program Keahlian Sekolah Updated Successfully"));
                } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest()
                                        .body(new ApiResponse(false, e.getMessage()));
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(new ApiResponse(false, e.getMessage()));
                }
        }

        @DeleteMapping("/{programKeahlianSekolahId}")
        public ResponseEntity<?> deleteProgramKeahlianSekolah(@PathVariable String programKeahlianSekolahId)
                        throws IOException {
                try {
                        programKeahlianSekolahService.deleteProgramKeahlianSekolahById(programKeahlianSekolahId);

                        return ResponseEntity.ok()
                                        .body(new ApiResponse(true, "Program Keahlian Sekolah Deleted Successfully"));
                } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest()
                                        .body(new ApiResponse(false, e.getMessage()));
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(new ApiResponse(false, e.getMessage()));
                }
        }

}
