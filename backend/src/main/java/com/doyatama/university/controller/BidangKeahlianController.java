/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doyatama.university.controller;

import com.doyatama.university.model.BidangKeahlian;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.BidangKeahlianRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.BidangKeahlianService;
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
@RequestMapping("/api/bidang-keahlian")
public class BidangKeahlianController {
    private final BidangKeahlianService bidangKeahlianService = new BidangKeahlianService();

    @GetMapping
    public PagedResponse<BidangKeahlian> getBidangKeahlians(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return bidangKeahlianService.getAllBidangKeahlian(page, size);
    }

    @PostMapping
    public ResponseEntity<?> createBidangKeahlian(@Valid @RequestBody BidangKeahlianRequest bidangKeahlianRequest) throws IOException {
        BidangKeahlian bidangKeahlian = bidangKeahlianService.createBidangKeahlian(bidangKeahlianRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{bidangKeahlianId}")
                .buildAndExpand(bidangKeahlian.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "BidangKeahlian Created Successfully"));
    }

    @GetMapping("/{bidangKeahlianId}")
    public DefaultResponse<BidangKeahlian> getBidangKeahlianById(@PathVariable String bidangKeahlianId) throws IOException {
        return bidangKeahlianService.getBidangKeahlianById(bidangKeahlianId);
    }




//    @PutMapping("/{bidangKeahlianId}")
//    public ResponseEntity<?> updateBidangKeahlian(@PathVariable String bidangKeahlianId,
//                                              @Valid @RequestBody BidangKeahlianRequest bidangKeahlianRequest) throws IOException {
//        BidangKeahlian bidangKeahlian = bidangKeahlianService.updateBidangKeahlian(bidangKeahlianId, bidangKeahlianRequest);
//
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest().path("/{bidangKeahlianId}")
//                .buildAndExpand(bidangKeahlian.getId()).toUri();
//
//        return ResponseEntity.created(location)
//                .body(new ApiResponse(true, "BidangKeahlian Updated Successfully"));
//    }
//
//    @DeleteMapping("/{bidangKeahlianId}")
//    public HttpStatus deleteBidangKeahlian(@PathVariable (value = "bidangKeahlianId") String bidangKeahlianId) throws IOException {
//        bidangKeahlianService.deleteBidangKeahlianById(bidangKeahlianId);
//        return HttpStatus.FORBIDDEN;
//    }
    @PutMapping("/{bidangKeahlianId}")
public ResponseEntity<?> updateBidangKeahlian(@PathVariable String bidangKeahlianId,
                                              @Valid @RequestBody BidangKeahlianRequest bidangKeahlianRequest) throws IOException {
    BidangKeahlian updatedBidangKeahlian = bidangKeahlianService.updateBidangKeahlian(bidangKeahlianId, bidangKeahlianRequest);
    return ResponseEntity.ok(new ApiResponse(true, "BidangKeahlian Updated Successfully"));
}

@DeleteMapping("/{bidangKeahlianId}")
public ResponseEntity<?> deleteBidangKeahlian(@PathVariable String bidangKeahlianId) throws IOException {
    bidangKeahlianService.deleteBidangKeahlianById(bidangKeahlianId);
    return new ResponseEntity<>(new ApiResponse(true, "BidangKeahlian Deleted Successfully"), HttpStatus.OK);
}

}

