/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.BidangKeahlian;
import com.doyatama.university.payload.BidangKeahlianRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.BidangKeahlianRepository;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author senja
 */

@Service
public class BidangKeahlianService {
    private BidangKeahlianRepository bidangKeahlianRepository = new BidangKeahlianRepository();
    // private SchoolRepository schoolRepository = new SchoolRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(BidangKeahlianService.class);

    public PagedResponse<BidangKeahlian> getAllBidangKeahlian(int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);
        // Retrieve Polls
        List<BidangKeahlian> bidangKeahlianResponse = new ArrayList<>();
        bidangKeahlianResponse = bidangKeahlianRepository.findAll(size);
        return new PagedResponse<>(bidangKeahlianResponse, bidangKeahlianResponse.size(), "Successfully get data", 200);
    }

    public BidangKeahlian createBidangKeahlian(BidangKeahlianRequest bidangKeahlianRequest) throws IOException {

        BidangKeahlian bidangKeahlian = new BidangKeahlian();
        bidangKeahlian.setId(bidangKeahlianRequest.getId());
        bidangKeahlian.setBidang(bidangKeahlianRequest.getBidang());
        return bidangKeahlianRepository.save(bidangKeahlian);
    }

    public DefaultResponse<BidangKeahlian> getBidangKeahlianById(String BDGid) throws IOException {
        // Retrieve BidangKeahlian
        BidangKeahlian bidangKeahlian = bidangKeahlianRepository.findById(BDGid);
        return new DefaultResponse<>(bidangKeahlian.isValid() ? bidangKeahlian : null, bidangKeahlian.isValid() ? 1 : 0,
                "Successfully get data");
    }

    public BidangKeahlian updateBidangKeahlian(String BDGid, BidangKeahlianRequest bidangKeahlianRequest)
            throws IOException {
        BidangKeahlian bidangKeahlian = new BidangKeahlian();
        bidangKeahlian.setBidang(bidangKeahlianRequest.getBidang());
        return bidangKeahlianRepository.update(BDGid, bidangKeahlian);
    }

    public void deleteBidangKeahlianById(String BDGid) throws IOException {
        BidangKeahlian bidangKeahlianResponse = bidangKeahlianRepository.findById(BDGid);
        if (bidangKeahlianResponse.isValid()) {
            bidangKeahlianRepository.deleteById(BDGid);
        } else {
            throw new ResourceNotFoundException("BidangKeahlian", "id", BDGid);
        }
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }
}
