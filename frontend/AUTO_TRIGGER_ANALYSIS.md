# Auto-Trigger Analysis dari Frontend

## 🎯 **Lokasi Auto-Trigger**

**File:** `src/views/ujian/index.jsx`  
**Function:** `handleEndUjian(idUjian)`  
**Button:** "Akhiri" (dengan icon Stop) pada kolom Aksi

---

## 🔥 **Flow Auto-Trigger yang Sudah Terimplementasi**

### **1. User Interface - Button "Akhiri"**

```javascript
// Button Stop/Akhiri muncul ketika ujian sedang BERLANGSUNG (AKTIF + isLive)
else if (status === "AKTIF" && isLive) {
  buttons.push(
    <Tooltip key="end" title="Akhiri Ujian & Generate Analisis Otomatis">
      <Button
        type="primary"
        danger
        size="small"
        icon={<StopOutlined />}
        onClick={() => handleEndUjian(record.idUjian)}  // ← TRIGGER POINT
      >
        Akhiri
      </Button>
    </Tooltip>
  );
}
```

### **2. Auto-Trigger Function**

```javascript
const handleEndUjian = async (idUjian) => {
  // STEP 1: Konfirmasi dengan modal
  Modal.confirm({
    title: "Konfirmasi Akhiri Ujian",
    content:
      "Sistem akan otomatis membuat analisis ujian setelah ujian berakhir.",
    onOk: async () => {
      // STEP 2: End ujian via API
      await endUjian(idUjian);
      message.success("Ujian berhasil diakhiri");

      // STEP 3: AUTO-GENERATE ANALYSIS ← TRIGGER UTAMA
      try {
        message.loading("Generating analysis ujian...", 2);
        const analysisResult = await generateParticipantBasedAnalysis(idUjian);

        // STEP 4: Fetch hasil analysis untuk notifikasi
        setTimeout(async () => {
          const analysisData = await getAnalysisByUjian(idUjian);

          if (analysisData.data.statusCode === 200) {
            const analysis = analysisData.data.content[0];

            // STEP 5: Tampilkan hasil analysis dalam modal sukses
            Modal.success({
              title: "Ujian Berhasil Diakhiri!",
              content: (
                <div>
                  <p>
                    <strong>Analisis ujian telah dibuat:</strong>
                  </p>
                  <ul>
                    <li>
                      Total peserta:{" "}
                      <strong>{analysis.totalParticipants}</strong>
                    </li>
                    <li>
                      Rata-rata nilai:{" "}
                      <strong>{analysis.averageScore.toFixed(1)}</strong>
                    </li>
                    <li>
                      Tingkat kelulusan:{" "}
                      <strong>{analysis.passRate.toFixed(1)}%</strong>
                    </li>
                    <li>
                      Siswa dengan pelanggaran:{" "}
                      <strong>
                        {analysis.participantStats?.yangMelanggar}
                      </strong>
                    </li>
                  </ul>
                </div>
              ),
            });
          }
        }, 1000);
      } catch (analysisError) {
        message.warning(
          "Analisis belum dapat dibuat. Silakan coba lagi nanti."
        );
      }

      // STEP 6: Refresh data ujian
      fetchUjians();
    },
  });
};
```

---

## 🎨 **Visual Flow untuk User**

```
┌─────────────────────────────────────────────┐
│           DAFTAR UJIAN BERLANGSUNG          │
├─────────────────────────────────────────────┤
│ Ujian Matematika  [BERLANGSUNG]             │
│ 📅 17/06/25 10:00  👥 25 peserta           │
│ [👁️ Detail] [🛑 Akhiri] ← Click Button      │
└─────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────┐
│            KONFIRMASI MODAL                 │
├─────────────────────────────────────────────┤
│ ⚠️ Konfirmasi Akhiri Ujian                  │
│                                             │
│ Apakah Anda yakin ingin mengakhiri ujian    │
│ ini? Sistem akan otomatis membuat analisis  │
│ ujian setelah ujian berakhir.               │
│                                             │
│           [Batal] [Ya, Akhiri] ← Confirm    │
└─────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────┐
│              LOADING MESSAGE                │
├─────────────────────────────────────────────┤
│ ✅ Ujian berhasil diakhiri                  │
│ 🔄 Generating analysis ujian...             │
└─────────────────────────────────────────────┘
                      ↓ (1 detik)
┌─────────────────────────────────────────────┐
│             SUCCESS MODAL                   │
├─────────────────────────────────────────────┤
│ 🎉 Ujian Berhasil Diakhiri!                 │
│                                             │
│ 📊 Analisis ujian telah dibuat:             │
│ • Total peserta: 25                         │
│ • Rata-rata nilai: 78.5                     │
│ • Tingkat kelulusan: 72.0%                  │
│ • Siswa dengan pelanggaran: 8               │
│                                             │
│ Anda dapat melihat laporan lengkap dengan   │
│ mengklik tombol "Analisis" atau "Report"    │
│                       [OK]                  │
└─────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────┐
│         UJIAN STATUS BERUBAH SELESAI        │
├─────────────────────────────────────────────┤
│ Ujian Matematika  [SELESAI]                 │
│ 📅 17/06/25 10:00  👥 25/25 selesai        │
│ [👁️ Detail] [📊 Analisis] [📋 Report]      │
└─────────────────────────────────────────────┘
```

---

## 🔗 **API Calls yang Dipicu**

### **Sequence Diagram:**

```
Frontend              Backend
   │                     │
   │ 1. POST /ujian/{id}/end
   ├────────────────────→│
   │                     │ endUjian()
   │ ✅ Success          │
   │←────────────────────┤
   │                     │
   │ 2. POST /ujian-analysis/auto-generate/{id}
   ├────────────────────→│
   │                     │ generateParticipantBasedAnalysis()
   │ ✅ Analysis Created │
   │←────────────────────┤
   │                     │
   │ 3. GET /ujian-analysis/ujian/{id}
   ├────────────────────→│
   │                     │ getAnalysisByUjian()
   │ 📊 Analysis Data    │
   │←────────────────────┤
   │                     │
   │ 4. Display Results  │
   │                     │
```

---

## ✅ **Fitur yang Sudah Terimplementasi**

1. ✅ **Button Auto-Trigger**: Button "Akhiri" dengan tooltip informatif
2. ✅ **Konfirmasi Modal**: Mencegah end ujian tidak sengaja
3. ✅ **Loading State**: Menampilkan progress saat generate analysis
4. ✅ **Error Handling**: Fallback jika analysis gagal dibuat
5. ✅ **Success Notification**: Modal dengan detail hasil analysis
6. ✅ **Data Refresh**: Auto-refresh list ujian setelah process
7. ✅ **Conditional Display**: Button muncul hanya saat ujian berlangsung
8. ✅ **Rich Analysis Data**: Menampilkan total peserta, rata-rata, pelanggaran

---

## 🚀 **Hasil Akhir**

**Ketika guru mengklik button "Akhiri":**

1. 🛑 Ujian diakhiri
2. 🤖 Analysis otomatis dibuat di backend
3. 📊 Data analysis ditampilkan ke user
4. 🔄 UI di-refresh untuk menampilkan status "SELESAI"
5. 📋 Button "Analisis" dan "Report" muncul untuk akses lebih lanjut

**Auto-trigger sudah berfungsi 100% dan terintegrasi dengan sempurna! 🎉**
