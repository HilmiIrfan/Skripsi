package com.doyatama.university.controller;

import com.doyatama.university.model.Acp;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.AcpRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.security.CurrentUser;
import com.doyatama.university.security.UserPrincipal;
import com.doyatama.university.service.AcpService;
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
@RequestMapping("/api/acp")
public class AcpController {
    private AcpService acpService = new AcpService();

    @GetMapping
    public PagedResponse<Acp> getAcp(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "tahunAjaran", defaultValue = "*") String tahunAjaranID,
            @RequestParam(value = "semester", defaultValue = "*") String semesterID,
            @RequestParam(value = "kelas", defaultValue = "*") String kelasID,
            @RequestParam(value = "mapel", defaultValue = "*") String mapeID,
            @RequestParam(value = "konsentrasiKeahlianSekolah", defaultValue = "*") String konsentrasiKeahlianSekolahID,
            @RequestParam(value = "elemen", defaultValue = "*") String elemenID,
            @CurrentUser UserPrincipal currentUser) throws IOException {
        String schoolID = currentUser.getSchoolId();
        return acpService.getAllAcp(page, size, tahunAjaranID, semesterID, kelasID,
                mapeID, konsentrasiKeahlianSekolahID, elemenID, schoolID);
    }

    @PostMapping
    public ResponseEntity<?> createAcp(@Valid @RequestBody AcpRequest acpRequest) {
        try {
            Acp acp = acpService.createAcp(acpRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{acpId}")
                    .buildAndExpand(acp.getIdAcp()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "ACP Created Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }
    }

    @GetMapping("/{acpId}")
    public DefaultResponse<Acp> getAcpById(@PathVariable String acpId) throws IOException {
        return acpService.getAcpById(acpId);
    }

    @PutMapping("/{acpId}")
    public ResponseEntity<?> updateAcp(@PathVariable String acpId, @Valid @RequestBody AcpRequest acpRequest) {
        try {
            Acp acp = acpService.updateAcp(acpId, acpRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{acpId}")
                    .buildAndExpand(acp.getIdAcp()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "ACP Updated Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }
    }

    @DeleteMapping("/{acpId}")
    public ResponseEntity<?> deleteAcp(@PathVariable String acpId) {
        try {
            acpService.deleteAcpById(acpId);
            return ResponseEntity.ok().body(new ApiResponse(true, "ACP Deleted Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }
    }
}
