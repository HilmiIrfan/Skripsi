package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.CheatDetection;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.Instant;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CheatDetectionRepository {

    Configuration conf = HBaseConfiguration.create();
    String tableName = "cheat_detection";

    @Autowired
    private ObjectMapper objectMapper; // Use the configured ObjectMapper with JSR310 support

    public List<CheatDetection> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableCheatDetection = TableName.valueOf(tableName);
        Map<String, String> columnMapping = getStandardColumnMapping();

        // Define indexed fields
        Map<String, String> indexedFields = getIndexedFields();

        return client.showListTableIndex(tableCheatDetection.toString(), columnMapping, CheatDetection.class,
                indexedFields, size);
    }

    public CheatDetection save(CheatDetection cheatDetection) throws IOException {
        // Ensure timestamps are not null before saving
        if (cheatDetection.getDetectedAt() == null) {
            if (cheatDetection.getCreatedAt() != null) {
                cheatDetection.setDetectedAt(cheatDetection.getCreatedAt());
            } else {
                cheatDetection.setDetectedAt(Instant.now());
            }
        }

        if (cheatDetection.getCreatedAt() == null) {
            cheatDetection.setCreatedAt(Instant.now());
        }

        if (cheatDetection.getUpdatedAt() == null) {
            cheatDetection.setUpdatedAt(Instant.now());
        }

        HBaseCustomClient client = new HBaseCustomClient(conf);
        String rowKey = cheatDetection.getIdDetection();
        TableName tableCheatDetection = TableName.valueOf(tableName);

        // Save main detection info
        saveMainInfo(client, tableCheatDetection, rowKey, cheatDetection);

        // Save relationships
        saveRelationships(client, tableCheatDetection, rowKey, cheatDetection);

        // Save detection data
        saveDetectionData(client, tableCheatDetection, rowKey, cheatDetection);

        // Save evidence and frontend events
        saveEvidenceData(client, tableCheatDetection, rowKey, cheatDetection);

        // Save timing and analysis data
        saveTimingData(client, tableCheatDetection, rowKey, cheatDetection);

        client.insertRecord(tableCheatDetection, rowKey, "detail", "created_by", "CHEAT-DETECTION-SYSTEM");
        return cheatDetection;
    }

    private void saveMainInfo(HBaseCustomClient client, TableName table, String rowKey, CheatDetection cheatDetection) {
        // Ensure required fields are saved
        client.insertRecord(table, rowKey, "main", "idDetection",
                cheatDetection.getIdDetection() != null ? cheatDetection.getIdDetection() : "");
        client.insertRecord(table, rowKey, "main", "sessionId",
                cheatDetection.getSessionId() != null ? cheatDetection.getSessionId() : "");
        client.insertRecord(table, rowKey, "main", "idPeserta",
                cheatDetection.getIdPeserta() != null ? cheatDetection.getIdPeserta() : "");
        client.insertRecord(table, rowKey, "main", "idUjian",
                cheatDetection.getIdUjian() != null ? cheatDetection.getIdUjian() : "");
        client.insertRecord(table, rowKey, "main", "idSchool",
                cheatDetection.getIdSchool() != null ? cheatDetection.getIdSchool() : "");

        // Detection details
        client.insertRecord(table, rowKey, "main", "typeViolation",
                cheatDetection.getTypeViolation() != null ? cheatDetection.getTypeViolation() : "");
        client.insertRecord(table, rowKey, "main", "severity",
                cheatDetection.getSeverity() != null ? cheatDetection.getSeverity() : "LOW");

        if (cheatDetection.getViolationCount() != null) {
            client.insertRecord(table, rowKey, "main", "violationCount", cheatDetection.getViolationCount().toString());
        }

        if (cheatDetection.getDetectedAt() != null) {
            client.insertRecord(table, rowKey, "main", "detectedAt", cheatDetection.getDetectedAt().toString());
        }

        if (cheatDetection.getFirstDetectedAt() != null) {
            client.insertRecord(table, rowKey, "main", "firstDetectedAt",
                    cheatDetection.getFirstDetectedAt().toString());
        }

        // Browser and system info
        if (cheatDetection.getBrowserInfo() != null) {
            client.insertRecord(table, rowKey, "main", "browserInfo", cheatDetection.getBrowserInfo());
        }

        if (cheatDetection.getUserAgent() != null) {
            client.insertRecord(table, rowKey, "main", "userAgent", cheatDetection.getUserAgent());
        }

        // Frontend detection fields
        if (cheatDetection.getWindowTitle() != null) {
            client.insertRecord(table, rowKey, "main", "windowTitle", cheatDetection.getWindowTitle());
        }

        if (cheatDetection.getScreenWidth() != null) {
            client.insertRecord(table, rowKey, "main", "screenWidth", cheatDetection.getScreenWidth().toString());
        }

        if (cheatDetection.getScreenHeight() != null) {
            client.insertRecord(table, rowKey, "main", "screenHeight", cheatDetection.getScreenHeight().toString());
        }

        if (cheatDetection.getFullscreenStatus() != null) {
            client.insertRecord(table, rowKey, "main", "fullscreenStatus",
                    cheatDetection.getFullscreenStatus().toString());
        }

        // Status fields
        if (cheatDetection.getResolved() != null) {
            client.insertRecord(table, rowKey, "main", "resolved", cheatDetection.getResolved().toString());
        }

        if (cheatDetection.getResolvedBy() != null) {
            client.insertRecord(table, rowKey, "main", "resolvedBy", cheatDetection.getResolvedBy());
        }

        if (cheatDetection.getResolvedAt() != null) {
            client.insertRecord(table, rowKey, "main", "resolvedAt", cheatDetection.getResolvedAt().toString());
        }

        if (cheatDetection.getResolutionNotes() != null) {
            client.insertRecord(table, rowKey, "main", "resolutionNotes", cheatDetection.getResolutionNotes());
        }
        client.insertRecord(table, rowKey, "main", "createdAt",
                cheatDetection.getCreatedAt() != null ? cheatDetection.getCreatedAt().toString()
                        : java.time.Instant.now().toString());
        client.insertRecord(table, rowKey, "main", "updatedAt",
                cheatDetection.getUpdatedAt() != null ? cheatDetection.getUpdatedAt().toString()
                        : java.time.Instant.now().toString());
    }

    private void saveRelationships(HBaseCustomClient client, TableName table, String rowKey,
            CheatDetection cheatDetection) {
        // Always save at least one column to prevent "No columns to insert" error
        client.insertRecord(table, rowKey, "status", "relationshipsStatus", "SAVING");

        // Save UjianSession relationship - using 'main' column family
        if (cheatDetection.getUjianSession() != null && cheatDetection.getUjianSession().getIdSession() != null) {
            client.insertRecord(table, rowKey, "main", "ujianSessionId",
                    cheatDetection.getUjianSession().getIdSession());

            if (cheatDetection.getUjianSession().getStatus() != null) {
                client.insertRecord(table, rowKey, "main", "ujianSessionStatus",
                        cheatDetection.getUjianSession().getStatus());
            }

            if (cheatDetection.getUjianSession().getStartTime() != null) {
                client.insertRecord(table, rowKey, "main", "ujianSessionStartTime",
                        cheatDetection.getUjianSession().getStartTime().toString());
            }
        }

        // Save Peserta relationship - use peserta column family
        if (cheatDetection.getPeserta() != null && cheatDetection.getPeserta().getId() != null) {
            client.insertRecord(table, rowKey, "peserta", "id",
                    cheatDetection.getPeserta().getId() != null ? cheatDetection.getPeserta().getId() : "");
            client.insertRecord(table, rowKey, "peserta", "name",
                    cheatDetection.getPeserta().getName() != null ? cheatDetection.getPeserta().getName() : "");
            client.insertRecord(table, rowKey, "peserta", "username",
                    cheatDetection.getPeserta().getUsername() != null ? cheatDetection.getPeserta().getUsername() : "");
        }

        // Save Ujian relationship - use ujian column family
        if (cheatDetection.getUjian() != null && cheatDetection.getUjian().getIdUjian() != null) {
            client.insertRecord(table, rowKey, "ujian", "idUjian", cheatDetection.getUjian().getIdUjian());
            client.insertRecord(table, rowKey, "ujian", "namaUjian",
                    cheatDetection.getUjian().getNamaUjian() != null ? cheatDetection.getUjian().getNamaUjian() : "");

            if (cheatDetection.getUjian().getDurasiMenit() != null) {
                client.insertRecord(table, rowKey, "ujian", "durasiMenit",
                        cheatDetection.getUjian().getDurasiMenit().toString());
            }
        }

        // Save School relationship - use school column family
        if (cheatDetection.getSchool() != null && cheatDetection.getSchool().getIdSchool() != null) {
            client.insertRecord(table, rowKey, "school", "idSchool", cheatDetection.getSchool().getIdSchool());
            client.insertRecord(table, rowKey, "school", "nameSchool",
                    cheatDetection.getSchool().getNameSchool() != null ? cheatDetection.getSchool().getNameSchool()
                            : "");
        }

        // Update status to completed
        client.insertRecord(table, rowKey, "status", "relationshipsStatus", "COMPLETED");
    }

    private void saveDetectionData(HBaseCustomClient client, TableName table, String rowKey,
            CheatDetection cheatDetection) {
        // Always save detection status to ensure at least one record exists
        client.insertRecord(table, rowKey, "detection", "status", "RECORDED");

        // Save action data using action column family
        if (cheatDetection.getActionTaken() != null) {
            client.insertRecord(table, rowKey, "action", "actionTaken", cheatDetection.getActionTaken());
        }

        if (cheatDetection.getActionBy() != null) {
            client.insertRecord(table, rowKey, "action", "actionBy", cheatDetection.getActionBy());
        }

        if (cheatDetection.getActionAt() != null) {
            client.insertRecord(table, rowKey, "action", "actionAt", cheatDetection.getActionAt().toString());
        }

        if (cheatDetection.getActionReason() != null) {
            client.insertRecord(table, rowKey, "action", "actionReason", cheatDetection.getActionReason());
        }
    }

    private void saveEvidenceData(HBaseCustomClient client, TableName table, String rowKey,
            CheatDetection cheatDetection) {
        try {
            // Save evidence using evidence column family
            if (cheatDetection.getEvidence() != null && !cheatDetection.getEvidence().isEmpty()) {
                String evidenceJson = objectMapper.writeValueAsString(cheatDetection.getEvidence());
                client.insertRecord(table, rowKey, "evidence", "data", evidenceJson);
            } else {
                client.insertRecord(table, rowKey, "evidence", "data", "{}");
            }

            // Save frontend events using frontend column family
            if (cheatDetection.getFrontendEvents() != null && !cheatDetection.getFrontendEvents().isEmpty()) {
                String frontendEventsJson = objectMapper.writeValueAsString(cheatDetection.getFrontendEvents());
                client.insertRecord(table, rowKey, "frontend", "events", frontendEventsJson);
            } else {
                client.insertRecord(table, rowKey, "frontend", "events", "{}");
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize evidence data", e);
        }
    }

    private void saveTimingData(HBaseCustomClient client, TableName table, String rowKey,
            CheatDetection cheatDetection) {
        try {
            // Always save timing status
            client.insertRecord(table, rowKey, "timing", "status", "RECORDED");

            // Save timing analysis using timing column family
            if (cheatDetection.getTimeBetweenAnswers() != null) {
                client.insertRecord(table, rowKey, "timing", "timeBetweenAnswers",
                        cheatDetection.getTimeBetweenAnswers().toString());
            }

            if (cheatDetection.getAnswerPattern() != null) {
                client.insertRecord(table, rowKey, "timing", "answerPattern", cheatDetection.getAnswerPattern());
            }

            // Save answer timestamps
            if (cheatDetection.getAnswerTimestamps() != null && !cheatDetection.getAnswerTimestamps().isEmpty()) {
                String answerTimestampsJson = objectMapper.writeValueAsString(cheatDetection.getAnswerTimestamps());
                client.insertRecord(table, rowKey, "timing", "answerTimestamps", answerTimestampsJson);
            } else {
                client.insertRecord(table, rowKey, "timing", "answerTimestamps", "{}");
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize timing data", e);
        }
    }

    public CheatDetection findById(String detectionId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableCheatDetection = TableName.valueOf(tableName);
        Map<String, String> columnMapping = getStandardColumnMapping();

        return client.showDataTable(tableCheatDetection.toString(), columnMapping, detectionId, CheatDetection.class);
    }

    public List<CheatDetection> findBySessionId(String sessionId) throws IOException {
        TableName tableCheatDetection = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        return client.getDataListByColumnIndeks(
                tableCheatDetection.toString(),
                columnMapping,
                "main",
                "sessionId",
                sessionId,
                CheatDetection.class,
                1000,
                indexedFields);
    }

    public List<CheatDetection> findByUjianId(String idUjian) throws IOException {
        TableName tableCheatDetection = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        return client.getDataListByColumnIndeks(
                tableCheatDetection.toString(),
                columnMapping,
                "main",
                "idUjian",
                idUjian,
                CheatDetection.class,
                1000,
                indexedFields);
    }

    public List<CheatDetection> findByPesertaId(String idPeserta) throws IOException {
        TableName tableCheatDetection = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        return client.getDataListByColumnIndeks(
                tableCheatDetection.toString(),
                columnMapping,
                "main",
                "idPeserta",
                idPeserta,
                CheatDetection.class,
                1000,
                indexedFields);
    }

    public List<CheatDetection> findBySchoolId(String idSchool) throws IOException {
        TableName tableCheatDetection = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        return client.getDataListByColumnIndeks(
                tableCheatDetection.toString(),
                columnMapping,
                "main",
                "idSchool",
                idSchool,
                CheatDetection.class,
                1000,
                indexedFields);
    }

    public List<CheatDetection> findByTypeViolation(String typeViolation) throws IOException {
        TableName tableCheatDetection = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        return client.getDataListByColumnIndeks(
                tableCheatDetection.toString(),
                columnMapping,
                "main",
                "typeViolation",
                typeViolation,
                CheatDetection.class,
                1000,
                indexedFields);
    }

    public List<CheatDetection> findBySeverity(String severity) throws IOException {
        TableName tableCheatDetection = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        return client.getDataListByColumnIndeks(
                tableCheatDetection.toString(),
                columnMapping,
                "main",
                "severity",
                severity,
                CheatDetection.class,
                1000,
                indexedFields);
    }

    public List<CheatDetection> findUnresolved(int limit) throws IOException {
        TableName tableCheatDetection = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        List<CheatDetection> allDetections = client.getDataListByColumnIndeks(
                tableCheatDetection.toString(),
                columnMapping,
                "main",
                "resolved",
                "false",
                CheatDetection.class,
                limit * 2,
                indexedFields);

        // Additional filtering for null resolved status
        List<CheatDetection> unresolvedByNull = client.showListTableIndex(
                tableCheatDetection.toString(),
                columnMapping,
                CheatDetection.class,
                indexedFields,
                limit);

        unresolvedByNull = unresolvedByNull.stream()
                .filter(d -> d.getResolved() == null || !d.getResolved())
                .collect(Collectors.toList());

        // Combine and deduplicate
        allDetections.addAll(unresolvedByNull);
        return allDetections.stream()
                .distinct()
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<CheatDetection> findCriticalViolations(int limit) throws IOException {
        return findBySeverity("CRITICAL").stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<CheatDetection> findBySessionAndType(String sessionId, String typeViolation) throws IOException {
        // Get by session first, then filter by type
        List<CheatDetection> sessionViolations = findBySessionId(sessionId);

        return sessionViolations.stream()
                .filter(violation -> typeViolation.equals(violation.getTypeViolation()))
                .collect(Collectors.toList());
    }

    public List<CheatDetection> findByUjianAndSeverity(String idUjian, String severity) throws IOException {
        // Get by ujian first, then filter by severity
        List<CheatDetection> ujianViolations = findByUjianId(idUjian);

        return ujianViolations.stream()
                .filter(violation -> severity.equals(violation.getSeverity()))
                .collect(Collectors.toList());
    }

    public List<CheatDetection> findRecentViolations(String timeRange, int limit) throws IOException {
        List<CheatDetection> allViolations = findAll(limit * 2);

        Instant threshold;
        switch (timeRange.toUpperCase()) {
            case "HOUR":
                threshold = Instant.now().minusSeconds(3600);
                break;
            case "DAY":
                threshold = Instant.now().minusSeconds(86400);
                break;
            case "WEEK":
                threshold = Instant.now().minusSeconds(604800);
                break;
            default:
                return allViolations.stream().limit(limit).collect(Collectors.toList());
        }

        return allViolations.stream()
                .filter(violation -> violation.getDetectedAt().isAfter(threshold))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Get standard column mapping used across all queries
     */
    // Inside CheatDetectionRepository.java

    private Map<String, String> getStandardColumnMapping() {
        Map<String, String> columnMapping = new HashMap<>();

        // Main detection fields - assuming these qualifiers are directly mapped
        columnMapping.put("idDetection", "idDetection");
        columnMapping.put("sessionId", "sessionId");
        columnMapping.put("idPeserta", "idPeserta");
        columnMapping.put("idUjian", "idUjian");
        columnMapping.put("idSchool", "idSchool");
        columnMapping.put("typeViolation", "typeViolation");
        columnMapping.put("severity", "severity");
        columnMapping.put("violationCount", "violationCount");
        columnMapping.put("detectedAt", "detectedAt");
        columnMapping.put("firstDetectedAt", "firstDetectedAt");

        // Browser and system info - assuming these qualifiers are directly mapped
        columnMapping.put("browserInfo", "browserInfo");
        columnMapping.put("userAgent", "userAgent");
        columnMapping.put("windowTitle", "windowTitle");
        columnMapping.put("screenWidth", "screenWidth");
        columnMapping.put("screenHeight", "screenHeight");
        columnMapping.put("fullscreenStatus", "fullscreenStatus");

        // Evidence and events - assuming these qualifiers are directly mapped
        // NOTE: If 'evidence:data' and 'frontend:events' are unique qualifiers across
        // column families,
        // they can be mapped directly. If 'data' or 'events' exist in multiple
        // families,
        // your HBaseCustomClient needs a more sophisticated way to differentiate.
        columnMapping.put("data", "evidence"); // Maps 'data' qualifier to CheatDetection.evidence
        columnMapping.put("events", "frontendEvents"); // Maps 'events' qualifier to CheatDetection.frontendEvents

        // Timing data - assuming these qualifiers are directly mapped
        columnMapping.put("timeBetweenAnswers", "timeBetweenAnswers");
        columnMapping.put("answerPattern", "answerPattern");
        columnMapping.put("answerTimestamps", "answerTimestamps");

        // Action data - assuming these qualifiers are directly mapped
        columnMapping.put("actionTaken", "actionTaken");
        columnMapping.put("actionBy", "actionBy");
        columnMapping.put("actionAt", "actionAt");
        columnMapping.put("actionReason", "actionReason");

        // Status fields - assuming these qualifiers are directly mapped
        columnMapping.put("resolved", "resolved");
        columnMapping.put("resolvedBy", "resolvedBy");
        columnMapping.put("resolvedAt", "resolvedAt");
        columnMapping.put("resolutionNotes", "resolutionNotes");

        // Timestamps - assuming these qualifiers are directly mapped
        columnMapping.put("createdAt", "createdAt");
        columnMapping.put("updatedAt", "updatedAt");

        // UjianSession fields - assuming these qualifiers are directly mapped
        columnMapping.put("ujianSessionId", "ujianSessionId");
        columnMapping.put("ujianSessionStatus", "ujianSessionStatus");
        columnMapping.put("ujianSessionStartTime", "ujianSessionStartTime");

        // Relationship fields - assuming these qualifiers are directly mapped
        columnMapping.put("id", "pesertaId"); // Maps 'id' qualifier (from 'peserta:id') to 'pesertaId'
        columnMapping.put("name", "pesertaName"); // Maps 'name' qualifier (from 'peserta:name') to 'pesertaName'
        columnMapping.put("username", "pesertaUsername"); // Maps 'username' qualifier (from 'peserta:username') to
                                                          // 'pesertaUsername'
        columnMapping.put("idUjian", "ujianId"); // Maps 'idUjian' qualifier (from 'ujian:idUjian') to 'ujianId'
        columnMapping.put("namaUjian", "ujianNama"); // Maps 'namaUjian' qualifier (from 'ujian:namaUjian') to
                                                     // 'ujianNama'
        columnMapping.put("durasiMenit", "ujianDurasiMenit"); // Maps 'durasiMenit' qualifier (from 'ujian:durasiMenit')
                                                              // to 'ujianDurasiMenit'
        columnMapping.put("idSchool", "schoolId"); // Maps 'idSchool' qualifier (from 'school:idSchool') to 'schoolId'
        columnMapping.put("nameSchool", "schoolName"); // Maps 'nameSchool' qualifier (from 'school:nameSchool') to
                                                       // 'schoolName'

        // Additional fields if they are always in 'detail', 'detection', 'status' and
        // their qualifiers are unique
        columnMapping.put("created_by", "createdBy");
        columnMapping.put("status", "detectionStatus"); // For 'detection:status'
        columnMapping.put("relationshipsStatus", "relationshipsStatus"); // For 'status:relationshipsStatus'

        return columnMapping;
    }

    /**
     * Get indexed fields configuration
     */
    private Map<String, String> getIndexedFields() {
        Map<String, String> indexedFields = new HashMap<>();
        indexedFields.put("evidence", "MAP");
        indexedFields.put("frontendEvents", "MAP");
        indexedFields.put("answerTimestamps", "MAP");
        return indexedFields;
    }

    public boolean deleteById(String detectionId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, detectionId);
        return true;
    }

    public boolean existsById(String detectionId) throws IOException {
        try {
            HBaseCustomClient client = new HBaseCustomClient(conf);
            TableName tableCheatDetection = TableName.valueOf(tableName);
            Map<String, String> columnMapping = new HashMap<>();
            columnMapping.put("idDetection", "idDetection");

            CheatDetection detection = client.getDataByColumn(tableCheatDetection.toString(), columnMapping,
                    "main", "idDetection", detectionId, CheatDetection.class);

            return detection != null && detection.getIdDetection() != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Update violation status - optimized for status changes
     */
    public boolean updateViolationStatus(String detectionId, String newSeverity, Boolean resolved) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableCheatDetection = TableName.valueOf(tableName);

        if (newSeverity != null) {
            client.insertRecord(tableCheatDetection, detectionId, "main", "severity", newSeverity);
        }

        if (resolved != null) {
            client.insertRecord(tableCheatDetection, detectionId, "main", "resolved", resolved.toString());
            if (resolved) {
                client.insertRecord(tableCheatDetection, detectionId, "main", "resolvedAt", Instant.now().toString());
            }
        }

        client.insertRecord(tableCheatDetection, detectionId, "main", "updatedAt", Instant.now().toString());

        return true;
    }

    /**
     * Update action taken - optimized for action updates
     */
    public boolean updateActionTaken(String detectionId, String actionTaken, String actionBy, String reason)
            throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableCheatDetection = TableName.valueOf(tableName);

        client.insertRecord(tableCheatDetection, detectionId, "action", "actionTaken", actionTaken);
        client.insertRecord(tableCheatDetection, detectionId, "action", "actionBy", actionBy);
        client.insertRecord(tableCheatDetection, detectionId, "action", "actionAt", Instant.now().toString());

        if (reason != null) {
            client.insertRecord(tableCheatDetection, detectionId, "action", "actionReason", reason);
        }

        client.insertRecord(tableCheatDetection, detectionId, "main", "updatedAt", Instant.now().toString());

        return true;
    }

    /**
     * Increment violation count - optimized for count updates
     */
    public boolean incrementViolationCount(String detectionId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableCheatDetection = TableName.valueOf(tableName);

        // Get current count
        CheatDetection current = findById(detectionId);
        if (current != null) {
            int newCount = (current.getViolationCount() != null ? current.getViolationCount() : 0) + 1;
            client.insertRecord(tableCheatDetection, detectionId, "main", "violationCount", String.valueOf(newCount));
            client.insertRecord(tableCheatDetection, detectionId, "main", "updatedAt", Instant.now().toString());
            return true;
        }

        return false;
    }

    /**
     * Count violations by criteria - useful for statistics
     */
    public long countBySessionId(String sessionId) throws IOException {
        List<CheatDetection> violations = findBySessionId(sessionId);
        return violations.size();
    }

    public long countByUjianId(String idUjian) throws IOException {
        List<CheatDetection> violations = findByUjianId(idUjian);
        return violations.size();
    }

    public long countByPesertaId(String idPeserta) throws IOException {
        List<CheatDetection> violations = findByPesertaId(idPeserta);
        return violations.size();
    }

    public long countBySchoolAndSeverity(String idSchool, String severity) throws IOException {
        List<CheatDetection> schoolViolations = findBySchoolId(idSchool);
        return schoolViolations.stream()
                .filter(violation -> severity == null || severity.equals(violation.getSeverity()))
                .count();
    }

    public long countUnresolvedBySchool(String idSchool) throws IOException {
        List<CheatDetection> schoolViolations = findBySchoolId(idSchool);
        return schoolViolations.stream()
                .filter(violation -> violation.getResolved() == null || !violation.getResolved())
                .count();
    }

    /**
     * Find sessions with multiple violations - useful for analysis
     */
    public Map<String, Long> getSessionViolationCounts(String idUjian, int minViolations) throws IOException {
        List<CheatDetection> ujianViolations = findByUjianId(idUjian);

        return ujianViolations.stream()
                .collect(Collectors.groupingBy(CheatDetection::getSessionId, Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() >= minViolations)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Get violation trends by type - useful for analytics
     */
    public Map<String, Long> getViolationTrendsByType(String idSchool, String timeRange) throws IOException {
        List<CheatDetection> recentViolations = findRecentViolations(timeRange, 10000);

        if (idSchool != null) {
            recentViolations = recentViolations.stream()
                    .filter(violation -> idSchool.equals(violation.getIdSchool()))
                    .collect(Collectors.toList());
        }

        return recentViolations.stream()
                .collect(Collectors.groupingBy(CheatDetection::getTypeViolation, Collectors.counting()));
    }
}
