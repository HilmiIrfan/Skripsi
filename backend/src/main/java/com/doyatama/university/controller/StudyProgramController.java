package com.doyatama.university.controller;

import com.doyatama.university.model.StudyProgram;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.StudyProgramRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.StudyProgramService;
import com.doyatama.university.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/study-program")
public class StudyProgramController {
    private StudyProgramService studyProgramService = new StudyProgramService();

    @GetMapping
    public PagedResponse<StudyProgram> getStudyPrograms(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return studyProgramService.getAllStudyProgram(page, size);
    }

    @PostMapping
    public ResponseEntity<?> createStudyProgram(@Valid @RequestBody StudyProgramRequest studyProgramRequest) throws IOException {
        StudyProgram studyProgram = studyProgramService.createStudyProgram(studyProgramRequest);
        if(studyProgram == null){
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Department ID not found"));
        }else{
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{studyProgramId}")
                    .buildAndExpand(studyProgram.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "StudyProgram Created Successfully"));
        }
    }

    @GetMapping("/{studyProgramId}")
    public DefaultResponse<StudyProgram> getStudyProgramById(@PathVariable String studyProgramId) throws IOException {
        return studyProgramService.getStudyProgramById(studyProgramId);
    }


    @PutMapping("/{studyProgramId}")
    public ResponseEntity<?> updateStudyProgram(@PathVariable String studyProgramId,
                                              @Valid @RequestBody StudyProgramRequest studyProgramRequest) throws IOException {
        StudyProgram studyProgram = studyProgramService.updateStudyProgram(studyProgramId, studyProgramRequest);

        if(studyProgram == null){
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Department ID not found"));
        }else{
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{studyProgramId}")
                    .buildAndExpand(studyProgram.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "StudyProgram Updated Successfully"));
        }
    }

    @DeleteMapping("/{studyProgramId}")
    public HttpStatus deleteStudyProgram(@PathVariable (value = "studyProgramId") String studyProgramId) throws IOException {
        studyProgramService.deleteStudyProgramById(studyProgramId);
        return HttpStatus.FORBIDDEN;
    }
}
