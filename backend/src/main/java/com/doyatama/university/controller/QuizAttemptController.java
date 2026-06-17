package com.doyatama.university.controller;

import com.doyatama.university.model.QuizAttempt;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.QuizAttemptRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.QuizAttemptService;
import com.doyatama.university.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/quiz-attempt")
public class QuizAttemptController {
    private QuizAttemptService quizAttemptService = new QuizAttemptService();

    @GetMapping
    public PagedResponse<QuizAttempt> getQuizAttempts(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "userID", defaultValue = "*") String userID,
            @RequestParam(value = "quizID", defaultValue = "*") String quizID) throws IOException {
        return quizAttemptService.getAllQuizAttempt(page, size, userID, quizID);
    }

    @PostMapping
    public ResponseEntity<?> createQuizAttempt(@Valid @RequestBody QuizAttemptRequest quizAttemptRequest)
            throws IOException {
        QuizAttempt quizAttempt = quizAttemptService.createQuizAttempt(quizAttemptRequest);

        if (quizAttempt == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Please check relational ID"));
        } else {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{quizAttemptId}")
                    .buildAndExpand(quizAttempt.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "QuizAttempt Created Successfully"));
        }
    }

    @GetMapping("/{quizAttemptId}")
    public DefaultResponse<QuizAttempt> getQuizAttemptById(@PathVariable String quizAttemptId) throws IOException {
        return quizAttemptService.getQuizAttemptById(quizAttemptId);
    }

    @PutMapping("/{quizAttemptId}")
    public ResponseEntity<?> updateQuizAttempt(@PathVariable String quizAttemptId,
            @Valid @RequestBody QuizAttemptRequest quizAttemptRequest) throws IOException {
        QuizAttempt quizAttempt = quizAttemptService.updateQuizAttempt(quizAttemptId, quizAttemptRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{quizAttemptId}")
                .buildAndExpand(quizAttempt.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "QuizAttempt Updated Successfully"));
    }

    @DeleteMapping("/{quizAttemptId}")
    public HttpStatus deleteQuizAttempt(@PathVariable(value = "quizAttemptId") String quizAttemptId)
            throws IOException {
        quizAttemptService.deleteQuizAttemptById(quizAttemptId);
        return HttpStatus.FORBIDDEN;
    }
}
