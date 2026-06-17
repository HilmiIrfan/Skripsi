package com.doyatama.university.payload;

import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UjianSessionRequest {
    private String idUjian;
    private String idPeserta;
    private String sessionId;
    private String timestamp;

    // Default constructor
    public UjianSessionRequest() {
        this.timestamp = Instant.now().toString();
    }

    // Constructor with common fields
    public UjianSessionRequest(String idUjian, String idPeserta, String sessionId) {
        this();
        this.idUjian = idUjian;
        this.idPeserta = idPeserta;
        this.sessionId = sessionId;
    }

    // Getters and setters
    public String getIdUjian() {
        return idUjian;
    }

    public void setIdUjian(String idUjian) {
        this.idUjian = idUjian;
    }

    public String getIdPeserta() {
        return idPeserta;
    }

    public void setIdPeserta(String idPeserta) {
        this.idPeserta = idPeserta;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    // Subclass for starting a session
    public static class StartSessionRequest extends UjianSessionRequest {
        private String kodeUjian;
        private Integer attemptNumber;
        private Map<String, Object> sessionMetadata;

        public StartSessionRequest() {
            super();
            this.attemptNumber = 1;
        }

        public String getKodeUjian() {
            return kodeUjian;
        }

        public void setKodeUjian(String kodeUjian) {
            this.kodeUjian = kodeUjian;
        }

        public Integer getAttemptNumber() {
            return attemptNumber;
        }

        public void setAttemptNumber(Integer attemptNumber) {
            this.attemptNumber = attemptNumber;
        }

        public Map<String, Object> getSessionMetadata() {
            return sessionMetadata;
        }

        public void setSessionMetadata(Map<String, Object> sessionMetadata) {
            this.sessionMetadata = sessionMetadata;
        }
    }

    // Subclass untuk simpan jawaban individual
    public static class SaveJawabanRequest extends UjianSessionRequest {
        private String idBankSoal; // Pastikan frontend mengirim idBankSoal
        private Object jawaban;
        private Integer currentSoalIndex;

        public SaveJawabanRequest() {
            super();
        }

        public String getIdBankSoal() {
            return idBankSoal;
        }

        public void setIdBankSoal(String idBankSoal) {
            this.idBankSoal = idBankSoal;
        }

        public Object getJawaban() {
            return jawaban;
        }

        public void setJawaban(Object jawaban) {
            this.jawaban = jawaban;
        }

        public Integer getCurrentSoalIndex() {
            return currentSoalIndex;
        }

        public void setCurrentSoalIndex(Integer currentSoalIndex) {
            this.currentSoalIndex = currentSoalIndex;
        }
    }

    // Subclass untuk auto-save progress
    public static class AutoSaveProgressRequest extends UjianSessionRequest {
        private Map<String, Object> answers;
        private Integer currentSoalIndex;
        private Integer timeRemaining;

        public AutoSaveProgressRequest() {
            super();
        }

        public Map<String, Object> getAnswers() {
            return answers;
        }

        public void setAnswers(Map<String, Object> answers) {
            this.answers = answers;
        }

        public Integer getCurrentSoalIndex() {
            return currentSoalIndex;
        }

        public void setCurrentSoalIndex(Integer currentSoalIndex) {
            this.currentSoalIndex = currentSoalIndex;
        }

        public Integer getTimeRemaining() {
            return timeRemaining;
        }

        public void setTimeRemaining(Integer timeRemaining) {
            this.timeRemaining = timeRemaining;
        }
    }

    // Subclass untuk submit ujian
    public static class SubmitUjianRequest {
        private String sessionId;
        private String idUjian;
        private String idPeserta;
        private Map<String, Object> answers;
        private Boolean isAutoSubmit;
        private Integer finalTimeRemaining;

        // Constructors
        public SubmitUjianRequest() {
        }

        // Getters and Setters
        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getIdUjian() {
            return idUjian;
        }

        public void setIdUjian(String idUjian) {
            this.idUjian = idUjian;
        }

        public String getIdPeserta() {
            return idPeserta;
        }

        public void setIdPeserta(String idPeserta) {
            this.idPeserta = idPeserta;
        }

        public Map<String, Object> getAnswers() {
            return answers;
        }

        public void setAnswers(Map<String, Object> answers) {
            this.answers = answers;
        }

        public Boolean getIsAutoSubmit() {
            return isAutoSubmit;
        }

        public void setIsAutoSubmit(Boolean isAutoSubmit) {
            this.isAutoSubmit = isAutoSubmit;
        }

        public Integer getFinalTimeRemaining() {
            return finalTimeRemaining;
        }

        public void setFinalTimeRemaining(Integer finalTimeRemaining) {
            this.finalTimeRemaining = finalTimeRemaining;
        }
    }

    // Subclass for updating current soal index
    public static class UpdateCurrentSoalRequest extends UjianSessionRequest {
        private Integer currentSoalIndex;
        private Integer previousSoalIndex;
        private String navigationAction; // NEXT, PREVIOUS, JUMP

        public UpdateCurrentSoalRequest() {
            super();
        }

        public Integer getCurrentSoalIndex() {
            return currentSoalIndex;
        }

        public void setCurrentSoalIndex(Integer currentSoalIndex) {
            this.currentSoalIndex = currentSoalIndex;
        }

        public Integer getPreviousSoalIndex() {
            return previousSoalIndex;
        }

        public void setPreviousSoalIndex(Integer previousSoalIndex) {
            this.previousSoalIndex = previousSoalIndex;
        }

        public String getNavigationAction() {
            return navigationAction;
        }

        public void setNavigationAction(String navigationAction) {
            this.navigationAction = navigationAction;
        }
    }

    // Subclass for keep-alive requests
    public static class KeepAliveRequest extends UjianSessionRequest {
        private Integer currentTimeRemaining;
        private String clientStatus;
        private Map<String, Object> clientMetadata;

        public KeepAliveRequest() {
            super();
        }

        public Integer getCurrentTimeRemaining() {
            return currentTimeRemaining;
        }

        public void setCurrentTimeRemaining(Integer currentTimeRemaining) {
            this.currentTimeRemaining = currentTimeRemaining;
        }

        public String getClientStatus() {
            return clientStatus;
        }

        public void setClientStatus(String clientStatus) {
            this.clientStatus = clientStatus;
        }

        public Map<String, Object> getClientMetadata() {
            return clientMetadata;
        }

        public void setClientMetadata(Map<String, Object> clientMetadata) {
            this.clientMetadata = clientMetadata;
        }
    }
}
