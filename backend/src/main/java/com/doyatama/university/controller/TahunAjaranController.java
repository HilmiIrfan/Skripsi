
package com.doyatama.university.controller;

import com.doyatama.university.model.TahunAjaran;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.TahunAjaranRequest;
import com.doyatama.university.security.CurrentUser;
import com.doyatama.university.security.UserPrincipal;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.TahunAjaranService;
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
@RequestMapping("/api/tahun")
public class TahunAjaranController {
    private TahunAjaranService tahunService = new TahunAjaranService();

    @GetMapping
    public PagedResponse<TahunAjaran> getTahunAjaran(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @CurrentUser UserPrincipal currentUser) throws IOException {

        String schoolID = currentUser.getSchoolId();
        System.out.println("⚙️ [Controller] schoolID diterima: " + schoolID);
        return tahunService.getAllTahunAjaran(page, schoolID, size);
    }

    @PostMapping
    public ResponseEntity<?> createTahunAjaran(@Valid @RequestBody TahunAjaranRequest tahunRequest) throws IOException {
        try {
            TahunAjaran tahun = tahunService.createTahunAjaran(tahunRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{tahunId}")
                    .buildAndExpand(tahun.getIdTahun()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "TahunAjaran Created Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{tahunId}")
    public DefaultResponse<TahunAjaran> getTahunAjaranById(@PathVariable String tahunId) throws IOException {
        return tahunService.getTahunAjaranById(tahunId);
    }

    @PutMapping("/{tahunId}")
    public ResponseEntity<?> updateTahunAjaran(@PathVariable String tahunId,
            @Valid @RequestBody TahunAjaranRequest tahunRequest) throws IOException {
        try {
            TahunAjaran tahun = tahunService.updateTahunAjaran(tahunId, tahunRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{tahunId}")
                    .buildAndExpand(tahun.getIdTahun()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "TahunAjaran Updated Successfully"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{tahunId}")
    public ResponseEntity<?> deleteTahunAjaran(@PathVariable String tahunId) throws IOException {
        try {
            tahunService.deleteTahunAjaranById(tahunId);

            return ResponseEntity.ok()
                    .body(new ApiResponse(true, "TahunAjaran Deleted Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

}
