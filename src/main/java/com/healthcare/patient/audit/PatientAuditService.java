
package com.healthcare.patient.audit;

import com.healthcare.patient.dto.response.PatientAuditResponse;

import java.util.List;

public interface PatientAuditService {

    // Record patient activity
    void logActivity(
            Long patientId,
            String action,
            String description,
            String performedBy
    );

    // Get patient audit history
    List<PatientAuditResponse> getPatientAuditHistory(Long patientId);

    // Get audit history by action
    List<PatientAuditResponse> getAuditHistoryByAction(String action);
}
