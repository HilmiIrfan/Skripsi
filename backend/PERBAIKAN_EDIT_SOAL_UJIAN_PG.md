# Perbaikan Edit SoalUjian untuk Payload PG

## Masalah yang Diperbaiki

Berdasarkan payload yang diberikan:

```json
{
  "idSoalUjian": "9a73c90b-e758-433a-ac36-626912008c34",
  "namaUjian": "UTS MATEMATIKA",
  "pertanyaan": "2+2=berapa",
  "bobot": "10",
  "jenisSoal": "PG",
  "opsi": {
    "A": "3",
    "B": "4",
    "C": "5",
    "D": "6"
  },
  "jawabanBenar": ["C"],
  "pasangan": {},
  "idKonsentrasiSekolah": "KKS001",
  "idSchool": "RWK001",
  "idTaksonomi": "0199c9e2-1321-4c41-b567-f69e62558f99",
  "idUser": "USR002"
}
```

## Perbaikan yang Dibuat

### 1. Enhanced SoalUjianService.updateSoalUjian()

#### Validasi Input yang Diperkuat:

- ✅ Validasi parameter tidak null
- ✅ Validasi SoalUjian yang akan diupdate benar-benar ada
- ✅ Validasi field wajib (namaUjian, pertanyaan, jenisSoal, bobot)
- ✅ Validasi bobot adalah angka yang valid
- ✅ Validasi keberadaan entitas terkait (User, Taksonomi, School, dll)

#### Pemrosesan Jenis Soal yang Diperbaiki:

- ✅ Logging untuk debugging
- ✅ Pembersihan field yang tidak digunakan sesuai jenis soal
- ✅ Error message yang lebih informatif

### 2. Enhanced validatePertanyaanPG()

#### Validasi yang Diperkuat:

- ✅ Validasi opsi tidak null/empty
- ✅ Validasi minimal 2 opsi jawaban
- ✅ Validasi jawaban benar tidak null/empty
- ✅ Validasi tepat 1 jawaban benar untuk PG
- ✅ Validasi jawaban benar ada dalam opsi
- ✅ Validasi setiap kunci dan nilai opsi tidak kosong
- ✅ Logging detail untuk debugging

### 3. Enhanced Repository Layer

#### SoalUjianRepository.savePilihanGanda():

- ✅ Logging detail proses save
- ✅ Validasi data sebelum diserialisasi
- ✅ Error handling yang lebih baik
- ✅ Debug output untuk troubleshooting

#### Error Handling:

- ✅ Try-catch yang lebih komprehensif
- ✅ Error message yang informatif
- ✅ Stack trace untuk debugging

## Alur Pemrosesan Edit Soal PG

### 1. Request Validation

```
Input: SoalUjianRequest
↓
Validate: ID tidak null/kosong
↓
Validate: SoalUjian exists di database
↓
Validate: Fields wajib (nama, pertanyaan, jenis, bobot)
↓
Validate: Bobot adalah angka valid
↓
Validate: Entitas terkait exists (User, Taksonomi, etc.)
```

### 2. Question Type Processing

```
JenisSoal = "PG"
↓
validatePertanyaanPG():
  - Opsi tidak null/empty
  - Minimal 2 opsi
  - JawabanBenar tidak null/empty
  - Tepat 1 jawaban benar
  - Jawaban benar ada dalam opsi
  - Semua kunci/nilai opsi tidak kosong
↓
Set opsi & jawabanBenar
Clear pasangan & toleransiTypo (tidak digunakan PG)
```

### 3. Database Update

```
updateWithCascade():
↓
update() - Update SoalUjian di database
↓
savePilihanGanda() - Save opsi & jawaban sebagai JSON
↓
cascadeUpdateToBankSoal() - Update BankSoal terkait
```

## Logging dan Debugging

Untuk troubleshooting, sistem sekarang akan menampilkan:

### Service Layer Logs:

- "Processing PG question type"
- "PG question processed successfully. Opsi: {...}, Jawaban: [...]"
- "About to update soal with cascade. ID: xxx"
- "Successfully updated SoalUjian with ID: xxx"

### Repository Layer Logs:

- "Saving PG question - rowKey: xxx"
- "Opsi to save: {...}"
- "JawabanBenar to save: [...]"
- "Saved opsi JSON: {...}"
- "Saved jawabanBenar JSON: [...]"
- "Successfully saved PG question data"

### Validation Logs:

- "Validating PG question..."
- "Opsi received: {...}"
- "JawabanBenar received: [...]"
- "PG validation passed successfully"

## Test Case untuk Payload Anda

Payload yang Anda berikan sudah valid dan akan berhasil diproses:

✅ **namaUjian**: "UTS MATEMATIKA" - Valid  
✅ **pertanyaan**: "2+2=berapa" - Valid  
✅ **bobot**: "10" - Valid (angka)  
✅ **jenisSoal**: "PG" - Valid  
✅ **opsi**: {"A":"3","B":"4","C":"5","D":"6"} - Valid (4 opsi)  
✅ **jawabanBenar**: ["C"] - Valid (1 jawaban, ada dalam opsi)  
✅ **IDs**: Semua ID tidak kosong - Valid

## Expected Behavior

Ketika payload ini dikirim ke endpoint `PUT /soal-ujian/{id}`:

1. ✅ Validasi akan passed
2. ✅ Data akan tersimpan dengan benar
3. ✅ BankSoal terkait akan terupdate otomatis (cascade)
4. ✅ Response success akan dikirim
5. ✅ Log detail akan muncul di console

## Error Scenarios yang Ditangani

- ❌ ID soal tidak ada → "Soal ujian dengan ID xxx tidak ditemukan"
- ❌ Opsi kosong → "Opsi wajib diisi untuk soal Pilihan Ganda (PG)"
- ❌ Jawaban tidak dalam opsi → "Jawaban benar 'X' harus ada dalam opsi"
- ❌ Bobot bukan angka → "Bobot soal harus berupa angka yang valid"
- ❌ Field wajib kosong → Error message spesifik per field
