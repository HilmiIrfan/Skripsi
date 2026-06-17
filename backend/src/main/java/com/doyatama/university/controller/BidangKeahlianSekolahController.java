package com.doyatama.university.controller;

import com.doyatama.university.model.BidangKeahlianSekolah;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.BidangKeahlianSekolahRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.security.CurrentUser;
import com.doyatama.university.security.UserPrincipal;
import com.doyatama.university.service.BidangKeahlianSekolahService;
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

@RestController
@RequestMapping("/api/bidang-keahlian-sekolah")
public class BidangKeahlianSekolahController {
        private BidangKeahlianSekolahService bidangKeahlianSekolahService = new BidangKeahlianSekolahService();

        @GetMapping
        public PagedResponse<BidangKeahlianSekolah> getBidangKeahlianSekolah(
                        @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                        @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                        @RequestParam(value = "bidangKeahlianID", defaultValue = "*") String bidangKeahlianID,
                        @CurrentUser UserPrincipal currentUser) throws IOException {
                String schoolID = currentUser.getSchoolId();
                return bidangKeahlianSekolahService.getAllBidangKeahlianSekolah(page, size, schoolID, bidangKeahlianID);
        }

        @PostMapping
        public ResponseEntity<?> createBidangKeahlianSekolah(
                        @Valid @RequestBody BidangKeahlianSekolahRequest bidangKeahlianSekolahRequest)
                        throws IOException {
                try {
                        BidangKeahlianSekolah bidangKeahlianSekolah = bidangKeahlianSekolahService
                                        .createBidangKeahlianSekolah(bidangKeahlianSekolahRequest);

                        URI location = ServletUriComponentsBuilder
                                        .fromCurrentRequest().path("/{bidangKeahlianSekolahId}")
                                        .buildAndExpand(bidangKeahlianSekolah.getIdBidangSekolah()).toUri();

                        return ResponseEntity.created(location)
                                        .body(new ApiResponse(true, "Bidang Keahlian Sekolah Created Successfully"));
                } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest()
                                        .body(new ApiResponse(false, e.getMessage()));
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(new ApiResponse(false, e.getMessage()));
                }
        }

        @GetMapping("/{bidangKeahlianSekolahId}")
        public DefaultResponse<BidangKeahlianSekolah> getBidangKeahlianSekolahById(
                        @PathVariable String bidangKeahlianSekolahId) throws IOException {
                return bidangKeahlianSekolahService.getBidangKeahlianSekolahById(bidangKeahlianSekolahId);
        }

        @PutMapping("/{bidangKeahlianSekolahId}")
        public ResponseEntity<?> updateBidangKeahlianSekolah(@PathVariable String bidangKeahlianSekolahId,
                        @Valid @RequestBody BidangKeahlianSekolahRequest bidangKeahlianSekolahRequest)
                        throws IOException {
                try {
                        BidangKeahlianSekolah bidangKeahlianSekolah = bidangKeahlianSekolahService
                                        .updateBidangKeahlianSekolah(bidangKeahlianSekolahId,
                                                        bidangKeahlianSekolahRequest);

                        URI location = ServletUriComponentsBuilder
                                        .fromCurrentRequest().path("/{bidangKeahlianSekolahId}")
                                        .buildAndExpand(bidangKeahlianSekolah.getIdBidangSekolah()).toUri();

                        return ResponseEntity.created(location)
                                        .body(new ApiResponse(true, "Bidang Keahlian Sekolah Updated Successfully"));
                } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest()
                                        .body(new ApiResponse(false, e.getMessage()));
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(new ApiResponse(false, e.getMessage()));
                }
        }

        @DeleteMapping("/{bidangKeahlianSekolahId}")
        public ResponseEntity<?> deleteBidangKeahlianSekolah(@PathVariable String bidangKeahlianSekolahId)
                        throws IOException {
                try {
                        bidangKeahlianSekolahService.deleteBidangKeahlianSekolahById(bidangKeahlianSekolahId);

                        return ResponseEntity.ok()
                                        .body(new ApiResponse(true, "Bidang Keahlian Sekolah Deleted Successfully"));
                } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest()
                                        .body(new ApiResponse(false, e.getMessage()));
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(new ApiResponse(false, e.getMessage()));
                }
        }

}
