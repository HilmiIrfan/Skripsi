# SoalUjian Repository Methods - CASCADE vs NON-CASCADE

## Method Overview

| Method                | Cascade ke BankSoal | Digunakan Untuk      | Keterangan                                |
| --------------------- | ------------------- | -------------------- | ----------------------------------------- |
| `save()`              | ❌ TIDAK            | CREATE operation     | Hanya buat SoalUjian, tidak buat BankSoal |
| `saveWithCascade()`   | ✅ YA               | _Not used currently_ | Buat SoalUjian + BankSoal otomatis        |
| `update()`            | ❌ TIDAK            | _Not used currently_ | Hanya update SoalUjian                    |
| `updateWithCascade()` | ✅ YA               | UPDATE operation     | Update SoalUjian + cascade ke BankSoal    |
| `deleteById()`        | ❌ TIDAK            | DELETE operation     | Hanya hapus SoalUjian                     |
| `deleteWithCascade()` | ✅ YA               | _Not used currently_ | Hapus SoalUjian + BankSoal                |

## Current Implementation

### ✅ CREATE Operation (NO CASCADE)

```java
// SoalUjianService.createSoalUjian()
return soalUjianRepository.save(soal);  // NO CASCADE

// SoalUjianRepository.save()
public SoalUjian save(SoalUjian soalUjian) throws IOException {
    // Only saves SoalUjian record
    // NO BankSoal creation
    // NO cascade operations
    return soalUjian;
}
```

### ✅ UPDATE Operation (WITH CASCADE)

```java
// SoalUjianService.updateSoalUjian()
return soalUjianRepository.updateWithCascade(soalUjianId, updatedSoal);  // WITH CASCADE

// SoalUjianRepository.updateWithCascade()
public SoalUjian updateWithCascade(String soalUjianId, SoalUjian soalUjian) throws IOException {
    // Updates SoalUjian record
    // Updates all related BankSoal records
    // Full cascade synchronization
    return updatedSoalUjian;
}
```

### ✅ DELETE Operation (NO CASCADE)

```java
// SoalUjianService.deleteSoalUjianById()
soalUjianRepository.deleteById(soalUjianId);  // NO CASCADE

// SoalUjianRepository.deleteById()
public boolean deleteById(String soalUjianId) throws IOException {
    // Only deletes SoalUjian record
    // NO BankSoal deletion
    // NO cascade operations
    return true;
}
```

## Method Details

### 1. save() - NO CASCADE

**Purpose**: Create SoalUjian independently without creating BankSoal

**What it does**:

- ✅ Validates and saves SoalUjian data
- ✅ Saves main info (nama, pertanyaan, bobot, jenis)
- ✅ Saves relationships (user, taksonomi, school, konsentrasi)
- ✅ Saves question-specific data (opsi, pasangan, jawaban, toleransi)
- ❌ Does NOT create BankSoal entries
- ❌ Does NOT perform any cascade operations

**Logging**:

```
Saving SoalUjian WITHOUT cascade - ID: xxx
Successfully saved SoalUjian without cascade - ID: xxx
```

### 2. saveWithCascade() - WITH CASCADE

**Purpose**: Create SoalUjian and automatically create corresponding BankSoal

**What it does**:

- ✅ Calls `save()` to create SoalUjian
- ✅ Creates corresponding BankSoal entry with same data
- ✅ Links BankSoal to SoalUjian via idSoalUjian
- ✅ Handles cascade failures gracefully

**Logging**:

```
Successfully created SoalUjian with cascade to BankSoal: xxx
```

### 3. updateWithCascade() - WITH CASCADE

**Purpose**: Update SoalUjian and sync changes to all related BankSoal entries

**What it does**:

- ✅ Calls `update()` to update SoalUjian
- ✅ Finds all BankSoal entries referencing this SoalUjian
- ✅ Updates all found BankSoal entries with new data
- ✅ Maintains data consistency across tables

**Logging**:

```
Successfully updated SoalUjian with cascade to BankSoal: xxx
```

## Usage in Service Layer

### Current Service Implementation:

```java
public class SoalUjianService {

    // CREATE - NO CASCADE
    public SoalUjian createSoalUjian(SoalUjianRequest request) {
        // ... validation ...
        return soalUjianRepository.save(soal);  // ❌ NO CASCADE
    }

    // UPDATE - WITH CASCADE
    public SoalUjian updateSoalUjian(String id, SoalUjianRequest request) {
        // ... validation ...
        return soalUjianRepository.updateWithCascade(id, soal);  // ✅ CASCADE
    }

    // DELETE - NO CASCADE
    public void deleteSoalUjianById(String id) {
        soalUjianRepository.deleteById(id);  // ❌ NO CASCADE
    }
}
```

## Expected Behavior

### Create SoalUjian:

```
POST /api/soal-ujian
↓
Service: createSoalUjian()
↓
Repository: save() - NO CASCADE
↓
Result: Only SoalUjian created, no BankSoal
```

### Update SoalUjian:

```
PUT /api/soal-ujian/{id}
↓
Service: updateSoalUjian()
↓
Repository: updateWithCascade() - WITH CASCADE
↓
Result: SoalUjian updated + all BankSoal entries updated
```

### Delete SoalUjian:

```
DELETE /api/soal-ujian/{id}
↓
Service: deleteSoalUjianById()
↓
Repository: deleteById() - NO CASCADE
↓
Result: Only SoalUjian deleted, BankSoal remains
```

## Benefits of This Approach

### ✅ **Independent Creation**

- Create SoalUjian without side effects
- No unwanted BankSoal entries
- Full control over when to create BankSoal

### ✅ **Synchronized Updates**

- When SoalUjian changes, BankSoal stays in sync
- Maintains data consistency
- Automatic propagation of changes

### ✅ **Independent Deletion**

- Delete SoalUjian without affecting BankSoal
- Preserves BankSoal for historical/reference purposes
- Prevents accidental data loss

### ✅ **Flexibility**

- Developer can choose when to cascade
- Different behavior for different operations
- Predictable and controlled side effects

## Testing

### Test Create (No Cascade):

```java
// Create SoalUjian
SoalUjian created = soalUjianService.createSoalUjian(request);

// Verify: SoalUjian exists
assert soalUjianRepository.existsById(created.getId());

// Verify: NO BankSoal created automatically
List<BankSoal> bankSoals = bankSoalRepository.findBySoalUjianId(created.getId());
assert bankSoals.isEmpty(); // Should be empty
```

### Test Update (With Cascade):

```java
// Update SoalUjian
SoalUjian updated = soalUjianService.updateSoalUjian(id, request);

// Verify: SoalUjian updated
SoalUjian current = soalUjianRepository.findById(id);
assert current.getNamaUjian().equals(request.getNamaUjian());

// Verify: BankSoal entries also updated (if any exist)
List<BankSoal> bankSoals = bankSoalRepository.findBySoalUjianId(id);
for (BankSoal bankSoal : bankSoals) {
    assert bankSoal.getNamaUjian().equals(request.getNamaUjian());
}
```

## Summary

✅ **CREATE**: `save()` - Independent creation, no BankSoal cascade  
✅ **UPDATE**: `updateWithCascade()` - Full synchronization with BankSoal  
✅ **DELETE**: `deleteById()` - Independent deletion, no BankSoal cascade

This implementation provides the perfect balance of independence and consistency as requested.
