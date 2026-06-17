# Fitur Resume Ujian - Mengatasi Masalah Laptop Mati/Putus Koneksi

## Masalah yang Dipecahkan

Sebelumnya, jika murid keluar dari web ujian (karena laptop mati, browser crash, atau masalah teknis lainnya), mereka tidak bisa masuk kembali ke ujian yang sama karena sistem akan menolak dengan pesan "Peserta sudah memiliki session aktif untuk ujian ini".

## Solusi yang Diimplementasikan

### 1. Modifikasi UjianSessionService.startSession()

**File**: `src/main/java/com/doyatama/university/service/UjianSessionService.java`

**Perubahan**:

- Sebelumnya akan langsung reject jika ada existing session
- Sekarang akan **resume session** jika:
  - Session belum di-submit (`!existingSession.getIsSubmitted()`)
  - Waktu ujian masih tersisa (`isSessionStillValid(existingSession)`)

**Fitur Resume**:

- Update metadata dengan informasi resume
- Hitung ulang waktu yang tersisa
- Track berapa kali session di-resume
- Return session yang sudah ada dengan data terkini

### 2. Modifikasi UjianSessionService.validateCanStart()

**Perubahan**:

- Jika ada existing session yang valid, return `canStart: true` dengan info resume
- Berikan informasi lengkap untuk frontend: timeRemaining, currentSoalIndex, answeredQuestions
- Bedakan antara session expired dan session yang bisa dilanjutkan

### 3. Endpoint Baru: /api/ujian-session/resume-or-start

**File**: `src/main/java/com/doyatama/university/controller/UjianSessionController.java`

**Endpoint**: `POST /api/ujian-session/resume-or-start`

**Fitur**:

- Otomatis detect apakah perlu resume atau start baru
- Response yang jelas apakah session di-resume atau baru dibuat
- Include semua data yang dibutuhkan frontend untuk continue ujian

### 4. Helper Method: isSessionStillValid()

**Fungsi**:

- Cek waktu tersisa dengan `calculateActualTimeRemaining()`
- Validasi ujian belum melewati waktu selesai otomatis
- Handle error dengan graceful fallback

## API Response Examples

### Resume Successful

```json
{
  "statusCode": 200,
  "message": "Ujian berhasil dilanjutkan dari session sebelumnya",
  "content": {
    "sessionId": "sess-12345",
    "idSession": "uuid-12345",
    "timeRemaining": 1800,
    "currentSoalIndex": 5,
    "totalQuestions": 20,
    "attemptNumber": 1,
    "startTime": "2024-01-01T10:00:00Z",
    "isResumed": true,
    "answers": {...},
    "answeredQuestions": 5
  }
}
```

### New Session Started

```json
{
  "statusCode": 200,
  "message": "Session ujian baru berhasil dimulai",
  "content": {
    "sessionId": "sess-67890",
    "isResumed": false,
    ...
  }
}
```

### Validation Response (Can Resume)

```json
{
  "success": true,
  "message": "Validasi berhasil",
  "data": {
    "canStart": true,
    "reason": "Dapat melanjutkan ujian yang sudah dimulai",
    "hasExistingSession": true,
    "existingSessionId": "sess-12345",
    "timeRemaining": 1800,
    "currentSoalIndex": 5,
    "answeredQuestions": 5
  }
}
```

## Skenario Penggunaan

### 1. Laptop Mati Saat Ujian

1. Murid sedang mengerjakan ujian
2. Laptop mati/battery habis
3. Murid nyalakan laptop lagi, buka browser
4. Masuk ke sistem ujian dengan kredensial sama
5. **✅ SEKARANG BISA**: Sistem detect ada session aktif dan allow resume
6. Ujian dilanjutkan dari soal terakhir dengan waktu yang tersisa

### 2. Browser Crash

1. Murid mengerjakan ujian di Chrome
2. Browser crash/hang
3. Murid buka browser baru
4. Login ke sistem
5. **✅ SEKARANG BISA**: Resume ujian dari progress terakhir

### 3. Koneksi Internet Terputus

1. Murid mengerjakan ujian
2. Koneksi internet putus sementara
3. Koneksi kembali normal
4. Refresh halaman atau login ulang
5. **✅ SEKARANG BISA**: Continue ujian tanpa kehilangan progress

## Keamanan & Validasi

### Validasi yang Dipertahankan:

- ✅ Hanya peserta yang sama yang bisa resume session-nya
- ✅ Waktu ujian harus masih tersisa
- ✅ Ujian belum di-submit sebelumnya
- ✅ Ujian masih dalam status aktif
- ✅ Belum melewati batas waktu selesai otomatis

### Tracking & Monitoring:

- ✅ Log setiap kali session di-resume
- ✅ Track jumlah resume dalam metadata
- ✅ Update waktu keep-alive saat resume
- ✅ Recalculate waktu tersisa secara akurat

## Backwards Compatibility

- ✅ Endpoint lama `/api/ujian-session/start` masih berfungsi
- ✅ Logic yang ada tidak berubah untuk session baru
- ✅ Hanya menambah kemampuan resume, tidak mengubah behavior existing

## Rekomendasi Frontend

### 1. Gunakan Endpoint Baru

```javascript
// Gunakan ini untuk start/resume otomatis
POST / api / ujian - session / resume - or - start;
```

### 2. Handle Resume Response

```javascript
if (response.content.isResumed) {
  // Show pesan "Melanjutkan ujian dari sebelumnya"
  // Load jawaban yang sudah ada
  // Set current soal index
} else {
  // Fresh start
  // Initialize kosong
}
```

### 3. Validasi Sebelum Start

```javascript
// Cek dulu apakah bisa start/resume
GET / api / ujian - session / validate - start / { idUjian } / { idPeserta };

if (response.data.hasExistingSession) {
  // Show dialog: "Anda memiliki ujian yang belum selesai. Lanjutkan?"
}
```

## Testing Scenarios

1. **Normal Start**: Murid start ujian pertama kali ✅
2. **Resume Valid**: Murid keluar lalu masuk lagi dalam waktu ✅
3. **Resume Expired**: Murid coba masuk setelah waktu habis ❌
4. **Resume Submitted**: Murid coba masuk setelah submit ❌
5. **Multiple Resume**: Murid keluar-masuk berkali-kali ✅

## Security Note

⚠️ **PENTING**: Pastikan frontend tidak allow multiple tab/browser untuk ujian yang sama untuk menghindari cheating. Resume feature ini untuk recovery purposes only.
