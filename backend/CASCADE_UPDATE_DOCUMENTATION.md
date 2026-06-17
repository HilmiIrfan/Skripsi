# Cascade Update Documentation

## Overview

Fitur cascade update telah ditambahkan untuk memastikan konsistensi data dalam hierarki Elemen → ACP → ATP → BankSoal. Ketika terjadi perubahan nama, semua record yang terkait akan otomatis ikut ter-update:

- **Elemen Update**: Semua ACP, ATP, dan BankSoal yang terkait akan ter-update nama elemennya
- **ACP Update**: Semua ATP dan BankSoal yang terkait akan ter-update nama ACP-nya
- **ATP Update**: Semua BankSoal yang terkait akan ter-update nama ATP-nya

## What's Changed

### 1. AcpRepository.java

- **Added Method**: `findAcpByElemen(String elemenId, int size)`
  - Mencari semua record ACP yang terkait dengan elemen tertentu
- **Added Method**: `updateNamaElemenByElemenId(String elemenId, String newNamaElemen)`
  - Update nama elemen di semua record ACP yang terkait
  - Menambahkan audit trail dengan "System_Cascade_Update"

### 2. AtpRepository.java

- **Added Method**: `findAtpByElemen(String elemenId, int size)`
  - Mencari semua record ATP yang terkait dengan elemen tertentu
- **Added Method**: `updateNamaElemenByElemenId(String elemenId, String newNamaElemen)`

  - Update nama elemen di semua record ATP yang terkait
  - Menambahkan audit trail dengan "System_Cascade_Update"

- **Added Method**: `findAtpByAcp(String acpId, int size)`
  - Mencari semua record ATP yang terkait dengan ACP tertentu
- **Added Method**: `updateNamaAcpByAcpId(String acpId, String newNamaAcp)`
  - Update nama ACP di semua record ATP yang terkait
  - Menambahkan audit trail dengan "System_Cascade_Update"

### 3. ElemenService.java

- **Modified**: `updateElemen(String elemenId, ElemenRequest elemenRequest)`
  - Menambahkan pengecekan perubahan nama elemen
  - Melakukan cascade update ke ACP dan ATP jika nama berubah
  - Menambahkan error handling untuk cascade update
  - Menambahkan logging untuk monitoring

### 4. AcpService.java

- **Modified**: `updateAcp(String acpId, AcpRequest acpRequest)`
  - Menambahkan pengecekan perubahan nama ACP
  - Melakukan cascade update ke ATP dan BankSoal jika nama ACP berubah
  - Menambahkan error handling untuk cascade update
  - Menambahkan logging untuk monitoring

### 5. AtpService.java

- **Modified**: `updateAtp(String atpId, AtpRequest atpRequest)`
  - Menambahkan pengecekan perubahan nama ATP
  - Melakukan cascade update ke BankSoal jika nama ATP berubah
  - Menambahkan error handling untuk cascade update
  - Menambahkan logging untuk monitoring

### 6. BankSoalRepository.java

- **Added Method**: `findBankSoalByElemen(String elemenId, int size)`
  - Mencari semua record BankSoal yang terkait dengan elemen tertentu
- **Added Method**: `findBankSoalByAcp(String acpId, int size)`
  - Mencari semua record BankSoal yang terkait dengan ACP tertentu
- **Added Method**: `findBankSoalByAtp(String atpId, int size)`
  - Mencari semua record BankSoal yang terkait dengan ATP tertentu
- **Added Method**: `updateNamaElemenByElemenId(String elemenId, String newNamaElemen)`
  - Update nama elemen di semua record BankSoal yang terkait
  - Menambahkan audit trail dengan "System_Cascade_Update"
- **Added Method**: `updateNamaAcpByAcpId(String acpId, String newNamaAcp)`
  - Update nama ACP di semua record BankSoal yang terkait
  - Menambahkan audit trail dengan "System_Cascade_Update"
- **Added Method**: `updateNamaAtpByAtpId(String atpId, String newNamaAtp)`
  - Update nama ATP di semua record BankSoal yang terkait
  - Menambahkan audit trail dengan "System_Cascade_Update"

## How It Works

### Elemen Update Cascade

1. **User Updates Elemen**: Ketika user melakukan PUT request ke `/api/elemen/{elemenId}`
2. **Check Name Change**: System mengecek apakah nama elemen berubah
3. **Update Elemen**: Update record elemen di database
4. **Cascade Update**: Jika nama berubah, system akan:
   - Mencari semua ACP yang terkait dengan elemen tersebut
   - Update nama elemen di semua record ACP
   - Mencari semua ATP yang terkait dengan elemen tersebut
   - Update nama elemen di semua record ATP
   - Mencari semua BankSoal yang terkait dengan elemen tersebut
   - Update nama elemen di semua record BankSoal
5. **Audit Trail**: Menambahkan `updated_by: "System_Cascade_Update"` untuk tracking

### ACP Update Cascade

1. **User Updates ACP**: Ketika user melakukan PUT request ke `/api/acp/{acpId}`
2. **Check Name Change**: System mengecek apakah nama ACP berubah
3. **Update ACP**: Update record ACP di database
4. **Cascade Update**: Jika nama berubah, system akan:
   - Mencari semua ATP yang terkait dengan ACP tersebut
   - Update nama ACP di semua record ATP
   - Mencari semua BankSoal yang terkait dengan ACP tersebut
   - Update nama ACP di semua record BankSoal
5. **Audit Trail**: Menambahkan `updated_by: "System_Cascade_Update"` untuk tracking

### ATP Update Cascade

1. **User Updates ATP**: Ketika user melakukan PUT request ke `/api/atp/{atpId}`
2. **Check Name Change**: System mengecek apakah nama ATP berubah
3. **Update ATP**: Update record ATP di database
4. **Cascade Update**: Jika nama berubah, system akan:
   - Mencari semua BankSoal yang terkait dengan ATP tersebut
   - Update nama ATP di semua record BankSoal
5. **Audit Trail**: Menambahkan `updated_by: "System_Cascade_Update"` untuk tracking

## Error Handling

- Jika cascade update gagal, error akan di-log tetapi tidak akan menggagalkan update elemen utama
- System akan tetap mengembalikan response success untuk update elemen utama
- Error cascade update akan dicatat di console untuk monitoring

## Example Flow

```
PUT /api/elemen/elem-001
{
  "namaElemen": "Elemen Baru",
  ...
}

1. Update elemen "elem-001" dengan nama "Elemen Baru"
2. Cari semua ACP dengan idElemen = "elem-001"
3. Update nama elemen menjadi "Elemen Baru" di semua ACP tersebut
4. Cari semua ATP dengan idElemen = "elem-001"
5. Update nama elemen menjadi "Elemen Baru" di semua ATP tersebut
6. Cari semua BankSoal dengan idElemen = "elem-001"
7. Update nama elemen menjadi "Elemen Baru" di semua BankSoal tersebut
8. Return success response
```

### ACP Update Flow

```
PUT /api/acp/acp-001
{
  "namaAcp": "ACP Baru",
  ...
}

1. Update ACP "acp-001" dengan nama "ACP Baru"
2. Cari semua ATP dengan idAcp = "acp-001"
3. Update nama ACP menjadi "ACP Baru" di semua ATP tersebut
4. Cari semua BankSoal dengan idAcp = "acp-001"
5. Update nama ACP menjadi "ACP Baru" di semua BankSoal tersebut
6. Return success response
```

### ATP Update Flow

```
PUT /api/atp/atp-001
{
  "namaAtp": "ATP Baru",
  ...
}

1. Update ATP "atp-001" dengan nama "ATP Baru"
2. Cari semua BankSoal dengan idAtp = "atp-001"
3. Update nama ATP menjadi "ATP Baru" di semua BankSoal tersebut
4. Return success response
```

## Benefits

- **Data Consistency**: Semua data terkait selalu konsisten
- **Automatic**: Tidak perlu manual update data terkait
- **Audit Trail**: Perubahan tercatat untuk tracking
- **Error Resilient**: Cascade update error tidak mempengaruhi update utama

## Monitoring

- Check console logs untuk pesan cascade update
- Check HBase records dengan `updated_by: "System_Cascade_Update"` untuk audit trail
