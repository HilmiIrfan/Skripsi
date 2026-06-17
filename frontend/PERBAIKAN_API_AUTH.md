# Panduan Penggunaan API dan Auth yang Benar

## Masalah yang Ditemukan dan Diperbaiki

### 1. **Import Statement yang Salah**

❌ **Salah:**

```javascript
import { getParticipantReports } from "../../api/hasilUjian";
import { useAuth } from "../../contexts/AuthContext";
```

✅ **Benar:**

```javascript
import {
  getHasilByUjian,
  autoGenerateAndDownloadReport,
} from "@/api/hasilUjian";
import { getUjian } from "@/api/ujian";
import { useAuth } from "@/contexts/AuthContext";
```

### 2. **Pola Penggunaan Auth yang Benar**

❌ **Salah:** Menggunakan context yang tidak ada

```javascript
const { user } = useAuth(); // Context AuthContext tidak ada
```

✅ **Benar:** Menggunakan reqUserInfo atau AuthContext yang sudah dibuat

```javascript
// Opsi 1: Menggunakan reqUserInfo langsung
const [user, setUser] = useState(null);

useEffect(() => {
  const fetchUserInfo = async () => {
    try {
      const response = await reqUserInfo();
      setUser(response.data);
    } catch (error) {
      console.error("Error fetching user info:", error);
    }
  };
  fetchUserInfo();
}, []);

// Opsi 2: Menggunakan AuthContext (sudah dibuat)
import { useAuth } from "@/contexts/AuthContext";
const { user } = useAuth();
```

### 3. **Struktur Response API yang Benar**

❌ **Salah:**

```javascript
if (response.data && response.data.success) {
  const reports = response.data.data || [];
}
```

✅ **Benar:**

```javascript
if (response.data.statusCode === 200) {
  const reports = response.data.content || [];
}
```

### 4. **Penggunaan API yang Benar**

❌ **Salah:** Menggunakan API yang tidak ada

```javascript
const response = await getParticipantReports();
const response = await api.get("/ujian");
```

✅ **Benar:** Menggunakan API yang sudah ada

```javascript
// Untuk mengambil data ujian
const response = await getUjian();

// Untuk mengambil hasil ujian
const response = await getHasilByUjian(ujianId, true, 1000);

// Untuk download laporan
const response = await autoGenerateAndDownloadReport(idPeserta, idUjian);
```

### 5. **Error Handling yang Benar**

✅ **Benar:**

```javascript
try {
  const response = await getUjian();

  if (response.data.statusCode === 200) {
    const ujianList = response.data.content || [];
    // Process data
  } else {
    throw new Error("Gagal mengambil data ujian");
  }
} catch (error) {
  console.error("Error:", error);
  message.error("Gagal memuat data: " + error.message);
}
```

### 6. **Transformasi Data yang Benar**

Sesuaikan struktur data dengan response API yang sebenarnya:

```javascript
// Transform data dari API ke format yang dibutuhkan UI
const transformedData = hasilData.map((hasil) => ({
  idPeserta: hasil.idPeserta,
  namaPeserta: hasil.namaPeserta || hasil.idPeserta,
  idUjian: ujian.idUjian,
  namaUjian: ujian.namaUjian,
  mapel: ujian.mapel || "-",
  kelas: ujian.kelas || "-",
  persentase: hasil.persentase || 0,
  tanggalUjian: hasil.waktuMulai || ujian.waktuMulaiDijadwalkan,
  totalViolations: hasil.jumlahPelanggaran || 0,
  statusPengerjaan: hasil.statusPengerjaan,
  lulus: hasil.lulus,
}));
```

## Contoh Implementasi yang Benar

### File: LaporanNilaiSiswa.jsx

- ✅ Menggunakan `useAuth()` dari AuthContext yang sudah dibuat
- ✅ Menggunakan API yang sebenarnya ada (`getUjian`, `getHasilByUjian`)
- ✅ Structure response yang konsisten dengan `statusCode` dan `content`
- ✅ Error handling yang proper
- ✅ Transform data sesuai kebutuhan UI

### File: ReportNilaiSiswa.jsx

- ✅ Menggunakan API yang benar
- ✅ Fetch data dari multiple ujian jika tidak ada filter spesifik
- ✅ Filter by date range
- ✅ Calculate statistics dari data yang sebenarnya

## Pola Umum yang Harus Diikuti

1. **Import:** Selalu gunakan `@/api/` untuk import API functions
2. **Auth:** Gunakan `useAuth()` hook atau `reqUserInfo()` langsung
3. **Response:** Selalu check `response.data.statusCode === 200`
4. **Data:** Ambil data dari `response.data.content`
5. **Error:** Proper try-catch dengan message.error()
6. **Loading:** Set loading state untuk UX yang baik

## Files yang Sudah Diperbaiki

1. `src/views/laporan-nilai/LaporanNilaiSiswa.jsx`
2. `src/views/report-nilai/index.jsx`
3. `src/contexts/AuthContext.jsx` (dibuat baru)

Semua file ini sekarang mengikuti pola yang konsisten dengan file `ujian/index.jsx` dan `ujian-view/index.jsx` yang sudah benar.
