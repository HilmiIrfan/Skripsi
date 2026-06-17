package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.Elemen;
import com.doyatama.university.model.Kelas;
import com.doyatama.university.model.TahunAjaran;
import com.doyatama.university.model.Semester;
import com.doyatama.university.model.Mapel;
import com.doyatama.university.model.School;
import com.doyatama.university.model.KonsentrasiKeahlianSekolah;
import com.doyatama.university.model.Acp;
import com.doyatama.university.payload.AcpRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.AcpRepository;
import com.doyatama.university.repository.AtpRepository;
import com.doyatama.university.repository.BankSoalRepository;
import com.doyatama.university.repository.ElemenRepository;
import com.doyatama.university.repository.KelasRepository;
import com.doyatama.university.repository.KonsentrasiKeahlianSekolahRepository;
import com.doyatama.university.repository.MapelRepository;
import com.doyatama.university.repository.SchoolRepository;
import com.doyatama.university.repository.SemesterRepository;
import com.doyatama.university.repository.TahunAjaranRepository;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class AcpService {

    private AcpRepository acpRepository = new AcpRepository();
    private AtpRepository atpRepository = new AtpRepository();
    private BankSoalRepository bankSoalRepository = new BankSoalRepository();
    private ElemenRepository elemenRepository = new ElemenRepository();
    private TahunAjaranRepository tahunAjaranRepository = new TahunAjaranRepository();
    private KelasRepository kelasRepository = new KelasRepository();
    private SemesterRepository semesterRepository = new SemesterRepository();
    private MapelRepository mapelRepository = new MapelRepository();
    private KonsentrasiKeahlianSekolahRepository konsentrasiKeahlianSekolahRepository = new KonsentrasiKeahlianSekolahRepository();
    private SchoolRepository schoolRepository = new SchoolRepository();

    public PagedResponse<Acp> getAllAcp(int page, int size, String tahunAjaranID, String semesterID,
            String kelasID, String mapelID, String konsentrasiKeahlianSekolahID, String elemenID, String schoolID)
            throws IOException {
        validatePageNumberAndSize(page, size);

        List<Acp> acpResponse;

        if (schoolID.equalsIgnoreCase("*")) {
            acpResponse = acpRepository.findAll(size);
        } else {
            acpResponse = acpRepository.findAcpBySekolah(schoolID, size);
        }

        return new PagedResponse<>(acpResponse, acpResponse.size(), "Successfully get data", 200);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    public Acp createAcp(AcpRequest acpRequest) throws IOException {
        if (acpRequest.getIdAcp() == null) {
            acpRequest.setIdAcp(UUID.randomUUID().toString());
        }

        if (acpRepository.existsById(acpRequest.getIdAcp())) {
            throw new BadRequestException("Acp already exists");
        }

        TahunAjaran tahunAjaranResponse = tahunAjaranRepository.findById(acpRequest.getIdTahun());
        Kelas kelasResponse = kelasRepository.findById(acpRequest.getIdKelas());
        Semester semesterResponse = semesterRepository.findById(acpRequest.getIdSemester());
        Mapel mapelResponse = mapelRepository.findById(acpRequest.getIdMapel());
        KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolahResponse = konsentrasiKeahlianSekolahRepository
                .findById(acpRequest.getIdKonsentrasiSekolah());
        Elemen elemenResponse = elemenRepository.findById(acpRequest.getIdElemen());
        School schoolResponse = schoolRepository.findById(acpRequest.getIdSchool());

        Acp acp = new Acp();
        acp.setIdAcp(acpRequest.getIdAcp());
        acp.setNamaAcp(acpRequest.getNamaAcp());
        acp.setTahunAjaran(tahunAjaranResponse);
        acp.setSemester(semesterResponse);
        acp.setKelas(kelasResponse);
        acp.setMapel(mapelResponse);
        acp.setKonsentrasiKeahlianSekolah(konsentrasiKeahlianSekolahResponse);
        acp.setElemen(elemenResponse);
        acp.setSchool(schoolResponse);

        return acpRepository.save(acp);
    }

    public DefaultResponse<Acp> getAcpById(String acpId) throws IOException {
        Acp acpResponse = acpRepository.findAcpById(acpId);
        return new DefaultResponse<>(acpResponse.isValid() ? acpResponse : null, acpResponse.isValid() ? 1 : 0,
                "Successfully get data");
    }

    public Acp updateAcp(String acpId, AcpRequest acpRequest) throws IOException {

        Acp acp = new Acp();

        TahunAjaran tahunAjaranResponse = tahunAjaranRepository.findById(acpRequest.getIdTahun());
        Kelas kelasResponse = kelasRepository.findById(acpRequest.getIdKelas());
        Semester semesterResponse = semesterRepository.findById(acpRequest.getIdSemester());
        Mapel mapelResponse = mapelRepository.findById(acpRequest.getIdMapel());
        KonsentrasiKeahlianSekolah konsentrasiKeahlianResponse = konsentrasiKeahlianSekolahRepository
                .findById(acpRequest.getIdKonsentrasiSekolah());
        Elemen elemenResponse = elemenRepository.findById(acpRequest.getIdElemen());
        School schoolResponse = schoolRepository.findById(acpRequest.getIdSchool());

        if (schoolResponse.getIdSchool() != null) {
            // Get current ACP data to check if nama changed
            Acp currentAcp = acpRepository.findAcpById(acpId);
            String oldNamaAcp = currentAcp.getNamaAcp();
            String newNamaAcp = acpRequest.getNamaAcp();

            acp.setNamaAcp(acpRequest.getNamaAcp());
            acp.setTahunAjaran(tahunAjaranResponse);
            acp.setKelas(kelasResponse);
            acp.setSemester(semesterResponse);
            acp.setMapel(mapelResponse);
            acp.setKonsentrasiKeahlianSekolah(konsentrasiKeahlianResponse);
            acp.setElemen(elemenResponse);
            acp.setSchool(schoolResponse);

            Acp updatedAcp = acpRepository.update(acpId, acp);

            // Cascade update if nama ACP changed
            if (updatedAcp != null && !oldNamaAcp.equals(newNamaAcp)) {
                try {
                    // Update nama ACP in ATP records
                    atpRepository.updateNamaAcpByAcpId(acpId, newNamaAcp);

                    // Update nama ACP in BankSoal records
                    bankSoalRepository.updateNamaAcpByAcpId(acpId, newNamaAcp);

                    System.out.println("Cascade update completed for ACP: " + acpId +
                            ", new nama: " + newNamaAcp);
                } catch (Exception e) {
                    System.err.println("Error during cascade update for ACP " + acpId + ": " + e.getMessage());
                    // Continue execution even if cascade update fails
                }
            }

            return updatedAcp;
        } else {
            return null;
        }
    }

    public void deleteAcpById(String acpId) throws IOException {
        Acp acpResponse = acpRepository.findAcpById(acpId);
        if (acpResponse.isValid()) {
            acpRepository.deleteById(acpId);
        } else {
            throw new ResourceNotFoundException("Acp", "id", acpId);
        }
    }
}
