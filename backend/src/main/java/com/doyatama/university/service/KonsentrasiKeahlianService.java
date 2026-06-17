/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.KonsentrasiKeahlian;
import com.doyatama.university.model.ProgramKeahlian;
import com.doyatama.university.payload.KonsentrasiKeahlianRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.KonsentrasiKeahlianRepository;
import com.doyatama.university.repository.ProgramKeahlianRepository;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author senja
 */

@Service
public class KonsentrasiKeahlianService {
    private KonsentrasiKeahlianRepository konsentrasiKeahlianRepository = new KonsentrasiKeahlianRepository();
    private ProgramKeahlianRepository programKeahlianRepository = new ProgramKeahlianRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(KonsentrasiKeahlianService.class);

    public PagedResponse<KonsentrasiKeahlian> getAllKonsentrasiKeahlian(int page, int size, String programId)
            throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<KonsentrasiKeahlian> konsentrasiKeahlianResponse = new ArrayList<>();

        if (programId.equalsIgnoreCase("*")) {
            konsentrasiKeahlianResponse = konsentrasiKeahlianRepository.findAll(size);
        } else {
            konsentrasiKeahlianResponse = konsentrasiKeahlianRepository.findKonsentrasiByProgram(programId, size);
        }

        return new PagedResponse<>(konsentrasiKeahlianResponse, konsentrasiKeahlianResponse.size(),
                "Successfully get data", 200);
    }

    public KonsentrasiKeahlian createKonsentrasiKeahlian(KonsentrasiKeahlianRequest konsentrasiKeahlianRequest)
            throws IOException {
        ProgramKeahlian program = programKeahlianRepository
                .findById(konsentrasiKeahlianRequest.getProgramKeahlian_id());

        KonsentrasiKeahlian konsentrasiKeahlian = new KonsentrasiKeahlian();
        konsentrasiKeahlian.setId(konsentrasiKeahlianRequest.getId());
        konsentrasiKeahlian.setKonsentrasi(konsentrasiKeahlianRequest.getKonsentrasi());
        konsentrasiKeahlian.setProgramKeahlian(program);
        return konsentrasiKeahlianRepository.save(konsentrasiKeahlian);
    }

    public DefaultResponse<KonsentrasiKeahlian> getKonsentrasiKeahlianById(String BDGid) throws IOException {
        // Retrieve KonsentrasiKeahlian
        KonsentrasiKeahlian konsentrasiKeahlian = konsentrasiKeahlianRepository.findById(BDGid);
        return new DefaultResponse<>(konsentrasiKeahlian.isValid() ? konsentrasiKeahlian : null,
                konsentrasiKeahlian.isValid() ? 1 : 0, "Successfully get data");
    }

    public KonsentrasiKeahlian updateKonsentrasiKeahlian(String BDGid,
            KonsentrasiKeahlianRequest konsentrasiKeahlianRequest) throws IOException {
        KonsentrasiKeahlian konsentrasiKeahlian = new KonsentrasiKeahlian();
        konsentrasiKeahlian.setKonsentrasi(konsentrasiKeahlianRequest.getKonsentrasi());
        return konsentrasiKeahlianRepository.update(BDGid, konsentrasiKeahlian);
    }

    public void deleteKonsentrasiKeahlianById(String BDGid) throws IOException {
        KonsentrasiKeahlian konsentrasiKeahlianResponse = konsentrasiKeahlianRepository.findById(BDGid);
        if (konsentrasiKeahlianResponse.isValid()) {
            konsentrasiKeahlianRepository.deleteById(BDGid);
        } else {
            throw new ResourceNotFoundException("KonsentrasiKeahlian", "id", BDGid);
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
