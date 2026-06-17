package com.doyatama.university.model;

import java.util.List;
import java.util.Map;

public class BankSoalUjian {

    private String idBankSoal;
    private String pertanyaan;
    private String bobot;
    private String jenisSoal;
    private Map<String, String> opsi;
    private Map<String, String> pasangan;
    private String toleransiTypo;
    private List<String> jawabanBenar;

    /**
     * Default constructor diperlukan untuk proses deserialisasi oleh
     * Jackson/library JSON lainnya.
     */
    public BankSoalUjian() {
    }

    /**
     * Constructor untuk mempermudah proses mapping dari objek BankSoal yang
     * lengkap.
     * Ini adalah inti dari proses transformasi.
     * 
     * @param fullBankSoal Objek BankSoal asli yang diambil dari repository.
     */
    public BankSoalUjian(BankSoal fullBankSoal) {
        this.idBankSoal = fullBankSoal.getIdBankSoal();
        this.pertanyaan = fullBankSoal.getPertanyaan();
        this.bobot = fullBankSoal.getBobot();
        this.jenisSoal = fullBankSoal.getJenisSoal();
        this.opsi = fullBankSoal.getOpsi();
        this.pasangan = fullBankSoal.getPasangan();
        this.toleransiTypo = fullBankSoal.getToleransiTypo();
        this.jawabanBenar = fullBankSoal.getJawabanBenar();
    }

    // == Getters and Setters untuk semua field ==//

    public String getIdBankSoal() {
        return idBankSoal;
    }

    public void setIdBankSoal(String idBankSoal) {
        this.idBankSoal = idBankSoal;
    }

    public String getPertanyaan() {
        return pertanyaan;
    }

    public void setPertanyaan(String pertanyaan) {
        this.pertanyaan = pertanyaan;
    }

    public String getBobot() {
        return bobot;
    }

    public void setBobot(String bobot) {
        this.bobot = bobot;
    }

    public String getJenisSoal() {
        return jenisSoal;
    }

    public void setJenisSoal(String jenisSoal) {
        this.jenisSoal = jenisSoal;
    }

    public Map<String, String> getOpsi() {
        return opsi;
    }

    public void setOpsi(Map<String, String> opsi) {
        this.opsi = opsi;
    }

    public Map<String, String> getPasangan() {
        return pasangan;
    }

    public void setPasangan(Map<String, String> pasangan) {
        this.pasangan = pasangan;
    }

    public String getToleransiTypo() {
        return toleransiTypo;
    }

    public void setToleransiTypo(String toleransiTypo) {
        this.toleransiTypo = toleransiTypo;
    }

    public List<String> getJawabanBenar() {
        return jawabanBenar;
    }

    public void setJawabanBenar(List<String> jawabanBenar) {
        this.jawabanBenar = jawabanBenar;
    }
}