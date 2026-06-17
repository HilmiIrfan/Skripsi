package com.doyatama.university.controller;

import com.doyatama.university.model.Modul;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.ModulRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.security.CurrentUser;
import com.doyatama.university.security.UserPrincipal;
import com.doyatama.university.service.ModulService;
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
@RequestMapping("/api/modul")
public class ModulController {
    private ModulService modulService = new ModulService();

    @GetMapping
    public PagedResponse<Modul> getModul(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "tahunAjaran", defaultValue = "*") String tahunAjaranID,
            @RequestParam(value = "semester", defaultValue = "*") String semesterID,
            @RequestParam(value = "kelas", defaultValue = "*") String kelasID,
            @RequestParam(value = "mapel", defaultValue = "*") String mapelID,
            @CurrentUser UserPrincipal currentUser) throws IOException {

        String schoolID = currentUser.getSchoolId();
        return modulService.getAllModul(page, size, tahunAjaranID, semesterID, kelasID, mapelID, schoolID);
    }

    @PostMapping
    public ResponseEntity<?> createModul(@Valid @RequestBody ModulRequest modulRequest) throws IOException {
        try {
            Modul modul = modulService.createModul(modulRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{modulId}")
                    .buildAndExpand(modul.getIdModul()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Modul Created Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }
    }

    @GetMapping("/{modulId}")
    public DefaultResponse<Modul> getModulById(@PathVariable String modulId) throws IOException {
        return modulService.getModulById(modulId);
    }

    @PutMapping("/{modulId}")
    public ResponseEntity<?> updateModul(@PathVariable String modulId,
            @Valid @RequestBody ModulRequest modulRequest) throws IOException {
        try {
            Modul modul = modulService.updateModul(modulId, modulRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{modulId}")
                    .buildAndExpand(modul.getIdModul()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Modul Updated Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }
    }

    @DeleteMapping("/{modulId}")
    public ResponseEntity<?> deleteModul(@PathVariable String modulId) throws IOException {
        try {
            modulService.deleteModulById(modulId);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(true, "Modul Deleted Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }
    }

}
