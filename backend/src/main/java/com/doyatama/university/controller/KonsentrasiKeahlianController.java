/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doyatama.university.controller;

import com.doyatama.university.model.KonsentrasiKeahlian;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.KonsentrasiKeahlianRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.KonsentrasiKeahlianService;
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
@RequestMapping("/api/konsentrasi-keahlian")
public class KonsentrasiKeahlianController {
    private KonsentrasiKeahlianService konsentrasiKeahlianService = new KonsentrasiKeahlianService();

    @GetMapping
    public PagedResponse<KonsentrasiKeahlian> getKonsentrasiKeahlians(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                                                    @RequestParam(value = "programId", defaultValue = "*") String programId) throws IOException {
        return konsentrasiKeahlianService.getAllKonsentrasiKeahlian(page, size, programId);
    }

    @PostMapping
    public ResponseEntity<?> createKonsentrasiKeahlian(@Valid @RequestBody KonsentrasiKeahlianRequest konsentrasiKeahlianRequest) throws IOException {
        KonsentrasiKeahlian konsentrasiKeahlian = konsentrasiKeahlianService.createKonsentrasiKeahlian(konsentrasiKeahlianRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{konsentrasiKeahlianId}")
                .buildAndExpand(konsentrasiKeahlian.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "KonsentrasiKeahlian Created Successfully"));
    }

    @GetMapping("/{konsentrasiKeahlianId}")
    public DefaultResponse<KonsentrasiKeahlian> getKonsentrasiKeahlianById(@PathVariable String konsentrasiKeahlianId) throws IOException {
        return konsentrasiKeahlianService.getKonsentrasiKeahlianById(konsentrasiKeahlianId);
    }


    @PutMapping("/{konsentrasiKeahlianId}")
    public ResponseEntity<?> updateKonsentrasiKeahlian(@PathVariable String konsentrasiKeahlianId,
                                              @Valid @RequestBody KonsentrasiKeahlianRequest konsentrasiKeahlianRequest) throws IOException {
        KonsentrasiKeahlian konsentrasiKeahlian = konsentrasiKeahlianService.updateKonsentrasiKeahlian(konsentrasiKeahlianId, konsentrasiKeahlianRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{konsentrasiKeahlianId}")
                .buildAndExpand(konsentrasiKeahlian.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "KonsentrasiKeahlian Updated Successfully"));
    }

    @DeleteMapping("/{konsentrasiKeahlianId}")
    public HttpStatus deleteKonsentrasiKeahlian(@PathVariable (value = "konsentrasiKeahlianId") String konsentrasiKeahlianId) throws IOException {
        konsentrasiKeahlianService.deleteKonsentrasiKeahlianById(konsentrasiKeahlianId);
        return HttpStatus.FORBIDDEN;
    }
}
