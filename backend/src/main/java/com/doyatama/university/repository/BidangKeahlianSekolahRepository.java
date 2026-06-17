package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.BidangKeahlianSekolah;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

public class BidangKeahlianSekolahRepository {

        Configuration conf = HBaseConfiguration.create();
        String tableName = "bidangKeahlianSekolah";

        public List<BidangKeahlianSekolah> findAll(int size) throws IOException {
                HBaseCustomClient client = new HBaseCustomClient(conf);

                TableName tableBidangKeahlianSekolah = TableName.valueOf(tableName);
                Map<String, String> columnMapping = new HashMap<>();

                // Add the mappings to the HashMap
                columnMapping.put("idBidangSekolah", "idBidangSekolah");
                columnMapping.put("namaBidangSekolah", "namaBidangSekolah");
                columnMapping.put("school", "school");
                columnMapping.put("bidangKeahlian", "bidangKeahlian");

                return client.showListTable(tableBidangKeahlianSekolah.toString(), columnMapping,
                                BidangKeahlianSekolah.class,
                                size);
        }

        public BidangKeahlianSekolah save(BidangKeahlianSekolah bidangKeahlianSekolah) throws IOException {
                HBaseCustomClient client = new HBaseCustomClient(conf);

                String rowKey = bidangKeahlianSekolah.getIdBidangSekolah();
                TableName tableBidangKeahlianSekolah = TableName.valueOf(tableName);
                // Map<String, String> columnMapping = new HashMap<>();

                client.insertRecord(tableBidangKeahlianSekolah, rowKey, "main", "idBidangSekolah",
                                bidangKeahlianSekolah.getIdBidangSekolah());
                client.insertRecord(tableBidangKeahlianSekolah, rowKey, "main", "namaBidangSekolah",
                                bidangKeahlianSekolah.getNamaBidangSekolah());

                // Sekolah
                client.insertRecord(tableBidangKeahlianSekolah, rowKey, "school", "idSchool",
                                bidangKeahlianSekolah.getSchool().getIdSchool());
                client.insertRecord(tableBidangKeahlianSekolah, rowKey, "school", "nameSchool",
                                bidangKeahlianSekolah.getSchool().getNameSchool());

                // Bidang Keahlian
                client.insertRecord(tableBidangKeahlianSekolah, rowKey, "bidangKeahlian", "id",
                                bidangKeahlianSekolah.getBidangKeahlian().getId());
                client.insertRecord(tableBidangKeahlianSekolah, rowKey, "bidangKeahlian", "bidang",
                                bidangKeahlianSekolah.getBidangKeahlian().getBidang());

                return bidangKeahlianSekolah;
        }

        public BidangKeahlianSekolah findBidangKeahlianSekolahById(String bidangKeahlianSekolahId) throws IOException {
                HBaseCustomClient client = new HBaseCustomClient(conf);

                TableName tableBidangKeahlianSekolah = TableName.valueOf(tableName);
                Map<String, String> columnMapping = new HashMap<>();

                // Add the mappings to the HashMap
                columnMapping.put("idBidangSekolah", "idBidangSekolah");
                columnMapping.put("namaBidangSekolah", "namaBidangSekolah");
                columnMapping.put("school", "school");
                columnMapping.put("bidangKeahlian", "bidangKeahlian");

                return client.showDataTable(tableBidangKeahlianSekolah.toString(), columnMapping,
                                bidangKeahlianSekolahId, BidangKeahlianSekolah.class);
        }

        public BidangKeahlianSekolah findById(String bidangKeahlianSekolahId) throws IOException {
                HBaseCustomClient client = new HBaseCustomClient(conf);

                TableName tableBidangKeahlianSekolah = TableName.valueOf(tableName);
                Map<String, String> columnMapping = new HashMap<>();

                // Add the mappings to the HashMap
                columnMapping.put("idBidangSekolah", "idBidangSekolah");
                columnMapping.put("namaBidangSekolah", "namaBidangSekolah");
                columnMapping.put("school", "school");
                columnMapping.put("bidangKeahlian", "bidangKeahlian");

                return client.showDataTable(tableBidangKeahlianSekolah.toString(), columnMapping,
                                bidangKeahlianSekolahId, BidangKeahlianSekolah.class);
        }

        public List<BidangKeahlianSekolah> findBidangKeahlianSekolahBySekolah(String schoolID, int size)
                        throws IOException {
                HBaseCustomClient client = new HBaseCustomClient(conf);

                TableName tableBidangKeahlianSekolah = TableName.valueOf(tableName);
                Map<String, String> columnMapping = new HashMap<>();

                // Add the mappings to the HashMap
                columnMapping.put("idBidangSekolah", "idBidangSekolah");
                columnMapping.put("namaBidangSekolah", "namaBidangSekolah");
                columnMapping.put("school", "school");
                columnMapping.put("bidangKeahlian", "bidangKeahlian");

                List<BidangKeahlianSekolah> bidangKeahlianSekolah = client.getDataListByColumn(
                                tableBidangKeahlianSekolah.toString(),
                                columnMapping, "school", "idSchool", schoolID, BidangKeahlianSekolah.class, size);

                return bidangKeahlianSekolah;
        }

        public BidangKeahlianSekolah update(String bidangKeahlianSekolahId, BidangKeahlianSekolah bidangKeahlianSekolah)
                        throws IOException {
                HBaseCustomClient client = new HBaseCustomClient(conf);

                TableName tableBidangKeahlianSekolah = TableName.valueOf(tableName);
                // Map<String, String> columnMapping = new HashMap<>();

                if (bidangKeahlianSekolah.getNamaBidangSekolah() != null) {
                        client.insertRecord(tableBidangKeahlianSekolah, bidangKeahlianSekolahId, "main",
                                        "namaBidangSekolah",
                                        bidangKeahlianSekolah.getNamaBidangSekolah());
                }

                // Sekolah
                if (bidangKeahlianSekolah.getSchool().getIdSchool() != null) {
                        client.insertRecord(tableBidangKeahlianSekolah, bidangKeahlianSekolahId, "school", "idSchool",
                                        bidangKeahlianSekolah.getSchool().getIdSchool());
                }
                if (bidangKeahlianSekolah.getSchool().getNameSchool() != null) {
                        client.insertRecord(tableBidangKeahlianSekolah, bidangKeahlianSekolahId, "school", "nameSchool",
                                        bidangKeahlianSekolah.getSchool().getNameSchool());
                }

                // Bidang Keahlian
                if (bidangKeahlianSekolah.getBidangKeahlian().getId() != null) {
                        client.insertRecord(tableBidangKeahlianSekolah, bidangKeahlianSekolahId, "bidangKeahlian", "id",
                                        bidangKeahlianSekolah.getBidangKeahlian().getId());
                }
                if (bidangKeahlianSekolah.getBidangKeahlian().getBidang() != null) {
                        client.insertRecord(tableBidangKeahlianSekolah, bidangKeahlianSekolahId, "bidangKeahlian",
                                        "bidang",
                                        bidangKeahlianSekolah.getBidangKeahlian().getBidang());
                }

                return bidangKeahlianSekolah;
        }

        public boolean deleteById(String bidangKeahlianSekolahId) throws IOException {
                HBaseCustomClient client = new HBaseCustomClient(conf);
                client.deleteRecord(tableName, bidangKeahlianSekolahId);
                return true;
        }

        public boolean existsById(String bidangKeahlianSekolahId) throws IOException {
                HBaseCustomClient client = new HBaseCustomClient(conf);
                TableName tableBidangKeahlianSekolah = TableName.valueOf(tableName);
                Map<String, String> columnMapping = new HashMap<>();

                columnMapping.put("idBidangSekolah", "idBidangSekolah");

                BidangKeahlianSekolah bidangKeahlianSekolah = client.getDataByColumn(
                                tableBidangKeahlianSekolah.toString(),
                                columnMapping, "main", "idBidangSekolah", bidangKeahlianSekolahId,
                                BidangKeahlianSekolah.class);

                return bidangKeahlianSekolah.getIdBidangSekolah() != null;
        }
}
