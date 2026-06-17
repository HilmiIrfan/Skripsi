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
import com.doyatama.university.payload.ElemenRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.ElemenRepository;
import com.doyatama.university.repository.KelasRepository;
import com.doyatama.university.repository.KonsentrasiKeahlianSekolahRepository;
import com.doyatama.university.repository.MapelRepository;
import com.doyatama.university.repository.SchoolRepository;
import com.doyatama.university.repository.SemesterRepository;
import com.doyatama.university.repository.TahunAjaranRepository;
import com.doyatama.university.repository.AcpRepository;
import com.doyatama.university.repository.AtpRepository;
import com.doyatama.university.repository.BankSoalRepository;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class ElemenService {

    private ElemenRepository elemenRepository = new ElemenRepository();
    private TahunAjaranRepository tahunAjaranRepository = new TahunAjaranRepository();
    private KelasRepository kelasRepository = new KelasRepository();
    private SemesterRepository semesterRepository = new SemesterRepository();
    private MapelRepository mapelRepository = new MapelRepository();
    private KonsentrasiKeahlianSekolahRepository konsentrasiKeahlianSekolahRepository = new KonsentrasiKeahlianSekolahRepository();
    private SchoolRepository schoolRepository = new SchoolRepository();
    private AcpRepository acpRepository = new AcpRepository();
    private AtpRepository atpRepository = new AtpRepository();
    private BankSoalRepository bankSoalRepository = new BankSoalRepository();

    public PagedResponse<Elemen> getAllElemen(int page, int size, String mapelID, String tahunAjaranID,
            String semesterID, String kelasID, String konsentrasiKeahlianSekolahID, String schoolID)
            throws IOException {
        validatePageNumberAndSize(page, size);

        List<Elemen> elemenResponse;

        if (schoolID.equalsIgnoreCase("*")) {
            elemenResponse = elemenRepository.findAll(size);
        } else {
            elemenResponse = elemenRepository.findElemenBySekolah(schoolID, size);
        }

        return new PagedResponse<>(elemenResponse, elemenResponse.size(), "Successfully get data", 200);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    public Elemen createElemen(ElemenRequest elemenRequest) throws IOException {

        if (elemenRequest.getIdElemen() == null) {
            elemenRequest.setIdElemen(UUID.randomUUID().toString());
        }

        if (elemenRepository.existsById(elemenRequest.getIdElemen())) {
            throw new IllegalArgumentException("Elemen already exist");
        }

        TahunAjaran tahunAjaranResponse = tahunAjaranRepository.findById(elemenRequest.getIdTahun());
        Kelas kelasResponse = kelasRepository.findById(elemenRequest.getIdKelas());
        Semester semesterResponse = semesterRepository.findById(elemenRequest.getIdSemester());
        Mapel mapelResponse = mapelRepository.findById(elemenRequest.getIdMapel());
        KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolahResponse = konsentrasiKeahlianSekolahRepository
                .findById(elemenRequest.getIdKonsentrasiSekolah());
        School schoolResponse = schoolRepository.findById(elemenRequest.getIdSekolah());

        Elemen elemen = new Elemen();

        elemen.setIdElemen(elemenRequest.getIdElemen());
        elemen.setNamaElemen(elemenRequest.getNamaElemen());
        elemen.setTahunAjaran(tahunAjaranResponse);
        elemen.setKelas(kelasResponse);
        elemen.setSemester(semesterResponse);
        elemen.setMapel(mapelResponse);
        elemen.setKonsentrasiKeahlianSekolah(konsentrasiKeahlianSekolahResponse);
        elemen.setSchool(schoolResponse);

        return elemenRepository.save(elemen);
    }

    public DefaultResponse<Elemen> getElemenById(String elemenId) throws IOException {
        Elemen elemenResponse = elemenRepository.findElemenById(elemenId);
        return new DefaultResponse<>(elemenResponse.isValid() ? elemenResponse : null, elemenResponse.isValid() ? 1 : 0,
                "Successfully get data");
    }

    public Elemen updateElemen(String elemenId, ElemenRequest elemenRequest) throws IOException {
        Elemen elemen = new Elemen();
        TahunAjaran tahunAjaranResponse = tahunAjaranRepository.findById(elemenRequest.getIdTahun());
        Kelas kelasResponse = kelasRepository.findById(elemenRequest.getIdKelas());
        Semester semesterResponse = semesterRepository.findById(elemenRequest.getIdSemester());
        Mapel mapelResponse = mapelRepository.findById(elemenRequest.getIdMapel());
        KonsentrasiKeahlianSekolah konsentrasiKeahlianSekolahResponse = konsentrasiKeahlianSekolahRepository
                .findById(elemenRequest.getIdKonsentrasiSekolah());
        School schoolResponse = schoolRepository.findById(elemenRequest.getIdSekolah());

        if (schoolResponse.getIdSchool() != null) {
            // Get current elemen data to check if nama changed
            Elemen currentElemen = elemenRepository.findElemenById(elemenId);
            String oldNamaElemen = currentElemen.getNamaElemen();
            String newNamaElemen = elemenRequest.getNamaElemen();

            elemen.setNamaElemen(elemenRequest.getNamaElemen());
            elemen.setTahunAjaran(tahunAjaranResponse);
            elemen.setKelas(kelasResponse);
            elemen.setSemester(semesterResponse);
            elemen.setMapel(mapelResponse);
            elemen.setKonsentrasiKeahlianSekolah(konsentrasiKeahlianSekolahResponse);
            elemen.setSchool(schoolResponse);

            Elemen updatedElemen = elemenRepository.update(elemenId, elemen);

            // Cascade update if nama elemen changed
            if (updatedElemen != null && !oldNamaElemen.equals(newNamaElemen)) {
                try {
                    // Update nama elemen in ACP records
                    acpRepository.updateNamaElemenByElemenId(elemenId, newNamaElemen);

                    // Update nama elemen in ATP records
                    atpRepository.updateNamaElemenByElemenId(elemenId, newNamaElemen);

                    // Update nama elemen in BankSoal records
                    bankSoalRepository.updateNamaElemenByElemenId(elemenId, newNamaElemen);

                    System.out.println("Cascade update completed for elemen: " + elemenId +
                            ", new nama: " + newNamaElemen);
                } catch (Exception e) {
                    System.err.println("Error during cascade update for elemen " + elemenId + ": " + e.getMessage());
                    // Continue execution even if cascade update fails
                }
            }

            return updatedElemen;
        } else {
            return null;
        }
    }

    public void deleteElemenById(String elemenId) throws IOException {
        Elemen elemenResponse = elemenRepository.findElemenById(elemenId);
        if (elemenResponse.isValid()) {
            elemenRepository.deleteById(elemenId);
        } else {
            throw new ResourceNotFoundException("Elemen", "id", elemenId);
        }
    }

}
