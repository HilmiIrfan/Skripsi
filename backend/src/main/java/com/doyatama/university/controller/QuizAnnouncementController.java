package com.doyatama.university.controller;

import com.doyatama.university.model.QuizAnnouncement;
import com.doyatama.university.payload.*;
import com.doyatama.university.service.QuizAnnouncementService;
import com.doyatama.university.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

/**
 * @author alfa
 */
@RestController
@RequestMapping("/api/quizAnnouncements")
public class QuizAnnouncementController {

    private QuizAnnouncementService quizAnnouncementService = new QuizAnnouncementService();

    @GetMapping
    public PagedResponse<QuizAnnouncement> getQuizAnnouncements(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        // Validate page and size parameters
        if (page < 1 || size < 1) {
            throw new IllegalArgumentException("Page number and size must be greater than zero.");
        }

        return quizAnnouncementService.getAllQuizAnnouncements(page, size);
    }

    @PostMapping
    public ResponseEntity<?> createQuizAnnouncement(@Valid @RequestBody QuizAnnouncementRequest quizAnnouncementRequest)
            throws IOException {
        QuizAnnouncement quizAnnouncement = quizAnnouncementService.createQuizAnnouncement(quizAnnouncementRequest);

        if (quizAnnouncement == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Please check relational ID"));
        } else {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{quizAnnouncementId}")
                    .buildAndExpand(quizAnnouncement.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Quiz Announcement Created Successfully"));
        }
    }

    @GetMapping("/{quizAnnouncementId}")
    public DefaultResponse<QuizAnnouncement> getQuizAnnouncementById(@PathVariable String quizAnnouncementId)
            throws IOException {
        return quizAnnouncementService.getQuizAnnouncementById(quizAnnouncementId);
    }

    @PutMapping("/{quizAnnouncementId}")
    public ResponseEntity<?> updateQuizAnnouncement(@PathVariable String quizAnnouncementId,
            @Valid @RequestBody QuizAnnouncementRequest quizAnnouncementRequest) throws IOException {
        QuizAnnouncement quizAnnouncement = quizAnnouncementService.updateQuizAnnouncement(quizAnnouncementId,
                quizAnnouncementRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{quizAnnouncementId}")
                .buildAndExpand(quizAnnouncement.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Quiz Announcement Updated Successfully"));
    }

    @DeleteMapping("/{quizAnnouncementId}")
    public HttpStatus deleteQuizAnnouncement(@PathVariable(value = "quizAnnouncementId") String quizAnnouncementId)
            throws IOException {
        quizAnnouncementService.deleteQuizAnnouncementById(quizAnnouncementId);
        return HttpStatus.FORBIDDEN;
    }
}
