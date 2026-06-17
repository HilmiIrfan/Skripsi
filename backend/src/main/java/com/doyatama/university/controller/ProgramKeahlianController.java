/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doyatama.university.controller;

import com.doyatama.university.model.ProgramKeahlian;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.ProgramKeahlianRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.ProgramKeahlianService;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author senja
 */

@RestController
@RequestMapping("/api/program-keahlian")
public class ProgramKeahlianController {
    private ProgramKeahlianService programKeahlianService = new ProgramKeahlianService();

    @GetMapping
    public PagedResponse<ProgramKeahlian> getProgramKeahlians(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                                                    @RequestParam(value = "bidangId", defaultValue = "*") String bidangId) throws IOException {
        return programKeahlianService.getAllProgramKeahlian(page, size, bidangId);
    }

    @PostMapping
    public ResponseEntity<?> createProgramKeahlian(@Valid @RequestBody ProgramKeahlianRequest programKeahlianRequest) throws IOException {
        ProgramKeahlian programKeahlian = programKeahlianService.createProgramKeahlian(programKeahlianRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{programKeahlianId}")
                .buildAndExpand(programKeahlian.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "ProgramKeahlian Created Successfully"));
    }

    @GetMapping("/{programKeahlianId}")
    public DefaultResponse<ProgramKeahlian> getProgramKeahlianById(@PathVariable String programKeahlianId) throws IOException {
        return programKeahlianService.getProgramKeahlianById(programKeahlianId);
    }
    



    @PutMapping("/{programKeahlianId}")
    public ResponseEntity<?> updateProgramKeahlian(@PathVariable String programKeahlianId,
                                              @Valid @RequestBody ProgramKeahlianRequest programKeahlianRequest) throws IOException {
        ProgramKeahlian programKeahlian = programKeahlianService.updateProgramKeahlian(programKeahlianId, programKeahlianRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{programKeahlianId}")
                .buildAndExpand(programKeahlian.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "ProgramKeahlian Updated Successfully"));
    }

    @DeleteMapping("/{programKeahlianId}")
    public HttpStatus deleteProgramKeahlian(@PathVariable (value = "programKeahlianId") String programKeahlianId) throws IOException {
        programKeahlianService.deleteProgramKeahlianById(programKeahlianId);
        return HttpStatus.FORBIDDEN;
    }
}



//    @PutMapping("/{programKeahlianId}")
//public ResponseEntity<?> updateProgramKeahlian(@PathVariable String programKeahlianid,
//                                              @Valid @RequestBody ProgramKeahlianRequest programKeahlianRequest) throws IOException {
//    ProgramKeahlian updatedProgramKeahlian = programKeahlianService.updateProgramKeahlian(programKeahlianid, programKeahlianRequest);
//    return ResponseEntity.ok(new ApiResponse(true, "ProgramKeahlian Updated Successfully"));
//}
//
//@DeleteMapping("/{programKeahlianId}")
//public ResponseEntity<?> deleteProgramKeahlian(@PathVariable String programKeahlianId) throws IOException {
//    programKeahlianService.deleteProgramKeahlianById(programKeahlianId);
//    return new ResponseEntity<>(new ApiResponse(true, "ProgramKeahlian Deleted Successfully"), HttpStatus.OK);
//}
