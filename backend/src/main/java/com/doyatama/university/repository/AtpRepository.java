package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.Atp;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

public class AtpRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "atp";

    public List<Atp> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableAtp = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idAtp", "idAtp");
        columnMapping.put("namaAtp", "namaAtp");
        columnMapping.put("jumlahJpl", "jumlahJpl");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("acp", "acp");
        columnMapping.put("school", "school");

        return client.showListTable(tableAtp.toString(), columnMapping, Atp.class, size);
    }

    public Atp save(Atp atp) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = atp.getIdAtp();
        TableName tableAtp = TableName.valueOf(tableName);
        // Map<String, String> columnMapping = new HashMap<>();

        client.insertRecord(tableAtp, rowKey, "main", "idAtp", atp.getIdAtp());
        client.insertRecord(tableAtp, rowKey, "main", "namaAtp", atp.getNamaAtp());
        client.insertRecord(tableAtp, rowKey, "main", "jumlahJpl", atp.getJumlahJpl());

        // Tahun Ajaran
        client.insertRecord(tableAtp, rowKey, "tahunAjaran", "idTahun", atp.getTahunAjaran().getIdTahun());
        client.insertRecord(tableAtp, rowKey, "tahunAjaran", "tahunAjaran",
                atp.getTahunAjaran().getTahunAjaran());
        // Semester
        client.insertRecord(tableAtp, rowKey, "semester", "idSemester", atp.getSemester().getIdSemester());
        client.insertRecord(tableAtp, rowKey, "semester", "namaSemester",
                atp.getSemester().getNamaSemester());
        // Kelas
        client.insertRecord(tableAtp, rowKey, "kelas", "idKelas", atp.getKelas().getIdKelas());
        client.insertRecord(tableAtp, rowKey, "kelas", "namaKelas", atp.getKelas().getNamaKelas());
        // Mapel
        client.insertRecord(tableAtp, rowKey, "mapel", "idMapel", atp.getMapel().getIdMapel());
        client.insertRecord(tableAtp, rowKey, "mapel", "name", atp.getMapel().getName());
        // Konsentrasi Keahlian
        client.insertRecord(tableAtp, rowKey, "konsentrasiKeahlianSekolah", "idKonsentrasiSekolah",
                atp.getKonsentrasiKeahlianSekolah().getIdKonsentrasiSekolah());
        client.insertRecord(tableAtp, rowKey, "konsentrasiKeahlianSekolah", "namaKonsentrasiSekolah",
                atp.getKonsentrasiKeahlianSekolah().getNamaKonsentrasiSekolah());
        // Elemen
        client.insertRecord(tableAtp, rowKey, "elemen", "idElemen", atp.getElemen().getIdElemen());
        client.insertRecord(tableAtp, rowKey, "elemen", "namaElemen", atp.getElemen().getNamaElemen());
        // Acp
        client.insertRecord(tableAtp, rowKey, "acp", "idAcp", atp.getAcp().getIdAcp());
        client.insertRecord(tableAtp, rowKey, "acp", "namaAcp", atp.getAcp().getNamaAcp());
        // School
        client.insertRecord(tableAtp, rowKey, "school", "idSchool", atp.getSchool().getIdSchool());
        client.insertRecord(tableAtp, rowKey, "school", "nameSchool", atp.getSchool().getNameSchool());

        return atp;
    }

    public Atp findAtpById(String atpId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableAtp = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idAtp", "idAtp");
        columnMapping.put("namaAtp", "namaAtp");
        columnMapping.put("jumlahJpl", "jumlahJpl");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("acp", "acp");
        columnMapping.put("school", "school");

        return client.showDataTable(tableAtp.toString(), columnMapping, atpId, Atp.class);
    }

    public Atp findById(String atpId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableAtp = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idAtp", "idAtp");
        columnMapping.put("namaAtp", "namaAtp");
        columnMapping.put("jumlahJpl", "jumlahJpl");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("acp", "acp");
        columnMapping.put("school", "school");

        return client.showDataTable(tableAtp.toString(), columnMapping, atpId, Atp.class);
    }

    public List<Atp> findAtpByMapel(String mapelId, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableAtp = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idAtp", "idAtp");
        columnMapping.put("namaAtp", "namaAtp");
        columnMapping.put("jumlahJpl", "jumlahJpl");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("acp", "acp");
        columnMapping.put("school", "school");

        List<Atp> atpList = client.getDataListByColumn(tableAtp.toString(), columnMapping, "mapel", "idMapel",
                mapelId, Atp.class, size);

        return atpList;
    }

    public List<Atp> findAtpByUser(String userId, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableAtp = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idAtp", "idAtp");
        columnMapping.put("namaAtp", "namaAtp");
        columnMapping.put("jumlahJpl", "jumlahJpl");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("acp", "acp");
        columnMapping.put("school", "school");

        List<Atp> atpList = client.getDataListByColumn(tableAtp.toString(), columnMapping, "user", "id",
                userId, Atp.class, size);

        return atpList;
    }

    public List<Atp> findAtpBySekolah(String schoolID, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableAtp = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("idAtp", "idAtp");
        columnMapping.put("namaAtp", "namaAtp");
        columnMapping.put("jumlahJpl", "jumlahJpl");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("acp", "acp");
        columnMapping.put("school", "school");

        List<Atp> atpList = client.getDataListByColumn(tableAtp.toString(), columnMapping, "school", "idSchool",
                schoolID, Atp.class, size);

        return atpList;
    }

    public Atp update(String atpId, Atp atp) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        String rowKey = atpId;
        TableName tableAtp = TableName.valueOf(tableName);
        // Map<String, String> columnMapping = new HashMap<>();

        if (atp.getNamaAtp() != null) {
            client.insertRecord(tableAtp, rowKey, "main", "namaAtp", atp.getNamaAtp());
        }

        if (atp.getJumlahJpl() != null) {
            client.insertRecord(tableAtp, rowKey, "main", "jumlahJpl", atp.getJumlahJpl());
        }

        // Tahun Ajaran
        if (atp.getTahunAjaran() != null) {
            client.insertRecord(tableAtp, atpId, "tahunAjaran", "idTahun", atp.getTahunAjaran().getIdTahun());
            client.insertRecord(tableAtp, atpId, "tahunAjaran", "tahunAjaran",
                    atp.getTahunAjaran().getTahunAjaran());
        }

        // Semester
        if (atp.getSemester() != null) {
            client.insertRecord(tableAtp, atpId, "semester", "idSemester", atp.getSemester().getIdSemester());
            client.insertRecord(tableAtp, atpId, "semester", "namaSemester",
                    atp.getSemester().getNamaSemester());
        }

        // Kelas
        if (atp.getKelas() != null) {
            client.insertRecord(tableAtp, atpId, "kelas", "idKelas", atp.getKelas().getIdKelas());
            client.insertRecord(tableAtp, atpId, "kelas", "namaKelas", atp.getKelas().getNamaKelas());
        }

        // Mapel
        if (atp.getMapel() != null) {
            client.insertRecord(tableAtp, atpId, "mapel", "idMapel", atp.getMapel().getIdMapel());
            client.insertRecord(tableAtp, atpId, "mapel", "name", atp.getMapel().getName());
        }

        // Konsentrasi Keahlian
        if (atp.getKonsentrasiKeahlianSekolah() != null) {
            client.insertRecord(tableAtp, atpId, "konsentrasiKeahlianSekolah", "idKonsentrasiSekolah",
                    atp.getKonsentrasiKeahlianSekolah().getIdKonsentrasiSekolah());
            client.insertRecord(tableAtp, atpId, "konsentrasiKeahlianSekolah", "namaKonsentrasiSekolah",
                    atp.getKonsentrasiKeahlianSekolah().getNamaKonsentrasiSekolah());
        }

        // Elemen
        if (atp.getElemen() != null) {
            client.insertRecord(tableAtp, atpId, "elemen", "idElemen", atp.getElemen().getIdElemen());
            client.insertRecord(tableAtp, atpId, "elemen", "namaElemen", atp.getElemen().getNamaElemen());
        }
        // Acp
        if (atp.getAcp() != null) {
            client.insertRecord(tableAtp, atpId, "acp", "idAcp", atp.getAcp().getIdAcp());
            client.insertRecord(tableAtp, atpId, "acp", "namaAcp", atp.getAcp().getNamaAcp());
        }

        // School
        if (atp.getSchool() != null) {
            client.insertRecord(tableAtp, atpId, "school", "idSchool", atp.getSchool().getIdSchool());
            client.insertRecord(tableAtp, atpId, "school", "nameSchool", atp.getSchool().getNameSchool());
        }

        client.insertRecord(tableAtp, atpId, "detail", "updated_by", "Polinema");

        return atp;
    }

    public boolean deleteById(String atpId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, atpId);
        return true;
    }

    public boolean existsById(String atpId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableAtp = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("idAtp", "idAtp");

        Atp atp = client.getDataByColumn(tableAtp.toString(), columnMapping,
                "main", "idAtp",
                atpId, Atp.class);

        return atp.getIdAtp() != null;
    }

    public List<Atp> findAtpByElemen(String elemenId, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableAtp = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("idAtp", "idAtp");
        columnMapping.put("namaAtp", "namaAtp");
        columnMapping.put("jumlahJpl", "jumlahJpl");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("acp", "acp");
        columnMapping.put("school", "school");

        List<Atp> atpList = client.getDataListByColumn(tableAtp.toString(), columnMapping, "elemen", "idElemen",
                elemenId, Atp.class, size);
        return atpList;
    }

    public void updateNamaElemenByElemenId(String elemenId, String newNamaElemen) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableAtp = TableName.valueOf(tableName);

        // Get all ATP records that reference this elemen
        List<Atp> atpList = findAtpByElemen(elemenId, 1000); // Use large size to get all records

        // Update nama elemen for each ATP record
        for (Atp atp : atpList) {
            if (atp.getIdAtp() != null) {
                client.insertRecord(tableAtp, atp.getIdAtp(), "elemen", "namaElemen", newNamaElemen);
                client.insertRecord(tableAtp, atp.getIdAtp(), "detail", "updated_by", "System_Cascade_Update");
            }
        }
    }

    public List<Atp> findAtpByAcp(String acpId, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableAtp = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("idAtp", "idAtp");
        columnMapping.put("namaAtp", "namaAtp");
        columnMapping.put("jumlahJpl", "jumlahJpl");
        columnMapping.put("mapel", "mapel");
        columnMapping.put("tahunAjaran", "tahunAjaran");
        columnMapping.put("semester", "semester");
        columnMapping.put("kelas", "kelas");
        columnMapping.put("konsentrasiKeahlianSekolah", "konsentrasiKeahlianSekolah");
        columnMapping.put("elemen", "elemen");
        columnMapping.put("acp", "acp");
        columnMapping.put("school", "school");

        List<Atp> atpList = client.getDataListByColumn(tableAtp.toString(), columnMapping, "acp", "idAcp",
                acpId, Atp.class, size);
        return atpList;
    }

    public void updateNamaAcpByAcpId(String acpId, String newNamaAcp) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableAtp = TableName.valueOf(tableName);

        // Get all ATP records that reference this ACP
        List<Atp> atpList = findAtpByAcp(acpId, 1000); // Use large size to get all records

        // Update nama ACP for each ATP record
        for (Atp atp : atpList) {
            if (atp.getIdAtp() != null) {
                client.insertRecord(tableAtp, atp.getIdAtp(), "acp", "namaAcp", newNamaAcp);
                client.insertRecord(tableAtp, atp.getIdAtp(), "detail", "updated_by", "System_Cascade_Update");
            }
        }
    }

}
