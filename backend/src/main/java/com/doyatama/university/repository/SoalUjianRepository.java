package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.SoalUjian;
import com.doyatama.university.model.BankSoal;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SoalUjianRepository {

    Configuration conf = HBaseConfiguration.create();
    String tableName = "soalUjian";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<SoalUjian> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableSoalUjian = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap (kolom standar)
        columnMapping.put("idSoalUjian", "idSoalUjian");
        columnMapping.put("namaUjian", "namaUjian");
        columnMapping.put("pertanyaan", "pertanyaan");
        columnMapping.put("bobot", "bobot");
        columnMapping.put("jenisSoal", "jenisSoal");
        columnMapping.put("toleransiTypo", "toleransiTypo");
        columnMapping.put("createdAt", "createdAt");
        columnMapping.put("user", "user");
        columnMapping.put("taksonomi", "taksonomi");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("school", "school");

        // Definisikan field yang menggunakan indeks
        Map<String, String> indexedFields = new HashMap<>();
        indexedFields.put("opsi", "MAP"); // opsi disimpan sebagai MAP
        indexedFields.put("pasangan", "MAP"); // pasangan disimpan sebagai MAP
        indexedFields.put("jawabanBenar", "LIST"); // jawabanBenar disimpan sebagai LIST

        return client.showListTableIndex(tableSoalUjian.toString(), columnMapping, SoalUjian.class, indexedFields,
                size);
    }

    /**
     * Save SoalUjian WITHOUT cascade to BankSoal
     * This method only creates the SoalUjian record, no BankSoal entries are
     * created
     */
    public SoalUjian save(SoalUjian soalUjian) throws IOException {
        System.out.println("Saving SoalUjian WITHOUT cascade - ID: " + soalUjian.getIdSoalUjian());

        HBaseCustomClient client = new HBaseCustomClient(conf);
        String rowKey = soalUjian.getIdSoalUjian();
        TableName tableSoalUjian = TableName.valueOf(tableName);

        // Save main info
        saveMainInfo(client, tableSoalUjian, rowKey, soalUjian);

        // Save relationships
        saveRelationships(client, tableSoalUjian, rowKey, soalUjian);

        // Save question details based on type
        switch (soalUjian.getJenisSoal().toUpperCase()) {
            case "PG":
            case "MULTI":
                savePilihanGanda(client, tableSoalUjian, rowKey, soalUjian);
                break;

            case "COCOK":
                saveCocokkan(client, tableSoalUjian, rowKey, soalUjian);
                break;

            case "ISIAN":
                saveIsian(client, tableSoalUjian, rowKey, soalUjian);
                break;
        }

        client.insertRecord(tableSoalUjian, rowKey, "detail", "created_by", "Polinema");

        System.out.println("Successfully saved SoalUjian without cascade - ID: " + soalUjian.getIdSoalUjian());
        return soalUjian;
    }

    private void saveMainInfo(HBaseCustomClient client, TableName table, String rowKey, SoalUjian soal) {
        client.insertRecord(table, rowKey, "main", "idSoalUjian", soal.getIdSoalUjian());
        client.insertRecord(table, rowKey, "main", "namaUjian", soal.getNamaUjian());
        client.insertRecord(table, rowKey, "main", "pertanyaan", soal.getPertanyaan());
        client.insertRecord(table, rowKey, "main", "bobot", soal.getBobot());
        client.insertRecord(table, rowKey, "main", "jenisSoal", soal.getJenisSoal());
        client.insertRecord(table, rowKey, "main", "createdAt", soal.getCreatedAt().toString());
    }

    private void saveRelationships(HBaseCustomClient client, TableName table, String rowKey, SoalUjian soal) {
        if (soal.getUser() != null) {
            client.insertRecord(table, rowKey, "user", "id", soal.getUser().getId());
            client.insertRecord(table, rowKey, "user", "name", soal.getUser().getName());
        }

        if (soal.getTaksonomi() != null) {
            client.insertRecord(table, rowKey, "taksonomi", "idTaksonomi", soal.getTaksonomi().getIdTaksonomi());
            client.insertRecord(table, rowKey, "taksonomi", "namaTaksonomi", soal.getTaksonomi().getNamaTaksonomi());
        }

        if (soal.getKonsentrasiKeahlianSekolah() != null) {
            client.insertRecord(table, rowKey, "konsentrasiKeahlianSekolah", "idKonsentrasiSekolah",
                    soal.getKonsentrasiKeahlianSekolah().getIdKonsentrasiSekolah());
            client.insertRecord(table, rowKey, "konsentrasiKeahlianSekolah", "namaKonsentrasiSekolah",
                    soal.getKonsentrasiKeahlianSekolah().getNamaKonsentrasiSekolah());

        }

        if (soal.getSchool() != null) {
            client.insertRecord(table, rowKey, "school", "idSchool", soal.getSchool().getIdSchool());
            client.insertRecord(table, rowKey, "school", "nameSchool", soal.getSchool().getNameSchool());
        }
    }

    private void savePilihanGanda(HBaseCustomClient client, TableName table, String rowKey, SoalUjian soal) {
        try {
            System.out.println("Saving PG question - rowKey: " + rowKey);
            System.out.println("Opsi to save: " + soal.getOpsi());
            System.out.println("JawabanBenar to save: " + soal.getJawabanBenar());

            // Validate data before saving
            if (soal.getOpsi() == null || soal.getOpsi().isEmpty()) {
                throw new RuntimeException("Opsi cannot be null or empty for PG question");
            }
            if (soal.getJawabanBenar() == null || soal.getJawabanBenar().isEmpty()) {
                throw new RuntimeException("JawabanBenar cannot be null or empty for PG question");
            }

            // Simpan opsi sebagai JSON
            String opsiJson = objectMapper.writeValueAsString(soal.getOpsi());
            client.insertRecord(table, rowKey, "main", "opsi", opsiJson);
            System.out.println("Saved opsi JSON: " + opsiJson);

            // Simpan jawaban benar sebagai JSON
            String jawabanJson = objectMapper.writeValueAsString(soal.getJawabanBenar());
            client.insertRecord(table, rowKey, "main", "jawabanBenar", jawabanJson);
            System.out.println("Saved jawabanBenar JSON: " + jawabanJson);

            System.out.println("Successfully saved PG question data");
        } catch (JsonProcessingException e) {
            System.err.println("Failed to serialize options or answers: " + e.getMessage());
            throw new RuntimeException("Failed to serialize options or answers", e);
        } catch (Exception e) {
            System.err.println("Error saving PG question: " + e.getMessage());
            throw new RuntimeException("Error saving PG question", e);
        }
    }

    private void saveCocokkan(HBaseCustomClient client, TableName table, String rowKey, SoalUjian soal) {
        try {
            // Simpan pasangan sebagai JSON
            String pasanganJson = objectMapper.writeValueAsString(soal.getPasangan());
            client.insertRecord(table, rowKey, "main", "pasangan", pasanganJson);

            // Simpan jawaban benar sebagai JSON
            String jawabanJson = objectMapper.writeValueAsString(soal.getJawabanBenar());
            client.insertRecord(table, rowKey, "main", "jawabanBenar", jawabanJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize matching pairs or answers", e);
        }
    }

    private void saveIsian(HBaseCustomClient client, TableName table, String rowKey, SoalUjian soal) {
        try {
            // Simpan jawaban benar sebagai JSON
            String jawabanJson = objectMapper.writeValueAsString(soal.getJawabanBenar());
            client.insertRecord(table, rowKey, "main", "jawabanBenar", jawabanJson);

            // Simpan toleransi typo
            client.insertRecord(table, rowKey, "main", "toleransiTypo",
                    String.valueOf(soal.getToleransiTypo()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize answers", e);
        }
    }

    public SoalUjian findById(String soalUjianId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableSoalUjian = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idSoalUjian", "idSoalUjian");
        columnMapping.put("namaUjian", "namaUjian");
        columnMapping.put("pertanyaan", "pertanyaan");
        columnMapping.put("bobot", "bobot");
        columnMapping.put("jenisSoal", "jenisSoal");
        columnMapping.put("opsi", "opsi");
        columnMapping.put("pasangan", "pasangan");
        columnMapping.put("toleransiTypo", "toleransiTypo");
        columnMapping.put("jawabanBenar", "jawabanBenar");
        columnMapping.put("createdAt", "createdAt");
        columnMapping.put("user", "user");
        columnMapping.put("taksonomi", "taksonomi");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("school", "school");

        return client.showDataTable(tableSoalUjian.toString(), columnMapping, soalUjianId, SoalUjian.class);
    }

    public List<SoalUjian> findSoalUjianBySekolah(String schoolID, int size) throws IOException {
        TableName tableSoalUjian = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        // Standard column mappings
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idSoalUjian", "idSoalUjian");
        columnMapping.put("namaUjian", "namaUjian");
        columnMapping.put("pertanyaan", "pertanyaan");
        columnMapping.put("bobot", "bobot");
        columnMapping.put("jenisSoal", "jenisSoal");
        columnMapping.put("opsi", "opsi");
        columnMapping.put("pasangan", "pasangan");
        columnMapping.put("jawabanBenar", "jawabanBenar");
        columnMapping.put("toleransiTypo", "toleransiTypo");
        columnMapping.put("createdAt", "createdAt");
        columnMapping.put("user", "user");
        columnMapping.put("taksonomi", "taksonomi");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("school", "school");

        // Define indexed fields with their types
        Map<String, String> indexedFields = new HashMap<>();
        indexedFields.put("opsi", "MAP");
        indexedFields.put("pasangan", "MAP");
        indexedFields.put("jawabanBenar", "LIST");

        List<SoalUjian> soalUjianList = client.getDataListByColumnIndeks(
                tableSoalUjian.toString(),
                columnMapping,
                "school",
                "idSchool",
                schoolID,
                SoalUjian.class,
                size,
                indexedFields);

        return soalUjianList;
    }

    public SoalUjian update(String soalUjianId, SoalUjian soalUjian) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableSoalUjian = TableName.valueOf(tableName);
        String tableNameStr = tableSoalUjian.toString();

        // First delete all existing detail records to avoid orphaned data
        deleteExistingDetails(client, tableNameStr, soalUjianId);

        // Update main info
        updateMainInfo(client, tableSoalUjian, soalUjianId, soalUjian);

        // Update relationships
        updateRelationships(client, tableSoalUjian, soalUjianId, soalUjian);

        // Save question details based on type
        switch (soalUjian.getJenisSoal().toUpperCase()) {
            case "PG":
            case "MULTI":
                savePilihanGanda(client, tableSoalUjian, soalUjianId, soalUjian);
                break;

            case "COCOK":
                saveCocokkan(client, tableSoalUjian, soalUjianId, soalUjian);
                break;

            case "ISIAN":
                saveIsian(client, tableSoalUjian, soalUjianId, soalUjian);
                break;
        }

        return soalUjian;
    }

    private void deleteExistingDetails(HBaseCustomClient client, String tableName, String rowKey) throws IOException {
        // Get all existing detail columns
        List<String> detailColumns = client.getColumns(tableName, rowKey, "detail");

        // Delete each detail column
        for (String column : detailColumns) {
            client.deleteRecordByColumn(tableName, rowKey, "detail", column);
        }
    }

    private void updateMainInfo(HBaseCustomClient client, TableName table, String rowKey, SoalUjian soal)
            throws IOException {
        if (soal.getNamaUjian() != null) {
            client.insertRecord(table, rowKey, "main", "namaUjian", soal.getNamaUjian());
        }
        if (soal.getPertanyaan() != null) {
            client.insertRecord(table, rowKey, "main", "pertanyaan", soal.getPertanyaan());
        }
        if (soal.getBobot() != null) {
            client.insertRecord(table, rowKey, "main", "bobot", soal.getBobot());
        }
        if (soal.getJenisSoal() != null) {
            client.insertRecord(table, rowKey, "main", "jenisSoal", soal.getJenisSoal());
        }
    }

    private void updateRelationships(HBaseCustomClient client, TableName table, String rowKey, SoalUjian soal)
            throws IOException {
        if (soal.getUser() != null) {
            client.insertRecord(table, rowKey, "user", "id", soal.getUser().getId());
            client.insertRecord(table, rowKey, "user", "name", soal.getUser().getName());
        }

        if (soal.getTaksonomi() != null) {
            client.insertRecord(table, rowKey, "taksonomi", "idTaksonomi", soal.getTaksonomi().getIdTaksonomi());
            client.insertRecord(table, rowKey, "taksonomi", "namaTaksonomi", soal.getTaksonomi().getNamaTaksonomi());
        }

        if (soal.getKonsentrasiKeahlianSekolah() != null) {
            client.insertRecord(table, rowKey, "konsentrasiKeahlianSekolah", "idKonsentrasiSekolah",
                    soal.getKonsentrasiKeahlianSekolah().getIdKonsentrasiSekolah());
            client.insertRecord(table, rowKey, "konsentrasiKeahlianSekolah", "namaKonsentrasiSekolah",
                    soal.getKonsentrasiKeahlianSekolah().getNamaKonsentrasiSekolah());
        }

        if (soal.getSchool() != null) {
            client.insertRecord(table, rowKey, "school", "idSchool", soal.getSchool().getIdSchool());
            client.insertRecord(table, rowKey, "school", "nameSchool", soal.getSchool().getNameSchool());
        }
    }

    public boolean deleteById(String soalUjianId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, soalUjianId);
        return true;
    }

    public boolean existsById(String soalUjianId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableSoalUjian = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idSoalUjian", "idSoalUjian");

        SoalUjian soalUjian = client.getDataByColumn(tableSoalUjian.toString(), columnMapping,
                "main", "idSoalUjian",
                soalUjianId, SoalUjian.class);

        return soalUjian.getIdSoalUjian() != null;
    }

    /**
     * Validate SoalUjian data before cascade operations
     */
    private void validateSoalUjianForCascade(SoalUjian soalUjian) {
        if (soalUjian == null) {
            throw new IllegalArgumentException("SoalUjian cannot be null");
        }

        if (soalUjian.getIdSoalUjian() == null || soalUjian.getIdSoalUjian().trim().isEmpty()) {
            throw new IllegalArgumentException("SoalUjian ID cannot be null or empty");
        }

        if (soalUjian.getNamaUjian() == null || soalUjian.getNamaUjian().trim().isEmpty()) {
            throw new IllegalArgumentException("Nama ujian cannot be null or empty");
        }

        if (soalUjian.getPertanyaan() == null || soalUjian.getPertanyaan().trim().isEmpty()) {
            throw new IllegalArgumentException("Pertanyaan cannot be null or empty");
        }

        if (soalUjian.getJenisSoal() == null || soalUjian.getJenisSoal().trim().isEmpty()) {
            throw new IllegalArgumentException("Jenis soal cannot be null or empty");
        }

        // Additional validation based on question type
        switch (soalUjian.getJenisSoal().toUpperCase()) {
            case "PG":
            case "MULTI":
                if (soalUjian.getOpsi() == null || soalUjian.getOpsi().isEmpty()) {
                    throw new IllegalArgumentException("Opsi cannot be null or empty for " + soalUjian.getJenisSoal());
                }
                break;
            case "COCOK":
                if (soalUjian.getPasangan() == null || soalUjian.getPasangan().isEmpty()) {
                    throw new IllegalArgumentException("Pasangan cannot be null or empty for COCOK");
                }
                break;
            case "ISIAN":
                // For ISIAN, toleransiTypo is optional, no additional validation needed
                break;
        }

        if (soalUjian.getJawabanBenar() == null || soalUjian.getJawabanBenar().isEmpty()) {
            throw new IllegalArgumentException("Jawaban benar cannot be null or empty");
        }
    }

    /**
     * Save SoalUjian with cascade to BankSoal
     * This method creates a SoalUjian and automatically creates a corresponding
     * BankSoal entry
     */
    public SoalUjian saveWithCascade(SoalUjian soalUjian) throws IOException {
        // Validate input for cascade operations
        validateSoalUjianForCascade(soalUjian);

        // First save the SoalUjian
        SoalUjian savedSoalUjian = save(soalUjian);

        try {
            // Then cascade create to BankSoal
            cascadeCreateToBankSoal(savedSoalUjian);
            System.out.println(
                    "Successfully created SoalUjian with cascade to BankSoal: " + savedSoalUjian.getIdSoalUjian());
            return savedSoalUjian;
        } catch (Exception e) {
            // If BankSoal creation fails, we should log but not fail the SoalUjian creation
            System.err.println("Warning: Failed to cascade create to BankSoal for SoalUjian: " +
                    savedSoalUjian.getIdSoalUjian() + ". Error: " + e.getMessage());
            e.printStackTrace();
            return savedSoalUjian;
        }
    }

    /**
     * Update SoalUjian with cascade to BankSoal
     * This method updates a SoalUjian and automatically updates corresponding
     * BankSoal entries
     */
    public SoalUjian updateWithCascade(String soalUjianId, SoalUjian soalUjian) throws IOException {
        // Validate input
        if (soalUjianId == null || soalUjianId.trim().isEmpty()) {
            throw new IllegalArgumentException("SoalUjianId cannot be null or empty");
        }
        // Validate SoalUjian for cascade operations
        validateSoalUjianForCascade(soalUjian);

        // First update the SoalUjian
        SoalUjian updatedSoalUjian = update(soalUjianId, soalUjian);

        try {
            // Then cascade update to BankSoal
            cascadeUpdateToBankSoal(updatedSoalUjian);
            System.out.println(
                    "Successfully updated SoalUjian with cascade to BankSoal: " + updatedSoalUjian.getIdSoalUjian());
            return updatedSoalUjian;
        } catch (Exception e) {
            // If BankSoal update fails, we should log but not fail the SoalUjian update
            System.err.println("Warning: Failed to cascade update to BankSoal for SoalUjian: " +
                    updatedSoalUjian.getIdSoalUjian() + ". Error: " + e.getMessage());
            e.printStackTrace();
            return updatedSoalUjian;
        }
    }

    /**
     * Delete SoalUjian with cascade to BankSoal
     * This method deletes a SoalUjian and automatically deletes corresponding
     * BankSoal entries
     */
    public boolean deleteWithCascade(String soalUjianId) throws IOException {
        // Validate input
        if (soalUjianId == null || soalUjianId.trim().isEmpty()) {
            throw new IllegalArgumentException("SoalUjianId cannot be null or empty");
        }

        try {
            // First cascade delete from BankSoal
            cascadeDeleteFromBankSoal(soalUjianId);
            System.out.println("Successfully deleted cascade BankSoal entries for SoalUjian: " + soalUjianId);
        } catch (Exception e) {
            // If BankSoal deletion fails, log warning but continue with SoalUjian deletion
            System.err.println("Warning: Failed to cascade delete from BankSoal for SoalUjian: " +
                    soalUjianId + ". Error: " + e.getMessage());
            e.printStackTrace();
        }

        // Then delete the SoalUjian
        boolean result = deleteById(soalUjianId);
        if (result) {
            System.out.println("Successfully deleted SoalUjian: " + soalUjianId);
        }
        return result;
    }

    /**
     * Create BankSoal entry from SoalUjian
     */
    private void cascadeCreateToBankSoal(SoalUjian soalUjian) throws IOException {
        // Validate SoalUjian before cascade creation
        validateSoalUjianForCascade(soalUjian);

        try {
            BankSoalRepository bankSoalRepository = new BankSoalRepository();

            // Create BankSoal entity from SoalUjian
            BankSoal bankSoal = new BankSoal();
            bankSoal.setIdBankSoal(UUID.randomUUID().toString());
            bankSoal.setIdSoalUjian(soalUjian.getIdSoalUjian());
            bankSoal.setNamaUjian(soalUjian.getNamaUjian());
            bankSoal.setPertanyaan(soalUjian.getPertanyaan());
            bankSoal.setBobot(soalUjian.getBobot());
            bankSoal.setJenisSoal(soalUjian.getJenisSoal());
            bankSoal.setOpsi(soalUjian.getOpsi());
            bankSoal.setPasangan(soalUjian.getPasangan());
            bankSoal.setToleransiTypo(soalUjian.getToleransiTypo());
            bankSoal.setJawabanBenar(soalUjian.getJawabanBenar());
            bankSoal.setCreatedAt(soalUjian.getCreatedAt());
            bankSoal.setSoalUjian(soalUjian);
            bankSoal.setTaksonomi(soalUjian.getTaksonomi());
            bankSoal.setKonsentrasiKeahlianSekolah(soalUjian.getKonsentrasiKeahlianSekolah());
            bankSoal.setSchool(soalUjian.getSchool());

            // Save to BankSoal
            BankSoal savedBankSoal = bankSoalRepository.save(bankSoal);
            System.out.println("Successfully created BankSoal entry: " + savedBankSoal.getIdBankSoal() +
                    " for SoalUjian: " + soalUjian.getIdSoalUjian());
        } catch (Exception e) {
            System.err.println("Error in cascadeCreateToBankSoal: " + e.getMessage());
            throw new IOException("Failed to cascade create BankSoal for SoalUjian: " + soalUjian.getIdSoalUjian(), e);
        }
    }

    /**
     * Update BankSoal entries related to SoalUjian
     */
    private void cascadeUpdateToBankSoal(SoalUjian soalUjian) throws IOException {
        // Validate SoalUjian before cascade update
        validateSoalUjianForCascade(soalUjian);

        try {
            BankSoalRepository bankSoalRepository = new BankSoalRepository();

            // Use the specialized method to update all BankSoal entries that reference this
            // SoalUjian
            bankSoalRepository.updateSoalUjianInfoBySoalUjianId(
                    soalUjian.getIdSoalUjian(),
                    soalUjian.getNamaUjian(),
                    soalUjian.getPertanyaan(),
                    soalUjian.getBobot(),
                    soalUjian.getJenisSoal(),
                    soalUjian.getToleransiTypo(),
                    soalUjian.getOpsi(),
                    soalUjian.getPasangan(),
                    soalUjian.getJawabanBenar());
            System.out.println(
                    "Successfully cascaded update to BankSoal entries for SoalUjian: " + soalUjian.getIdSoalUjian());
        } catch (Exception e) {
            System.err.println("Error in cascadeUpdateToBankSoal: " + e.getMessage());
            throw new IOException("Failed to cascade update BankSoal for SoalUjian: " + soalUjian.getIdSoalUjian(), e);
        }
    }

    /**
     * Delete BankSoal entries related to SoalUjian
     */
    private void cascadeDeleteFromBankSoal(String soalUjianId) throws IOException {
        if (soalUjianId == null || soalUjianId.trim().isEmpty()) {
            throw new IllegalArgumentException("SoalUjianId cannot be null or empty for cascade delete");
        }

        try {
            BankSoalRepository bankSoalRepository = new BankSoalRepository();

            // Find all BankSoal entries with this SoalUjian ID
            List<BankSoal> bankSoalList = bankSoalRepository.findBankSoalBySoalUjian(soalUjianId, 1000);

            System.out.println(
                    "Found " + bankSoalList.size() + " BankSoal entries to delete for SoalUjian: " + soalUjianId);

            // Delete each BankSoal entry
            int deletedCount = 0;
            for (BankSoal bankSoal : bankSoalList) {
                if (bankSoal.getIdBankSoal() != null) {
                    boolean deleted = bankSoalRepository.deleteById(bankSoal.getIdBankSoal());
                    if (deleted) {
                        deletedCount++;
                    }
                }
            }

            System.out.println(
                    "Successfully deleted " + deletedCount + " BankSoal entries for SoalUjian: " + soalUjianId);
        } catch (Exception e) {
            System.err.println("Error in cascadeDeleteFromBankSoal: " + e.getMessage());
            throw new IOException("Failed to cascade delete BankSoal entries for SoalUjian: " + soalUjianId, e);
        }
    }
}
