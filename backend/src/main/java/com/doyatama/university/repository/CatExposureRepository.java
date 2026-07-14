package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;

/**
 * Repository untuk menyimpan dan membaca data Sympson-Hetter Exposure Control
 * secara persisten ke HBase.
 *
 * <p>Desain Tabel HBase: {@code cat_exposure}
 * <pre>
 * Row Key : idBankSoal   (contoh: "soal-001", "BSL202412001")
 * Column Family "stats":
 *   - exposureCount  : jumlah kali soal benar-benar diadministrasikan
 *   - selectionCount : jumlah kali soal terpilih oleh MFI (sebelum gating)
 *   - updatedAt      : timestamp update terakhir
 * </pre>
 *
 * <p>Menggunakan operasi {@code Increment} HBase agar penambahan counter
 * bersifat atomic — aman jika ada request bersamaan (race condition-free).
 */
@Repository
public class CatExposureRepository {

    private static final Logger logger = LoggerFactory.getLogger(CatExposureRepository.class);

    private static final String TABLE_NAME      = "cat_exposure";
    private static final String CF_STATS        = "stats";
    private static final String COL_EXPOSURE    = "exposureCount";
    private static final String COL_SELECTION   = "selectionCount";
    private static final String COL_UPDATED_AT  = "updatedAt";

    Configuration conf = HBaseConfiguration.create();

    // =========================================================================
    // WRITE: Tambah counter — gunakan HBase Increment (atomic)
    // =========================================================================

    /**
     * Tambah {@code exposureCount} item sebesar 1 secara atomic.
     * Dipanggil setiap kali soal lolos gating Sympson-Hetter dan
     * benar-benar diadministrasikan kepada peserta.
     *
     * @param idBankSoal ID soal yang diadministrasikan
     */
    public void incrementExposureCount(String idBankSoal) {
        if (idBankSoal == null || idBankSoal.isEmpty()) return;
        try (Connection connection = ConnectionFactory.createConnection(conf);
             Table table = connection.getTable(TableName.valueOf(TABLE_NAME))) {

            Increment increment = new Increment(Bytes.toBytes(idBankSoal));
            increment.addColumn(
                    Bytes.toBytes(CF_STATS),
                    Bytes.toBytes(COL_EXPOSURE),
                    1L   // tambah 1
            );
            table.increment(increment);

            // Update timestamp (best-effort, bukan atomic)
            HBaseCustomClient client = new HBaseCustomClient(conf);
            client.insertRecord(TableName.valueOf(TABLE_NAME), idBankSoal,
                    CF_STATS, COL_UPDATED_AT,
                    java.time.Instant.now().toString());

            logger.debug("[SH] incrementExposure soal={}", idBankSoal);

        } catch (IOException e) {
            // Log saja, jangan lempar exception agar ujian tidak terganggu
            logger.warn("[SH] Gagal increment exposureCount soal={}: {}", idBankSoal, e.getMessage());
        }
    }

    /**
     * Tambah {@code selectionCount} item sebesar 1 secara atomic.
     * Dipanggil setiap kali soal masuk daftar kandidat MFI (sebelum gating).
     *
     * @param idBankSoal ID soal kandidat
     */
    public void incrementSelectionCount(String idBankSoal) {
        if (idBankSoal == null || idBankSoal.isEmpty()) return;
        try (Connection connection = ConnectionFactory.createConnection(conf);
             Table table = connection.getTable(TableName.valueOf(TABLE_NAME))) {

            Increment increment = new Increment(Bytes.toBytes(idBankSoal));
            increment.addColumn(
                    Bytes.toBytes(CF_STATS),
                    Bytes.toBytes(COL_SELECTION),
                    1L
            );
            table.increment(increment);

            logger.debug("[SH] incrementSelection soal={}", idBankSoal);

        } catch (IOException e) {
            logger.warn("[SH] Gagal increment selectionCount soal={}: {}", idBankSoal, e.getMessage());
        }
    }

    // =========================================================================
    // READ: Baca counter dari HBase
    // =========================================================================

    /**
     * Baca nilai {@code exposureCount} dari HBase.
     *
     * @param idBankSoal ID soal
     * @return jumlah exposure, atau 0 jika belum ada data
     */
    public int getExposureCount(String idBankSoal) {
        if (idBankSoal == null || idBankSoal.isEmpty()) return 0;
        try (Connection connection = ConnectionFactory.createConnection(conf);
             Table table = connection.getTable(TableName.valueOf(TABLE_NAME))) {

            Get get = new Get(Bytes.toBytes(idBankSoal));
            get.addColumn(Bytes.toBytes(CF_STATS), Bytes.toBytes(COL_EXPOSURE));
            Result result = table.get(get);

            if (result == null || result.isEmpty()) return 0;

            byte[] value = result.getValue(
                    Bytes.toBytes(CF_STATS),
                    Bytes.toBytes(COL_EXPOSURE)
            );
            return value != null ? (int) Bytes.toLong(value) : 0;

        } catch (IOException e) {
            logger.warn("[SH] Gagal baca exposureCount soal={}: {}", idBankSoal, e.getMessage());
            return 0;
        }
    }

    /**
     * Baca nilai {@code selectionCount} dari HBase.
     *
     * @param idBankSoal ID soal
     * @return jumlah selection, atau 0 jika belum ada data
     */
    public int getSelectionCount(String idBankSoal) {
        if (idBankSoal == null || idBankSoal.isEmpty()) return 0;
        try (Connection connection = ConnectionFactory.createConnection(conf);
             Table table = connection.getTable(TableName.valueOf(TABLE_NAME))) {

            Get get = new Get(Bytes.toBytes(idBankSoal));
            get.addColumn(Bytes.toBytes(CF_STATS), Bytes.toBytes(COL_SELECTION));
            Result result = table.get(get);

            if (result == null || result.isEmpty()) return 0;

            byte[] value = result.getValue(
                    Bytes.toBytes(CF_STATS),
                    Bytes.toBytes(COL_SELECTION)
            );
            return value != null ? (int) Bytes.toLong(value) : 0;

        } catch (IOException e) {
            logger.warn("[SH] Gagal baca selectionCount soal={}: {}", idBankSoal, e.getMessage());
            return 0;
        }
    }

    // =========================================================================
    // RESET (untuk keperluan admin / testing)
    // =========================================================================

    /**
     * Reset seluruh counter satu soal ke nol.
     * Gunakan ini jika soal direvisi dan ingin mulai pengumpulan data ulang.
     *
     * @param idBankSoal ID soal yang akan di-reset
     */
    public void resetCounters(String idBankSoal) {
        if (idBankSoal == null || idBankSoal.isEmpty()) return;
        try {
            HBaseCustomClient client = new HBaseCustomClient(conf);
            TableName table = TableName.valueOf(TABLE_NAME);
            client.insertRecord(table, idBankSoal, CF_STATS, COL_EXPOSURE, "0");
            client.insertRecord(table, idBankSoal, CF_STATS, COL_SELECTION, "0");
            client.insertRecord(table, idBankSoal, CF_STATS, COL_UPDATED_AT,
                    java.time.Instant.now().toString());
            logger.info("[SH] Reset counter soal={}", idBankSoal);
        } catch (Exception e) {
            logger.warn("[SH] Gagal reset counter soal={}: {}", idBankSoal, e.getMessage());
        }
    }
}
