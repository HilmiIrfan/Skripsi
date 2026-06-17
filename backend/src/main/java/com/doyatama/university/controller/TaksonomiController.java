package com.doyatama.university.controller;

import com.doyatama.university.model.Taksonomi;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.TaksonomiRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.security.CurrentUser;
import com.doyatama.university.security.UserPrincipal;
import com.doyatama.university.service.TaksonomiService;
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
@RequestMapping("/api/taksonomi")
public class TaksonomiController {

    private TaksonomiService taksonomiService = new TaksonomiService();

    @GetMapping
    public PagedResponse<Taksonomi> getTaksonomi(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @CurrentUser UserPrincipal currentUser) throws IOException {

        String schoolID = currentUser.getSchoolId();
        return taksonomiService.getAllTaksonomi(page, size, schoolID);
    }

    @PostMapping
    public ResponseEntity<?> createTaksonomi(@Valid @RequestBody TaksonomiRequest taksonomiRequest) throws IOException {
        try {
            Taksonomi taksonomi = taksonomiService.createTaksonomi(taksonomiRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{taksonomiId}")
                    .buildAndExpand(taksonomi.getIdTaksonomi()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Taksonomi Created Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }
    }

    @GetMapping("/{taksonomiId}")
    public DefaultResponse<Taksonomi> getTaksonomiById(@PathVariable String taksonomiId) throws IOException {
        return taksonomiService.getTaksonomiById(taksonomiId);
    }

    @PutMapping("/{taksonomiId}")
    public ResponseEntity<?> updateTaksonomi(@PathVariable String taksonomiId,
            @Valid @RequestBody TaksonomiRequest taksonomiRequest) throws IOException {
        try {
            Taksonomi taksonomi = taksonomiService.updateTaksonomi(taksonomiId, taksonomiRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{taksonomiId}")
                    .buildAndExpand(taksonomi.getIdTaksonomi()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Taksonomi Updated Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }
    }

    @DeleteMapping("/{taksonomiId}")

    public ResponseEntity<?> deleteTaksonomi(@PathVariable String taksonomiId) throws IOException {
        try {
            taksonomiService.deleteTaksonomiById(taksonomiId);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(true, "Taksonomi Deleted Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }

    }
}
