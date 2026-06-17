package com.doyatama.university.controller;

import com.doyatama.university.model.ExerciseAttempt;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.ExerciseAttemptRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.ExerciseAttemptService;
import com.doyatama.university.util.AppConstants;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/exercise-attempt")
public class ExerciseAttemptController {
    private final ExerciseAttemptService exerciseAttemptService;

    // @Autowired
    public ExerciseAttemptController(ExerciseAttemptService exerciseAttemptService) {
        this.exerciseAttemptService = exerciseAttemptService;
    }

    @GetMapping
    public PagedResponse<ExerciseAttempt> getExerciseAttempts(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "userID", defaultValue = "*") String userID,
            @RequestParam(value = "exerciseID", defaultValue = "*") String exerciseID) throws IOException {
        return exerciseAttemptService.getAllExerciseAttempt(page, size, userID, exerciseID);
    }

    @GetMapping("/exercise-attempt-id")
    public PagedResponse<ExerciseAttempt> getExerciseAttemptsById(
            @RequestParam(value = "exerciseAttemptId", defaultValue = "*") String exerciseAttemptId,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return exerciseAttemptService.getExerciseAttemptById(exerciseAttemptId, page, size);
    }

    @PostMapping
    public ResponseEntity<?> createExerciseAttempt(@Valid @RequestBody ExerciseAttemptRequest exerciseAttemptRequest)
            throws IOException {
        ExerciseAttempt exerciseAttempt = exerciseAttemptService.createExerciseAttempt(exerciseAttemptRequest);

        if (exerciseAttempt == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Please check relational ID"));
        } else {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{exerciseAttemptId}")
                    .buildAndExpand(exerciseAttempt.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "ExerciseAttempt Created Successfully"));
        }
    }

    @GetMapping("/{exerciseAttemptId}")
    public PagedResponse<ExerciseAttempt> getExerciseAttemptById(@PathVariable String exerciseAttemptId)
            throws IOException {
        return exerciseAttemptService.getExerciseAttemptById(exerciseAttemptId);
    }

    @PutMapping("/{exerciseAttemptId}")
    public ResponseEntity<?> updateExerciseAttempt(@PathVariable String exerciseAttemptId,
            @Valid @RequestBody ExerciseAttemptRequest exerciseAttemptRequest) throws IOException {
        ExerciseAttempt exerciseAttempt = exerciseAttemptService.updateExerciseAttempt(exerciseAttemptId,
                exerciseAttemptRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{exerciseAttemptId}")
                .buildAndExpand(exerciseAttempt.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "ExerciseAttempt Updated Successfully"));
    }

    @DeleteMapping("/{exerciseAttemptId}")
    public HttpStatus deleteExerciseAttempt(@PathVariable(value = "exerciseAttemptId") String exerciseAttemptId)
            throws IOException {
        exerciseAttemptService.deleteExerciseAttemptById(exerciseAttemptId);
        return HttpStatus.FORBIDDEN;
    }
}
