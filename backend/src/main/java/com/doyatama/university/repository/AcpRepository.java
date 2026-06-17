package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.Acp;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

public class AcpRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "acp";

    public List<Acp> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableAcp = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idAcp", "idAcp");
        columnMapping.put("namaAcp", "namaAcp");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("school", "school");

        return client.showListTable(tableAcp.toString(), columnMapping, Acp.class, size);
    }

    public Acp save(Acp acp) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = acp.getIdAcp();
        TableName tableAcp = TableName.valueOf(tableName);
        // Map<String, String> columnMapping = new HashMap<>();

        client.insertRecord(tableAcp, rowKey, "main", "idAcp", acp.getIdAcp());
        client.insertRecord(tableAcp, rowKey, "main", "namaAcp", acp.getNamaAcp());

        // Tahun Ajaran
        client.insertRecord(tableAcp, rowKey, "tahunAjaran", "idTahun", acp.getTahunAjaran().getIdTahun());
        client.insertRecord(tableAcp, rowKey, "tahunAjaran", "tahunAjaran",
                acp.getTahunAjaran().getTahunAjaran());
        // Semester
        client.insertRecord(tableAcp, rowKey, "semester", "idSemester", acp.getSemester().getIdSemester());
        client.insertRecord(tableAcp, rowKey, "semester", "namaSemester",
                acp.getSemester().getNamaSemester());
        // Kelas
        client.insertRecord(tableAcp, rowKey, "kelas", "idKelas", acp.getKelas().getIdKelas());
        client.insertRecord(tableAcp, rowKey, "kelas", "namaKelas", acp.getKelas().getNamaKelas());
        // Mapel
        client.insertRecord(tableAcp, rowKey, "mapel", "idMapel", acp.getMapel().getIdMapel());
        client.insertRecord(tableAcp, rowKey, "mapel", "name", acp.getMapel().getName());
        // Konsentrasi Keahlian
        client.insertRecord(tableAcp, rowKey, "konsentrasiKeahlianSekolah", "idKonsentrasiSekolah",
                acp.getKonsentrasiKeahlianSekolah().getIdKonsentrasiSekolah());
        client.insertRecord(tableAcp, rowKey, "konsentrasiKeahlianSekolah", "namaKonsentrasiSekolah",
                acp.getKonsentrasiKeahlianSekolah().getNamaKonsentrasiSekolah());
        // Elemen
        client.insertRecord(tableAcp, rowKey, "elemen", "idElemen", acp.getElemen().getIdElemen());
        client.insertRecord(tableAcp, rowKey, "elemen", "namaElemen", acp.getElemen().getNamaElemen());
        // School
        client.insertRecord(tableAcp, rowKey, "school", "idSchool", acp.getSchool().getIdSchool());
        client.insertRecord(tableAcp, rowKey, "school", "nameSchool", acp.getSchool().getNameSchool());

        return acp;

    }

    public Acp findAcpById(String acpId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableAcp = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("idAcp", "idAcp");
        columnMapping.put("namaAcp", "namaAcp");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("school", "school");

        return client.showDataTable(tableAcp.toString(), columnMapping, acpId, Acp.class);
    }

    public Acp findById(String acpId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableAcp = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("idAcp", "idAcp");
        columnMapping.put("namaAcp", "namaAcp");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("school", "school");

        return client.showDataTable(tableAcp.toString(), columnMapping, acpId, Acp.class);
    }

    public List<Acp> findAcpByMapel(String mapelId, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableAcp = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("idAcp", "idAcp");
        columnMapping.put("namaAcp", "namaAcp");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("school", "school");

        List<Acp> acpList = client.getDataListByColumn(tableAcp.toString(), columnMapping, "mapel", "idMapel",
                mapelId, Acp.class, size);
        return acpList;
    }

    public List<Acp> findAcpBySekolah(String schoolID, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableAcp = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("idAcp", "idAcp");
        columnMapping.put("namaAcp", "namaAcp");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("school", "school");

        List<Acp> acpList = client.getDataListByColumn(tableAcp.toString(), columnMapping, "school", "idSchool",
                schoolID, Acp.class, size);
        return acpList;
    }

    public Acp update(String acpId, Acp acp) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableAcp = TableName.valueOf(tableName);
        // Map<String, String> columnMapping = new HashMap<>();

        if (acp.getNamaAcp() != null) {
            client.insertRecord(tableAcp, acpId, "main", "namaAcp", acp.getNamaAcp());
        }

        // Tahun Ajaran
        if (acp.getTahunAjaran() != null) {
            client.insertRecord(tableAcp, acpId, "tahunAjaran", "idTahun", acp.getTahunAjaran().getIdTahun());
            client.insertRecord(tableAcp, acpId, "tahunAjaran", "tahunAjaran",
                    acp.getTahunAjaran().getTahunAjaran());
        }

        // Semester
        if (acp.getSemester() != null) {
            client.insertRecord(tableAcp, acpId, "semester", "idSemester", acp.getSemester().getIdSemester());
            client.insertRecord(tableAcp, acpId, "semester", "namaSemester",
                    acp.getSemester().getNamaSemester());
        }

        // Kelas
        if (acp.getKelas() != null) {
            client.insertRecord(tableAcp, acpId, "kelas", "idKelas", acp.getKelas().getIdKelas());
            client.insertRecord(tableAcp, acpId, "kelas", "namaKelas", acp.getKelas().getNamaKelas());
        }

        // Mapel
        if (acp.getMapel() != null) {
            client.insertRecord(tableAcp, acpId, "mapel", "idMapel", acp.getMapel().getIdMapel());
            client.insertRecord(tableAcp, acpId, "mapel", "name", acp.getMapel().getName());
        }

        // Konsentrasi Keahlian
        if (acp.getKonsentrasiKeahlianSekolah() != null) {
            client.insertRecord(tableAcp, acpId, "konsentrasiKeahlianSekolah", "idKonsentrasiSekolah",
                    acp.getKonsentrasiKeahlianSekolah().getIdKonsentrasiSekolah());
            client.insertRecord(tableAcp, acpId, "konsentrasiKeahlianSekolah", "namaKonsentrasiSekolah",
                    acp.getKonsentrasiKeahlianSekolah().getNamaKonsentrasiSekolah());
        }

        // Elemen
        if (acp.getElemen() != null) {
            client.insertRecord(tableAcp, acpId, "elemen", "idElemen", acp.getElemen().getIdElemen());
            client.insertRecord(tableAcp, acpId, "elemen", "namaElemen", acp.getElemen().getNamaElemen());
        }

        // School
        if (acp.getSchool() != null) {
            client.insertRecord(tableAcp, acpId, "school", "idSchool", acp.getSchool().getIdSchool());
            client.insertRecord(tableAcp, acpId, "school", "nameSchool", acp.getSchool().getNameSchool());
        }

        client.insertRecord(tableAcp, acpId, "detail", "updated_by", "Polinema");

        return acp;
    }

    public boolean deleteById(String acpId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, acpId);
        return true;
    }

    public boolean existsById(String acpId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableAcp = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idAcp", "idAcp");

        Acp acp = client.getDataByColumn(tableAcp.toString(), columnMapping, "main", "idAcp", acpId, Acp.class);
        return acp.getIdAcp() != null;
    }

    public List<Acp> findAcpByElemen(String elemenId, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableAcp = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("idAcp", "idAcp");
        columnMapping.put("namaAcp", "namaAcp");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("school", "school");

        List<Acp> acpList = client.getDataListByColumn(tableAcp.toString(), columnMapping, "elemen", "idElemen",
                elemenId, Acp.class, size);
        return acpList;
    }

    public void updateNamaElemenByElemenId(String elemenId, String newNamaElemen) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableAcp = TableName.valueOf(tableName);

        // Get all ACP records that reference this elemen
        List<Acp> acpList = findAcpByElemen(elemenId, 1000); // Use large size to get all records

        // Update nama elemen for each ACP record
        for (Acp acp : acpList) {
            if (acp.getIdAcp() != null) {
                client.insertRecord(tableAcp, acp.getIdAcp(), "elemen", "namaElemen", newNamaElemen);
                client.insertRecord(tableAcp, acp.getIdAcp(), "detail", "updated_by", "System_Cascade_Update");
            }
        }
    }
}
