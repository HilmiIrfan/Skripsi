# Import Soal Bank Soal - Panduan Penggunaan

## Overview

Fitur import soal bank soal memungkinkan pengguna untuk mengimpor soal secara massal menggunakan file Excel. Sistem mendukung 4 jenis soal yang berbeda:

1. **Pilihan Ganda (PG)**
2. **Multi Jawaban (MULTI)**
3. **Mencocokkan (COCOK)**
4. **Isian (ISIAN)**

## Cara Menggunakan

### 1. Buka Modal Import

- Klik tombol "Import Soal" di halaman Bank Soal
- Modal import akan terbuka

### 2. Pilih Jenis Soal

- Pilih jenis soal dari dropdown:
  - Pilihan Ganda (PG)
  - Multi Jawaban (MULTI)
  - Mencocokkan (COCOK)
  - Isian (ISIAN)

### 3. Upload File CSV/Excel

- Klik "Pilih File" untuk memilih file CSV (.csv) atau Excel (.xlsx, .xls)
- **Disarankan menggunakan format CSV** untuk menghindari masalah kompatibilitas
- File yang dipilih akan ditampilkan beserta jenis soal yang dipilih

### 4. Download Template (Disarankan)

- Download template CSV sesuai jenis soal yang dipilih
- Template dalam format CSV untuk menghindari file corrupt
- Template tersedia untuk setiap jenis soal: PG, MULTI, COCOK, ISIAN

### 5. Import

- Klik tombol "Upload" untuk memulai proses import
- **Alur Import 2 Tahap:**
  1. **Tahap 1**: Data disimpan ke **Soal Ujian** terlebih dahulu
  2. **Tahap 2**: Kemudian otomatis dikaitkan ke **Bank Soal** berdasarkan nama ujian
- **Multiple Soal per Nama Ujian:**
  - Setiap baris CSV = 1 Soal Ujian + 1 Bank Soal
  - Nama ujian yang sama bisa punya banyak soal (contoh: 20 soal "UTS Matematika")
  - Setiap soal dikaitkan individual ke Bank Soal
- **Keuntungan alur ini:**
  - Konsisten dengan alur manual (Soal Ujian → Bank Soal)
  - Data terstruktur dengan baik di kedua tabel
  - Mudah untuk pengelolaan dan maintain data
  - Mendukung multiple soal per ujian
- Pesan sukses/error akan ditampilkan untuk setiap tahap

## Format File CSV/Excel

### Kolom Wajib (Semua Jenis Soal)

- `namaUjian` - Nama ujian
- `pertanyaan` - Teks pertanyaan
- `bobot` - Bobot soal (angka)
- `jawabanBenar` - Jawaban yang benar
- `tahunAjaran` - Tahun Ajaran (contoh: "2025/2026")
- `namaSemester` - Nama Semester ("Ganjil" atau "Genap")
- `namaKelas` - Nama Kelas ("X", "XI", atau "XII")
- `namaMapel` - Nama Mata Pelajaran (sesuai dengan data di sistem)
- `namaElemen` - Nama Elemen (sesuai dengan data di sistem)
- `namaAcp` - Nama ACP (sesuai dengan data di sistem)
- `namaAtp` - Nama ATP (sesuai dengan data di sistem)
- `namaKonsentrasiSekolah` - Nama Konsentrasi Keahlian Sekolah (sesuai dengan data di sistem)
- `idSchool` - ID Sekolah (wajib diisi dengan "RWK001")
- `namaTaksonomi` - Nama Taksonomi (sesuai dengan data di sistem)

### Pencocokan Data (Fuzzy Matching)

Sistem menggunakan **fuzzy matching** untuk mencocokkan nama dengan data di database:

- **Mengabaikan spasi**: "Bahasa Indonesia" = "BahasaIndonesia"
- **Mengabaikan karakter khusus**: "Desain-Komunikasi" = "DesainKomunikasi"
- **Tidak case-sensitive**: "MATEMATIKA" = "matematika" = "Matematika"
- **Mengabaikan tanda baca**: "Bahasa, Indonesia" = "Bahasa Indonesia"

**Contoh yang akan dikenali sebagai sama:**

- "Bahasa Indonesia"
- "bahasa indonesia"
- "Bahasa-Indonesia"
- "BAHASA_INDONESIA"
- "Bahasa, Indonesia"

### Informasi Sekolah

- **ID Sekolah**: Kolom `idSchool` wajib ada di Excel
- **Nilai**: Harus diisi dengan "RWK001" (nilai tetap/paten)
- **Default**: Jika kosong, sistem otomatis menggunakan "RWK001"

### Kolom Khusus per Jenis Soal

#### Pilihan Ganda (PG)

- `opsiA` - Opsi jawaban A
- `opsiB` - Opsi jawaban B
- `opsiC` - Opsi jawaban C
- `opsiD` - Opsi jawaban D
- `opsiE` - Opsi jawaban E (opsional)
- `jawabanBenar` - Huruf jawaban benar (A, B, C, D, atau E)

#### Multi Jawaban (MULTI)

- `opsiA` - Opsi jawaban A
- `opsiB` - Opsi jawaban B
- `opsiC` - Opsi jawaban C
- `opsiD` - Opsi jawaban D
- `opsiE` - Opsi jawaban E (opsional)
- `jawabanBenar` - Jawaban benar dipisah koma (contoh: "A,C,D")

#### Mencocokkan (COCOK)

- `kiri1`, `kanan1` - Pasangan pertama (wajib)
- `kiri2`, `kanan2` - Pasangan kedua (opsional)
- `kiri3`, `kanan3` - Pasangan ketiga (opsional)
- `kiri4`, `kanan4` - Pasangan keempat (opsional)
- `kiri5`, `kanan5` - Pasangan kelima (opsional)
- `jawabanBenar` - Pasangan benar dipisah koma

#### Isian (ISIAN)

- `toleransiTypo` - Toleransi kesalahan pengetikan (angka, opsional)
- `jawabanBenar` - Jawaban yang benar (teks)

## Alur Data Import

```
File CSV (20 baris dengan namaUjian "UTS Matematika")
    ↓
TAHAP 1: Buat 20 Soal Ujian terpisah
    - Soal Ujian 1: "UTS Matematika" - Pertanyaan: "2+2=?"
    - Soal Ujian 2: "UTS Matematika" - Pertanyaan: "3+5=?"
    - ... (18 soal lainnya)
    ↓
TAHAP 2: Kaitkan ke 20 Bank Soal
    - Bank Soal 1 → Link ke Soal Ujian 1 + Data ATP/ACP
    - Bank Soal 2 → Link ke Soal Ujian 2 + Data ATP/ACP
    - ... (18 bank soal lainnya)
    ↓
HASIL: 1 Nama Ujian → 20 Soal Ujian → 20 Bank Soal
```

## Contoh Format CSV/Excel

### Pilihan Ganda (PG)

| namaUjian | pertanyaan | bobot | opsiA | opsiB | opsiC | opsiD | jawabanBenar | idSchool | tahunAjaran | namaTaksonomi |
| --------- | ---------- | ----- | ----- | ----- | ----- | ----- | ------------ | -------- | ----------- | ------------- |
| UTS IPA   | 2+2=?      | 10    | 3     | 4     | 5     | 6     | B            | RWK001   | 2025/2026   | C1            |
| UTS IPA   | 3+5=?      | 10    | 7     | 8     | 9     | 10    | B            | RWK001   | 2025/2026   | C1            |
| UTS IPA   | 10-4=?     | 10    | 5     | 6     | 7     | 8     | B            | RWK001   | 2025/2026   | C1            |

**Catatan:** Nama ujian yang sama ("UTS IPA") akan menghasilkan 3 Soal Ujian terpisah yang masing-masing dikaitkan ke Bank Soal.

### Multi Jawaban (MULTI)

| namaUjian | pertanyaan | bobot | opsiA | opsiB | opsiC    | opsiD | jawabanBenar | idSchool | namaTaksonomi |
| --------- | ---------- | ----- | ----- | ----- | -------- | ----- | ------------ | -------- | ------------- |
| UTS IPA   | Planet?    | 15    | Bumi  | Mars  | Matahari | Venus | A,B,D        | RWK001   | C2            |

### Mencocokkan (COCOK)

| namaUjian | pertanyaan | bobot | kiri1 | kanan1 | kiri2 | kanan2 | jawabanBenar       | idSchool | namaTaksonomi |
| --------- | ---------- | ----- | ----- | ------ | ----- | ------ | ------------------ | -------- | ------------- |
| UTS IPA   | Cocokkan   | 20    | H2O   | Air    | CO2   | Karbon | H2O-Air,CO2-Karbon | RWK001   | C3            |

### Isian (ISIAN)

| namaUjian | pertanyaan | bobot | jawabanBenar | toleransiTypo | idSchool | namaTaksonomi |
| --------- | ---------- | ----- | ------------ | ------------- | -------- | ------------- |
| UTS IPA   | Ibu kota?  | 5     | Jakarta      | 1             | RWK001   | C1            |

## Mapping Nama ke ID

⚠️ **PENTING**: Sistem menggunakan nama untuk mapping ke ID secara otomatis. Pastikan nama yang digunakan sesuai persis dengan data yang ada di sistem.

### Contoh Data yang Valid:

- **tahunAjaran**: "2025/2026", "2024/2025"
- **namaSemester**: "Ganjil", "Genap" (case-sensitive)
- **namaKelas**: "X", "XI", "XII" (case-sensitive)
- **namaMapel**: "Bahasa Indonesia", "Matematika", "Informatika" (harus sesuai persis)
- **namaElemen**: "Menyimak", "Membaca dan Memirsa" (harus sesuai persis)
- **namaKonsentrasiSekolah**: "Desain Komunikasi Visual" (harus sesuai persis)

### Validasi:

- Sistem akan memvalidasi bahwa nama yang dimasukkan bisa di-mapping ke ID yang ada
- Jika mapping gagal, data tersebut tidak akan diimport dan akan muncul error

## Tips dan Catatan

1. **Format File**: Gunakan format Excel (.xlsx) untuk hasil terbaik
2. **Header**: Pastikan baris pertama berisi header kolom yang sesuai
3. **Nama Exact Match**: Pastikan nama sesuai persis dengan data di sistem (case-sensitive)
4. **Validasi**: Sistem akan memvalidasi mapping nama ke ID
5. **Backup**: Selalu backup data sebelum import massal
6. **Testing**: Test dengan sedikit data terlebih dahulu
7. **Data Reference**: Lihat data yang sudah ada di sistem untuk memastikan nama yang digunakan benar

## Error Handling

Sistem akan menampilkan:

- Jumlah soal yang berhasil diimport
- Jumlah soal yang gagal diimport
- Pesan error spesifik jika ada masalah dengan format data

## Troubleshooting

### File tidak bisa dibaca

- **Gunakan format CSV (.csv)** untuk menghindari masalah corrupt
- Jika menggunakan Excel, pastikan format .xlsx atau .xls
- Periksa apakah file tidak corrupt
- CSV lebih kompatibel dan tidak mudah corrupt

### Import gagal

- **Tahap 1 (Soal Ujian) gagal:**

  - Periksa format header kolom
  - Pastikan kolom wajib terisi
  - Periksa format data (angka untuk bobot, dll)
  - Periksa mapping nama taksonomi dan konsentrasi keahlian

- **Tahap 2 (Bank Soal) gagal:**
  - Periksa mapping nama ke ID untuk semua field
  - Pastikan data ATP/ACP/Elemen valid

### Sebagian data tidak terimport

- Periksa log error di console browser
- Pastikan semua ID referensi (idMapel, idKelas, dll) valid dan exists di database
