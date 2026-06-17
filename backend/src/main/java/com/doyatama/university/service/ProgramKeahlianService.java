/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.BidangKeahlian;
import com.doyatama.university.model.ProgramKeahlian;
import com.doyatama.university.payload.ProgramKeahlianRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.BidangKeahlianRepository;
import com.doyatama.university.repository.ProgramKeahlianRepository;
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
public class ProgramKeahlianService {
    private ProgramKeahlianRepository programKeahlianRepository = new ProgramKeahlianRepository();
    private BidangKeahlianRepository bidangKeahlianRepository = new BidangKeahlianRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(ProgramKeahlianService.class);

    public PagedResponse<ProgramKeahlian> getAllProgramKeahlian(int page, int size, String bidangId)
            throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<ProgramKeahlian> programKeahlianResponse = new ArrayList<>();

        if (bidangId.equalsIgnoreCase("*")) {
            programKeahlianResponse = programKeahlianRepository.findAll(size);
        } else {
            programKeahlianResponse = programKeahlianRepository.findProgramByBidang(bidangId, size);
        }

        return new PagedResponse<>(programKeahlianResponse, programKeahlianResponse.size(), "Successfully get data",
                200);
    }

    public ProgramKeahlian createProgramKeahlian(ProgramKeahlianRequest programKeahlianRequest) throws IOException {
        BidangKeahlian bidang = bidangKeahlianRepository.findById(programKeahlianRequest.getBidangKeahlian_id());

        ProgramKeahlian programKeahlian = new ProgramKeahlian();
        programKeahlian.setId(programKeahlianRequest.getId());
        programKeahlian.setProgram(programKeahlianRequest.getProgram());
        programKeahlian.setBidangKeahlian(bidang);
        return programKeahlianRepository.save(programKeahlian);
    }

    public DefaultResponse<ProgramKeahlian> getProgramKeahlianById(String BDGid) throws IOException {
        // Retrieve ProgramKeahlian
        ProgramKeahlian programKeahlian = programKeahlianRepository.findById(BDGid);
        return new DefaultResponse<>(programKeahlian.isValid() ? programKeahlian : null,
                programKeahlian.isValid() ? 1 : 0, "Successfully get data");
    }

    public ProgramKeahlian updateProgramKeahlian(String BDGid, ProgramKeahlianRequest programKeahlianRequest)
            throws IOException {
        ProgramKeahlian programKeahlian = new ProgramKeahlian();

        programKeahlian.setProgram(programKeahlianRequest.getProgram());

        return programKeahlianRepository.update(BDGid, programKeahlian);
    }

    public void deleteProgramKeahlianById(String BDGid) throws IOException {
        ProgramKeahlian programKeahlianResponse = programKeahlianRepository.findById(BDGid);
        if (programKeahlianResponse.isValid()) {
            programKeahlianRepository.deleteById(BDGid);
        } else {
            throw new ResourceNotFoundException("ProgramKeahlian", "id", BDGid);
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
