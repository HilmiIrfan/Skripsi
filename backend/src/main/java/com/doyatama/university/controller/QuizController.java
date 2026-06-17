package com.doyatama.university.controller;

import com.doyatama.university.model.Question;
import com.doyatama.university.model.Quiz;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.QuizRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.QuizRepository;
import com.doyatama.university.repository.RPSRepository;
import com.doyatama.university.service.QuizService;
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
@RequestMapping("/api/quiz")
public class QuizController {
    private final QuizService quizService;

    // @Autowired
    public QuizController(QuizRepository quizRepository, RPSRepository rpsRepository) {
        this.quizService = new QuizService(quizRepository, rpsRepository);
    }

    @GetMapping
    public PagedResponse<Quiz> getQuiz(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return quizService.getAllQuiz(page, size);
    }

    @GetMapping("/questionsByRPSQuiz1")
    public PagedResponse<Question> getAllQuestionsByRPSQuiz1(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam("rpsID") String rpsID) throws IOException {
        return quizService.getAllQuestionsByRPSQuiz1(page, size, rpsID);
    }

    @GetMapping("/questionsByRPSQuiz2")
    public PagedResponse<Question> getAllQuestionsByRPSQuiz2(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam("rpsID") String rpsID) throws IOException {
        return quizService.getAllQuestionsByRPSQuiz2(page, size, rpsID);
    }

    @PostMapping
    public ResponseEntity<?> createQuiz(@Valid @RequestBody QuizRequest quizRequest) throws IOException {
        Quiz quiz = quizService.createQuiz(quizRequest);

        if (quiz == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Please check relational ID"));
        } else {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{quizId}")
                    .buildAndExpand(quiz.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Quiz Created Successfully"));
        }
    }

    @GetMapping("/{quizId}")
    public DefaultResponse<Quiz> getQuizById(@PathVariable String quizId) throws IOException {
        return quizService.getQuizById(quizId);
    }

    @PutMapping("/{quizId}")
    public ResponseEntity<?> updateQuiz(@PathVariable String quizId,
            @Valid @RequestBody QuizRequest quizRequest) throws IOException {
        Quiz quiz = quizService.updateQuiz(quizId, quizRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{quizId}")
                .buildAndExpand(quiz.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Quiz Updated Successfully"));
    }

    @DeleteMapping("/{quizId}")
    public HttpStatus deleteQuiz(@PathVariable(value = "quizId") String quizId) throws IOException {
        quizService.deleteQuizById(quizId);
        return HttpStatus.FORBIDDEN;
    }
}
