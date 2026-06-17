# Fix Cascade Update untuk Opsi, Pasangan, dan Jawaban Benar

## Masalah

Ketika melakukan edit soal ujian, perubahan pada **opsi**, **pasangan**, dan **jawaban benar** tidak ikut berubah di bagian bank soal, padahal cascade update sudah diimplementasi.

## Analisis Root Cause

Masalah terjadi pada dua level:

### 1. Level Service (SoalUjianService.java)

Di method `updateSoalUjian()`, cascade update logic hanya memeriksa perubahan pada:

- namaUjian
- pertanyaan
- bobot
- jenisSoal
- toleransiTypo

**TIDAK** memeriksa perubahan pada:

- **opsi** (untuk soal PG dan MULTI)
- **pasangan** (untuk soal COCOK)
- **jawabanBenar**

### 2. Level Repository (BankSoalRepository.java)

Method `updateSoalUjianInfoBySoalUjianId()` hanya mengupdate field:

- namaUjian, pertanyaan, bobot, jenisSoal, toleransiTypo

**TIDAK** mengupdate:

- **opsi**, **pasangan**, **jawabanBenar**

## Solusi Implementasi

### 1. Modifikasi SoalUjianService.java

**Tambah pengecekan perubahan opsi, pasangan, dan jawabanBenar:**

```java
// Check opsi changes (for PG and MULTI questions)
if (!java.util.Objects.equals(existingSoal.getOpsi(), updatedSoal.getOpsi())) {
    needsCascadeUpdate = true;
}

// Check pasangan changes (for COCOK questions)
if (!java.util.Objects.equals(existingSoal.getPasangan(), updatedSoal.getPasangan())) {
    needsCascadeUpdate = true;
}

// Check jawaban benar changes
if (!java.util.Objects.equals(existingSoal.getJawabanBenar(), updatedSoal.getJawabanBenar())) {
    needsCascadeUpdate = true;
}
```

**Update method call untuk include parameter tambahan:**

```java
bankSoalRepository.updateSoalUjianInfoBySoalUjianId(
    soalUjianId,
    updatedSoal.getNamaUjian(),
    updatedSoal.getPertanyaan(),
    updatedSoal.getBobot(),
    updatedSoal.getJenisSoal(),
    updatedSoal.getToleransiTypo(),
    updatedSoal.getOpsi(),           // TAMBAHAN
    updatedSoal.getPasangan(),       // TAMBAHAN
    updatedSoal.getJawabanBenar()    // TAMBAHAN
);
```

### 2. Modifikasi BankSoalRepository.java

**Update method signature:**

```java
public void updateSoalUjianInfoBySoalUjianId(String soalUjianId, String namaUjian, String pertanyaan, String bobot,
        String jenisSoal, String toleransiTypo, Map<String, String> opsi,
        Map<String, String> pasangan, List<String> jawabanBenar) throws IOException
```

**Tambah logika update untuk opsi, pasangan, dan jawabanBenar:**

```java
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
        client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "soalUjian", "pasangan", pasanganJson);
    } catch (JsonProcessingException e) {
        System.err.println("Error serializing pasangan: " + e.getMessage());
    }
}

// Update jawabanBenar
if (jawabanBenar != null) {
    try {
        String jawabanJson = objectMapper.writeValueAsString(jawabanBenar);
        client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "main", "jawabanBenar", jawabanJson);
        client.insertRecord(tableBankSoal, bankSoal.getIdBankSoal(), "soalUjian", "jawabanBenar", jawabanJson);
    } catch (JsonProcessingException e) {
        System.err.println("Error serializing jawabanBenar: " + e.getMessage());
    }
}
```

## Format Penyimpanan Data

### Opsi (PG/MULTI)

```json
{
  "A": "Jakarta",
  "B": "Surabaya",
  "C": "Bandung",
  "D": "Medan"
}
```

### Pasangan (COCOK)

```json
{
  "1_kiri": "Indonesia",
  "2_kiri": "Malaysia",
  "a_kanan": "Jakarta",
  "b_kanan": "Kuala Lumpur"
}
```

### Jawaban Benar

```json
// PG: ["A"]
// MULTI: ["A", "C"]
// COCOK: ["Indonesia=Jakarta", "Malaysia=Kuala Lumpur"]
// ISIAN: ["Jakarta"]
```

## Jenis Soal yang Terpengaruh

1. **PG (Pilihan Ganda)**: Update `opsi` dan `jawabanBenar`
2. **MULTI (Multiple Choice)**: Update `opsi` dan `jawabanBenar`
3. **COCOK (Matching)**: Update `pasangan` dan `jawabanBenar`
4. **ISIAN (Fill in the blank)**: Update `jawabanBenar` dan `toleransiTypo`

## Testing

Dibuat file test `CascadeUpdateTest.java` untuk memvalidasi:

- Update soal PG dengan perubahan opsi dan jawaban
- Update soal COCOK dengan perubahan pasangan dan jawaban
- Update soal MULTI dengan perubahan opsi dan jawaban

## Impact

Setelah fix ini:
✅ Perubahan opsi pada soal PG/MULTI akan ter-cascade ke bank soal  
✅ Perubahan pasangan pada soal COCOK akan ter-cascade ke bank soal
✅ Perubahan jawaban benar akan ter-cascade ke bank soal
✅ Backward compatibility tetap terjaga
✅ Tidak ada breaking changes pada existing functionality
