/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.Kelas;
import com.doyatama.university.model.Mapel;
import com.doyatama.university.model.School;
import com.doyatama.university.model.Semester;
import com.doyatama.university.model.TahunAjaran;
import com.doyatama.university.payload.MapelRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.KelasRepository;
import com.doyatama.university.repository.MapelRepository;
import com.doyatama.university.repository.SchoolRepository;
import com.doyatama.university.repository.SemesterRepository;
import com.doyatama.university.repository.TahunAjaranRepository;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class MapelService {
    private MapelRepository mapelRepository = new MapelRepository();
    private SchoolRepository schoolRepository = new SchoolRepository();
    private KelasRepository kelasRepository = new KelasRepository();
    private SemesterRepository semesterRepository = new SemesterRepository();
    private TahunAjaranRepository tahunAjaranRepository = new TahunAjaranRepository();

    public PagedResponse<Mapel> getAllMapel(int page, int size, String schoolID) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Mapel
        List<Mapel> mapelResponse;

        if (schoolID.equalsIgnoreCase("*")) {
            mapelResponse = mapelRepository.findAll(size);
        } else {
            mapelResponse = mapelRepository.findMapelBySekolah(schoolID, size);
        }

        return new PagedResponse<>(mapelResponse, mapelResponse.size(), "Successfully get data", 200);
    }

    public Mapel createMapel(MapelRequest mapelRequest) throws IOException {

        if (mapelRequest.getIdMapel() == null) {
            mapelRequest.setIdMapel(UUID.randomUUID().toString());
        }

        if (mapelRepository.existsById(mapelRequest.getIdMapel())) {
            throw new IllegalArgumentException("Mapel already exist");
        }

        School schoolResponse = schoolRepository.findById(mapelRequest.getIdSekolah());
        Kelas kelasResponse = kelasRepository.findById(mapelRequest.getIdKelas());
        Semester semesterResponse = semesterRepository.findById(mapelRequest.getIdSemester());
        TahunAjaran tahunAjaranResponse = tahunAjaranRepository.findById(mapelRequest.getIdTahun());

        Mapel mapel = new Mapel();
        mapel.setIdMapel(mapelRequest.getIdMapel());
        mapel.setName(mapelRequest.getName());
        mapel.setSchool(schoolResponse);
        mapel.setTahunAjaran(tahunAjaranResponse);
        mapel.setSemester(semesterResponse);
        mapel.setKelas(kelasResponse);
        return mapelRepository.save(mapel);
    }

    public DefaultResponse<Mapel> getMapelById(String mapelId) throws IOException {
        // Retrieve Mapel
        Mapel mapel = mapelRepository.findById(mapelId);
        return new DefaultResponse<>(mapel.isValid() ? mapel : null, mapel.isValid() ? 1 : 0, "Successfully get data");
    }

    public Mapel updateMapel(String mapelId, MapelRequest mapelRequest) throws IOException {
        Mapel mapel = new Mapel();

        School schoolResponse = schoolRepository.findById(mapelRequest.getIdSekolah());
        Semester semesterResponse = semesterRepository.findById(mapelRequest.getIdSemester());
        Kelas kelasResponse = kelasRepository.findById(mapelRequest.getIdKelas());
        TahunAjaran tahunAjaranResponse = tahunAjaranRepository.findById(mapelRequest.getIdTahun());

        if (schoolResponse.getIdSchool() != null) {
            mapel.setName(mapelRequest.getName());
            mapel.setSchool(schoolResponse);
            mapel.setTahunAjaran(tahunAjaranResponse);
            mapel.setSemester(semesterResponse);
            mapel.setKelas(kelasResponse);
            return mapelRepository.update(mapelId, mapel);
        } else {
            return null;
        }
    }

    public void deleteMapelById(String mapelId) throws IOException {
        Mapel mapelResponse = mapelRepository.findById(mapelId);
        if (mapelResponse.isValid()) {
            mapelRepository.deleteById(mapelId);
        } else {
            throw new ResourceNotFoundException("Mapel", "id", mapelId);
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
