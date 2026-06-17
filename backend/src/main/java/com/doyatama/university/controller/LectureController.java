package com.doyatama.university.controller;

import com.doyatama.university.model.Lecture;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.LectureRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.LectureRepository;
import com.doyatama.university.service.LectureService;
import com.doyatama.university.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/lecture")
public class LectureController {
    private LectureService lectureService = new LectureService();

    LectureRepository lectureRepository;

    @GetMapping
    public PagedResponse<Lecture> getLectures(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                        @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return lectureService.getAllLecture(page, size);
    }

    @PostMapping
    public ResponseEntity<?> createLecture(@Valid @RequestBody LectureRequest lectureRequest) throws IOException {
        Lecture lecture = lectureService.createLecture(lectureRequest);

        if(lecture == null){
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Religion ID / User ID not found"));
        }else{
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{lectureId}")
                    .buildAndExpand(lecture.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Lecture Created Successfully"));
        }
    }

    @GetMapping("/{lectureId}")
    public DefaultResponse<Lecture> getLectureById(@PathVariable String lectureId) throws IOException {
        return lectureService.getLectureById(lectureId);
    }


    @PutMapping("/{lectureId}")
    public ResponseEntity<?> updateLecture(@PathVariable String lectureId,
                                                @Valid @RequestBody LectureRequest lectureRequest) throws IOException {
        Lecture lecture = lectureService.updateLecture(lectureId, lectureRequest);

        if(lecture == null){
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Religion ID / User ID not found"));
        }else{
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{lectureId}")
                    .buildAndExpand(lecture.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Lecture Updated Successfully"));
        }
    }

    @DeleteMapping("/{lectureId}")
    public HttpStatus deleteLecture(@PathVariable (value = "lectureId") String lectureId) throws IOException {
        lectureService.deleteLectureById(lectureId);
        return HttpStatus.FORBIDDEN;
    }
}
