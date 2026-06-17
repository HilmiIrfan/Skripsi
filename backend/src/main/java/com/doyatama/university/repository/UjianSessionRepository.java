package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.UjianSession;

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
import org.springframework.stereotype.Repository;

@Repository
public class UjianSessionRepository {

    Configuration conf = HBaseConfiguration.create();
    String tableName = "ujian_session";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<UjianSession> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUjianSession = TableName.valueOf(tableName);
        Map<String, String> columnMapping = getStandardColumnMapping();

        // Define indexed fields
        Map<String, String> indexedFields = getIndexedFields();

        return client.showListTableIndex(tableUjianSession.toString(), columnMapping, UjianSession.class, indexedFields,
                size);
    }

    public UjianSession save(UjianSession ujianSession) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        String rowKey = ujianSession.getIdSession();
        TableName tableUjianSession = TableName.valueOf(tableName);

        // Save main session info
        saveMainInfo(client, tableUjianSession, rowKey, ujianSession);

        // Save relationships
        saveRelationships(client, tableUjianSession, rowKey, ujianSession);

        // Save session data
        saveSessionData(client, tableUjianSession, rowKey, ujianSession);

        // Save tracking data
        saveTrackingData(client, tableUjianSession, rowKey, ujianSession);

        client.insertRecord(tableUjianSession, rowKey, "detail", "created_by", "CAT-System");
        return ujianSession;
    }

    private void saveMainInfo(HBaseCustomClient client, TableName table, String rowKey, UjianSession ujianSession) {
        client.insertRecord(table, rowKey, "main", "idSession", ujianSession.getIdSession());
        client.insertRecord(table, rowKey, "main", "idUjian", ujianSession.getIdUjian());
        client.insertRecord(table, rowKey, "main", "idPeserta", ujianSession.getIdPeserta());
        client.insertRecord(table, rowKey, "main", "sessionId", ujianSession.getSessionId());
        client.insertRecord(table, rowKey, "main", "status", ujianSession.getStatus());

        if (ujianSession.getStartTime() != null) {
            client.insertRecord(table, rowKey, "main", "startTime", ujianSession.getStartTime().toString());
        }

        if (ujianSession.getEndTime() != null) {
            client.insertRecord(table, rowKey, "main", "endTime", ujianSession.getEndTime().toString());
        }

        if (ujianSession.getCurrentSoalIndex() != null) {
            client.insertRecord(table, rowKey, "main", "currentSoalIndex",
                    ujianSession.getCurrentSoalIndex().toString());
        }

        if (ujianSession.getAttemptNumber() != null) {
            client.insertRecord(table, rowKey, "main", "attemptNumber", ujianSession.getAttemptNumber().toString());
        }

        if (ujianSession.getTimeRemaining() != null) {
            client.insertRecord(table, rowKey, "main", "timeRemaining", ujianSession.getTimeRemaining().toString());
        }

        if (ujianSession.getIsSubmitted() != null) {
            client.insertRecord(table, rowKey, "main", "isSubmitted", ujianSession.getIsSubmitted().toString());
        }

        if (ujianSession.getIsAutoSubmit() != null) {
            client.insertRecord(table, rowKey, "main", "isAutoSubmit", ujianSession.getIsAutoSubmit().toString());
        }

        if (ujianSession.getSubmittedAt() != null) {
            client.insertRecord(table, rowKey, "main", "submittedAt", ujianSession.getSubmittedAt().toString());
        }

        client.insertRecord(table, rowKey, "main", "idSchool", ujianSession.getIdSchool());
        client.insertRecord(table, rowKey, "main", "createdAt", ujianSession.getCreatedAt().toString());
        client.insertRecord(table, rowKey, "main", "updatedAt", ujianSession.getUpdatedAt().toString());
    }

    private void saveRelationships(HBaseCustomClient client, TableName table, String rowKey,
            UjianSession ujianSession) {
        // Save Ujian relationship
        if (ujianSession.getUjian() != null && ujianSession.getUjian().getIdUjian() != null) {
            client.insertRecord(table, rowKey, "ujian", "idUjian", ujianSession.getUjian().getIdUjian());
            client.insertRecord(table, rowKey, "ujian", "namaUjian", ujianSession.getUjian().getNamaUjian());

            if (ujianSession.getUjian().getDurasiMenit() != null) {
                client.insertRecord(table, rowKey, "ujian", "durasiMenit",
                        ujianSession.getUjian().getDurasiMenit().toString());
            }
        }

        // Save Peserta relationship
        if (ujianSession.getPeserta() != null && ujianSession.getPeserta().getId() != null) {
            client.insertRecord(table, rowKey, "peserta", "id", ujianSession.getPeserta().getId());
            client.insertRecord(table, rowKey, "peserta", "name", ujianSession.getPeserta().getName());
            client.insertRecord(table, rowKey, "peserta", "username", ujianSession.getPeserta().getUsername());
        }

        // Save School relationship
        if (ujianSession.getSchool() != null && ujianSession.getSchool().getIdSchool() != null) {
            client.insertRecord(table, rowKey, "school", "idSchool", ujianSession.getSchool().getIdSchool());
            client.insertRecord(table, rowKey, "school", "nameSchool", ujianSession.getSchool().getNameSchool());
        }
    }

    private void saveSessionData(HBaseCustomClient client, TableName table, String rowKey, UjianSession ujianSession) {
        try {
            // Save answers
            if (ujianSession.getAnswers() != null && !ujianSession.getAnswers().isEmpty()) {
                String answersJson = objectMapper.writeValueAsString(ujianSession.getAnswers());
                client.insertRecord(table, rowKey, "main", "answers", answersJson);
            } else {
                client.insertRecord(table, rowKey, "main", "answers", "{}");
            }

            // Save session metadata
            if (ujianSession.getSessionMetadata() != null && !ujianSession.getSessionMetadata().isEmpty()) {
                String sessionMetadataJson = objectMapper.writeValueAsString(ujianSession.getSessionMetadata());
                client.insertRecord(table, rowKey, "main", "sessionMetadata", sessionMetadataJson);
            } else {
                client.insertRecord(table, rowKey, "main", "sessionMetadata", "{}");
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize session data", e);
        }
    }

    private void saveTrackingData(HBaseCustomClient client, TableName table, String rowKey, UjianSession ujianSession) {
        try {
            // Save navigation history
            if (ujianSession.getNavigationHistory() != null && !ujianSession.getNavigationHistory().isEmpty()) {
                String navigationHistoryJson = objectMapper.writeValueAsString(ujianSession.getNavigationHistory());
                client.insertRecord(table, rowKey, "tracking", "navigationHistory", navigationHistoryJson);
            } else {
                client.insertRecord(table, rowKey, "tracking", "navigationHistory", "{}");
            }

            // Save progress tracking
            if (ujianSession.getAnsweredQuestions() != null) {
                client.insertRecord(table, rowKey, "tracking", "answeredQuestions",
                        ujianSession.getAnsweredQuestions().toString());
            }

            if (ujianSession.getTotalQuestions() != null) {
                client.insertRecord(table, rowKey, "tracking", "totalQuestions",
                        ujianSession.getTotalQuestions().toString());
            }

            if (ujianSession.getLastKeepAliveTime() != null) {
                client.insertRecord(table, rowKey, "tracking", "lastKeepAliveTime",
                        ujianSession.getLastKeepAliveTime().toString());
            }

            if (ujianSession.getLastAutoSave() != null) {
                client.insertRecord(table, rowKey, "tracking", "lastAutoSave",
                        ujianSession.getLastAutoSave().toString());
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize tracking data", e);
        }
    }

    public UjianSession findById(String sessionId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableUjianSession = TableName.valueOf(tableName);
        Map<String, String> columnMapping = getStandardColumnMapping();

        return client.showDataTable(tableUjianSession.toString(), columnMapping, sessionId, UjianSession.class);
    }

    public UjianSession findBySessionId(String sessionId) throws IOException {
        TableName tableUjianSession = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        List<UjianSession> sessions = client.getDataListByColumnIndeks(
                tableUjianSession.toString(),
                columnMapping,
                "main",
                "sessionId",
                sessionId,
                UjianSession.class,
                1,
                indexedFields);

        // Pastikan sessionId benar-benar cocok (hindari duplikasi)
        return sessions.stream()
                .filter(s -> sessionId.equals(s.getSessionId()))
                .findFirst()
                .orElse(null);
    }

    public UjianSession findActiveSessionByUjianAndPeserta(String idUjian, String idPeserta) throws IOException {
        // Validate input parameters
        if (idUjian == null || idPeserta == null) {
            throw new IllegalArgumentException("idUjian and idPeserta cannot be null");
        }

        TableName tableUjianSession = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        try {
            // Find sessions by ujian first
            List<UjianSession> sessions = client.getDataListByColumnIndeks(
                    tableUjianSession.toString(),
                    columnMapping,
                    "main",
                    "idUjian",
                    idUjian,
                    UjianSession.class,
                    100, // Get more for filtering
                    indexedFields);

            // Filter by peserta and active status
            return sessions.stream()
                    .filter(session -> idPeserta.equals(session.getIdPeserta()))
                    .filter(session -> "ACTIVE".equals(session.getStatus()))
                    .filter(session -> session.getIsSubmitted() == null || !session.getIsSubmitted())
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            throw new IOException("Failed to find active session for ujian: " + idUjian + " and peserta: " + idPeserta,
                    e);
        }
    }

    public List<UjianSession> findSessionsByUjian(String idUjian) throws IOException {
        TableName tableUjianSession = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        return client.getDataListByColumnIndeks(
                tableUjianSession.toString(),
                columnMapping,
                "main",
                "idUjian",
                idUjian,
                UjianSession.class,
                1000,
                indexedFields);
    }

    public List<UjianSession> findSessionsByPeserta(String idPeserta) throws IOException {
        TableName tableUjianSession = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        return client.getDataListByColumnIndeks(
                tableUjianSession.toString(),
                columnMapping,
                "main",
                "idPeserta",
                idPeserta,
                UjianSession.class,
                1000,
                indexedFields);
    }

    public List<UjianSession> findAllActiveSessions(int limit) throws IOException {
        TableName tableUjianSession = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        List<UjianSession> allSessions = client.getDataListByColumnIndeks(
                tableUjianSession.toString(),
                columnMapping,
                "main",
                "status",
                "ACTIVE",
                UjianSession.class,
                limit * 2, // Get more for additional filtering
                indexedFields);

        // Additional filtering for truly active sessions
        return allSessions.stream()
                .filter(session -> session.getIsSubmitted() == null || !session.getIsSubmitted())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<UjianSession> findActiveSessionsBySchool(String schoolId, int limit) throws IOException {
        TableName tableUjianSession = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        // Get sessions by school first
        List<UjianSession> sessions = client.getDataListByColumnIndeks(
                tableUjianSession.toString(),
                columnMapping,
                "main",
                "idSchool",
                schoolId,
                UjianSession.class,
                limit * 2,
                indexedFields);

        // Filter by active status
        return sessions.stream()
                .filter(session -> "ACTIVE".equals(session.getStatus()))
                .filter(session -> session.getIsSubmitted() == null || !session.getIsSubmitted())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<UjianSession> findSessionsByStatus(String status, int limit) throws IOException {
        TableName tableUjianSession = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        return client.getDataListByColumnIndeks(
                tableUjianSession.toString(),
                columnMapping,
                "main",
                "status",
                status,
                UjianSession.class,
                limit,
                indexedFields);
    }

    public List<UjianSession> findSessionsByStatusAndSchool(String status, String schoolId, int limit)
            throws IOException {
        TableName tableUjianSession = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        // Get sessions by school first, then filter by status
        List<UjianSession> sessions = client.getDataListByColumnIndeks(
                tableUjianSession.toString(),
                columnMapping,
                "main",
                "idSchool",
                schoolId,
                UjianSession.class,
                limit * 2,
                indexedFields);

        return sessions.stream()
                .filter(session -> status.equals(session.getStatus()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Get standard column mapping used across all queries
     */
    private Map<String, String> getStandardColumnMapping() {
        Map<String, String> columnMapping = new HashMap<>();

        // Main session fields
        columnMapping.put("idSession", "idSession");
        columnMapping.put("idUjian", "idUjian");
        columnMapping.put("idPeserta", "idPeserta");
        columnMapping.put("sessionId", "sessionId");
        columnMapping.put("status", "status");
        columnMapping.put("startTime", "startTime");
        columnMapping.put("endTime", "endTime");
        columnMapping.put("currentSoalIndex", "currentSoalIndex");
        columnMapping.put("attemptNumber", "attemptNumber");
        columnMapping.put("timeRemaining", "timeRemaining");
        columnMapping.put("isSubmitted", "isSubmitted");
        columnMapping.put("isAutoSubmit", "isAutoSubmit");
        columnMapping.put("submittedAt", "submittedAt");
        columnMapping.put("idSchool", "idSchool");
        columnMapping.put("createdAt", "createdAt");
        columnMapping.put("updatedAt", "updatedAt");

        // Session data
        columnMapping.put("answers", "answers");
        columnMapping.put("sessionMetadata", "sessionMetadata");

        // Tracking data
        columnMapping.put("navigationHistory", "navigationHistory");
        columnMapping.put("answeredQuestions", "answeredQuestions");
        columnMapping.put("totalQuestions", "totalQuestions");
        columnMapping.put("lastKeepAliveTime", "lastKeepAliveTime");
        columnMapping.put("lastAutoSave", "lastAutoSave");

        // Relationships
        columnMapping.put("ujian", "ujian");
        columnMapping.put("peserta", "peserta");
        columnMapping.put("school", "school");

        return columnMapping;
    }

    /**
     * Get indexed fields configuration
     */
    private Map<String, String> getIndexedFields() {
        Map<String, String> indexedFields = new HashMap<>();
        indexedFields.put("answers", "MAP");
        indexedFields.put("sessionMetadata", "MAP");
        indexedFields.put("navigationHistory", "MAP");
        return indexedFields;
    }

    public boolean deleteById(String sessionId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, sessionId);
        return true;
    }

    public boolean existsById(String sessionId) throws IOException {
        try {
            HBaseCustomClient client = new HBaseCustomClient(conf);
            TableName tableUjianSession = TableName.valueOf(tableName);
            Map<String, String> columnMapping = new HashMap<>();
            columnMapping.put("idSession", "idSession");

            UjianSession session = client.getDataByColumn(tableUjianSession.toString(), columnMapping,
                    "main", "idSession",
                    sessionId, UjianSession.class);

            return session != null && session.getIdSession() != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Update session status - optimized for status changes
     */
    public boolean updateStatus(String sessionId, String newStatus) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableUjianSession = TableName.valueOf(tableName);

        client.insertRecord(tableUjianSession, sessionId, "main", "status", newStatus);
        client.insertRecord(tableUjianSession, sessionId, "main", "updatedAt", Instant.now().toString());

        return true;
    }

    /**
     * Update session answers - optimized for answer updates
     */
    public boolean updateAnswers(String sessionId, Map<String, Object> answers) throws IOException {
        try {
            HBaseCustomClient client = new HBaseCustomClient(conf);
            TableName tableUjianSession = TableName.valueOf(tableName);

            String answersJson = objectMapper.writeValueAsString(answers);
            client.insertRecord(tableUjianSession, sessionId, "main", "answers", answersJson);
            client.insertRecord(tableUjianSession, sessionId, "main", "updatedAt", Instant.now().toString());
            client.insertRecord(tableUjianSession, sessionId, "tracking", "answeredQuestions",
                    String.valueOf(answers != null ? answers.size() : 0));

            return true;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize answers", e);
        }
    }

    /**
     * Update keep alive time - optimized for ping updates
     */
    public boolean updateKeepAliveTime(String sessionId, Instant keepAliveTime) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableUjianSession = TableName.valueOf(tableName);

        client.insertRecord(tableUjianSession, sessionId, "tracking", "lastKeepAliveTime", keepAliveTime.toString());
        client.insertRecord(tableUjianSession, sessionId, "main", "updatedAt", Instant.now().toString());

        return true;
    }

    /**
     * Update current soal index - optimized for navigation updates
     */
    public boolean updateCurrentSoalIndex(String sessionId, Integer soalIndex) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableUjianSession = TableName.valueOf(tableName);

        client.insertRecord(tableUjianSession, sessionId, "main", "currentSoalIndex", soalIndex.toString());
        client.insertRecord(tableUjianSession, sessionId, "main", "updatedAt", Instant.now().toString());

        return true;
    }

    /**
     * Finalize session - update submission status
     */
    public boolean finalizeSession(String sessionId, boolean isAutoSubmit, Instant endTime) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableUjianSession = TableName.valueOf(tableName);

        client.insertRecord(tableUjianSession, sessionId, "main", "status", "COMPLETED");
        client.insertRecord(tableUjianSession, sessionId, "main", "isSubmitted", "true");
        client.insertRecord(tableUjianSession, sessionId, "main", "isAutoSubmit", String.valueOf(isAutoSubmit));
        client.insertRecord(tableUjianSession, sessionId, "main", "endTime", endTime.toString());
        client.insertRecord(tableUjianSession, sessionId, "main", "submittedAt", Instant.now().toString());
        client.insertRecord(tableUjianSession, sessionId, "main", "updatedAt", Instant.now().toString());

        return true;
    }

    /**
     * Count sessions by criteria - useful for statistics
     */
    public long countByUjianAndStatus(String idUjian, String status) throws IOException {
        List<UjianSession> sessions = findSessionsByUjian(idUjian);
        return sessions.stream()
                .filter(session -> status == null || status.equals(session.getStatus()))
                .count();
    }

    public long countBySchoolAndStatus(String schoolId, String status) throws IOException {
        List<UjianSession> sessions = findActiveSessionsBySchool(schoolId, 1000); // Get large number for counting
        return sessions.stream()
                .filter(session -> status == null || status.equals(session.getStatus()))
                .count();
    }

    /**
     * Find sessions with timeout risk - sessions that haven't been updated recently
     */
    public List<UjianSession> findSessionsWithTimeoutRisk(int minutesThreshold, int limit) throws IOException {
        List<UjianSession> activeSessions = findAllActiveSessions(limit * 2);
        Instant thresholdTime = Instant.now().minusSeconds(minutesThreshold * 60);

        return activeSessions.stream()
                .filter(session -> session.getLastKeepAliveTime() != null)
                .filter(session -> session.getLastKeepAliveTime().isBefore(thresholdTime))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Update session metadata - optimized for metadata updates
     */
    public boolean updateSessionMetadata(String sessionId, Map<String, Object> sessionMetadata) throws IOException {
        try {
            HBaseCustomClient client = new HBaseCustomClient(conf);
            TableName tableUjianSession = TableName.valueOf(tableName);

            String metadataJson = objectMapper.writeValueAsString(sessionMetadata);
            client.insertRecord(tableUjianSession, sessionId, "main", "sessionMetadata", metadataJson);
            client.insertRecord(tableUjianSession, sessionId, "main", "updatedAt", Instant.now().toString());

            return true;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize session metadata", e);
        }
    }
}
