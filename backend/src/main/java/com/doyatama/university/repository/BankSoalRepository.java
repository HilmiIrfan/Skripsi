package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.BankSoal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository

public class BankSoalRepository {

    Configuration conf = HBaseConfiguration.create();
    String tableName = "bankSoal";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<BankSoal> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableBankSoal = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap (kolom standar)
        columnMapping.put("idBankSoal", "idBankSoal");
        columnMapping.put("idSoalUjian", "idSoalUjian");
        columnMapping.put("namaUjian", "namaUjian");
        columnMapping.put("pertanyaan", "pertanyaan");
        columnMapping.put("bobot", "bobot");
        columnMapping.put("jenisSoal", "jenisSoal");
        columnMapping.put("toleransiTypo", "toleransiTypo");
        columnMapping.put("createdAt", "createdAt");
        columnMapping.put("soalUjian", "soalUjian");
        columnMapping.put("taksonomi", "taksonomi");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("acp", "acp");
        columnMapping.put("atp", "atp");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("school", "school");

        // Definisikan field yang menggunakan indeks
        Map<String, String> indexedFields = new HashMap<>();
        indexedFields.put("opsi", "MAP"); // opsi disimpan sebagai MAP
        indexedFields.put("pasangan", "MAP"); // pasangan disimpan sebagai MAP
        indexedFields.put("jawabanBenar", "LIST"); // jawabanBenar disimpan sebagai LIST

        return client.showListTableIndex(tableBankSoal.toString(), columnMapping, BankSoal.class, indexedFields,
                size);
    }

    public BankSoal save(BankSoal bankSoal) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        String rowKey = bankSoal.getIdBankSoal();
        TableName tableBankSoal = TableName.valueOf(tableName);

        // Save main info
        saveMainInfo(client, tableBankSoal, rowKey, bankSoal);

        // Save relationships
        saveRelationships(client, tableBankSoal, rowKey, bankSoal);

        // Save question details based on type
        switch (bankSoal.getJenisSoal().toUpperCase()) {
            case "PG":
            case "MULTI":
                savePilihanGanda(client, tableBankSoal, rowKey, bankSoal);
                break;

            case "COCOK":
                saveCocokkan(client, tableBankSoal, rowKey, bankSoal);
                break;

            case "ISIAN":
                saveIsian(client, tableBankSoal, rowKey, bankSoal);
                break;
        }

        client.insertRecord(tableBankSoal, rowKey, "detail", "created_by", "Polinema");
        return bankSoal;
    }

    private void saveMainInfo(HBaseCustomClient client, TableName table, String rowKey, BankSoal bankSoal) {
        client.insertRecord(table, rowKey, "main", "idBankSoal", bankSoal.getIdBankSoal());
        client.insertRecord(table, rowKey, "main", "idSoalUjian", bankSoal.getIdSoalUjian());
        client.insertRecord(table, rowKey, "main", "namaUjian", bankSoal.getNamaUjian());
        client.insertRecord(table, rowKey, "main", "pertanyaan", bankSoal.getPertanyaan());
        client.insertRecord(table, rowKey, "main", "bobot", bankSoal.getBobot());
        client.insertRecord(table, rowKey, "main", "jenisSoal", bankSoal.getJenisSoal());
        client.insertRecord(table, rowKey, "main", "createdAt", bankSoal.getCreatedAt().toString());
    }

    private void saveRelationships(HBaseCustomClient client, TableName table, String rowKey, BankSoal bankSoal) {
        if (bankSoal.getIdSoalUjian() != null) {
            client.insertRecord(table, rowKey, "soalUjian", "idSoalUjian", bankSoal.getSoalUjian().getIdSoalUjian());
            client.insertRecord(table, rowKey, "soalUjian", "namaUjian", bankSoal.getSoalUjian().getNamaUjian());
            client.insertRecord(table, rowKey, "soalUjian", "pertanyaan", bankSoal.getSoalUjian().getPertanyaan());
            client.insertRecord(table, rowKey, "soalUjian", "bobot", bankSoal.getSoalUjian().getBobot());
            client.insertRecord(table, rowKey, "soalUjian", "jenisSoal", bankSoal.getSoalUjian().getJenisSoal());
            client.insertRecord(table, rowKey, "soalUjian", "createdAt",
                    bankSoal.getSoalUjian().getCreatedAt().toString());
        }

        if (bankSoal.getTahunAjaran().getIdTahun() != null) {
            client.insertRecord(table, rowKey, "tahunAjaran", "idTahun", bankSoal.getTahunAjaran().getIdTahun());
            client.insertRecord(table, rowKey, "tahunAjaran", "tahunAjaran",
                    bankSoal.getTahunAjaran().getTahunAjaran());
        }

        if (bankSoal.getSemester().getIdSemester() != null) {
            client.insertRecord(table, rowKey, "semester", "idSemester", bankSoal.getSemester().getIdSemester());
            client.insertRecord(table, rowKey, "semester", "namaSemester", bankSoal.getSemester().getNamaSemester());
        }

        if (bankSoal.getKelas().getIdKelas() != null) {
            client.insertRecord(table, rowKey, "kelas", "idKelas", bankSoal.getKelas().getIdKelas());
            client.insertRecord(table, rowKey, "kelas", "namaKelas", bankSoal.getKelas().getNamaKelas());
        }

        if (bankSoal.getMapel().getIdMapel() != null) {
            client.insertRecord(table, rowKey, "mapel", "idMapel", bankSoal.getMapel().getIdMapel());
            client.insertRecord(table, rowKey, "mapel", "name", bankSoal.getMapel().getName());
        }

        if (bankSoal.getElemen().getIdElemen() != null) {
            client.insertRecord(table, rowKey, "elemen", "idElemen", bankSoal.getElemen().getIdElemen());
            client.insertRecord(table, rowKey, "elemen", "namaElemen", bankSoal.getElemen().getNamaElemen());
        }

        if (bankSoal.getAcp().getIdAcp() != null) {
            client.insertRecord(table, rowKey, "acp", "idAcp", bankSoal.getAcp().getIdAcp());
            client.insertRecord(table, rowKey, "acp", "namaAcp", bankSoal.getAcp().getNamaAcp());
        }

        if (bankSoal.getAtp().getIdAtp() != null) {
            client.insertRecord(table, rowKey, "atp", "idAtp", bankSoal.getAtp().getIdAtp());
            client.insertRecord(table, rowKey, "atp", "namaAtp", bankSoal.getAtp().getNamaAtp());
        }

        if (bankSoal.getTaksonomi().getIdTaksonomi() != null) {
            client.insertRecord(table, rowKey, "taksonomi", "idTaksonomi", bankSoal.getTaksonomi().getIdTaksonomi());
            client.insertRecord(table, rowKey, "taksonomi", "namaTaksonomi",
                    bankSoal.getTaksonomi().getNamaTaksonomi());
        }

        if (bankSoal.getKonsentrasiKeahlianSekolah().getIdKonsentrasiSekolah() != null) {
            client.insertRecord(table, rowKey, "konsentrasiKeahlianSekolah", "idKonsentrasiSekolah",
                    bankSoal.getKonsentrasiKeahlianSekolah().getIdKonsentrasiSekolah());
            client.insertRecord(table, rowKey, "konsentrasiKeahlianSekolah", "namaKonsentrasiSekolah",
                    bankSoal.getKonsentrasiKeahlianSekolah().getNamaKonsentrasiSekolah());

        }

        if (bankSoal.getSchool().getIdSchool() != null) {
            client.insertRecord(table, rowKey, "school", "idSchool", bankSoal.getSchool().getIdSchool());
            client.insertRecord(table, rowKey, "school", "nameSchool", bankSoal.getSchool().getNameSchool());
        }
    }

    private void savePilihanGanda(HBaseCustomClient client, TableName table, String rowKey, BankSoal bankSoal) {
        try {
            // Simpan opsi sebagai JSON
            String opsiJson = objectMapper.writeValueAsString(bankSoal.getOpsi());
            client.insertRecord(table, rowKey, "main", "opsi", opsiJson);

            // Simpan jawaban benar sebagai JSON
            String jawabanJson = objectMapper.writeValueAsString(bankSoal.getJawabanBenar());
            client.insertRecord(table, rowKey, "main", "jawabanBenar", jawabanJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize options or answers", e);
        }
    }

    private void saveCocokkan(HBaseCustomClient client, TableName table, String rowKey, BankSoal bankSoal) {
        try {
            // Simpan pasangan sebagai JSON
            String pasanganJson = objectMapper.writeValueAsString(bankSoal.getPasangan());
            client.insertRecord(table, rowKey, "main", "pasangan", pasanganJson);

            // Simpan jawaban benar sebagai JSON
            String jawabanJson = objectMapper.writeValueAsString(bankSoal.getJawabanBenar());
            client.insertRecord(table, rowKey, "main", "jawabanBenar", jawabanJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize matching pairs or answers", e);
        }
    }

    private void saveIsian(HBaseCustomClient client, TableName table, String rowKey, BankSoal bankSoal) {
        try {
            // Simpan jawaban benar sebagai JSON
            String jawabanJson = objectMapper.writeValueAsString(bankSoal.getJawabanBenar());
            client.insertRecord(table, rowKey, "main", "jawabanBenar", jawabanJson);

            // Simpan toleransi typo
            client.insertRecord(table, rowKey, "main", "toleransiTypo",
                    String.valueOf(bankSoal.getToleransiTypo()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize answers", e);
        }
    }

    public BankSoal findById(String bankSoalId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableBankSoal = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("idBankSoal", "idBankSoal");
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
        columnMapping.put("soalUjian", "soalUjian");
        columnMapping.put("taksonomi", "taksonomi");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("acp", "acp");
        columnMapping.put("atp", "atp");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("school", "school");

        Map<String, String> indexedFields = new HashMap<>();
        indexedFields.put("opsi", "MAP");
        indexedFields.put("pasangan", "MAP");
        indexedFields.put("jawabanBenar", "LIST");

        return client.showDataTable(tableBankSoal.toString(), columnMapping, bankSoalId, BankSoal.class);
    }

    public List<BankSoal> findBankSoalBySekolah(String schoolID, int size) throws IOException {
        TableName tableBankSoal = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        // Standard column mappings
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idBankSoal", "idBankSoal");
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
        columnMapping.put("soalUjian", "soalUjian");
        columnMapping.put("taksonomi", "taksonomi");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("acp", "acp");
        columnMapping.put("atp", "atp");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("school", "school");

        Map<String, String> indexedFields = new HashMap<>();
        indexedFields.put("opsi", "MAP");
        indexedFields.put("pasangan", "MAP");
        indexedFields.put("jawabanBenar", "LIST");

        List<BankSoal> bankSoalList = client.getDataListByColumnIndeks(
                tableBankSoal.toString(),
                columnMapping,
                "school",
                "idSchool",
                schoolID,
                BankSoal.class,
                size,
                indexedFields);

        return bankSoalList;
    }

    public List<BankSoal> findAllById(List<String> bankSoalIds) throws IOException {
        if (bankSoalIds == null || bankSoalIds.isEmpty()) {
            return new ArrayList<>();
        }

        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableBankSoal = TableName.valueOf(tableName);

        // Standard column mappings
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idBankSoal", "idBankSoal");
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
        columnMapping.put("soalUjian", "soalUjian");
        columnMapping.put("taksonomi", "taksonomi");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("acp", "acp");
        columnMapping.put("atp", "atp");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("school", "school");

        // Definisikan field yang menggunakan indeks
        Map<String, String> indexedFields = new HashMap<>();
        indexedFields.put("opsi", "MAP");
        indexedFields.put("pasangan", "MAP");
        indexedFields.put("jawabanBenar", "LIST");

        List<BankSoal> result = new ArrayList<>();

        // Fetch each BankSoal by ID
        for (String bankSoalId : bankSoalIds) {
            try {
                BankSoal bankSoal = client.showDataTable(
                        tableBankSoal.toString(),
                        columnMapping,
                        bankSoalId,
                        BankSoal.class);

                if (bankSoal != null) {
                    result.add(bankSoal);
                }
            } catch (Exception e) {
                // Log the error for debugging but continue processing other IDs
                System.err.println("Error fetching BankSoal with ID: " + bankSoalId + " - " + e.getMessage());
            }
        }

        return result;
    }

    public boolean deleteById(String bankSoalId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, bankSoalId);
        return true;
    }

    public boolean existsById(String bankSoalId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableBankSoal = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idBankSoal", "idBankSoal");

        BankSoal bankSoal = client.getDataByColumn(tableBankSoal.toString(), columnMapping,
                "main", "idBankSoal",
                bankSoalId, BankSoal.class);

        return bankSoal.getIdBankSoal() != null;
    }

    public BankSoal update(String bankSoalId, BankSoal bankSoal) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableBankSoal = TableName.valueOf(tableName);
        String tableNameStr = tableBankSoal.toString();

        // First delete existing detail records (opsi, pasangan, jawabanBenar) to avoid
        // orphaned data
        deleteExistingDetailsBankSoal(client, tableNameStr, bankSoalId);

        // Update main info
        updateMainInfoBankSoal(client, tableBankSoal, bankSoalId, bankSoal);

        // Update relationships
        updateRelationshipsBankSoal(client, tableBankSoal, bankSoalId, bankSoal);

        // Save question details based on type
        switch (bankSoal.getJenisSoal().toUpperCase()) {
            case "PG":
            case "MULTI":
                savePilihanGanda(client, tableBankSoal, bankSoalId, bankSoal);
                break;

            case "COCOK":
                saveCocokkan(client, tableBankSoal, bankSoalId, bankSoal);
                break;

            case "ISIAN":
                saveIsian(client, tableBankSoal, bankSoalId, bankSoal);
                break;
        }

        client.insertRecord(tableBankSoal, bankSoalId, "detail", "updated_by", "System_Update");
        return bankSoal;
    }

    private void deleteExistingDetailsBankSoal(HBaseCustomClient client, String tableName, String rowKey)
            throws IOException {
        // Delete existing question-specific details to avoid conflicts
        try {
            // Delete opsi (for PG/MULTI) - stored in main column family
            client.deleteRecordByColumn(tableName, rowKey, "main", "opsi");

            // Delete pasangan (for COCOK) - stored in main column family
            client.deleteRecordByColumn(tableName, rowKey, "main", "pasangan");

            // Delete jawabanBenar (for all types) - stored in main column family
            client.deleteRecordByColumn(tableName, rowKey, "main", "jawabanBenar");

            // Delete toleransiTypo (for ISIAN) - stored in main column family
            client.deleteRecordByColumn(tableName, rowKey, "main", "toleransiTypo");
        } catch (Exception e) {
            // Ignore errors if columns don't exist
            System.out.println("Some question-specific columns may not exist during update: " + e.getMessage());
        }
    }

    private void updateMainInfoBankSoal(HBaseCustomClient client, TableName table, String rowKey, BankSoal bankSoal) {
        client.insertRecord(table, rowKey, "main", "idBankSoal", bankSoal.getIdBankSoal());
        client.insertRecord(table, rowKey, "main", "idSoalUjian", bankSoal.getIdSoalUjian());
        client.insertRecord(table, rowKey, "main", "namaUjian", bankSoal.getNamaUjian());
        client.insertRecord(table, rowKey, "main", "pertanyaan", bankSoal.getPertanyaan());
        client.insertRecord(table, rowKey, "main", "bobot", bankSoal.getBobot());
        client.insertRecord(table, rowKey, "main", "jenisSoal", bankSoal.getJenisSoal());
        client.insertRecord(table, rowKey, "main", "createdAt", bankSoal.getCreatedAt().toString());
    }

    private void updateRelationshipsBankSoal(HBaseCustomClient client, TableName table, String rowKey,
            BankSoal bankSoal) throws IOException {
        // Update SoalUjian relationship
        if (bankSoal.getSoalUjian() != null) {
            client.insertRecord(table, rowKey, "soalUjian", "idSoalUjian", bankSoal.getSoalUjian().getIdSoalUjian());
            client.insertRecord(table, rowKey, "soalUjian", "namaUjian", bankSoal.getSoalUjian().getNamaUjian());
            client.insertRecord(table, rowKey, "soalUjian", "pertanyaan", bankSoal.getSoalUjian().getPertanyaan());
            client.insertRecord(table, rowKey, "soalUjian", "bobot", bankSoal.getSoalUjian().getBobot());
            client.insertRecord(table, rowKey, "soalUjian", "jenisSoal", bankSoal.getSoalUjian().getJenisSoal());
            client.insertRecord(table, rowKey, "soalUjian", "createdAt",
                    bankSoal.getSoalUjian().getCreatedAt().toString());
        }

        // Update other relationships
        if (bankSoal.getTahunAjaran() != null) {
            client.insertRecord(table, rowKey, "tahunAjaran", "idTahun", bankSoal.getTahunAjaran().getIdTahun());
            client.insertRecord(table, rowKey, "tahunAjaran", "tahunAjaran",
                    bankSoal.getTahunAjaran().getTahunAjaran());
        }

        if (bankSoal.getSemester() != null) {
            client.insertRecord(table, rowKey, "semester", "idSemester", bankSoal.getSemester().getIdSemester());
            client.insertRecord(table, rowKey, "semester", "namaSemester", bankSoal.getSemester().getNamaSemester());
        }

        if (bankSoal.getKelas() != null) {
            client.insertRecord(table, rowKey, "kelas", "idKelas", bankSoal.getKelas().getIdKelas());
            client.insertRecord(table, rowKey, "kelas", "namaKelas", bankSoal.getKelas().getNamaKelas());
        }

        if (bankSoal.getMapel() != null) {
            client.insertRecord(table, rowKey, "mapel", "idMapel", bankSoal.getMapel().getIdMapel());
            client.insertRecord(table, rowKey, "mapel", "name", bankSoal.getMapel().getName());
        }

        if (bankSoal.getElemen() != null) {
            client.insertRecord(table, rowKey, "elemen", "idElemen", bankSoal.getElemen().getIdElemen());
            client.insertRecord(table, rowKey, "elemen", "namaElemen", bankSoal.getElemen().getNamaElemen());
        }

        if (bankSoal.getAcp() != null) {
            client.insertRecord(table, rowKey, "acp", "idAcp", bankSoal.getAcp().getIdAcp());
            client.insertRecord(table, rowKey, "acp", "namaAcp", bankSoal.getAcp().getNamaAcp());
        }

        if (bankSoal.getAtp() != null) {
            client.insertRecord(table, rowKey, "atp", "idAtp", bankSoal.getAtp().getIdAtp());
            client.insertRecord(table, rowKey, "atp", "namaAtp", bankSoal.getAtp().getNamaAtp());
        }

        if (bankSoal.getTaksonomi() != null) {
            client.insertRecord(table, rowKey, "taksonomi", "idTaksonomi", bankSoal.getTaksonomi().getIdTaksonomi());
            client.insertRecord(table, rowKey, "taksonomi", "namaTaksonomi",
                    bankSoal.getTaksonomi().getNamaTaksonomi());
        }

        if (bankSoal.getKonsentrasiKeahlianSekolah() != null) {
            client.insertRecord(table, rowKey, "konsentrasiKeahlianSekolah", "idKonsentrasiSekolah",
                    bankSoal.getKonsentrasiKeahlianSekolah().getIdKonsentrasiSekolah());
            client.insertRecord(table, rowKey, "konsentrasiKeahlianSekolah", "namaKonsentrasiSekolah",
                    bankSoal.getKonsentrasiKeahlianSekolah().getNamaKonsentrasiSekolah());
        }

        if (bankSoal.getSchool() != null) {
            client.insertRecord(table, rowKey, "school", "idSchool", bankSoal.getSchool().getIdSchool());
            client.insertRecord(table, rowKey, "school", "nameSchool", bankSoal.getSchool().getNameSchool());
        }
    }

    public List<BankSoal> findBankSoalByElemen(String elemenId, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableBankSoal = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("idBankSoal", "idBankSoal");
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
        columnMapping.put("soalUjian", "soalUjian");
        columnMapping.put("taksonomi", "taksonomi");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("acp", "acp");
        columnMapping.put("atp", "atp");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("school", "school");

        List<BankSoal> bankSoalList = client.getDataListByColumn(tableBankSoal.toString(), columnMapping, "elemen",
                "idElemen",
                elemenId, BankSoal.class, size);
        return bankSoalList;
    }

    public List<BankSoal> findBankSoalByAcp(String acpId, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableBankSoal = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("idBankSoal", "idBankSoal");
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
        columnMapping.put("soalUjian", "soalUjian");
        columnMapping.put("taksonomi", "taksonomi");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("acp", "acp");
        columnMapping.put("atp", "atp");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("school", "school");

        List<BankSoal> bankSoalList = client.getDataListByColumn(tableBankSoal.toString(), columnMapping, "acp",
                "idAcp",
                acpId, BankSoal.class, size);
        return bankSoalList;
    }

    public List<BankSoal> findBankSoalByAtp(String atpId, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableBankSoal = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("idBankSoal", "idBankSoal");
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
        columnMapping.put("soalUjian", "soalUjian");
        columnMapping.put("taksonomi", "taksonomi");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("acp", "acp");
        columnMapping.put("atp", "atp");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("school", "school");

        List<BankSoal> bankSoalList = client.getDataListByColumn(tableBankSoal.toString(), columnMapping, "atp",
                "idAtp",
                atpId, BankSoal.class, size);
        return bankSoalList;
    }

    public void updateNamaElemenByElemenId(String elemenId, String newNamaElemen) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableBankSoal = TableName.valueOf(tableName);

        // Get all BankSoal records that reference this elemen
        List<BankSoal> bankSoalList = findBankSoalByElemen(elemenId, 1000); // Use large size to get all records

        // Update nama elemen for each BankSoal record
        for (BankSoal bankSoal : bankSoalList) {
            if (bankSoal.getIdBankSoal() != null) {
                client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "elemen", "namaElemen", newNamaElemen);
                client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "detail", "updated_by",
                        "System_Cascade_Update");
            }
        }
    }

    public void updateNamaAcpByAcpId(String acpId, String newNamaAcp) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableBankSoal = TableName.valueOf(tableName);

        // Get all BankSoal records that reference this ACP
        List<BankSoal> bankSoalList = findBankSoalByAcp(acpId, 1000); // Use large size to get all records

        // Update nama ACP for each BankSoal record
        for (BankSoal bankSoal : bankSoalList) {
            if (bankSoal.getIdBankSoal() != null) {
                client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "acp", "namaAcp", newNamaAcp);
                client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "detail", "updated_by",
                        "System_Cascade_Update");
            }
        }
    }

    public void updateNamaAtpByAtpId(String atpId, String newNamaAtp) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableBankSoal = TableName.valueOf(tableName);

        // Get all BankSoal records that reference this ATP
        List<BankSoal> bankSoalList = findBankSoalByAtp(atpId, 1000); // Use large size to get all records

        // Update nama ATP for each BankSoal record
        for (BankSoal bankSoal : bankSoalList) {
            if (bankSoal.getIdBankSoal() != null) {
                client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "atp", "namaAtp", newNamaAtp);
                client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "detail", "updated_by",
                        "System_Cascade_Update");
            }
        }
    }

    public List<BankSoal> findBankSoalBySoalUjian(String soalUjianId, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableBankSoal = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("idBankSoal", "idBankSoal");
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
        columnMapping.put("soalUjian", "soalUjian");
        columnMapping.put("taksonomi", "taksonomi");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("acp", "acp");
        columnMapping.put("atp", "atp");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("school", "school");

        List<BankSoal> bankSoalList = client.getDataListByColumn(tableBankSoal.toString(), columnMapping, "soalUjian",
                "idSoalUjian",
                soalUjianId, BankSoal.class, size);
        return bankSoalList;
    }

    public void updateSoalUjianInfoBySoalUjianId(String soalUjianId, String namaUjian, String pertanyaan, String bobot,
            String jenisSoal, String toleransiTypo, Map<String, String> opsi,
            Map<String, String> pasangan, List<String> jawabanBenar) throws IOException {
        System.out.println("=== BankSoalRepository.updateSoalUjianInfoBySoalUjianId started ===");
        System.out.println("SoalUjianId: " + soalUjianId);
        System.out.println("Opsi: " + (opsi != null ? opsi.size() + " items" : "null"));
        System.out.println("Pasangan: " + (pasangan != null ? pasangan.size() + " items" : "null"));
        System.out.println("JawabanBenar: " + (jawabanBenar != null ? jawabanBenar.size() + " items" : "null"));

        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableBankSoal = TableName.valueOf(tableName);

        // Get all BankSoal records that reference this SoalUjian
        List<BankSoal> bankSoalList = findBankSoalBySoalUjian(soalUjianId, 1000); // Use large size to get all records
        System.out.println("Found " + bankSoalList.size() + " BankSoal records to update");

        // Update SoalUjian info for each BankSoal record
        for (BankSoal bankSoal : bankSoalList) {
            if (bankSoal.getIdBankSoal() != null) {
                // Update fields that are stored directly in BankSoal
                if (namaUjian != null) {
                    client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "main", "namaUjian", namaUjian);
                }
                if (pertanyaan != null) {
                    client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "main", "pertanyaan", pertanyaan);
                }
                if (bobot != null) {
                    client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "main", "bobot", bobot);
                }
                if (jenisSoal != null) {
                    client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "main", "jenisSoal", jenisSoal);
                }
                if (toleransiTypo != null) {
                    client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "main", "toleransiTypo",
                            toleransiTypo);
                }

                // Update in soalUjian nested object as well
                if (namaUjian != null) {
                    client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "soalUjian", "namaUjian", namaUjian);
                }
                if (pertanyaan != null) {
                    client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "soalUjian", "pertanyaan", pertanyaan);
                }
                if (bobot != null) {
                    client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "soalUjian", "bobot", bobot);
                }
                if (jenisSoal != null) {
                    client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "soalUjian", "jenisSoal", jenisSoal);
                }
                if (toleransiTypo != null) {
                    client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "soalUjian", "toleransiTypo",
                            toleransiTypo);
                }

                // Update opsi (for PG and MULTI questions)
                if (opsi != null) {
                    try {
                        String opsiJson = objectMapper.writeValueAsString(opsi);
                        client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "main", "opsi", opsiJson);
                        client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "soalUjian", "opsi", opsiJson);
                    } catch (JsonProcessingException e) {
                        System.err.println("Error serializing opsi: " + e.getMessage());
                    }
                }

                // Update pasangan (for COCOK questions)
                if (pasangan != null) {
                    try {
                        String pasanganJson = objectMapper.writeValueAsString(pasangan);
                        client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "main", "pasangan", pasanganJson);
                        client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "soalUjian", "pasangan",
                                pasanganJson);
                    } catch (JsonProcessingException e) {
                        System.err.println("Error serializing pasangan: " + e.getMessage());
                    }
                }

                // Update jawabanBenar
                if (jawabanBenar != null) {
                    try {
                        String jawabanJson = objectMapper.writeValueAsString(jawabanBenar);
                        client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "main", "jawabanBenar",
                                jawabanJson);
                        client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "soalUjian", "jawabanBenar",
                                jawabanJson);
                    } catch (JsonProcessingException e) {
                        System.err.println("Error serializing jawabanBenar: " + e.getMessage());
                    }
                }

                client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "detail", "updated_by",
                        "System_Cascade_Update");
            }
        }
    }

}
