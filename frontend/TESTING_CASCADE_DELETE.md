# 🧪 Testing Guide: Cascade Delete Functionality

## 📋 Overview

Implementasi Cascade Delete telah berhasil diterapkan dengan hierarki:

```
Elemen → ACP → ATP
```

## ⚡ Cascade Delete Rules

### 1. **Hapus Elemen** 🔴

- ✅ **Menghapus**: Elemen + Semua ACP terkait + Semua ATP terkait
- 🎯 **Urutan**: ATP → ACP → Elemen (bottom-up)
- 📊 **Info**: Menampilkan jumlah ACP dan ATP yang akan dihapus

### 2. **Hapus ACP** 🟡

- ✅ **Menghapus**: ACP + Semua ATP terkait
- ❌ **Tidak Menghapus**: Elemen (tetap ada)
- 🎯 **Urutan**: ATP → ACP
- 📊 **Info**: Menampilkan jumlah ATP yang akan dihapus

### 3. **Hapus ATP** 🟢

- ✅ **Menghapus**: Hanya ATP
- ❌ **Tidak Menghapus**: Elemen dan ACP (tetap ada)
- 🎯 **Urutan**: ATP saja
- 📊 **Info**: Konfirmasi bahwa hanya ATP yang dihapus

## 🧪 Test Scenarios

### Test Case 1: Hapus Elemen

```
Given: 1 Elemen dengan 2 ACP dan 5 ATP terkait
When: User menghapus Elemen
Then:
  - Konfirmasi modal menampilkan "2 ACP dan 5 ATP akan dihapus"
  - Semua ATP (5) dihapus terlebih dahulu
  - Kemudian ACP (2) dihapus
  - Terakhir Elemen dihapus
  - Success message: "Berhasil menghapus Elemen 'X' beserta 2 ACP dan 5 ATP terkait"
```

### Test Case 2: Hapus ACP

```
Given: 1 ACP dengan 3 ATP terkait
When: User menghapus ACP
Then:
  - Konfirmasi modal menampilkan "3 ATP akan dihapus, Elemen tetap ada"
  - Semua ATP (3) dihapus terlebih dahulu
  - Kemudian ACP dihapus
  - Elemen tetap ada
  - Success message: "Berhasil menghapus ACP beserta 3 ATP terkait"
```

### Test Case 3: Hapus ATP

```
Given: 1 ATP terkait dengan 1 ACP dan 1 Elemen
When: User menghapus ATP
Then:
  - Konfirmasi modal menampilkan "Hanya ATP yang akan dihapus"
  - ATP dihapus
  - ACP dan Elemen tetap ada
  - Success message: "Berhasil menghapus ATP 'X'"
```

## 🔍 Debug Information

### Console Logs

Setiap operasi cascade delete akan menampilkan log:

```
🔍 Step 1: Mencari ACP terkait dengan Elemen ID: [ID]
📊 Found ACP to delete: [jumlah]
📊 Found ATP to delete: [jumlah]
🗑️ Deleting ATP: [nama]
🗑️ Deleting ACP: [nama]
🗑️ Deleting Elemen: [nama]
```

### Visual Indicators

- ⚠️ Modal konfirmasi dengan warna danger (merah)
- 📊 Loading indicator selama proses penghapusan
- ✅ Success message dengan durasi 5 detik
- 🔄 Auto-refresh table setelah delete berhasil

## 🛡️ Error Handling

- Jika ada error di tengah proses, operasi dihentikan
- Message error ditampilkan dengan detail
- Console log error untuk debugging
- Table tidak di-refresh jika ada error

## 🎯 Testing Steps

1. **Preparation**

   - Buat sample data: 1 Elemen → 2 ACP → 4 ATP
   - Pastikan data terhubung dengan benar

2. **Test Hapus ATP**

   - Pilih 1 ATP
   - Klik delete, verifikasi modal
   - Konfirmasi delete
   - Verifikasi hanya ATP yang hilang

3. **Test Hapus ACP**

   - Pilih 1 ACP yang masih punya ATP
   - Klik delete, verifikasi info cascade
   - Konfirmasi delete
   - Verifikasi ACP + ATP terkait hilang, Elemen tetap ada

4. **Test Hapus Elemen**
   - Pilih Elemen yang masih punya ACP + ATP
   - Klik delete, verifikasi info cascade
   - Konfirmasi delete
   - Verifikasi semua data terkait hilang

## ⚡ Performance Notes

- Operasi cascade menggunakan sequential delete (satu per satu)
- Loading indicator mencegah user melakukan aksi lain
- Console logs membantu monitor progress
- Error di tengah proses akan menghentikan cascade

## 🚀 Ready for Testing!

Implementasi sudah siap untuk testing. Silakan coba semua skenario di atas untuk memastikan cascade delete berfungsi dengan benar.
