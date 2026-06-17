package com.doyatama.university.controller;

import com.doyatama.university.model.LearningMedia;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.LearningMediaRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.LearningMediaService;
import com.doyatama.university.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/learning-media")
public class LearningMediaController {
    private LearningMediaService learningMediaService = new LearningMediaService();

    @GetMapping
    public PagedResponse<LearningMedia> getLearningMedias(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size, @RequestParam(value = "type", defaultValue = "*") String type) throws IOException {
        return learningMediaService.getAllLearningMedia(page, size, type);
    }

    @PostMapping
    public ResponseEntity<?> createLearningMedia(@Valid @RequestBody LearningMediaRequest learningMediaRequest) throws IOException {
        LearningMedia learningMedia = learningMediaService.createLearningMedia(learningMediaRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{learningMediaId}")
                .buildAndExpand(learningMedia.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "LearningMedia Created Successfully"));
    }

    @GetMapping("/{learningMediaId}")
    public DefaultResponse<LearningMedia> getLearningMediaById(@PathVariable String learningMediaId) throws IOException {
        return learningMediaService.getLearningMediaById(learningMediaId);
    }


    @PutMapping("/{learningMediaId}")
    public ResponseEntity<?> updateLearningMedia(@PathVariable String learningMediaId,
                                              @Valid @RequestBody LearningMediaRequest learningMediaRequest) throws IOException {
        LearningMedia learningMedia = learningMediaService.updateLearningMedia(learningMediaId, learningMediaRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{learningMediaId}")
                .buildAndExpand(learningMedia.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "LearningMedia Updated Successfully"));
    }

    @DeleteMapping("/{learningMediaId}")
    public HttpStatus deleteLearningMedia(@PathVariable (value = "learningMediaId") String learningMediaId) throws IOException {
        learningMediaService.deleteLearningMediaById(learningMediaId);
        return HttpStatus.FORBIDDEN;
    }
}
