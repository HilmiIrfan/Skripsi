package com.doyatama.university.controller;

import com.doyatama.university.model.Question;
import com.doyatama.university.model.Exercise;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.ExerciseRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.ExerciseService;
import com.doyatama.university.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseController {
    private ExerciseService exerciseService = new ExerciseService();

    @GetMapping
    public PagedResponse<Exercise> getExercises(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return exerciseService.getAllExercise(page, size);
    }

    @GetMapping("/questions")
    public PagedResponse<Question> getAllQuestionsByRPS(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "rpsID", required = false) String rpsID,
            @RequestParam(value = "type_exercise", required = false) String type_exercise) throws IOException {
        return exerciseService.getAllQuestionsByRPS(page, size, rpsID, type_exercise);
    }

    @PostMapping
    public ResponseEntity<?> createExercise(@Valid @RequestBody ExerciseRequest exerciseRequest) throws IOException {
        Exercise exercise = exerciseService.createExercise(exerciseRequest);

        if (exercise == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Please check relational ID"));
        } else {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{exerciseId}")
                    .buildAndExpand(exercise.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Exercise Created Successfully"));
        }
    }

    @GetMapping("/{exerciseId}")
    public DefaultResponse<Exercise> getExerciseById(@PathVariable String exerciseId) throws IOException {
        return exerciseService.getExerciseById(exerciseId);
    }

    @PutMapping("/{exerciseId}")
    public ResponseEntity<?> updateExercise(@PathVariable String exerciseId,
            @Valid @RequestBody ExerciseRequest exerciseRequest) throws IOException {
        Exercise exercise = exerciseService.updateExercise(exerciseId, exerciseRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{exerciseId}")
                .buildAndExpand(exercise.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Exercise Updated Successfully"));
    }

    @DeleteMapping("/{exerciseId}")
    public HttpStatus deleteExercise(@PathVariable(value = "exerciseId") String exerciseId) throws IOException {
        exerciseService.deleteExerciseById(exerciseId);
        return HttpStatus.FORBIDDEN;
    }
}
