package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.Ujian;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository

public class UjianRepository {

    Configuration conf = HBaseConfiguration.create();
    String tableName = "ujian";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Ujian> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUjian = TableName.valueOf(tableName);
        Map<String, String> columnMapping = getStandardColumnMapping();

        // Define indexed fields
        Map<String, String> indexedFields = getIndexedFields();

        return client.showListTableIndex(tableUjian.toString(), columnMapping, Ujian.class, indexedFields, size);
    }

    public Ujian save(Ujian ujian) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        String rowKey = ujian.getIdUjian();
        TableName tableUjian = TableName.valueOf(tableName);

        // Save main info
        saveMainInfo(client, tableUjian, rowKey, ujian);

        // Save relationships
        saveRelationships(client, tableUjian, rowKey, ujian);

        // Save settings and configurations
        saveSettings(client, tableUjian, rowKey, ujian);

        // Save bank soal list
        saveBankSoalList(client, tableUjian, rowKey, ujian);

        client.insertRecord(tableUjian, rowKey, "detail", "created_by", "Polinema");
        return ujian;
    }

    private void saveMainInfo(HBaseCustomClient client, TableName table, String rowKey, Ujian ujian) {
        client.insertRecord(table, rowKey, "main", "idUjian", ujian.getIdUjian());
        client.insertRecord(table, rowKey, "main", "namaUjian", ujian.getNamaUjian());

        if (ujian.getDeskripsi() != null) {
            client.insertRecord(table, rowKey, "main", "deskripsi", ujian.getDeskripsi());
        }

        if (ujian.getDurasiMenit() != null) {
            client.insertRecord(table, rowKey, "main", "durasiMenit", ujian.getDurasiMenit().toString());
        }

        if (ujian.getWaktuMulaiDijadwalkan() != null) {
            client.insertRecord(table, rowKey, "main", "waktuMulaiDijadwalkan",
                    ujian.getWaktuMulaiDijadwalkan().toString());
        }

        if (ujian.getWaktuSelesaiOtomatis() != null) {
            client.insertRecord(table, rowKey, "main", "waktuSelesaiOtomatis",
                    ujian.getWaktuSelesaiOtomatis().toString());
        }

        client.insertRecord(table, rowKey, "main", "statusUjian", ujian.getStatusUjian());

        if (ujian.getIsLive() != null) {
            client.insertRecord(table, rowKey, "main", "isLive", ujian.getIsLive().toString());
        }

        if (ujian.getIsAutoStart() != null) {
            client.insertRecord(table, rowKey, "main", "isAutoStart", ujian.getIsAutoStart().toString());
        }

        if (ujian.getIsAutoEnd() != null) {
            client.insertRecord(table, rowKey, "main", "isAutoEnd", ujian.getIsAutoEnd().toString());
        }

        if (ujian.getJumlahSoal() != null) {
            client.insertRecord(table, rowKey, "main", "jumlahSoal", ujian.getJumlahSoal().toString());
        }

        if (ujian.getTotalBobot() != null) {
            client.insertRecord(table, rowKey, "main", "totalBobot", ujian.getTotalBobot().toString());
        }

        if (ujian.getTipeSoal() != null) {
            client.insertRecord(table, rowKey, "main", "tipeSoal", ujian.getTipeSoal());
        }

        if (ujian.getTampilkanNilai() != null) {
            client.insertRecord(table, rowKey, "main", "tampilkanNilai", ujian.getTampilkanNilai().toString());
        }

        client.insertRecord(table, rowKey, "main", "createdAt", ujian.getCreatedAt().toString());
        client.insertRecord(table, rowKey, "main", "updatedAt", ujian.getUpdatedAt().toString());
    }

    private void saveRelationships(HBaseCustomClient client, TableName table, String rowKey, Ujian ujian) {
        // Save TahunAjaran
        if (ujian.getTahunAjaran() != null && ujian.getTahunAjaran().getIdTahun() != null) {
            client.insertRecord(table, rowKey, "tahunAjaran", "idTahun", ujian.getTahunAjaran().getIdTahun());
            client.insertRecord(table, rowKey, "tahunAjaran", "tahunAjaran", ujian.getTahunAjaran().getTahunAjaran());
        }

        // Save Kelas
        if (ujian.getKelas() != null && ujian.getKelas().getIdKelas() != null) {
            client.insertRecord(table, rowKey, "kelas", "idKelas", ujian.getKelas().getIdKelas());
            client.insertRecord(table, rowKey, "kelas", "namaKelas", ujian.getKelas().getNamaKelas());
        }

        // Save Semester
        if (ujian.getSemester() != null && ujian.getSemester().getIdSemester() != null) {
            client.insertRecord(table, rowKey, "semester", "idSemester", ujian.getSemester().getIdSemester());
            client.insertRecord(table, rowKey, "semester", "namaSemester", ujian.getSemester().getNamaSemester());
        }

        // Save Mapel
        if (ujian.getMapel() != null && ujian.getMapel().getIdMapel() != null) {
            client.insertRecord(table, rowKey, "mapel", "idMapel", ujian.getMapel().getIdMapel());
            client.insertRecord(table, rowKey, "mapel", "name", ujian.getMapel().getName());
        }

        // Save KonsentrasiKeahlianSekolah
        if (ujian.getKonsentrasiKeahlianSekolah() != null
                && ujian.getKonsentrasiKeahlianSekolah().getIdKonsentrasiSekolah() != null) {
            client.insertRecord(table, rowKey, "konsentrasiKeahlianSekolah", "idKonsentrasiSekolah",
                    ujian.getKonsentrasiKeahlianSekolah().getIdKonsentrasiSekolah());
            client.insertRecord(table, rowKey, "konsentrasiKeahlianSekolah", "namaKonsentrasiSekolah",
                    ujian.getKonsentrasiKeahlianSekolah().getNamaKonsentrasiSekolah());
        }

        // Save School
        if (ujian.getSchool() != null && ujian.getSchool().getIdSchool() != null) {
            client.insertRecord(table, rowKey, "school", "idSchool", ujian.getSchool().getIdSchool());
            client.insertRecord(table, rowKey, "school", "nameSchool", ujian.getSchool().getNameSchool());
        }

        // Save CreatedBy User
        if (ujian.getCreatedBy() != null && ujian.getCreatedBy().getId() != null) {
            client.insertRecord(table, rowKey, "createdBy", "id", ujian.getCreatedBy().getId());
            client.insertRecord(table, rowKey, "createdBy", "name", ujian.getCreatedBy().getName());
            client.insertRecord(table, rowKey, "createdBy", "username", ujian.getCreatedBy().getUsername());
        }
    }

    private void saveSettings(HBaseCustomClient client, TableName table, String rowKey, Ujian ujian) {
        try {
            // Save CAT settings
            if (ujian.getIsCatEnabled() != null) {
                client.insertRecord(table, rowKey, "main", "isCatEnabled", ujian.getIsCatEnabled().toString());
            }

            if (ujian.getCatSettings() != null && !ujian.getCatSettings().isEmpty()) {
                String catSettingsJson = objectMapper.writeValueAsString(ujian.getCatSettings());
                client.insertRecord(table, rowKey, "main", "catSettings", catSettingsJson);
            }

            // Save timing settings
            if (ujian.getAllowLateStart() != null) {
                client.insertRecord(table, rowKey, "main", "allowLateStart", ujian.getAllowLateStart().toString());
            }

            if (ujian.getMaxLateStartMinutes() != null) {
                client.insertRecord(table, rowKey, "main", "maxLateStartMinutes",
                        ujian.getMaxLateStartMinutes().toString());
            }

            if (ujian.getShowTimerToParticipants() != null) {
                client.insertRecord(table, rowKey, "main", "showTimerToParticipants",
                        ujian.getShowTimerToParticipants().toString());
            }

            if (ujian.getPreventCheating() != null) {
                client.insertRecord(table, rowKey, "main", "preventCheating", ujian.getPreventCheating().toString());
            }

            if (ujian.getIsFlexibleTiming() != null) {
                client.insertRecord(table, rowKey, "main", "isFlexibleTiming", ujian.getIsFlexibleTiming().toString());
            }

            if (ujian.getBatasAkhirMulai() != null) {
                client.insertRecord(table, rowKey, "main", "batasAkhirMulai", ujian.getBatasAkhirMulai().toString());
            }

            if (ujian.getAutoEndAfterDuration() != null) {
                client.insertRecord(table, rowKey, "main", "autoEndAfterDuration",
                        ujian.getAutoEndAfterDuration().toString());
            }

            if (ujian.getToleransiKeterlambatanMenit() != null) {
                client.insertRecord(table, rowKey, "main", "toleransiKeterlambatanMenit",
                        ujian.getToleransiKeterlambatanMenit().toString());
            }

            // Save question and scoring settings
            if (ujian.getStrategiPemilihanSoal() != null) {
                client.insertRecord(table, rowKey, "main", "strategiPemilihanSoal", ujian.getStrategiPemilihanSoal());
            }

            if (ujian.getMinPassingScore() != null) {
                client.insertRecord(table, rowKey, "main", "minPassingScore", ujian.getMinPassingScore().toString());
            }

            if (ujian.getAllowReview() != null) {
                client.insertRecord(table, rowKey, "main", "allowReview", ujian.getAllowReview().toString());
            }

            if (ujian.getAllowBacktrack() != null) {
                client.insertRecord(table, rowKey, "main", "allowBacktrack", ujian.getAllowBacktrack().toString());
            }

            if (ujian.getMaxAttempts() != null) {
                client.insertRecord(table, rowKey, "main", "maxAttempts", ujian.getMaxAttempts().toString());
            }

            // Save general settings
            if (ujian.getPengaturan() != null && !ujian.getPengaturan().isEmpty()) {
                String pengaturanJson = objectMapper.writeValueAsString(ujian.getPengaturan());
                client.insertRecord(table, rowKey, "main", "pengaturan", pengaturanJson);
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize settings", e);
        }
    }

    private void saveBankSoalList(HBaseCustomClient client, TableName table, String rowKey, Ujian ujian) {
        try {
            // Save idBankSoalList
            if (ujian.getIdBankSoalList() != null) {
                String idBankSoalListJson = objectMapper.writeValueAsString(ujian.getIdBankSoalList());
                client.insertRecord(table, rowKey, "main", "idBankSoalList", idBankSoalListJson);
            }

            // Save bankSoalList - even if empty, save empty array
            if (ujian.getBankSoalList() != null) {
                String bankSoalListJson = objectMapper.writeValueAsString(ujian.getBankSoalList());
                client.insertRecord(table, rowKey, "main", "bankSoalList", bankSoalListJson);
            } else {
                // Save empty array if bankSoalList is null
                client.insertRecord(table, rowKey, "main", "bankSoalList", "[]");
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize bank soal list", e);
        }
    }

    public Ujian findById(String ujianId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableUjian = TableName.valueOf(tableName);
        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        return client.showDataTable(tableUjian.toString(), columnMapping, ujianId, Ujian.class);
    }

    public List<Ujian> findUjianBySekolah(String schoolID, int size) throws IOException {
        TableName tableUjian = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        List<Ujian> ujianList = client.getDataListByColumnIndeks(
                tableUjian.toString(),
                columnMapping,
                "school",
                "idSchool",
                schoolID,
                Ujian.class,
                size,
                indexedFields);

        return ujianList;
    }

    public List<Ujian> findByStatus(String status, int size) throws IOException {
        TableName tableUjian = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        List<Ujian> ujianList = client.getDataListByColumnIndeks(
                tableUjian.toString(),
                columnMapping,
                "main",
                "statusUjian",
                status,
                Ujian.class,
                size,
                indexedFields);

        return ujianList;
    }

    public List<Ujian> findByStatusAndSekolah(String status, String schoolID, int size) throws IOException {
        TableName tableUjian = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        // Get all ujian by school first, then filter by status
        List<Ujian> ujianList = client.getDataListByColumnIndeks(
                tableUjian.toString(),
                columnMapping,
                "school",
                "idSchool",
                schoolID,
                Ujian.class,
                size * 2, // Get more to account for filtering
                indexedFields);

        // Filter by status
        return ujianList.stream()
                .filter(ujian -> status.equals(ujian.getStatusUjian()))
                .limit(size)
                .collect(Collectors.toList());
    }

    public List<Ujian> findActiveUjian(int size) throws IOException {
        return findByStatus("AKTIF", size);
    }

    public List<Ujian> findActiveUjianBySekolah(String schoolID, int size) throws IOException {
        return findByStatusAndSekolah("AKTIF", schoolID, size);
    }

    /**
     * Find ujian by multiple criteria - useful for complex queries
     */
    public List<Ujian> findUjianByCriteria(String schoolID, String status, String createdBy, int size)
            throws IOException {
        // First get by school if specified
        List<Ujian> ujianList;
        if (schoolID != null && !schoolID.equals("*")) {
            ujianList = findUjianBySekolah(schoolID, size * 3); // Get more for filtering
        } else {
            ujianList = findAll(size * 3);
        }

        // Apply filters
        return ujianList.stream()
                .filter(ujian -> status == null || status.equals(ujian.getStatusUjian()))
                .filter(ujian -> createdBy == null ||
                        (ujian.getCreatedBy() != null && createdBy.equals(ujian.getCreatedBy().getId())))
                .limit(size)
                .collect(Collectors.toList());
    }

    /**
     * Find ujian that are currently live
     */
    public List<Ujian> findLiveUjian(String schoolID, int size) throws IOException {
        List<Ujian> ujianList;
        if (schoolID != null && !schoolID.equals("*")) {
            ujianList = findUjianBySekolah(schoolID, size * 2);
        } else {
            ujianList = findAll(size * 2);
        }

        return ujianList.stream()
                .filter(ujian -> ujian.getIsLive() != null && ujian.getIsLive())
                .limit(size)
                .collect(Collectors.toList());
    }

    /**
     * Count ujian by status and school
     */
    public long countByStatusAndSchool(String status, String schoolID) throws IOException {
        List<Ujian> ujianList;
        if (schoolID != null && !schoolID.equals("*")) {
            ujianList = findUjianBySekolah(schoolID, 1000); // Get large number for counting
        } else {
            ujianList = findAll(1000);
        }

        return ujianList.stream()
                .filter(ujian -> status == null || status.equals(ujian.getStatusUjian()))
                .count();
    }

    /**
     * Get standard column mapping used across all queries
     */
    private Map<String, String> getStandardColumnMapping() {
        Map<String, String> columnMapping = new HashMap<>();

        // Basic fields
        columnMapping.put("idUjian", "idUjian");
        columnMapping.put("namaUjian", "namaUjian");
        columnMapping.put("deskripsi", "deskripsi");
        columnMapping.put("durasiMenit", "durasiMenit");
        columnMapping.put("waktuMulaiDijadwalkan", "waktuMulaiDijadwalkan");
        columnMapping.put("waktuSelesaiOtomatis", "waktuSelesaiOtomatis");
        columnMapping.put("statusUjian", "statusUjian");
        columnMapping.put("isLive", "isLive");
        columnMapping.put("isAutoStart", "isAutoStart");
        columnMapping.put("isAutoEnd", "isAutoEnd");
        columnMapping.put("jumlahSoal", "jumlahSoal");
        columnMapping.put("totalBobot", "totalBobot");
        columnMapping.put("tipeSoal", "tipeSoal");
        columnMapping.put("tampilkanNilai", "tampilkanNilai");

        // Configuration fields
        columnMapping.put("idBankSoalList", "idBankSoalList");
        columnMapping.put("pengaturan", "pengaturan");
        columnMapping.put("createdAt", "createdAt");
        columnMapping.put("updatedAt", "updatedAt");

        columnMapping.put("isCatEnabled", "isCatEnabled");
        columnMapping.put("catSettings", "catSettings");
        columnMapping.put("allowLateStart", "allowLateStart");
        columnMapping.put("maxLateStartMinutes", "maxLateStartMinutes");
        columnMapping.put("showTimerToParticipants", "showTimerToParticipants");
        columnMapping.put("preventCheating", "preventCheating");

        columnMapping.put("isFlexibleTiming", "isFlexibleTiming");
        columnMapping.put("batasAkhirMulai", "batasAkhirMulai");
        columnMapping.put("autoEndAfterDuration", "autoEndAfterDuration");
        columnMapping.put("toleransiKeterlambatanMenit", "toleransiKeterlambatanMenit");

        columnMapping.put("strategiPemilihanSoal", "strategiPemilihanSoal");
        columnMapping.put("minPassingScore", "minPassingScore");
        columnMapping.put("allowReview", "allowReview");
        columnMapping.put("allowBacktrack", "allowBacktrack");
        columnMapping.put("maxAttempts", "maxAttempts");

        // Relationships
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("semester", "semester");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("school", "school");
        columnMapping.put("createdBy", "createdBy");
        columnMapping.put("bankSoalList", "bankSoalList");

        return columnMapping;
    }

    /**
     * Get indexed fields configuration
     */
    private Map<String, String> getIndexedFields() {
        Map<String, String> indexedFields = new HashMap<>();
        indexedFields.put("idBankSoalList", "LIST");
        indexedFields.put("pengaturan", "MAP");
        indexedFields.put("catSettings", "MAP");
        indexedFields.put("bankSoalList", "LIST");
        return indexedFields;
    }

    public boolean deleteById(String ujianId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, ujianId);
        return true;
    }

    public boolean existsById(String ujianId) throws IOException {
        try {
            HBaseCustomClient client = new HBaseCustomClient(conf);
            TableName tableUjian = TableName.valueOf(tableName);
            Map<String, String> columnMapping = new HashMap<>();
            columnMapping.put("idUjian", "idUjian");

            Ujian ujian = client.getDataByColumn(tableUjian.toString(), columnMapping,
                    "main", "idUjian",
                    ujianId, Ujian.class);

            return ujian != null && ujian.getIdUjian() != null;
        } catch (Exception e) {
            return false; // If any error occurs, assume record doesn't exist
        }
    }

    /**
     * Update ujian status - optimized for status changes
     */
    public boolean updateStatus(String ujianId, String newStatus) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableUjian = TableName.valueOf(tableName);

        client.insertRecord(tableUjian, ujianId, "main", "statusUjian", newStatus);
        client.insertRecord(tableUjian, ujianId, "main", "updatedAt",
                java.time.Instant.now().toString());

        return true;
    }

    /**
     * Update isLive status - optimized for live status changes
     */
    public boolean updateLiveStatus(String ujianId, boolean isLive) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableUjian = TableName.valueOf(tableName);

        client.insertRecord(tableUjian, ujianId, "main", "isLive", String.valueOf(isLive));
        client.insertRecord(tableUjian, ujianId, "main", "updatedAt",
                java.time.Instant.now().toString());

        return true;
    }
}
