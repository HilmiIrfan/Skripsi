
package com.doyatama.university.controller;

import com.doyatama.university.model.JadwalPelajaran;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.JadwalPelajaranRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.JadwalPelajaranService;
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
@RequestMapping("/api/jadwal-pelajaran")
public class JadwalPelajaranController {
    private JadwalPelajaranService jadwalService = new JadwalPelajaranService();

    @GetMapping
    public PagedResponse<JadwalPelajaran> getJadPel(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return jadwalService.getAllJadPel(page, size);
    }

    @PostMapping
    public ResponseEntity<?> createJadPel(@Valid @RequestBody JadwalPelajaranRequest jadwalRequest) throws IOException {
        JadwalPelajaran jadwal = jadwalService.createJadPel(jadwalRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{jadwalId}")
                .buildAndExpand(jadwal.getIdJadwal()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "JadPel Created Successfully"));
    }

    @GetMapping("/{jadwalId}")
    public DefaultResponse<JadwalPelajaran> getJadPelById(@PathVariable String jadwalId) throws IOException {
        return jadwalService.getJadPelById(jadwalId);
    }


    @PutMapping("/{jadwalId}")
    public ResponseEntity<?> updateJadPel(@PathVariable String jadwalId,
                                              @Valid @RequestBody JadwalPelajaranRequest jadwalRequest) throws IOException {
        JadwalPelajaran jadwal = jadwalService.updateJadPel(jadwalId, jadwalRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{jadwalId}")
                .buildAndExpand(jadwal.getIdJadwal()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "JadPel Updated Successfully"));
    }

    @DeleteMapping("/{jadwalId}")
    public HttpStatus deleteJadPel(@PathVariable (value = "jadwalId") String jadwalId) throws IOException {
        jadwalService.deleteJadPelById(jadwalId);
        return HttpStatus.FORBIDDEN;
    }
}
