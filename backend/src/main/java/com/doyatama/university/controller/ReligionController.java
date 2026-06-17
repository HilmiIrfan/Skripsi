package com.doyatama.university.controller;

import com.doyatama.university.model.Religion;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.ReligionRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.ReligionService;
import com.doyatama.university.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/religion")
public class ReligionController {
    private ReligionService religionService = new ReligionService();

    @GetMapping
    public PagedResponse<Religion> getReligions(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return religionService.getAllReligion(page, size);
    }

    @PostMapping
    public ResponseEntity<?> createReligion(@Valid @RequestBody ReligionRequest religionRequest) throws IOException {
        Religion religion = religionService.createReligion(religionRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{religionId}")
                .buildAndExpand(religion.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Religion Created Successfully"));
    }

    @GetMapping("/{religionId}")
    public DefaultResponse<Religion> getReligionById(@PathVariable String religionId) throws IOException {
        return religionService.getReligionById(religionId);
    }


    @PutMapping("/{religionId}")
    public ResponseEntity<?> updateReligion(@PathVariable String religionId,
                                              @Valid @RequestBody ReligionRequest religionRequest) throws IOException {
        Religion religion = religionService.updateReligion(religionId, religionRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{religionId}")
                .buildAndExpand(religion.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Religion Updated Successfully"));
    }

    @DeleteMapping("/{religionId}")
    public HttpStatus deleteReligion(@PathVariable (value = "religionId") String religionId) throws IOException {
        religionService.deleteReligionById(religionId);
        return HttpStatus.FORBIDDEN;
    }
}
