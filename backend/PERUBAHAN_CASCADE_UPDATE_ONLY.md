# Perubahan Cascade SoalUjian - UPDATE ONLY

## Perubahan yang Dibuat

### 1. SoalUjianService.java

**Method `createSoalUjian()`:**

- ✅ SEBELUM: `return soalUjianRepository.saveWithCascade(soal);`
- ✅ SEKARANG: `return soalUjianRepository.save(soal);`
- 📝 **Hasil**: Create SoalUjian TIDAK akan membuat BankSoal otomatis

**Method `updateSoalUjian()`:**

- ✅ TETAP: `return soalUjianRepository.updateWithCascade(soalUjianId, updatedSoal);`
- 📝 **Hasil**: Update SoalUjian AKAN mengupdate semua BankSoal yang terkait

**Method `deleteSoalUjianById()`:**

- ✅ SEBELUM: `soalUjianRepository.deleteWithCascade(soalUjianId);`
- ✅ SEKARANG: `soalUjianRepository.deleteById(soalUjianId);`
- 📝 **Hasil**: Delete SoalUjian TIDAK akan menghapus BankSoal otomatis

### 2. Dokumentasi

Updated `SOAL_UJIAN_CASCADE_FIX_SUMMARY.md` untuk mencerminkan bahwa cascade hanya berlaku untuk UPDATE operations.

## Ringkasan Fungsionalitas

| Operasi    | Cascade ke BankSoal | Keterangan                            |
| ---------- | ------------------- | ------------------------------------- |
| **CREATE** | ❌ TIDAK            | SoalUjian dibuat secara independen    |
| **UPDATE** | ✅ YA               | Semua BankSoal terkait ikut terupdate |
| **DELETE** | ❌ TIDAK            | SoalUjian dihapus secara independen   |

## Keuntungan Implementasi Ini

1. **Fleksibilitas**: Create dan Delete operation tidak memiliki side effect
2. **Konsistensi Update**: Ketika SoalUjian diupdate, semua BankSoal yang terkait otomatis sinkron
3. **Kontrol Manual**: Developer memiliki kontrol penuh untuk create dan delete BankSoal secara terpisah
4. **Performa**: Tidak ada overhead cascade saat create/delete yang mungkin tidak diperlukan

## Cara Kerja

### Create SoalUjian

```java
SoalUjian soal = soalUjianService.createSoalUjian(request);
// Hanya membuat SoalUjian, tidak membuat BankSoal
```

### Update SoalUjian

```java
SoalUjian updated = soalUjianService.updateSoalUjian(id, request);
// Update SoalUjian DAN semua BankSoal yang referensi ke SoalUjian ini
```

### Delete SoalUjian

```java
soalUjianService.deleteSoalUjianById(id);
// Hanya hapus SoalUjian, BankSoal tetap ada (jika ada)
```

## Catatan Penting

- Jika Anda ingin membuat BankSoal dari SoalUjian, lakukan secara manual melalui BankSoalService
- Jika Anda ingin menghapus BankSoal saat menghapus SoalUjian, lakukan secara manual
- Update operation akan selalu sinkron otomatis untuk menjaga konsistensi data
