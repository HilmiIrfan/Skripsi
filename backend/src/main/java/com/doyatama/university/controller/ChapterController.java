package com.doyatama.university.controller;

import com.doyatama.university.model.Chapter;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.ChapterRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.ChapterService;
import com.doyatama.university.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/chapter")
public class ChapterController {
    private ChapterService chapterService = new ChapterService();

    @GetMapping
    public PagedResponse<Chapter> getChapters(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return chapterService.getAllChapter(page, size);
    }

    @PostMapping
    public ResponseEntity<?> createChapter(@Valid @RequestBody ChapterRequest chapterRequest) throws IOException {
        Chapter chapter = chapterService.createChapter(chapterRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{chapterId}")
                .buildAndExpand(chapter.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Chapter Created Successfully"));
    }

    @GetMapping("/{chapterId}")
    public DefaultResponse<Chapter> getChapterById(@PathVariable String chapterId) throws IOException {
        return chapterService.getChapterById(chapterId);
    }


    @PutMapping("/{chapterId}")
    public ResponseEntity<?> updateChapter(@PathVariable String chapterId,
                                              @Valid @RequestBody ChapterRequest chapterRequest) throws IOException {
        Chapter chapter = chapterService.updateChapter(chapterId, chapterRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{chapterId}")
                .buildAndExpand(chapter.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Chapter Updated Successfully"));
    }

    @DeleteMapping("/{chapterId}")
    public HttpStatus deleteChapter(@PathVariable (value = "chapterId") String chapterId) throws IOException {
        chapterService.deleteChapterById(chapterId);
        return HttpStatus.FORBIDDEN;
    }
}
