package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.BidangKeahlian;
import com.doyatama.university.model.BidangKeahlianSekolah;
import com.doyatama.university.model.School;
import com.doyatama.university.payload.BidangKeahlianSekolahRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.BidangKeahlianSekolahRepository;
import com.doyatama.university.repository.BidangKeahlianRepository;
import com.doyatama.university.repository.SchoolRepository;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class BidangKeahlianSekolahService {

    private BidangKeahlianSekolahRepository bidangKeahlianSekolahRepository = new BidangKeahlianSekolahRepository();
    private BidangKeahlianRepository bidangKeahlianRepository = new BidangKeahlianRepository();
    private SchoolRepository schoolRepository = new SchoolRepository();

    public PagedResponse<BidangKeahlianSekolah> getAllBidangKeahlianSekolah(int page, int size, String schoolID,
            String bidangKeahlianID) throws IOException {
        validatePageNumberAndSize(page, size);

        List<BidangKeahlianSekolah> bidangKeahlianSekolahResponse;

        if (schoolID.equalsIgnoreCase("*")) {
            bidangKeahlianSekolahResponse = bidangKeahlianSekolahRepository.findAll(size);
        } else {
            bidangKeahlianSekolahResponse = bidangKeahlianSekolahRepository.findBidangKeahlianSekolahBySekolah(schoolID,
                    size);
        }

        return new PagedResponse<>(bidangKeahlianSekolahResponse, bidangKeahlianSekolahResponse.size(),
                "Successfully get data", 200);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    public BidangKeahlianSekolah createBidangKeahlianSekolah(BidangKeahlianSekolahRequest bidangKeahlianSekolahRequest)
            throws IOException {

        if (bidangKeahlianSekolahRequest.getIdBidangSekolah() == null) {
            bidangKeahlianSekolahRequest.setIdBidangSekolah(UUID.randomUUID().toString());
        }

        if (bidangKeahlianSekolahRepository.existsById(bidangKeahlianSekolahRequest.getIdBidangSekolah())) {
            throw new IllegalArgumentException("Bidang Sekolah already exist");
        }

        School schoolResponse = schoolRepository.findById(bidangKeahlianSekolahRequest.getIdSekolah());
        BidangKeahlian bidangKeahlianResponse = bidangKeahlianRepository
                .findById(bidangKeahlianSekolahRequest.getIdBidangKeahlian());

        BidangKeahlianSekolah bidangKeahlianSekolah = new BidangKeahlianSekolah();

        bidangKeahlianSekolah.setIdBidangSekolah(bidangKeahlianSekolahRequest.getIdBidangSekolah());
        bidangKeahlianSekolah.setNamaBidangSekolah(bidangKeahlianSekolahRequest.getNamaBidangSekolah());

        bidangKeahlianSekolah.setSchool(schoolResponse);
        bidangKeahlianSekolah.setBidangKeahlian(bidangKeahlianResponse);

        return bidangKeahlianSekolahRepository.save(bidangKeahlianSekolah);

    }

    public DefaultResponse<BidangKeahlianSekolah> getBidangKeahlianSekolahById(String bidangKeahlianSekolahId)
            throws IOException {
        BidangKeahlianSekolah bidangKeahlianSekolahResponse = bidangKeahlianSekolahRepository
                .findBidangKeahlianSekolahById(bidangKeahlianSekolahId);
        return new DefaultResponse<>(bidangKeahlianSekolahResponse.isValid() ? bidangKeahlianSekolahResponse : null,
                bidangKeahlianSekolahResponse.isValid() ? 1 : 0, "Successfully get data");
    }

    public BidangKeahlianSekolah updateBidangKeahlianSekolah(String bidangKeahlianSekolahId,
            BidangKeahlianSekolahRequest bidangKeahlianSekolahRequest) throws IOException {

        BidangKeahlianSekolah bidangKeahlianSekolah = new BidangKeahlianSekolah();

        School schoolResponse = schoolRepository.findById(bidangKeahlianSekolahRequest.getIdSekolah());
        BidangKeahlian bidangKeahlianResponse = bidangKeahlianRepository
                .findById(bidangKeahlianSekolahRequest.getIdBidangKeahlian());

        if (schoolResponse.getIdSchool() != null) {
            bidangKeahlianSekolah.setNamaBidangSekolah(bidangKeahlianSekolahRequest.getNamaBidangSekolah());
            bidangKeahlianSekolah.setSchool(schoolResponse);
            bidangKeahlianSekolah.setBidangKeahlian(bidangKeahlianResponse);

            return bidangKeahlianSekolahRepository.update(bidangKeahlianSekolahId, bidangKeahlianSekolah);
        } else {
            return null;
        }

    }

    public void deleteBidangKeahlianSekolahById(String bidangKeahlianSekolahId) throws IOException {
        BidangKeahlianSekolah bidangKeahlianSekolahResponse = bidangKeahlianSekolahRepository
                .findBidangKeahlianSekolahById(bidangKeahlianSekolahId);
        if (bidangKeahlianSekolahResponse.isValid()) {
            bidangKeahlianSekolahRepository.deleteById(bidangKeahlianSekolahId);
        } else {
            throw new ResourceNotFoundException("Bidang Keahlian Sekolah", "id",
                    bidangKeahlianSekolahId);
        }
    }
}
