# Test Case untuk Payload PG Edit SoalUjian

## Test Payload

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

## Expected Validation Results

### ✅ Basic Validations (PASS)

- ID SoalUjian: "9a73c90b-e758-433a-ac36-626912008c34" → Valid (not null/empty)
- Nama Ujian: "UTS MATEMATIKA" → Valid (not null/empty)
- Pertanyaan: "2+2=berapa" → Valid (not null/empty)
- Jenis Soal: "PG" → Valid (not null/empty, supported type)
- Bobot: "10" → Valid (parseable as Double)

### ✅ PG-Specific Validations (PASS)

- Opsi count: 4 → Valid (≥ 2 required)
- Opsi keys: ["A", "B", "C", "D"] → Valid (all non-empty)
- Opsi values: ["3", "4", "5", "6"] → Valid (all non-empty)
- JawabanBenar count: 1 → Valid (exactly 1 required for PG)
- JawabanBenar value: "C" → Valid (exists in opsi keys)

### ✅ Entity Reference Validations (PASS - assuming entities exist)

- User "USR002" → Should exist in database
- Taksonomi "0199c9e2-1321-4c41-b567-f69e62558f99" → Should exist
- School "RWK001" → Should exist
- KonsentrasiSekolah "KKS001" → Should exist

## Expected Processing Flow

### 1. Service Layer Processing

```
Input: PUT /soal-ujian/9a73c90b-e758-433a-ac36-626912008c34
↓
SoalUjianService.updateSoalUjian()
↓
✅ Basic validations pass
↓
✅ Entity lookups succeed
↓
✅ jenisSoal = "PG" → validatePertanyaanPG()
↓
✅ PG validations pass
↓
✅ Set opsi & jawabanBenar
✅ Clear pasangan & toleransiTypo
↓
SoalUjianRepository.updateWithCascade()
```

### 2. Repository Layer Processing

```
updateWithCascade()
↓
✅ validateSoalUjianForCascade() passes
↓
update() - Update main SoalUjian record
  ↓
  ✅ updateMainInfo() - Save basic fields
  ✅ updateRelationships() - Save entity refs
  ✅ savePilihanGanda() - Save opsi & jawaban as JSON
↓
cascadeUpdateToBankSoal() - Update related BankSoal records
  ↓
  ✅ Find BankSoal records with idSoalUjian
  ✅ Update each record with new SoalUjian data
```

## Expected JSON Serialization

### Opsi JSON:

```json
{ "A": "3", "B": "4", "C": "5", "D": "6" }
```

### JawabanBenar JSON:

```json
["C"]
```

## Expected Console Logs

### Service Layer:

```
Processing PG question type
Validating PG question...
Opsi received: {A=3, B=4, C=5, D=6}
JawabanBenar received: [C]
PG validation passed successfully
PG question processed successfully. Opsi: {A=3, B=4, C=5, D=6}, Jawaban: [C]
About to update soal with cascade. ID: 9a73c90b-e758-433a-ac36-626912008c34
Successfully updated SoalUjian with ID: 9a73c90b-e758-433a-ac36-626912008c34
```

### Repository Layer:

```
Saving PG question - rowKey: 9a73c90b-e758-433a-ac36-626912008c34
Opsi to save: {A=3, B=4, C=5, D=6}
JawabanBenar to save: [C]
Saved opsi JSON: {"A":"3","B":"4","C":"5","D":"6"}
Saved jawabanBenar JSON: ["C"]
Successfully saved PG question data
Successfully updated SoalUjian with cascade to BankSoal: 9a73c90b-e758-433a-ac36-626912008c34
```

## Expected Response

### Success Response (200 OK):

```json
{
  "success": true,
  "message": "Soal Ujian Updated Successfully"
}
```

### Location Header:

```
Location: /api/soal-ujian/9a73c90b-e758-433a-ac36-626912008c34
```

## Potential Error Scenarios

### If SoalUjian doesn't exist:

```json
{
  "success": false,
  "message": "Soal ujian dengan ID 9a73c90b-e758-433a-ac36-626912008c34 tidak ditemukan"
}
```

### If referenced entities don't exist:

```json
{
  "success": false,
  "message": "User dengan ID USR002 tidak ditemukan"
}
```

### If validation fails:

```json
{
  "success": false,
  "message": "Jawaban benar 'X' harus ada dalam opsi. Opsi yang tersedia: [A, B, C, D]"
}
```

## Database State After Success

### SoalUjian Table:

- ✅ Main fields updated (nama, pertanyaan, bobot, jenis)
- ✅ Opsi stored as JSON: `{"A":"3","B":"4","C":"5","D":"6"}`
- ✅ JawabanBenar stored as JSON: `["C"]`
- ✅ Relationships updated (user, taksonomi, school, konsentrasi)

### BankSoal Table (Cascade Update):

- ✅ All BankSoal records with idSoalUjian="9a73c90b..." updated
- ✅ Same data synchronized from SoalUjian
- ✅ Maintains referential integrity

## Manual Testing Steps

1. **Prerequisites**: Ensure entities exist in database:

   - User USR002
   - Taksonomi 0199c9e2-1321-4c41-b567-f69e62558f99
   - School RWK001
   - KonsentrasiSekolah KKS001
   - SoalUjian 9a73c90b-e758-433a-ac36-626912008c34

2. **Send Request**:

   ```bash
   PUT /api/soal-ujian/9a73c90b-e758-433a-ac36-626912008c34
   Content-Type: application/json

   [payload above]
   ```

3. **Verify Response**: Should be 200 OK with success message

4. **Check Logs**: Should see expected console output

5. **Verify Database**: Query SoalUjian and BankSoal tables to confirm updates
