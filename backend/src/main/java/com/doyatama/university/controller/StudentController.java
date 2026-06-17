package com.doyatama.university.controller;

import com.doyatama.university.model.Student;
import com.doyatama.university.payload.*;
import com.doyatama.university.repository.StudentRepository;
import com.doyatama.university.service.StudentService;
import com.doyatama.university.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    private StudentService studentService = new StudentService();

    StudentRepository studentRepository;

    @GetMapping
    public PagedResponse<Student> getStudents(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return studentService.getAllStudent(page, size);
    }

    @PostMapping
    public ResponseEntity<?> createStudent(@Valid @RequestBody StudentRequest studentRequest) throws IOException {
        Student student = studentService.createStudent(studentRequest);

        if (student == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Religion ID / User ID not found"));
        } else {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{studentId}")
                    .buildAndExpand(student.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Student Created Successfully"));
        }
    }

    @GetMapping("/{studentId}")
    public DefaultResponse<Student> getStudentById(@PathVariable String studentId) throws IOException {
        return studentService.getStudentById(studentId);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<?> updateStudent(@PathVariable String studentId,
            @Valid @RequestBody StudentRequest studentRequest) throws IOException {
        Student student = studentService.updateStudent(studentId, studentRequest);

        if (student == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Religion ID / User ID not found"));
        } else {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{studentId}")
                    .buildAndExpand(student.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "Student Updated Successfully"));
        }
    }

    @DeleteMapping("/{studentId}")
    public HttpStatus deleteStudent(@PathVariable(value = "studentId") String studentId) throws IOException {
        studentService.deleteStudentById(studentId);
        return HttpStatus.FORBIDDEN;
    }
}
