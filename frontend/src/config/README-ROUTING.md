# Dokumentasi Routing Ujian dan Analitik

## Perubahan yang Dilakukan

### 1. Penambahan Route Baru

**Frontend Routes (`routeMap.js`):**

- `/ujian-analysis` - Halaman daftar analisis ujian (ROLE_OPERATOR, ROLE_TEACHER)
- `/ujian-analysis/detail/:idUjian` - Detail analisis ujian tertentu (ROLE_OPERATOR, ROLE_TEACHER)
- `/integrasi-ujian` - Dashboard integrasi ujian, hasil, dan analitik (ROLE_OPERATOR, ROLE_TEACHER)
- `/ujian-history` - Riwayat ujian siswa (ROLE_STUDENT)

**Menu Configuration (`menuConfig.js`):**

- "Analisis Ujian" (`/ujian-analysis`) dengan icon `RadarChartOutlined`
- "Integrasi Dashboard" (`/integrasi-ujian`) dengan icon `AppstoreOutlined`
- "Riwayat Ujian" (`/ujian-history`) untuk siswa dengan icon `FileTextOutlined`

### 2. Komponen Baru yang Dibuat

#### A. UjianAnalysis (`views/ujian-analysis/index.jsx`)

- **Fungsi**: Menampilkan daftar analisis ujian dengan statistik ringkasan
- **Features**:
  - Tabel analisis dengan pagination
  - Statistik ringkasan (total analisis, pass rate, pelanggaran, dll)
  - Generate analisis baru
  - Navigasi ke detail analisis
- **API yang digunakan**:
  - `getAllAnalysis()` dari `/api/ujianAnalysis.js`
  - `generateAnalysis()` dari `/api/ujianAnalysis.js`

#### B. DetailAnalysis (`views/ujian-analysis/DetailAnalysis.jsx`)

- **Fungsi**: Menampilkan detail analisis ujian tertentu
- **Features**:
  - Statistik deskriptif ujian
  - Tabel pelanggaran (cheat detection)
  - Distribusi nilai peserta
  - Rekomendasi perbaikan
- **API yang digunakan**:
  - `getAnalysisByUjian(idUjian)` dari `/api/ujianAnalysis.js`
  - `getViolationsByUjian(idUjian)` dari `/api/cheatDetection.js`
  - `getHasilByUjian(idUjian, true)` dari `/api/hasilUjian.js`

#### C. IntegrasiUjianDashboard (`views/integrasi-ujian/IntegrasiUjianDashboard.jsx`)

- **Fungsi**: Dashboard terpadu untuk melihat integrasi ujian, hasil, dan analitik
- **Features**:
  - Pemilihan ujian dari daftar
  - Tampilan terintegrasi hasil, pelanggaran, dan analisis
  - Navigasi antar komponen terkait
- **API yang digunakan**:
  - `getUjian()` dari `/api/ujian.js`
  - `getHasilByUjian()` dari `/api/hasilUjian.js`
  - `getViolationsByUjian()` dari `/api/cheatDetection.js`
  - `getAnalysisByUjian()` dari `/api/ujianAnalysis.js`

### 3. Kompatibilitas API

#### Backend API Endpoints:

- `/api/ujian-analysis/**` - Managed by UjianAnalysisController
- `/api/hasil-ujian/**` - Managed by HasilUjianController
- `/api/cheat-detection/**` - Managed by CheatDetectionController
- `/api/ujian/**` - Managed by UjianController

#### Frontend API Services:

- `/api/ujianAnalysis.js` - Interface untuk ujian analysis API
- `/api/hasilUjian.js` - Interface untuk hasil ujian API
- `/api/cheatDetection.js` - Interface untuk cheat detection API
- `/api/ujian.js` - Interface untuk ujian API

### 4. Data Flow dan Integrasi

#### Flow Analisis Ujian:

1. **Ujian** → **UjianSession** → **HasilUjian** → **UjianAnalysis**
2. **CheatDetection** → terintegrasi dengan **HasilUjian** dan **UjianAnalysis**

#### Field Mapping Frontend-Backend:

```javascript
// UjianAnalysis Response dari Backend
{
  idAnalysis: string,
  idUjian: string,
  totalParticipants: number,
  averageScore: number,
  passRate: number,
  suspiciousSubmissions: number,
  // ... other fields
}

// Format untuk Frontend UI
{
  key: idAnalysis,
  ujianNama: ujian?.namaUjian || `Ujian ${idUjian}`,
  ujianId: idUjian,
  totalPeserta: totalParticipants || 0,
  avgScore: averageScore || 0,
  passRate: passRate || calculatedPassRate,
  totalViolations: suspiciousSubmissions || 0,
  // ... formatted fields
}
```

### 5. Role-Based Access Control

- **ROLE_STUDENT**: Hanya akses `/ujian-history` untuk melihat riwayat ujian sendiri
- **ROLE_TEACHER**: Akses ke semua halaman ujian dan analisis untuk kelas yang diajar
- **ROLE_OPERATOR**: Akses penuh ke semua halaman ujian dan analisis

### 6. Navigation Pattern

```
Dashboard → Ujian → Ujian Analysis → Detail Analysis
                 ↘ Integrasi Dashboard
                 ↘ Ujian History (untuk student)
```

### 7. URL Parameters

- `/ujian-analysis/detail/:idUjian` - Parameter `idUjian` digunakan untuk fetch data analysis ujian tertentu
- Route menggunakan React Router v6 dengan `useParams()` hook

### 8. Error Handling

- Loading states untuk semua komponen
- Fallback values untuk data yang kosong
- Try-catch blocks untuk API calls
- User-friendly error messages dengan Ant Design Alert components

### 9. Responsive Design

- Menggunakan Ant Design Grid system (Row, Col)
- Table dengan horizontal scroll untuk mobile
- Card layouts yang responsive
- Consistent spacing dan typography

### 10. Performance Optimizations

- React.lazy() untuk code splitting
- Pagination untuk large datasets
- Debounced search (jika diperlukan)
- Memoization untuk expensive calculations

## Penggunaan

### Akses Halaman Analisis:

1. Login sebagai teacher/operator
2. Navigasi ke menu "Analisis Ujian"
3. View list analisis atau generate baru
4. Klik detail untuk analisis mendalam

### Akses History (Siswa):

1. Login sebagai student
2. Navigasi ke menu "Riwayat Ujian"
3. View riwayat ujian yang pernah dikerjakan

### Akses Integrasi Dashboard:

1. Login sebagai teacher/operator
2. Navigasi ke menu "Integrasi Dashboard"
3. Pilih ujian untuk melihat data terintegrasi

## Testing

Pastikan untuk test:

1. Navigation antar halaman
2. API response handling
3. Role-based access
4. Error scenarios
5. Loading states
6. Data formatting consistency
