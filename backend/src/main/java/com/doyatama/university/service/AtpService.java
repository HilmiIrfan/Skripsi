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
import com.doyatama.university.model.Atp;
import com.doyatama.university.payload.AtpRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.AtpRepository;
import com.doyatama.university.repository.AcpRepository;
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

public class AtpService {

    private AtpRepository atpRepository = new AtpRepository();
    private AcpRepository acpRepository = new AcpRepository();
    private BankSoalRepository bankSoalRepository = new BankSoalRepository();
    private ElemenRepository elemenRepository = new ElemenRepository();
    private TahunAjaranRepository tahunAjaranRepository = new TahunAjaranRepository();
    private KelasRepository kelasRepository = new KelasRepository();
    private SemesterRepository semesterRepository = new SemesterRepository();
    private MapelRepository mapelRepository = new MapelRepository();
    private KonsentrasiKeahlianSekolahRepository konsentrasiKeahlianSekolahRepository = new KonsentrasiKeahlianSekolahRepository();
    private SchoolRepository schoolRepository = new SchoolRepository();

    public PagedResponse<Atp> getAllAtp(int page, int size, String tahunAjaranID, String semesterID,
            String kelasID, String mapelID, String konsentrasiKeahlianSekolahID, String elemenID, String acpID,
            String schoolID)
            throws IOException {
        validatePageNumberAndSize(page, size);

        List<Atp> atpResponse;

        if (schoolID.equalsIgnoreCase("*")) {
            atpResponse = atpRepository.findAll(size);
        } else {
            atpResponse = atpRepository.findAtpBySekolah(schoolID, size);
        }

        return new PagedResponse<>(atpResponse, atpResponse.size(), "Successfully get data", 200);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    public Atp createAtp(AtpRequest atpRequest) throws IOException {

        if (atpRequest.getIdAtp() == null) {
            atpRequest.setIdAtp(UUID.randomUUID().toString());
        }

        if (atpRepository.existsById(atpRequest.getIdAtp())) {
            throw new BadRequestException("Atp already exists");
        }

        TahunAjaran tahunAjaranResponse = tahunAjaranRepository.findById(atpRequest.getIdTahun());
        Kelas kelasResponse = kelasRepository.findById(atpRequest.getIdKelas());
        Semester semesterResponse = semesterRepository.findById(atpRequest.getIdSemester());
        Mapel mapelResponse = mapelRepository.findById(atpRequest.getIdMapel());
        KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolahResponse = konsentrasiKeahlianSekolahRepository
                .findById(atpRequest.getIdKonsentrasiSekolah());
        Elemen elemenResponse = elemenRepository.findById(atpRequest.getIdElemen());
        Acp acpResponse = acpRepository.findById(atpRequest.getIdAcp());
        School schoolResponse = schoolRepository.findById(atpRequest.getIdSekolah());

        Atp atp = new Atp();
        atp.setIdAtp(atpRequest.getIdAtp());
        atp.setNamaAtp(atpRequest.getNamaAtp());
        atp.setJumlahJpl(atpRequest.getJumlahJpl());
        atp.setTahunAjaran(tahunAjaranResponse);
        atp.setSemester(semesterResponse);
        atp.setKelas(kelasResponse);
        atp.setMapel(mapelResponse);
        atp.setKonsentrasiKeahlianSekolah(konsentrasiKeahlianSekolahResponse);
        atp.setElemen(elemenResponse);
        atp.setAcp(acpResponse);
        atp.setSchool(schoolResponse);

        return atpRepository.save(atp);
    }

    public DefaultResponse<Atp> getAtpById(String atpId) throws IOException {
        Atp atpResponse = atpRepository.findAtpById(atpId);
        return new DefaultResponse<>(atpResponse.isValid() ? atpResponse : null, atpResponse.isValid() ? 1 : 0,
                "Successfully get data");
    }

    public Atp updateAtp(String atpId, AtpRequest atpRequest) throws IOException {
        System.out.println("Atp ID: " + atpId);

        Atp atp = new Atp();

        TahunAjaran tahunAjaranResponse = tahunAjaranRepository.findById(atpRequest.getIdTahun());
        Kelas kelasResponse = kelasRepository.findById(atpRequest.getIdKelas());
        Semester semesterResponse = semesterRepository.findById(atpRequest.getIdSemester());
        Mapel mapelResponse = mapelRepository.findById(atpRequest.getIdMapel());
        KonsentrasiKeahlianSekolah konsentrasiKeahlianResponse = konsentrasiKeahlianSekolahRepository
                .findById(atpRequest.getIdKonsentrasiSekolah());
        Elemen elemenResponse = elemenRepository.findById(atpRequest.getIdElemen());
        Acp acpResponse = acpRepository.findById(atpRequest.getIdAcp());
        School schoolResponse = schoolRepository.findById(atpRequest.getIdSekolah());

        if (schoolResponse.getIdSchool() != null) {
            // Get current ATP data to check if nama changed
            Atp currentAtp = atpRepository.findAtpById(atpId);
            String oldNamaAtp = currentAtp.getNamaAtp();
            String newNamaAtp = atpRequest.getNamaAtp();

            atp.setNamaAtp(atpRequest.getNamaAtp());
            atp.setJumlahJpl(atpRequest.getJumlahJpl());
            atp.setTahunAjaran(tahunAjaranResponse);
            atp.setSemester(semesterResponse);
            atp.setKelas(kelasResponse);
            atp.setMapel(mapelResponse);
            atp.setKonsentrasiKeahlianSekolah(konsentrasiKeahlianResponse);
            atp.setElemen(elemenResponse);
            atp.setAcp(acpResponse);
            atp.setSchool(schoolResponse);

            Atp updatedAtp = atpRepository.update(atpId, atp);

            // Cascade update if nama ATP changed
            if (updatedAtp != null && !oldNamaAtp.equals(newNamaAtp)) {
                try {
                    // Update nama ATP in BankSoal records
                    bankSoalRepository.updateNamaAtpByAtpId(atpId, newNamaAtp);

                    System.out.println("Cascade update completed for ATP: " + atpId +
                            ", new nama: " + newNamaAtp);
                } catch (Exception e) {
                    System.err.println("Error during cascade update for ATP " + atpId + ": " + e.getMessage());
                    // Continue execution even if cascade update fails
                }
            }

            return updatedAtp;
        } else {
            return null;
        }
    }

    public void deleteAtpById(String atpId) throws IOException {
        Atp atpResponse = atpRepository.findAtpById(atpId);
        if (atpResponse.isValid()) {
            atpRepository.deleteById(atpId);
        } else {
            throw new ResourceNotFoundException("Atp", "id", atpId);

        }
    }
}
