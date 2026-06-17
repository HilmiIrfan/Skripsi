--- Backend Fix Instructions ---

The JSON deserialization errors are caused by complex Map<String, Object> fields in HasilUjian model:

- Map<String, Object> metadata
- Map<String, Object> securityFlags
- Map<String, Double> skorPerSoal (also causing String to Double cast issues)

SOLUTION: Replace these complex Map fields with simple primitive fields that align with other model patterns.

## Step 1: Update HasilUjian.java

Replace the problematic fields:

FROM (lines ~85-95):

```java
// Security & Integrity
private String resultHash;
private Boolean isVerified;
private String verifiedBy;
private Instant verificationTime;
private String securityStatus; // CLEAN, FLAGGED, SUSPICIOUS
private Map<String, Object> securityFlags;

// Quality Assurance
private Boolean hasAppeal;
private String appealReason;
private String appealStatus; // PENDING, APPROVED, REJECTED
private Instant appealSubmittedAt;
private String appealReviewedBy;
private Map<String, Object> appealData;

// Metadata & Relations
private Map<String, Object> metadata;
```

TO:

```java
// Security & Integrity
private String resultHash;
private Boolean isVerified;
private String verifiedBy;
private Instant verificationTime;
private String securityStatus; // CLEAN, FLAGGED, SUSPICIOUS
private String securityNotes; // Simple string instead of Map
private Integer securityViolationCount;

// Quality Assurance
private Boolean hasAppeal;
private String appealReason;
private String appealStatus; // PENDING, APPROVED, REJECTED
private Instant appealSubmittedAt;
private String appealReviewedBy;
private String appealNotes; // Simple string instead of Map

// Additional simple fields instead of complex metadata
private String sessionNotes;
private String workingNotes;
private String analysisNotes;
```

## Step 2: Update Map<String, Double> skorPerSoal field

FROM:

```java
private Map<String, Double> skorPerSoal; // idBankSoal -> skor
```

TO:

```java
private String skorPerSoalJson; // Store as simple JSON string, don't deserialize
```

## Step 3: Add Simple Getter/Setter Methods

Add these methods to replace the complex Map operations:

```java
// Security fields
public String getSecurityNotes() { return securityNotes; }
public void setSecurityNotes(String securityNotes) { this.securityNotes = securityNotes; }

public Integer getSecurityViolationCount() { return securityViolationCount; }
public void setSecurityViolationCount(Integer securityViolationCount) { this.securityViolationCount = securityViolationCount; }

// Appeal fields
public String getAppealNotes() { return appealNotes; }
public void setAppealNotes(String appealNotes) { this.appealNotes = appealNotes; }

// Session fields
public String getSessionNotes() { return sessionNotes; }
public void setSessionNotes(String sessionNotes) { this.sessionNotes = sessionNotes; }

public String getWorkingNotes() { return workingNotes; }
public void setWorkingNotes(String workingNotes) { this.workingNotes = workingNotes; }

public String getAnalysisNotes() { return analysisNotes; }
public void setAnalysisNotes(String analysisNotes) { this.analysisNotes = analysisNotes; }

// Score per question as JSON string
public String getSkorPerSoalJson() { return skorPerSoalJson; }
public void setSkorPerSoalJson(String skorPerSoalJson) { this.skorPerSoalJson = skorPerSoalJson; }
```

## Step 4: Update methods that use the old Map fields

Remove or update methods like:

- addCheatDetection() method that uses metadata.get("violations")
- updateSecurityStatusFromViolations() method
- syncWithSession() method that puts data into metadata

Replace with simple field assignments:

```java
public void addViolation(String violationType, String severity) {
    if (this.securityViolationCount == null) {
        this.securityViolationCount = 0;
    }
    this.securityViolationCount++;

    String currentNotes = this.securityNotes != null ? this.securityNotes : "";
    this.securityNotes = currentNotes + "; " + violationType + "(" + severity + ")";

    updateSecurityStatus();
}

private void updateSecurityStatus() {
    if (this.securityViolationCount == null || this.securityViolationCount == 0) {
        this.securityStatus = "CLEAN";
    } else if (this.securityViolationCount > 3) {
        this.securityStatus = "FLAGGED";
    } else if (this.securityViolationCount > 1) {
        this.securityStatus = "SUSPICIOUS";
    } else {
        this.securityStatus = "MONITORED";
    }
}
```

## Step 5: Update Constructor

Update the constructor to initialize simple fields instead of Maps:

FROM:

```java
this.securityFlags = new HashMap<>();
this.appealData = new HashMap<>();
this.metadata = new HashMap<>();
```

TO:

```java
this.securityNotes = "";
this.appealNotes = "";
this.sessionNotes = "";
this.workingNotes = "";
this.analysisNotes = "";
this.securityViolationCount = 0;
```

## Benefits:

1. ✅ No more JSON deserialization errors
2. ✅ Simpler data structure aligned with other models
3. ✅ Better performance (no complex Map serialization)
4. ✅ Easier to query and index in database
5. ✅ Frontend gets predictable data types

## Frontend Impact:

The frontend changes I already made will handle this gracefully by:

- Setting violation counts to 0 when metadata is not available
- Using simple fallback values
- Not relying on complex nested metadata objects

This approach follows the same pattern as other models in the codebase that use simple primitive fields instead of complex Map structures.
