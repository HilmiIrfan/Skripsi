package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.ProgramKeahlianSekolah;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

public class ProgramKeahlianSekolahRepository {

        Configuration conf = HBaseConfiguration.create();
        String tableName = "programKeahlianSekolah";

        public List<ProgramKeahlianSekolah> findAll(int size) throws IOException {
                HBaseCustomClient client = new HBaseCustomClient(conf);

                TableName tableProgramKeahlianSekolah = TableName.valueOf(tableName);
                Map<String, String> columnMapping = new HashMap<>();

                // Add the mappings to the HashMap
                columnMapping.put("idProgramSekolah", "idProgramSekolah");
                columnMapping.put("namaProgramSekolah", "namaProgramSekolah");
                columnMapping.put("school", "school");
                columnMapping.put("programKeahlian", "programKeahlian");

                return client.showListTable(tableProgramKeahlianSekolah.toString(), columnMapping,
                                ProgramKeahlianSekolah.class,
                                size);
        }

        public ProgramKeahlianSekolah save(ProgramKeahlianSekolah programKeahlianSekolah) throws IOException {
                HBaseCustomClient client = new HBaseCustomClient(conf);

                String rowKey = programKeahlianSekolah.getIdProgramSekolah();
                TableName tableProgramKeahlianSekolah = TableName.valueOf(tableName);
                // Map<String, String> columnMapping = new HashMap<>();

                client.insertRecord(tableProgramKeahlianSekolah, rowKey, "main", "idProgramSekolah",
                                programKeahlianSekolah.getIdProgramSekolah());
                client.insertRecord(tableProgramKeahlianSekolah, rowKey, "main", "namaProgramSekolah",
                                programKeahlianSekolah.getNamaProgramSekolah());

                // Sekolah
                client.insertRecord(tableProgramKeahlianSekolah, rowKey, "school", "idSchool",
                                programKeahlianSekolah.getSchool().getIdSchool());
                client.insertRecord(tableProgramKeahlianSekolah, rowKey, "school", "nameSchool",
                                programKeahlianSekolah.getSchool().getNameSchool());

                // Program Keahlian
                client.insertRecord(tableProgramKeahlianSekolah, rowKey, "programKeahlian", "id",
                                programKeahlianSekolah.getProgramKeahlian().getId());
                client.insertRecord(tableProgramKeahlianSekolah, rowKey, "programKeahlian", "program",
                                programKeahlianSekolah.getProgramKeahlian().getProgram());

                return programKeahlianSekolah;
        }

        public ProgramKeahlianSekolah findProgramKeahlianSekolahById(String programKeahlianSekolahId)
                        throws IOException {
                HBaseCustomClient client = new HBaseCustomClient(conf);
                TableName tableProgramKeahlianSekolah = TableName.valueOf(tableName);
                Map<String, String> columnMapping = new HashMap<>();

                // Add the mappings to the HashMap
                columnMapping.put("idProgramSekolah", "idProgramSekolah");
                columnMapping.put("namaProgramSekolah", "namaProgramSekolah");
                columnMapping.put("school", "school");
                columnMapping.put("programKeahlian", "programKeahlian");

                return client.showDataTable(tableProgramKeahlianSekolah.toString(), columnMapping,
                                programKeahlianSekolahId,
                                ProgramKeahlianSekolah.class);
        }

        public ProgramKeahlianSekolah findById(String programKeahlianSekolahId) throws IOException {
                HBaseCustomClient client = new HBaseCustomClient(conf);
                TableName tableProgramKeahlianSekolah = TableName.valueOf(tableName);
                Map<String, String> columnMapping = new HashMap<>();

                // Add the mappings to the HashMap
                columnMapping.put("idProgramSekolah", "idProgramSekolah");
                columnMapping.put("namaProgramSekolah", "namaProgramSekolah");
                columnMapping.put("school", "school");
                columnMapping.put("programKeahlian", "programKeahlian");

                return client.showDataTable(tableProgramKeahlianSekolah.toString(), columnMapping,
                                programKeahlianSekolahId,
                                ProgramKeahlianSekolah.class);
        }

        public List<ProgramKeahlianSekolah> findProgramKeahlianSekolahBySekolah(String schoolID, int size)
                        throws IOException {
                HBaseCustomClient client = new HBaseCustomClient(conf);
                TableName tableProgramKeahlianSekolah = TableName.valueOf(tableName);
                Map<String, String> columnMapping = new HashMap<>();

                // Add the mappings to the HashMap
                columnMapping.put("idProgramSekolah", "idProgramSekolah");
                columnMapping.put("namaProgramSekolah", "namaProgramSekolah");
                columnMapping.put("school", "school");
                columnMapping.put("programKeahlian", "programKeahlian");

                List<ProgramKeahlianSekolah> programKeahlianSekolah = client.getDataListByColumn(
                                tableProgramKeahlianSekolah.toString(), columnMapping, "school", "idSchool", schoolID,
                                ProgramKeahlianSekolah.class, size);
                return programKeahlianSekolah;
        }

        public ProgramKeahlianSekolah update(String programKeahlianSekolahId,
                        ProgramKeahlianSekolah programKeahlianSekolah)
                        throws IOException {
                HBaseCustomClient client = new HBaseCustomClient(conf);
                TableName tableProgramKeahlianSekolah = TableName.valueOf(tableName);

                if (programKeahlianSekolah.getNamaProgramSekolah() != null) {
                        client.insertRecord(tableProgramKeahlianSekolah, programKeahlianSekolahId, "main",
                                        "namaProgramSekolah",
                                        programKeahlianSekolah.getNamaProgramSekolah());
                }

                // Sekolah
                if (programKeahlianSekolah.getSchool() != null) {
                        client.insertRecord(tableProgramKeahlianSekolah, programKeahlianSekolahId, "school", "idSchool",
                                        programKeahlianSekolah.getSchool().getIdSchool());
                        client.insertRecord(tableProgramKeahlianSekolah, programKeahlianSekolahId, "school",
                                        "nameSchool",
                                        programKeahlianSekolah.getSchool().getNameSchool());
                }

                if (programKeahlianSekolah.getProgramKeahlian() != null) {
                        client.insertRecord(tableProgramKeahlianSekolah, programKeahlianSekolahId, "programKeahlian",
                                        "id",
                                        programKeahlianSekolah.getProgramKeahlian().getId());
                        client.insertRecord(tableProgramKeahlianSekolah, programKeahlianSekolahId, "programKeahlian",
                                        "program",
                                        programKeahlianSekolah.getProgramKeahlian().getProgram());
                }

                return programKeahlianSekolah;
        }

        public boolean deleteById(String programKeahlianSekolahId) throws IOException {
                HBaseCustomClient client = new HBaseCustomClient(conf);
                client.deleteRecord(tableName, programKeahlianSekolahId);
                return true;
        }

        public boolean existsById(String programKeahlianSekolahId) throws IOException {
                HBaseCustomClient client = new HBaseCustomClient(conf);
                TableName tableProgramKeahlianSekolah = TableName.valueOf(tableName);

                Map<String, String> columnMapping = new HashMap<>();

                columnMapping.put("idProgramSekolah", "idProgramSekolah");

                ProgramKeahlianSekolah programKeahlianSekolah = client.getDataByColumn(
                                tableProgramKeahlianSekolah.toString(),
                                columnMapping, "main", "idProgramSekolah", programKeahlianSekolahId,
                                ProgramKeahlianSekolah.class);

                return programKeahlianSekolah.getIdProgramSekolah() != null;
        }

}
