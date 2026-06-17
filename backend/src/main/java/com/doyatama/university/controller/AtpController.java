package com.doyatama.university.controller;

import com.doyatama.university.model.Atp;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.AtpRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.security.CurrentUser;
import com.doyatama.university.security.UserPrincipal;
import com.doyatama.university.service.AtpService;
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
@RequestMapping("/api/atp")
public class AtpController {

    private AtpService atpService = new AtpService();

    @GetMapping
    public PagedResponse<Atp> getAtp(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "tahunAjaran", defaultValue = "*") String tahunAjaranID,
            @RequestParam(value = "semester", defaultValue = "*") String semesterID,
            @RequestParam(value = "kelas", defaultValue = "*") String kelasID,
            @RequestParam(value = "mapel", defaultValue = "*") String mapeID,
            @RequestParam(value = "konsentrasiKeahlianSekolah", defaultValue = "*") String konsentrasiKeahlianSekolahID,
            @RequestParam(value = "elemen", defaultValue = "*") String elemenID,
            @RequestParam(value = "acp", defaultValue = "*") String acpID,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        String schoolID = currentUser.getSchoolId();
        return atpService.getAllAtp(page, size, tahunAjaranID, semesterID, kelasID,
                mapeID, konsentrasiKeahlianSekolahID, elemenID, acpID, schoolID);
    }

    @PostMapping
    public ResponseEntity<?> createAtp(@Valid @RequestBody AtpRequest atpRequest) {
        try {
            Atp atp = atpService.createAtp(atpRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{atpId}")
                    .buildAndExpand(atp.getIdAtp()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "ATP Created Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{atpId}")
    public DefaultResponse<Atp> getAtpById(@PathVariable String atpId) throws IOException {
        return atpService.getAtpById(atpId);
    }

    @PutMapping("/{atpId}")
    public ResponseEntity<?> updateAtp(@PathVariable String atpId, @Valid @RequestBody AtpRequest atpRequest) {
        try {
            Atp atp = atpService.updateAtp(atpId, atpRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{atpId}")
                    .buildAndExpand(atp.getIdAtp()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "ATP Updated Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{atpId}")
    public ResponseEntity<?> deleteAtp(@PathVariable String atpId) {
        try {
            atpService.deleteAtpById(atpId);
            return ResponseEntity.ok()
                    .body(new ApiResponse(true, "ATP Deleted Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

}
