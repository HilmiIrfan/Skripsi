package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.Kelas;
import com.doyatama.university.model.Mapel;
import com.doyatama.university.model.Modul;
import com.doyatama.university.model.School;
import com.doyatama.university.model.Semester;
import com.doyatama.university.model.TahunAjaran;
import com.doyatama.university.payload.ModulRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.KelasRepository;
import com.doyatama.university.repository.MapelRepository;
import com.doyatama.university.repository.ModulRepository;
import com.doyatama.university.repository.SchoolRepository;
import com.doyatama.university.repository.SemesterRepository;
import com.doyatama.university.repository.TahunAjaranRepository;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.hadoop.hbase.exceptions.IllegalArgumentIOException;

public class ModulService {
    private ModulRepository modulRepository = new ModulRepository();
    private SchoolRepository schoolRepository = new SchoolRepository();
    private MapelRepository mapelRepository = new MapelRepository();
    private TahunAjaranRepository tahunAjaranRepository = new TahunAjaranRepository();
    private SemesterRepository semesterRepository = new SemesterRepository();
    private KelasRepository kelasRepository = new KelasRepository();

    public PagedResponse<Modul> getAllModul(int page, int size, String mapelID, String tahunAjaranID, String semesterID,
            String kelasID, String schoolID) throws IOException {
        validatePageNumberAndSize(page, size);

        List<Modul> modulResponse;

        if (schoolID.equalsIgnoreCase("*")) {
            modulResponse = modulRepository.findAll(size);
        } else {
            modulResponse = modulRepository.findModulBySekolah(schoolID, size);
        }

        return new PagedResponse<>(modulResponse, modulResponse.size(), "Successfully get data", 200);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    public Modul createModul(ModulRequest modulRequest) throws IOException {

        if (modulRequest.getIdModul() == null) {
            modulRequest.setIdModul(UUID.randomUUID().toString());
        }

        if (modulRepository.existsById(modulRequest.getIdModul())) {
            throw new IllegalArgumentIOException("Modul with ID already exists.");
        }

        Mapel mapelResponse = mapelRepository.findById(modulRequest.getIdMapel());
        School schoolResponse = schoolRepository.findById(modulRequest.getIdSekolah());
        TahunAjaran tahunAjaranResponse = tahunAjaranRepository.findById(modulRequest.getIdTahun());
        Semester semesterResponse = semesterRepository.findById(modulRequest.getIdSemester());
        Kelas kelasResponse = kelasRepository.findById(modulRequest.getIdKelas());

        Modul modul = new Modul();
        modul.setIdModul(modulRequest.getIdModul());
        modul.setNamaModul(modulRequest.getNamaModul());
        modul.setTahunAjaran(tahunAjaranResponse);
        modul.setSemester(semesterResponse);
        modul.setKelas(kelasResponse);
        modul.setMapel(mapelResponse);
        modul.setSchool(schoolResponse);

        return modulRepository.save(modul);
    }

    public DefaultResponse<Modul> getModulById(String modulId) throws IOException {
        Modul modulResponse = modulRepository.findModulById(modulId);
        return new DefaultResponse<>(modulResponse.isValid() ? modulResponse : null, modulResponse.isValid() ? 1 : 0,
                "Successfully get data");
    }

    public Modul updateModul(String modulId, ModulRequest modulRequest) throws IOException {

        Modul modul = new Modul();
        TahunAjaran tahunAjaranResponse = tahunAjaranRepository.findById(modulRequest.getIdTahun());
        Kelas kelasResponse = kelasRepository.findById(modulRequest.getIdKelas());
        Semester semesterResponse = semesterRepository.findById(modulRequest.getIdSemester());
        Mapel mapelResponse = mapelRepository.findById(modulRequest.getIdMapel());
        School schoolResponse = schoolRepository.findById(modulRequest.getIdSekolah());

        if (schoolResponse.getIdSchool() != null) {
            modul.setNamaModul(modulRequest.getNamaModul());
            modul.setTahunAjaran(tahunAjaranResponse);
            modul.setSemester(semesterResponse);
            modul.setKelas(kelasResponse);
            modul.setMapel(mapelResponse);
            modul.setSchool(schoolResponse);

            return modulRepository.update(modulId, modul);
        } else {
            return null;

        }
    }

    public void deleteModulById(String modulId) throws IOException {
        Modul modulResponse = modulRepository.findModulById(modulId);
        if (modulResponse.isValid()) {
            modulRepository.deleteById(modulId);
        } else {
            throw new ResourceNotFoundException("Modul", "id", modulId);
        }

    }

}
